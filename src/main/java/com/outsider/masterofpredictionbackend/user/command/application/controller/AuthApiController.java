package com.outsider.masterofpredictionbackend.user.command.application.controller;

import com.outsider.masterofpredictionbackend.user.command.application.dto.LoginRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.SignUpRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.RegistUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final AuthService authService;
    private final RegistUserService registUserService;

    public AuthApiController(AuthService authService, RegistUserService registUserService) {
        this.authService = authService;
        this.registUserService = registUserService;
    }
    @CrossOrigin(origins = "https://monitor.master-of-prediction.shop:3001", allowedHeaders = "*",allowCredentials = "true")
    @PostMapping("login")
    public void postMemberProfile(
            @Valid @RequestBody LoginRequestDTO request2,
            HttpServletResponse response
            , HttpServletRequest request
    ) throws IOException {

        String token = this.authService.login(request2);

        // 쿠키 생성
        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(false); // 클라이언트 측에서 자바스크립트로 접근 불가능하게 설정
        cookie.setSecure(true); // HTTPS 환경에서만 전송되도록 설정 (HTTPS를 사용하는 경우)
        cookie.setDomain("master-of-prediction.shop"); // 쿠키가 유효한 경로 설정
        cookie.setPath("/"); // 쿠키가 유효한 경로 설정
        cookie.setAttribute("SameSite","None");
        cookie.setMaxAge(7 * 24 * 60 * 60); // 쿠키 유효 기간 설정 (7일 예시)
        response.addCookie(cookie);

//        response.sendRedirect("https://monitor.master-of-prediction.shop:3001");
        // 리다이렉트
        // HTTP 200 OK 상태 코드 반환
        // HTTP 200 OK 상태 코드 설정
        response.setStatus(HttpServletResponse.SC_OK);

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody SignUpRequestDTO user ) {
        registUserService.registUser(user);
        return ResponseEntity.ok().build();
    }

}
