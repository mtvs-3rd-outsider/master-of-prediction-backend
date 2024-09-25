package com.outsider.masterofpredictionbackend.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfig {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages"); // 메시지 파일의 기본 경로
        messageSource.setDefaultEncoding("UTF-8"); // 인코딩 설정
        messageSource.setUseCodeAsDefaultMessage(true); // 메시지가 없을 때 코드 자체를 반환
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.KOREAN); // 기본 로케일 설정
        return localeResolver;
    }
}
