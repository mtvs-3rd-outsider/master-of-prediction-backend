package com.outsider.masterofpredictionbackend.feed.command.application.service;

// FollowFeedService.java
import com.outsider.masterofpredictionbackend.channelsubscribe.query.service.ChannelSubscriptionService;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedsResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.ChannelInfo;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowFeedService {
    private final FeedRepository feedRepository;
    private final FeedsResponseDTOConverter converterFacade;
    private final ChannelSubscriptionService channelSubscriptionService;

    public FollowFeedService(
            FeedRepository feedRepository,
            FeedsResponseDTOConverter converterFacade,
            ChannelSubscriptionService channelSubscriptionService) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
        this.channelSubscriptionService = channelSubscriptionService;
    }

    @Transactional(readOnly = true)
    public Page<FeedsResponseDTO> getFollowingFeeds(Long userId, Pageable pageable) {
        // 1. 사용자가 팔로우한 채널 목록 조회
        Page<ChannelInfo> followingChannels = channelSubscriptionService.getAllFollowingByUserId( userId,
                userId,
                true,
                PageRequest.of(0, 1000), // 적절한 최대값 설정
                "ALL"
        );

        List<Feed> userChannelFeeds = new ArrayList<>();
        List<Feed> categoryChannelFeeds = new ArrayList<>();

        // 2. UserChannel 피드 조회
        List<ChannelInfo> userChannels = followingChannels.getContent().stream()
                .filter(ChannelInfo::isUserChannel)
                .toList();

        if (!userChannels.isEmpty()) {
            List<Long> userChannelIds = userChannels.stream()
                    .map(ChannelInfo::getChannelId)
                    .toList();

            userChannelFeeds = feedRepository.findByChannel_ChannelTypeAndChannel_ChannelIdsOrReuploadedBy(
                    ChannelType.MYCHANNEL,
                    userChannelIds,
                    PageRequest.of(0, 10, Sort.by("shortAt").descending())
            ).getContent();
        }

        // 3. CategoryChannel 피드 조회
        List<ChannelInfo> categoryChannels = followingChannels.getContent().stream()
                .filter(channel -> !channel.isUserChannel())
                .toList();

        if (!categoryChannels.isEmpty()) {
            List<Long> categoryChannelIds = categoryChannels.stream()
                    .map(ChannelInfo::getChannelId)
                    .collect(Collectors.toList());

            categoryChannelFeeds = feedRepository.findByChannelTypeAndChannelIds(
                    ChannelType.CATEGORYCHANNEL,
                    categoryChannelIds,
                    PageRequest.of(0, 10, Sort.by("shortAt").descending())
            );
        }

        // 4. 두 리스트 병합 및 정렬
        List<Feed> allFeeds = new ArrayList<>();
        allFeeds.addAll(userChannelFeeds);
        allFeeds.addAll(categoryChannelFeeds);
        allFeeds.sort((f1, f2) -> f2.getShortAt().compareTo(f1.getShortAt()));

        // 5. 페이징 처리
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), allFeeds.size());

        List<Feed> pagedFeeds = allFeeds.subList(start, end);

        // 6. FeedsResponseDTO 변환
        List<FeedsResponseDTO> feedDtos = pagedFeeds.stream()
                .map(feed -> converterFacade.fromEntity(feed, userId))
                .collect(Collectors.toList());

        return new PageImpl<>(
                feedDtos,
                pageable,
                allFeeds.size()
        );
    }
}