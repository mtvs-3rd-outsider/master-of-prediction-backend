package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChannelInfo {
    private String displayName;
    private Long channelId;
    private String channelName;
    private String channelImageUrl;
    private boolean isUserChannel;
    private Boolean isFollowing; // 추가된 필드
    public ChannelInfo(String displayName,Long channelId, String channelName, String channelImageUrl,boolean isUserChannel) {
        this.displayName = displayName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelImageUrl = channelImageUrl;
        this.isUserChannel = isUserChannel;
    }
    public ChannelInfo(String displayName, Long channelId, String channelName, String channelImageUrl, boolean isUserChannel, boolean isFollowing) {
        this.displayName = displayName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelImageUrl = channelImageUrl;
        this.isUserChannel = isUserChannel;
        this.isFollowing = isFollowing; // 새로운 필드 초기화
    }
    // Getters and Setters 생략
}
