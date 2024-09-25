package com.outsider.masterofpredictionbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Locale;

public class CustomLocaleInterceptor implements HandlerInterceptor {

    private final MessageSource messageSource;

    public CustomLocaleInterceptor(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 쿼리 파라미터에서 'lang' 값 가져오기
        String lang = request.getParameter("lang");
        Locale locale;

        // 'lang' 값이 있으면 해당 로케일로 설정, 없으면 기본 로케일
        if (lang != null && !lang.isEmpty()) {
            locale = Locale.forLanguageTag(lang);
        } else {
            locale = Locale.getDefault(); // 기본 로케일
        }

        // LocaleContextHolder에 로케일 설정
        LocaleContextHolder.setLocale(locale);

        return true;
    }
}
