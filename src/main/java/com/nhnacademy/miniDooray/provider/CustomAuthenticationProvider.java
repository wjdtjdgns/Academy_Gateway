package com.nhnacademy.miniDooray.provider;

import com.nhnacademy.miniDooray.dto.LoginRequest;
import com.nhnacademy.miniDooray.dto.MemberDto;
import com.nhnacademy.miniDooray.util.RestApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collections;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private RestApiClient restApiClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String id = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        try {
            String url = "http://localhost:8081/member/" + id;
            ResponseEntity<MemberDto> response = restApiClient.sendGetRequest(url, MemberDto.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                MemberDto member = response.getBody();
                if (passwordEncoder.matches(rawPassword, member.getPassword())) {
                    List<GrantedAuthority> authorities = Collections.emptyList();
                    return new UsernamePasswordAuthenticationToken(id, rawPassword, authorities);
                } else {
                    throw new BadCredentialsException("Invalid credentials");
                }
            } else {
                throw new BadCredentialsException("Member not found");
            }
        } catch (HttpStatusCodeException e) {
            throw new BadCredentialsException("Authentication failed", e);
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}