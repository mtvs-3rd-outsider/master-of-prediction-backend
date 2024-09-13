package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.*;
import jakarta.persistence.*;

@Entity
@Table(name="MY_CHANNEL")
public class MyChannel {

    @Id
    @Column(name="channel_id")
    private Long id;

    @Embedded
    private Bio bio;

    @Embedded
    private Website website;

    @Embedded
    private UserCounts userCounts;


    @Lob
    @Column(name="banner_img",length = 1000)
    private String bannerImg;


    public MyChannel() {

        this.userCounts = new UserCounts(0,0);
    }
    public MyChannel(Long userId) {
        this.id = userId;
        this.userCounts = new UserCounts(0,0);
    }
    public MyChannel(Long userId, Bio bio, Website website) {
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

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio.getValue();
    }


    public String getWebsite() {
        return website.getWebsite();
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


    public void setBio(Bio bio) {
        this.bio = bio;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }
}
