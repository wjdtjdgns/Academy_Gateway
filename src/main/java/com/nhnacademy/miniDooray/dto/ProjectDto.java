package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.common.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private ProjectStatus status;
    private String adminId;
    private List<String> memberIds;
}