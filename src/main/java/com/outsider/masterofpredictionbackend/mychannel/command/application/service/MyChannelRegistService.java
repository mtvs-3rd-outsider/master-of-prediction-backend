package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelInfoUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Bio;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.Website;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_CREATE_TOPIC;
import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.MY_CHANNEL_UPDATE_TOPIC;

/**
 * 서비스 클래스는 새로운 채널을 등록하는 기능을 제공합니다.
 */
@Service
@Slf4j
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
     * @param Long 등록할 채널의 userId
     * @return 등록된 채널의 ID
     */
    @Transactional
    public Long registMyChannel(Long userId) {

        MyChannel myChannel = new MyChannel(
                userId
        );

        MyChannel savedChannel = myChannelRepository.save(myChannel);

        return savedChannel.getId(); // 등록된 채널의 ID 반환
    }
    @KafkaListener(topics = MY_CHANNEL_CREATE_TOPIC)
    @Transactional
    public void consume(String message)  {
        registMyChannel(Long.parseLong(message));
        log.info("Updated channel from Kafka message: {}", message);
    }
}
