package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateChannelUserCountDTO {
    private Long channelId;
    private Boolean isPlus;

}
