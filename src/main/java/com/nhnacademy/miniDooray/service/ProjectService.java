package com.nhnacademy.miniDooray.service;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.util.RestApiClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final RestApiClient restApiClient;
    private final MemberService memberService;

    public List<ProjectDto> getProjects(HttpServletRequest request, int page, int size) {
        String userId = (String) request.getAttribute("validatedUserId");
        try {

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

    public ProjectDto getProjectById(HttpServletRequest request, Long projectId) {
        String userId = (String) request.getAttribute("validatedUserId");
        try {

            String url = "http://localhost:8082/projects/" + projectId;
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<ProjectDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    ProjectDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch projects", e);
        }
    }

    public ProjectDto createProject(HttpServletRequest request, ProjectRegisterDto projectDto) {
        String userId = (String) request.getAttribute("validatedUserId");

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

    public List<MemberInfoDto> lookupMembers(List<String> memberIds, String userId) {
        try {
            String url = "http://localhost:8081/members/lookup";

            MemberRequest memberRequest = new MemberRequest(memberIds);

            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.set("Content-Type", "application/json");

            ResponseEntity<List<MemberInfoDto>> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    memberRequest,
                    headers,
                    (Class<List<MemberInfoDto>>) (Class<?>) List.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                // #TODO 오류처리
                throw new IllegalStateException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to lookup members", e);
        }
    }

    public List<TagDto> getProjectTags(Long projectId, String userId) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tag";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<List<TagDto>> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    (Class<List<TagDto>>) (Class<?>) List.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get tags", e);
        }
    }

    public List<MilestoneDto> getProjectMilestones(Long projectId, String userId) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/milestone";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            ResponseEntity<List<MilestoneDto>> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.GET,
                    null,
                    headers,
                    (Class<List<MilestoneDto>>) (Class<?>) List.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get milestones", e);
        }
    }

    public ProjectDto addProjectMember(Long projectId, String userId, List<String> memberIds) {
        try {
            if (!memberService.isExistMembers(userId, memberIds)) {
                throw new RuntimeException("사용자가 존재하지 않습니다");
            }

            String url = "http://localhost:8082/projects/" + projectId + "/members";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.setContentType(MediaType.APPLICATION_JSON);

            MemberRequest memberRequestDto = new MemberRequest(memberIds);

            ResponseEntity<ProjectDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    memberRequestDto,
                    headers,
                    ProjectDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Project member", e);
        }
    }

    public TagDto addProjectTag(Long projectId, String userId, String tagName, HttpServletRequest request) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/tag";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.setContentType(MediaType.APPLICATION_JSON);

            TagDto tagDto = new TagDto();
            tagDto.setName(tagName);
            tagDto.setProject(getProjectById(request, projectId));

            ResponseEntity<TagDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    tagDto,
                    headers,
                    TagDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Project Tag", e);
        }
    }

    public MilestoneDto addProjectMilestone(Long projectId, String userId, String title, String startDate, String endDate, HttpServletRequest request) {
        try {
            String url = "http://localhost:8082/projects/" + projectId + "/milestone";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);
            headers.setContentType(MediaType.APPLICATION_JSON);

            MilestoneDto milestoneDto = new MilestoneDto();
            milestoneDto.setTitle(title);
            milestoneDto.setStartDate(convertToZonedDateTime(startDate));
            milestoneDto.setEndDate(convertToZonedDateTime(endDate));
            milestoneDto.setProject(getProjectById(request, projectId));

            ResponseEntity<MilestoneDto> response = restApiClient.sendRequestWithHeaders(
                    url,
                    HttpMethod.POST,
                    milestoneDto,
                    headers,
                    MilestoneDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to add Project Milestone", e);
        }
    }

    private ZonedDateTime convertToZonedDateTime(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
}
