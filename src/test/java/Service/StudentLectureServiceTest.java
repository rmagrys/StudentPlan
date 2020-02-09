package Service;

import ModelCreator.LectureModelCreator;
import ModelCreator.StudentLectureModelCreator;
import ModelCreator.UserModelCreator;
import com.student_plan.AbstractTest;
import com.student_plan.entity.Lecture;
import com.student_plan.entity.StudentLecture;
import com.student_plan.entity.User;
import com.student_plan.expections.NotFoundException;
import com.student_plan.expections.NotSavedException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.StudentLectureRepository;
import com.student_plan.repository.UserRepository;
import com.student_plan.service.StudentLectureService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.CharBuffer;
import java.time.LocalDate;
import java.util.List;

import static com.student_plan.entity.Type.LECTURER;
import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StudentLectureServiceTest extends AbstractTest {

    @Autowired
    private StudentLectureService studentLectureService;

    @Autowired
    private StudentLectureRepository studentLectureRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;


    @Test
    void getAllStudentLecture_Empty_Success(){

        //when
        final List<StudentLecture> emptyPresenceList = studentLectureService.getAllStudentLectures();

        //then
        assertThat(emptyPresenceList.size(),equalTo(0));
    }

    @Test
    void getAllStudentLecture_OneElement_Success(){

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2017-12-27"));

        lectureRepository.save(lecture);

        final StudentLecture studentLecture = StudentLectureModelCreator.createStudentLecture(
                lecture,
                user,
                false);

        studentLectureRepository.save(studentLecture);

        //when
        final List<StudentLecture> singlePresence = studentLectureService.getAllStudentLectures();

        //then
        assertThat(singlePresence.size(),equalTo(1));
        assertThat(singlePresence.get(0).getId(),equalTo(studentLecture.getId()));
        assertThat(singlePresence.get(0).getUser().getId(),equalTo(user.getId()));
        assertThat(singlePresence.get(0).getUser().getMail(),equalTo("mail@mail.com"));
        assertThat(singlePresence.get(0).getLecture().getId(),equalTo(lecture.getId()));
        assertThat(singlePresence.get(0).getLecture().getLectureName(),equalTo("lectureName"));
        assertThat(singlePresence.get(0).getPresence(),equalTo(false));

    }

    @Test
    void registerStudentLecturePresence_Success(){

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2017-12-27"));

        lectureRepository.save(lecture);

        //when
        studentLectureService.registerStudentLecturePresence(lecture.getId(), user.getId(),true);

        //then
        final List<StudentLecture> singlePresence = studentLectureService.getAllStudentLectures();

        assertThat(singlePresence.size(),equalTo(1));
        assertThat(singlePresence.get(0).getUser().getId(),equalTo(user.getId()));
        assertThat(singlePresence.get(0).getUser().getMail(),equalTo("mail@mail.com"));
        assertThat(singlePresence.get(0).getLecture().getId(),equalTo(lecture.getId()));
        assertThat(singlePresence.get(0).getLecture().getLectureName(),equalTo("lectureName"));
        assertThat(singlePresence.get(0).getPresence(),equalTo(true));
    }

    @Test
    void registerStudentLecturePresence_notStudent_ThrowsNotSaved(){

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                LECTURER,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2017-12-27"));

        lectureRepository.save(lecture);

        //then
        assertThrows(NotSavedException.class, () ->
                studentLectureService
                        .registerStudentLecturePresence(
                                lecture.getId(),
                                user.getId(),
                                true));
    }

    @Test
    void registerStudentLecturePresence_beforeLecture_ThrowsNotSaved(){

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2030-12-27"));

        lectureRepository.save(lecture);

        //then
        assertThrows(NotSavedException.class, () ->
                studentLectureService
                        .registerStudentLecturePresence(
                                lecture.getId(),
                                user.getId(),
                                true));
    }

    @Test
    void registerStudentLecturePresence_notUnique_ThrowsNotSaved(){

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                LECTURER,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2017-12-27"));

        lectureRepository.save(lecture);

        final StudentLecture studentLecture = StudentLectureModelCreator.createStudentLecture(
                lecture,
                user,
                false);

        studentLectureRepository.save(studentLecture);

        //then
        assertThrows(NotSavedException.class, () ->
                studentLectureService
                        .registerStudentLecturePresence(
                                lecture.getId(),
                                user.getId(),
                                true));
    }
    @Test
    void registerStudentLecturePresence_wrongId_ThrowsNotSaved(){
        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                LECTURER,
                true);

        userRepository.save(user);

        //then
        assertThrows(NotFoundException.class, () ->
                studentLectureService
                        .registerStudentLecturePresence(
                                123123L,
                                user.getId(),
                                true));
    }

    @Test
    void updateStudentLecturePresence_Success(){
        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                LECTURER,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2017-12-27"));

        lectureRepository.save(lecture);

        final StudentLecture studentLecture = StudentLectureModelCreator.createStudentLecture(
                lecture,
                user,
                false);

        studentLectureRepository.save(studentLecture);

        //when
        studentLectureService.updateStudentLecturePresence(studentLecture.getId(),true);

        //then
        final List<StudentLecture> singlePresence = studentLectureRepository.findAll();

        assertThat(singlePresence.size(),equalTo(1));
        assertThat(singlePresence.get(0).getUser().getId(),equalTo(user.getId()));
        assertThat(singlePresence.get(0).getUser().getMail(),equalTo("mail@mail.com"));
        assertThat(singlePresence.get(0).getLecture().getId(),equalTo(lecture.getId()));
        assertThat(singlePresence.get(0).getLecture().getLectureName(),equalTo("lectureName"));
        assertThat(singlePresence.get(0).getPresence(),equalTo(true));
    }

    @Test
    void updateStudentLecturePresence_wrongId_ThrowsNotFound(){

        assertThrows(NotFoundException.class, () ->
                studentLectureService.updateStudentLecturePresence(
                        123123L,
                        true));
    }
}
