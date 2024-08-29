package com.outsider.masterofpredictionbackend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EXAMPLE_ERROR(HttpStatus.BAD_REQUEST, "예외 예시", "Example exception"),
    
    /**배팅 댓글 예외*/
    BETTING_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"BETTING_COMMENT_01", "베팅 댓글을 찾을 수 없습니다."),
    LOGIN_REQUIRED(HttpStatus.NOT_FOUND,"AUTH-001", "로그인이 필요합니다.");


    private final HttpStatus status;
    private final String message;
    private final String errorType;

    ErrorCode(HttpStatus status, String errorType, String message) {
        this.status = status;
        this.errorType = errorType;
        this.message = message;
    }

}
