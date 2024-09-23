package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate;


import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.Content;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.aggregate.embedded.WriterInfo;
import com.outsider.masterofpredictionbackend.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "category_channel_comment")
public class CategoryChannelComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long channelId;

    @Embedded
    private WriterInfo writer;

    public void setContent(Content content) {
        this.content = content;
    }

    @Embedded
    private Content content;

    protected CategoryChannelComment() {}

    public CategoryChannelComment(WriterInfo writer, Content content, Long channelId) {
        this.writer = writer;
        this.content = content;
        this.channelId = channelId;
    }
}
