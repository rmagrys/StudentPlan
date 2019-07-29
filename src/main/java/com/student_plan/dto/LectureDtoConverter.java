package com.student_plan.dto;

import com.student_plan.entity.Lecture;

public class LectureDtoConverter {

    public static LectureDto toDto(Lecture lecture){

        LectureDto lectureDto = new LectureDto();

        lectureDto.setId(lecture.getId());
        lectureDto.setLectureName(lecture.getLectureName());

        return lectureDto;
    }

    public static Lecture toEntity(LectureDto lectureDto){

        Lecture lecture = new Lecture();

        lecture.setId(lectureDto.getId());
        lecture.setLectureName(lectureDto.getLectureName());

        return lecture;
    }
}
