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

    // 기본 생성자
    public DMThread() {}

    public DMThread(DMThreadKey id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
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
}
