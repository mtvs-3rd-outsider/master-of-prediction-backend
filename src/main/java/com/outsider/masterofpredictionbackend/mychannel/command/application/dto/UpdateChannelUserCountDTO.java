package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateChannelUserCountDTO {
    private Long channelId;
    private Boolean isPlus;

}
