package com.nhnacademy.miniDooray.interceptor;

import com.nhnacademy.miniDooray.dto.MemberDto;
import com.nhnacademy.miniDooray.util.CookieUtil;
import com.nhnacademy.miniDooray.util.RestApiClient;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class MemberValidationInterceptor implements HandlerInterceptor {

    private final RestApiClient restApiClient;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = CookieUtil.getUserIdFromSession(request, redisTemplate);

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized: Missing X-USER-ID header");
            return false;
        }

        String url = "http://localhost:8081/members/" + userId;
        ResponseEntity<MemberDto> apiResponse = restApiClient.sendGetRequest(url, MemberDto.class);

        if (!apiResponse.getStatusCode().is2xxSuccessful()) {
            switch (apiResponse.getStatusCode().value()) {
                case 400:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request: Invalid userId format");
                    return false;
                case 404:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found: User does not exist");
                    return false;
                default:
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to validate user");
                    return false;
            }
        }

        request.setAttribute("validatedUserId", userId);
        return true;
    }
}
