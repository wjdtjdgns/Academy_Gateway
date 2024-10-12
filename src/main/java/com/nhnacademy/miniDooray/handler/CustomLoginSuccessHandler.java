package com.nhnacademy.miniDooray.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final RedisTemplate<String, Object> redisTemplate;

    public static final String SESSION_PREFIX = "Session:";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String id = authentication.getName();
        String sessionId = UUID.randomUUID().toString();
        redisTemplate.opsForHash().put(SESSION_PREFIX, sessionId, id);
        redisTemplate.expire(SESSION_PREFIX, 3, TimeUnit.DAYS);

        Cookie cookie = new Cookie("SESSION", sessionId);
        cookie.setMaxAge(259200);
        cookie.setPath("/");
        response.addCookie(cookie);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
