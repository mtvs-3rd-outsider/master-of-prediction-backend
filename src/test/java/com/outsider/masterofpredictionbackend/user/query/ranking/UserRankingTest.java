package com.outsider.masterofpredictionbackend.user.query.ranking;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Tier;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.ranking.command.domain.repository.UserRankingRepository;
import com.outsider.masterofpredictionbackend.ranking.command.application.service.RankingService;
import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.UserRanking;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserRankingTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRankingRepository userRankingRepository;

    @Autowired
    private UserCommandRepository userCommandRepository;

    @Autowired
    private RankingService rankingService;

    @Test
    void testRankingUpdates() {
        // Test Case 1: New User Ranking Update
        Long userId1 = 1L;
        BigDecimal newPointsForUser1 = BigDecimal.valueOf(150);

        // 실제 데이터베이스에 새로운 유저 추가
        User user1 = new User();
        user1.setId(userId1);
        user1.setTier(new Tier("UNRANKED", 0));
        userCommandRepository.save(user1);

        // 실행: 새로운 유저의 랭킹 업데이트
        rankingService.updateRanking(userId1, newPointsForUser1);

        // 랭킹 데이터 검증
        Optional<UserRanking> savedRankingOpt1 = userRankingRepository.findById(userId1);
        assertTrue(savedRankingOpt1.isPresent());
        UserRanking savedRanking1 = savedRankingOpt1.get();
        assertEquals(newPointsForUser1, savedRanking1.getPoints());
        assertEquals(1, savedRanking1.getRank()); // 예상 랭크에 맞게 수정 필요
        assertNotNull(savedRanking1.getLastUpdated());

        // Test Case 2: Existing User Rank Up
        Long userId2 = 2L;
        BigDecimal newPointsForUser2 = BigDecimal.valueOf(300);

        // 기존 유저와 랭킹 추가
        User user2 = new User();
        user2.setId(userId2);
        user2.setTier(new Tier("NOVICE", 2));
        userCommandRepository.save(user2);

        // 실행
        rankingService.updateRanking(userId2, newPointsForUser2);

        // 검증
        Optional<UserRanking> updatedRankingOpt2 = userRankingRepository.findById(userId2);
        Optional<UserRanking> updatedRankingOpt1AfterUser2 = userRankingRepository.findById(userId1);  // userId1의 순위도 확인

        // 엔티티 다시 로드
        updatedRankingOpt1AfterUser2.ifPresent(userRanking -> entityManager.refresh(userRanking));

        assertTrue(updatedRankingOpt2.isPresent());
        assertTrue(updatedRankingOpt1AfterUser2.isPresent());

        UserRanking updatedRanking2 = updatedRankingOpt2.get();
        assertEquals(newPointsForUser2, updatedRanking2.getPoints());
        assertEquals(1, updatedRanking2.getRank()); // 예상 순위 수정

        UserRanking updatedRanking1AfterUser2 = updatedRankingOpt1AfterUser2.get();
        assertEquals(2, updatedRanking1AfterUser2.getRank()); // 예상 순위 수정

        // Test Case 3: Existing User Rank Down
        Long userId3 = 3L;
        BigDecimal newPointsForUser3 = BigDecimal.valueOf(100);

        // 기존 유저와 랭킹 추가
        User user3 = new User();
        user3.setId(userId3);
        user3.setTier(new Tier("PROPHET", 3));
        userCommandRepository.save(user3);

        // 실행
        rankingService.updateRanking(userId3, newPointsForUser3);

        // 검증
        Optional<UserRanking> updatedRankingOpt3 = userRankingRepository.findById(userId3);
        Optional<UserRanking> updatedRankingOpt1AfterUser3 = userRankingRepository.findById(userId1);  // userId1의 순위도 확인
        Optional<UserRanking> updatedRankingOpt2AfterUser3 = userRankingRepository.findById(userId2);  // userId2의 순위도 확인

        assertTrue(updatedRankingOpt3.isPresent());
        assertTrue(updatedRankingOpt1AfterUser3.isPresent());
        assertTrue(updatedRankingOpt2AfterUser3.isPresent());

        UserRanking updatedRanking3 = updatedRankingOpt3.get();
        assertEquals(newPointsForUser3, updatedRanking3.getPoints());
        assertEquals(3, updatedRanking3.getRank()); // 예상 순위 수정

        UserRanking updatedRanking1AfterUser3 = updatedRankingOpt1AfterUser3.get();
        assertEquals(2, updatedRanking1AfterUser3.getRank()); // 예상 순위 수정

        UserRanking updatedRanking2AfterUser3 = updatedRankingOpt2AfterUser3.get();
        assertEquals(1, updatedRanking2AfterUser3.getRank()); // 예상 순위 수정

        newPointsForUser3 = BigDecimal.valueOf(1300);

        // 실행
        rankingService.updateRanking(userId3, newPointsForUser3);

        // 검증
        updatedRankingOpt3 = userRankingRepository.findById(userId3);
        updatedRankingOpt1AfterUser3 = userRankingRepository.findById(userId1);  // userId1의 순위도 확인
        updatedRankingOpt2AfterUser3 = userRankingRepository.findById(userId2);  // userId2의 순위도 확인

        updatedRankingOpt2AfterUser3.ifPresent(userRanking -> entityManager.refresh(userRanking));
        updatedRankingOpt1AfterUser3.ifPresent(userRanking -> entityManager.refresh(userRanking));

        assertTrue(updatedRankingOpt3.isPresent());
        assertTrue(updatedRankingOpt1AfterUser3.isPresent());
        assertTrue(updatedRankingOpt2AfterUser3.isPresent());

        updatedRanking3 = updatedRankingOpt3.get();
        assertEquals(newPointsForUser3, updatedRanking3.getPoints());
        assertEquals(1, updatedRanking3.getRank()); // 예상 순위 수정

        updatedRanking1AfterUser3 = updatedRankingOpt1AfterUser3.get();
        assertEquals(3, updatedRanking1AfterUser3.getRank()); // 예상 순위 수정

        updatedRanking2AfterUser3 = updatedRankingOpt2AfterUser3.get();
        assertEquals(2, updatedRanking2AfterUser3.getRank()); // 예상 순위 수정
    }
}
