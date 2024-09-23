package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.service.UserService;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public Boolean isExistUser(Long userId) {
        if (userId == null || userId == 0){
            return false;
        }
        // TODO: 임의값
        return true;
    }

    @Override
    public Long getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetail) {
            return ((CustomUserDetail) authentication.getPrincipal()).getId();
        }
        return null;
    }


}
