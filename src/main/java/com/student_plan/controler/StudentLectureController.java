package com.student_plan.controler;

import com.student_plan.dto.StudentLectureDto;
import com.student_plan.dto.StudentLectureDtoConverter;
import com.student_plan.service.StudentLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/studentLecture")
@RequiredArgsConstructor
public class StudentLectureController {

    private final StudentLectureService studentLectureService;

    @GetMapping
    List<StudentLectureDto> getAll(){
        return studentLectureService
                .findAllLecturesForStudent()
                .stream()
                .map(StudentLectureDtoConverter::toDto)
                .collect(Collectors.toList());
    }
}
