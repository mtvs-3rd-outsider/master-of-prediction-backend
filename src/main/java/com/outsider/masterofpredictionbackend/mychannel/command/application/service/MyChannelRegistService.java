package com.outsider.masterofpredictionbackend.mychannel.command.application.service;


import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.*;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyChannelRegistService {

    private MyChannelRepository myChannelRepository;

    @Autowired
    public MyChannelRegistService(MyChannelRepository bookRepository) {
        this.myChannelRepository = bookRepository;
    }

    @Transactional
    public void registMyChannel(MyChannelRegistRequestDTO myChannelRegistRequestDTO) {

        MyChannel myChannel = new MyChannel(
                new DisplayName( myChannelRegistRequestDTO.getDisplayName()),
                new Bio(myChannelRegistRequestDTO.getBio()),
                new Location( myChannelRegistRequestDTO.getLocation()),
                myChannelRegistRequestDTO.getJoinDate(),
                new Website(
                        myChannelRegistRequestDTO.getWebsite()
                ),
                new UserCounts(
                        myChannelRegistRequestDTO.getFollowersCount(),
                        myChannelRegistRequestDTO.getFollowingCount()
                ),
                new User(
                        myChannelRegistRequestDTO.getUser()
                )
        );
        myChannelRepository.save(myChannel);
    }
}
