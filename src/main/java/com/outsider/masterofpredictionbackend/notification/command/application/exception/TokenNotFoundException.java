package com.outsider.masterofpredictionbackend.notification.command.application.exception;

public class TokenNotFoundException extends RuntimeException {
    public TokenNotFoundException(String message) {
        super(message);
    }
}