package com.student_plan;

import com.student_plan.dto.UserParamsDto;
import com.student_plan.dto.UserPasswordDto;
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

    static UserPasswordDto createUserPasswordDto(final char[] oldPassword, final char[] newPassword){

        return UserPasswordDto
                .builder()
                    .newPassword(newPassword)
                    .oldPassword(oldPassword)
                .build();
    }

    static UserParamsDto createUserParamsDto(final String firstName, final String lastName, final String mail){

        return UserParamsDto
                .builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .mail(mail)
                .build();
    }
}
