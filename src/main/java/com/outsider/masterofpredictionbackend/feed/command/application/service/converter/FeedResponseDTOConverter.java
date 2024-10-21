package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import org.springframework.stereotype.Component;

@Component
public class FeedResponseDTOConverter {
    private final ExternalUserService externalUserService;


    public FeedResponseDTOConverter(ExternalUserService externalUserService) {
        this.externalUserService = externalUserService;
    }



    public Feed toEntity(FeedResponseDTO dto) {
        return null;
    }


    public FeedResponseDTO fromEntity(Feed feed) {
        return new FeedResponseDTO(
                feed.getId(),
                feed.getAuthorType(),
                feed.getTitle(),
                feed.getContent(),
                feed.getCreatedAt(),
                feed.getUpdatedAt(),
                feed.getViewCount(),
                feed.getUser().getUserId() != null ?  externalUserService.getUser(feed.getUser().getUserId()): null,
                null,
                feed.getMediaFiles(),
                feed.getYoutubeVideos(),
                null,
                null,
                feed.getIsLike(),
                feed.getLikesCount(),
                feed.getCommentsCount(),
                feed.getQuoteCount()
        );
    }
}
