package com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service.ChannelSubscribeEventConsumer;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service.ChannelSubscribeService;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.UpdateMyChannelFollowerCountService;

public interface UpdateFollowerCountClient {

    public void publish(Long channelId, boolean isUserChannel, boolean isPlus);
}
