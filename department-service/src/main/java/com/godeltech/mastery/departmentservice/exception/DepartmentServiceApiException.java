package com.godeltech.mastery.departmentservice.exception;

public abstract class DepartmentServiceApiException extends RuntimeException {

    DepartmentServiceApiException(String message) {
        super(message);
    }

}
