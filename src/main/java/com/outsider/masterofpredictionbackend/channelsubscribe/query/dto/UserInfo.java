package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserInfo {
    private String displayName;
    private Long userId;
    private String userName;
    private String userAvatarUrl;
    private boolean isFollowing; // 추가된 필드
    public UserInfo(String displayName, Long userId, String userName, String userAvatarUrl) {
        this.displayName = displayName;
        this.userId = userId;
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;

    }
    public UserInfo(String displayName, Long userId, String userName, String userAvatarUrl, boolean isFollowing) {
        this.displayName = displayName;
        this.userId = userId;
        this.userName = userName;
        this.userAvatarUrl = userAvatarUrl;
        this.isFollowing = isFollowing; // 새로운 필드 초기화
    }
    // Getters and Setters 생략
}
