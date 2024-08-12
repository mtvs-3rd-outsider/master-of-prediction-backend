package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service;

public interface UpdateFollowerService {

    public void updateFollowerChannel(Long channelId, boolean isUserChannel, boolean isPlus);
}
