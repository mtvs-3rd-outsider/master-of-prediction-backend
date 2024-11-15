package com.outsider.masterofpredictionbackend.ranking.query;

import com.outsider.masterofpredictionbackend.exception.NotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ScoreRankingQueryService {

    private final ScoreRankingQueryRepository scoreRankingQueryRepository;
    private final ScoreRankingMapper scoreRankingMapper;

    public ScoreRankingQueryService(ScoreRankingQueryRepository scoreRankingQueryRepository,
                                    ScoreRankingMapper scoreRankingMapper) {
        this.scoreRankingQueryRepository = scoreRankingQueryRepository;
        this.scoreRankingMapper = scoreRankingMapper;
    }

    // 사용자 ID로 점수 기반 순위 조회
    public ScoreRankingDTO getScoreRankingByUserId(Long userId) {
        return scoreRankingQueryRepository.findByUserId(userId)
                .map(scoreRankingMapper::toScoreRankingDTO)
                .orElseGet(() -> {
                    // 기본값으로 설정하거나 사용자에게 없는 랭킹 정보를 알려줄 수 있음
                    ScoreRankingDTO defaultRanking = new ScoreRankingDTO();
                    defaultRanking.setUserId(userId);
                    defaultRanking.setScore(0);
                    defaultRanking.setRank(-1); // 랭킹 없음 또는 기본 값
                    defaultRanking.setDisplayName("Unknown");
                    defaultRanking.setUserName("Unknown");
                    return defaultRanking;
                });
    }


    // 전체 점수 기반 순위 목록 조회 (페이징)
    public Page<ScoreRankingDTO> getScoreRankings(Pageable pageable) {
        return scoreRankingQueryRepository.findAll(pageable)
                .map(scoreRankingMapper::toScoreRankingDTO);
    }
}
