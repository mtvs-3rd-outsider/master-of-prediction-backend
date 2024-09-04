package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@Document(collection = "my_channel_info")
public class MyChannelInfoViewModel {

    @Id
    private Long id;  // MongoDB의 기본 키로 사용할 필드
    private String name;
    private String username;
    private String userEmail;
    private String displayName;
    private String bio;
    private String location;
    private String website;
    private String birthdate;
    private String joinedDate;
    private String gender;
    private BigDecimal points;
    private int tier_level;
    private int transactions;
    private String profitRate;
    private String positionValue;
    private int tradeCount;
    private int followingCount;
    private int followersCount;




}