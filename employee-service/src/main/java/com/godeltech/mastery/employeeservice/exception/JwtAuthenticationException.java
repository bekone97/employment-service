package com.godeltech.mastery.employeeservice.exception;

public class JwtAuthenticationException extends EmployeeServiceApiException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
