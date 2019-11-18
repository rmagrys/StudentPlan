package com.student_plan.service;


import com.student_plan.entity.User;
import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id){
        return userRepository.findById(id);
    }

    public User saveNewUser(User user){
        CharBuffer passwordBuffer = CharBuffer.wrap(user.getPassword());
        user.setPassword(passwordEncoder.encode(passwordBuffer).toCharArray());

        return userRepository.save(user);
    }

    public void deleteById(Long id){
        userRepository
                .findById(id)
                .orElseThrow(() ->
                    new NotFoundException("User [id=" + id + "] not found")
                );

        userRepository.deleteById(id);
    }

    public User updateUser(String firstName, String lastName, String mail, Long id){
        User userForUpdate = userRepository
                .findById(id)
                .orElseThrow(() ->
                    new NotFoundException("User [id=" + id + "] not found")
                );

        updateUserValues(firstName, lastName, mail, userForUpdate);

        return userRepository.save(userForUpdate);
    }

    private void updateUserValues(String firstName, String lastName, String mail, User user) {

        if(firstName != null)
            user.setFirstName(firstName);
        if(lastName != null)
            user.setLastName(lastName);
        if(mail != null)
            user.setMail(mail);
    }

    public User updateUserPassword(char[] oldPassword, char[] newPassword, Long userId) {

        User userForUpdatePassword = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id=" + userId + "] not found")
                );

        if(isCurrentUserEqualToChangingUser(userForUpdatePassword)) {
            changeUserPassword(newPassword, oldPassword, userForUpdatePassword);
        } else throw new BadRequestException("User [id" + userId + "] is not logged user");


        return userRepository.save(userForUpdatePassword);
    }

    private void changeUserPassword(char[] newPassword, char[] oldPassword, User userForUpdatePassword) {

        final CharBuffer oldPassBuffer = CharBuffer.wrap(oldPassword);
        final CharBuffer newPasswordBuffer = CharBuffer.wrap(newPassword);
        final char[] encodedOldPassword = passwordEncoder
                        .encode(oldPassBuffer)
                        .toCharArray();


        if(Arrays.equals(encodedOldPassword, userForUpdatePassword.getPassword())){
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
}


