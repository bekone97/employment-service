package com.godeltech.mastery.securityservice.exception;

public class ResourceNotFoundException extends SecurityServiceApiException {
    public static final String PATTERN = "%s wasn't found by %s=%s";

    public ResourceNotFoundException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(PATTERN, resourceType, fieldName, fieldValue));
    }
}
