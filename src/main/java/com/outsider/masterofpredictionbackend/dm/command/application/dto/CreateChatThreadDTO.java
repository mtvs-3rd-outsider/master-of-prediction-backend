package com.outsider.masterofpredictionbackend.dm.command.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CreateChatThreadDTO {
    private Boolean isGroupThread;  // 그룹 채팅 여부
    private List<Long> participantIds = new ArrayList<>();
}
