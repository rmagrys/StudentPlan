package com.student_plan;

import com.student_plan.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void afterEachTest(){
        cleanDatabase();
    }

    private void cleanDatabase() {
        userRepository.deleteAll();
    }
}
