package com.outsider.masterofpredictionbackend.feed.command.application.dto;

import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.Feed;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.MediaFile;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.YouTubeVideo;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Guest;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.User;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.AuthorType;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class FeedResponseDTO {
    private long id;
    private AuthorType authorType;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private int viewCount;
    private ChannelType channelType;
    private UserDTO user;
    private Guest guest;
    private List<MediaFile> mediaFiles;
    private List<YouTubeVideo> youTubeVideos;
    private List<CommentDTO> commentDTOS;
    private List<ReplyDTO> replyDTOS;
    private int likesCount;
    private int commentsCount;
    private int quoteCount;


    public void setCommentsWithReplies(Map<Long, List<CommentDTO>> commentsWithReplies) {
        this.commentDTOS = new ArrayList<>();

        for (Map.Entry<Long, List<CommentDTO>> entry : commentsWithReplies.entrySet()) {
            CommentDTO parentComment = entry.getValue().get(0); // 첫 번째 요소는 부모 댓글
            List<CommentDTO> replies = entry.getValue().subList(1, entry.getValue().size()); // 나머지는 답글들

            parentComment.setReplies(replies);
            this.commentDTOS.add(parentComment);
        }
    }
}