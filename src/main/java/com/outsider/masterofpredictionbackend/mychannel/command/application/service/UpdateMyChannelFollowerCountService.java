package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateChannelUserCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 서비스 클래스는 특정 채널의 팔로워 수를 업데이트하는 기능을 제공합니다.
 */
@Service
public class UpdateMyChannelFollowerCountService {

    private final MyChannelRepository myChannelRepository;

    /**
     * UpdateMyChannelFollowerCountService의 생성자입니다.
     *
     * @param myChannelRepository 채널 정보를 관리하는 리포지토리
     */
    @Autowired
    public UpdateMyChannelFollowerCountService(MyChannelRepository myChannelRepository) {
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
                .orElseThrow(IllegalArgumentException::new);

        if(dto.getIsPlus()) {
            myChannel.setFollowers(myChannel.getUserCounts().getFollowersCount() + 1);
        } else {
            myChannel.setFollowers(myChannel.getUserCounts().getFollowersCount() - 1);
        }
    }

}
