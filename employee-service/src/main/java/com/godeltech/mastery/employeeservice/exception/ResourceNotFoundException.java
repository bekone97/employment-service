package com.godeltech.mastery.employeeservice.exception;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Exception.NO_FOUNDED_PATTERN;

public class ResourceNotFoundException extends EmployeeServiceApiException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(NO_FOUNDED_PATTERN, resourceType.getSimpleName(), fieldName, fieldValue));
    }

}
