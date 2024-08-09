package com.outsider.masterofpredictionbackend.channelsubscribe.command.infrastructure;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UpdateFollowingServiceImpl implements UpdateFollowingService {

    private final RestTemplate restTemplate;
    private final String externalSystemBaseUrl = "https://api.external-system.com"; // 외부 시스템의 기본 URL

    public UpdateFollowingServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    @Transactional
    public void updateFollowingChannel(Long channelId, boolean isUserChannel, boolean isPlus) {
        if(isUserChannel) {
            updateFollowingCountMyChannel(channelId, isPlus);
        }else
        {
            updateFollowingCountCategoryChannel(channelId, isPlus);
        }
    }

    private void updateFollowingCountMyChannel(Long channelId, boolean isPlus) {
        String url = externalSystemBaseUrl + "/my-channels/" + channelId + "/following";
        // 외부 시스템에 followerChange 값을 POST 요청으로 보낸다.
        restTemplate.postForEntity(url, isPlus, Void.class);
    }
    private void updateFollowingCountCategoryChannel(Long channelId, boolean isPlus) {
        String url = externalSystemBaseUrl + "/category-channels/" + channelId + "/following";
        // 외부 시스템에 followerChange 값을 POST 요청으로 보낸다.
        restTemplate.postForEntity(url, isPlus, Void.class);
    }
}
