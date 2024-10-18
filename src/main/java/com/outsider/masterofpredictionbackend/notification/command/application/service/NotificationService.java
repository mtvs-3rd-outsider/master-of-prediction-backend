package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper.NotificationMapper;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final NotificationMapper notifcationMapper;

    public NotificationService(NotificationRepository notificationRepository, FCMService fcmService, NotificationMapper notifcationMapper) {
        this.notificationRepository = notificationRepository;
        this.fcmService = fcmService;
        this.notifcationMapper = notifcationMapper;
    }
    // 파싱된 알림을 처리하는 메서드
    public void handleNotification(NotificationDTO notification) {
        Notification notificationEntity =notifcationMapper.ToEntity(notification);
        notificationRepository.save(notificationEntity);
        fcmService.sendNotificationToUser(notification);
    }
}
