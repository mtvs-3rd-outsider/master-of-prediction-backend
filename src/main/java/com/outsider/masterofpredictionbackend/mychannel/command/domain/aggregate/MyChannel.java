package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.outsider.masterofpredictionbackend.common.BaseEntity;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.*;
import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name="MY_CHANNEL")
public class MyChannel extends BaseEntity {

    @Id
    @Column(name="channel_id")
    @JsonProperty("channel_id")
    private Long id;

    private String bio;

    private String website;

    @Embedded
    private UserCounts userCounts;


    @Lob
    @Column(name="banner_img",length = 1000)
    @JsonProperty("banner_img")
    private String bannerImg;


    public MyChannel() {

        this.userCounts = new UserCounts(0,0);
    }
    public MyChannel(Long userId) {
        this.id = userId;
        this.userCounts = new UserCounts(0,0);
    }
    public MyChannel(Long userId, String bio, String website) {
        this.id = userId;
        this.bio = bio;
        this.website = website;
        this.userCounts = new UserCounts(0,0);
    }

    @Override
    public String toString() {
        return "MyChannel{" +
                "id=" + id +
                ", bio=" + bio +
                ", website=" + website +
                ", userCounts=" + userCounts +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }


    public String getWebsite() {
        return website;
    }

    public UserCounts getUserCounts() {
        return userCounts;
    }


    public void setFollowers(int userCounts) {
        this.userCounts.setFollowersCount(userCounts);
    }
    public void setFollowings(int userCounts) {
        this.userCounts.setFollowingCount(userCounts);
    }


    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }
}
