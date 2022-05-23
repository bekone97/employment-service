package com.godeltech.mastery.securityservice.exception;

public class TokenNotActiveException extends SecurityServiceApiException {
    public static final String NOT_ACTIVE_MESSAGE = "Token isn't active";

    public TokenNotActiveException() {
        super(NOT_ACTIVE_MESSAGE);
    }
}
