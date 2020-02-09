package com.student_plan.controler;

import com.student_plan.dto.StudentLectureDto;
import dtoConverters.StudentLectureDtoConverter;
import com.student_plan.entity.StudentLecture;
import com.student_plan.service.StudentLectureService;
import io.swagger.annotations.*;
import jdk.jfr.Frequency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/student-lecture")
@RequiredArgsConstructor
public class  StudentLectureController {

    private final StudentLectureService studentLectureService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
    @ApiOperation(value = "Get all presences", response = StudentLecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched presence list"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
    })
    public List<StudentLectureDto> getAll(){
        log.info("Fetching all presences");

        return studentLectureService
                .getAllStudentLectures()
                .stream()
                .map(StudentLectureDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{studentLectureId}")
    @PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
    @ApiOperation(value = "Get presence by id", response = StudentLecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched presence"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Presence not found"),
    })
    public StudentLectureDto getOne(@ApiParam(value ="Presence id",required = true) @PathVariable Long studentLectureId){
        log.info("Fetching user by id[id =" + studentLectureId + "]");
        StudentLecture studentLecture = studentLectureService.findOneById(studentLectureId);

        return StudentLectureDtoConverter.toDto(studentLecture);
    }

    @PostMapping("lecture/{lectureId}/student/{studentId}/ascribe")
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN','LECTURER')")
    @ApiOperation(value = "Register user presence", response = StudentLecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered presence"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "User or lecture not found"),
            @ApiResponse(code = 409, message = "Cannot save presence"),
    })
    public Long addNewStudentLecturePresence(
            @ApiParam(value = "Lecture id", required = true) @PathVariable Long lectureId,
            @ApiParam(value = "User id" , required = true) @PathVariable Long studentId,
            @ApiParam(value = "Presence", required = true) @RequestParam(value = "present") boolean presence){
        log.info("Registering user[id = " + studentId + " ] to lecture [id = "+ lectureId + "]");

        return studentLectureService.registerStudentLecturePresence(lectureId, studentId, presence);
    }

    @PatchMapping("/{studentLectureId}/presence-update")
    @PreAuthorize("hasAnyAuthority('LECTURER','ADMIN')")
    @ApiOperation(value = "Update presence by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully updated presence"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Presence not found"),
            @ApiResponse(code = 409, message = "Cannot save presence"),
    })
    public Long updateStudentLecturePresence(
            @ApiParam(value = "Presence id", required = true)@PathVariable Long studentLectureId,
            @ApiParam(value = " Presence", required =  true)@RequestParam(value = "present") Boolean presence) {
        log.info("Updating presence by id [id= " + studentLectureId + "]");

        return studentLectureService.updateStudentLecturePresence(studentLectureId,presence);
    }
}
