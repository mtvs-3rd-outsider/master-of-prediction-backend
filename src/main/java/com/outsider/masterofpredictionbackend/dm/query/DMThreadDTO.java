package com.outsider.masterofpredictionbackend.dm.query;

import lombok.Data;

@Data
public class DMThreadDTO {
    private Long senderId;
    private Long receiverId;
    private String senderName;
    private String receiverName;
    private String receiverDisplayName;
    private String receiverImg;

    // Getters and Setters
}
