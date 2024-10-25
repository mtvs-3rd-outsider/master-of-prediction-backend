package com.outsider.masterofpredictionbackend.dm.command.application.dto;

import lombok.Data;

@Data

public class UpdateMessageDTO {

    private Long senderId;
    private Long receiverId;
    private String lastMessage;
    public UpdateMessageDTO() {}
}
