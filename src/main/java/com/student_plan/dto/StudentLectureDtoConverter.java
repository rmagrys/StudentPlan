package com.student_plan.dto;


import com.student_plan.entity.Lecture;
import com.student_plan.entity.StudentLecture;

public class StudentLectureDtoConverter {

    public static StudentLectureDto toDto(StudentLecture studentLecture){

        StudentLectureDto studentLectureDto = new StudentLectureDto();

        studentLectureDto.setId(studentLecture.getId());
 /*       studentLectureDto.setLectureId(studentLecture.getLecture().getId());
        studentLectureDto.setStudentId(studentLecture.getStudent().getId());*/
        studentLectureDto.setPresence(studentLecture.getPresence());


        return studentLectureDto;
    }
     public static StudentLecture toEntity(StudentLectureDto studentLectureDto){

        StudentLecture studentLecture = new StudentLecture();
        studentLecture.setPresence(studentLectureDto.getPresence());

        return studentLecture;
     }
}
