package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.service.UserService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import com.outsider.masterofpredictionbackend.user.query.application.dto.UserInfoResponseDTO;
import com.outsider.masterofpredictionbackend.user.query.application.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserInfoService userInfoService;
    private final UserRepository userRepository;

    public UserServiceImpl(UserInfoService userInfoService, UserRepository userRepository) {
        this.userInfoService = userInfoService;
        this.userRepository = userRepository;
    }

    @Override
    public Boolean isExistUser(Long userId) {
        UserInfoResponseDTO userInfo =  userInfoService.getUserInfoById(userId);
        return userInfo != null;
    }

    @Override
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail) {
            return ((CustomUserDetail) authentication.getPrincipal()).getId();
        }
        return null;
    }

    @Override
    public BigDecimal pointUpdate(Long userId, BigDecimal point) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        user.setPoints(user.getPoints().add(point));
        return user.getPoints();
    }

    @Override
    public List<User> findUsersByIds(List<Long> userIds) {
        return userRepository.findAllById(userIds);
    }
}
