package com.student_plan.controler;


import com.student_plan.dto.LectureDto;
import dtoConverters.LectureDtoConverter;
import com.student_plan.dto.LectureParamsDto;
import com.student_plan.entity.Lecture;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.service.LectureService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "Get all lectures", response = Lecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved lecture list"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "No lectures in database"),
    })
    public List<LectureDto> getAll(){
    log.info("Fetching all lectures");

        return lectureService
                .getAllLectures()
                .stream()
                .map(LectureDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{lectureId}")
    @PreAuthorize("isAuthenticated()")
    @ApiOperation(value = "Get lecture by id", response = Lecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetched lecture"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Lecture not found"),
    })
    public LectureDto getOne(@ApiParam(value = "Lecture id", required = true) @PathVariable Long lectureId){
        log.info("Fetching lecture by id [id = " + lectureId +"]");
        Lecture lecture = lectureService
                .getLectureById(lectureId)
                .orElseThrow(() ->
                    new NotFoundException("Lecture [id="+lectureId+"] not found")
                );

        return LectureDtoConverter.toDto(lecture);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Add new lecture to database", response = Lecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added lecture"),
            @ApiResponse(code = 400, message = "Wrong data or date format"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
    })
    public LectureDto addNewLecture(@ApiParam(value = "Lecture params container", required = true) @RequestBody @Valid LectureParamsDto lectureParamsDto) {
        log.info("Adding new lecture to database");
        LocalDate date;
            try {
                 date = LocalDate.parse(lectureParamsDto.getYear() + "-"
                        + lectureParamsDto.getMonth() + "-"
                        + lectureParamsDto.getDay());

            } catch (Exception e) {
                throw new BadRequestException("Wrong Date format");
            }

        Lecture lecture = LectureDtoConverter.toEntityWithParams(lectureParamsDto,date);

        return LectureDtoConverter.toDto(lectureService.saveNewLecture(lecture));
    }

    @DeleteMapping("/{lectureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Delete lecture by id", response = Lecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted lecture"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Lecture not found"),
            @ApiResponse(code = 409, message = "Lecture not deleted")
    })
    public void deleteLecture(@ApiParam(value = "Lecture id",required = true) @PathVariable long lectureId){
        log.info("Deleting Lecture by id[id = "+ lectureId +"]");
        lectureService.deleteLectureById(lectureId);
    }

    @PatchMapping("/{lectureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update lecture by id", response = Lecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted lecture"),
            @ApiResponse(code = 400, message = "Wrong data or date format"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Lecture not found"),
            @ApiResponse(code = 409, message = "Lecture not deleted")
    })
    public LectureDto updateLecture(
            @ApiParam(value = "Lecture id", required = true) @PathVariable long lectureId,
            @ApiParam(value = "Lecture params container", required = true) @RequestBody @Valid LectureParamsDto lectureParamsDto){
        log.info("Updating Lecture by id[id =" + lectureId + "]");
        LocalDate date;
            try {
                date = LocalDate.parse(lectureParamsDto.getYear() + "-"
                        + lectureParamsDto.getMonth() + "-"
                        + lectureParamsDto.getDay());
            } catch (Exception e) {
                throw new BadRequestException("Wrong Date format");
            }

        return LectureDtoConverter.toDto(
                lectureService
                        .updateLecture(
                                lectureId,
                                lectureParamsDto.getLectureName(),
                                date));
    }


    @PostMapping("/{lectureId}/lecturer/{userId}/ascribe")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Register lecturer to lecture", response = Lecture.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully registered lecturer to lecture"),
            @ApiResponse(code = 400, message = "Cannot update lecturer to lecture"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Lecture or lecturer not found"),
    })
    public Long registerLecturerToLecture(
            @ApiParam(value = "Lecture id", required = true)@PathVariable long lectureId,
            @ApiParam(value = "User id", required = true)@PathVariable long userId){
        log.info("Registering Lecture by id[id =" + lectureId + "]");
        return lectureService.registerLecturerToLecture(lectureId, userId);
    }

}

