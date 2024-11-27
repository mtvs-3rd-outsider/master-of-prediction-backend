package com.outsider.masterofpredictionbackend.notification.command.application.dto;

import com.outsider.masterofpredictionbackend.notification.command.domain.aggregate.NotificationType;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class NotificationDTO {
    private String content;
    private String title;
    private Long userId;
    private NotificationType type;
    private boolean isRead;
    private Map<String, String> additionalData; // 추가 데이터 저장 필드

    // 기본 생성자
    public NotificationDTO() {
        this.additionalData = new HashMap<>(); // 추가 데이터 초기화
    }

    public NotificationDTO(String content, String title, Long userId, NotificationType type, boolean isRead) {
        this.content = content;
        this.title = title;
        this.userId = userId;
        this.type = type;
        this.isRead = isRead;
        this.additionalData = new HashMap<>();
    }

    public NotificationDTO(String content, String title, Long userId, NotificationType type) {
        this(content, title, userId, type, false);
    }

    // Map 변환 메서드 (추가 데이터 포함)
    public Map<String, String> toMap() {
        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("content", this.content);
        dataMap.put("title", this.title);
        dataMap.put("userId", this.userId != null ? this.userId.toString() : null);
        dataMap.put("type", this.type != null ? this.type.toString() : null);
        dataMap.put("isRead", String.valueOf(this.isRead));

        // 추가 데이터 병합
        if (this.additionalData != null) {
            dataMap.putAll(this.additionalData);
        }

        return dataMap;
    }
}
