package com.outsider.masterofpredictionbackend.channelsubscribe.query.service;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository.ChannelSubscribeCommandRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ChannelSubscribeQueryService {

    private final ChannelSubscribeCommandRepository repository;

    public ChannelSubscribeQueryService(ChannelSubscribeCommandRepository repository) {
        this.repository = repository;
    }

    // 구독 상태를 확인하는 메서드
    public boolean isSubscribed(Long userId, Long channelId, Boolean isUserChannel) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(userId, channelId, isUserChannel);
        Optional<ChannelSubscribe> subscription = repository.findById(id);
        return subscription.isPresent() && subscription.get().getIsActive();
    }
}
