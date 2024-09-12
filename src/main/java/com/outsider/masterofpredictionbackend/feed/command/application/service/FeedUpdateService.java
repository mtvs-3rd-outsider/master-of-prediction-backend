package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.MediaFileRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.YouTubeVideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedUpdateService {
    private final FeedRepository feedRepository;
    @Autowired
    public FeedUpdateService(FeedRepository feedRepository,
                             MediaFileRepository mediaFileRepository,
                             YouTubeVideoRepository youtubeVideoRepository) {
        this.feedRepository = feedRepository;
    }

    // Feed 수정 메서드
    @Transactional
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO, List<String> fileUrls) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        // Update basic feed information
        feed.setTitle(feedUpdateDTO.getTitle());
        feed.setContent(feedUpdateDTO.getContent());
        feed.setUpdatedAt(LocalDateTime.now());
        feed.setChannelType(feedUpdateDTO.getChannelType());

        // Clear existing files
        feed.getMediaFiles().clear();
        feed.getYoutubeVideos().clear();

        // Add new files
        if (fileUrls != null && !fileUrls.isEmpty()) {
            for (String url : fileUrls) {
                if (url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".jpeg") || url.toLowerCase().endsWith(".png") || url.toLowerCase().endsWith(".gif")) {
                    feed.getMediaFiles().add(new MediaFile(url));
                } else if (url.toLowerCase().endsWith(".mp4") || url.toLowerCase().endsWith(".avi") || url.toLowerCase().endsWith(".mov")) {
                    feed.getYoutubeVideos().add(new YouTubeVideo(url));
                }
            }
        }

        feedRepository.save(feed);
    }
}