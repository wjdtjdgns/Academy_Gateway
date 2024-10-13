package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectPageResponse;
import com.nhnacademy.miniDooray.util.CookieUtil;
import com.nhnacademy.miniDooray.util.RestApiClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final RestApiClient restApiClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public ProjectService(RestApiClient restApiClient, RedisTemplate<String, Object> redisTemplate) {
        this.restApiClient = restApiClient;
        this.redisTemplate = redisTemplate;
    }

    public List<ProjectDto> getProjects(HttpServletRequest request, int page, int size) {
        try {
            String userId = CookieUtil.getUserIdFromSession(request, redisTemplate);
            if (userId == null) {
                throw new RuntimeException("Unauthorized: User is not logged in");
            }

            String url = "http://localhost:8082/projects?page=" + page + "&size=" + size;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<ProjectPageResponse> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    ProjectPageResponse.class
            );

            List<ProjectDto> projects = response.getBody().getContent();
            return projects;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch projects", e);
        }
    }
}
