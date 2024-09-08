package com.outsider.masterofpredictionbackend.mychannel.command.application.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChannelUpdateDTO {

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("bio")
    private String bio;

    @JsonProperty("location")
    private String location;

    @JsonProperty("website")
    private String website;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty( "banner_img")
    private String bannerImg ;

    @JsonProperty( "user_img")
    private String userImg ;

    @JsonProperty( "age")
    private Integer age ;


}