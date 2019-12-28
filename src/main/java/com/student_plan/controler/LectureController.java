package com.student_plan.controler;


import com.student_plan.dto.LectureDto;
import com.student_plan.dto.LectureDtoConverter;
import com.student_plan.dto.LectureParamsDto;
import com.student_plan.entity.Lecture;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<LectureDto> getAll(){

        return lectureService
                .getAllLectures()
                .stream()
                .map(LectureDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{lectureId}")
    @PreAuthorize("isAuthenticated()")
    public LectureDto getOne(@PathVariable Long lectureId){
        Lecture lecture = lectureService
                .getLectureById(lectureId)
                .orElseThrow(() ->
                    new NotFoundException("Lecture [id="+lectureId+"] not found")
                );

        return LectureDtoConverter.toDto(lecture);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public LectureDto addNewLecture(@RequestBody @Valid LectureParamsDto lectureParamsDto) {

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
    public void deleteLecture(@PathVariable long lectureId){
        lectureService.deleteLectureById(lectureId);
    }

    @PatchMapping("/{lectureId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public LectureDto updateLecture(
            @PathVariable long lectureId,
            @RequestBody @Valid LectureParamsDto lectureParamsDto){

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
    public Long registerLecturerToLecture(
            @PathVariable long lectureId,
            @PathVariable long userId){

        return lectureService.registerLecturerToLecture(lectureId, userId);
    }

}

