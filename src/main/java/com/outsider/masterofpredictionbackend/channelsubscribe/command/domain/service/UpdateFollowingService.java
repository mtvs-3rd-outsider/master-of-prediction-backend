package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service;

public interface UpdateFollowingService {

    public void updateFollowingChannel(Long channelId, boolean isUserChannel, boolean isPlus);
}
