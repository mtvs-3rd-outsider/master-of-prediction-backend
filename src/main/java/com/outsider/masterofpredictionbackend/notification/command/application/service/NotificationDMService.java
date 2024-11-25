package com.outsider.masterofpredictionbackend.notification.command.application.service;

import com.outsider.masterofpredictionbackend.dm.command.application.dto.DMNotificationDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.UserPointsProfitEvent;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper.NotificationMapper;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationDMService {

    private final NotificationRepository notificationRepository;
    private final FCMService fcmService;
    private final NotificationMapper notificationMapper;

    // 파싱된 알림을 처리하는 메서드
    public void handleNotification(NotificationDTO notification) {
        Notification notificationEntity =notificationMapper.toEntity(notification);
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

    /**
     * 알림 처리 메서드: NotificationDTO 기반
     */
    public void handleNotification(DMNotificationDTO notificationDTO) {
        // "New Message" 다국어 처리

        // 각 recipient에 대해 Notification 생성 및 저장
        List<String> recipients = notificationDTO.getRecipients();
        for (String recipientId : recipients) {
            NotificationDTO individualNotificationDTO = new NotificationDTO();
            individualNotificationDTO.setContent(notificationDTO.getContent());
            individualNotificationDTO.setTitle("New Message"); // 기본 제목 설정
            individualNotificationDTO.setUserId(Long.valueOf(recipientId)); // 수신자 ID 설정
            individualNotificationDTO.setType(NotificationType.MESSAGE);
            individualNotificationDTO.setRead(false); // 초기 상태는 읽지 않음

            // Notification 엔티티로 변환 및 저장
            Notification notification = notificationMapper.toEntity(individualNotificationDTO);
            notificationRepository.save(notification);

            // FCM으로 알림 전송
            fcmService.sendNotificationToUser(individualNotificationDTO);
        }
    }
    @Transactional
    public void updateNotificationIsRead(Long id, Boolean isRead) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(isRead);
        notificationRepository.save(notification);
    }
}
