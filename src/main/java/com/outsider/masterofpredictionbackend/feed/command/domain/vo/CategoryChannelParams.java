package com.outsider.masterofpredictionbackend.feed.command.domain.vo;
public class CategoryChannelParams {
    public final Long categoryId;

    public CategoryChannelParams(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }
}
