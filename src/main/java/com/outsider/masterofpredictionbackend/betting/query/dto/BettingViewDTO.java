package com.outsider.masterofpredictionbackend.betting.query.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    public BettingViewDTO(Long userID, String userName, String displayName, String tierName, String userImg, String title, Long bettingId, Boolean isBlind) {
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

    public void addImgUrl(String imgUrl){
        this.imgUrls.add(imgUrl);
    }
}
