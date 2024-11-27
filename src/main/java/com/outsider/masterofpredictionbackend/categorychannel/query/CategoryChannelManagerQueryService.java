package com.outsider.masterofpredictionbackend.categorychannel.query;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryChannelManagerQueryService {

    private final CategoryChannelManagerRepository categoryChannelManagerRepository;

    @Autowired
    public CategoryChannelManagerQueryService(CategoryChannelManagerRepository categoryChannelManagerRepository) {
        this.categoryChannelManagerRepository = categoryChannelManagerRepository;
    }

    @Transactional(readOnly = true)
    public CategoryChannelManager getManagerInfo(Long channelId, Long userId) {
        return categoryChannelManagerRepository.findByCategoryChannelIdAndUserId(channelId, userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "No manager found for channelId: " + channelId + " and userId: " + userId));
    }



    public List<CategoryChannelManager> getChannelManagers(Long channelId) {
        return categoryChannelManagerRepository.findByCategoryChannelId(channelId);
    }
}
