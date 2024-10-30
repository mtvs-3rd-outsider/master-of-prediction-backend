package com.outsider.masterofpredictionbackend.dm.command.application.service;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Message;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.MessageRepository;
import com.outsider.masterofpredictionbackend.dm.query.MessageMapper;
import com.outsider.masterofpredictionbackend.dm.query.MessageVM;
import com.outsider.masterofpredictionbackend.dm.query.ReactionVM;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public Page<MessageVM> getMessagesByRoomId(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sent"));
        Page<Message> messagePage = messageRepository.findByRoomIdOrderBySentDesc(roomId, pageable);

        List<MessageVM> messageVMList = messagePage.getContent().stream()
                .map(messageMapper::toMessageVMWithReactions)
                .collect(Collectors.toList());

        return new PageImpl<>(messageVMList, pageable, messagePage.getTotalElements());
    }


    public Page<MessageVM> getMessagesByRoomId_old(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sent"));
        Page<Message> messagePage = messageRepository.findByRoomIdOrderBySentDesc(roomId, pageable);

        // Convert each Message entity to MessageVM using the mapper
        List<MessageVM> messageVMList = messagePage.getContent().stream()
                .map(messageMapper::toMessageVM)
                .collect(Collectors.toList());

        return new PageImpl<>(messageVMList, pageable, messagePage.getTotalElements());
    }
}
