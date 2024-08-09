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

import java.time.LocalDateTime;

@Service
public class ChannelUnSubscribeService {
    private final ChannelSubscribeRepository repository;
    private final UpdateFollowerService updateFollowerService;
    private final UpdateFollowingService updateFollowingService;
    @Autowired
    public ChannelUnSubscribeService(ChannelSubscribeRepository repository, UpdateFollowerService updateFollowerService, UpdateFollowingService updateFollowingService) {
        this.repository = repository;
        this.updateFollowerService = updateFollowerService;
        this.updateFollowingService = updateFollowingService;
    }

    @Transactional
    public void unsubscribe(ChannelSubscribeRequestDTO dto) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId(),dto.isUserChannel());
        ChannelSubscribe myChannelSubscribe = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        myChannelSubscribe.setActive(false);
        myChannelSubscribe.setExpirationDate(LocalDateTime.now());

        updateFollowingService.updateFollowingChannel(dto.getUserId(),dto.isUserChannel(),false);
        updateFollowerService.updateFollowerChannel(dto.getChannelId(),dto.isUserChannel(),false);

        repository.save(myChannelSubscribe);
    }
}
