package com.student_plan;

import com.student_plan.entity.User;
import com.student_plan.repository.UserRepository;
import com.student_plan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import javax.swing.*;
import java.beans.Encoder;
import java.beans.ExceptionListener;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@AutoConfigureMockMvc
class UserServiceTest extends AbstractTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUsers_Empty_Success() {

        //when
        final List<User> emptyUsersList = userService.getAllUsers();

        //then
        assertThat(emptyUsersList.size(), equalTo(0));
    }

    @Test
     void getAllUsers_One_Element_Success() {
        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        //when
        final List<User> singletonUser = userService.getAllUsers();

        //then
        assertThat(singletonUser.get(0).getFirstName(), equalTo("firstName"));
        assertThat(singletonUser.get(0).getLastName(), equalTo("lastName"));
        assertThat(singletonUser.get(0).getMail(), equalTo("mail@mail.com"));
        assertThat(singletonUser.get(0).getPassword(), equalTo("password".toCharArray()));
        assertThat(singletonUser.get(0).getType(), equalTo(STUDENT));
        assertThat(singletonUser.get(0).isEnabled(), equalTo(true));

     }

    @Test
    void deleteUser_Success() {

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.pl",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        //when

            userService.deleteById(user.getId());


            final Optional<User> singleUser = userRepository.findById(user.getId());
            assertFalse(singleUser.isPresent());
    }

    @Test
    void saveNewUser_Success() {

           //given
           final User user = UserModelCreator.createUser(
                   "firstName",
                   "lastName",
                   "mail@mail.com",
                   "password".toCharArray(),
                   STUDENT,
                   true);

           //when

           userService.saveNewUser(user);
           CharBuffer passwordBuffer = CharBuffer.wrap(user.getPassword());

           //then

           final List<User> singletonUser = userService.getAllUsers();

           assertThat(singletonUser.get(0).getFirstName(), equalTo("firstName"));
           assertThat(singletonUser.get(0).getLastName(), equalTo("lastName"));
           assertThat(singletonUser.get(0).getMail(), equalTo("mail@mail.com"));
           assertThat(singletonUser.get(0).getPassword(), equalTo(passwordBuffer.array()));
           assertThat(singletonUser.get(0).getType(), equalTo(STUDENT));
           assertThat(singletonUser.get(0).isEnabled(), equalTo(true));
    }

    @Test
    void updateUser_Correct_Params_Success() {

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        //when
        userRepository.save(user);


        //then
        userService.updateUser("name","surname","test@mail.com",user.getId());

        final Optional<User> singleUser = userRepository.findById(user.getId());

        assertTrue((singleUser.isPresent()));
        assertThat(singleUser.get().getFirstName(), equalTo("name"));
        assertThat(singleUser.get().getLastName(), equalTo("surname"));
        assertThat(singleUser.get().getMail(), equalTo("test@mail.com"));
        assertThat(singleUser.get().getPassword(), equalTo("password".toCharArray()));
        assertThat(singleUser.get().getType(), equalTo(STUDENT));
        assertThat(singleUser.get().isEnabled(), equalTo(true));
    }

    @Test
    @WithMockUser(username = "mail@mail.com")
    void updateUserPassword_Correct_Params_Success() {

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);


        //when
        userService.saveNewUser(user);
        final char[] pass = "pass".toCharArray();


        //then
        userService.updateUserPassword("password".toCharArray() ,"pass".toCharArray(),user.getId());

        final Optional<User> singleUser = userRepository.findById(user.getId());

        assertTrue((singleUser.isPresent()));
        assertThat(singleUser.get().getFirstName(), equalTo("firstName"));
        assertThat(singleUser.get().getLastName(), equalTo("lastName"));
        assertThat(singleUser.get().getMail(), equalTo("mail@mail.com"));
        assertTrue(passwordEncoder.matches(String.valueOf(pass),String.valueOf(singleUser.get().getPassword())));
        assertThat(singleUser.get().getType(), equalTo(STUDENT));
        assertThat(singleUser.get().isEnabled(), equalTo(true));
    }
}

