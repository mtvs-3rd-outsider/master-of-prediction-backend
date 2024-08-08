package com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate.embeded;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MyChannelSubscribeId implements Serializable {

    private Long userId;
    private Long channelId;

    public MyChannelSubscribeId() {
    }

    public MyChannelSubscribeId(Long userId, Long channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }

    // getters, setters, hashCode, equals methods

    public Long getUserId() {
        return userId;
    }



    public Long getChannelId() {
        return channelId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyChannelSubscribeId that = (MyChannelSubscribeId) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(channelId, that.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, channelId);
    }
}
