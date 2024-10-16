package com.outsider.masterofpredictionbackend.feed.command.application.service.converter;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.util.DTOConverter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class FeedCreateDTOConverter implements DTOConverter<FeedCreateDTO, Feed> {

    private final UserDTOConverter userDTOConverter;
    private final GuestDTOConverter guestDTOConverter;

    @Autowired
    public FeedCreateDTOConverter(UserDTOConverter userDTOConverter,GuestDTOConverter guestDTOConverter){
        this.userDTOConverter = userDTOConverter;
        this.guestDTOConverter = guestDTOConverter;
    }

    @Override
    public Feed toEntity(FeedCreateDTO dto) {
        return getFeed(dto);
    }

    @Override
    public FeedCreateDTO fromEntity(Feed entity) {
        return null;
    }

    @NotNull
    public Feed getFeed(FeedCreateDTO dto) {
        Feed feed = new Feed(
                dto.getAuthorType(),
                dto.getTitle(),
                dto.getContent(),
                LocalDateTime.now(),
                null,
                0,
                0,
                0,
                0,
                userDTOConverter.toEntity(dto.getUser()),
                new Guest(),//수정요청
                new Channel(),//수정요청
                false,
                new ArrayList<>(),
                new ArrayList<>()
        );

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

    @Override
    public Class<FeedCreateDTO> getDtoClass(){
        return FeedCreateDTO.class;
    }
}
