package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.service.CategoryChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CategoryChannelRegistService {

    private CategoryChannelService categoryChannelService;

    @Autowired
    public CategoryChannelRegistService(CategoryChannelService categoryChannelService) {
        this.categoryChannelService = categoryChannelService;
    }

    public void registerCategoryChannel(
            CategoryChannelRegistRequestDTO registRequestDTO,
            MultipartFile imageFile) {

        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                // TODO: 현재 로그인 된 유저 정보 넣기, 임의로 99 넣음.
                99, // ownerUserId
                "", // image upload 는 도메인 서비스에서 진행
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );

        try {
            categoryChannelService.createCategoryChannel(categoryChannel, imageFile);
        } catch (IOException e) {
            // TODO: 예외처리 통일되면 추가 예정
            e.printStackTrace();
        }
    }
}
