package com.outsider.masterofpredictionbackend.mychannel.command.application.service;


import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateFollowerCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateFollowerCountService {

    private final MyChannelRepository myChannelRepository;

    @Autowired
    public UpdateFollowerCountService(MyChannelRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }
    @Transactional
    public void updateFollowerMyChannel(UpdateFollowerCountDTO dto) {
        MyChannel myChannel = myChannelRepository.findById(dto.getChannelId()).orElseThrow(IllegalArgumentException::new);
        if(dto.getIsPlus())
        {
            myChannel.setFollowers(myChannel.getUserCounts().getFollowersCount()+1);
        }
        else
        {
            myChannel.setFollowers(myChannel.getUserCounts().getFollowersCount()-1);
        }
    }

}
