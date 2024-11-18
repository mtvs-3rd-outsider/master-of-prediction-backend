package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.QuoteFeed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.QuoteUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;
@Component
public class FeedCreateDTOConverter {

    private final UserDTOConverter userDTOConverter;
    private final GuestDTOConverter guestDTOConverter;

    @Autowired
    public FeedCreateDTOConverter(UserDTOConverter userDTOConverter,GuestDTOConverter guestDTOConverter){
        this.userDTOConverter = userDTOConverter;
        this.guestDTOConverter = guestDTOConverter;
    }


    public Feed toEntity(FeedCreateDTO dto) {
        return getFeed(dto);
    }


    public FeedCreateDTO fromEntity(Feed entity) {
        return null;
    }

    @NotNull
    public Feed getFeed(FeedCreateDTO dto) {
        Feed feed = new Feed(
                dto.getAuthorType(),
                dto.getContent(),
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                0,
                0,
                0,
                0,
                userDTOConverter.toEntity(dto.getUser()),
                (dto.getGuest()!=null)?new Guest(dto.getGuest().getGuestId(),dto.getGuest().getGuestPassword()):new Guest(),//수정요청
                dto.getChannel(),//수정요청
                dto.getIsQuote(), // isQuote 추가
                new ArrayList<>(),
                new ArrayList<>()
        );

        // 인용된 게시글 정보 설정
        if (dto.getQuoteFeed() != null) {
            QuoteFeed quoteFeed = new QuoteFeed(
                    dto.getQuoteFeed().getQuoteId(),
                    dto.getQuoteFeed().getQuoteContent(),
                    dto.getQuoteFeed().getQuoteCreateAt(),
                    new QuoteUser(dto.getQuoteFeed().getQuoteUser().getUserId()).getQuoteUserId(),
                    dto.getQuoteFeed().getMediaFileUrls(),
                    dto.getQuoteFeed().getYoutubeUrls()
            );
            feed.setQuoteFeed(quoteFeed);
        }

        if (dto.getMediaFileUrls() != null) {
            feed.setMediaFiles(dto.getMediaFileUrls().stream()
                    .map(url -> new MediaFile(url, feed))
                    .collect(Collectors.toList()));
        }

        if (dto.getYoutubeUrls() != null) {
            feed.setYoutubeVideos(dto.getYoutubeUrls().stream()
                    .map(url -> new YouTubeVideo(url, feed))
                    .collect(Collectors.toList()));
        }
        return feed;
    }
}
