package com.student_plan.controler;

import com.student_plan.dto.StudentLectureDto;
import com.student_plan.dto.StudentLectureDtoConverter;
import com.student_plan.entity.StudentLecture;
import com.student_plan.service.StudentLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student-lecture")
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
    @GetMapping("/{studentLectureId}")
    public StudentLectureDto getOne(@PathVariable Long studentLectureId){
        StudentLecture studentLecture = studentLectureService.findOneById(studentLectureId);
        return StudentLectureDtoConverter.toDto(studentLecture);
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
             @RequestParam(value = "present") boolean presence){

        return studentLectureService.registerStudentLectureDependency(lectureId, studentId, presence);



    }
}
