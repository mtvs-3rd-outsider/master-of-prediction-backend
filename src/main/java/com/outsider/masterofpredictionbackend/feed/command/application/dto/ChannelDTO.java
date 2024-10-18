package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

@Data
public class ChannelDTO {
    private Long channelId;
    private ChannelType channelType;
}
