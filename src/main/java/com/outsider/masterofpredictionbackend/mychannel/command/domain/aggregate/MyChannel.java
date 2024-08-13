package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="MY_CHANNEL")
public class MyChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private DisplayName displayName;

    @Embedded
    private Bio bio;

    @Embedded
    private Website website;

    @Embedded
    private UserCounts userCounts;

    @Embedded
    private User user;

    public MyChannel() {
    }

    public MyChannel(DisplayName displayName, Bio bio, Website website,  User user) {
        this.displayName = displayName;
        this.bio = bio;
        this.website = website;
        this.userCounts = new UserCounts(0,0);
        this.user = user;
    }

    @Override
    public String toString() {
        return "MyChannel{" +
                "id=" + id +
                ", displayName=" + displayName +
                ", bio=" + bio +
                ", website=" + website +
                ", userCounts=" + userCounts +
                ", user=" + user +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName.getValue();
    }

    public String getBio() {
        return bio.getValue();
    }


    public String getWebsite() {
        return website.getUrl();
    }

    public UserCounts getUserCounts() {
        return userCounts;
    }

    public User getUser() {
        return user;
    }
    public void setFollowers(int userCounts) {
        this.userCounts.setFollowersCount(userCounts);
    }
    public void setFollowings(int userCounts) {
        this.userCounts.setFollowingCount(userCounts);
    }

    public void setDisplayName(DisplayName displayName) {
        this.displayName = displayName;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
