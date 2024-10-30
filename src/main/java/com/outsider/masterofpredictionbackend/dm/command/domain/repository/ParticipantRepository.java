package com.outsider.masterofpredictionbackend.dm.command.domain.repository;


import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // 특정 채팅방과 사용자 ID를 기반으로 참여자를 조회하는 메서드
    Optional<Participant> findByThreadAndUserId(ChatThread thread, Long userId);

    // 채팅방과 사용자 ID로 해당 참여자가 존재하는지 확인하는 메서드

    List<Participant> findByThread(ChatThread thread);
    void deleteAllByThread(ChatThread thread);

    @Query("SELECT p FROM Participant p WHERE p.thread.chatRoomId = :roomId AND p.user.id = :userId")
    Participant findByRoomIdAndUserId(@Param("roomId") Long roomId, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Participant p SET p.isActive = false WHERE p.thread.chatRoomId = :roomId AND p.user.id = :userId")
    void deactivateParticipant(@Param("roomId") Long roomId, @Param("userId") Long userId);
}