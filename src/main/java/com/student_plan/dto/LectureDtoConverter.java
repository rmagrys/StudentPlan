package com.student_plan.dto;

import com.student_plan.entity.Lecture;
import com.student_plan.entity.StudentLecture;

import java.time.LocalDate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class LectureDtoConverter {

    public static LectureDto toDto(Lecture lecture){

        LectureDto lectureDto = new LectureDto();

        lectureDto.setId(lecture.getId());
        lectureDto.setLectureName(lecture.getLectureName());
        lectureDto.setDate(lecture.getDate());

        return lectureDto;
    }

    public static Lecture toEntity(LectureDto lectureDto){

        Lecture lecture = new Lecture();

        lecture.setId(lectureDto.getId());
        lecture.setLectureName(lectureDto.getLectureName());
        lecture.setDate(lectureDto.getDate());

        return lecture;
    }

    public static Lecture toEntityWithParams(LectureParamsDto lectureParamsDto, LocalDate date){

        Lecture lecture = new Lecture();

        lecture.setId(lectureParamsDto.getId());
        lecture.setLectureName(lectureParamsDto.getLectureName());
        lecture.setDate(date);

        return lecture;
    }

    /*public static LectureDto toDtoWithStudentList(Lecture lecture){

        LectureDto lectureDto = new LectureDto();

        lectureDto.setId(lecture.getId());
        lectureDto.setLectureName(lecture.getLectureName());
        lectureDto.setStudentLectures(StudentLecture
                .getStudentLecturesBy()
                .stream()
                .map(StudentLectureDtoConverter::toDto)
                .collect(Collectors.toList());
                }
*/

}
