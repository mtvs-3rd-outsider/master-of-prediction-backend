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
    public void changeCategoryChannelStatus(Long channelId, CategoryChannelStatus newStatus) {
        CategoryChannel categoryChannel = categoryChannelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid channel ID"));

        // 현재 상태와 동일한 상태로의 변경을 방지
        if (categoryChannel.getCategoryChannelStatus() == newStatus) {
            throw new IllegalStateException("The channel is already " + newStatus);
        }

        // 이미 확정된 상태인지 확인
        if (categoryChannel.getCategoryChannelStatus() == CategoryChannelStatus.APPROVED
                || categoryChannel.getCategoryChannelStatus() == CategoryChannelStatus.REJECTED) {
            throw new IllegalStateException("The channel has already been finalized with status: "
                    + categoryChannel.getCategoryChannelStatus());
        }

        categoryChannel.setCategoryChannelStatus(newStatus);
        categoryChannelRepository.save(categoryChannel);
    }

}
