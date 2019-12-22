package com.student_plan;


import com.student_plan.entity.Lecture;
import com.student_plan.expections.NotDeletedException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.service.LectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class LectureServiceTest extends AbstractTest {

    @Autowired
    LectureService lectureService;

    @Autowired
    LectureRepository lectureRepository;

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

        assertThrows(NotDeletedException.class, () -> lectureService
            .deleteLectureById(lecture.getId()));

    }
}
