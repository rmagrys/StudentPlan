package com.student_plan.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lecture {

    private final static String MESSAGE = "CONTENT_NOT_VALID";

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Size(min=5 , max = 40, message = Lecture.MESSAGE)
    private String lectureName;



    @OneToMany(mappedBy = "lecture", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<StudentLecture> studentLectures;
}
