package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Message;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.MessageReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    // Message의 개별 필드를 UserVM으로 매핑
    @Mapping(target = "reactions", ignore = true)
    @Mapping(target = "user.name", source = "user.displayName")
    @Mapping(target = "user.userName", source = "user.userName")
    @Mapping(target = "user.avatarImageLink", source = "user.userImg")
    @Mapping(target = "user.id", source = "user.id")
    MessageVM toMessageVM(Message message);
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "displayName", source = "user.displayName")
    @Mapping(target = "userImg", source = "user.userImg")
    ReactionVM toReactionVM(MessageReaction reaction);

    // Message에서 Reaction 리스트를 포함한 MessageVM 생성
    default MessageVM toMessageVMWithReactions(Message message) {
        MessageVM messageVM = toMessageVM(message);
        List<ReactionVM> reactionVMs = message.getReactions().stream()
                .map(this::toReactionVM)
                .collect(Collectors.toList());
        messageVM.setReactions(reactionVMs);
        return messageVM;
    }
}
