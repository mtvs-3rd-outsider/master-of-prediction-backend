package com.outsider.masterofpredictionbackend.channelsubscribe.query.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionEvent {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("channel_id")
    private Long channelId;
    @JsonProperty("is_user_channel")
    private boolean userChannel;
    @JsonProperty("is_active")
    private boolean subscribed;

    public SubscriptionEvent() {}

    public SubscriptionEvent(Long userId, Long channelId, boolean userChannel, boolean isSubscribed) {
        this.userId = userId;
        this.channelId = channelId;
        this.userChannel = userChannel;
        this.subscribed = isSubscribed;
    }

    // Getter와 Setter
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getChannelId() { return channelId; }
    public void setChannelId(Long channelId) { this.channelId = channelId; }

    public boolean isUserChannel() { return userChannel; }
    public void setUserChannel(boolean userChannel) { this.userChannel = userChannel; }

    public boolean isSubscribed() { return subscribed; }
    public void setSubscribed(boolean isSubscribed) { this.subscribed = isSubscribed; }
}