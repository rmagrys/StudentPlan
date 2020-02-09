package Controller;

import ModelCreator.LectureModelCreator;
import ModelCreator.UserModelCreator;
import com.student_plan.AbstractTest;
import com.student_plan.TestUtils;
import com.student_plan.dto.LectureParamsDto;
import com.student_plan.entity.Lecture;
import com.student_plan.entity.User;
import com.student_plan.repository.LectureRepository;
import com.student_plan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.student_plan.entity.Type.LECTURER;
import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@AutoConfigureMockMvc
class LectureControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private UserService userService;


    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewLecture_Success() throws Exception {

        final LectureParamsDto lectureParamsDto = LectureModelCreator.createLectureParamsDto(
                "lectureName",
                "01",
                "02",
                "1992");

        mvc.perform(
                post("/api/lectures")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtils.convertObjectsToJsonBytes(lectureParamsDto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureName", equalTo("lectureName")))
                .andExpect(jsonPath("$.date", equalTo("1992-02-01")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewLecture_unrealDateParams_ThrowsBadRequest() throws Exception {

        final LectureParamsDto lectureParamsDto = LectureModelCreator.createLectureParamsDto(
                "lectureName",
                "91",
                "92",
                "1992");

        mvc.perform(
                post("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectsToJsonBytes(lectureParamsDto))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Wrong Date format")));
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewLecture_wrongSizeParams_ThrowsBadRequest() throws Exception {

        final LectureParamsDto lectureParamsDto = LectureModelCreator.createLectureParamsDto(
                "l",
                "9132",
                "9",
                "1992123");

        mvc.perform(
                post("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectsToJsonBytes(lectureParamsDto))
        )
                .andExpect(status().isBadRequest());
    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecture_Success() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        final LectureParamsDto lectureToUpdate = LectureModelCreator.createLectureParamsDto(
                "newLectureName",
                "01",
                "02",
                "1992");

        lectureRepository.save(lecture);

        mvc.perform(
               patch("/api/lectures/" + lecture.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectsToJsonBytes(lectureToUpdate))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureName", equalTo("newLectureName")))
                .andExpect(jsonPath("$.date", equalTo("1992-02-01")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecture_unrealDateParams_ThrowBadRequest() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        final LectureParamsDto lectureToUpdate = LectureModelCreator.createLectureParamsDto(
                "newLectureName",
                "91",
                "92",
                "9992");

        lectureRepository.save(lecture);

        mvc.perform(
                patch("/api/lectures/" + lecture.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectsToJsonBytes(lectureToUpdate))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Wrong Date format")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecture_wrongDateSizeParams_ThrowBadRequest() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        final LectureParamsDto lectureToUpdate = LectureModelCreator.createLectureParamsDto(
                "l",
                "999991",
                "9299",
                "1");

        lectureRepository.save(lecture);

        mvc.perform(
                patch("/api/lectures/" + lecture.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectsToJsonBytes(lectureToUpdate))
        )
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecture_wrongId_ThrowNotFound() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        final LectureParamsDto lectureToUpdate = LectureModelCreator.createLectureParamsDto(
                "NewLectureName",
                "11",
                "11",
                "1996");

        lectureRepository.save(lecture);

        mvc.perform(
                patch("/api/lectures/123123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.convertObjectsToJsonBytes(lectureToUpdate))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Lecture [id=123123] not found")));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getOneLecture_success() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        lectureRepository.save(lecture);

        mvc.perform(
                get("/api/lectures/" + lecture.getId())
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureName",equalTo("lectureName")))
                .andExpect(jsonPath("$.date", equalTo("2018-12-27")));


    }

    @Test
    @WithMockUser(authorities = "USER")
    void getOneLecture_wrongId_throwsNotFound() throws Exception {

        mvc.perform(
                get("/api/lectures/123123")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Lecture [id=123123] not found")));

    }
    @Test
    void getOneLecture_AnonymousRole_Failure() throws Exception {

        mvc.perform(
                get("/api/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getAllLectures_Empty_Success() throws Exception {

        mvc.perform(
                get("/api/lectures")
                    .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(0)));
    }

    @Test
    @WithMockUser(authorities = "USER")
    void getAllLectures_OneElement_Success() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2018-12-27"));

        lectureRepository.save(lecture);

        mvc.perform(
                get("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].lectureName", equalTo("lectureName")))
                .andExpect(jsonPath("$[0].date", equalTo("2018-12-27")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteLecture_Success() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("3000-12-27"));

        lectureRepository.save(lecture);

        mvc.perform(
                delete("/api/lectures/" + lecture.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteLecture_lectureTookPlace_throwsNotDeleted() throws Exception {

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2011-12-27"));

        lectureRepository.save(lecture);

        mvc.perform(
                delete("/api/lectures/" + lecture.getId())
                            .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code",equalTo(409)))
                .andExpect(jsonPath("$.message",equalTo("This Lecture actually took place, cannot be deleted")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteLecture_wrongId_throwsNotFound() throws Exception {

        mvc.perform(
                delete("/api/lectures/123123")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code",equalTo(404)))
                .andExpect(jsonPath("$.message",equalTo("Lecture [id=123123] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecturerToLecture_Success() throws Exception{

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2011-12-27"));

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                LECTURER,
                true);

        lectureRepository.save(lecture);
        userService.saveNewLecturer(user);

        mvc.perform(
                post("/api/lectures/" + lecture.getId() +  "/lecturer/" + user.getId() + "/ascribe")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",equalTo(lecture.getId().intValue())));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecturerToLecture_wrongLectureId_ThrowsNotFound() throws Exception{

        mvc.perform(
                post("/api/lectures/12345/lecturer/12345/ascribe")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code",equalTo(404)))
                .andExpect(jsonPath("$.message",equalTo("Lecture [id=12345] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecturerToLecture_wrongUserId_ThrowsNotFound() throws Exception{

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2011-12-27"));

        lectureRepository.save(lecture);

        mvc.perform(
                post("/api/lectures/"+ lecture.getId() + "/lecturer/12345/ascribe")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code",equalTo(404)))
                .andExpect(jsonPath("$.message",equalTo("User [id=12345] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateLecturerToLecture_NotLecturer_ThrowsBadRequest() throws Exception{

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2011-12-27"));

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        lectureRepository.save(lecture);
        userService.saveNewUser(user);

        mvc.perform(
                post("/api/lectures/" + lecture.getId() +  "/lecturer/" + user.getId() + "/ascribe")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code",equalTo(400)))
                .andExpect(jsonPath("$.message",equalTo("User is not a Lecturer")));
    }


}
