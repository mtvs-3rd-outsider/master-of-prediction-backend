package com.outsider.masterofpredictionbackend.user.command.domain.repository;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCommandRepository extends JpaRepository<User, Long> {

    // 이메일을 기반으로 사용자 검색
    Optional<User> findByEmail(String email);

    // 사용자 이름(userName)을 기반으로 사용자 검색 (중복 확인을 위해 필요)
    Optional<User> findByUserName(String userName);

    // 사용자 탈퇴 상태를 업데이트하는 커스텀 메서드
    @Modifying
    @Query("UPDATE User u SET u.isWithdrawal = true WHERE u = :user")
    void updateWithdrawalStatusByUser(@Param("user") User user);
}
