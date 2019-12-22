package com.student_plan.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LectureParamsDto {

    private final static String MESSAGE = "CONTENT_NOT_VALID";

    private Long id;

    @Size(min = 5 , max = 50, message = MESSAGE)
    private String lectureName;

    @Size(min = 2, max = 2, message = MESSAGE)
    private String day;

    @Size(min = 2, max = 2, message = MESSAGE)
    private String month;

    @Size(min = 4, max = 4, message = MESSAGE)
    private String year;

    private List<StudentLectureDto> studentLecturesDto;

}