package com.student_plan.dto;


import com.student_plan.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private final static String message = "CONTENT_NOT_VALID";

    private Long id;

    @Size(min = 3, max = 60, message = message)
    private String firstName;

    @Size(min = 3, max = 60, message = message)
    private String lastName;

    @Email
    @Size(min = 5, max = 50, message = message)
    private String mail;

    private Type type;

    @Size(min = 5, max = 70, message = message)
    private char[] password;

    private boolean enabled = true;
    private List<StudentLectureDto> studentLecturesDto;

}
