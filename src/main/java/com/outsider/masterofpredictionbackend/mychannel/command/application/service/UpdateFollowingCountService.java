package com.outsider.masterofpredictionbackend.mychannel.command.application.service;


import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateFollowingCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateFollowingCountService {

    private final MyChannelRepository myChannelRepository;

    @Autowired
    public UpdateFollowingCountService(MyChannelRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }

    @Transactional
    public void updateFollowingsMyChannel(UpdateFollowingCountDTO dto) {
        MyChannel myChannel = myChannelRepository.findById(dto.getChannelId()).orElseThrow(IllegalArgumentException::new);
        if(dto.getIsPlus())
        {
            myChannel.setFollowings(myChannel.getUserCounts().getFollowingCount()+1);
        }
        else
        {
            myChannel.setFollowings(myChannel.getUserCounts().getFollowingCount()-1);
        }
    }

}
