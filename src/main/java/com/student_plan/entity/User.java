package com.student_plan.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private final static String message = "CONTENT_NOT_VALID";

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
    @Size(min = 5, max = 70, message = message)
    private char[] password;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private boolean enabled = true;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<StudentLecture> studentLectures;

    @OneToMany(mappedBy = "lecturer")
    private List<Lecture> lectures;

   /* public User(final Builder builder) {
        this.id = builder.id;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.mail = builder.email;
        this.password = builder.password;
        this.type = builder.type;

    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private char[] password;
        private Type type;


        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder mail(String email) {
            this.email = email;
            return this;
        }

        public Builder password(char[] password) {
            this.password = password;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }*/
}

