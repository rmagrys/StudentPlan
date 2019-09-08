package com.student_plan.service;


import com.student_plan.entity.User;
import com.student_plan.expections.NotFoundException;
import com.student_plan.repository.LectureRepository;
import com.student_plan.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() ->
                    new NotFoundException("User [id=" + id + "] not found")
                );
    }

    public User saveNewUser(User user){
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
}
