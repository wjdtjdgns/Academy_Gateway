package com.nhnacademy.miniDooray.exception;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
@Controller
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String REDIRECT_COUNT = "redirectCount";
    private static final int MAX_REDIRECT_COUNT = 3;

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        int redirectCount = getRedirectCountFromCookies(request);

        if (redirectCount >= MAX_REDIRECT_COUNT) {
            model.addAttribute("errorMessage", "리다이렉트 한도가 초과되었습니다. 문제가 지속될 경우 관리자에게 문의하세요.");
            return "maxRedirectError";
        }

        setRedirectCountCookie(response, redirectCount + 1);
        model.addAttribute("errorMessage", "서버에 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
        return "error";
    }

    private int getRedirectCountFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(cookie -> REDIRECT_COUNT.equals(cookie.getName()))
                    .findFirst()
                    .map(cookie -> Integer.parseInt(cookie.getValue()))
                    .orElse(0);
        }
        return 0;
    }

    private void setRedirectCountCookie(HttpServletResponse response, int count) {
        Cookie cookie = new Cookie(REDIRECT_COUNT, String.valueOf(count));
        cookie.setMaxAge(60);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

}
