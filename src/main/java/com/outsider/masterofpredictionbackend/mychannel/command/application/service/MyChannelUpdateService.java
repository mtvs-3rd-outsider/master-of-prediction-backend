package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Bio;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.DisplayName;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Website;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 서비스 클래스는 채널의 정보를 업데이트하는 기능을 제공합니다.
 */
@Service
public class MyChannelUpdateService {

    private final MyChannelRepository myChannelRepository;

    /**
     * MyChannelUpdateService의 생성자입니다.
     *
     * @param myChannelRepository 채널 정보를 관리하는 리포지토리
     */
    @Autowired
    public MyChannelUpdateService(MyChannelRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }

    /**
     * 주어진 채널 ID에 따라 채널의 정보를 업데이트합니다.
     *
     * 업데이트할 수 있는 정보는 DisplayName, Bio, Website입니다.
     * DTO에 null 값이 아닌 데이터만 업데이트됩니다.
     *
     * @param dto 채널 ID와 업데이트할 정보가 포함된 DTO
     * @throws RuntimeException 채널이 존재하지 않는 경우 발생
     */
    @Transactional
    public void updateMyChannel(MyChannelUpdateRequestDTO dto) {
        MyChannel myChannel = myChannelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        if (dto.getDisplayName() != null) {
            myChannel.setDisplayName(new DisplayName(dto.getDisplayName()));
        }

        if (dto.getBio() != null) {
            myChannel.setBio(new Bio(dto.getBio()));
        }

        if (dto.getWebsite() != null) {
            myChannel.setWebsite(new Website(dto.getWebsite()));
        }

        myChannelRepository.save(myChannel);
    }

}
