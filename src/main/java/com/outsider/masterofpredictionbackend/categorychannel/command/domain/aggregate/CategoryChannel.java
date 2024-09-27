package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "CATEGORY_CHANNEL")
public class CategoryChannel {

    @Id
    @Column(name = "CATEGORY_CHANNEL_ID")
    private long categoryChannelId;

    public void setCategoryChannelId(long categoryChannelId) {
        this.categoryChannelId = categoryChannelId;
    }

    @Column(name = "DISPLAY_NAME")
    private String displayName;

    @Column(name = "OWNER_USER_ID")
    private long ownerUserId;

    @Column(name = "IMAGE_URL")
    private String imageUrl;
    @Column(name = "BANNER_IMG")
    private String bannerImg;
    @Column(name = "DESCRIPTION")
    private String description;

    public String getBannerImg() {
        return bannerImg;
    }

    public void setBannerImg(String bannerImg) {
        this.bannerImg = bannerImg;
    }

    @Embedded
    private CommunityRule communityRule;

    @Embedded
    private CategoryChannelUserCounts categoryChannelUserCounts;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATRGORY_CHANNEL_STATUS")
    private CategoryChannelStatus categoryChannelStatus;

    public void setCategoryChannelStatus(CategoryChannelStatus categoryChannelStatus) {
        this.categoryChannelStatus = categoryChannelStatus;
    }

    public CategoryChannel() {}

    public CategoryChannel(String displayName, long ownerUserId,String description, CommunityRule communityRule, CategoryChannelUserCounts categoryChannelUserCounts, CategoryChannelStatus categoryChannelStatus) {
        this.displayName = displayName;
        this.ownerUserId = ownerUserId;
        this.description = description;
        this.communityRule = communityRule;
        this.categoryChannelUserCounts = categoryChannelUserCounts;
        this.categoryChannelStatus = categoryChannelStatus;
    }

    public long getCategoryChannelId() {
        return categoryChannelId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getOwnerUserId() {
        return ownerUserId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public CommunityRule getCommunityRule() {
        return communityRule;
    }

    public CategoryChannelUserCounts getCategoryChannelUserCounts() {
        return categoryChannelUserCounts;
    }

    public CategoryChannelStatus getCategoryChannelStatus() {
        return categoryChannelStatus;
    }

    @Override
    public String toString() {
        return "CategoryChannel{" +
                "categoryChannelId=" + categoryChannelId +
                ", displayName='" + displayName + '\'' +
                ", ownerUserId=" + ownerUserId +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", communityRule='" + communityRule + '\'' +
                ", categoryChannelUserCounts=" + categoryChannelUserCounts +
                ", categoryChannelStatus=" + categoryChannelStatus +
                '}';
    }
}
