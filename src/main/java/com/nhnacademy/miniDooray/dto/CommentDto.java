package com.nhnacademy.miniDooray.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CommentDto {
    private Long id;
    private TaskDto task;
    @NotEmpty
    private String memberId;
    @NotEmpty
    private String content;
}