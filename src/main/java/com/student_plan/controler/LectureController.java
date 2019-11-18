package com.student_plan.controler;


import com.student_plan.dto.LectureDto;
import com.student_plan.dto.LectureDtoConverter;
import com.student_plan.entity.Lecture;
import com.student_plan.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        Lecture lecture = lectureService.getLectureById(lectureId);

        return LectureDtoConverter.toDto(lecture);
    }

    @PostMapping
    public LectureDto addNewLecture(@RequestBody @Valid LectureDto lectureDto){
        Lecture lecture = LectureDtoConverter.toEntity(lectureDto);

        return LectureDtoConverter.toDto(lectureService.saveNewLecture(lecture));
    }

    @DeleteMapping("/{lectureId")
    public void deleteLecture(@PathVariable long id){
        lectureService.deleteLectureById(id);
    }

    @PatchMapping("/{lectureId}")
    public LectureDto updateLecture(
            @PathVariable long lectureId,
            @RequestParam(required = false) String lectureName){

        return LectureDtoConverter.toDto(lectureService.updateLecture(lectureId,lectureName));
    }

}

