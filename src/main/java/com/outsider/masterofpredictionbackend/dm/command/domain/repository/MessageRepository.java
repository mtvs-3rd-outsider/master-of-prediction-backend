package com.outsider.masterofpredictionbackend.dm.command.domain.repository;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    Page<Message> findByRoomIdOrderBySentDesc(String roomId, Pageable pageable);
}