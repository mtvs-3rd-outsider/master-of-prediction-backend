package com.outsider.masterofpredictionbackend.ranking.query;

import com.outsider.masterofpredictionbackend.exception.NotExistException;
import com.outsider.masterofpredictionbackend.ranking.command.domain.repository.UserRankingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRankingQueryService {

    private final UserRankingRepository userRankingRepository;
    private final UserRankingMapper userRankingMapper;

    public UserRankingQueryService(UserRankingRepository userRankingRepository, UserRankingMapper userRankingMapper) {
        this.userRankingRepository = userRankingRepository;
        this.userRankingMapper = userRankingMapper;
    }

    // 사용자 ID로 순위 조회

//    @Transactional(readOnly = true)
//    public UserRankingDTO getUserRankingByUserId(Long userId) {
//        return userRankingRepository.findById(userId)
//                .map(userRankingMapper::toDTO)
//                .orElseThrow(() -> new RuntimeException("UserRanking not found for userId: " + userId));
//    }
public UserRankingDTO getUserRankingByUserId(Long userId) {
    return userRankingRepository.findUserRankingByUserId(userId)
            .orElseThrow(() -> new NotExistException("UserRanking not found for userId: " + userId));
}
    // 전체 사용자 순위 조회 (페이징 처리)
    @Transactional(readOnly = true)
    public Page<UserRankingDTO> getAllUserRankings(Pageable pageable) {
        return userRankingRepository.findAll(pageable)
                .map(userRankingMapper::toDTO);
    }

    public Page<UserRankingDTO> getUserRankingsWithUserName(Pageable pageable) {
        return userRankingRepository.findUserRankingsWithUserName(pageable);
    }
}
