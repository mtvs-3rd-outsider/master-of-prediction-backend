package com.outsider.masterofpredictionbackend.ranking.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user-rankings")
public class UserRankingController {

    private final UserRankingQueryService userRankingQueryService;

    public UserRankingController(UserRankingQueryService userRankingQueryService) {
        this.userRankingQueryService = userRankingQueryService;
    }

    // 사용자 ID로 순위 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserRankingDTO> getRankingByUserId(@PathVariable Long userId) {
        UserRankingDTO userRankingDTO = userRankingQueryService.getUserRankingByUserId(userId);
        return ResponseEntity.ok(userRankingDTO); // 200 OK 반환
    }


    @GetMapping
    public ResponseEntity<Page<UserRankingDTO>> getRankings(Pageable pageable) {
        return ResponseEntity.ok(userRankingQueryService.getUserRankingsWithUserName(pageable));
    }
}
