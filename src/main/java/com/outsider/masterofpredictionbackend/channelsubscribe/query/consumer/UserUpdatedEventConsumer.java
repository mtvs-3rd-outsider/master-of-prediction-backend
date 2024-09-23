package com.outsider.masterofpredictionbackend.channelsubscribe.query.consumer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscriptionId;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.repository.ChannelSubscriptionRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.query.mychannelinfo.service.MyChannelInfoCDCUserPartConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class UserUpdatedEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MyChannelInfoCDCUserPartConsumer.class);
    private final ObjectMapper mapper;
    private final ChannelSubscriptionRepository repository;
    public UserUpdatedEventConsumer(ObjectMapper objectMapper, ChannelSubscriptionRepository repository) {
        this.mapper = objectMapper;
        this.repository = repository;
    }


    @KafkaListener(topics = "dbserver1.forecasthub.user", groupId = "User-updated-info-subscribe-group")
    @Transactional
    public void consume(ConsumerRecord<String, String> record) {

        String consumedValue = record.value();

        if (consumedValue == null) {
            logger.error("Consumed record value is null for record: {}", record);
            return;
        }

        try {
            JsonNode jsonNode = mapper.readTree(consumedValue);
            JsonNode payload = jsonNode.path("payload");
            String operation = payload.get("op").asText().substring(0, 1);
            JsonNode after = payload.path("after");
            JsonNode before = payload.path("before");

            switch (operation) {
                case "u":
                    handleCreateOrUpdate(after);
                    break;
                case "d":
                    handleDelete(before);
                    break;
                default:
                    logger.warn("Unknown operation type: {}", operation);
            }

        } catch (Exception e) {
            logger.error("Error processing Kafka record", e);
        }
    }
    private void handleCreateOrUpdate(JsonNode after) {
        if (after != null && !after.isNull()) {
            try {
                User userData = mapper.treeToValue(after, User.class);
                logger.info(userData.toString());
                updateUserInfoInSubscriptions(userData);
            } catch (Exception e) {
                logger.error("Error parsing UserUpdatedEvent data", e);
            }
        }
    }

    private void handleDelete(JsonNode before) {
        // 사용자 삭제 시 처리 로직 (필요 시 구현)
        // 예: 사용자 삭제에 따른 구독 정보 정리
        if (before != null && !before.isNull()) {
            try {
                User userData = mapper.treeToValue(before, User.class);
                logger.info(userData.toString());

                removeUserInfoFromSubscriptions(userData);
            } catch (Exception e) {
                logger.error("Error parsing UserDeletedEvent data", e);
            }
        }
    }

    private void updateUserInfoInSubscriptions(User userData) {
        Long userId = userData.getId();
        String newDisplayName = userData.getDisplayName();
        String newUserName = userData.getUserName();
        String newUserAvatarUrl = userData.getUserImg();

        logger.info("Updating user info in subscriptions for userId={}", userId);

        // 1. 사용자 채널 구독 정보 업데이트
//        ChannelSubscriptionId userChannelSubscriptionId = new ChannelSubscriptionId(userId, true);
//        ChannelSubscription userChannelSubscription = repository.findById(userChannelSubscriptionId)
//                .orElse(null);

//        if (userChannelSubscription != null) {
//            userChannelSubscription.updateUserInfo(newDisplayName, newUserName, newUserAvatarUrl);
//            repository.save(userChannelSubscription);
//            logger.info("Updated userChannelSubscription for userId={}", userId);
//        }

        // 2. 채널 구독자 정보 업데이트
        // 모든 채널 구독에서 해당 사용자를 구독자로 포함하는 경우 업데이트
        // 예를 들어, 사용자 정보를 포함한 모든 구독자를 조회하여 업데이트
        repository.findBySubscribersUserId(userId).forEach(subscription -> {
            subscription.updateSubscriberInfo(userId, newDisplayName, newUserName, newUserAvatarUrl);
            repository.save(subscription);
            logger.info("Updated subscriber info in subscriptionId={} for userId={}", subscription.getId(), userId);
        });
        repository.findByFollowingChannelId(userId).forEach(subscription -> {
            // 팔로잉 정보 업데이트
            subscription.updateFollowingInfo(userId, newDisplayName, newUserName, newUserAvatarUrl);
            repository.save(subscription);
            logger.info("Updated following info in subscriptionId={} for userId={}", subscription.getId(), userId);
        });

    }

    private void removeUserInfoFromSubscriptions(User userData) {
        Long userId = userData.getId();
        logger.info("Removing user info from subscriptions for userId={}", userId);

        // 1. 사용자 채널 구독 정보 삭제
        ChannelSubscriptionId userChannelSubscriptionId = new ChannelSubscriptionId(userId, true);
        repository.deleteById(userChannelSubscriptionId);
        logger.info("Deleted userChannelSubscription for userId={}", userId);

        // 2. 채널 구독자 정보 삭제
        repository.findBySubscribersUserId(userId).forEach(subscription -> {
            subscription.removeSubscriber(userId);
            repository.save(subscription);
            logger.info("Removed subscriber from subscriptionId={} for userId={}", subscription.getId(), userId);
        });
    }
}
