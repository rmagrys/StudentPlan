package com.student_plan.expections;

public class NotUniqueException extends RuntimeException {

    public NotUniqueException(String message){
        super(message);
    }
}
