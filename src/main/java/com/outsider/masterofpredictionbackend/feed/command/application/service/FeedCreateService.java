package com.outsider.masterofpredictionbackend.feed.command.application.service;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class FeedCreateService {
    private final FeedRepository feedRepository;
    @Autowired
    public FeedCreateService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }
    // Feed 생성 메서드
    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO, List<String> fileUrls) {
        Feed feed = feedCreateDTO.toEntity();

        if (fileUrls != null && !fileUrls.isEmpty()) {
            for (String url : fileUrls) {
                if (url.toLowerCase().endsWith(".jpg") || url.toLowerCase().endsWith(".jpeg") || url.toLowerCase().endsWith(".png") || url.toLowerCase().endsWith(".gif")) {
                    feed.getImageFiles().add(new ImageFile(url));
                } else if (url.toLowerCase().endsWith(".mp4") || url.toLowerCase().endsWith(".avi") || url.toLowerCase().endsWith(".mov")) {
                    feed.getYoutubeVideos().add(new YouTubeVideo(url));
                }
            }
        }

        Feed savedFeed = feedRepository.save(feed);
        return savedFeed.getId();
    }
}