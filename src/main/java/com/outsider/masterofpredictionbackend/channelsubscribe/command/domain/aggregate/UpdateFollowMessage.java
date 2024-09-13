package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate;

import lombok.Data;

@Data
public class UpdateFollowMessage {
    private Long channelId;
    private boolean isUserChannel;
    private boolean isPlus;

    public UpdateFollowMessage(Long channelId, boolean isUserChannel, boolean isPlus) {
        this.channelId = channelId;
        this.isUserChannel = isUserChannel;
        this.isPlus = isPlus;
    }

    public Long getChannelId() {
        return channelId;
    }

    public boolean isUserChannel() {
        return isUserChannel;
    }

    public boolean isPlus() {
        return isPlus;
    }
}