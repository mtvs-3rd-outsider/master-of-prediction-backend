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
import java.util.List;

@Data
public class FeedCreateDTO {
    private AuthorType authorType;
    private String title;
    private String content;
    private ChannelType channelType;
    private User user;
    private Guest guest;
    private List<ImageFile> imageFiles;
    private List<YouTubeVideo> youTubeVideos;

    // DTO를 Feed 엔티티로 변환하는 메서드
    public Feed toEntity(FeedCreateDTO feedCreateDTO) {
        return new Feed(
                feedCreateDTO.authorType,
                feedCreateDTO.title,
                feedCreateDTO.content,
                LocalDateTime.now(),
                null, // createdAt이기 때문에 최초 생성 시 updatedAt은 null로 설정
                feedCreateDTO.channelType,
                0, // 초기 조회수
                0, // 초기 좋아요 수
                0, // 초기 댓글 수
                feedCreateDTO.user,
                feedCreateDTO.guest,
                null, // 초기 좋아요 리스트
                feedCreateDTO.imageFiles,
                feedCreateDTO.youTubeVideos
        );
    }

}
