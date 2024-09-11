package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.ImageFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class FeedCreateDTO {
    private AuthorType authorType;
    private String title;
    private String content;
    private ChannelType channelType;
    private User user;
    private Guest guest;
    private List<String> fileUrls; // 파일 URL 리스트로 변경

    // DTO를 Feed 엔티티로 변환하는 메서드
    public Feed toEntity() {
        List<ImageFile> imageFiles = new ArrayList<>();
        List<YouTubeVideo> youTubeVideos = new ArrayList<>();

        if (fileUrls != null) {
            for (String url : fileUrls) {
                if (isImageFile(url)) {
                    imageFiles.add(new ImageFile(url));
                } else if (isVideoFile(url)) {
                    youTubeVideos.add(new YouTubeVideo(url));
                }
            }
        }

        return new Feed(
                this.authorType,
                this.title,
                this.content,
                LocalDateTime.now(),
                null, // createdAt이기 때문에 최초 생성 시 updatedAt은 null로 설정
                this.channelType,
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                0, // 초기 인용수
                this.user,
                this.guest,
                null, // 초기 좋아요 리스트
                imageFiles,
                youTubeVideos
        );
    }

    private boolean isImageFile(String url) {
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.endsWith(".jpg") || lowercaseUrl.endsWith(".jpeg")
                || lowercaseUrl.endsWith(".png") || lowercaseUrl.endsWith(".gif");
    }

    private boolean isVideoFile(String url) {
        String lowercaseUrl = url.toLowerCase();
        return lowercaseUrl.endsWith(".mp4") || lowercaseUrl.endsWith(".avi")
                || lowercaseUrl.endsWith(".mov");
    }
}