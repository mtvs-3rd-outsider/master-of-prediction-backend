package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelManagerAssignRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelManagerMapper;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelManagerRepository;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryChannelManagerService {

    private final CategoryChannelManagerRepository categoryChannelManagerRepository;
    private final CategoryChannelRepository categoryChannelRepository;
    private final CategoryChannelManagerMapper categoryChannelManagerMapper;

    @Transactional
    public CategoryChannelManager assignManager(CategoryChannelManagerAssignRequestDTO requestDTO) {
        // CategoryChannelId로 CategoryChannel 조회
        CategoryChannel categoryChannel = categoryChannelRepository.findById(requestDTO.getCategoryChannelId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid CategoryChannel ID: " + requestDTO.getCategoryChannelId()));

        // DTO -> Entity 매핑
        CategoryChannelManager manager = categoryChannelManagerMapper.toEntity(requestDTO);

        // 조회한 CategoryChannel을 매핑
        manager.setCategoryChannel(categoryChannel);

        // 엔티티 저장
        return categoryChannelManagerRepository.save(manager);
    }
}
