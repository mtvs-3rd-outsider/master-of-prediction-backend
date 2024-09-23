package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service;

public interface UpdateFollowingCountClient {

    public void publish(Long channelId, boolean isUserChannel, boolean isPlus);
}
