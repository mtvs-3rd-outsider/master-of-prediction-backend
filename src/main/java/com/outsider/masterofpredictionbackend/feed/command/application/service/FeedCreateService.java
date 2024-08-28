package com.outsider.masterofpredictionbackend.feed.command.application.service;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.ImageFileRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.YouTubeVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FeedCreateService {
    private final FeedRepository feedRepository;
    private final ImageFileRepository imageFileRepository;
    private final YouTubeVideoRepository youtubeVideoRepository;

    @Autowired
    public FeedCreateService(FeedRepository feedRepository,
                             ImageFileRepository imageFileRepository,
                             YouTubeVideoRepository youtubeVideoRepository
    ) {
        this.feedRepository = feedRepository;
        this.imageFileRepository = imageFileRepository;
        this.youtubeVideoRepository = youtubeVideoRepository;
    }

    // Feed 생성 메서드
    public void createFeed(FeedCreateDTO feedCreateDTO) {

        // Feed 엔티티 생성
        Feed feed = feedCreateDTO.toEntity(feedCreateDTO);

        // Feed 저장
        feedRepository.save(feed);

        // 이미지 파일 저장
        assert feedCreateDTO.getImageFiles() != null;
        imageFileRepository.saveAll(feedCreateDTO.getImageFiles());

        // YouTube 비디오 저장
        assert feedCreateDTO.getYouTubeVideos() != null;
        youtubeVideoRepository.saveAll(feedCreateDTO.getYouTubeVideos());
    }
}