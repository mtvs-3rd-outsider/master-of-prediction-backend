package com.outsider.masterofpredictionbackend.notification.query;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDTO {

    private String id;
    private String content;
    private String title;
    private Long userId;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime createdAt; // 생성일
    // 기본 생성자
    public NotificationResponseDTO() {
    }

    public NotificationResponseDTO(String content, String title, Long userId, NotificationType type, boolean isRead) {
        this.content = content;
        this.title = title;
        this.userId = userId;
        this.type = type;
        this.isRead = isRead;
    }

    public NotificationResponseDTO(String content, String title, Long userId, NotificationType type) {
        this.content = content;
        this.title = title;
        this.userId = userId;
        this.type = type;
    }
}
