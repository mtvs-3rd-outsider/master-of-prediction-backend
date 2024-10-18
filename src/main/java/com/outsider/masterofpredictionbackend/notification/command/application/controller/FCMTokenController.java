package com.outsider.masterofpredictionbackend.notification.command.application.controller;



import com.outsider.masterofpredictionbackend.notification.command.application.dto.FCMTokenRequest;
import com.outsider.masterofpredictionbackend.notification.command.application.service.FCMService;
import com.outsider.masterofpredictionbackend.notification.command.application.service.FCMTokenService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@Controller
@RequestMapping("/api/v1/fcm")
public class FCMTokenController {
    private FCMTokenService tokenService;
    private FCMService fcmService;
    public FCMTokenController(FCMTokenService tokenService, FCMService fcmService) {
        this.tokenService = tokenService;
        this.fcmService = fcmService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> registerToken(@UserId CustomUserInfoDTO customUserInfoDTO, @RequestBody FCMTokenRequest token) {
        String userId = customUserInfoDTO.getUserId().toString();
        // Redis에 토큰과 이메일을 저장하는 로직
        tokenService.saveToken(userId , token.getToken());
        Set<String> tokens=tokenService.getTokens(userId);
        tokens.forEach(token2 ->fcmService.sendNotificationToUser(userId, token2, "알림 설정 완료", "로그인시에만 유지 됩니다.") );
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping()
    public ResponseEntity<String> removeToken(@UserId CustomUserInfoDTO customUserInfoDTO, @RequestBody FCMTokenRequest token) {
        tokenService.deleteToken(customUserInfoDTO.getUserId().toString(),token.getToken());
        return ResponseEntity.ok("FCM token removed successfully");
    }


    @GetMapping()
    public ResponseEntity<Set<String>> getToken(@UserId CustomUserInfoDTO customUserInfoDTO) {
        Set<String> token = tokenService.getTokens(customUserInfoDTO.getUserId().toString());
        if (token == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(token);
    }

}