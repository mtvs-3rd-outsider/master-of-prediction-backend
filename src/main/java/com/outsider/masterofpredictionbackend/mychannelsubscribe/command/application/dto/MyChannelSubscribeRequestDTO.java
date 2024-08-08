package com.outsider.masterofpredictionbackend.mychannelsubscribe.command.application.dto;


import lombok.Data;

@Data
public class MyChannelSubscribeRequestDTO {
    private Long userId;
    private Long userChannelId;
    private Long channelId;
}
