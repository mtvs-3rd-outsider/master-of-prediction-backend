package com.outsider.masterofpredictionbackend.dm.command.application.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.DMThreadCreateDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.service.DMThreadService;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping("/api/v1/dmthreads")
@RequiredArgsConstructor
public class DMThreadController {

    // SSE Emitter 목록 (실시간 업데이트를 구독하는 클라이언트)
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final DMThreadService dmThreadService;
    // 실시간 스트림 시작 (SSE 구독)
    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamMessages() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); // 무기한 유지
        emitters.add(emitter);

        // 연결 종료 시 목록에서 제거
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // SSE로 읽기 상태 및 LastMessage 상태를 전송하는 메서드
    public void sendThreadUpdate(Long threadId, String lastMessage, boolean lastMessageRead) {
        for (SseEmitter emitter : emitters) {
            try {
                // 업데이트 데이터를 JSON 형식으로 전송
                String updateData = String.format(
                        "{\"threadId\": %d, \"lastMessage\": \"%s\", \"lastMessageRead\": %b}",
                        threadId, lastMessage, lastMessageRead
                );
                emitter.send(SseEmitter.event().name("threadUpdate").data(updateData));
            } catch (IOException e) {
                emitters.remove(emitter); // 오류가 발생한 emitter는 목록에서 제거
            }
        }
    }
    // DMThread 생성 또는 조회
    @PostMapping("/create")
    public ResponseEntity<DMThread> createThread(
            @RequestBody DMThreadCreateDTO dmThreadCreateDTO) {
        DMThread thread = dmThreadService.getOrCreateThread(
                dmThreadCreateDTO.getSenderId(),
                dmThreadCreateDTO.getReceiverId()
        );
        return ResponseEntity.ok(thread);
    }
    // 특정 DMThread 조회
    @GetMapping("/get")
    public ResponseEntity<Optional<DMThread>> getThread(
            @RequestParam Long senderId,
            @RequestParam Long receiverId) {
        Optional<DMThread> thread = dmThreadService.getThread(senderId, receiverId);
        return ResponseEntity.ok(thread);
    }
    // senderId로 DMThread 목록을 페이징 처리하여 조회
    @GetMapping("/sender/{senderId}")
    public ResponseEntity<Page<DMThreadDTO>> getThreadsBySenderId(
            @PathVariable Long senderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<DMThreadDTO> dmThreads = dmThreadService.getThreadsBySenderId(senderId, pageRequest);
        return ResponseEntity.ok(dmThreads);
    }

    // 사용자가 DM 쓰레드에 접속했을 때 호출
    @PostMapping("/join")
    public ResponseEntity<Void> joinThread(@RequestBody DMThreadKey dmThreadKey) {
        dmThreadService.userJoinedThread(dmThreadKey);
        return ResponseEntity.ok().build();
    }

    // 사용자가 DM 쓰레드에서 나갔을 때 호출
    @PostMapping("/leave")
    public ResponseEntity<Void> leaveThread(@RequestBody DMThreadKey dmThreadKey) {
        dmThreadService.userLeftThread(dmThreadKey);
        return ResponseEntity.ok().build();
    }

    // 사용자가 최신 메시지를 읽었을 때 호출
    @PostMapping("/read")
    public ResponseEntity<Void> markMessageAsRead(@RequestBody DMThreadKey dmThreadKey) {
        dmThreadService.markMessageAsRead(dmThreadKey);
        return ResponseEntity.ok().build();
    }

    // 사용자가 현재 쓰레드에 접속 중인지 확인
    @GetMapping("/is-online")
    public ResponseEntity<Boolean> isUserOnline(@RequestParam DMThreadKey dmThreadKey) {
        boolean isOnline = dmThreadService.isUserOnline(dmThreadKey);
        return ResponseEntity.ok(isOnline);
    }

    // 사용자가 최신 메시지를 읽었는지 확인
    @GetMapping("/has-read")
    public ResponseEntity<Boolean> hasUserReadLatestMessage(@RequestParam DMThreadKey dmThreadKey) {
        boolean hasRead = dmThreadService.hasUserReadLatestMessage(dmThreadKey);
        return ResponseEntity.ok(hasRead);
    }

    @PostMapping("/update-message")
    public ResponseEntity<Void> updateLastMessage(@RequestBody UpdateMessageDTO updateMessageDTO, @UserId CustomUserInfoDTO userInfoDTO) throws JsonProcessingException {
        DMThreadKey dmThreadKey = new DMThreadKey(userInfoDTO.getUserId(), updateMessageDTO.getReceiverId());
        dmThreadService.updateLastMessage(dmThreadKey, updateMessageDTO.getLastMessage() ,updateMessageDTO);
        sendThreadUpdate(updateMessageDTO.getReceiverId(), updateMessageDTO.getLastMessage(), false);
        return ResponseEntity.ok().build();
    }
}
