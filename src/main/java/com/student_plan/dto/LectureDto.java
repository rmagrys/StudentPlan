package com.student_plan.dto;

import com.student_plan.entity.StudentLecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDto {

    private Long id;
    private String lectureName;
    private List<StudentLectureDto> studentLecturesDto;

}
