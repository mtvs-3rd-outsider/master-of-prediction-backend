package com.outsider.masterofpredictionbackend.categorychannel.command.domain.service;

public interface ChannelSubscribeClient {

    public void publish( Long userId,Long channelId, boolean isUserChannel, String actionType);
}
