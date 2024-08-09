package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository.ChannelSubscribeRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowerService;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelRenewSubscribeService {

    private final ChannelSubscribeRepository repository;

    private final UpdateFollowerService updateFollowerService;
    private final UpdateFollowingService updateFollowingService;
    @Autowired
    public ChannelRenewSubscribeService(ChannelSubscribeRepository repository, UpdateFollowerService updateFollowerService, UpdateFollowingService updateFollowingService) {
        this.repository = repository;
        this.updateFollowerService = updateFollowerService;
        this.updateFollowingService = updateFollowingService;
    }

    @Transactional
    public void renewSubscription(ChannelSubscribeRequestDTO dto) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId(),dto.isUserChannel());
        ChannelSubscribe myChannelSubscribe = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        myChannelSubscribe.setExpirationDate(null);
        myChannelSubscribe.setActive(true);

        updateFollowingService.updateFollowingChannel(dto.getUserId(),dto.isUserChannel(),true);
        updateFollowerService.updateFollowerChannel(dto.getChannelId(),dto.isUserChannel(),true);

        repository.save(myChannelSubscribe);
    }
}
