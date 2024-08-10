package com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.model;


import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.categoryChannelComment.command.domain.model.embedded.WriterInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "category_channel_comment")
public class CategoryChannelComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    WriterInfo writer;

    @Embedded
    Content content;
}
