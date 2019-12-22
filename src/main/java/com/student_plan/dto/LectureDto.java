package com.student_plan.dto;

import com.student_plan.entity.Lecture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureDto {

    private final static String MESSAGE = "CONTENT_NOT_VALID";

    private Long id;

    @Size(min = 5 , max = 50, message = LectureDto.MESSAGE)
    private String lectureName;

    @DateTimeFormat(pattern = "dd/MM/yyyy",iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;

    private List<StudentLectureDto> studentLecturesDto;

}
