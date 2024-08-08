package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.Data;

@Data
public class UpdateFollowingCountDTO {
    private Long channelId;
    private Boolean isPlus;
}
