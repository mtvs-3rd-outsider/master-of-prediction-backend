package com.outsider.masterofpredictionbackend.channelsubscribe.query.model;

import lombok.ToString;

import java.io.Serializable;

@ToString
public class ChannelSubscriptionId implements Serializable {

    private Long channelId;
    private boolean isUserChannel;

    // 기본 생성자
    public ChannelSubscriptionId() {}

    // 모든 필드를 받는 생성자
    public ChannelSubscriptionId(Long channelId, boolean isUserChannel) {
        this.channelId = channelId;
        this.isUserChannel = isUserChannel;
    }

    // Getter와 Setter
    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public boolean isUserChannel() {
        return isUserChannel;
    }

    public void setUserChannel(boolean userChannel) {
        isUserChannel = userChannel;
    }

    // equals()와 hashCode() 메서드 구현 (필수)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChannelSubscriptionId that = (ChannelSubscriptionId) o;

        if (isUserChannel != that.isUserChannel) return false;
        return channelId != null ? channelId.equals(that.channelId) : that.channelId == null;
    }

    @Override
    public int hashCode() {
        int result = channelId != null ? channelId.hashCode() : 0;
        result = 31 * result + (isUserChannel ? 1 : 0);
        return result;
    }
}
