package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class MyChannelSubscribeId implements Serializable {

    private Long userId;
    private Long channelId;
    @Column(name = "is_user_channel", nullable = false)
    private Boolean isUserChannel;
    public MyChannelSubscribeId() {
    }

    public Boolean getUserChannel() {
        return isUserChannel;
    }

    public MyChannelSubscribeId(Long userId, Long channelId, Boolean isUserChannel) {
        this.userId = userId;
        this.channelId = channelId;
        this.isUserChannel = isUserChannel;
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
                Objects.equals(channelId, that.channelId) &&
                Objects.equals(isUserChannel, that.isUserChannel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, channelId,isUserChannel);
    }
}
