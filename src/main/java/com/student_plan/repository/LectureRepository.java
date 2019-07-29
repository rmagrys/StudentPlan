package com.student_plan.repository;

import com.student_plan.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LectureRepository extends JpaRepository<Lecture, Long> {

    @Query("SELECT lecture FROM Lecture lecture WHERE lecture.lectureName = :lectureName")
    List<Lecture> findAllByLectureNameJpql();
}
