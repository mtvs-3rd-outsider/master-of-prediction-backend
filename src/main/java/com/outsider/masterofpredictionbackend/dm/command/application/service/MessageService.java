package com.outsider.masterofpredictionbackend.dm.command.application.service;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Message;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.MessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Page<Message> getMessagesByRoomId(String roomId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sent"));
        return messageRepository.findByRoomIdOrderBySentDesc(roomId, pageable);
    }
}
