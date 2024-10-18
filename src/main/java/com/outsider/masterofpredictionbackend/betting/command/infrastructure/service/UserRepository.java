package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
