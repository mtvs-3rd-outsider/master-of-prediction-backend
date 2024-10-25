package com.outsider.masterofpredictionbackend.dm.command.domain.aggregate;


import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "chat_threads")
public class ChatThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long chatRoomId;  // 채팅방을 구분하는 고유 ID

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 마지막 메시지 내용을 저장하는 필드
    @Column(name = "last_message", nullable = true, length = 500)
    private String lastMessage;

    // 마지막 메시지가 전송된 시간을 저장하는 필드
    @Column(name = "last_message_time", nullable = true)
    private LocalDateTime lastMessageTime;

    // 채팅방의 참여자 목록을 나타내는 관계 설정
    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Participant> participants;
    // 그룹 채팅방인지 여부를 나타내는 필드
    @Column(name = "is_group_thread", nullable = false)
    private Boolean isGroupThread;
    public Boolean getIsGroupThread() {
        return isGroupThread;
    }

    public void setIsGroupThread(Boolean isGroupThread) {
        this.isGroupThread = isGroupThread;
    }
    // 기본 생성자
    public ChatThread() {
        this.createdAt = LocalDateTime.now();
    }

    public ChatThread(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
