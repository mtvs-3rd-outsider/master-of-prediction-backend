package com.outsider.masterofpredictionbackend.dm.command.domain.aggregate;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "content_type", nullable = false)
    private String contentTypeStr;  // DB의 문자열 값

    @Column(name = "sent", nullable = false)
    private Instant sent;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "user_avatar_image_link")
    private String userAvatarImageLink;

    @Column(name = "room_id", nullable = false)
    private String roomId;

    @Column(name = "reply_to_message_id")
    private Integer replyToMessageId;

    // getters and setters
}
