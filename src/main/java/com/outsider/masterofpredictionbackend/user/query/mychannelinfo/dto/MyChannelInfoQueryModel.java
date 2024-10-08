package com.outsider.masterofpredictionbackend.user.query.mychannelinfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor  // 기본 생성자 추가
@AllArgsConstructor // 모든 필드를 사용하는 생성자 추가
@Document(collection = "my_channel_info")
public class MyChannelInfoQueryModel {

    @Id
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("user_email")
    private String userEmail;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("user_location")
    private String location;

    @JsonProperty("website")
    private String website;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("joined_date")
    private String joinedDate;

    @JsonProperty("user_gender")
    private String gender;

    @JsonProperty("user_point")
    private BigDecimal points;
    @JsonProperty( "banner_img")
    private String bannerImg ;

    @JsonProperty("user_img")
    private String userImg ;

    @JsonProperty("tier_name")
    private String tierName;

    @JsonProperty("tier_level")
    private int tierLevel;

    @JsonProperty("transactions")
    private int transactions;

    @JsonProperty("profit_rate")
    private String profitRate;

    @JsonProperty("position_value")
    private String positionValue;

    @JsonProperty("trade_count")
    private int tradeCount;

    @JsonProperty("following_count")
    private int followingCount;

    @JsonProperty("followers_count")
    private int followersCount;

}