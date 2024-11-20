package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.service.ChannelSubscribeClient;
import com.outsider.masterofpredictionbackend.file.FileUploadService;
import com.outsider.masterofpredictionbackend.utils.IdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryChannelRegistService {

    private final CategoryChannelRepository categoryChannelRepository;
    private final FileUploadService fileUploadService;
    private final ChannelSubscribeClient channelSubscribeClient;

    @Autowired
    public CategoryChannelRegistService(CategoryChannelRepository categoryChannelRepository, FileUploadService fileUploadService, ChannelSubscribeClient channelSubscribeClient) {
        this.categoryChannelRepository = categoryChannelRepository;
        this.fileUploadService = fileUploadService;
        this.channelSubscribeClient = channelSubscribeClient;
    }

    @Transactional
    public Long registerCategoryChannelWithManualId(
            CategoryChannelRegistRequestDTO registRequestDTO,
            MultipartFile representativeImageFile,
            MultipartFile bannerImageFile,
            Long userId,
            Long manualId
    ) {
        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                userId,
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );
        categoryChannel.setCategoryChannelId(manualId);

        channelSubscribeClient.publish(userId, manualId, false, "subscribe");

        try {
            if (representativeImageFile != null && !representativeImageFile.isEmpty()) {
                String representativeImageUrl = fileUploadService.uploadFile(representativeImageFile);
                categoryChannel.setImageUrl(representativeImageUrl);
            }

            if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
                String bannerImageUrl = fileUploadService.uploadFile(bannerImageFile);
                categoryChannel.setBannerImg(bannerImageUrl);
            }

            categoryChannelRepository.save(categoryChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryChannel.getId(); // 생성된 ID 반환
    }

    @Transactional
    public Long registerCategoryChannel(
            CategoryChannelRegistRequestDTO registRequestDTO,
            MultipartFile representativeImageFile,
            MultipartFile bannerImageFile,
            Long userId
    ) {
        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                userId,
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );
        categoryChannel.setCategoryChannelId(IdGenerator.generateId());
        channelSubscribeClient.publish(userId, categoryChannel.getId(), false, "subscribe");

        try {
            if (representativeImageFile != null && !representativeImageFile.isEmpty()) {
                String representativeImageUrl = fileUploadService.uploadFile(representativeImageFile);
                categoryChannel.setImageUrl(representativeImageUrl);
            }

            if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
                String bannerImageUrl = fileUploadService.uploadFile(bannerImageFile);
                categoryChannel.setBannerImg(bannerImageUrl);
            }

            categoryChannelRepository.save(categoryChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryChannel.getId(); // 생성된 ID 반환
    }
}
