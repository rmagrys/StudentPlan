package com.student_plan.repository;

import com.student_plan.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> getAllByFirstName(String firstName);

    @Query("SELECT student FROM Student student WHERE student.firstName = :firstName")
    List<Student> getAllByFirstNameJpql(String firstName);

}
