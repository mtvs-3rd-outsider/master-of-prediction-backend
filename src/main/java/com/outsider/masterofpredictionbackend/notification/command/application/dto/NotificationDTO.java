package com.outsider.masterofpredictionbackend.notification.command.application.dto;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import lombok.Data;

@Data
public class NotificationDTO {

    private String content;
    private String title;
    private Long userId;
    private NotificationType type;

    public NotificationDTO(String content, String title, Long userId, NotificationType type) {
        this.content = content;
        this.title = title;
        this.userId = userId;
        this.type = type;
    }
}
