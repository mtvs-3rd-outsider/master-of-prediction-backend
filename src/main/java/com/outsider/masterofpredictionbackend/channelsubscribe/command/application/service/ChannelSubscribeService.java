package com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.event.ChannelSubscriptionEvent;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository.ChannelSubscribeCommandRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowerCountClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowingCountClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.exception.InvalidSubscriptionException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
@Service
public class ChannelSubscribeService {

    private final ChannelSubscribeCommandRepository repository;
    private final UpdateFollowerCountClient updateFollowerService;
    private final UpdateFollowingCountClient updateFollowingService;
    private final ChannelSubscriptionProducer producer;
    private final ObjectMapper objectMapper;
    @Autowired
    public ChannelSubscribeService(
            ChannelSubscribeCommandRepository repository,
            UpdateFollowerCountClient updateFollowerService,
            UpdateFollowingCountClient updateFollowingService, ChannelSubscriptionProducer producer, ObjectMapper objectMapper
    ) {
        this.repository = repository;
        this.updateFollowerService = updateFollowerService;
        this.updateFollowingService = updateFollowingService;
        this.producer = producer;
        this.objectMapper = objectMapper;
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

    public void startSubscriptionSaga(ChannelSubscribeRequestDTO dto) throws JsonProcessingException {
        try {
            // Saga의 첫 번째 단계: 사용자 및 채널 존재 여부 확인 요청
            producer.sendValidationRequestUserPart(dto);

            // 이후 단계는 Validation 결과를 Kafka를 통해 수신하여 처리합니다.
            log.info("Subscription Saga started for userId: {}, channelId: {}", dto.getUserId(), dto.getChannelId());
        } catch (Exception e) {
            log.error("Failed to start Subscription Saga: {}", e.getMessage());
            throw e;
        }
    }
    @KafkaListener(topics = "user-channel-validation-response-user-part", groupId = "channel-subscribe-group")
    @Transactional
    public void handleChannelValidationResponse(String message, Acknowledgment ack) {
        try {
            // JSON 문자열을 ChannelSubscriptionEvent 객체로 역직렬화
            ChannelSubscriptionEvent event = objectMapper.readValue(message, ChannelSubscriptionEvent.class);
            ChannelSubscribeRequestDTO dto = event.getDto();if (event.isValidationSuccess()) {
                // 검증 성공 시 구독 처리 진행
                producer.sendValidationRequestChannelPart(dto);
                // 팔로워 및 팔로잉 수 업데이트 이벤트 발행
//                producer.sendFollowerFollowingUpdate(dto);
            } else {
                // 검증 실패 시 보상 트랜잭션 실행 또는 에러 처리
                log.error("Validation failed for userId: {}, channelId: {}", dto.getUserId(), dto.getChannelId());
                // 필요한 경우 보상 트랜잭션 실행
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to handle validation response: {}", e.getMessage());
            ack.nack(Duration.ofSeconds(1));
        }
    }

    /**
     * 사용자 및 채널 검증 결과를 처리합니다.
     *
     * @param event 검증 결과 이벤트
     */
    @KafkaListener(topics = "user-channel-validation-response-channel-part", groupId = "channel-subscribe-group")
    @Transactional
    public void handleUserValidationResponse(String message, Acknowledgment ack) {
        try {
            // JSON 문자열을 ChannelSubscriptionEvent 객체로 역직렬화
            ChannelSubscriptionEvent event = objectMapper.readValue(message, ChannelSubscriptionEvent.class);
            ChannelSubscribeRequestDTO dto = event.getDto();

            if (event.isValidationSuccess()) {
                // 검증 성공 시 구독 처리 진행
                manageSubscription(dto);
                // 팔로워 및 팔로잉 수 업데이트 이벤트 발행
//                producer.sendFollowerFollowingUpdate(dto);
            } else {
                // 검증 실패 시 보상 트랜잭션 실행 또는 에러 처리
                log.error("Validation failed for userId: {}, channelId: {}", dto.getUserId(), dto.getChannelId());
                // 필요한 경우 보상 트랜잭션 실행
            }

            ack.acknowledge();
        } catch (Exception e) {
            log.error("Failed to handle validation response: {}", e.getMessage());
            ack.nack(Duration.ofSeconds(1));
        }
    }


    @Transactional
    public void manageSubscription(ChannelSubscribeRequestDTO dto) {
        MyChannelSubscribeId id = new MyChannelSubscribeId(dto.getUserId(), dto.getChannelId(), dto.getIsUserChannel());
        Optional<ChannelSubscribe> optionalSubscribe = repository.findById(id);
        ChannelSubscribe channelSubscribe = optionalSubscribe.orElse(null);

        if(dto.getUserId().equals(dto.getChannelId()) && dto.getIsUserChannel()) {
            throw new InvalidSubscriptionException();
        }

        if ("subscribe".equals(dto.getActionType())) {
            // 기존 구독이 없으면 새로운 구독 처리
            subscribe(channelSubscribe, id, dto);
        } else {
            // 구독이 활성화된 상태라면 구독 해지
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
//        updateFollowingService.publish(userId,  true, isSubscribing);

        // 팔로워 수 업데이트
//        updateFollowerService.publish(channelId, isUserChannel, isSubscribing);
    }
}
