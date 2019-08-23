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

    @Id
    @GeneratedValue
    private Long id;

    private final String message = "CONTENT_NOT_VALID";

    @Column
    @Size(min=5 , max = 40, message = message)
    private String lectureName;

    @OneToMany(mappedBy = "lecture", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<StudentLecture> studentLectures;
}
