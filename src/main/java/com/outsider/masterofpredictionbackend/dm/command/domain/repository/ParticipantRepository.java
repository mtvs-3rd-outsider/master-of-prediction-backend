package com.outsider.masterofpredictionbackend.dm.command.domain.repository;


import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.ChatThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThread;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.DMThreadKey;
import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Participant;
import com.outsider.masterofpredictionbackend.dm.query.DMThreadRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    // 특정 채팅방과 사용자 ID를 기반으로 참여자를 조회하는 메서드
    Optional<Participant> findByThreadAndUserId(ChatThread thread, Long userId);

    // 채팅방과 사용자 ID로 해당 참여자가 존재하는지 확인하는 메서드
    boolean existsByThreadAndUserId(DMThread thread, Long userId);

    List<Participant> findByThread(ChatThread thread);
}