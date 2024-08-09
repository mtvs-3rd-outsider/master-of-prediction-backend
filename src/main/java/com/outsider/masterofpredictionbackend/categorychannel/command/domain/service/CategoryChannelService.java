package com.outsider.masterofpredictionbackend.categorychannel.command.domain.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryChannelService {

    private CategoryChannelRepository categoryChannelRepository;

    @Autowired
    public CategoryChannelService(CategoryChannelRepository categoryChannelRepository) {
        this.categoryChannelRepository = categoryChannelRepository;
    }

    @Transactional
    public void createCategoryChannel(CategoryChannelRegistRequestDTO registRequestDTO) {

        CategoryChannel categoryChannel = new CategoryChannel(
            registRequestDTO.getDisplayName(),
                // TODO: 현재 로그인 된 유저 정보 넣으면 될 듯
                99,// ownerUserId
                // TODO: 업로드 완료 후 S3 에서 받은 url 넣기
                "",// imageUrl
                registRequestDTO.getDescription(),
                registRequestDTO.getCommunityRule(),
                new CategoryChannelUserCounts(1),
                registRequestDTO.getCategoryChannelStatus()
        );

        categoryChannelRepository.save(categoryChannel);
    }
}
