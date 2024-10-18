package com.outsider.masterofpredictionbackend.betting.command.domain.service;

import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;

import java.math.BigDecimal;
import java.util.List;

public interface UserService {

    Boolean isExistUser(Long userId);

    Long getUserId();

    /*
     * NOTE: 사용자 포인트가 제대로 업데이트 되었는지 확인용으로 BigDecimal로 반환하도록 수정함
     */
    BigDecimal pointUpdate(Long userId, BigDecimal point);

    /*
     * id 를 기반으로 사용자들 조회
     */
    List<User> findUsersByIds(List<Long> userIds);

}
