package com.outsider.masterofpredictionbackend.categorychannel.command.application.dto;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;

public class CategoryChannelRegistRequestDTO {

    private String displayName;
    private String description;
    private String communityRule; // 추가 삭제가 자유롭도록 JSON 형식
    private CategoryChannelStatus categoryChannelStatus;

    public CategoryChannelRegistRequestDTO(String displayName, String description, String communityRule, CategoryChannelStatus categoryChannelStatus) {
        this.displayName = displayName;
        this.description = description;
        this.communityRule = communityRule;
        this.categoryChannelStatus = categoryChannelStatus;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCommunityRule() {
        return communityRule;
    }

    public void setCommunityRule(String communityRule) {
        this.communityRule = communityRule;
    }

    public CategoryChannelStatus getCategoryChannelStatus() {
        return categoryChannelStatus;
    }

    public void setCategoryChannelStatus(CategoryChannelStatus categoryChannelStatus) {
        this.categoryChannelStatus = categoryChannelStatus;
    }
}
