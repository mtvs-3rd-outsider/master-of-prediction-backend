package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedsResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeChannelFeedService {

    private final FeedRepository feedRepository;
    private final FeedsResponseDTOConverter converterFacade;
    private final ExternalLikeService externalLikeService;

    @Autowired
    public HomeChannelFeedService(FeedRepository feedRepository, FeedsResponseDTOConverter converterFacade, ExternalLikeService externalLikeService) {
        this.feedRepository = feedRepository;
        this.converterFacade = converterFacade;
        this.externalLikeService = externalLikeService;
    }


    @Transactional
    public Page<FeedsResponseDTO> getFeeds(Pageable pageable, long userId) {
        // pageable의 정렬 기준을 사용하여 데이터 조회
        Page<Feed> feedPage = feedRepository.findAll(pageable);

        // 페이징된 피드들의 좋아요 수 동기화
        List<Feed> pagedFeeds = feedPage.getContent();
        for (Feed feed : pagedFeeds) {
            if(feed.getAuthorType()== AuthorType.USER) {
                int likeCount = externalLikeService.getLikeCount(
                        new LikeDTO(LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getUser().getUserId(), feed.getId())
                );
                feed.setLikesCount(likeCount);
            }

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

            return responseDTO;
        });
    }

    public Page<FeedsResponseDTO>getBettingFeeds(Pageable pageable, long userId) {
        // pageable의 정렬 기준을 사용하여 데이터 조회
        Page<Feed> feedPage = feedRepository.findAllByIdLessThanZero(pageable);

        // 페이징된 피드들의 좋아요 수 동기화
        List<Feed> pagedFeeds = feedPage.getContent();
        for (Feed feed : pagedFeeds) {
            if(feed.getAuthorType()== AuthorType.USER) {
                int likeCount = externalLikeService.getLikeCount(
                        new LikeDTO(LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getUser().getUserId(), feed.getId())
                );
                feed.setLikesCount(likeCount);
            }

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

            return responseDTO;
        });
    }
}