package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadResponse;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.ParticipantDTO;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ChatThreadMapper {
    CreateChatThreadResponse toCreateChatThreadResponse(ChatThread chatThread);

    // ChatThread -> ChatThreadDTO 변환
    @Mapping(target = "participants", source = "participants", qualifiedByName = "toParticipantDTOs")
    ChatThreadDTO toChatThreadDTO(ChatThread chatThread);

    // Participant -> ParticipantDTO 변환
    // Participant -> ParticipantDTO 변환
    @Mapping(target = "userId", source = "user.id") // User의 ID를 ParticipantDTO의 userId로 매핑
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "displayName", source = "user.displayName")
    @Mapping(target = "avatarUrl", source = "user.userImg")
    ParticipantDTO toParticipantDTO(Participant participant);
    // List<Participant> -> List<ParticipantDTO> 변환
    @Named("toParticipantDTOs")
    List<ParticipantDTO> toParticipantDTOs(List<Participant> participants);
}
