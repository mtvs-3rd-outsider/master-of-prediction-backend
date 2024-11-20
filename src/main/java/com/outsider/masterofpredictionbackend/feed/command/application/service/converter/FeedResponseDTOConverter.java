package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.QuoteFeedDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
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


    public FeedResponseDTO fromEntity(Feed feed, Long currentUserId) {
        QuoteFeedDTO quoteFeedDTO = null;
        if (feed.getQuoteFeed() != null) {
            // QuoteFeed의 userId를 사용하여 UserDTO를 가져옴
            UserDTO quoteUserDTO = feed.getQuoteFeed().getQuoteUserId() != null ?
                    externalUserService.getUser(feed.getQuoteFeed().getQuoteUserId()) : null;

            quoteFeedDTO = new QuoteFeedDTO(
                    feed.getQuoteFeed().getQuoteId(),
                    feed.getQuoteFeed().getQuoteContent(),
                    feed.getQuoteFeed().getQuoteCreateAt(),
                    feed.getQuoteFeed().getMediaFileUrls(),
                    feed.getQuoteFeed().getYoutubeUrls(),
                    quoteUserDTO,
                    null
            );
        }

        return new FeedResponseDTO(
                feed.getId(),
                feed.getAuthorType(),
                feed.getContent(),
                feed.getCreatedAt(),
                feed.getShortAt(),
                feed.getUpdatedAt(),
                feed.getViewCount(),
                (feed.getAuthorType()==AuthorType.USER) ? externalUserService.getUser(feed.getUser().getUserId()) : null,
                null,
                feed.getMediaFiles(),
                feed.getYoutubeVideos(),
                null,
                null,
                feed.getIsLike(),
                feed.getLikesCount(),
                feed.getCommentsCount(),
                feed.getShareCount(),
                feed.getAuthorType() == AuthorType.USER && feed.isReupLoadedBy(currentUserId),
                feed.getIsquote(),
                quoteFeedDTO
        );
    }
}
