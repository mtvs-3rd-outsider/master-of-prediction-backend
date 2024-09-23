package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedUpdateService {
    private final FeedRepository feedRepository;
    private final FileService fileService;

    @Autowired
    public FeedUpdateService(FeedRepository feedRepository, FileService fileService) {
        this.feedRepository = feedRepository;
        this.fileService = fileService;
    }

    @Transactional
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO, List<MultipartFile> files, List<String> youtubeUrls) throws Exception {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        // Update basic feed information
        feed.setTitle(feedUpdateDTO.getTitle());
        feed.setContent(feedUpdateDTO.getContent());
        feed.setChannelType(feedUpdateDTO.getChannelType());

        // Clear existing files and YouTube videos
        feed.getMediaFiles().clear();
        feed.getYoutubeVideos().clear();

        // Add new files
        if (files != null && !files.isEmpty()) {
            List<String> fileUrls = files.stream()
                    .map(file -> {
                        try {
                            return fileService.uploadFile(file);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to upload file", e);
                        }
                    })
                    .collect(Collectors.toList());

            fileUrls.forEach(url -> feed.getMediaFiles().add(new MediaFile(url, feed)));
        }

        // Add new YouTube videos
        if (youtubeUrls != null && !youtubeUrls.isEmpty()) {
            youtubeUrls.forEach(url -> feed.getYoutubeVideos().add(new YouTubeVideo(url, feed)));
        }

        feedRepository.save(feed);
    }
}