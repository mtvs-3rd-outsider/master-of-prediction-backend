package com.outsider.masterofpredictionbackend.mychannel.command.application.service;



import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateProfileDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.embeded.*;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyChannelUpdateService {

    private final MyChannelRepository myChannelRepository;

    @Autowired
    public MyChannelUpdateService(MyChannelRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }

    @Transactional
    public void updateChannel(UpdateProfileDTO dto) {
        MyChannel myChannel = myChannelRepository.findById(dto.getChannelId())
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        if (dto.getDisplayName() != null) {
            myChannel.setDisplayName(new DisplayName(dto.getDisplayName()));
        }

        if (dto.getBio() != null) {
            myChannel.setBio(new Bio(dto.getBio()));
        }

        if (dto.getLocation() != null) {
            myChannel.setLocation(new Location(dto.getLocation()));
        }

        if (dto.getWebsite() != null) {
            myChannel.setWebsite(new Website(dto.getWebsite()));
        }

        myChannelRepository.save(myChannel);
    }

}
