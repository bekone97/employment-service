package com.godeltech.mastery.employeeservice.handling;

import lombok.Data;

import java.util.List;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Exception.VALIDATION_ERROR;

@Data
public class ValidationErrorResponse extends ApiErrorResponse {

    private List<ValidationMessage> validationMessages;

    public ValidationErrorResponse(List<ValidationMessage> validationMessages) {
        super(VALIDATION_ERROR);
        this.validationMessages = validationMessages;
    }
}


