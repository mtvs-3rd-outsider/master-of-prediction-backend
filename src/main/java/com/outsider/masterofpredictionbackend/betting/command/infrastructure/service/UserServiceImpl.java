package com.outsider.masterofpredictionbackend.betting.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.betting.command.domain.service.UserService;
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
}
