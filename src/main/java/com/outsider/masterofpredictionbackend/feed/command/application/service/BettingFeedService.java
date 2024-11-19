package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.betting.query.dto.BettingDetailDTO;
import com.outsider.masterofpredictionbackend.betting.query.service.BettingProductQueryService;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.converter.FeedCreateDTOConverter;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalLikeService;
import com.outsider.masterofpredictionbackend.like.command.domain.aggregate.enumtype.LikeType;
import com.outsider.masterofpredictionbackend.like.query.application.dto.LikeCountIdDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service

public class BettingFeedService {
    private final BettingProductQueryService bettingProductQueryService;
    private final FeedRepository feedRepository;
    private final FeedCreateDTOConverter converterFacade;
    private final ExternalLikeService externalLikeService;

    @Autowired
    public BettingFeedService(BettingProductQueryService bettingProductQueryService, FeedRepository feedRepository,FeedCreateDTOConverter feedCreateDTOConverter,ExternalLikeService externalLikeService) {
        this.bettingProductQueryService = bettingProductQueryService;
        this.feedRepository = feedRepository;
        this.converterFacade = feedCreateDTOConverter;
        this.externalLikeService = externalLikeService;
    }



    public void createBettingFeed(Long id) {
        BettingDetailDTO dto = bettingProductQueryService.detail(-id);
        FeedCreateDTO feedCreateDTO = convertBetting(dto);
        Feed feed = converterFacade.toEntity(feedCreateDTO);
        feedRepository.save(feed);
        LikeCountIdDTO likeCountIdDTO = new LikeCountIdDTO(feed.getId(), LikeType.FEED);
        externalLikeService.saveLikeCount(likeCountIdDTO);
    }

    public FeedCreateDTO convertBetting(BettingDetailDTO dto){
        FeedCreateDTO response = new FeedCreateDTO();
        response.setUser(new UserDTO(
                dto.getUser().getUserID(),
                dto.getUser().getUserName(),
                dto.getUser().getDisplayName(),
                Authority.ROLE_USER,
                dto.getUser().getUserImg()
        ));
        response.setChannel(new Channel());//수정
        response.setContent(dto.getProduct().getContent());
        response.setMediaFileUrls(dto.getProductImages());
        response.setAuthorType(AuthorType.USER);
        response.setTitle(dto.getProduct().getTitle());
        return response;
    }
}
