package com.student_plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordDto {

    private final static String message = "CONTENT_NOT_VALID";

    @Size(min = 5, max = 70, message = message)
    private char[] newPassword;

    @Size(min = 5, max = 70, message = message)
    private char[] oldPassword;
}
