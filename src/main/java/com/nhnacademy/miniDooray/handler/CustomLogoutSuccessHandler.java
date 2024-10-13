package com.nhnacademy.miniDooray.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.Objects;

import static com.nhnacademy.miniDooray.handler.CustomLoginSuccessHandler.SESSION_PREFIX;

@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    private final RedisTemplate<String, Object> sessionRedisTemplate;
    private final RedirectStrategy redirectStrategy;
    private static final String SESSION_COOKIE_NAME = "SESSION";

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (SESSION_COOKIE_NAME.equals(cookie.getName())) {
                    String sessionId = cookie.getValue();
                    if (Objects.nonNull(sessionId)) {
                        sessionRedisTemplate.opsForHash().delete(SESSION_PREFIX, sessionId);
                        Cookie deleteCookie = new Cookie(SESSION_COOKIE_NAME, null);
                        deleteCookie.setMaxAge(0);
                        deleteCookie.setPath("/");
                        response.addCookie(deleteCookie);
                    }
                    break;
                }
            }
        }
        redirectStrategy.sendRedirect(request, response, "/login");
    }
}