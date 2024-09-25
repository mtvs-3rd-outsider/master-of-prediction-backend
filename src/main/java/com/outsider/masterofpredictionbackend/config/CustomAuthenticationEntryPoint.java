package com.outsider.masterofpredictionbackend.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final MessageSource messageSource;

    public CustomAuthenticationEntryPoint(MessageSource messageSource) {
        this.messageSource = messageSource; // MessageSource 주입
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // 쿼리 파라미터에서 'lang' 값 가져오기
        String lang = request.getParameter("lang");

        // 로케일을 쿼리 파라미터 값에 따라 설정
        Locale locale = (lang != null && !lang.isEmpty()) ? Locale.forLanguageTag(lang) : Locale.getDefault();

        System.out.println("Locale: " + locale);

        // 로케일에 맞는 메시지 가져오기
        String errorMessage = messageSource.getMessage("authentication.failure", null, locale);

        // 응답 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"message\": \"" + errorMessage + "\"}");
    }
}