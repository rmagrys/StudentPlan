package com.student_plan.dto;


import com.student_plan.entity.User;

import java.util.stream.Collectors;

public class UserDtoConverter {

    public static UserDto toDto(User user){

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMail(user.getMail());

        return userDto;
    }

    public static User toEntity(UserDto userDto){

        User user = new User();

        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMail(userDto.getMail());

        return user;

    }

    public static UserDto allToDto(User user){

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getFirstName());
        userDto.setMail(user.getMail());
        userDto.setStudentLecturesDto(user
                .getStudentLectures()
                .stream()
                .map(StudentLectureDtoConverter::toDto)
                .collect(Collectors.toList()));

        return userDto;
    }
}
