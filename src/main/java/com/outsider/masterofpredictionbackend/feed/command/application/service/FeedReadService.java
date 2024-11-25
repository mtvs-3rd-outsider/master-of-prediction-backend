package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.channelsubscribe.query.client.ChannelServiceClient;
import com.outsider.masterofpredictionbackend.channelsubscribe.query.dto.response.ChannelResponse;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.ChannelDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalCommentService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import com.outsider.masterofpredictionbackend.util.UserId;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class FeedReadService {
    private final FeedRepository feedRepository;
    private final FeedViewCountService feedViewCountService;
    private final FeedResponseDTOConverter converterFacade;
    private final ExternalLikeService externalLikeService;
    private final ChannelServiceClient channelServiceClient;


    @Transactional(readOnly = false)
    public FeedResponseDTO getFeed(Long feedId, Long userId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        feed.setIsLike(externalLikeService.checkUserLike(userId, LikeType.FEED, ViewType.HOTTOPICCHANNEL,feedId));
        feed.setLikesCount(externalLikeService.getLikeCount(new LikeDTO(LikeType.FEED,ViewType.HOTTOPICCHANNEL,feedId)));

        FeedResponseDTO feedResponseDTO = converterFacade.fromEntity(feed,(feed.getAuthorType()==AuthorType.USER)?feed.getUser().getUserId():null);
        Boolean isShared = feed.isReupLoadedBy(userId);
        CompletableFuture<ChannelResponse> channelResponse =channelServiceClient.getChannelById(feed.getChannel().getChannelId());
        feedResponseDTO.setIsShare(isShared);
        channelResponse.thenAccept(channel -> {
            feedResponseDTO.setChannel(new ChannelDTO(
                    channel.getChannelId(),
                    channel.getDisplayName(),
                    feed.getChannel().getChannelType()
            ));
        });
        // 비동기적으로 조회수를 증가시킵니다.
        feedViewCountService.incrementViewCount(feedId);

        return feedResponseDTO;
    }


}