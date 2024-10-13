package com.nhnacademy.miniDooray.controller;

import com.nhnacademy.miniDooray.dto.CommentDto;
import com.nhnacademy.miniDooray.dto.CommentRegisterDto;
import com.nhnacademy.miniDooray.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/projects/{projectId}/tasks/{taskId}/comments")
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public String createComment(@PathVariable Long projectId, @PathVariable Long taskId, @ModelAttribute CommentRegisterDto commentRegisterDto, HttpServletRequest request) {
        String userId = (String) request.getAttribute("validatedUserId");

        commentService.createComment(projectId, taskId, userId, commentRegisterDto);


        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }
}
