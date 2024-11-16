package com.outsider.masterofpredictionbackend.dm.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // User와의 ManyToOne 관계 설정
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user; // 참여자 User 객체

    @ManyToOne
    @JoinColumn(name = "chat_room_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ChatThread thread;

//    @Column(name = "user_id", nullable = false)
//    private Long userId; // 참여자 ID

    @Column(name = "is_online", nullable = false)
    private Boolean isOnline;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive=true;

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Column(name = "last_read_time", nullable = true)
    private Instant lastReadTime = Instant.now();
    // Boolean 대신 Integer로 수정하여 읽지 않은 메시지 카운트로 사용
    @Column(name = "unread_message_count", nullable = false)
    private Integer unreadMessageCount = 0;

    public Integer getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(Integer unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public Participant() {}

    public Participant(ChatThread dmThread, User userId) {
        this.thread = dmThread;
        this.user = userId;
        this.isOnline = false; // 기본값은 오프라인
        this.isActive = true;
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

//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public Instant getLastReadTime() {
        return lastReadTime;
    }

    public void setLastReadTime(Instant lastReadTime) {
        this.lastReadTime = lastReadTime;
    }
}
