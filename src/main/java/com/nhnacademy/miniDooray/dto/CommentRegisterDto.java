package com.nhnacademy.miniDooray.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommentRegisterDto {
    @NotNull
    @Length(min = 1, max = 255)
    private String content;
}
