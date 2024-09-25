package com.outsider.masterofpredictionbackend.notification.command.domain.service;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;


    private final KafkaTemplate<String, String> kafkaTemplate;

    public NotificationService(NotificationRepository notificationRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.notificationRepository = notificationRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    // 새로운 알림 생성
    public Notification createNotification(Long userId, NotificationType type, String title, String content) {
        Notification notification = new Notification();
        notification.setNotificationType(type);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setUserId(userId);

//        Notification savedNotification = notificationRepository.save(notification);

        // Kafka 토픽 생성 (userId와 notificationType을 조합)
        String topicName = generateTopicName(userId, type);

        // Kafka 메시지 발송
        Map<String, Object> message = new HashMap<>();
        message.put("userId", userId);
        message.put("title", title);
        message.put("content", content);
        message.put("type", type.toString());

        kafkaTemplate.send(topicName, message.toString());

        return notification;


    }
    // 토픽 이름 생성 메서드 (userId와 notificationType 조합)
    private String generateTopicName(Long userId, NotificationType type) {
        return "notification_" + userId + "_" + type.name();
    }

}
