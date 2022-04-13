package com.godeltech.mastery.departmentservice.exception;


import static com.godeltech.mastery.departmentservice.utils.ConstantUtil.Exception.NO_FOUNDED_PATTERN;

public class ResourceNotFoundException extends DepartmentServiceApiException {

    public ResourceNotFoundException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(NO_FOUNDED_PATTERN, resourceType.getSimpleName(), fieldName, fieldValue));
    }

}
