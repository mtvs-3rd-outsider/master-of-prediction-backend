package com.outsider.masterofpredictionbackend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EXAMPLE_ERROR(HttpStatus.BAD_REQUEST, "예외 예시", "Example exception", "EXAMPLE");


    private final String description;
    private final HttpStatus status;
    private final String code;
    private final String errorType;

    ErrorCode(HttpStatus status, String errorType, String description, String code) {
        this.status = status;
        this.errorType = errorType;
        this.description = description;
        this.code = code;
    }

}
