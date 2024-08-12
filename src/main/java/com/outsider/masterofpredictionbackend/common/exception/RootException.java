package com.outsider.masterofpredictionbackend.common.exception;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 공통 예외처리 객체
 */
@Getter
@Slf4j
public class RootException extends Exception{
    private ErrorCode errorCode;
    private final String logMessage;
    private final String clientMessage;
    private final Exception cause;


    /**
     * Instantiates a new Root exception.
     *
     * @param errorCode     에러 코드(예외 종류)
     * @param logMessage    시스템에 남길 로그 메시지
     * @param clientMessage 클라이언트에게 보낼 메시지
     * @param cause         발생한 예외
     */
    public RootException(ErrorCode errorCode, String logMessage, String clientMessage, Exception cause){
        this.errorCode = errorCode;
        this.logMessage = logMessage;
        this.clientMessage = clientMessage;
        this.cause = cause;
        log.error("예외 발생: {}, 발생 예외: {}", logMessage, cause.getClass().getName());
    }
}
