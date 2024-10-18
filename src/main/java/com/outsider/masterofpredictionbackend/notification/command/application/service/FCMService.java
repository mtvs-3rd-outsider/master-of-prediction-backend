package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FCMService {
    private final FCMTokenService fcmTokenService;

    public FCMService(FCMTokenService fcmTokenService) {
        this.fcmTokenService = fcmTokenService;
    }

    // NotificationDTO를 받아 사용자의 모든 토큰으로 FCM 메시지를 전송
    // NotificationDTO를 받아 사용자의 모든 토큰으로 FCM 메시지를 전송
    public void sendNotificationToUser(NotificationDTO notificationDTO) {
        String userId = notificationDTO.getUserId().toString();
        Set<String> tokens = fcmTokenService.getTokens(userId); // 해당 유저의 모든 토큰을 가져옴

        if (tokens.isEmpty()) {
            System.out.println("No tokens found for userId: " + userId);
            return;
        }

        // 각각의 토큰에 대해 메시지를 전송
        for (String token : tokens) {
            sendNotification(notificationDTO, token);
        }
    }
    // 각 토큰에 맞게 메시지를 생성하고 전송
    public void sendNotification(NotificationDTO notificationDTO, String token) {
        Notification notification = Notification.builder()
                .setTitle(notificationDTO.getTitle())
                .setBody(notificationDTO.getContent())
                .build();

        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Successfully sent message: " + response);
        } catch (FirebaseMessagingException e) {
            System.out.println("Failed to send message to token: " + token);
            fcmTokenService.deleteToken(notificationDTO.getUserId().toString(), token); // 실패 시 토큰 삭제
        }
    }
}
