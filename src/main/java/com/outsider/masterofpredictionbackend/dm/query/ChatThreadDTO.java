package com.outsider.masterofpredictionbackend.dm.query;


import com.outsider.masterofpredictionbackend.dm.command.application.dto.ParticipantDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ChatThreadDTO {

    private Long chatRoomId;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private Boolean isGroupThread;
    private List<ParticipantDTO> participants;
}
