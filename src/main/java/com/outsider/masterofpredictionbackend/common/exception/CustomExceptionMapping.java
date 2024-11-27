package com.outsider.masterofpredictionbackend.common.exception;

import com.outsider.masterofpredictionbackend.notification.command.application.exception.TokenNotFoundException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

public enum CustomExceptionMapping {
    USERNAME_NOT_FOUND(UsernameNotFoundException.class, HttpStatus.NOT_FOUND, "custom.http.status.username_not_found"),
    BAD_CREDENTIALS(BadCredentialsException.class, HttpStatus.UNAUTHORIZED, "custom.http.status.bad_credentials"),
    TOKEN_NOT_FOUND(TokenNotFoundException.class, HttpStatus.NOT_FOUND, "custom.http.status.token_not_found"),
    SUBSCRIPTION_NOT_FOUND(SubscriptionNotFoundException.class, HttpStatus.NOT_FOUND, "custom.http.status.subscription_not_found"),
    USER_ALREADY_EXISTS(UserAlreadyExistsException.class, HttpStatus.CONFLICT, "custom.http.status.user_already_exists"),
    NO_RESOURCE_FOUND(NoResourceFoundException.class, HttpStatus.NOT_FOUND, "custom.http.status.no_resource_found"), // 추가
    NO_HANDLER_FOUND(NoHandlerFoundException .class, HttpStatus.NOT_FOUND, "custom.http.status.no_handler_found"); // 추가
    private final Class<? extends Exception> exceptionClass;
    private final HttpStatus httpStatus;
    private final String messageKey;

    CustomExceptionMapping(Class<? extends Exception> exceptionClass, HttpStatus httpStatus, String messageKey) {
        this.exceptionClass = exceptionClass;
        this.httpStatus = httpStatus;
        this.messageKey = messageKey;
    }

    public Class<? extends Exception> getExceptionClass() {
        return exceptionClass;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage(MessageSource messageSource) {
        return messageSource.getMessage(this.messageKey, null, LocaleContextHolder.getLocale());
    }

    public static CustomExceptionMapping fromException(Class<? extends Exception> exceptionClass) {
        for (CustomExceptionMapping mapping : values()) {
            if (mapping.getExceptionClass().equals(exceptionClass)) {
                return mapping;
            }
        }
        return null;
    }
}
