package com.nhnacademy.miniDooray.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;

import static com.nhnacademy.miniDooray.handler.CustomLoginSuccessHandler.SESSION_PREFIX;

public class CookieUtil {

    public static String getUserIdFromSession(HttpServletRequest request, RedisTemplate<String, Object> redisTemplate) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION".equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    return (String) redisTemplate.opsForHash().get(SESSION_PREFIX, sessionId);
                }
            }
        }
        return null;
    }
}
