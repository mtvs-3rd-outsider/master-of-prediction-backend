package com.outsider.masterofpredictionbackend.dm.command.application.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.application.service.ChatThreadService;
import com.outsider.masterofpredictionbackend.dm.query.ChatThreadDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/chat-threads")
@RequiredArgsConstructor
public class ChatThreadController {

    private final ChatThreadService chatThreadService;
    // 사용자가 특정 채팅방에 새로운 메시지를 실시간으로 수신할 수 있는 SSE 연결
    @GetMapping(value = "/{chatRoomId}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToChatRoom(@PathVariable Long chatRoomId, @UserId CustomUserInfoDTO userInfo) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 시간 제한 없음

        // 구독 처리 로직 (메시지 전송을 위한 서비스 연결)
        chatThreadService.subscribeToChatRoom(chatRoomId, userInfo.getUserId(), emitter);

        return emitter;
    }
    // 채팅방 생성 또는 1:1 채팅방 조회 API
    @PostMapping("/create")
    public ResponseEntity<ChatThreadDTO> createChatThread(@RequestBody CreateChatThreadDTO createChatThreadDTO,
                                                          @UserId CustomUserInfoDTO userInfo) {
        ChatThreadDTO chatThreadDTO = chatThreadService.createOrGetThread(createChatThreadDTO, userInfo.getUserId());
        return ResponseEntity.ok(chatThreadDTO);
    }

    // 사용자가 쓰레드에 참여했을 때 상태 업데이트 API
    @PostMapping("/{chatRoomId}/join")
    public ResponseEntity<Void> userJoinedThread(@PathVariable Long chatRoomId, @UserId CustomUserInfoDTO userInfo) {
        chatThreadService.userJoinedThread(chatRoomId, userInfo.getUserId());
        return ResponseEntity.ok().build();
    }

    // 사용자가 쓰레드에서 나갔을 때 상태 업데이트 API
    @PostMapping("/{chatRoomId}/leave")
    public ResponseEntity<Void> userLeftThread(@PathVariable Long chatRoomId, @UserId CustomUserInfoDTO userInfo) {
        chatThreadService.userLeftThread(chatRoomId, userInfo.getUserId());
        return ResponseEntity.ok().build();
    }

    // 사용자가 메시지를 읽었을 때 상태 업데이트 API
    @PostMapping("/{chatRoomId}/read")
    public ResponseEntity<Void> markMessageAsRead(@PathVariable Long chatRoomId, @UserId CustomUserInfoDTO userInfo) {
        chatThreadService.markMessageAsRead(chatRoomId, userInfo.getUserId());
        return ResponseEntity.ok().build();
    }

    // 채팅방의 마지막 메시지와 시간 업데이트 API
    @PostMapping("/{chatRoomId}/update-message")
    public ResponseEntity<Void> updateLastMessage(@PathVariable Long chatRoomId, @RequestParam String lastMessage, @RequestBody UpdateMessageDTO updateMessageDTO) throws JsonProcessingException {
        chatThreadService.updateLastMessage(chatRoomId, lastMessage, updateMessageDTO);
        return ResponseEntity.ok().build();
    }

    // 특정 채팅방 조회 API
    @GetMapping("/{chatRoomId}")
    public ResponseEntity<ChatThread> getThread(@PathVariable Long chatRoomId) {
        Optional<ChatThread> thread = chatThreadService.getThread(chatRoomId);
        return thread.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 특정 사용자가 참여한 채팅방 목록을 페이징 처리하여 조회하는 API
    @GetMapping("/user-threads")
    public ResponseEntity<Page<ChatThread>> getThreadsByUserId(@UserId CustomUserInfoDTO userInfo, Pageable pageable) {
        Page<ChatThread> threads = chatThreadService.getThreadsByUserId(userInfo.getUserId(), pageable);
        return ResponseEntity.ok(threads);
    }

    // 특정 사용자가 현재 쓰레드에 접속 중인지 여부 확인 API
    @GetMapping("/{chatRoomId}/is-online")
    public ResponseEntity<Boolean> isUserOnline(@PathVariable Long chatRoomId, @UserId CustomUserInfoDTO userInfo) {
        boolean isOnline = chatThreadService.isUserOnline(chatRoomId, userInfo.getUserId());
        return ResponseEntity.ok(isOnline);
    }
}
