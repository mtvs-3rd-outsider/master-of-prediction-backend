package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;


import lombok.Data;

@Data
public class UserUpdatedEvent {
    private String eventType;
    private UserData data;

    // Getters and Setters

    public static class UserData {
        private Long userId;
        private String displayName;
        private String userName;
        private String userAvatarUrl;

        // Getters and Setters
    }
}
