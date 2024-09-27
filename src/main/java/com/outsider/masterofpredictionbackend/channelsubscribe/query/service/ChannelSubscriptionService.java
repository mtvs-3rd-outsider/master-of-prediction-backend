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
                        // Check if the target user is following the channel
                        ChannelSubscriptionId targetUserSubscriptionId =new ChannelSubscriptionId(targetUserId,true);
                        ChannelSubscriptionId channelId = new ChannelSubscriptionId(channel.getChannelId(),channel.isUserChannel());
                        channel.setFollowing(isUserFollowingChannel(targetUserSubscriptionId, channelId));

                        // If it's not a user channel, set isFollowing to null
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
                ChannelSubscriptionId targetUserSubscriptionId = new ChannelSubscriptionId(targetUserId,true);
                ChannelSubscriptionId subscriberSubscriptionId = new ChannelSubscriptionId(subscriber.getUserId(),true);
                subscriber.setFollowing(isUserFollowingChannel( targetUserSubscriptionId,subscriberSubscriptionId ));
            }
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), subscribersList.size());

            return new PageImpl<>(subscribersList.subList(start, end), pageable, subscribersList.size());
        }

        return Page.empty(pageable);
    }
    // 사용자가 특정 채널을 팔로우하고 있는지 확인
    public boolean isUserFollowingChannel(ChannelSubscriptionId userSubscriptionId, ChannelSubscriptionId channelId) {
        ChannelSubscription userSubscription = repository.findById(userSubscriptionId).orElse(null);
        return userSubscription != null && userSubscription.hasFollowing(channelId);
    }

    // 특정 채널에 특정 사용자가 구독자인지 확인
    public boolean isChannelSubscribedByUser(Long channelId, Long userId, boolean isUserChannel) {
        ChannelSubscriptionId channelSubscriptionId = new ChannelSubscriptionId(channelId, isUserChannel);
        ChannelSubscription channelSubscription = repository.findById(channelSubscriptionId).orElse(null);
        return channelSubscription != null && channelSubscription.hasSubscriber(userId);
    }
    // 사용자가 팔로우한 채널 수를 가져오는 메서드
    public Long getFollowingCountByUserId(Long userId, boolean isUserChannel) {
        ChannelSubscriptionId userSubscriptionId = new ChannelSubscriptionId(userId, isUserChannel);
        ChannelSubscription userSubscription = repository.findById(userSubscriptionId).orElse(null);

        if (userSubscription != null) {
            // List of following channels
            List<ChannelInfo> followingList = userSubscription.getFollowing();
            return (long) followingList.size(); // Return the count of following channels
        }

        return 0L; // Return 0 if no subscriptions found
    }

    // 특정 채널의 구독자 수를 가져오는 메서드
    public Long getSubscribersCountByChannelId(Long channelId, boolean isUserChannel) {
        ChannelSubscriptionId channelSubscriptionId = new ChannelSubscriptionId(channelId, isUserChannel);
        ChannelSubscription channelSubscription = repository.findById(channelSubscriptionId).orElse(null);

        if (channelSubscription != null) {
            // List of subscribers
            List<UserInfo> subscribersList = channelSubscription.getSubscribers();
            return (long) subscribersList.size(); // Return the count of subscribers
        }

        return 0L; // Return 0 if no subscriptions found
    }
}
