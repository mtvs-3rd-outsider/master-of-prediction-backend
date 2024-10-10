package com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Column(name = "feed_channel_id")
    private long channelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "feed_channel_type", nullable = false)
    private ChannelType channelType;

    @Override
    public String toString() {
        return "Channel{" +
                "channelId=" + channelId +
                ", channelType=" + channelType +
                '}';
    }
}
