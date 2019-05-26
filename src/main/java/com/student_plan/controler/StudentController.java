package com.student_plan.controler;

import com.student_plan.dto.StudentDto;
import com.student_plan.dto.StudentDtoConverter;
import com.student_plan.entity.Student;
import com.student_plan.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> getAll() {
        return studentService
                .getAllStudents()
                .stream()
                .map(StudentDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{studentId}")
    public StudentDto getOne(@PathVariable Long studentId){
        Student student = studentService
                .getStudentById(studentId)
                .orElse(new Student());

        return StudentDtoConverter.toDto(student);
    }
}
