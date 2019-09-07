package com.student_plan.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private final String message = "CONTENT_NOT_VALID";

    @Column
    @Size(min = 3, max = 60, message = message)
    private String firstName;

    @Column
    @Size(min = 3, max = 60, message = message)
    private String lastName;

    @Column
    @Size(min = 5, max = 50, message = message)
    @Email
    private String mail;

    @Column
    @Size(min = 5, max = 50, message = message)
    private char[] password;

    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    @JsonBackReference
    private List<StudentLecture> studentLectures;

}
