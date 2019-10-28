package com.student_plan.expections;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message){
        super(message);
    }
}
