package com.outsider.masterofpredictionbackend.notification.command.domain.aggregate;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType; // 알림 유형

    private String title; // 알림 제목

    @Column(length = 1000)
    private String content; // 알림 내용

    private boolean isRead = false; // 읽음 여부

    private LocalDateTime createdAt = LocalDateTime.now(); // 생성일

    public void setAdditionalData(Map<String, String> additionalData) {
        this.additionalData = additionalData;
    }

    public Map<String, String> getAdditionalData() {
        return additionalData;
    }

    // 추가 데이터 필드
    @ElementCollection
    @CollectionTable(name = "notification_additional_data", joinColumns = @JoinColumn(name = "notification_id"))
    @MapKeyColumn(name = "data_key")
    @Column(name = "data_value")
    private Map<String, String> additionalData = new HashMap<>();


    private Long  userId; // 알림을 받는 사용자

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
