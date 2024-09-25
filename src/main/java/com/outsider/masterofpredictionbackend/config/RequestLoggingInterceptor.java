package com.outsider.masterofpredictionbackend.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 요청 정보를 로깅
        logger.info("Incoming request: {} {}", request.getMethod(), request.getRequestURI());

        // 쿼리 파라미터 로깅
        Map<String, String[]> parameters = request.getParameterMap();
        if (!parameters.isEmpty()) {
            logger.info("Query Parameters: {}", formatParameters(parameters));
        }

        // 요청 본문(body) 로깅 (POST, PUT 등의 경우)
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            ContentCachingRequestWrapper cachingRequest = new ContentCachingRequestWrapper(request);
            String requestBody = getRequestBody(cachingRequest);
            logger.info("Request Body: {}", requestBody);
        }

        return true; // true를 반환하면 컨트롤러로 요청이 전달됨
    }

    private String formatParameters(Map<String, String[]> parameterMap) {
        return parameterMap.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
                .collect(Collectors.joining(", "));
    }

    private String getRequestBody(ContentCachingRequestWrapper request) throws IOException {
        byte[] content = request.getContentAsByteArray();
        return new String(content, request.getCharacterEncoding());
    }
}
