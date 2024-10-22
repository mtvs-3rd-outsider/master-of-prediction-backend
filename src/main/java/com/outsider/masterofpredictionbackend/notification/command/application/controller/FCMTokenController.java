package com.outsider.masterofpredictionbackend.notification.command.application.controller;



import com.outsider.masterofpredictionbackend.notification.command.application.dto.FCMTokenRequest;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.service.FCMService;
import com.outsider.masterofpredictionbackend.notification.command.application.service.FCMTokenService;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
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
    public ResponseEntity<Map<String, String>> registerToken(@UserId CustomUserInfoDTO customUserInfoDTO, @RequestBody FCMTokenRequest tokenRequest) {
        String userId = customUserInfoDTO.getUserId().toString();
        tokenService.saveToken(userId, tokenRequest.getToken());
        NotificationDTO notificationDTO = new NotificationDTO( "로그인시에만 유지 됩니다.","알림 설정 완료",Long.parseLong(userId), NotificationType.SYSTEM);
        fcmService.sendNotification(notificationDTO,tokenRequest.getToken());
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<String> removeToken(@UserId CustomUserInfoDTO customUserInfoDTO, @RequestBody FCMTokenRequest token) {
        if (customUserInfoDTO != null && customUserInfoDTO.getUserId() != null) {
            tokenService.deleteToken(customUserInfoDTO.getUserId().toString(), token.getToken());
            return ResponseEntity.ok("FCM token removed successfully");
        } else {
            return ResponseEntity.badRequest().body("User ID is missing or invalid");
        }
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