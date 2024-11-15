package com.outsider.masterofpredictionbackend.ranking.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/score-rankings")
public class ScoreRankingController {

    private final ScoreRankingQueryService scoreRankingQueryService;

    public ScoreRankingController(ScoreRankingQueryService scoreRankingQueryService) {
        this.scoreRankingQueryService = scoreRankingQueryService;
    }

    // 사용자 ID로 점수 기반 순위 조회
    @GetMapping("/{userId}")
    public ResponseEntity<ScoreRankingDTO> getScoreRankingByUserId(@PathVariable Long userId) {
        ScoreRankingDTO scoreRankingDTO = scoreRankingQueryService.getScoreRankingByUserId(userId);
        return ResponseEntity.ok(scoreRankingDTO);
    }

    // 전체 점수 기반 순위 목록 조회 (페이징)
    @GetMapping
    public ResponseEntity<Page<ScoreRankingDTO>> getScoreRankings(Pageable pageable) {
        return ResponseEntity.ok(scoreRankingQueryService.getScoreRankings(pageable));
    }
}
