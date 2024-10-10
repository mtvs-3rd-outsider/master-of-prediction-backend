package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedResponseDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedUpdateDTO;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.repository.FeedRepository;
import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalFileService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
//수정 필요
@Service
public class FeedUpdateService {
    private final FeedRepository feedRepository;
    private final ExternalFileService externalFileService;
    private final FeedReadService feedReadService;

    @Autowired
    public FeedUpdateService(FeedRepository feedRepository,ExternalFileService externalFileService,FeedReadService feedReadService) {
        this.feedRepository = feedRepository;
        this.externalFileService = externalFileService;
        this.feedReadService = feedReadService;
    }

    @Transactional
    public void updateFeed(Long feedId, FeedUpdateDTO feedUpdateDTO, List<MultipartFile> files, List<String> youtubeUrls)throws Exception {

        FeedResponseDTO existingFeed = feedReadService.getFeed(feedId);
        validateUpdatePermission(existingFeed, feedUpdateDTO);

        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new EntityNotFoundException("Feed not found with id: " + feedId));

        feed.setTitle(feedUpdateDTO.getTitle());
        feed.setContent(feedUpdateDTO.getContent());

        feed.getMediaFiles().clear();
        feed.getYoutubeVideos().clear();

        if (files != null && !files.isEmpty()) {
            List<String> fileUrls = files.stream()
                    .map(file -> {
                        try {
                            return externalFileService.uploadFile(file);
                        } catch (Exception e) {
                            throw new RuntimeException("Failed to upload file", e);
                        }
                    })
                    .toList();

            fileUrls.forEach(url -> feed.getMediaFiles().add(new MediaFile(url, feed)));
        }

        // Add new YouTube videos
        if (youtubeUrls != null && !youtubeUrls.isEmpty()) {
            youtubeUrls.forEach(url -> feed.getYoutubeVideos().add(new YouTubeVideo(url, feed)));
        }

        feedRepository.save(feed);
    }


    private void validateUpdatePermission(FeedResponseDTO existingFeed, FeedUpdateDTO feedUpdateDTO) {
        if(existingFeed.getAuthorType() == AuthorType.GUEST) {
            if(!(existingFeed.getGuest().getGuestId().equals(feedUpdateDTO.getGuest().getGuestId()))
                    || !(existingFeed.getGuest().getGuestPassword().equals(feedUpdateDTO.getGuest().getGuestPassword()))) {
                throw new AccessDeniedException("아이디 혹은 비밀번호가 틀립니다.");
            }
        }
    }
}