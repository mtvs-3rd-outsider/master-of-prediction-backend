package com.outsider.masterofpredictionbackend.feed.command.application.service;
import ch.qos.logback.core.net.SyslogOutputStream;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedImageFileRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class FeedCreateService {
    private final FeedRepository feedRepository;
    private final FeedImageFileRepository imageFileRepository;
    @Autowired
    public FeedCreateService(FeedRepository feedRepository, FeedImageFileRepository imageFileRepository) {
        this.feedRepository = feedRepository;
        this.imageFileRepository = imageFileRepository;
    }
    // Feed 생성 메서드
    @Transactional
    public Long createFeed(FeedCreateDTO feedCreateDTO, List<String> fileUrls) {
        Feed feed = feedCreateDTO.toEntity();
        Feed saveFeed=feedRepository.save(feed);

        for(String fileUrl : fileUrls) {
            ImageFile imageFile = new ImageFile();
            imageFile.setFeed(saveFeed);
            imageFile.setImageUrl(fileUrl);
            imageFileRepository.save(imageFile);
        }

        return saveFeed.getId();
    }
}