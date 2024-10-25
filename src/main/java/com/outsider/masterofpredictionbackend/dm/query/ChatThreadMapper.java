package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChatThreadMapper {
    ChatThreadDTO toChatThreadDTO(ChatThread chatThread);
}
