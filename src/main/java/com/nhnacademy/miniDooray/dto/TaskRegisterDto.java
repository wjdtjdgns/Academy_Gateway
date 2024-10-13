package com.nhnacademy.miniDooray.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskRegisterDto {
    @NotNull
    private Long milestoneId;

    @NotNull
    @Length(min = 1, max = 50)
    private String title;

    @NotNull
    @Length
    private String content;
}
