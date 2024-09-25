package com.outsider.masterofpredictionbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final RequestLoggingInterceptor requestLoggingInterceptor;
    private final MessageSource messageSource;


    public WebConfig(RequestLoggingInterceptor requestLoggingInterceptor, MessageSource messageSource) {
        this.requestLoggingInterceptor = requestLoggingInterceptor;
        this.messageSource = messageSource;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://125.132.216.190:3301")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // 쿼리 파라미터 이름 설정
        return interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
        registry.addInterceptor(localeChangeInterceptor());
        registry.addInterceptor(new CustomLocaleInterceptor(messageSource));
    }
    // CookieLocaleResolver를 사용하여 로케일을 쿠키에 저장
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.KOREA); // 기본 로케일 설정
        cookieLocaleResolver.setCookieName("lang"); // 쿠키 이름 설정
        cookieLocaleResolver.setCookieMaxAge(3600); // 쿠키 유효 시간 설정 (1시간)
        return cookieLocaleResolver;
    }

}