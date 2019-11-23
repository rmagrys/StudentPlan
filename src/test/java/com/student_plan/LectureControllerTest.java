package com.student_plan;

import com.student_plan.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class LectureControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private LectureRepository lectureRepository;



}
