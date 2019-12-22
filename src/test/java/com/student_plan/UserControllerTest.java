package com.student_plan;

import com.student_plan.dto.UserParamsDto;
import com.student_plan.dto.UserPasswordDto;
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

import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class UserControllerTest extends AbstractTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentLectureRepository studentLectureRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllUsers_Empty_Success() throws Exception {


        mvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getAllUsers_OneElement_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);


        mvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_STREAM_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", equalTo("firstName")))
                .andExpect(jsonPath("$[0].lastName", equalTo("lastName")))
                .andExpect(jsonPath("$[0].mail", equalTo("mail@mail.com")))
                .andExpect(jsonPath("$[0].password", equalTo(null)))
                .andExpect(jsonPath("$[0].type", equalTo("STUDENT")))
                .andExpect(jsonPath("$[0].enabled", equalTo(true)));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOneUser_ById_NotFound() throws Exception {

        mvc.perform(
                get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("User [id=1] not found")));


    }

    @Test
    @WithMockUser(authorities = "USER")
    void getOneUserById_UnauthorizedRole_Failure() throws Exception {

        mvc.perform(
                get("/api/users/11111")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getOneUserById_AnonymousRole_Failure() throws Exception {

        mvc.perform(
                get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOneUserById_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        mvc.perform(
                get("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("firstName")))
                .andExpect(jsonPath("$.lastName", equalTo("lastName")))
                .andExpect(jsonPath("$.mail", equalTo("mail@mail.com")))
                .andExpect(jsonPath("$.password", equalTo(null)))
                .andExpect(jsonPath("$.type", equalTo("STUDENT")))
                .andExpect(jsonPath("$.enabled", equalTo(true)));
    }


    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewUser_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        mvc.perform(
                post("/api/users")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("firstName")))
                .andExpect(jsonPath("$.lastName", equalTo("lastName")))
                .andExpect(jsonPath("$.mail", equalTo("mail@mail.com")))
                .andExpect(jsonPath("$.password", equalTo(null)))
                .andExpect(jsonPath("$.type", equalTo("STUDENT")))
                .andExpect(jsonPath("$.enabled", equalTo(true)));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUser_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);


        mvc.perform(
                delete("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUser_Failure_NotFound() throws Exception {

        mvc.perform(
                delete("/api/users/512")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("User [id=512] not found")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUser_CorrectParams_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final UserParamsDto userParamsDto = UserModelCreator.createUserParamsDto(
                "name",
                "surname",
                "test@mail.com");

        mvc.perform(
                patch("/api/users/" + user.getId())
                        .content(TestUtils.convertObjectsToJsonBytes(userParamsDto))
                        .contentType(MediaType.APPLICATION_JSON)

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("name")))
                .andExpect(jsonPath("$.lastName", equalTo("surname")))
                .andExpect(jsonPath("$.mail", equalTo("test@mail.com")))
                .andExpect(jsonPath("$.password", equalTo(null)))
                .andExpect(jsonPath("$.type", equalTo("STUDENT")))
                .andExpect(jsonPath("$.enabled", equalTo(true)));
    }


    @Test
    @WithMockUser(username = "mail@mail.com", authorities = "ADMIN")
    void updateUserPassword_CorrectParams_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final UserPasswordDto userPasswordDto = UserModelCreator.createUserPasswordDto(
                "password".toCharArray(),
                "newPassword".toCharArray()
        );

        mvc.perform(
                patch("/api/users/password/" + user.getId())
                        .content(TestUtils.convertObjectsToJsonBytes(userPasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)

        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("firstName")))
                .andExpect(jsonPath("$.lastName", equalTo("lastName")))
                .andExpect(jsonPath("$.mail", equalTo("mail@mail.com")))
                .andExpect(jsonPath("$.password", equalTo(null)))
                .andExpect(jsonPath("$.type", equalTo("STUDENT")))
                .andExpect(jsonPath("$.enabled", equalTo(true)));
    }

    @Test
    @WithMockUser(username = "mail@mail.com", authorities = "ADMIN")
    void updateUserPassword_WrongPassword_Failure() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final UserPasswordDto userPasswordDto = UserModelCreator.createUserPasswordDto(
                "wrongPassword".toCharArray(),
                "newPassword".toCharArray()
        );

        mvc.perform(
                patch("/api/users/password/" + user.getId())
                        .content(TestUtils.convertObjectsToJsonBytes(userPasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)

        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("User old password is incorrect")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUser_WrongLastNameSize_Failure() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "N",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        mvc.perform(
                patch("/api/users/" + user.getId())
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());

        }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewUser_WrongFirstNameSize_Failure() throws Exception {

        final User user = UserModelCreator.createUser(
                "N",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);


        mvc.perform(
                post("/api/users")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewUser_WrongMailNameSize_Failure() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "a@n",
                "password".toCharArray(),
                STUDENT,
                true);

        mvc.perform(
                post("/api/users")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());

    }
    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewUser_WrongMailFormat_Failure() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "emailWithWrongFormat",
                "password".toCharArray(),
                STUDENT,
                true);

        mvc.perform(
                post("/api/users")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUserPassword_WrongPasswordNameSize_Failure() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final UserPasswordDto userPasswordDto= UserModelCreator.createUserPasswordDto(
                "N".toCharArray(),
                "lastName".toCharArray()
                );


        mvc.perform(
                patch("/api/users/password/" + user.getId())
                        .content(TestUtils.convertObjectsToJsonBytes(userPasswordDto))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewUser_NotUniqueMail_ThrowsNotUnique() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.pl",
                passwordEncoder.encode(CharBuffer.wrap("password")).toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        mvc.perform(
                post("/api/users")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code",equalTo(409)))
                .andExpect(jsonPath("$.message",equalTo("Email already exist")));
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUser_NotUniqueParams_ThrowsNotUnique() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final UserParamsDto userParamsDto = UserModelCreator.createUserParamsDto(
                "name",
                "surname",
                "mail@mail.com");

        mvc.perform(
                patch("/api/users/" + user.getId())
                        .content(TestUtils.convertObjectsToJsonBytes(userParamsDto))
                        .contentType(MediaType.APPLICATION_JSON)

        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo("Email already exist")));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void addNewLecturer_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        mvc.perform(
                post("/api/users/lecturer")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("firstName")))
                .andExpect(jsonPath("$.lastName", equalTo("lastName")))
                .andExpect(jsonPath("$.mail", equalTo("mail@mail.com")))
                .andExpect(jsonPath("$.password", equalTo(null)))
                .andExpect(jsonPath("$.type", equalTo("LECTURER")))
                .andExpect(jsonPath("$.enabled", equalTo(true)));

    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOneUserWithPresences_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        final Lecture lecture = LectureModelCreator.createLecture(
                "lectureName",
                LocalDate.parse("2011-12-27"));

        lectureRepository.save(lecture);

        final StudentLecture studentLecture = StudentLectureModelCreator.createStudentLecture(
                lecture,
                user,
                true);

        studentLectureRepository.saveAndFlush(studentLecture);

        mvc.perform(
                get("/api/users/" + user.getId() + "/presences")
                        .content(TestUtils.convertObjectsToJsonBytes(user))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("firstName")))
                .andExpect(jsonPath("$.lastName", equalTo("lastName")))
                .andExpect(jsonPath("$.mail", equalTo("mail@mail.com")))
                .andExpect(jsonPath("$.password", equalTo(null)))
                .andExpect(jsonPath("$.type", equalTo("STUDENT")))
                .andExpect(jsonPath("$.enabled", equalTo(true)))
                .andExpect(jsonPath("$.studentLecturesDto[0].lectureId", equalTo(lecture.getId().intValue())))
                .andExpect(jsonPath("$.studentLecturesDto[0].userId", equalTo(user.getId().intValue())))
                .andExpect(jsonPath("$.studentLecturesDto[0].presence", equalTo(true)));
    }
}



