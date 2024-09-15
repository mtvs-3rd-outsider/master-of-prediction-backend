package com.outsider.masterofpredictionbackend.channelsubscribe.query.model;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.ChannelInfo;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.UserInfo;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "channel_subscriptions")
@ToString
public class ChannelSubscription {

    @Id
    private ChannelSubscriptionId id;

    // 구독자 목록
    private List<UserInfo> subscribers = new ArrayList<>();

    // 팔로잉 목록 (해당 채널이 구독한 채널들)
    private List<ChannelInfo> following = new ArrayList<>();

    // 기본 생성자
    public ChannelSubscription() {}
    public void updateUserInfo(String displayName, String userName, String userAvatarUrl) {
        if (id.isUserChannel()) {
            // 사용자 채널일 경우, 사용자 정보 업데이트
            // 예시로, 팔로잉 정보 업데이트
            for (ChannelInfo following : following) {
                if (following.getChannelId().equals(id.getChannelId())) {
                    following.setDisplayName(displayName);
                    following.setChannelName(userName);
                    following.setChannelImageUrl(userAvatarUrl);
                }
            }
        }

        // 구독자 정보 업데이트
        for (UserInfo subscriber : subscribers) {
            if (subscriber.getUserId().equals(id.getChannelId())) {
                subscriber.setDisplayName(displayName);
                subscriber.setUserName(userName);
                subscriber.setUserAvatarUrl(userAvatarUrl);
            }
        }
    }

    // 구독자 정보 업데이트 메서드 예시
    public void updateSubscriberInfo(Long userId, String displayName, String userName, String userAvatarUrl) {
        subscribers.stream()
                .filter(subscriber -> subscriber.getUserId().equals(userId))
                .forEach(subscriber -> {
                    subscriber.setDisplayName(displayName);
                    subscriber.setUserName(userName);
                    subscriber.setUserAvatarUrl(userAvatarUrl);
                });
    }
    public void updateFollowingInfo(Long userId, String displayName, String userName, String userAvatarUrl) {
        for (ChannelInfo following : following) {
            if (following.getChannelId().equals(userId) && following.isUserChannel()) {
                following.setDisplayName(displayName);
                following.setChannelName(userName);
                following.setChannelImageUrl(userAvatarUrl);
            }
        }
    }
    // 생성자
    public ChannelSubscription(ChannelSubscriptionId id) {
        this.id = id;
    }

    // Getter와 Setter
    public ChannelSubscriptionId getId() {
        return id;
    }

    public void setId(ChannelSubscriptionId id) {
        this.id = id;
    }

    public List<UserInfo> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<UserInfo> subscribers) {
        this.subscribers = subscribers;
    }

    public List<ChannelInfo> getFollowing() {
        return following;
    }

    public void setFollowing(List<ChannelInfo> following) {
        this.following = following;
    }


    // 구독자 목록에 중복 여부 확인
    public boolean hasSubscriber(Long userId) {
        return subscribers.stream().anyMatch(subscriber -> subscriber.getUserId().equals(userId));
    }

    // 팔로잉 목록에 중복 여부 확인
    public boolean hasFollowing(Long channelId) {
        return following.stream().anyMatch(following -> following.getChannelId().equals(channelId));
    }

    // 구독자 추가 메서드
    public void addSubscriber(String displayName, Long userId, String userName, String userAvatarUrl) {
        subscribers.add(new UserInfo(displayName, userId, userName, userAvatarUrl));
    }

    // 구독자 제거 메서드
    public void removeSubscriber(Long userId) {
        subscribers.removeIf(subscriber -> subscriber.getUserId().equals(userId));
    }

    // 팔로잉 추가 메서드
    public void addFollowing(String displayName, Long channelId, String channelName, String channelImageUrl,boolean isUserChannel) {
        following.add(new ChannelInfo(displayName, channelId, channelName, channelImageUrl,isUserChannel));
    }

    // 팔로잉 제거 메서드
    public void removeFollowing(Long channelId) {
        following.removeIf(following -> following.getChannelId().equals(channelId));
    }
}
