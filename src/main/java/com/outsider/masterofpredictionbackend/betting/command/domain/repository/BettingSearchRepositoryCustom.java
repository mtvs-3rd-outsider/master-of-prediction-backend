package com.outsider.masterofpredictionbackend.betting.command.domain.repository;
import com.outsider.masterofpredictionbackend.betting.command.domain.aggregate.BettingSearchModel;
import com.outsider.masterofpredictionbackend.user.query.usersearch.UserSearchModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BettingSearchRepositoryCustom {
    Page<BettingSearchModel> findByTitleOrContaining(String displayName, Pageable pageable);
}
