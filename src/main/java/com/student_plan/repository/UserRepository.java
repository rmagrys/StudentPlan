package com.student_plan.repository;

import com.student_plan.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user " +
            "WHERE user.firstName = :firstName")
    List<User> getAllByFirstName(String firstName);

    User getByFirstName(String firstName);

}
