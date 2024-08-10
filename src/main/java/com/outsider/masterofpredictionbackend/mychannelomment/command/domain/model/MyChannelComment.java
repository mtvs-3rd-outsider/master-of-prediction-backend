package com.outsider.masterofpredictionbackend.mychannelomment.command.domain.model;

import com.outsider.masterofpredictionbackend.mychannelomment.command.domain.model.embedded.Content;
import com.outsider.masterofpredictionbackend.mychannelomment.command.domain.model.embedded.WriterInfo;
import jakarta.persistence.*;

@Entity
@Table(name = "my_channel_comment")
public class MyChannelComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Embedded
    WriterInfo writer;

    @Embedded
    Content content;
}
