package com.godeltech.mastery.securityservice.exception;

public class TokenExpiredException extends SecurityServiceApiException {
    public static final String PATTERN = "%s is expired at %s=%s";

    public TokenExpiredException(Class<?> resourceType, String fieldName, Object fieldValue) {
        super(String.format(PATTERN, resourceType, fieldName, fieldValue));
    }
}
