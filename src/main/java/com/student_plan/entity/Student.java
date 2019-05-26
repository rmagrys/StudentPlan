package com.student_plan.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    @Size(min = 3, max = 60, message = "First name not valid")
    private String firstName;

    @Column
    @Size(min = 3, max = 60, message = "Last name not valid")
    private String lastName;

    @Column
    @Size(min = 5, max = 50, message = " Email not valid")
    @Email
    private String mail;
}
