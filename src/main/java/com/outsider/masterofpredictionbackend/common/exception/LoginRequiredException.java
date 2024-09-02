package com.outsider.masterofpredictionbackend.common.exception;

import lombok.Getter;

@Getter
public class LoginRequiredException extends RootException {

    private final String logMessage;
    private final String clientMessage;
    /**
     * Instantiates a new Root exception.
     *
     * @param logMessage    시스템에 남길 로그 메시지
     * @param clientMessage 클라이언트에게 보낼 메시지
     */
    public LoginRequiredException(String logMessage, String clientMessage) {
        super(ErrorCode.LOGIN_REQUIRED, logMessage, clientMessage);
        this.logMessage = logMessage;
        this.clientMessage = clientMessage;
    }
}