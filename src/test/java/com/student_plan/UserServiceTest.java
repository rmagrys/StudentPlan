package com.student_plan;

import com.student_plan.entity.Type;
import com.student_plan.entity.User;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.expections.NotUniqueException;
import com.student_plan.repository.UserRepository;
import com.student_plan.service.UserService;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.TransactionSystemException;

import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

import static com.student_plan.entity.Type.LECTURER;
import static com.student_plan.entity.Type.STUDENT;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
     void getAllUsers_OneElement_Success() {
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

        //then
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
    void updateUser_CorrectParams_Success() {

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

    @Test
    @WithMockUser(username = "mail@mail.com")
    void updateUserPassword_WrongPassword_ThrowsBadRequest() {

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

        //then

        assertThrows(BadRequestException.class, () -> userService
                .updateUserPassword("wrongPassword"
                        .toCharArray() ,"pass"
                        .toCharArray(),user.getId())
        );
    }
    @Test
    void updateUser_WrongParams_Success() {

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

        assertThrows(TransactionSystemException.class, () -> userService
                .updateUser(
                        "n",
                        "surname",
                        "test@mail.com",
                        user.getId())
        );
    }

    @Test
    void updateUser_WrongMailFormat_Success() {

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
        assertThrows(TransactionSystemException.class, () -> userService
                .updateUser(
                        "name",
                        "surname",
                        "x",
                        user.getId())
        );
    }
    @Test
    void saveNewUser_NotUniqueMail_ThrowsNotUnique() {

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

        //then
       assertThrows(NotUniqueException.class, () -> userService
               .saveNewUser(user)
       );

    }
    @Test
    void saveNewUser_WrongSizeParams_ThrowsTransactional() {


        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "l",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        //then
        assertThrows(TransactionSystemException.class, () -> userService
                .saveNewUser(user)
        );
    }
    @Test
    void deleteUser_WrongId_ThrowsNotFound() {

        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.pl",
                "password".toCharArray(),
                STUDENT,
                true);

        userRepository.save(user);


        //then
        assertThrows(NotFoundException.class, () -> userService.deleteById(1234L));
    }

    @Test
    void updateUser_WrongId_ThrowsNotFound() {

        //then
        assertThrows(NotFoundException.class, () -> userService
                .updateUser(
                        "name",
                        "surname",
                        "test@test.com",
                        1231L)
        );
    }

    @Test
    void saveNewLecturer_Success(){
        //given
        final User user = UserModelCreator.createUser(
                "firstName",
                "lastName",
                "mail@mail.com",
                "password".toCharArray(),
                STUDENT,
                true);

        //when

        userService.saveNewLecturer(user);
        CharBuffer passwordBuffer = CharBuffer.wrap(user.getPassword());

        //then

        final List<User> singletonUser = userService.getAllUsers();

        assertThat(singletonUser.get(0).getFirstName(), equalTo("firstName"));
        assertThat(singletonUser.get(0).getLastName(), equalTo("lastName"));
        assertThat(singletonUser.get(0).getMail(), equalTo("mail@mail.com"));
        assertThat(singletonUser.get(0).getPassword(), equalTo(passwordBuffer.array()));
        assertThat(singletonUser.get(0).getType(), equalTo(Type.LECTURER));
        assertThat(singletonUser.get(0).isEnabled(), equalTo(true));
    }

}



