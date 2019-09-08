package com.student_plan.expections.handlers;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ErrorResponse {

        private int code;
        private String message;
}
