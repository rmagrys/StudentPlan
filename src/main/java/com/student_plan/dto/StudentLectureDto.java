package com.student_plan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLectureDto {

    private Long id;
    private Long studentId;
    private Long lectureId;
    private Boolean presence;

}
