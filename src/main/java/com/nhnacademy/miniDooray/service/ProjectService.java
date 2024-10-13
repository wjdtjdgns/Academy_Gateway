package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectPageResponse;
import com.nhnacademy.miniDooray.dto.ProjectRegisterDto;
import com.nhnacademy.miniDooray.util.CookieUtil;
import com.nhnacademy.miniDooray.util.RestApiClient;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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

    public ProjectDto createProject(HttpServletRequest request, ProjectRegisterDto projectDto) {
        String userId = CookieUtil.getUserIdFromSession(request, redisTemplate);
        if (userId == null) {
            throw new RuntimeException("Unauthorized: User is not logged in");
        }
        try {
            String url = "http://localhost:8082/projects";

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.set("Content-Type", "application/json");

            ResponseEntity<ProjectDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    projectDto,
                    headers,
                    ProjectDto.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                if (response.getStatusCode().value() == 401) {
                    throw new RuntimeException("Unauthorized: User is not logged in");
                } else if (response.getStatusCode().value() == 400) {
                    throw new RuntimeException("Bad Request: Please check the required fields");
                } else if (response.getStatusCode().value() == 409) {
                    throw new RuntimeException("Conflict: Project name already exists");
                } else {
                    throw new RuntimeException("Failed to create project");
                }
            }

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create project", e);
        }
    }
}
