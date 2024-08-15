package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository.ChannelSubscribeCommandRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowerService;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ChannelSubscribeService {

    private final ChannelSubscribeCommandRepository repository;
    private final UpdateFollowerService  updateFollowerService;
    private final UpdateFollowingService updateFollowingService;

    @Autowired
    public ChannelSubscribeService(ChannelSubscribeCommandRepository repository, UpdateFollowerService updateFollowerService, UpdateFollowingService updateFollowingService) {
        this.repository = repository;
        this.updateFollowerService = updateFollowerService;
        this.updateFollowingService = updateFollowingService;
    }

    @Transactional
    public void subscribe(ChannelSubscribeRequestDTO dto) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId(),dto.isUserChannel());
        ChannelSubscribe myChannelSubscribe = new ChannelSubscribe(
                id,
                LocalDateTime.now(),
                null,
                true
        );
        updateFollowingService.updateFollowingChannel(dto.getUserId(),dto.isUserChannel(),true);
        updateFollowerService.updateFollowerChannel(dto.getChannelId(),dto.isUserChannel(),true);

        repository.save(myChannelSubscribe);
    }

}
