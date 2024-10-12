package com.nhnacademy.miniDooray.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static com.nhnacademy.miniDooray.handler.CustomLoginSuccessHandler.SESSION_PREFIX;

@RequiredArgsConstructor
public class UserAuthenticationFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String SESSION_COOKIE_NAME = "SESSION";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    String key = SESSION_PREFIX + sessionId;
                    Object userId = redisTemplate.opsForHash().get(key, sessionId);

                    if (Objects.nonNull(userId)) {
                        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    break;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}