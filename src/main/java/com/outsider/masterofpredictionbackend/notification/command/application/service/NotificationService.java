package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper.NotificationMapper;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.outsider.masterofpredictionbackend.user.query.tier.UserPointsProfitEvent;
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
    public void handleNotification(UserPointsProfitEvent userPointsProfitEvent) {
        // Determine if profitability is positive or negative
        String content;
        String title;
        int profitability = userPointsProfitEvent.getProfitability(); // Get the profitability points

        if (profitability >= 0) {
            // Positive profitability
            content = "예측에 성공하셨습니다. " + profitability + " 포인트의 수익을 얻었습니다.";
            title = "수익을 얻었습니다.";
        } else {
            // Negative profitability
            content = "예측에 실패하셨습니다. " + Math.abs(profitability) + " 포인트의 손실을 보았습니다.";
            title = "손실을 얻었습니다.";
        }

        // Create NotificationDTO based on the profitability
        NotificationDTO notification = new NotificationDTO(
                content,
                title,
                userPointsProfitEvent.getUserId(),
                NotificationType.RESULT // Assuming you have a corresponding NotificationType
        );

        // Handle the notification (send it, store it, etc.)
        handleNotification(notification);
    }

}
