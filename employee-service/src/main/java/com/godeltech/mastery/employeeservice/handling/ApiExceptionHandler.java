package com.godeltech.mastery.employeeservice.handling;


import com.godeltech.mastery.employeeservice.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.net.ConnectException;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {



    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse resourceNotFoundExceptionHandler(HttpServletRequest request, ResourceNotFoundException exception) {
        log.error("The {}.There is no entity in database : {} and url of request : {}",
            exception.getClass().getSimpleName(),exception.getMessage(),request.getRequestURL());
        return new ApiErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse httpMessageNotReadableExceptionHandler(HttpServletRequest request,HttpMessageNotReadableException exception) {
        log.warn("The {}. Wrong income parameters :{} and url of request: {}",
               exception.getClass().getSimpleName(),exception.getMessage(),request.getRequestURL());
        return new ApiErrorResponse(exception.getCause().getLocalizedMessage());
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse illegalArgumentExceptionHandler(HttpServletRequest request,IllegalArgumentException exception) {
        log.error("The {}. Wrong parameters :{} and url of request: {} ",
                exception.getClass().getSimpleName(),exception.getMessage(),request.getRequestURL());
        return new ApiErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse methodArgumentNotValidExceptionHandler(HttpServletRequest request,MethodArgumentNotValidException exception){

        var errorResponse= new ValidationErrorResponse(exception.getBindingResult().getAllErrors().stream()
                        .map(FieldError.class::cast)
                        .map(error->new ValidationMessage(error.getField(),error.getDefaultMessage()))
                        .collect(Collectors.toList()));
        log.warn("The{}.Validation messages :{} and url of request :{}" ,
                exception.getClass().getSimpleName(),errorResponse.getValidationMessages(),request.getRequestURL());
        return errorResponse;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse constraintViolationExceptionHandler(HttpServletRequest request,ConstraintViolationException exception) {

        var errorResponse= new ValidationErrorResponse(exception.getConstraintViolations().stream()
                        .map(set->new ValidationMessage(set.getPropertyPath().toString(),set.getMessage()))
                        .collect(Collectors.toList()));
        log.warn("The {}.Validation messages :{} and url of request :{}",
               exception.getClass().getSimpleName(),errorResponse.getValidationMessages(),request.getRequestURL());

        return errorResponse;

    }

    @ExceptionHandler(value = ConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorResponse restClientException(HttpServletRequest request,ConnectException exception){
        log.error("The {}. Wrong parameters :{} and url of request: {} ",
                exception.getClass().getSimpleName(),exception.getMessage(),request.getRequestURL());
        return new ApiErrorResponse(exception.getMessage());
    }

}
