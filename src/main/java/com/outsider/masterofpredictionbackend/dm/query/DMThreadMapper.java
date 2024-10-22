package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface DMThreadMapper {
    @Mapping(source = "sender.userName", target = "senderName")
    @Mapping(source = "receiver.userName", target = "receiverName")
    @Mapping(source = "receiver.displayName", target = "receiverDisplayName")
    @Mapping(source = "receiver.userImg", target = "receiverImg")
    @Mapping(source = "receiver.id", target = "receiverId")
    DMThreadDTO toDTO(DMThread dmThread, User sender, User receiver);


}
