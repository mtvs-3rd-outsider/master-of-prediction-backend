package com.outsider.masterofpredictionbackend.dm.command.application.controller;

import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadResponse;

import com.outsider.masterofpredictionbackend.dm.command.application.service.ChatThreadService;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.query.ChatThreadDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/v1/chat-threads")
@RequiredArgsConstructor
public class ChatThreadController {
    private final ChatThreadService chatThreadService;
    // 채팅방 생성 또는 1:1 채팅방 조회 API
    @PostMapping("/create")
    public ResponseEntity<CreateChatThreadResponse> createChatThread(@RequestBody CreateChatThreadDTO createChatThreadDTO,
                                                                     @UserId CustomUserInfoDTO userInfo) {
        CreateChatThreadResponse chatThreadDTO = chatThreadService.createOrGetThread(createChatThreadDTO, userInfo.getUserId());
        return ResponseEntity.ok(chatThreadDTO);
    }
    // 특정 사용자가 참여한 채팅방 목록을 페이징 처리하여 조회하는 API
    @GetMapping("/user-threads")
    public ResponseEntity<Page<ChatThreadDTO>> getThreadsByUserId(@UserId CustomUserInfoDTO userInfo, Pageable pageable) {
        Page<ChatThreadDTO> threads = chatThreadService.getThreadsByUserId(userInfo.getUserId(), pageable);
        return ResponseEntity.ok(threads);
    }
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatThread> getThread(@PathVariable Long chatRoomId) {
        Optional<ChatThread> chatThread = chatThreadService.getThread(chatRoomId);

        return chatThread
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // 채팅방 삭제 API
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<Void> deleteChatThread(@PathVariable Long chatRoomId, @UserId CustomUserInfoDTO userInfoDTO) {
        chatThreadService.deactivateParticipant(chatRoomId,userInfoDTO.getUserId());
        return ResponseEntity.noContent().build();
    }
}
