package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.dto.CommentRegisterDto;
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
public class CommentService {
    private final RestApiClient restApiClient;

    public List<CommentDto> getCommentByTaskId(Long projectId, Long taskId, String userId) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tasks/" + taskId + "/comments";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<List<CommentDto>> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    (Class<List<CommentDto>>) (Class<?>) List.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tasks", e);
        }
    }

    public CommentDto createComment(Long projectId, Long taskId, String userId, CommentRegisterDto commentRegisterDto) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tasks/" + taskId + "/comments";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<CommentDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    commentRegisterDto,
                    headers,
                    CommentDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tasks", e);
        }
    }
}
