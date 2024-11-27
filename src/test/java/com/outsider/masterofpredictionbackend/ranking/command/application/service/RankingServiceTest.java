package com.outsider.masterofpredictionbackend.ranking.command.application.service;

import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.ScoreRanking;
import com.outsider.masterofpredictionbackend.ranking.command.domain.aggregate.UserRanking;
import com.outsider.masterofpredictionbackend.ranking.command.domain.repository.ScoreRankingRepository;
import com.outsider.masterofpredictionbackend.ranking.command.domain.repository.UserRankingRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private UserRankingRepository userRankingRepository;

    @Mock
    private UserCommandRepository userCommandRepository;

    @Mock
    private ScoreRankingRepository scoreRankingRepository;

    @InjectMocks
    private RankingService rankingService;

    private User testUser;
    private UserRanking testUserRanking;
    private ScoreRanking testScoreRanking;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        
        testUserRanking = new UserRanking(1L);
        testUserRanking.setPoints(BigDecimal.valueOf(100));
        testUserRanking.setRank(1);

        testScoreRanking = new ScoreRanking(1L);
        testScoreRanking.setScore(100);
        testScoreRanking.setRank(1);
    }

    @Test
    @DisplayName("포인트 업데이트 시 랭킹이 정상적으로 계산되어야 한다")
    void updateRanking_ShouldCalculateRankCorrectly() {
        // Given
        BigDecimal newPoints = BigDecimal.valueOf(150);
        when(userRankingRepository.findById(1L)).thenReturn(Optional.of(testUserRanking));
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRankingRepository.countByPointsGreaterThan(newPoints)).thenReturn(2L);
        when(userRankingRepository.findByPoints(newPoints))
            .thenReturn(Arrays.asList(testUserRanking));

        // When
        rankingService.updateRanking(1L, newPoints);

        // Then
        verify(userRankingRepository).save(any(UserRanking.class));
        verify(userCommandRepository).save(any(User.class));
        assertThat(testUserRanking.getPoints()).isEqualTo(newPoints);
        assertThat(testUserRanking.getRank()).isEqualTo(3); // 2명이 더 높은 점수를 가지고 있으므로 3등
    }

    @Test
    @DisplayName("승리 시 스코어가 3점 증가하고 랭킹이 올바르게 업데이트되어야 한다")
    void updateRankingByScore_WinShouldIncreaseScoreBy3() {
        // Given
        when(scoreRankingRepository.findById(1L)).thenReturn(Optional.of(testScoreRanking));
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scoreRankingRepository.countByScoreGreaterThan(103)).thenReturn(1L);
        when(scoreRankingRepository.count()).thenReturn(10L);
        when(scoreRankingRepository.findByScore(103))
            .thenReturn(Arrays.asList(testScoreRanking));

        // When
        rankingService.updateRankingByScore(1L, "win");

        // Then
        verify(scoreRankingRepository).save(any(ScoreRanking.class));
        verify(userCommandRepository).save(any(User.class));
        assertThat(testScoreRanking.getScore()).isEqualTo(103); // 100 + 3
        assertThat(testScoreRanking.getRank()).isEqualTo(2); // 1명이 더 높은 점수를 가지고 있으므로 2등
    }

    @Test
    @DisplayName("패배 시 스코어가 3점 감소하고 랭킹이 올바르게 업데이트되어야 한다")
    void updateRankingByScore_LoseShouldDecreaseScoreBy3() {
        // Given
        when(scoreRankingRepository.findById(1L)).thenReturn(Optional.of(testScoreRanking));
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(scoreRankingRepository.countByScoreGreaterThan(97)).thenReturn(3L);
        when(scoreRankingRepository.count()).thenReturn(10L);
        when(scoreRankingRepository.findByScore(97))
            .thenReturn(Arrays.asList(testScoreRanking));

        // When
        rankingService.updateRankingByScore(1L, "lose");

        // Then
        verify(scoreRankingRepository).save(any(ScoreRanking.class));
        verify(userCommandRepository).save(any(User.class));
        assertThat(testScoreRanking.getScore()).isEqualTo(97); // 100 - 3
        assertThat(testScoreRanking.getRank()).isEqualTo(4); // 3명이 더 높은 점수를 가지고 있으므로 4등
    }

    @Test
    @DisplayName("동점자가 있을 경우 같은 랭크를 가져야 한다")
    void updateRanking_SamePointsShouldHaveSameRank() {
        // Given
        BigDecimal samePoints = BigDecimal.valueOf(150);
        UserRanking anotherUserRanking = new UserRanking(2L);
        anotherUserRanking.setPoints(samePoints);
        
        when(userRankingRepository.findById(1L)).thenReturn(Optional.of(testUserRanking));
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRankingRepository.countByPointsGreaterThan(samePoints)).thenReturn(0L);
        when(userRankingRepository.findByPoints(samePoints))
            .thenReturn(Arrays.asList(testUserRanking, anotherUserRanking));

        // When
        rankingService.updateRanking(1L, samePoints);

        // Then
        verify(userRankingRepository, times(1)).save(any(UserRanking.class));
        assertThat(testUserRanking.getRank()).isEqualTo(1);
        assertThat(anotherUserRanking.getRank()).isEqualTo(1);
    }
}

