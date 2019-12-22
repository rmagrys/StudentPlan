package com.student_plan.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Builder
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
    @Size(min = 5 , max = 50, message = Lecture.MESSAGE)
    private String lectureName;

    @Column
    private LocalDate date;

    @ManyToOne(targetEntity = User.class)
    private User lecturer;


    @OneToMany(mappedBy = "lecture", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<StudentLecture> studentLectures;
}
