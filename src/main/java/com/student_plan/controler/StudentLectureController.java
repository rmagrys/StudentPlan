package com.student_plan.controler;

import com.student_plan.dto.StudentLectureDto;
import com.student_plan.dto.StudentLectureDtoConverter;
import com.student_plan.entity.StudentLecture;
import com.student_plan.service.StudentLectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/student-lecture")
@RequiredArgsConstructor
public class  StudentLectureController {

    private final StudentLectureService studentLectureService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
    public List<StudentLectureDto> getAll(){
        return studentLectureService
                .getAllStudentLectures()
                .stream()
                .map(StudentLectureDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{studentLectureId}")
    @PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
    public StudentLectureDto getOne(@PathVariable Long studentLectureId){
        StudentLecture studentLecture = studentLectureService.findOneById(studentLectureId);
        return StudentLectureDtoConverter.toDto(studentLecture);
    }

    @PostMapping("lecture/{lectureId}/student/{studentId}/ascribe")
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN','LECTURER')")
    public Long addNewStudentLecturePresence(
             @PathVariable Long lectureId,
             @PathVariable Long studentId,
             @RequestParam(value = "present") boolean presence){

        return studentLectureService.registerStudentLecturePresence(lectureId, studentId, presence);
    }

    @PatchMapping("/{studentLectureId}/presence-update")
    @PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
    public Long updateStudentLecturePresence(
            @PathVariable Long studentLectureId,
            @RequestParam(value = "present") Boolean presence) {

        return studentLectureService.updateStudentLecturePresence(studentLectureId,presence);
    }
}
