package com.outsider.masterofpredictionbackend.peed.command.domain.aggregate.embedded;

import jakarta.persistence.Embeddable;

@Embeddable
public class User {
    private Long userId;

    public User() {
    }

    public User(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                '}';
    }

}