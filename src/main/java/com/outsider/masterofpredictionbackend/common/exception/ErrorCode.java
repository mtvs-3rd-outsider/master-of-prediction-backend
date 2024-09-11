package com.outsider.masterofpredictionbackend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    EXAMPLE_ERROR(HttpStatus.BAD_REQUEST, "예외 예시", "Example exception"),
    
    /**배팅 댓글 예외*/
    BETTING_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"BETTING_COMMENT_01", "베팅 댓글을 찾을 수 없습니다."),
    LOGIN_REQUIRED(HttpStatus.NOT_FOUND,"AUTH-001", "로그인이 필요합니다."),
    MISMATCH_USER(HttpStatus.UNAUTHORIZED,"AUTH-002", "사용자 정보가 일치하지 않습니다."),

    /**카테고리 채널 예외*/
    CATEGORY_CHANNEL_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,"CATEGORY_CHANNEL_COMMENT_01", "카테고리 채널 댓글을 찾을 수 없습니다."),
    CATEGORY_CHANNEL_COMMENT_PASSWORD_MIS_MATCH(HttpStatus.BAD_REQUEST,"CATEGORY_CHANNEL_COMMENT_02", "댓글 비밀번호가 일치하지 않습니다."),
    CATEGORY_CHANNEL_COMMENT_PASSWORD_NOT_FOUND(HttpStatus.NOT_FOUND,"CATEGORY_CHANNEL_COMMENT_03", "댓글 비밀번호가 존재하지 않습니다."),;


    private final HttpStatus status;
    private final String message;
    private final String errorType;

    ErrorCode(HttpStatus status, String errorType, String message) {
        this.status = status;
        this.errorType = errorType;
        this.message = message;
    }

}
