package com.student_plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserParamsDto {

    private final static String message = "CONTENT_NOT_VALID";

    @Size(min = 3, max = 60, message = message)
    private String firstName;

    @Size(min = 3, max = 60, message = message)
    private String lastName;

    @Email
    @Size(min = 5, max = 50, message = message)
    private String mail;
}
