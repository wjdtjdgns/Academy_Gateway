package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.*;
import com.nhnacademy.miniDooray.service.CommentService;
import com.nhnacademy.miniDooray.service.ProjectService;
import com.nhnacademy.miniDooray.service.TaskService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks")
@Controller
public class TaskController {

    private final ProjectService projectService;
    private final TaskService taskService;
    private final CommentService commentService;

    @GetMapping
    public String getTaskList(@PathVariable Long projectId, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("validatedUserId");
        ProjectDto project = projectService.getProjectById(request, projectId);
        List<MemberInfoDto> members = projectService.lookupMembers(project.getMemberIds(), userId);
        List<TagDto> tags = projectService.getProjectTags(projectId, userId);
        List<MilestoneDto> milestones = projectService.getProjectMilestones(projectId, userId);
        List<TaskDto> tasks = taskService.getTasks(projectId, userId);

        model.addAttribute("projectId", project.getId());
        model.addAttribute("isAdmin", project.getAdminId().equals(userId));
        model.addAttribute("members", members);
        model.addAttribute("tags", tags);
        model.addAttribute("milestones", milestones);
        model.addAttribute("tasks", tasks);


        return "taskList";
    }

    @GetMapping("/add")
    public String getTaskForm(@PathVariable Long projectId, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("validatedUserId");
        List<MilestoneDto> milestones = projectService.getProjectMilestones(projectId, userId);
        List<TagDto> tags = projectService.getProjectTags(projectId, userId);

        model.addAttribute("milestones", milestones);
        model.addAttribute("tags", tags);

        return "addTaskForm";
    }

    @PostMapping
    public String addTask(@PathVariable Long projectId,
                          @RequestParam String title,
                          @RequestParam String content,
                          @RequestParam Long milestoneId,
                          @RequestParam(required = false) List<Long> tagIds,
                          HttpServletRequest request
    ) {
        String userId = (String) request.getAttribute("validatedUserId");

        taskService.createTask(projectId, userId, title, content, milestoneId, tagIds);

        return String.format("redirect:/projects/%d/tasks", projectId);
    }

    @GetMapping("/{taskId}")
    public String getTaskDetail(@PathVariable Long projectId, @PathVariable Long taskId, Model model, HttpServletRequest request) {
        String userId = (String) request.getAttribute("validatedUserId");
        TaskDto task = taskService.getTask(projectId, taskId, userId);
        List<CommentDto> comments = commentService.getCommentByTaskId(projectId, taskId, userId);

        model.addAttribute("task", task);
        model.addAttribute("comments", comments);
        return "taskDetail";
    }
}
