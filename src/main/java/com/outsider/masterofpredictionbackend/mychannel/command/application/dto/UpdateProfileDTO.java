package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;

import lombok.Data;

@Data
public class UpdateProfileDTO {
    private Long channelId;
    private String displayName;
    private String bio;
    private String location;
    private String website;
}
