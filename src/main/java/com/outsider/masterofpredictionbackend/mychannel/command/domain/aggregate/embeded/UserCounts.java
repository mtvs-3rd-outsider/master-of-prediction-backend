package com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded;

import jakarta.persistence.Embeddable;

@Embeddable
public class UserCounts {
    private int followersCount;
    private int followingCount;

    protected UserCounts() {}

    public UserCounts(int followersCount, int followingCount) {
        validateCounts(followersCount, followingCount);
        this.followersCount = followersCount;
        this.followingCount = followingCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        validateSingleCount(followersCount, "Followers count");
        this.followersCount = followersCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        validateSingleCount(followingCount, "Following count");
        this.followingCount = followingCount;
    }




    private void validateCounts(int followersCount, int followingCount) {
        if (followersCount < 0 || followingCount < 0 ) {
            throw new IllegalArgumentException("Counts cannot be negative");
        }
    }

    private void validateSingleCount(int count, String countType) {
        if (count < 0) {
            throw new IllegalArgumentException(countType + " cannot be negative");
        }
    }

    @Override
    public String toString() {
        return "UserCounts{" +
                "followersCount=" + followersCount +
                ", followingCount=" + followingCount +
                '}';
    }
}
