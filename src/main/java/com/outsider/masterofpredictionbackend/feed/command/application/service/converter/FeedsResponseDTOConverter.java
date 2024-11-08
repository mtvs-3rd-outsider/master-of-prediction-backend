package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedsResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.GuestDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.QuoteFeedDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FeedsResponseDTOConverter {
    private final ExternalUserService externalUserService;
    private final GuestDTOConverter guestDTOConverter;

    public FeedsResponseDTOConverter(ExternalUserService externalUserService, GuestDTOConverter guestDTOConverter) {
        this.externalUserService = externalUserService;
        this.guestDTOConverter = guestDTOConverter;
    }

    public FeedsResponseDTO fromEntity(Feed feed, Long currentUserId) {
        boolean isReupLoaded = feed.isReupLoadedBy(currentUserId);

        // QuoteFeed 변환 로직 추가
        QuoteFeedDTO quoteFeedDTO = null;
        if (feed.getQuoteFeed() != null) {
            quoteFeedDTO = new QuoteFeedDTO(
                    feed.getQuoteFeed().getQuoteId(),
                    feed.getQuoteFeed().getQuoteContent(),
                    feed.getQuoteFeed().getQuoteCreateAt(),
                    feed.getQuoteFeed().getMediaFileUrls(),
                    feed.getQuoteFeed().getYoutubeUrls(),
                    feed.getQuoteFeed().getQuoteUserId() != null ?
                            externalUserService.getUser(feed.getQuoteFeed().getQuoteUserId()) : null,
                    null
            );
        }

        FeedsResponseDTO dto = new FeedsResponseDTO();
        dto.setId(feed.getId());
        dto.setAuthorType(feed.getAuthorType());
        dto.setContent(feed.getContent());
        dto.setCreatedAt(feed.getCreatedAt());
        dto.setUpdatedAt(feed.getUpdatedAt());
        dto.setViewCount(feed.getViewCount());
        dto.setIsLike(feed.getIsLike());

        if (feed.getUser() != null && feed.getUser().getUserId() != null) {
            dto.setUser(externalUserService.getUser(feed.getUser().getUserId()));
        }

        if (feed.getGuest() != null) {
            dto.setGuest(guestDTOConverter.fromEntity(feed.getGuest()));
        }

        dto.setMediaFileUrls(feed.getMediaFiles().stream()
                .map(MediaFile::getFileUrl)
                .collect(Collectors.toList()));
        dto.setYoutubeUrls(feed.getYoutubeVideos().stream()
                .map(YouTubeVideo::getYoutubeUrl)
                .collect(Collectors.toList()));

        dto.setLikesCount(feed.getLikesCount());
        dto.setCommentsCount(feed.getCommentsCount());
        dto.setShareCount(feed.getShareCount());
        dto.setIsShare(feed.isReupLoadedBy(currentUserId));

        // Quote 관련 필드 설정
        dto.setIsQuote(feed.getIsquote());
        dto.setQuoteFeed(quoteFeedDTO);

        return dto;
    }
}