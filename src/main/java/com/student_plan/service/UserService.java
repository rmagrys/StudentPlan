package com.student_plan.service;


import com.student_plan.dto.UserParamsDto;
import com.student_plan.dto.UserPasswordDto;
import com.student_plan.entity.User;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.expections.NotUniqueException;
import com.student_plan.repository.StudentLectureRepository;
import com.student_plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.nio.CharBuffer;
import java.util.List;
import java.util.Optional;

import static com.student_plan.entity.Type.LECTURER;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentLectureRepository studentLectureRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public User saveNewUser(@Valid User user) throws NotUniqueException {
        if(isEmailUnique(user.getMail())){
                setUserPassword(user);
                return userRepository.save(user);
        } else {
                throw new NotUniqueException("Email already exist");
        }
    }

    private void setUserPassword(@Valid User user) {
        CharBuffer passwordBuffer = CharBuffer.wrap(user.getPassword());
        user.setPassword(passwordEncoder.encode(passwordBuffer).toCharArray());
    }

    private boolean isEmailUnique(String mail) {
        return userRepository.countByMail(mail) == 0;
    }

    @Transactional
    public void deleteById(Long id){
        userRepository
                .findById(id)
                .orElseThrow(() ->
                            new NotFoundException("User [id=" + id + "] not found")
                );

        studentLectureRepository.deleteAllByUserId(id);
        userRepository.deleteById(id);
    }

    public User updateUser(UserParamsDto userParamsDto, Long id){
       if(!isEmailUnique(userParamsDto.getMail())) {
           throw new NotUniqueException("Email already exist");
       } else {
           User userForUpdate = userRepository
                   .findById(id)
                   .orElseThrow(() ->
                           new NotFoundException("User [id=" + id + "] not found")
                   );
           updateUserValues(userParamsDto.getFirstName(),
                   userParamsDto.getLastName(),
                   userParamsDto.getMail(), userForUpdate);

           return userRepository.save(userForUpdate);
       }
    }

    private void updateUserValues(String firstName, String lastName, String mail, @Valid User user) {

        if(firstName != null)
            user.setFirstName(firstName);
        if(lastName != null)
            user.setLastName(lastName);
        if(mail != null)
            user.setMail(mail);
    }

    public User updateUserPassword(UserPasswordDto userPasswordDto, Long userId) {

        User userForUpdatePassword = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id=" + userId + "] not found")
                );

        if(isCurrentUserEqualToChangingUser(userForUpdatePassword)) {
            changeUserPassword(userPasswordDto.getNewPassword(), userPasswordDto.getOldPassword(), userForUpdatePassword);
        } else throw new BadRequestException("User [id" + userId + "] is not logged user");


        return userRepository.save(userForUpdatePassword);
    }

    private void changeUserPassword(char[] newPassword, char[] oldPassword, User userForUpdatePassword) {

        final CharBuffer newPasswordBuffer = CharBuffer.wrap(newPassword);

        if(passwordEncoder.matches(String.valueOf(oldPassword),String.valueOf(userForUpdatePassword.getPassword()))){
            userForUpdatePassword
                    .setPassword(passwordEncoder
                            .encode(newPasswordBuffer)
                            .toCharArray());
        } else {
            throw new BadRequestException("User old password is incorrect");
        }
    }

    private boolean isCurrentUserEqualToChangingUser(User userForUpdate){
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String mail;

        if(user instanceof UserDetails) {
            mail = ((UserDetails)user).getUsername();

        } else {
            mail = user.toString();
        }

        return userForUpdate.getMail().equals(mail);
    }

    public User saveNewLecturer(@Valid User lecturer) {
        if(isEmailUnique(lecturer.getMail())){
            CharBuffer passwordBuffer = CharBuffer.wrap(lecturer.getPassword());
            lecturer.setPassword(passwordEncoder.encode(passwordBuffer).toCharArray());
            lecturer.setType(LECTURER);

            return userRepository.save(lecturer);
        } else {
            throw new NotUniqueException("Email already exist");
        }
    }
}


