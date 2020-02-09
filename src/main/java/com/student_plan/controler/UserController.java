package com.student_plan.controler;

import com.student_plan.dto.UserDto;
import dtoConverters.UserDtoConverter;
import com.student_plan.dto.UserParamsDto;
import com.student_plan.dto.UserPasswordDto;
import com.student_plan.entity.User;
import com.student_plan.expections.NotFoundException;
import com.student_plan.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','LECTURER')")
    @ApiOperation(value = "Get all users", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved users list"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 404, message = "No users in the database"),
            @ApiResponse(code = 403, message = "Request forbidden")

    })
    public List<UserDto> getAll() {
        log.info("Fetching all users");
        return userService
                .getAllUsers()
                .stream()
                .map(UserDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LECTURER')")
    @ApiOperation(value = "Get user by id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden")
    })
    public UserDto getOne(@ApiParam(value ="User id", required = true) @PathVariable Long userId){
        log.info("Fetching user by id [id = " + userId + "]");
        User user = userService
                .getUserById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id= " + userId + "] not found"));


        return UserDtoConverter.allToDto(user);
    }

    @GetMapping("/{userId}/presences")
    @PreAuthorize("hasAnyAuthority('ADMIN','LECTURER')")
    @ApiOperation(value = "Get user with presences", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved user"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden")
            })
    public UserDto getOneWithPresences(@ApiParam(value = "User id", required = true)@PathVariable Long userId){
        log.info("Fetching user by id [id = " + userId + " ] with presences");
        User user = userService
                .getUserById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User [id=" + userId + "] not found"));

        return UserDtoConverter.allToDto(user);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @ApiOperation(value = "Add new user", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new user"),
            @ApiResponse(code = 400, message = "Wrong data format"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 409, message = "Mail already exists")
    })
    public UserDto addNewUser(@ApiParam(value = "New user container", required = true) @RequestBody @Valid UserDto userDto){
    log.info("Adding new user to database");
        User user = UserDtoConverter.toEntity(userDto);
        return UserDtoConverter.toDto(userService.saveNewUser(user));

    }

    @PostMapping("/lecturer")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Add new lecturer", response =  User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added new lecturer"),
            @ApiResponse(code = 400, message = "Wrong data format"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 409, message = "Mail already exist")
    })
    public UserDto addNewLecturer(@ApiParam(value = "New lecturer container", required = true) @RequestBody @Valid UserDto userDto ){
        log.info("Adding new lecturer to database");
        User lecturer = UserDtoConverter.toEntity(userDto);

        return UserDtoConverter.toDto(userService.saveNewLecturer(lecturer));
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Delete user by id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted user"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Entity not found"),

    })
    public void deleteUser(@ApiParam(value = "User id", required = true) @PathVariable Long userId){
        log.info("Deleting user from database");

        userService.deleteById(userId);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ApiOperation(value = "Update user by id", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user params"),
            @ApiResponse(code = 400, message = "Wrong data format"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Entity not found"),
            @ApiResponse(code = 409, message = "Mail already exist")
    })
    public UserDto updateUser(
            @ApiParam(value = "User id", required = true) @PathVariable Long userId,
            @ApiParam(value = "User params updating container", required = true) @Valid @RequestBody UserParamsDto userParamsDto){
        log.info("Updating user params by id [id = " + userId + "]");

        return UserDtoConverter.toDto(
                userService.updateUser(userParamsDto, userId));
    }

    @PatchMapping("/password/{userId}")
    @PreAuthorize("hasAnyAuthority('STUDENT','ADMIN','LECTURER')")
    @ApiOperation(value = "Update user password", response = User.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully updated user password"),
            @ApiResponse(code = 400, message = "Wrong password, data format"),
            @ApiResponse(code = 401, message = "Request is unauthorized"),
            @ApiResponse(code = 403, message = "Request forbidden"),
            @ApiResponse(code = 404, message = "Entity not found"),
    })
    public UserDto updateUserPassword(
            @ApiParam(value = "User id", required =  true) @PathVariable Long userId,
            @ApiParam(value = "User password updating container", required = true) @Valid @RequestBody UserPasswordDto userPasswordDto){
        log.info("Updating user password by id [id = " + userId + "]");
        return UserDtoConverter.toDto(
                userService.updateUserPassword(userPasswordDto,userId));
    }

}
