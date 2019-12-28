package com.student_plan.expections;

public class NotSavedException extends RuntimeException {

    public NotSavedException(String message){
        super(message);
    }
}
