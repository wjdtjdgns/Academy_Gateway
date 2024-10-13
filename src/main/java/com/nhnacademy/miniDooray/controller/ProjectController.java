package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.ProjectDto;
import com.nhnacademy.miniDooray.service.ProjectService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}