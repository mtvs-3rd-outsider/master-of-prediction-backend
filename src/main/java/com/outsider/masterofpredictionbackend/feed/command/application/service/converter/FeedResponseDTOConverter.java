package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.springframework.stereotype.Component;

@Component
public class FeedResponseDTOConverter implements DTOConverter<FeedResponseDTO, Feed> {
    private final UserDTOConverter userDTOConverter;
    private final GuestDTOConverter guestDTOConverter;

    public FeedResponseDTOConverter(UserDTOConverter userDTOConverter, GuestDTOConverter guestDTOConverter) {
        this.userDTOConverter = userDTOConverter;
        this.guestDTOConverter = guestDTOConverter;
    }


    @Override
    public Feed toEntity(FeedResponseDTO dto) {
        return null;
    }

    @Override
    public FeedResponseDTO fromEntity(Feed feed) {
        return new FeedResponseDTO(
                feed.getId(),
                feed.getAuthorType(),
                feed.getTitle(),
                feed.getContent(),
                feed.getCreatedAt(),
                feed.getUpdatedAt(),
                feed.getViewCount(),
                feed.getUser().getUserId() != null ?userDTOConverter.fromEntity(feed.getUser()) : null,
                guestDTOConverter.fromEntity(feed.getGuest()),
                feed.getMediaFiles(),
                feed.getYoutubeVideos(),
                null,
                null,
                feed.getLikesCount(),
                feed.getCommentsCount(),
                feed.getQuoteCount()
        );
    }

    @Override
    public Class<FeedResponseDTO> getDtoClass() {
        return FeedResponseDTO.class;
    }
}
