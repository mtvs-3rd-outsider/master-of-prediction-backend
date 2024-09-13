package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import lombok.Data;

@Data
public class UserInfo {
    private String displayName;
    private Long userId;
    private String userName;
    private String userAvatarUrl;

    public UserInfo(String displayName, Long userId, String userName, String userAvatarUrl) {
        this.displayName = displayName;
        this.userId = userId;
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;
    }

    // Getters and Setters 생략
}
