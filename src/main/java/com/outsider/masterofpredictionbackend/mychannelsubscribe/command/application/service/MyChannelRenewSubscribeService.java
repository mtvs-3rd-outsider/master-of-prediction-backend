package com.outsider.masterofpredictionbackend.mychannelsubscribe.command.application.service;

import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.application.dto.MyChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate.MyChannelSubscribe;
import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.repository.MyChannelSubscribeRepository;
import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.service.UpdateFollowerService;
import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.service.UpdateFollowingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyChannelRenewSubscribeService {

    private final MyChannelSubscribeRepository repository;

    private final UpdateFollowerService updateFollowerService;
    private final UpdateFollowingService updateFollowingService;
    @Autowired
    public MyChannelRenewSubscribeService(MyChannelSubscribeRepository repository, UpdateFollowerService updateFollowerService, UpdateFollowingService updateFollowingService) {
        this.repository = repository;
        this.updateFollowerService = updateFollowerService;
        this.updateFollowingService = updateFollowingService;
    }

    @Transactional
    public void renewSubscription(MyChannelSubscribeRequestDTO dto) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId());
        MyChannelSubscribe myChannelSubscribe = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found"));
        myChannelSubscribe.setExpirationDate(null);
        myChannelSubscribe.setActive(true);

        updateFollowingService.updateFollowingMyChannel(dto.getUserChannelId(),true);
        updateFollowerService.updateFollowerMyChannel(dto.getChannelId(),true);

        repository.save(myChannelSubscribe);
    }
}
