package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.Data;

@Data
public class MyChannelUpdateRequestDTO {
    private Long channelId;
    private String displayName;
    private String bio;
    private String website;
}
