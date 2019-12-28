package com.student_plan.service;

import com.student_plan.entity.Lecture;
import com.student_plan.entity.StudentLecture;
import com.student_plan.entity.Type;
import com.student_plan.entity.User;
import com.student_plan.expections.NotFoundException;
import com.student_plan.expections.NotSavedException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.StudentLectureRepository;
import com.student_plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public Long registerStudentLecturePresence(Long lectureId, Long studentId, boolean presence) {
        User user = userRepository
                .findById(studentId)
                .orElseThrow(() ->
                    new NotFoundException("User [id=" + studentId + "] not found")
                );

        Lecture lecture = lectureRepository
                .findById(lectureId)
                .orElseThrow(() ->
                    new NotFoundException("Lecture [id=" + lectureId + "] not found")
                );


        if(isPresenceUnique(user,lecture) && isUserRoleStudent(user) && isRegisteredAfterLecture(lecture)) {
            return SaveStatus(user, lecture, presence);
        } else {
            throw new NotSavedException("Presence is not saved");
        }
    }

    private boolean isRegisteredAfterLecture(Lecture lecture) {
        return lecture.getDate().isBefore(LocalDate.now());
    }

    private boolean isUserRoleStudent(User user) {
        return user.getType().equals(Type.STUDENT);
    }

    private boolean isPresenceUnique(User user, Lecture lecture) {
        return studentLectureRepository.countByLectureAndUser(lecture, user) == 0;
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
                .orElseThrow(() ->
                    new NotFoundException("Presence [id=" + studentLectureId + "] not found")
                );
    }

    public Long updateStudentLecturePresence(Long studentLectureId, Boolean presence) {
        StudentLecture studentLecture = studentLectureRepository
                .findById(studentLectureId)
                .orElseThrow(() ->
                        new NotFoundException("Presence [id=" + studentLectureId + "] not found")
                );

        studentLecture.setPresence(presence);

       return studentLectureRepository.save(studentLecture).getId();
    }
}
