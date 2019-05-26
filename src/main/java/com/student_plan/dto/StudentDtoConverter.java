package com.student_plan.dto;


import com.student_plan.entity.Student;

public class StudentDtoConverter {

    public static StudentDto toDto(Student student){

        StudentDto studentDto = new StudentDto();

        studentDto.setId(student.getId());
        studentDto.setFirstName(student.getFirstName());
        studentDto.setLastName(student.getLastName());
        studentDto.setMail(student.getMail());

        return studentDto;
    }
}
