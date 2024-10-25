package com.outsider.masterofpredictionbackend.dm.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DMThreadDTO {
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String receiverName;
    private String receiverDisplayName;
    private String receiverImg;
    private Boolean lastMessageRead;
    private LocalDateTime lastMessageTime;
    private String lastMessage;
    // Getters and Setters
}
