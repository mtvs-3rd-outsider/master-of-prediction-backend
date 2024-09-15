package com.outsider.masterofpredictionbackend.channelsubscribe.query.consumer;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.client.ChannelServiceClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.client.UserServiceClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.ChannelInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.SubscriptionEvent;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.UserInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.ChannelResponse;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.UserResponse;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscription;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscriptionId;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.repository.ChannelSubscriptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class SubscriptionEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionEventHandler.class);

    private final UserServiceClient userServiceClient;
    private final ChannelServiceClient channelServiceClient;
    private final ChannelSubscriptionRepository repository;

    public SubscriptionEventHandler(UserServiceClient userServiceClient, ChannelServiceClient channelServiceClient, ChannelSubscriptionRepository repository) {
        this.userServiceClient = userServiceClient;
        this.channelServiceClient = channelServiceClient;
        this.repository = repository;
    }

    public void handleEvent(SubscriptionEvent event) {
        Long userId = event.getUserId();
        Long channelId = event.getChannelId();
        boolean isUserChannel = event.isUserChannel();
        boolean isSubscribed = event.isSubscribed();

        logger.info("Handling event: userId={}, channelId={}, isUserChannel={}, isSubscribed={}",
                userId, channelId, isUserChannel, isSubscribed);

        // 사용자 정보 비동기 조회
        CompletableFuture<UserResponse> userFuture = userServiceClient.getUserById(userId)
                .exceptionally(ex -> {
                    logger.error("Failed to fetch user with ID {}", userId, ex);
                    return null;
                });

        // 채널 정보 비동기 조회
        CompletableFuture<?> channelFuture;
        if (isUserChannel) {
            // 만약 구독 대상이 사용자라면, 해당 사용자의 정보를 조회
            channelFuture = userServiceClient.getUserById(channelId)
                    .exceptionally(ex -> {
                        logger.error("Failed to fetch user channel with ID {}", channelId, ex);
                        return null;
                    });
        } else {
            // 구독 대상이 채널이라면, 해당 채널의 정보를 조회
            channelFuture = channelServiceClient.getChannelById(channelId)
                    .exceptionally(ex -> {
                        logger.error("Failed to fetch channel with ID {}", channelId, ex);
                        return null;
                    });
        }

        // 두 Future가 완료되면 처리 진행
        CompletableFuture.allOf(userFuture, channelFuture).thenAccept(voidResult -> {
            try {
                UserResponse userResponse = userFuture.get();
                if (userResponse == null) {
                    logger.warn("UserResponse is null for userId {}", userId);
                    return;
                }

                // 채널 정보 조회
                Object channelObject = channelFuture.get();
                if (channelObject == null) {
                    logger.warn("Channel/UserResponse is null for channelId {}", channelId);
                    return;
                }

                // **1. 채널 구독자(`subscribers`) 업데이트**
                ChannelSubscriptionId channelSubscriptionId = new ChannelSubscriptionId(channelId, isUserChannel);
                ChannelSubscription channelSubscription = repository.findById(channelSubscriptionId)
                        .orElseGet(() -> new ChannelSubscription(channelSubscriptionId));

                if (isSubscribed) {
                    if (!channelSubscription.hasSubscriber(userId)) {
                        channelSubscription.addSubscriber(
                                userResponse.getDisplayName(),
                                userResponse.getUserId(),
                                userResponse.getUserName(),
                                userResponse.getUserAvatarUrl()
                        );
                        logger.info("Added subscriber: userId={} to channelId={}", userId, channelId);
                    } else {
                        logger.info("Subscriber already exists: userId={} for channelId={}", userId, channelId);
                    }
                } else {
                    channelSubscription.removeSubscriber(userId);
                    logger.info("Removed subscriber: userId={} from channelId={}", userId, channelId);
                }

                // **2. 사용자 팔로잉(`following`) 업데이트**
                // 사용자 채널 ID는 사용자 ID이고, isUserChannel=true
                ChannelSubscriptionId userChannelSubscriptionId = new ChannelSubscriptionId(userId, true);
                ChannelSubscription userChannelSubscription = repository.findById(userChannelSubscriptionId)
                        .orElseGet(() -> new ChannelSubscription(userChannelSubscriptionId));

                if (isSubscribed) {
                    // 채널 정보 타입에 따라 팔로잉 추가
                    if (isUserChannel) {
                        UserResponse channelUserResponse = (UserResponse) channelObject;
                        UserInfo channelInfo = new UserInfo(
                                channelUserResponse.getDisplayName(),
                                channelUserResponse.getUserId(),
                                channelUserResponse.getUserName(),
                                channelUserResponse.getUserAvatarUrl()
                        );

                        if (!userChannelSubscription.hasFollowing(channelId)) {
                            userChannelSubscription.addFollowing(
                                    channelInfo.getDisplayName(),
                                    channelInfo.getUserId(),
                                    channelInfo.getUserName(),
                                    channelInfo.getUserAvatarUrl(),
                                    isUserChannel
                            );
                            logger.info("Added following: channelId={} to userId={}", channelId, userId);
                        } else {
                            logger.info("Already following: channelId={} by userId={}", channelId, userId);
                        }
                    } else {
                        ChannelResponse channelResponse = (ChannelResponse) channelObject;
                        ChannelInfo channelInfo = new ChannelInfo(
                                channelResponse.getDisplayName(),
                                channelResponse.getChannelId(),
                                channelResponse.getChannelName(),
                                channelResponse.getChannelImageUrl(),
                                isUserChannel
                        );
                        if (!userChannelSubscription.hasFollowing(channelId)) {
                            userChannelSubscription.addFollowing(
                                    channelInfo.getDisplayName(),
                                    channelInfo.getChannelId(),
                                    channelInfo.getChannelName(),
                                    channelInfo.getChannelImageUrl(),
                                    isUserChannel
                            );
                            logger.info("Added following: channelId={} to userId={}", channelId, userId);
                        } else {
                            logger.info("Already following: channelId={} by userId={}", channelId, userId);
                        }
                    }
                } else {
                    userChannelSubscription.removeFollowing(channelId);
                    logger.info("Removed following: channelId={} from userId={}", channelId, userId);
                }

                // 변경 사항 저장
                repository.save(channelSubscription);
                repository.save(userChannelSubscription);
                logger.info("Successfully processed event for userId={} and channelId={}", userId, channelId);

            } catch (Exception e) {
                logger.error("Error processing subscription event", e);
                // 추가적인 에러 처리 로직 (예: 알림 전송 등)
            }
        }).exceptionally(ex -> {
            logger.error("Error in CompletableFuture processing", ex);
            // 추가적인 에러 처리 로직
            return null;
        });
    }
}
