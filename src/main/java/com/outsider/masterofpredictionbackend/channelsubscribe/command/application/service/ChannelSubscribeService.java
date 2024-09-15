package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository.ChannelSubscribeCommandRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowerCountClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowingCountClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChannelSubscribeService {

    private final ChannelSubscribeCommandRepository repository;
    private final UpdateFollowerCountClient updateFollowerService;
    private final UpdateFollowingCountClient updateFollowingService;

    @Autowired
    public ChannelSubscribeService(
            ChannelSubscribeCommandRepository repository,
            UpdateFollowerCountClient updateFollowerService,
            UpdateFollowingCountClient updateFollowingService
    ) {
        this.repository = repository;
        this.updateFollowerService = updateFollowerService;
        this.updateFollowingService = updateFollowingService;
    }
    @Transactional
    public void manageSubscription(ChannelSubscribeRequestDTO dto,String action) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId(), dto.getIsUserChannel());
        Optional<ChannelSubscribe> optionalSubscribe = repository.findById(id);
        ChannelSubscribe channelSubscribe = optionalSubscribe.orElse(null);

        if ("subscribe".equals(action)) {
            // 기존 구독이 없으면 새로운 구독 처리
            subscribe(channelSubscribe, id, dto);
        } else {
            // 구독이 활성화된 상태라면 구독 해지
            unsubscribe(channelSubscribe, dto);
        }
    }
    @Transactional
    public void manageSubscription(ChannelSubscribeRequestDTO dto) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId(), dto.getIsUserChannel());
        Optional<ChannelSubscribe> optionalSubscribe = repository.findById(id);
        ChannelSubscribe channelSubscribe = optionalSubscribe.orElse(null);

        if (channelSubscribe == null || !channelSubscribe.getIsActive()) {
            // 구독이 없거나 비활성화된 경우 구독 처리
            subscribe(channelSubscribe, id, dto);
        } else {
            // 구독이 활성화된 상태라면 구독 해지 처리
            unsubscribe(channelSubscribe, dto);
        }
    }

    private void subscribe(ChannelSubscribe existingSubscribe, MyChannelSubscribeId id, ChannelSubscribeRequestDTO dto) {
        if (existingSubscribe == null) {
            // 새 구독 생성
            ChannelSubscribe newSubscription = new ChannelSubscribe(id, LocalDateTime.now(), null, true);
            repository.save(newSubscription);
        } else {
            // 기존 구독 활성화 및 구독 날짜 갱신
            existingSubscribe.setActive(true);
            existingSubscribe.setSubscriptionDate(LocalDateTime.now());
            existingSubscribe.setExpirationDate(null);
            repository.save(existingSubscribe);
        }

        // 팔로워 및 팔로잉 수 업데이트
        updateFollowerAndFollowing(dto.getUserId(), dto.getChannelId(), dto.getIsUserChannel(), true);

    }

    private void unsubscribe(ChannelSubscribe existingSubscribe, ChannelSubscribeRequestDTO dto) {
        if (existingSubscribe == null) {
            throw new RuntimeException("Subscription not found");
        }
        // 구독 해지 및 만료일 설정
        existingSubscribe.setActive(false);
        existingSubscribe.setExpirationDate(LocalDateTime.now());
        repository.save(existingSubscribe);

        // 팔로워 및 팔로잉 수 업데이트
        updateFollowerAndFollowing(dto.getUserId(), dto.getChannelId(), dto.getIsUserChannel(), false);

    }

    @Transactional
    public void deleteById(Long userId, Long channelId, Boolean isUserChannel) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(userId, channelId, isUserChannel);

        if (!repository.existsById(id)) {
            throw new RuntimeException("Subscription not found");
        }

        // Delete the subscription
        repository.deleteById(id);

        // Update follower and following counts
        updateFollowerAndFollowing(userId, channelId, isUserChannel, false);
    }

    private void updateFollowerAndFollowing(Long userId, Long channelId, Boolean isUserChannel, Boolean isSubscribing) {
        // 팔로잉 수 업데이트
        updateFollowingService.publish(userId,  isUserChannel, isSubscribing);

        // 팔로워 수 업데이트
        updateFollowerService.publish(channelId, isUserChannel, isSubscribing);
    }
}
