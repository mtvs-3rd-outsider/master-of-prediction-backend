package com.outsider.masterofpredictionbackend.dm.query;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChatThreadDTO {

    private Long chatRoomId;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Boolean isGroupThread;

}
