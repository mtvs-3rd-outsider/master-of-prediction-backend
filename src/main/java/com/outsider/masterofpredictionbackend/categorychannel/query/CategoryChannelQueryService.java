package com.outsider.masterofpredictionbackend.categorychannel.query;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryChannelQueryService {

    private final CategoryChannelRepository categoryChannelRepository;

    @Autowired
    public CategoryChannelQueryService(CategoryChannelRepository categoryChannelRepository) {
        this.categoryChannelRepository = categoryChannelRepository;
    }

    // 페이지네이션을 적용하여 모든 카테고리 채널을 DTO로 변환하여 조회
    public Page<CategoryChannelDTO> getAllApprovedCategoryChannels(Pageable pageable) {
        Page<CategoryChannel> channelPage = categoryChannelRepository.findByCategoryChannelStatus(CategoryChannelStatus.APPROVED, pageable);
        return channelPage.map(this::convertToDTO);
    }
    // ID로 특정 카테고리 채널 조회 후 DTO로 변환
    public Optional<CategoryChannelDTO> getCategoryChannelById(Long channelId) {
        return categoryChannelRepository.findById(channelId).map(this::convertToDTO);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private CategoryChannelDTO convertToDTO(CategoryChannel categoryChannel) {
        return new CategoryChannelDTO(
                categoryChannel.getId(),
                categoryChannel.getDisplayName(),
                categoryChannel.getDescription(),
                categoryChannel.getCommunityRule().toJson(), // Use toJson() to return valid JSON
                categoryChannel.getImageUrl(),
                categoryChannel.getBannerImg(),
                categoryChannel.getCategoryChannelUserCounts().getJoinCount()
        );
    }

    // 카테고리 채널의 소유자인지 확인하는 메서드
    public boolean isCategoryChannelOwner(Long channelId, Long userId) {
        Optional<CategoryChannel> channelOpt = categoryChannelRepository.findById(channelId);
        if (channelOpt.isPresent()) {
            CategoryChannel channel = channelOpt.get();
            return channel.getOwnerUserId() == userId; // 소유자 확인
        }
        return false; // 채널이 존재하지 않으면 false 반환
    }
}
