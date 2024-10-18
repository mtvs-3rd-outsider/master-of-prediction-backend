package com.outsider.masterofpredictionbackend.notification.query;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import com.outsider.masterofpredictionbackend.notification.command.domain.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class NotificationQueryService {


    private final NotificationRepository notificationRepository;

    public NotificationQueryService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // 사용자 ID를 기반으로 알림을 페이지네이션하여 조회
    public Page<Notification> getNotificationsByUserId(String userId, Pageable pageable) {
        return notificationRepository.findByUserId(userId, pageable);
    }
}
