package com.student_plan.expections;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message){
        super(message);
    }
}
