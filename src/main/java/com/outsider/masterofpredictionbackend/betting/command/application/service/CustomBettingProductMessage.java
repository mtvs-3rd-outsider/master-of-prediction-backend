package com.outsider.masterofpredictionbackend.betting.command.application.service;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CustomBettingProductMessage {

    private final MessageSource messageSource;

    public CustomBettingProductMessage(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(BettingProductMessageCode code, Object... args) {
        Locale locale = LocaleContextHolder.getLocale(); // 현재 로케일 가져오기
        return messageSource.getMessage(code.getCode(), args, locale);
    }

}

