package com.outsider.masterofpredictionbackend.channelsubscribe.query.service;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.ChannelInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.UserInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscription;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.model.ChannelSubscriptionId;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.repository.ChannelSubscriptionRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ChannelSubscriptionService {

    private final ChannelSubscriptionRepository repository;

    public ChannelSubscriptionService(ChannelSubscriptionRepository repository) {
        this.repository = repository;
    }

    // 특정 구독 정보를 ID와 isUserChannel을 통해 가져오기
    public ChannelSubscription getChannelSubscriptionById(Long id, boolean isUserChannel) {
        ChannelSubscriptionId subscriptionId = new ChannelSubscriptionId(id, isUserChannel);
        return repository.findById(subscriptionId).orElse(null);
    }

    public Page<ChannelInfo> getAllFollowingByUserId(Long userId, Long targetUserId, Boolean isUserChannel, Pageable pageable, String flag) {
        ChannelSubscriptionId userSubscriptionId = new ChannelSubscriptionId(userId, isUserChannel);
        ChannelSubscription userSubscription = repository.findById(userSubscriptionId).orElse(null);

        if (userSubscription != null) {
            List<ChannelInfo> followingList = userSubscription.getFollowing();
            List<ChannelInfo> filteredList = new ArrayList<>();

            for (ChannelInfo channel : followingList) {
                // Filter based on the flag
                if ("ALL".equalsIgnoreCase(flag) ||
                        ("USER".equalsIgnoreCase(flag) && channel.isUserChannel()) ||
                        ("CATEGORY".equalsIgnoreCase(flag) && !channel.isUserChannel())) {

                    // Verify if the channel is a user channel
                    if (channel.isUserChannel()) {
                        // Check if the target user is following the channel
                        channel.setIsFollowing(isUserFollowingChannel(targetUserId, channel.getChannelId(), true));
                    } else {
                        // If it's not a user channel, set isFollowing to null
                        channel.setIsFollowing(null);
                    }
                    filteredList.add(channel);
                }
            }

            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredList.size());

            return new PageImpl<>(filteredList.subList(start, end), pageable, filteredList.size());
        }

        return Page.empty(pageable);
    }

    // 특정 채널의 모든 구독자 목록을 페이징하여 가져오기 + 특정 사용자를 팔로우하는지 여부
    public Page<UserInfo> getAllSubscribersByChannelId(Long channelId, Long targetUserId, boolean isUserChannel, Pageable pageable) {
        ChannelSubscriptionId channelSubscriptionId = new ChannelSubscriptionId(channelId, isUserChannel);
        ChannelSubscription channelSubscription = repository.findById(channelSubscriptionId).orElse(null);

        if (channelSubscription != null) {
            List<UserInfo> subscribersList = channelSubscription.getSubscribers();
            for (UserInfo subscriber : subscribersList) {
                // targetUserId를 활용하여 특정 사용자가 구독자인지 확인
                subscriber.setFollowing(isUserFollowingChannel( targetUserId, subscriber.getUserId(),true));
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), subscribersList.size());

            return new PageImpl<>(subscribersList.subList(start, end), pageable, subscribersList.size());
        }

        return Page.empty(pageable);
    }
    // 사용자가 특정 채널을 팔로우하고 있는지 확인
    public boolean isUserFollowingChannel(Long userId, Long channelId, boolean isUserChannel) {
        ChannelSubscriptionId userSubscriptionId = new ChannelSubscriptionId(userId, isUserChannel);
        ChannelSubscription userSubscription = repository.findById(userSubscriptionId).orElse(null);
        return userSubscription != null && userSubscription.hasFollowing(channelId);
    }

    // 특정 채널에 특정 사용자가 구독자인지 확인
    public boolean isChannelSubscribedByUser(Long channelId, Long userId, boolean isUserChannel) {
        ChannelSubscriptionId channelSubscriptionId = new ChannelSubscriptionId(channelId, isUserChannel);
        ChannelSubscription channelSubscription = repository.findById(channelSubscriptionId).orElse(null);
        return channelSubscription != null && channelSubscription.hasSubscriber(userId);
    }
}
