package com.outsider.masterofpredictionbackend.feed.command.application.service.impl;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedsResponseDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.application.dto.LikeDTO;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.ViewType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        // 페이징된 데이터만 조회
        Page<Feed> feedPage = feedRepository.findAllByOrderByShortAtDesc(pageable);

        // 페이징된 피드들만 좋아요 수 동기화
        List<Feed> pagedFeeds = feedPage.getContent();
        for (Feed feed : pagedFeeds) {
            int likeCount = externalLikeService.getLikeCount(
                    new LikeDTO(LikeType.FEED, ViewType.HOTTOPICCHANNEL, feed.getUser().getUserId(), feed.getId())
            );
            feed.setLikesCount(likeCount);
        }
        feedRepository.saveAll(pagedFeeds);

        // 동기화된 데이터로 DTO 변환
        return feedPage.map(feed -> {
            boolean isLiked = externalLikeService.checkUserLike(
                    userId,
                    LikeType.FEED,
                    ViewType.HOTTOPICCHANNEL,
                    feed.getId()
            );
            feed.setIsLike(isLiked);

            // feed의 isReupLoadedBy를 통해 현재 사용자의 share 여부 확인
            boolean isShared = feed.isReupLoadedBy(userId);

            FeedsResponseDTO responseDTO = converterFacade.fromEntity(feed,userId);
            responseDTO.setIsShare(isShared);  // FeedsResponseDTO에 setIsShare 메서드가 있다고 가정

            return responseDTO;
        });
    }
}