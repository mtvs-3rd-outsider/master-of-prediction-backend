package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.application.service.MessageService;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Message;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/room/{roomId}")
    public Page<Message> getMessages(
            @PathVariable String roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return messageService.getMessagesByRoomId(roomId, page, size);
    }
}
