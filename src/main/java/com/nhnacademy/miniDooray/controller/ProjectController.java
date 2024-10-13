package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.dto.ProjectRegisterDto;
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

    @GetMapping
    public String getProjects(HttpServletRequest request,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        List<ProjectDto> projects = projectService.getProjects(request, page, size);
        model.addAttribute("projects", projects);
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
}
