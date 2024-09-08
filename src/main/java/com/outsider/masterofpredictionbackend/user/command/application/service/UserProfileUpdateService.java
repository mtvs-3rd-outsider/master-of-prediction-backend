package com.outsider.masterofpredictionbackend.user.command.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UserChannelUpdateDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Gender;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Location;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.service.MyChannelInfoUpdateClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserProfileUpdateService {
    private final UserCommandRepository userRepository;
    private final MyChannelInfoUpdateClient myChannelInfoUpdateClient;
    @Autowired
    public UserProfileUpdateService(UserCommandRepository userRepository, MyChannelInfoUpdateClient myChannelInfoUpdateClient) {
        this.userRepository = userRepository;
        this.myChannelInfoUpdateClient = myChannelInfoUpdateClient;
    }
    @Transactional
    public User updateUser(UserUpdateRequestDTO userUpdateRequestDTO) {
        // 사용자 조회
        Optional<User> optionalUser = userRepository.findById(userUpdateRequestDTO.getUserId());
        User user = optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found"));
        // 각 필드 업데이트 (null이 아닌 값만 업데이트)

        if (userUpdateRequestDTO.getUserName() != null) {
            user.setUsername(userUpdateRequestDTO.getUserName());
        }
        if (userUpdateRequestDTO.getAge() != null) {
            user.setAge(userUpdateRequestDTO.getAge());
        }
        if (userUpdateRequestDTO.getGender() != null) {
            user.setGender(userUpdateRequestDTO.getGender());
        }
        if (userUpdateRequestDTO.getLocation() != null) {
            user.setLocation(userUpdateRequestDTO.getLocation());
        }
        if (userUpdateRequestDTO.getBirthday() != null) {
            user.setBirthday(userUpdateRequestDTO.getBirthday());
        }
        if (userUpdateRequestDTO.getAvatarUrl() != null) {
            user.setUserImg(userUpdateRequestDTO.getAvatarUrl());
        }
        if (userUpdateRequestDTO.getDisplayName() != null) {
            user.setDisplayName(userUpdateRequestDTO.getDisplayName());
        }
        // 사용자 저장
        return userRepository.save(user);
    }
    @Transactional
    public void updateUser(UserChannelUpdateDTO updateDTO,Long userId) throws JsonProcessingException {
        // MyChannelInfoViewModel을 UserUpdateRequestDTO로 변환
        UserUpdateRequestDTO userUpdateRequestDTO = convertToUserUpdateRequestDTO(updateDTO,userId);
        // MyChannelInfoViewModel을 MyChannelInfoUpdateRequestDTO로 변환
        MyChannelInfoUpdateRequestDTO myChannelInfoUpdateRequestDTO = convertToMyChannelInfoUpdateRequestDTO(updateDTO,userId);
        // 변환된 UserUpdateRequestDTO로 updateUser 호출
        updateUser(userUpdateRequestDTO);
        // 채널 정보 업데이트 클라이언트 호출
        myChannelInfoUpdateClient.publish(myChannelInfoUpdateRequestDTO);
    }

    private MyChannelInfoUpdateRequestDTO convertToMyChannelInfoUpdateRequestDTO(UserChannelUpdateDTO userChannelUpdateDTO, Long userId) {
        return MyChannelInfoUpdateRequestDTO.builder()
                .userId(userId)
                .bio(userChannelUpdateDTO.getBio())
                .website(userChannelUpdateDTO.getWebsite())
                .bannerImg(userChannelUpdateDTO.getBannerImg())
                .build();
    }
    private UserUpdateRequestDTO convertToUserUpdateRequestDTO(UserChannelUpdateDTO userChannelUpdateDTO, Long userId) {
        return UserUpdateRequestDTO.builder()
                .userId(userId)
                .displayName(userChannelUpdateDTO.getDisplayName())
                .userName(userChannelUpdateDTO.getUserName())
                .age(userChannelUpdateDTO.getAge()) // Age는 MyChannelInfoViewModel에 없으므로 기본값 설정
                .gender(userChannelUpdateDTO.getGender() != null ? Gender.fromString(userChannelUpdateDTO.getGender()) : null)
                .location(userChannelUpdateDTO.getLocation() != null ? Location.fromString(userChannelUpdateDTO.getLocation()) : null)
                .birthday(userChannelUpdateDTO.getBirthday() != null ? LocalDate.parse(userChannelUpdateDTO.getBirthday()) : null)
                .avatarUrl(userChannelUpdateDTO.getUserImg())
                .build();
    }
}
