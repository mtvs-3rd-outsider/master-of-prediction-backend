package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.ImageFileRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.YouTubeVideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedUpdateService {
    private final FeedRepository feedRepository;
    private final ImageFileRepository imageFileRepository;
    private final YouTubeVideoRepository youtubeVideoRepository;

    @Autowired
    public FeedUpdateService(FeedRepository feedRepository,
                             ImageFileRepository imageFileRepository,
                             YouTubeVideoRepository youtubeVideoRepository) {
        this.feedRepository = feedRepository;
        this.imageFileRepository = imageFileRepository;
        this.youtubeVideoRepository = youtubeVideoRepository;
    }

    // Feed 수정 메서드
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        // Feed 정보 업데이트
        feed=feedUpdateDTO.updateFeed(feed);

        // 이미지 파일 업데이트
        updateImageFiles(feed, feedUpdateDTO.getImageFiles());

        // YouTube 비디오 업데이트
        updateYouTubeVideos(feed, feedUpdateDTO.getYouTubeVideos());

        feedRepository.save(feed);
    }

    private void updateImageFiles(Feed feed, List<ImageFile> newImageFiles) {
        // 기존 이미지 파일 삭제
        imageFileRepository.deleteAll(feed.getImageFiles());
        feed.getImageFiles().clear();

        // 새 이미지 파일 추가
        for (ImageFile imageFile : newImageFiles) {
            feed.getImageFiles().add(imageFile);
        }
    }

    private void updateYouTubeVideos(Feed feed, List<YouTubeVideo> newYoutubeVideos) {
        // 기존 YouTube 비디오 삭제
        youtubeVideoRepository.deleteAll(feed.getYoutubeVideos());
        feed.getYoutubeVideos().clear();

        // 새 YouTube 비디오 추가
        for (YouTubeVideo youtubeVideo : newYoutubeVideos) {
            feed.getYoutubeVideos().add(youtubeVideo);
        }
    }
}