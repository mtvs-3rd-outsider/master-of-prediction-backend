package com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.ManagerRole;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "CATEGORY_CHANNEL")
public class CategoryChannel {

    @Id
    @Column(name = "CATEGORY_CHANNEL_ID")
    private long id;

    public void setCategoryChannelId(long categoryChannelId) {
        this.id = categoryChannelId;
    }

    @Column(name = "DISPLAY_NAME")
    private String displayName;
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

    @OneToMany(mappedBy = "categoryChannel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryChannelManager> managers = new ArrayList<>();
    // 부매니저 리스트 조회
    public List<CategoryChannelManager> getAssistantManagers() {
        return managers.stream()
                .filter(manager -> manager.getRole() == ManagerRole.ASSISTANT_MANAGER)
                .collect(Collectors.toList());
    }
    // 소유자 조회
    public Optional<CategoryChannelManager> getOwner() {
        return managers.stream()
                .filter(manager -> manager.getRole() == ManagerRole.OWNER)
                .findFirst();
    }
    public List<CategoryChannelManager> getManagers() {
        return managers;
    }

    public void setManagers(List<CategoryChannelManager> managers) {
        this.managers = managers;
    }

    public void setCategoryChannelStatus(CategoryChannelStatus categoryChannelStatus) {
        this.categoryChannelStatus = categoryChannelStatus;
    }

    public CategoryChannel() {}

    public CategoryChannel(String displayName, String description, CommunityRule communityRule, CategoryChannelUserCounts categoryChannelUserCounts, CategoryChannelStatus categoryChannelStatus) {
        this.displayName = displayName;
        this.description = description;
        this.communityRule = communityRule;
        this.categoryChannelUserCounts = categoryChannelUserCounts;
        this.categoryChannelStatus = categoryChannelStatus;
    }

    public long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getOwnerUserId() {
        return managers.stream()
                .filter(manager -> manager.getRole() == ManagerRole.OWNER)
                .map(CategoryChannelManager::getUserId)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No owner found for CategoryChannel ID: " + id));
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
                "categoryChannelId=" + id +
                ", displayName='" + displayName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", communityRule='" + communityRule + '\'' +
                ", categoryChannelUserCounts=" + categoryChannelUserCounts +
                ", categoryChannelStatus=" + categoryChannelStatus +
                '}';
    }
}
