package com.outsider.masterofpredictionbackend.common.exception;

import lombok.Data;

@Data
public class ErrorResponse {
    private Integer status;

    private String errorType;

    private String message;
}
