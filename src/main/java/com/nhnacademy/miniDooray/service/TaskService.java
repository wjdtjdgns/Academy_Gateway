package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.MilestoneDto;
import com.nhnacademy.miniDooray.dto.TaskDto;
import com.nhnacademy.miniDooray.dto.TaskRegisterDto;
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

    public TaskDto createTask(Long projectId, String userId, String title, String content, Long milestoneId, List<Long> tagIds) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tasks";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            TaskRegisterDto taskRegisterDto = new TaskRegisterDto();
            taskRegisterDto.setTitle(title);
            taskRegisterDto.setContent(content);
            taskRegisterDto.setMilestoneId(milestoneId);

            ResponseEntity<TaskDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    taskRegisterDto,
                    headers,
                    TaskDto.class
            );

            // #TODO tag update

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tasks", e);
        }
    }

    public TaskDto getTask(Long projectId, Long taskId, String userId) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tasks/" + taskId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<TaskDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    TaskDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tasks", e);
        }
    }
}
