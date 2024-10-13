package com.nhnacademy.miniDooray.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskTagDto {
    private Long id;
    private TagDto tag;
    private TaskDto task;
    private boolean isSelected;
}
