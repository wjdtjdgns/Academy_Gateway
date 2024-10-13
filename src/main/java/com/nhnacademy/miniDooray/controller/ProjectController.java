package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectRegisterDto;
import com.nhnacademy.miniDooray.service.MemberService;
import com.nhnacademy.miniDooray.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final MemberService memberService;

    @GetMapping
    public String getProjects(HttpServletRequest request,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        String userId = (String) request.getAttribute("validatedUserId");
        String userName = (String) request.getAttribute("validatedUserName");
        List<ProjectDto> projects = projectService.getProjects(request, page, size);

        model.addAttribute("projects", projects);
        model.addAttribute("userId", userId);
        model.addAttribute("userName", userName);

        return "projectList";
    }

    @GetMapping("/add")
    public String addProjectForm() {
        return "addProjectForm";
    }

    @PostMapping("/add")
    public String createProject(@ModelAttribute ProjectRegisterDto projectRegisterDto,
                                HttpServletRequest request,
                                Model model) {
        try {
            projectService.createProject(request, projectRegisterDto);
            model.addAttribute("message", "프로젝트가 성공적으로 추가되었습니다.");
            return "redirect:/projects";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "addProjectForm";
        }
    }

    @PostMapping("/{projectId}/members")
    public String addProjectMember(@PathVariable Long projectId, @RequestParam String memberId, HttpServletRequest request) {
        String userId = (String) request.getAttribute("validatedUserId");
        projectService.addProjectMember(projectId, userId, List.of(memberId));

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/{projectId}/tags")
    public String addProjectTag(@PathVariable Long projectId, @RequestParam String tagName, HttpServletRequest request) {
        String userId = (String) request.getAttribute("validatedUserId");

        projectService.addProjectTag(projectId, userId, tagName, request);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

    @PostMapping("/{projectId}/milestones")
    public String addProjectMilestone(
                    @PathVariable Long projectId,
                    @RequestParam String milestoneTitle,
                    @RequestParam String startDate,
                    @RequestParam String endDate,
                    HttpServletRequest request
    ) {
        String userId = (String) request.getAttribute("validatedUserId");

        projectService.addProjectMilestone(projectId, userId, milestoneTitle, startDate, endDate, request);

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
