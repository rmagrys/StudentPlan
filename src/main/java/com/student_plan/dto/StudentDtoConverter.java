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

    public static Student toEntity(StudentDto studentDto){

        Student student = new Student();

        student.setId(studentDto.getId());
        student.setFirstName(studentDto.getFirstName());
        student.setLastName(studentDto.getLastName());
        student.setMail(studentDto.getMail());

        return student;

    }
}
