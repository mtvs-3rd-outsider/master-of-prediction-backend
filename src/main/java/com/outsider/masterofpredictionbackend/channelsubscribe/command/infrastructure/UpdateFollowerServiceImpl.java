package com.outsider.masterofpredictionbackend.channelsubscribe.command.infrastructure;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.service.UpdateFollowerService;
import jakarta.transaction.Transactional;
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
    public void updateFollowerChannel(Long channelId, boolean isUserChannel, boolean isPlus) {
        if(isUserChannel) {
            updateFollowerCountMyChannel(channelId, isPlus);
        }else
        {
            updateFollowerCountCategoryChannel(channelId, isPlus);
        }
    }

    private void updateFollowerCountMyChannel(Long channelId, boolean isPlus) {
        String url = externalSystemBaseUrl + "/my-channels/" + channelId + "/followers";
        // 외부 시스템에 followerChange 값을 POST 요청으로 보낸다.
        restTemplate.postForEntity(url, isPlus, Void.class);
    }
    private void updateFollowerCountCategoryChannel(Long channelId, boolean isPlus) {
        String url = externalSystemBaseUrl + "/category-channels/" + channelId + "/followers";
        // 외부 시스템에 followerChange 값을 POST 요청으로 보낸다.
        restTemplate.postForEntity(url, isPlus, Void.class);
    }
}
