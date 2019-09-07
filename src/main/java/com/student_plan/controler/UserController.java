package com.student_plan.controler;

import com.student_plan.dto.UserDto;
import com.student_plan.dto.UserDtoConverter;
import com.student_plan.entity.User;
import com.student_plan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        return userService
                .getAllUsers()
                .stream()
                .map(UserDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getOne(@PathVariable Long userId){
        User user = userService.getUserById(userId);

        return UserDtoConverter.toDto(user);
    }

    @GetMapping("/{userId}/presences")
    public UserDto getOneWithPresences(@PathVariable Long userId){
        User user = userService.getUserById(userId);

        return UserDtoConverter.allToDto(user);
    }
    @PostMapping
    public UserDto addNewUser(@RequestBody UserDto userDto){
        User user = UserDtoConverter.toEntity(userDto);

        return UserDtoConverter.toDto(userService.saveNewUser(user));

    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(
            @PathVariable Long userId,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String mail){

        return UserDtoConverter.toDto(userService.updateUser(firstName, lastName, mail, userId));
    }

    @PatchMapping("/{userId}/ascribe")
    public UserDto updateLecturesToStudent(
            @PathVariable Long userId ){
        User user = userService.getUserById(userId);

        return UserDtoConverter.toDto(user);
    }
}
