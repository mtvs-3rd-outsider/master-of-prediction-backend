package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass
public abstract class GenerateEntity {
    @Id
    @Column(name = "id")
    private long id;

    @PrePersist
    public void prePersist() {
        if (id == 0) {
            this.id = generateId();
        }
    }
    private long generateId() {
        return System.currentTimeMillis();
    }
}