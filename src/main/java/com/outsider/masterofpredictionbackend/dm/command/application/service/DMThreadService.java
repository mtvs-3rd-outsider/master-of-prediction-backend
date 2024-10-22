package com.outsider.masterofpredictionbackend.dm.command.application.service;


import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.DMThreadRepository;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class DMThreadService {

    @Autowired
    private DMThreadRepository dmThreadRepository;

    // DMThread 생성 또는 가져오기
    public DMThread getOrCreateThread(Long senderId, Long receiverId) {
        DMThreadKey threadKey = new DMThreadKey(senderId, receiverId);
        Optional<DMThread> existingThread = dmThreadRepository.findById(threadKey);
        return existingThread.orElseGet(() -> {
            DMThread newThread = new DMThread(threadKey);
            return dmThreadRepository.save(newThread);
        });
    }
    // 특정 DMThread 조회
    public Optional<DMThread> getThread(Long senderId, Long receiverId) {
        DMThreadKey threadKey = new DMThreadKey(senderId, receiverId);
        return dmThreadRepository.findById(threadKey);
    }

    // 특정 senderId로 DMThread 목록을 페이징 처리하여 조회
    public Page<DMThreadDTO> getThreadsBySenderId(Long senderId, Pageable pageable) {
        return dmThreadRepository.getThreadsBySenderId(senderId, pageable);
    }
}
