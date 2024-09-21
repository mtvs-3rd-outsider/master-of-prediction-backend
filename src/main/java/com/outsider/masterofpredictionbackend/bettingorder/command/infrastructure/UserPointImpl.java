package com.outsider.masterofpredictionbackend.bettingorder.command.infrastructure;

import com.outsider.masterofpredictionbackend.bettingorder.command.domain.service.UserPoint;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class UserPointImpl implements UserPoint {

    private final BettingUserRepository userCommandRepository;

    public UserPointImpl(BettingUserRepository userCommandRepository) {
        this.userCommandRepository = userCommandRepository;
    }

    @Override
    public BigDecimal find(Long userId) {
        return userCommandRepository.findById(userId).orElseThrow().getPoints();
    }

    @Override
    public void pointUpdate(Long userId, BigDecimal point) {
        Optional<User> user = userCommandRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("user not found");
        }
        if (user.get().getPoints().add(point).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("point is not enough");
        }
        userCommandRepository.updatePoint(userId, point);
    }
}
