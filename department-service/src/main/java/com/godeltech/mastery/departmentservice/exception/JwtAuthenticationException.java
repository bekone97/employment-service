package com.godeltech.mastery.departmentservice.exception;

public class JwtAuthenticationException extends DepartmentServiceApiException {
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
