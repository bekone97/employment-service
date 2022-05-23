package com.godeltech.mastery.departmentservice.handling;


import com.godeltech.mastery.departmentservice.exception.JwtAuthenticationException;
import com.godeltech.mastery.departmentservice.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {


    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse resourceNotFoundExceptionHandler(ResourceNotFoundException exception) {
        return new ApiErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException exception) {
        return new ApiErrorResponse(exception.getCause().getLocalizedMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse illegalArgumentExceptionHandler(IllegalArgumentException exception) {
        return new ApiErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = JwtAuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse jwtAuthenticationExceptionHandler(JwtAuthenticationException exception) {
        return new ApiErrorResponse(exception.getMessage());
    }
}
