package com.student_plan.controler;

import com.student_plan.dto.*;
import com.student_plan.entity.Lecture;
import com.student_plan.entity.Student;
import com.student_plan.entity.StudentLecture;
import com.student_plan.service.LectureService;
import com.student_plan.service.StudentLectureService;
import com.student_plan.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/studentLecture")
@RequiredArgsConstructor
public class StudentLectureController {

    private final StudentLectureService studentLectureService;


    @GetMapping
    public List<StudentLectureDto> getAll(){
        return studentLectureService
                .getAllStudentLectures()
                .stream()
                .map(StudentLectureDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    public StudentLectureDto addNewStudentLecture(@RequestBody StudentLectureDto studentLectureDto){
        StudentLecture studentLecture = StudentLectureDtoConverter.toEntity(studentLectureDto);
        return StudentLectureDtoConverter.toDto(studentLectureService.saveNewStudentLecture(studentLecture));
    }

    @PostMapping("lecture/{lectureId}/student/{studentId}/ascribe")
    public Long addNewStudentLectureDependency(
             @PathVariable Long lectureId,
             @PathVariable Long studentId,
             @RequestParam(value = "true" ,required = true) boolean presence){

        return studentLectureService.registerStudentLectureDependency(lectureId, studentId, presence);



    }
}
