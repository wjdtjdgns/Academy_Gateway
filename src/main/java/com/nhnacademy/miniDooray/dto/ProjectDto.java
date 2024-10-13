package com.nhnacademy.miniDooray.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long projectId;
    private String projectName;
    private String status;
    private String adminId;
    private List<MemberDto> members;
}