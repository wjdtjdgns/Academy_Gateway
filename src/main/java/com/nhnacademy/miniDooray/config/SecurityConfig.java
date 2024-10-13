package com.nhnacademy.miniDooray.config;

import com.nhnacademy.miniDooray.filter.UserAuthenticationFilter;
import com.nhnacademy.miniDooray.handler.CustomLoginSuccessHandler;
import com.nhnacademy.miniDooray.handler.CustomLogoutSuccessHandler;
import com.nhnacademy.miniDooray.provider.CustomAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(customAuthenticationProvider)
                .authorizeHttpRequests(authorizeRequests ->
                                authorizeRequests
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers(HttpMethod.GET, "/members/register").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/members/register").permitAll()
                                        .anyRequest().authenticated()
//                                      .anyRequest().permitAll()
                );

        http.csrf(AbstractHttpConfigurer::disable);

        http.formLogin(formLogin ->
                formLogin.loginPage("/login")
                        .usernameParameter("id")
                        .passwordParameter("password")
                        .loginProcessingUrl("/login")
                        .successHandler(new CustomLoginSuccessHandler(redisTemplate))
        );

        http.logout(logout ->
                logout.logoutUrl("/logout")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler(redisTemplate, new DefaultRedirectStrategy()))
        );

        http.addFilterBefore(new UserAuthenticationFilter(redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
