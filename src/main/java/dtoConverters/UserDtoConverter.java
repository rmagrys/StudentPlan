package dtoConverters;


import com.student_plan.dto.UserDto;
import com.student_plan.entity.User;

import java.util.stream.Collectors;

public class UserDtoConverter {

    public static UserDto toDto(User user){

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMail(user.getMail());
        userDto.setType(user.getType());
        userDto.setEnabled(user.isEnabled());

        return userDto;
    }

    public static User toEntity(UserDto userDto){

        User user = new User();

        user.setId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMail(userDto.getMail());
        user.setType(userDto.getType());
        user.setPassword(userDto.getPassword());

        return user;
    }

    public static UserDto allToDto(User user){

        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMail(user.getMail());
        userDto.setType(user.getType());
        userDto.setPassword(userDto.getPassword());
        userDto.setStudentLecturesDto(
                user
                    .getStudentLectures()
                    .stream()
                    .map(StudentLectureDtoConverter::toDto)
                    .collect(Collectors.toList())
        );

        return userDto;
    }

}
