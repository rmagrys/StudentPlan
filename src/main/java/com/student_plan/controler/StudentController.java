package com.student_plan.controler;

import com.student_plan.dto.StudentDto;
import com.student_plan.dto.StudentDtoConverter;
import com.student_plan.entity.Student;
import com.student_plan.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
                .getStudentById(studentId);


        return StudentDtoConverter.toDto(student);
    }

    @PostMapping
    public StudentDto addNewStudent(@RequestBody StudentDto studentDto){
        Student student = StudentDtoConverter.toEntity(studentDto);

        return StudentDtoConverter
                .toDto(studentService.saveNewStudent(student));

    }

    @DeleteMapping("/{studentId}")
    public void deleteStudent(@PathVariable Long studentId){
        studentService.deleteById(studentId);
    }

    @PatchMapping("/{studentId}")
    public StudentDto updateStudent(
            @PathVariable Long studentId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String mail){

        return StudentDtoConverter.toDto(studentService.updateStudent(firstName, lastName, mail, studentId));
    }

}
