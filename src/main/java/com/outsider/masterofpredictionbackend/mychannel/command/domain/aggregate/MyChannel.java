package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.*;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name="MY_CHANNEL")
public class MyChannel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private DisplayName displayName;

    @Embedded
    private Bio bio;

    @Embedded
    private Location location;

    private LocalDate joinDate;

    @Embedded
    private Website website;

    @Embedded
    private UserCounts userCounts;

    @Embedded
    private User user;


    public MyChannel() {
    }

    public MyChannel(DisplayName displayName, Bio bio, Location location, LocalDate joinDate, Website website, UserCounts userCounts, User user) {
        this.displayName = displayName;
        this.bio = bio;
        this.location = location;
        this.joinDate = joinDate;
        this.website = website;
        this.userCounts = userCounts;
        this.user = user;
    }

    @Override
    public String toString() {
        return "MyChannel{" +
                "id=" + id +
                ", displayName=" + displayName +
                ", bio=" + bio +
                ", location=" + location +
                ", joinDate=" + joinDate +
                ", website=" + website +
                ", userCounts=" + userCounts +
                ", user=" + user +
                '}';
    }

    public int getId() {
        return id;
    }

    public DisplayName getDisplayName() {
        return displayName;
    }

    public Bio getBio() {
        return bio;
    }

    public Location getLocation() {
        return location;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public Website getWebsite() {
        return website;
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

    public void setLocation(Location location) {

        this.location = location;
    }

    public void setWebsite(Website website) {
        this.website = website;
    }
}
