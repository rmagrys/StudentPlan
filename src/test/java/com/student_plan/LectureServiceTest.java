package com.student_plan;


import com.student_plan.entity.Lecture;
import com.student_plan.entity.User;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotDeletedException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.service.LectureService;
import com.student_plan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDate;
import java.util.List;

import static com.student_plan.entity.Type.LECTURER;
import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LectureServiceTest extends AbstractTest {

    @Autowired
    LectureService lectureService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void getAllLectures_empty_success() {

        //when
        final List<Lecture> emptyLectureList = lectureService.getAllLectures();

        //then
        assertThat(emptyLectureList.size(),equalTo(0));

    }

    @Test
    void getAllLectures_oneElement_success() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        lectureRepository.save(lecture);

        //when
        final List<Lecture> singleLecture = lectureService.getAllLectures();

        //then
        assertThat(singleLecture.size(),equalTo(1));
        assertThat(singleLecture.get(0).getLectureName(),equalTo("lectureName"));
        assertThat(singleLecture.get(0).getDate(),equalTo(lecture.getDate()));
    }

    @Test
    void deleteLecture_success() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2020-12-27"));

        lectureRepository.save(lecture);

        //when
        lectureService.deleteLectureById(lecture.getId());

        //then
        final List<Lecture> emptyList = lectureService.getAllLectures();

        assertTrue(emptyList.isEmpty());
    }

    @Test
    void deleteLecture_tookPlace_throwBadRequest() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2011-12-27"));

        lectureRepository.save(lecture);


        //when
        assertThrows(NotDeletedException.class, () -> lectureService
            .deleteLectureById(lecture.getId()));

    }

    @Test
    void deleteLecture_wrongId_throwsNotFound() {

        assertThrows(NotFoundException.class, () -> lectureService
                .deleteLectureById(11111));
    }

    @Test
    void updateLecture_Success() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        lectureRepository.save(lecture);

        LocalDate date = LocalDate.parse("2040-12-27");

        //when

        lectureService.updateLecture(lecture.getId(),"newLectureName", date);

        //then

        final List<Lecture> singleLecture = lectureService.getAllLectures();

        assertThat(singleLecture.size(),equalTo(1));
        assertThat(singleLecture.get(0).getLectureName(),equalTo("newLectureName"));
        assertThat(singleLecture.get(0).getDate(),equalTo(date));
    }

    @Test
    void updateLecture_wrongId_ThrowsNotFound() {

        //given
        LocalDate date = LocalDate.parse("2040-12-27");

        //then

        assertThrows(NotFoundException.class, () ->lectureService
                .updateLecture(123123,"newLectureName", date));

    }

    @Test
    void updateLecture_WrongParamSize_ThrowsTransactional() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        lectureRepository.save(lecture);
        LocalDate date = LocalDate.parse("2040-12-27");

        //then

        assertThrows(TransactionSystemException.class, () -> lectureService
                .updateLecture(lecture.getId(),"n", date));
    }

    @Test
    void updateLecturerToLecture_Success() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                LECTURER,
                true);

        lectureRepository.save(lecture);
        userService.saveNewLecturer(user);

        //when
        lectureService.registerLecturerToLecture(lecture.getId(),user.getId());

        //then
        final List<Lecture> singleLecture = lectureService.getAllLectures();

        assertThat(singleLecture.size(),equalTo(1));
        assertThat(singleLecture.get(0).getLectureName(),equalTo("lectureName"));
        assertThat(singleLecture.get(0).getDate(),equalTo(lecture.getDate()));
        assertThat(singleLecture.get(0).getLecturer().getId(),equalTo(user.getId()));
        assertThat(singleLecture.get(0).getLecturer().getFirstName(),equalTo("firstName"));
        assertThat(singleLecture.get(0).getLecturer().getLastName(),equalTo("lastName"));
        assertThat(singleLecture.get(0).getLecturer().getMail(),equalTo("mail@mail.com"));
        assertTrue(passwordEncoder.matches(
                String.valueOf("password".toCharArray()),
                String.valueOf(singleLecture.get(0).getLecturer().getPassword())
                ));
        assertThat(singleLecture.get(0).getLecturer().getType(),equalTo(LECTURER));
        assertThat(singleLecture.get(0).getLecturer().isEnabled(),equalTo(true));

    }

    @Test
    void UpdateLecturerToLecture_notLecturer_ThrowsBadRequest() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        lectureRepository.save(lecture);
        userService.saveNewUser(user);

        assertThrows(BadRequestException.class, () ->
                lectureService.registerLecturerToLecture(lecture.getId(),user.getId()));
    }

    @Test
    void UpdateLecturerToLecture_wrongUserId_ThrowsNotFound() {

        //given
        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        lectureRepository.save(lecture);

        assertThrows(NotFoundException.class, () ->
                lectureService.registerLecturerToLecture(lecture.getId(),123123));
    }

    @Test
    void UpdateLecturerToLecture_wrongLectureId_ThrowsNotFound() {

        assertThrows(NotFoundException.class, () ->
                lectureService.registerLecturerToLecture(123123,123123));
    }
}
