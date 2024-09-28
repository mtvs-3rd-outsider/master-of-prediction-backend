package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;


import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryChannelApprovalService {

    private final CategoryChannelRepository categoryChannelRepository;

    @Autowired
    public CategoryChannelApprovalService(CategoryChannelRepository categoryChannelRepository) {
        this.categoryChannelRepository = categoryChannelRepository;
    }

    @Transactional
    public void approveCategoryChannel(Long channelId) {
        CategoryChannel categoryChannel = categoryChannelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel ID"));

        categoryChannel.setCategoryChannelStatus(CategoryChannelStatus.APPROVED);
        categoryChannelRepository.save(categoryChannel);
    }
}
