package com.student_plan;

import com.student_plan.entity.User;
import com.student_plan.repository.UserRepository;
import com.student_plan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends AbstractTest {

    @Autowired
    UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

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
    void getOneUser_ById_Unauthorized_Role_Failure() throws Exception {

        mvc.perform(
                get("/api/users/11111")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void getOneUser_ById_Anonymous_Role_Failure() throws Exception {

        mvc.perform(
                get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void getOneUser_ById_Success() throws Exception {

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
    void updateUser_Correct_Params_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        final String queryParams = "?firstName=name&lastName=surname&mail=test@mail.com";

        userRepository.save(user);


        mvc.perform(
                patch("/api/users/" + user.getId() + queryParams)
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
    @WithMockUser(authorities = "ADMIN")
    void updateUserPassword_Correct_Params_Success() throws Exception {

        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        String queryParams = "?oldPassword=password&newPassword=pass";

        mvc.perform(
                patch("/api/users/password/" + user.getId() + queryParams)
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

}
