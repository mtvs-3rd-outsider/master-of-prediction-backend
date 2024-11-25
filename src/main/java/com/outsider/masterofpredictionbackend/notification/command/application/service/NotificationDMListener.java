package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.DMNotificationDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.UserPointsProfitEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class NotificationDMListener {
    private final ObjectMapper objectMapper;
    private final NotificationDMService notificationService;

    @KafkaListener(topics = "user-notifications", groupId = "notification_dm_group")
    public void listenNotification(String message) {
        System.out.println("Received notification: " + message);

        try {
            // Kafka 메시지를 DMNotificationDTO로 변환
            DMNotificationDTO dmNotificationDTO = objectMapper.readValue(message, DMNotificationDTO.class);

            System.out.println("Parsed DMNotificationDTO: " + dmNotificationDTO);

            // 알림 서비스 호출
            notificationService.handleNotification(dmNotificationDTO);

        } catch (JsonProcessingException e) {
            System.err.println("Failed to parse notification message: " + e.getMessage());
        }
    }
}
