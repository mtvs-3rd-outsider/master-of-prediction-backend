package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.HotTopicFeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotTopicFeedService {

    private final FeedRepository feedRepository;
    private final ExternalUserService externalUserService;

    @Autowired
    public HotTopicFeedService(FeedRepository feedRepository, ExternalUserService externalUserService) {
        this.feedRepository = feedRepository;
        this.externalUserService = externalUserService;
    }

    public List<HotTopicFeedResponseDTO> getInitialHotTopicFeeds(int size) {
        List<Feed> feeds = feedRepository.findInitialHotTopicFeeds(PageRequest.of(0, size));

        List<HotTopicFeedResponseDTO> feedResponseDTOS = feeds.stream()
                .map(this::fromFeed)
                .toList();



        return feeds.stream()
                .map(this::fromFeed)
                .collect(Collectors.toList());
    }

    public List<HotTopicFeedResponseDTO> getNextHotTopicFeeds(Long lastId, int size) {
        List<Feed> feeds = feedRepository.findHotTopicFeedsAfter(lastId, PageRequest.of(0, size));
        return feeds.stream()
                .map(this::fromFeed)
                .collect(Collectors.toList());
    }


    public HotTopicFeedResponseDTO fromFeed(Feed feed) {
        HotTopicFeedResponseDTO dto = new HotTopicFeedResponseDTO();
        dto.setId(feed.getId());
        dto.setAuthorType(feed.getAuthorType());
        dto.setTitle(feed.getTitle());
        dto.setContent(feed.getContent());
        dto.setCreatedAt(feed.getCreatedAt());
        dto.setUpdatedAt(feed.getUpdatedAt());
        dto.setViewCount(feed.getViewCount());
        dto.setChannelType(feed.getChannelType());

        if (feed.getUser().getUserId() != null) {
            dto.setUser(externalUserService.getUser(feed.getUser().getUserId()));
        }

        if (feed.getGuest() != null) {
            HotTopicFeedResponseDTO.GuestDTO guestDto = new HotTopicFeedResponseDTO.GuestDTO();
            guestDto.setGuestId(feed.getGuest().getGuestId());
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
}