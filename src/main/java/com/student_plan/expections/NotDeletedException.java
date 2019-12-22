package com.student_plan.expections;

public class NotDeletedException extends RuntimeException {
    public NotDeletedException(String message){
        super(message);
    }
}
