package com.outsider.masterofpredictionbackend.dm.command.domain.aggregate;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatThread thread;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 참여자 ID

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;

    @Column(name = "last_read_time", nullable = true)
    private LocalDateTime lastReadTime;
    // Boolean 대신 Integer로 수정하여 읽지 않은 메시지 카운트로 사용
    @Column(name = "unread_message_count", nullable = false)
    private Integer unreadMessageCount = 0;

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public Participant() {}

    public Participant(ChatThread dmThread, Long userId) {
        this.thread = dmThread;
        this.userId = userId;
        this.isOnline = false; // 기본값은 오프라인
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ChatThread getThread() {
        return thread;
    }

    public void setThread(ChatThread thread) {
        this.thread = thread;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public LocalDateTime getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(LocalDateTime lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
