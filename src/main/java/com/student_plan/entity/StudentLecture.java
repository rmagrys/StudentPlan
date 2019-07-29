package com.student_plan.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "StudentLecture")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentLecture {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(targetEntity = Student.class)
    private Student student;

    @ManyToOne(targetEntity = Lecture.class)
    private Lecture lecture;

    @Column
    private Boolean presence;
}
