package com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model;


import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.bettingChannelComment.command.domain.model.embedded.WriterInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "betting_channel_comment")
public class BettingChannelComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    WriterInfo writer;

    @Embedded
    Content content;
}
