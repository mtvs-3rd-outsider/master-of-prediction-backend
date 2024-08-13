package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.Data;

@Data
public class MyChannelInfoUpdateRequestDTO {
    private Long channelId;
    private String displayName;
    private String bio;
    private String website;
}
