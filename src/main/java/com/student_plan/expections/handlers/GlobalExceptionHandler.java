package com.student_plan.expections.handlers;

import com.student_plan.expections.BadRequestException;
import com.student_plan.expections.NotDeletedException;
import com.student_plan.expections.NotFoundException;

import com.student_plan.expections.NotUniqueException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundHandler(NotFoundException notFoundException){
        log.info("UNAVAILABLE | " + notFoundException.getMessage());
        return new ErrorResponse(404, notFoundException.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandler(BadRequestException badRequestException){
        log.info("BAD_REQUEST | " + badRequestException.getMessage());
        return new ErrorResponse(400, badRequestException.getMessage());
    }

    @ExceptionHandler(NotUniqueException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse notUniqueHandler(NotUniqueException notUniqueException){
        log.info("NOT_UNIQUE | " + notUniqueException.getMessage());
        return new ErrorResponse(409, notUniqueException.getMessage());
    }

    @ExceptionHandler(NotDeletedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse notDeletedHandler(NotDeletedException notDeletedException){
        log.info("NOT_DELETED | " + notDeletedException.getMessage());
        return new ErrorResponse(409,notDeletedException.getMessage());
    }

}

