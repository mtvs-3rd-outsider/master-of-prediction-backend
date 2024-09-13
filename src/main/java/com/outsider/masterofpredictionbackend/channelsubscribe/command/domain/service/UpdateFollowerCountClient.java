package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service;

public interface UpdateFollowerCountClient {

    public void publish(Long channelId, boolean isUserChannel, boolean isPlus);
}
