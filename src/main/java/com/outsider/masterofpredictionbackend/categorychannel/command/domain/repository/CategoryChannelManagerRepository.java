package com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryChannelManagerRepository extends JpaRepository<CategoryChannelManager, Long> {

    // 특정 채널에 속한 매니저들을 조회
    List<CategoryChannelManager> findByCategoryChannelId(Long categoryChannelId);

    // 특정 채널에 속한 특정 역할의 매니저들을 조회
    List<CategoryChannelManager> findByCategoryChannelIdAndRole(Long categoryChannelId, String role);

    Optional<CategoryChannelManager> findByCategoryChannelIdAndUserId(Long categoryChannelId, Long userId);
    // 특정 사용자가 특정 채널에서 매니저인지 확인
    boolean existsByCategoryChannelIdAndUserId(Long categoryChannelId, Long userId);
}
