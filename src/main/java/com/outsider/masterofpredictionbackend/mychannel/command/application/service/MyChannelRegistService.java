package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Bio;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.DisplayName;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.User;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Website;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 서비스 클래스는 새로운 채널을 등록하는 기능을 제공합니다.
 */
@Service
public class MyChannelRegistService {

    private final MyChannelCommandRepository myChannelRepository;

    /**
     * MyChannelRegistService의 생성자입니다.
     *
     * @param myChannelRepository 채널 정보를 관리하는 리포지토리
     */
    @Autowired
    public MyChannelRegistService(MyChannelCommandRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }

    /**
     * 새로운 채널을 등록합니다.
     *
     * 주어진 정보에 따라 새로운 채널을 생성하고, 데이터베이스에 저장합니다.
     *
     * @param myChannelRegistRequestDTO 등록할 채널의 정보가 담긴 DTO
     * @return 등록된 채널의 ID
     */
    @Transactional
    public Long registMyChannel(MyChannelRegistRequestDTO myChannelRegistRequestDTO) {

        MyChannel myChannel = new MyChannel(
                new DisplayName(myChannelRegistRequestDTO.getDisplayName()),
                new Bio(myChannelRegistRequestDTO.getBio()),
                new Website(myChannelRegistRequestDTO.getWebsite()),
                new User(myChannelRegistRequestDTO.getUser())
        );

        MyChannel savedChannel = myChannelRepository.save(myChannel);

        return savedChannel.getId(); // 등록된 채널의 ID 반환
    }
}
