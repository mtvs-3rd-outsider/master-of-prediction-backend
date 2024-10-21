package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.outsider.masterofpredictionbackend.user.query.tier.UserPointsProfitEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class NotificationListener {



    private final ObjectMapper objectMapper;
    private final NotificationService notificationService;
    @Autowired
    public NotificationListener(ObjectMapper objectMapper, NotificationService notificationService) {

        this.objectMapper = objectMapper;
        this.notificationService = notificationService;
    }

    // Kafka로부터 메시지를 받는 리스너 메서드
    @KafkaListener(topics = "profitability", groupId = "notification_group")
    public void listenNotification(String message) {
        // 받은 메시지를 처리하는 로직
        System.out.println("Received notification: " + message);

        // 메시지를 NotificationType 객체로 변환
        try {
            UserPointsProfitEvent userPointsChangeEvent = objectMapper.readValue(message, UserPointsProfitEvent.class);
            System.out.println("Parsed Notification: " + userPointsChangeEvent);
            notificationService.handleNotification(userPointsChangeEvent);

        } catch (JsonProcessingException e) {
            // 메시지 파싱 중 에러가 발생할 경우 처리
            System.err.println("Failed to parse notification: " + e.getMessage());
        }
    }


}
