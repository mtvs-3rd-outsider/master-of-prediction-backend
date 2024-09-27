package com.outsider.masterofpredictionbackend.common.constant;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public enum CustomHttpStatus {
    LOGIN_ERROR(403, "custom.http.status.login_error"); // 메시지 키를 저장

    private final int code;
    private final String messageKey;

    CustomHttpStatus(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public int value() {
        return this.code;
    }

    public String getMessage(MessageSource messageSource) {
        Locale locale = LocaleContextHolder.getLocale(); // 현재 로케일 가져오기
        return messageSource.getMessage(this.messageKey, null, locale); // 로케일에 맞는 메시지 반환
    }

    public static CustomHttpStatus fromCode(int code) {
        for (CustomHttpStatus status : CustomHttpStatus.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP Status Code: " + code);
    }
}