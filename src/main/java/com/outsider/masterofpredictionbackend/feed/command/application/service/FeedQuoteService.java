package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.QuoteFeed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.user.query.application.eventhandler.ChannelRequestHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeedQuoteService {
    private final FeedRepository feedRepository;
    private final ChannelRequestHandler channelRequestHandler;

    @Autowired
    public FeedQuoteService(FeedRepository feedRepository, ChannelRequestHandler channelRequestHandler) {
        this.feedRepository = feedRepository;
        this.channelRequestHandler = channelRequestHandler;
    }

    public Long quoteFeed(Long originalFeedId, FeedCreateDTO feedCreateDTO, Long userId) {
        Feed originalFeed = feedRepository.findById(originalFeedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + originalFeedId));

        if (originalFeed.getIsquote()) {
            throw new IllegalStateException("이미 인용된 게시물은 다시 인용할 수 없습니다.");
        }

        originalFeed.setShareCount(originalFeed.getShareCount() + 1);
        feedRepository.save(originalFeed);

        // Create QuoteFeed with only userId
        QuoteFeed quoteFeed = new QuoteFeed(
                originalFeed.getId(),
                originalFeed.getContent(),
                originalFeed.getCreatedAt(),
                originalFeed.getUser().getUserId(),  // userId만 저장
                getMediaFileUrls(originalFeed.getMediaFiles()),
                getYoutubeUrls(originalFeed.getYoutubeVideos())
        );

        // Create new feed with quote
        Feed newFeed = new Feed();
        newFeed.setQuoteFeed(quoteFeed);
        newFeed.setIsquote(true);
        newFeed.setAuthorType(AuthorType.USER);
        newFeed.setContent(feedCreateDTO.getContent());
        newFeed.setCreatedAt(LocalDateTime.now());
        newFeed.setShortAt(LocalDateTime.now());
        newFeed.setUser(new User(userId));
//        newFeed.setChannel(feedCreateDTO.getChannel());
        newFeed.setChannel(feedCreateDTO.getChannel());
        Feed savedFeed = feedRepository.save(newFeed);
        return savedFeed.getId();
    }
    private List<String> getMediaFileUrls(List<MediaFile> mediaFiles) {
        return mediaFiles.stream()
                .map(MediaFile::getFileUrl)
                .collect(Collectors.toList());
    }

    private List<String> getYoutubeUrls(List<YouTubeVideo> youtubeVideos) {
        return youtubeVideos.stream()
                .map(YouTubeVideo::getYoutubeUrl)
                .collect(Collectors.toList());
    }


    public void cancelQuote(Long feedId, Long userId) {
        Feed quotedFeed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        Feed originalFeed = feedRepository.findById(quotedFeed.getQuoteFeed().getQuoteId())
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id" ));
        originalFeed.setShareCount(originalFeed.getShareCount()-1);

        feedRepository.save(originalFeed);


        // Check if the feed is actually a quote
        if (!quotedFeed.getIsquote()) {
            throw new IllegalStateException("This feed is not a quote and cannot be unquoted.");
        }

        // Check if the user is the owner of the quoted feed
        if (!quotedFeed.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("Only the creator of the quote can cancel it.");
        }

        // Check if the feed has any interactions (likes, comments, etc.)
        if (hasInteractions(quotedFeed)) {
            throw new IllegalStateException("Cannot cancel quote as it already has interactions (likes, comments, etc.)");
        }

        feedRepository.delete(quotedFeed);
    }

    private boolean hasInteractions(Feed feed) {
        return feed.getLikesCount() > 0 ||
                feed.getCommentsCount() > 0 ||
                feed.getShareCount() > 0 ||
                feed.getViewCount() > 10; // 조회수는 어느 정도 허용
    }
}
