package com.outsider.masterofpredictionbackend.mychannel.query;


import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class MyChannelQueryService {

    private final MyChannelCommandRepository myChannelRepository;

    @Autowired
    public MyChannelQueryService(MyChannelCommandRepository myChannelRepository) {
        this.myChannelRepository = myChannelRepository;
    }

    // 채널 ID로 팔로워 수를 조회하는 메서드
    public int getFollowerCount(Long channelId) {
        Optional<MyChannel> channelOptional = myChannelRepository.findById(channelId);
        return channelOptional.map(channel -> channel.getUserCounts().getFollowersCount()).orElse(0);
    }

    // 채널 ID로 팔로잉 수를 조회하는 메서드
    public int getFollowingCount(Long channelId) {
        Optional<MyChannel> channelOptional = myChannelRepository.findById(channelId);
        return channelOptional.map(channel -> channel.getUserCounts().getFollowingCount()).orElse(0);
    }
}
