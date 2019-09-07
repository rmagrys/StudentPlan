package com.student_plan.service;

import com.student_plan.entity.Lecture;
import com.student_plan.entity.User;
import com.student_plan.entity.StudentLecture;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.StudentLectureRepository;
import com.student_plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentLectureService {

    private final StudentLectureRepository studentLectureRepository;
    private final UserRepository userRepository;
    private final LectureRepository lectureRepository;


    public List<StudentLecture> getAllStudentLectures(){
        return studentLectureRepository.findAll();
    }

    public List<StudentLecture> findAllLecturesForStudent(){
        return studentLectureRepository.findAllByStudentId();
    }

    public StudentLecture saveNewStudentLecture(StudentLecture studentLecture){
        return studentLectureRepository.save(studentLecture);
    }

    public StudentLecture saveNewStudentLectureDependency(StudentLecture studentLecture) {
        return studentLectureRepository.saveAndFlush(studentLecture);
    }

    public Long registerStudentLectureDependency(Long lectureId, Long studentId, boolean presence) {
        User user = userRepository
                .findById(studentId)
                .orElseThrow(()-> new NotFoundException("User [id="+studentId+"] not found"));
        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(()-> new NotFoundException("Lecture [id="+lectureId+"] not found"));

        return SaveStatus(user,lecture,presence);
    }

    private Long SaveStatus(User user, Lecture lecture, Boolean presence){
        StudentLecture studentLecture = StudentLecture
                .builder()
                .lecture(lecture)
                .user(user)
                .presence(presence)
                .build();

        return studentLectureRepository.save(studentLecture).getId();
    }

    public StudentLecture findOneById(Long studentLectureId){
        return studentLectureRepository
                .findById(studentLectureId)
                .orElseThrow(()-> new NotFoundException("Dependency [id="+studentLectureId+"] not found"));
    }
}
