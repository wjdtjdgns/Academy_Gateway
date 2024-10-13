package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.dto.TaskDto;
import com.nhnacademy.miniDooray.util.RestApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final RestApiClient restApiClient;

    public List<TaskDto> getTasks(Long projectId, String userId) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tasks";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<List<TaskDto>> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    (Class<List<TaskDto>>) (Class<?>) List.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tasks", e);
        }
    }
}
