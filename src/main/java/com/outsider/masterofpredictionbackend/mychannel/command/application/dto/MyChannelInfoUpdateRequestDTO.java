package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyChannelInfoUpdateRequestDTO {
    private Long userId;
    private String bio;
    private String website;
    private String bannerImg;

}
