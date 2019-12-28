package com.student_plan;

import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.StudentLectureRepository;
import com.student_plan.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private StudentLectureRepository studentLectureRepository;

    @AfterEach
    public void afterEachTest(){

        cleanStudentLectureDatabase();
        cleanLectureDatabase();
        cleanUserDatabase();
    }

    private void cleanLectureDatabase() {
        lectureRepository.deleteAll();
    }

    private void cleanStudentLectureDatabase() {
        studentLectureRepository.deleteAll();
    }

    private void cleanUserDatabase() {
       userRepository.deleteAll();
    }
}
