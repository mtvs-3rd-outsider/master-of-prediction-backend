package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

public class ChannelSubscribeRequestDTO {
    private Long userId;
    private Long channelId;
    private Boolean isUserChannel;

    public ChannelSubscribeRequestDTO(Long userId, Long channelId, Boolean isUserChannel) {
        this.userId = userId;
        this.channelId = channelId;
        this.isUserChannel = isUserChannel;
    }

    // 기본 생성자, 게터/세터
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Boolean getIsUserChannel() {
        return isUserChannel;
    }

    public void setIsUserChannel(Boolean isUserChannel) {
        this.isUserChannel = isUserChannel;
    }

}
