package com.outsider.masterofpredictionbackend.dm.command.infrastructure;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.dm.command.domain.service.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaNotificationClient implements NotificationClient {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendNotification(UpdateMessageDTO updateMessageDTO) throws JsonProcessingException {
        // 카프카 메시지 생성
        // NotificationDTO를 JSON으로 변환
        String notificationMessage = objectMapper.writeValueAsString(updateMessageDTO);
        // 카프카 토픽으로 메시지 전송
        kafkaTemplate.send("update-messages", notificationMessage);
        System.out.println("Notification sent to Kafka: " + notificationMessage);
    }
}
