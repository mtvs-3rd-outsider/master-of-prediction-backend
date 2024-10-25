package com.outsider.masterofpredictionbackend.dm.command.application.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.CreateChatThreadDTO;
import com.outsider.masterofpredictionbackend.dm.command.application.dto.UpdateMessageDTO;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Participant;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.ChatThreadRepository;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.DMThreadRepository;
import com.outsider.masterofpredictionbackend.dm.command.domain.repository.ParticipantRepository;
import com.outsider.masterofpredictionbackend.dm.command.domain.service.NotificationClient;
import com.outsider.masterofpredictionbackend.dm.query.ChatThreadDTO;
import com.outsider.masterofpredictionbackend.dm.query.ChatThreadMapper;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
@RequiredArgsConstructor
public class ChatThreadService {

    // 클라이언트에 이벤트를 전송하기 위한 SSE 구독자 리스트
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final NotificationClient notificationClient;
    private final ChatThreadRepository dmThreadRepository;
    private final ParticipantRepository participantRepository;
    private final ChatThreadMapper chatThreadMapper; // MapStruct Mapper

    // 특정 채팅방에 SSE 구독을 설정하고, 실시간 메시지를 전송하는 로직
    public void subscribeToChatRoom(Long chatRoomId, Long userId, SseEmitter emitter) {
        emitters.add(emitter);

        // 클라이언트 연결 종료 시 구독 해제 처리
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        // 구독 완료 시 즉시 초기 메시지를 전송할 수도 있음
        try {
            emitter.send(SseEmitter.event().name("INIT").data("Connected to chat room: " + chatRoomId));
        } catch (Exception e) {
            emitters.remove(emitter);
        }
    }

    // 새로운 메시지가 도착했을 때 SSE로 메시지를 전송하는 로직
    public void sendNewMessageToSubscribers(Long chatRoomId, String message) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("NEW_MESSAGE").data(message));
            } catch (Exception e) {
                emitters.remove(emitter); // 오류가 발생하면 해당 emitter 제거
            }
        }
    }


    // 채팅방 생성 또는 가져오기
    @Transactional
    public ChatThreadDTO createOrGetThread(CreateChatThreadDTO dto, Long currentUserId) {
        // 그룹 채팅이면 바로 새 채팅방 생성
        if (dto.getIsGroupThread()) {
            ChatThread newThread = new ChatThread();
            newThread.setIsGroupThread(true);
            newThread.setCreatedAt(LocalDateTime.now());
            return chatThreadMapper.toChatThreadDTO(dmThreadRepository.save(newThread));
        }

        // 1:1 채팅이면 기존 채팅방이 있는지 확인
        Long otherUserId = dto.getOtherUserId();
        Optional<ChatThread> existingThread = dmThreadRepository.findOneToOneThread(currentUserId, otherUserId);

        if (existingThread.isPresent()) {
            return chatThreadMapper.toChatThreadDTO(existingThread.get());
        }

        // 기존 채팅방이 없으면 새로 생성
        ChatThread newThread = new ChatThread();
        newThread.setIsGroupThread(false);
        newThread.setCreatedAt(LocalDateTime.now());
        ChatThread savedThread = dmThreadRepository.save(newThread);

        // 참여자 추가 로직 (생략)
        // participantRepository.save(new Participant(savedThread, currentUserId));
        // participantRepository.save(new Participant(savedThread, otherUserId));

        return chatThreadMapper.toChatThreadDTO(savedThread);
    }
    // 사용자가 쓰레드에 참여했을 때 상태 업데이트
    @Transactional
    public void userJoinedThread(Long chatRoomId, Long userId) {
        ChatThread dmThread = dmThreadRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Optional<Participant> participantOpt = participantRepository.findByThreadAndUserId(dmThread, userId);

        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            participant.setIsOnline(true);
            participantRepository.save(participant);
        } else {
            Participant newParticipant = new Participant(dmThread, userId);
            newParticipant.setIsOnline(true);
            participantRepository.save(newParticipant);
        }
    }

    // 사용자가 최신 메시지를 읽었을 때 상태 업데이트
    @Transactional
    public void markMessageAsRead(Long chatRoomId, Long userId) {
        ChatThread dmThread = dmThreadRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Optional<Participant> participantOpt = participantRepository.findByThreadAndUserId(dmThread, userId);

        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            participant.setLastReadTime(LocalDateTime.now());
            participant.setUnreadMessageCount(0);
            participantRepository.save(participant);
        }
    }

    // 사용자가 현재 해당 쓰레드에 접속 중인지 여부를 반환
    public boolean isUserOnline(Long chatRoomId, Long userId) {
        ChatThread dmThread = dmThreadRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        return participantRepository.findByThreadAndUserId(dmThread, userId)
                .map(Participant::getIsOnline)
                .orElse(false);
    }

    // 사용자가 쓰레드에서 나갔을 때 상태를 업데이트
    @Transactional
    public void userLeftThread(Long chatRoomId, Long userId) {
        ChatThread dmThread = dmThreadRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Optional<Participant> participantOpt = participantRepository.findByThreadAndUserId(dmThread, userId);

        if (participantOpt.isPresent()) {
            Participant participant = participantOpt.get();
            participant.setIsOnline(false);
            participantRepository.save(participant);
        }
    }

    // 채팅방의 마지막 메시지와 시간 업데이트
    @Transactional
    public void updateLastMessage(Long chatRoomId, String lastMessage, UpdateMessageDTO updateMessageDTO) throws JsonProcessingException {
        ChatThread dmThread = dmThreadRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // 채팅방의 마지막 메시지와 시간 업데이트
        dmThread.setLastMessage(lastMessage);
        dmThread.setLastMessageTime(LocalDateTime.now());
        dmThreadRepository.save(dmThread);

        // 채팅방의 모든 사용자 중 온라인이 아닌 사용자에게 알림 전송 및 읽지 않음으로 설정
        participantRepository.findByThread(dmThread).stream()
                .filter(participant -> !participant.getIsOnline())
                .forEach(participant -> {
                    // "읽지 않음" 상태로 설정
                    participant.setUnreadMessageCount(participant.getUnreadMessageCount() + 1);
                    participantRepository.save(participant); // 상태 업데이트

                    // 알림 전송
                    try {
                        notificationClient.sendNotification(updateMessageDTO);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    // 특정 채팅방 조회
    public Optional<ChatThread> getThread(Long chatRoomId) {
        return dmThreadRepository.findById(chatRoomId);
    }

    // 특정 사용자가 참여한 채팅방 목록을 페이징 처리하여 조회
    public Page<ChatThread> getThreadsByUserId(Long userId, Pageable pageable) {
        return dmThreadRepository.getThreadsByUserId(userId, pageable);
    }

}
