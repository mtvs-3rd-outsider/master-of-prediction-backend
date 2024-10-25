package com.outsider.masterofpredictionbackend.dm.command.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateChatThreadDTO {
    private Long otherUserId;  // 1:1 채팅인 경우 상대방 ID
    private Boolean isGroupThread;  // 그룹 채팅 여부
}
