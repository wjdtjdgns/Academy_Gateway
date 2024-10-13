package com.nhnacademy.miniDooray.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProjectResponse {
    private List<ProjectDto> projects;
}
