package com.outsider.masterofpredictionbackend.user.command.domain.repository;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCommandRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
