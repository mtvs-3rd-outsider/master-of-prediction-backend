package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscriptionId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelInfo {
    private String displayName;
    private Long channelId;
    private String channelName;
    private String channelImageUrl;
    private boolean isUserChannel;
    private boolean isFollowing; // 추가된 필드
    public ChannelInfo(String displayName,Long channelId, String channelName, String channelImageUrl,boolean isUserChannel) {
        this.displayName = displayName;
        this.channelId = channelId;
        this.channelName = channelName;
        this.channelImageUrl = channelImageUrl;
        this.isUserChannel = isUserChannel;
    }
    public ChannelSubscriptionId getChannelSubscriptionId()
    {
        return new ChannelSubscriptionId(channelId,isUserChannel);
    }
    // Getters and Setters 생략
}
