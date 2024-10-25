package com.outsider.masterofpredictionbackend.dm.command.domain.aggregate;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dm_threads")
public class DMThread {

    @EmbeddedId
    private DMThreadKey id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 사용자가 최신 메시지를 읽었는지 여부를 저장하는 필드
    @Column(name = "last_message_read", nullable = false)
    private Boolean lastMessageRead;

    // 사용자가 현재 DM 쓰레드에 접속 중인지 여부를 저장하는 필드
    @Column(name = "is_user_online", nullable = false)
    private Boolean isUserOnline;
    // 마지막 메시지 내용을 저장하는 필드
    @Column(name = "last_message", nullable = true, length = 500) // 필요에 따라 길이 조정
    private String lastMessage;

    // 마지막 메시지가 전송된 시간을 저장하는 필드
    @Column(name = "last_message_time", nullable = true)
    private LocalDateTime lastMessageTime;
    // 기본 생성자
    public DMThread() {}

    public DMThread(DMThreadKey id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.lastMessageRead = false; // 기본값은 읽지 않음
        this.isUserOnline = false; // 기본값은 오프라인 상태
    }

    // Getters and Setters
    public DMThreadKey getId() {
        return id;
    }

    public void setId(DMThreadKey id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getLastMessageRead() {
        return lastMessageRead;
    }

    public void setLastMessageRead(Boolean lastMessageRead) {
        this.lastMessageRead = lastMessageRead;
    }

    public Boolean getIsUserOnline() {
        return isUserOnline;
    }

    public void setIsUserOnline(Boolean isUserOnline) {
        this.isUserOnline = isUserOnline;
    }


    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(LocalDateTime lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }
}
