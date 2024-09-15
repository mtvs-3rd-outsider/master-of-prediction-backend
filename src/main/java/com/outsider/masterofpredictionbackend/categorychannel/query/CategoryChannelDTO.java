package com.outsider.masterofpredictionbackend.categorychannel.query;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryChannelDTO {
    private Long channelId;
    private String displayName;
    private String description;
    private String communityRule;
    private String imageUrl;
    private String bannerImg;
    private int userCount;
}
