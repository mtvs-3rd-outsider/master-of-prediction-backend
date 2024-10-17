package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.GuestDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FeedsResponseDTOConverter implements DTOConverter<FeedsResponseDTO, Feed> {

    private final ExternalUserService externalUserService;
    private final GuestDTOConverter guestDTOConverter;

    public FeedsResponseDTOConverter(ExternalUserService externalUserService,GuestDTOConverter guestDTOConverter) {
        this.externalUserService = externalUserService;
        this.guestDTOConverter = guestDTOConverter;
    }


    @Override
    public Feed toEntity(FeedsResponseDTO dto) {
        return null;
    }

    @Override
    public FeedsResponseDTO fromEntity(Feed feed) {
        FeedsResponseDTO dto = new FeedsResponseDTO();
        dto.setId(feed.getId());
        dto.setAuthorType(feed.getAuthorType());
        dto.setTitle(feed.getTitle());
        dto.setContent(feed.getContent());
        dto.setCreatedAt(feed.getCreatedAt());
        dto.setUpdatedAt(feed.getUpdatedAt());
        dto.setViewCount(feed.getViewCount());

        if (feed.getUser() != null && feed.getUser().getUserId() != null) {
            dto.setUser(externalUserService.getUser(feed.getUser().getUserId()));
        }

        if (feed.getGuest() != null) {
            GuestDTO guestDto =  guestDTOConverter.fromEntity(feed.getGuest());
            dto.setGuest(guestDto);
        }

        dto.setMediaFileUrls(feed.getMediaFiles().stream()
                .map(MediaFile::getFileUrl)
                .collect(Collectors.toList()));
        dto.setYoutubeUrls(feed.getYoutubeVideos().stream()
                .map(YouTubeVideo::getYoutubeUrl)
                .collect(Collectors.toList()));

        dto.setLikesCount(feed.getLikesCount());
        dto.setCommentsCount(feed.getCommentsCount());
        dto.setQuoteCount(feed.getQuoteCount());
        return dto;
    }

    @Override
    public Class<FeedsResponseDTO> getDtoClass() {
        return FeedsResponseDTO.class;
    }
}
