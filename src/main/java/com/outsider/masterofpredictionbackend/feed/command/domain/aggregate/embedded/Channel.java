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
    private Long channelId = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "feed_channel_type")
    private ChannelType channelType = ChannelType.MYCHANNEL;;

    @Override
    public String toString() {
        return "Channel{" +
                "channelId=" + channelId +
                ", channelType=" + channelType +
                '}';
    }
}
