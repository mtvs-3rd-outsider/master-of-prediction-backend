package com.outsider.masterofpredictionbackend.dm.command.application.controller;


import com.outsider.masterofpredictionbackend.dm.command.application.dto.DMThreadCreateDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.service.DMThreadService;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/dmthreads")
public class DMThreadController {

    @Autowired
    private DMThreadService dmThreadService;

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
}
