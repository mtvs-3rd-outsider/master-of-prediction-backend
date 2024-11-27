package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannelManager;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.ManagerRole;
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
        // CategoryChannel 생성
        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );
        categoryChannel.setCategoryChannelId(manualId);

        // 소유자 설정
        CategoryChannelManager owner = new CategoryChannelManager();
        owner.setUserId(userId);
        owner.setRole(ManagerRole.OWNER);
        owner.setCategoryChannel(categoryChannel);

        // 소유자 추가
        categoryChannel.getManagers().add(owner);

        // 구독 알림 전송
        channelSubscribeClient.publish(userId, manualId, false, "subscribe");

        try {
            // 대표 이미지 업로드
            if (representativeImageFile != null && !representativeImageFile.isEmpty()) {
                String representativeImageUrl = fileUploadService.uploadFile(representativeImageFile);
                categoryChannel.setImageUrl(representativeImageUrl);
            }

            // 배너 이미지 업로드
            if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
                String bannerImageUrl = fileUploadService.uploadFile(bannerImageFile);
                categoryChannel.setBannerImg(bannerImageUrl);
            }

            // 저장
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
        // CategoryChannel 생성
        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );
        categoryChannel.setCategoryChannelId(IdGenerator.generateId());

        // 소유자 설정
        CategoryChannelManager owner = new CategoryChannelManager();
        owner.setUserId(userId);
        owner.setRole(ManagerRole.OWNER);
        owner.setCategoryChannel(categoryChannel);

        // 소유자 추가
        categoryChannel.getManagers().add(owner);

        // 구독 알림 전송
        channelSubscribeClient.publish(userId, categoryChannel.getId(), false, "subscribe");

        try {
            // 대표 이미지 업로드
            if (representativeImageFile != null && !representativeImageFile.isEmpty()) {
                String representativeImageUrl = fileUploadService.uploadFile(representativeImageFile);
                categoryChannel.setImageUrl(representativeImageUrl);
            }

            // 배너 이미지 업로드
            if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
                String bannerImageUrl = fileUploadService.uploadFile(bannerImageFile);
                categoryChannel.setBannerImg(bannerImageUrl);
            }

            // 저장
            categoryChannelRepository.save(categoryChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categoryChannel.getId(); // 생성된 ID 반환
    }
}
