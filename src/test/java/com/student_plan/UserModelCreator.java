package com.student_plan;

import com.student_plan.entity.Type;
import com.student_plan.entity.User;

class UserModelCreator {

    static User createUser(final String firstName, final String lastName, final String mail,
                           final char[] password, final Type type, final boolean enabled){

        return User
                .builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .mail(mail)
                    .password(password)
                    .type(type)
                    .enabled(enabled)
                .build();
    }
}
