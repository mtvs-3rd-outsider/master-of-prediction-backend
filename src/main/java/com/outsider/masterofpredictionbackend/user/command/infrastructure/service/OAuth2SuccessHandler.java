package com.outsider.masterofpredictionbackend.user.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Profile("prod")
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil tokenProvider;
    private static final String URI = "/auth/success";
    @Value("${google.redirect.successfulUri}")
    private String successfulUri;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // accessToken, refreshToken 발급
        CustomUserDetail customUserDetail=  (CustomUserDetail) authentication.getPrincipal();
        String accessToken = tokenProvider.createAccessToken(new CustomUserInfoDTO(customUserDetail.getUser()));

        // 토큰 전달을 위한 redirect
        String redirectUrl = UriComponentsBuilder.fromUriString(successfulUri)
                .build().toUriString();
        Cookie authCookie = new Cookie("accessToken", accessToken);
        authCookie.setMaxAge(60 * 60 * 24 * 7);
        authCookie.setHttpOnly(false);  // Authorization Header 에 넣을려면 false로 가져와야됨
        authCookie.setSecure(true);
        authCookie.setDomain("master-of-prediction.shop"); // 쿠키가 유효한 경로 설정
        authCookie.setPath("/"); // 쿠키가 유효한 경로 설정
        authCookie.setAttribute("SameSite", "None"); // 크로스 오리진시 필요
        response.addCookie(authCookie);
        response.sendRedirect(redirectUrl);
    }
}