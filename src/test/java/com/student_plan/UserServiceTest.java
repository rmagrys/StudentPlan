package com.student_plan;

import com.student_plan.entity.User;
import com.student_plan.repository.UserRepository;
import com.student_plan.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.nio.CharBuffer;
import java.util.List;

import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

@AutoConfigureMockMvc
class UserServiceTest extends AbstractTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void getAllUsers_Empty_Success() {

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

       /* @Test
        public void deleteUser_Success(){

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);

        //when
            final List<User> singleUser = userRepository.getByFirstName("firstName");

            userRepository.delete(singleUser);

            assertThat(singleUser, equalTo(not(empty())));
            assertThat(singleUser.);
        }*/

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

           assertThat(singletonUser.get(0).getId(), equalTo(1L));
           assertThat(singletonUser.get(0).getFirstName(), equalTo("firstName"));
           assertThat(singletonUser.get(0).getLastName(), equalTo("lastName"));
           assertThat(singletonUser.get(0).getMail(), equalTo("mail@mail.com"));
           assertThat(singletonUser.get(0).getPassword(), equalTo(passwordBuffer.array()));
           assertThat(singletonUser.get(0).getType(), equalTo(STUDENT));
           assertThat(singletonUser.get(0).isEnabled(), equalTo(true));
       }
}
