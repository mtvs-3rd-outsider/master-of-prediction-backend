package com.outsider.masterofpredictionbackend.dm.command.application.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.DMThreadRepository;
import com.outsider.masterofpredictionbackend.dm.command.domain.service.NotificationClient;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DMThreadService {

    private final NotificationClient notificationClient;



    private final DMThreadRepository dmThreadRepository;

    // DMThread 생성 또는 가져오기
// DMThread 생성 또는 가져오기
    public DMThread getOrCreateThread(Long senderId, Long receiverId) {
        DMThreadKey threadKey = new DMThreadKey(senderId, receiverId);
        Optional<DMThread> existingThread = dmThreadRepository.findById(threadKey);

        // 상대방의 DMThread도 생성
        DMThreadKey reverseThreadKey = new DMThreadKey(receiverId, senderId);
        Optional<DMThread> reverseExistingThread = dmThreadRepository.findById(reverseThreadKey);

        DMThread thread = existingThread.orElseGet(() -> {
            DMThread newThread = new DMThread(threadKey);
            return dmThreadRepository.save(newThread);
        });

        // 상대방용 쓰레드가 없을 경우 생성
        reverseExistingThread.orElseGet(() -> {
            DMThread newReverseThread = new DMThread(reverseThreadKey);
            return dmThreadRepository.save(newReverseThread);
        });

        return thread;
    }

    // 사용자가 쓰레드에 접속했을 때 상태를 업데이트
    @Transactional
    public void userJoinedThread(DMThreadKey dmThreadKey) {
        Optional<DMThread> optionalDMThread = dmThreadRepository.findById(dmThreadKey);
        if (optionalDMThread.isPresent()) {
            DMThread dmThread = optionalDMThread.get();
            dmThread.setIsUserOnline(true);
            dmThreadRepository.save(dmThread);
        }
    }
    // 사용자가 최신 메시지를 읽었을 때 상태를 업데이트
    @Transactional
    public void markMessageAsRead(DMThreadKey dmThreadKey) {
        Optional<DMThread> optionalDMThread = dmThreadRepository.findById(dmThreadKey);
        if (optionalDMThread.isPresent()) {
            DMThread dmThread = optionalDMThread.get();
            dmThread.setLastMessageRead(true);
            dmThreadRepository.save(dmThread);
        }
    }

    // 사용자가 현재 해당 쓰레드에 접속 중인지 여부를 반환
    public boolean isUserOnline(DMThreadKey dmThreadKey) {
        Optional<DMThread> optionalDMThread = dmThreadRepository.findById(dmThreadKey);
        return optionalDMThread.map(DMThread::getIsUserOnline).orElse(false);
    }

    // 사용자가 최신 메시지를 읽었는지 여부를 반환
    public boolean hasUserReadLatestMessage(DMThreadKey dmThreadKey) {
        Optional<DMThread> optionalDMThread = dmThreadRepository.findById(dmThreadKey);
        return optionalDMThread.map(DMThread::getLastMessageRead).orElse(false);
    }
    // 사용자가 쓰레드에서 나갔을 때 상태를 업데이트
    @Transactional
    public void userLeftThread(DMThreadKey dmThreadKey) {
        Optional<DMThread> optionalDMThread = dmThreadRepository.findById(dmThreadKey);
        if (optionalDMThread.isPresent()) {
            DMThread dmThread = optionalDMThread.get();
            dmThread.setIsUserOnline(false);
            dmThreadRepository.save(dmThread);
        }
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

    public void updateLastMessage(DMThreadKey dmThreadKey, String lastMessage, UpdateMessageDTO updateMessageDTO) throws JsonProcessingException {
        // 정상 방향 업데이트 (sender -> receiver)
        Optional<DMThread> optionalDMThread = dmThreadRepository.findById(dmThreadKey);
        if (optionalDMThread.isPresent()) {
            DMThread dmThread = optionalDMThread.get();
            dmThread.setLastMessage(lastMessage);
            dmThread.setLastMessageTime(LocalDateTime.now());
            dmThreadRepository.save(dmThread);
        }

        // 역방향 쓰레드도 업데이트 (receiver -> sender)
        DMThreadKey reverseKey = new DMThreadKey(dmThreadKey.getReceiverId(), dmThreadKey.getSenderId());
        Optional<DMThread> optionalReverseThread = dmThreadRepository.findById(reverseKey);
        if (optionalReverseThread.isPresent()) {
            DMThread reverseThread = optionalReverseThread.get();
            reverseThread.setLastMessage(lastMessage);
            reverseThread.setLastMessageRead(false);
            reverseThread.setLastMessageTime(LocalDateTime.now());
            dmThreadRepository.save(reverseThread);

            // 상대방이 온라인이 아닌 경우 알림 전송
            if (!reverseThread.getIsUserOnline()) {
                notificationClient.sendNotification(updateMessageDTO);
            }
        }
    }
}
