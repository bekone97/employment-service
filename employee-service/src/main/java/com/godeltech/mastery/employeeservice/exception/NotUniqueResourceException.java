package com.godeltech.mastery.employeeservice.exception;

import static com.godeltech.mastery.employeeservice.utils.ConstantUtil.Exception.NOT_UNIQUE_PATTERN;

public class NotUniqueResourceException extends EmployeeServiceApiException {
    public NotUniqueResourceException(String message) {
        super(message);
    }

    public NotUniqueResourceException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(NOT_UNIQUE_PATTERN, resourceType.getSimpleName(), fieldName, fieldValue));
    }

}
