package com.outsider.masterofpredictionbackend.dm.command.application.dto;

public class DMThreadCreateDTO {
    private Long senderId;
    private Long receiverId;

    // Getters and Setters
    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
