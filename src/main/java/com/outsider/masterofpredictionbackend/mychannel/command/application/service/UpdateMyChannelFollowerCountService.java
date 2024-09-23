package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateChannelUserCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 서비스 클래스는 특정 채널의 팔로워 수를 업데이트하는 기능을 제공합니다.
 */
@Service
@Slf4j
public class UpdateMyChannelFollowerCountService {

    private final MyChannelCommandRepository myChannelRepository;

    @Autowired
    public UpdateMyChannelFollowerCountService(MyChannelCommandRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }

    /**
     * 주어진 채널 ID에 따라 팔로워 수를 증가 또는 감소시킵니다.
     *
     * @param dto 채널 ID와 팔로워 수 증가 여부를 포함하는 DTO
     * @throws IllegalArgumentException 채널이 존재하지 않는 경우 발생
     */
    @Transactional
    public void updateFollowerMyChannel(UpdateChannelUserCountDTO dto) {
        MyChannel myChannel = myChannelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new IllegalArgumentException("Channel with ID " + dto.getChannelId() + " not found"));

        // 팔로워 수 증가 또는 감소
        if (dto.getIsPlus()) {
            log.info("Increasing followers for channel ID: {}", dto.getChannelId());
            myChannel.setFollowers(myChannel.getUserCounts().getFollowersCount() + 1);
        } else {
            log.info("Decreasing followers for channel ID: {}", dto.getChannelId());
            myChannel.setFollowers(myChannel.getUserCounts().getFollowersCount() - 1);
        }

        // 변경 사항 저장
        myChannelRepository.save(myChannel);
    }
}