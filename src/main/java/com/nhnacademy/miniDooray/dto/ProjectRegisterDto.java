package com.nhnacademy.miniDooray.dto;

import com.nhnacademy.miniDooray.common.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRegisterDto {
    private String name;

    private ProjectStatus status = ProjectStatus.ACTIVATED;
}
