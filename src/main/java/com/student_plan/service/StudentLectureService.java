package com.student_plan.service;

import com.student_plan.entity.StudentLecture;
import com.student_plan.repository.StudentLectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentLectureService {

    private final StudentLectureRepository studentLectureRepository;

    public List<StudentLecture> findAllLecturesForStudent(){
        return studentLectureRepository.findAllByStudentId();
    }
}
