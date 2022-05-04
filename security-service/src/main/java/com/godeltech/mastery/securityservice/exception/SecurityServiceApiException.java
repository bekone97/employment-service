package com.godeltech.mastery.securityservice.exception;

public abstract class SecurityServiceApiException extends RuntimeException {

    SecurityServiceApiException(String message) {
        super(message);
    }

}
