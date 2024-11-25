package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.client.ChannelServiceClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.ChannelResponse;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.ChannelDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedsResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;


@Service
@RequiredArgsConstructor
public class CategoryChannelFeedService {

    private final FeedRepository feedRepository;
    private final FeedsResponseDTOConverter converterFacade;
    private final ExternalLikeService externalLikeService;
    private final ChannelServiceClient channelServiceClient;

    public Page<FeedsResponseDTO> getFeeds(ChannelType channelType, Long channelId, Pageable pageable, Long userId) {
        Page<Feed> feedPage = feedRepository.findByChannel_ChannelTypeAndChannel_ChannelId(channelType, channelId, pageable);
        List<Feed> pagedFeeds = feedPage.getContent();
        for (Feed feed : pagedFeeds) {
            int likeCount = externalLikeService.getLikeCount(
                    new LikeDTO(LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getId())
            );
            feed.setLikesCount(likeCount);
        }
        feedRepository.saveAll(pagedFeeds);

        return feedPage.map(feed -> {
            boolean isLiked = externalLikeService.checkUserLike(
                    userId,
                    LikeType.FEED,
                    ViewType.HOTTOPICCHANNEL,
                    feed.getId()
            );
            feed.setIsLike(isLiked);

            boolean isShared = feed.isReupLoadedBy(userId);
            FeedsResponseDTO responseDTO = converterFacade.fromEntity(feed, userId);
            responseDTO.setIsShare(isShared);
            CompletableFuture<ChannelResponse> channelResponse =channelServiceClient.getChannelById(feed.getChannel().getChannelId());
            channelResponse.thenAccept(channel -> {
                responseDTO.setChannel(new ChannelDTO(
                        channel.getChannelId(),
                        channel.getDisplayName(),
                        feed.getChannel().getChannelType()
                ));
            });

            return responseDTO;
        });
    }
}
