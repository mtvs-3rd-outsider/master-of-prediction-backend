package com.outsider.masterofpredictionbackend.mychannelsubscribe.command.infrastructure;

import com.outsider.masterofpredictionbackend.mychannelsubscribe.command.domain.service.UpdateFollowerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class UpdateFollowerServiceImpl implements UpdateFollowerService {

    private final RestTemplate restTemplate;
    private final String externalSystemBaseUrl = "https://api.external-system.com"; // 외부 시스템의 기본 URL

    public UpdateFollowerServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    @Transactional
    public void updateFollowerMyChannel(Long channelId, boolean isPlus) {
        updateFollowerCount(channelId, isPlus);
    }

    public void updateFollowerCount(Long channelId, boolean isPlus) {
        String url = externalSystemBaseUrl + "/channels/" + channelId + "/followers";
        // 외부 시스템에 followerChange 값을 POST 요청으로 보낸다.
        restTemplate.postForEntity(url, isPlus, Void.class);
    }

}
