package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;


import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelManagerAssignRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelManagerMapper;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CategoryChannelManagerService {

    private final CategoryChannelManagerRepository categoryChannelManagerRepository;
    private final CategoryChannelManagerMapper categoryChannelManagerMapper;

    @Transactional
    public CategoryChannelManager assignManager(CategoryChannelManagerAssignRequestDTO requestDTO) {
        // DTO -> Entity 매핑
        CategoryChannelManager manager = categoryChannelManagerMapper.toEntity(requestDTO);

        // 엔티티 저장
        return categoryChannelManagerRepository.save(manager);
    }
}
