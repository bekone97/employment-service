package com.godeltech.mastery.employeeservice.exception;

public abstract class EmployeeServiceApiException extends RuntimeException {

    EmployeeServiceApiException(String message) {
        super(message);
    }

}
