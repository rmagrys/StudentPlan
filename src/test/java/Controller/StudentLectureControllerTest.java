package Controller;

import ModelCreator.LectureModelCreator;
import ModelCreator.StudentLectureModelCreator;
import ModelCreator.UserModelCreator;
import com.student_plan.AbstractTest;
import com.student_plan.entity.Lecture;
import com.student_plan.entity.StudentLecture;
import com.student_plan.entity.User;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.StudentLectureRepository;
import com.student_plan.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.CharBuffer;
import java.time.LocalDate;

import static com.student_plan.entity.Type.LECTURER;
import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class StudentLectureControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StudentLectureRepository studentLectureRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllStudentLectures_OneElement_Success() throws Exception{

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
                true);

        studentLectureRepository.save(studentLecture);

        mvc.perform(
                get("/api/student-lecture")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(studentLecture.getId().intValue())))
                .andExpect(jsonPath("$[0].userId", equalTo(user.getId().intValue())))
                .andExpect(jsonPath("$[0].lectureId", equalTo(lecture.getId().intValue())))
                .andExpect(jsonPath("$[0].presence", equalTo(true)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllStudentLectures_Empty_Success() throws Exception {

        mvc.perform(
                get("/api/student-lecture")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOneStudentLecture_Success() throws Exception {

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

        mvc.perform(
                get("/api/student-lecture/" + studentLecture.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(studentLecture.getId().intValue())))
                .andExpect(jsonPath("$.userId", equalTo(user.getId().intValue())))
                .andExpect(jsonPath("$.lectureId", equalTo(lecture.getId().intValue())))
                .andExpect(jsonPath("$.presence", equalTo(false)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOneStudentLecture_WrongId_ThrowsNotFound() throws Exception {

        mvc.perform(
                get("/api/student-lecture/123123")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code",equalTo(404)))
                .andExpect(jsonPath("$.message",equalTo("Presence [id=123123] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewStudentLecturePresence_Success() throws Exception {

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

        final String ulrTemplate = "/api/student-lecture/lecture/";
        final String ulrPresence = "/ascribe?present=1";

        mvc.perform(
                post(ulrTemplate + lecture.getId() + "/student/" + user.getId() + ulrPresence)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(userRepository
                        .findById(user.getId())
                        .orElseThrow(Exception::new)
                        .getStudentLectures()
                        .get(0)
                        .getId()
                        .intValue()
                )));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewStudentLecturePresence_notStudent_ThrowsNotSaved() throws Exception {

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

        final String ulrTemplate = "/api/student-lecture/lecture/";
        final String ulrPresence = "/ascribe?present=1";

        mvc.perform(
                post(ulrTemplate + lecture.getId() + "/student/" + user.getId() + ulrPresence)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo("Presence is not saved")));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewStudentLecturePresence_notUnique_ThrowsNotSaved() throws Exception {

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
                true );

        studentLectureRepository.save(studentLecture);

        final String ulrTemplate = "/api/student-lecture/lecture/";
        final String ulrPresence = "/ascribe?present=1";

        mvc.perform(
                post(ulrTemplate + lecture.getId() + "/student/" + user.getId() + ulrPresence)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo("Presence is not saved")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewStudentLecturePresence_beforeLecture_ThrowsNotSaved() throws Exception {

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

        final String ulrTemplate = "/api/student-lecture/lecture/";
        final String ulrPresence = "/ascribe?present=1";

        mvc.perform(
                post(ulrTemplate + lecture.getId() + "/student/" + user.getId() + ulrPresence)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo("Presence is not saved")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewStudentLecturePresence_wrongId_ThrowsNotFound() throws Exception {


        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2017-12-27"));

        lectureRepository.save(lecture);

        final String ulrTemplate = "/api/student-lecture/lecture/";
        final String ulrPresence = "/ascribe?present=1";

        mvc.perform(
                post(ulrTemplate + lecture.getId() + "/student/123123/" + ulrPresence)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("User [id=123123] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateStudentLecturePresence_Success() throws Exception{

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

        final String urlTemplate1 = "/api/student-lecture/";
        final String urlTemplate2 = "/presence-update/?present=true";

        mvc.perform(
                patch(urlTemplate1 + studentLecture.getId() + urlTemplate2 )
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(userRepository
                        .findById(user.getId())
                        .orElseThrow(Exception::new)
                        .getStudentLectures()
                        .get(0)
                        .getId()
                        .intValue()
                )));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateStudentLecturePresence_wrongId_Throws() throws Exception {

        final String urlTemplate1 = "/api/student-lecture/";
        final String urlTemplate2 = "/presence-update/?present=true";

        mvc.perform(
                patch(urlTemplate1 + "123123" + urlTemplate2 )
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Presence [id=123123] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateStudentLecturePresence_WrongParam_Success() throws Exception{

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

        final String urlTemplate1 = "/api/student-lecture/";
        final String urlTemplate2 = "/presence-update/?present=wrongParam";

        mvc.perform(
                patch(urlTemplate1 + studentLecture.getId() + urlTemplate2 )
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }
}

