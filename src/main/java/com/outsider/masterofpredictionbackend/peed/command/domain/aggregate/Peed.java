package com.outsider.masterofpredictionbackend.peed.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.peed.command.application.dto.PeedDTO;
import com.outsider.masterofpredictionbackend.peed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.peed.command.domain.aggregate.embedded.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_peed")
public class Peed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "peed_id")
    private long id;

    @Column(name = "peed_author_type", nullable = false)
    private String authorType;

    @Embedded
    private User user; // User 임베디드 객체

    @Embedded
    private Guest guest;

    @Column(name = "peed_title", nullable = false)
    private String title;

    @Column(name ="peed_content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "peed_created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "peed_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "peed_view_count", nullable = false)
    private int viewCount;

    @Column(name = "peed_channel_type", nullable = false)
    private String channelType; // mychannel 또는 categorychannel

    public Peed() {
    }

    public Peed(String authorType, User user, Guest guest, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, int viewCount, String channelType) {
        this.authorType = authorType;
        this.user = user;
        this.guest = guest;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.channelType = channelType;
    }

    // Constructor using DTO
    public Peed(PeedDTO dto) {
        this.authorType = dto.getAuthorType();
        this.user = dto.getUser();
        this.guest = dto.getGuest();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.channelType = dto.getChannelType();
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0;
    }

    public long getId() {
        return id;
    }

    public String getAuthorType() {
        return authorType;
    }

    public User getUser() {
        return user;
    }

    public Guest getGuest() {
        return guest;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public int getViewCount() {
        return viewCount;
    }

    public String getChannelType() {
        return channelType;
    }

    // Update method
    public void updatePeed(PeedDTO dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Peed{" +
                "id=" + id +
                ", authorType='" + authorType + '\'' +
                ", user=" + user +
                ", guest=" + guest +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", viewCount=" + viewCount +
                ", channelType='" + channelType + '\'' +
                '}';
    }
}
