package com.student_plan.service;


import com.student_plan.entity.Lecture;
import com.student_plan.entity.Student;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final LectureRepository lectureRepository;
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id){
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));
    }

    public Student saveNewStudent(Student student){
        return studentRepository.save(student);
    }

    public void deleteById(Long id){
        studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User [id="+id+"] not found"));
        studentRepository.deleteById(id);
    }

    public Student updateStudent(String firstName, String lastName, String mail, Long studentId){
        Student studentForUpdate = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new NotFoundException("User [id="+studentId+"] not found") );

        updateStudentValues(firstName, lastName, mail, studentForUpdate);
        return studentRepository.save(studentForUpdate);
    }

    private void updateStudentValues(String firstName, String lastName, String mail, Student student) {

        if(firstName != null)
            student.setFirstName(firstName);
        if(lastName != null)
            student.setLastName(lastName);
        if(mail != null)
            student.setMail(mail);

    }
}
