package com.outsider.masterofpredictionbackend.dm.query;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.application.service.ChatThreadService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.CustomUserInfoDTO;
import com.outsider.masterofpredictionbackend.util.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat-threads")
@RequiredArgsConstructor
public class ChatThreadQueryController {

    private final ChatThreadService chatThreadService;
    // 특정 사용자가 참여한 채팅방 목록을 페이징 처리하여 조회하는 API
    @GetMapping("/my-threads")
    public ResponseEntity<Page<ChatThread>> getMyThreads(@UserId CustomUserInfoDTO userInfo, Pageable pageable) {
        Page<ChatThread> threads = chatThreadService.getThreadsByUserId(userInfo.getUserId(), pageable);
        return ResponseEntity.ok(threads);
    }

}
