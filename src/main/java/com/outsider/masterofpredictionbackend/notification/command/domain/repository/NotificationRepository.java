package com.outsider.masterofpredictionbackend.notification.command.domain.repository;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndIsReadFalse(Long userId); // 읽지 않은 알림 조회
}
