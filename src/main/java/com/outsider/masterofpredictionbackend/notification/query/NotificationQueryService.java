package com.outsider.masterofpredictionbackend.notification.query;

import com.outsider.masterofpredictionbackend.notification.command.application.dto.NotificationDTO;
import com.outsider.masterofpredictionbackend.notification.command.application.dto.mapper.NotificationMapper;
import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationQueryService {

    private final NotificationMapper notificationMapper;

    private final NotificationRepository notificationRepository;

    public NotificationQueryService(NotificationMapper notificationMapper, NotificationRepository notificationRepository) {
        this.notificationMapper = notificationMapper;
        this.notificationRepository = notificationRepository;
    }

    // 사용자 ID를 기반으로 알림을 페이지네이션하여 조회
    public Page<NotificationResponseDTO> getNotificationsByUserId(Long userId, Pageable pageable) {
        // Fetch the notifications from the repository
        Page<Notification> notificationsPage = notificationRepository.findByUserId(userId, pageable);

        // Convert each Notification entity to NotificationDTO using the mapper
        return notificationsPage.map(notificationMapper::ToDTO);
    }
}
