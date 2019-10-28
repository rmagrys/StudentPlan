package com.student_plan.dto;


import com.student_plan.entity.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String mail;
    private Type type;
    private char[] password;
    private boolean enabled = true;
    private List<StudentLectureDto> studentLecturesDto;

}
