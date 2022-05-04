package com.godeltech.mastery.securityservice.exception;


public class NotUniqueResourceException extends SecurityServiceApiException {
    public final static String NOT_UNIQUE_PATTERN = "%s is already exists by %s=%s";

    public NotUniqueResourceException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(NOT_UNIQUE_PATTERN, resourceType.getSimpleName(), fieldName, fieldValue));
    }

}
