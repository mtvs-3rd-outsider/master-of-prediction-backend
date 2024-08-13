package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto;


import lombok.Data;

@Data
public class ChannelSubscribeRequestDTO {
    private Long userId;
    private Long channelId;
    private boolean isUserChannel;
}
