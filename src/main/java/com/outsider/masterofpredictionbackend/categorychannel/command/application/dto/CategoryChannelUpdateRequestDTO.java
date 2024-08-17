package com.outsider.masterofpredictionbackend.categorychannel.command.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryChannelUpdateRequestDTO {

    private long id;
    private String displayName;
    private String description;
    private String communityRule;

}
