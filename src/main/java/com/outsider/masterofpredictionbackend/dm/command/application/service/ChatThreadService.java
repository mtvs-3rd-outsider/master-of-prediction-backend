package com.outsider.masterofpredictionbackend.dm.command.application.service;


import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadResponse;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Participant;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.ChatThreadRepository;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.ParticipantRepository;
import com.outsider.masterofpredictionbackend.dm.command.domain.service.NotificationClient;
import com.outsider.masterofpredictionbackend.dm.query.ChatThreadDTO;
import com.outsider.masterofpredictionbackend.dm.query.ChatThreadMapper;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class ChatThreadService {


    private final NotificationClient notificationClient;
    private final ChatThreadRepository dmThreadRepository;
    private final UserCommandRepository userCommandRepository;
    private final ParticipantRepository participantRepository;
    private final ChatThreadMapper chatThreadMapper; // MapStruct Mapper

    @Transactional
    public CreateChatThreadResponse createOrGetThread(CreateChatThreadDTO dto, Long currentUserId) {
        ChatThread newThread;

        // 1:1 채팅이면 기존 채팅방이 있는지 확인
        if (!dto.getIsGroupThread() && dto.getParticipantIds().size() == 2) {
            Long otherUserId = dto.getParticipantIds().stream()
                    .filter(id -> !id.equals(currentUserId))
                    .findFirst()
                    .orElse(currentUserId);

            Optional<ChatThread> existingThread = dmThreadRepository.findOneToOneThread(currentUserId, otherUserId);
            if (existingThread.isPresent()) {
                ChatThread existing = existingThread.get();

                // 참여자 상태 업데이트 로직
                existing.getParticipants().forEach(participant -> {
                    if (participant.getUser().getId().equals(currentUserId) || participant.getUser().getId().equals(otherUserId)) {
                        if (!participant.getActive()) {
                            participant.setActive(true); // 비활성화된 경우 활성화
                            participantRepository.save(participant); // 상태 업데이트
                        }
                    }
                });

                return chatThreadMapper.toCreateChatThreadResponse(existing);
            }
        }

        // 기존 채팅방이 없으면 새로 생성 (그룹 채팅이거나 새로운 1:1 채팅)
        newThread = new ChatThread();
        newThread.setIsGroupThread(dto.getIsGroupThread());
        newThread.setCreatedAt(LocalDateTime.now());
        ChatThread savedThread = dmThreadRepository.save(newThread);

        // 참여자 추가 로직
        Set<Long> uniqueParticipantIds = new HashSet<>(dto.getParticipantIds());
        uniqueParticipantIds.add(currentUserId); // 항상 currentUserId 포함

        for (Long participantId : uniqueParticipantIds) {
            User user = userCommandRepository.findById(participantId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            participantRepository.save(new Participant(savedThread, user));
        }

        return chatThreadMapper.toCreateChatThreadResponse(savedThread);
    }


    // 특정 채팅방 조회
    public Optional<ChatThread> getThread(Long chatRoomId) {
        return dmThreadRepository.findById(chatRoomId);
    }

    // 특정 사용자가 참여한 채팅방 목록을 페이징 처리하여 DTO로 조회
    @Transactional(readOnly = true)
    public Page<ChatThreadDTO> getThreadsByUserId(Long userId, Pageable pageable) {
        Page<ChatThread> threads = dmThreadRepository.getThreadsByUserId(userId, pageable);

        // ChatThread를 ChatThreadDTO로 매핑
        return threads.map(chatThreadMapper::toChatThreadDTO);
    }
    @Transactional
    public void deleteThread(Long chatRoomId) {
        // 1. 채팅방 존재 여부 확인
        ChatThread chatThread = dmThreadRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        // 2. 채팅방의 모든 참여자 삭제
        participantRepository.deleteAllByThread(chatThread);

        // 3. 채팅방 삭제
        dmThreadRepository.delete(chatThread);
    }




        @Transactional
        public void deactivateParticipant(Long roomId, Long userId) {
            participantRepository.deactivateParticipant(roomId, userId);
        }

}
