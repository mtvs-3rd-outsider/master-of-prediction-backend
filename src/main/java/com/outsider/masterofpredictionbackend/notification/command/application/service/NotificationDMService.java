package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.UserPointsProfitEvent;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper.NotificationMapper;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationDMService {

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final NotificationMapper notifcationMapper;

    public NotificationDMService(NotificationRepository notificationRepository, FCMService fcmService, NotificationMapper notifcationMapper) {
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
    public void handleNotification(UpdateMessageDTO updateMessageDTO) {
        // Determine if profitability is positive or negative
        NotificationDTO notificationDTO = new NotificationDTO(
                updateMessageDTO.getLastMessage(),
                "새로운 메시지가 왔습니다.", // 타이틀 예시
                updateMessageDTO.getReceiverId(),
                NotificationType.MESSAGE,
                false // 메시지 읽음 여부
        );
        // Handle the notification (send it, store it, etc.)
        handleNotification(notificationDTO);
    }
    @Transactional
    public void updateNotificationIsRead(Long id, Boolean isRead) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(isRead);
        notificationRepository.save(notification);
    }
}
