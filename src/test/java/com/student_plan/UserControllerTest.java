package com.student_plan;

import com.student_plan.entity.Type;
import com.student_plan.entity.User;
import com.student_plan.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import javax.swing.tree.ExpandVetoException;

import static com.student_plan.entity.Type.STUDENT;
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest extends AbstractTest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUsers_Empty_Success() throws Exception {

            mvc.perform(
                    get("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())

                    .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
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
                        get("/api/students")
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].firstName", equalTo("firstName")))
                    .andExpect(jsonPath("$[0].lastName", equalTo("lastName")))
                    .andExpect(jsonPath("$[0].mail", equalTo("mail@mail.com")))
                    .andExpect(jsonPath("$[0].password", equalTo("password")))
                    .andExpect(jsonPath("$[0].type", equalTo("STUDENT")))
                    .andExpect(jsonPath("$[0].enabled", equalTo("true")));






    }
}
