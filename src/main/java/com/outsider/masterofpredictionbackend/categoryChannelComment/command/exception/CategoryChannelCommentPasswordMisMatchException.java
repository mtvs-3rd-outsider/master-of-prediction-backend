package com.outsider.masterofpredictionbackend.categoryChannelComment.command.exception;

import com.outsider.masterofpredictionbackend.common.exception.ErrorCode;
import com.outsider.masterofpredictionbackend.common.exception.RootException;
import lombok.Getter;

@Getter
public class CategoryChannelCommentPasswordMisMatchException extends RootException {

    private final String logMessage;
    private final String clientMessage;

    /**
     * Instantiates a new Root exception.
     *
     * @param logMessage    시스템에 남길 로그 메시지
     * @param clientMessage 클라이언트에게 보낼 메시지
     */
    public CategoryChannelCommentPasswordMisMatchException(String logMessage, String clientMessage) {

        super(ErrorCode.CATEGORY_CHANNEL_COMMENT_PASSWORD_MIS_MATCH, logMessage, clientMessage);
        this.logMessage = logMessage;
        this.clientMessage = clientMessage;
    }
}
