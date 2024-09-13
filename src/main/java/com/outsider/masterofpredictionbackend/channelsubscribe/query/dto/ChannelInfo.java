package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import lombok.Data;

@Data
public class ChannelInfo {
    private String displayName;
    private Long channelId;
    private String channelName;
    private String channelImageUrl;

    public ChannelInfo(String displayName,Long channelId, String channelName, String channelImageUrl) {
        this.displayName = displayName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelImageUrl = channelImageUrl;
    }

    // Getters and Setters 생략
}
