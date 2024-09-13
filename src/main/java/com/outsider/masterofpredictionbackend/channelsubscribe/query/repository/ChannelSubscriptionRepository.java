package com.outsider.masterofpredictionbackend.channelsubscribe.query.repository;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscription;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscriptionId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface ChannelSubscriptionRepository extends MongoRepository<ChannelSubscription, ChannelSubscriptionId> {
    // 필요한 쿼리 메서드 정의
    // 사용자 ID로 구독자 목록 조회 메서드 예시
    List<ChannelSubscription> findBySubscribersUserId(Long userId);
}
