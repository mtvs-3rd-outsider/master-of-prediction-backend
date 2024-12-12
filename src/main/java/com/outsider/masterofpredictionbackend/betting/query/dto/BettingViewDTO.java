package com.outsider.masterofpredictionbackend.betting.query.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.cglib.core.Local;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.ChannelDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BettingViewDTO {

    private Long userID;

    private String userName;

    private String displayName;

    private String tierName;

    private String userImg;

    private String title;

    private List<String> imgUrls;

    private Long bettingId;

    private Boolean isBlind;

    private String blindName;

    private LocalDateTime createdAt;

    private ChannelDTO channel;

    public BettingViewDTO(Long userID, String userName, String displayName,
                          String tierName, String userImg, String title,
                          Long bettingId, Boolean isBlind) {
        this.userID = userID;
        this.userName = userName;
        this.displayName = displayName;
        this.tierName = tierName;
        this.userImg = userImg;
        this.title = title;
        this.imgUrls = new ArrayList<>();
        this.bettingId = bettingId;
        this.isBlind = isBlind;
    }
    public BettingViewDTO(Long userID, String userName, String displayName,
                          String tierName, String userImg, String title,
                          Long bettingId, LocalDateTime createdAt) {
        this.userID = userID;
        this.userName = userName;
        this.displayName = displayName;
        this.tierName = tierName;
        this.userImg = userImg;
        this.title = title;
        this.imgUrls = new ArrayList<>();
        this.bettingId = bettingId;
        this.createdAt = createdAt;
    }
    public BettingViewDTO(Long userID, String userName, String displayName,
                          String tierName, String userImg, String title,
                          Long bettingId, Boolean isBlind, String blindName, LocalDateTime createdAt) {
        this.userID = userID;
        this.userName = userName;
        this.displayName = displayName;
        this.tierName = tierName;
        this.userImg = userImg;
        this.title = title;
        this.imgUrls = new ArrayList<>();
        this.bettingId = bettingId;
        this.isBlind = isBlind;
        this.blindName = blindName;
        this.createdAt = createdAt;
    }
    public BettingViewDTO(Long userID, String userName, String displayName,
                          String tierName, String userImg, String title,
                          Long bettingId, Boolean isBlind, String blindName, 
                          LocalDateTime createdAt, Long channelId, String channelDisplayName, 
                          CategoryChannelStatus categoryChannelStatus) {
        this.userID = userID;
        this.userName = userName;
        this.displayName = displayName;
        this.tierName = tierName;
        this.userImg = userImg;
        this.title = title;
        this.imgUrls = new ArrayList<>();
        this.bettingId = bettingId;
        this.isBlind = isBlind;
        this.blindName = blindName;
        this.createdAt = createdAt;
        
        this.channel = new ChannelDTO(channelId, channelDisplayName, ChannelType.CATEGORYCHANNEL);
    }

    public void addImgUrl(String imgUrl){
        this.imgUrls.add(imgUrl);
    }
}
