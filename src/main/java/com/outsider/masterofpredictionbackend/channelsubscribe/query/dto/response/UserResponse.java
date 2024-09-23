package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response;

import lombok.Data;

@Data

public class UserResponse {
    private String correlationId;
    private Long userId;
    private String userName;
    private String displayName;
    private String userAvatarUrl;

    public UserResponse(String correlationId, Long userId, String userName, String displayName, String userAvatarUrl) {
        this.correlationId = correlationId;
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.userAvatarUrl = userAvatarUrl;
    }

    // 생성자, getter, setter
}
