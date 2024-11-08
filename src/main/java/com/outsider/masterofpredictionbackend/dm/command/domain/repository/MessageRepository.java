package com.outsider.masterofpredictionbackend.dm.command.domain.repository;

import com.outsider.masterofpredictionbackend.dm.command.domain.aggregate.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m LEFT JOIN FETCH m.reactions r WHERE m.roomId = :roomId ORDER BY m.sent DESC")
    Page<Message> findByRoomIdOrderBySentDesc(@Param("roomId") String roomId, Pageable pageable);
}