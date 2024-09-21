package com.outsider.masterofpredictionbackend.bettingorder.command.infrastructure;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface BettingUserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User u set u.points = u.points + :point where u.id = :userId")
    void updatePoint(Long userId, BigDecimal point);
}
