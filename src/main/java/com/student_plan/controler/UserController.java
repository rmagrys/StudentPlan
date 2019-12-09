package com.student_plan.controler;

import com.student_plan.dto.UserDto;
import com.student_plan.dto.UserDtoConverter;
import com.student_plan.dto.UserPasswordDto;
import com.student_plan.entity.User;
import com.student_plan.expections.NotFoundException;
import com.student_plan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<UserDto> getAll() {
        return userService
                .getAllUsers()
                .stream()
                .map(UserDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMINNNNNN', 'ADMIN')")
    public UserDto getOne(@PathVariable Long userId){
        User user = userService
                .getUserById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id=" + userId + "] not found"));


        return UserDtoConverter.allToDto(user);
    }

    @GetMapping("/{userId}/presences")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDto getOneWithPresences(@PathVariable Long userId){
        User user = userService
                .getUserById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id=" + userId + "] not found"));

        return UserDtoConverter.allToDto(user);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public UserDto addNewUser(@RequestBody @Valid UserDto userDto){

        User user = UserDtoConverter.toEntity(userDto);
        return UserDtoConverter.toDto(userService.saveNewUser(user));

    }

    @PostMapping("/lecturer")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDto addNewLecturer(@RequestBody @Valid UserDto userDto ){

        User lecturer = UserDtoConverter.toEntity(userDto);
        return UserDtoConverter.toDto(userService.saveNewLecturer(lecturer));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteById(userId);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UserDto updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserDto userDto){

        return UserDtoConverter.toDto(
                userService
                        .updateUser(
                            userDto.getFirstName(),
                            userDto.getLastName(),
                            userDto.getMail(),
                            userId)
                        );
    }

    @PatchMapping("/password/{userId}")
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN')")
    public UserDto updateUserPassword(
            @PathVariable Long userId,
            @RequestBody UserPasswordDto userPasswordDto){


        return UserDtoConverter.toDto(
                userService
                        .updateUserPassword(
                                userPasswordDto.getOldPassword(),
                                userPasswordDto.getNewPassword(),
                                userId)
                        );
    }

}
