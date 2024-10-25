package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    // Kafka로부터 메시지를 받는 리스너 메서드
    @KafkaListener(topics = "update-messages", groupId = "notification_dm_group")
    public void listenNotification(String message) {
        // 받은 메시지를 처리하는 로직
        System.out.println("Received notification: " + message);

        // 메시지를 NotificationType 객체로 변환
        try {
            UpdateMessageDTO updateMessageDTO = objectMapper.readValue(message, UpdateMessageDTO.class);
            System.out.println("Parsed Notification: " + updateMessageDTO);
            notificationService.handleNotification(updateMessageDTO);

        } catch (JsonProcessingException e) {
            // 메시지 파싱 중 에러가 발생할 경우 처리
            System.err.println("Failed to parse notification: " + e.getMessage());
        }
    }


}
