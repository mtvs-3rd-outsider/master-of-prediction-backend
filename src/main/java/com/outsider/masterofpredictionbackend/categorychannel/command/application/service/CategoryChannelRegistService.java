package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.file.FileUploadService;
import com.outsider.masterofpredictionbackend.utils.IdGenerator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryChannelRegistService {

    private final CategoryChannelRepository categoryChannelRepository;
    private final FileUploadService fileUploadService;  // 파일 업로드 인터페이스

    @Autowired
    public CategoryChannelRegistService(CategoryChannelRepository categoryChannelRepository, FileUploadService fileUploadService) {
        this.categoryChannelRepository = categoryChannelRepository;
        this.fileUploadService = fileUploadService;
    }
    @Transactional
    public void registerCategoryChannelWithManualId(
            CategoryChannelRegistRequestDTO registRequestDTO,
            MultipartFile representativeImageFile,
            MultipartFile bannerImageFile,
            Long userId,
            Long manualId // 수동으로 할당할 ID
    ) {

        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                userId, // ownerUserId
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );
        categoryChannel.setCategoryChannelId(manualId); // 수동으로 ID 할당
        try {
            // 대표 이미지 업로드 및 URL 설정
            if (representativeImageFile != null && !representativeImageFile.isEmpty()) {
                String representativeImageUrl = fileUploadService.uploadFile(representativeImageFile);
                categoryChannel.setImageUrl(representativeImageUrl);
            }

            // 배너 이미지 업로드 및 URL 설정
            if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
                String bannerImageUrl = fileUploadService.uploadFile(bannerImageFile);
                categoryChannel.setBannerImg(bannerImageUrl);
            }

            categoryChannelRepository.save(categoryChannel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Transactional
    public void registerCategoryChannel(
            CategoryChannelRegistRequestDTO registRequestDTO,
            MultipartFile representativeImageFile,  // 대표 이미지 파일
            MultipartFile bannerImageFile, // 배너 이미지 파일
            Long userId
    ) {

        CategoryChannel categoryChannel = new CategoryChannel(
                registRequestDTO.getDisplayName(),
                // TODO: 현재 로그인 된 유저 정보 넣기, 임의로 99 넣음.
                userId, // ownerUserId
                registRequestDTO.getDescription(),
                new CommunityRule(registRequestDTO.getCommunityRule()),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );
        categoryChannel.setCategoryChannelId(IdGenerator.generateId());
        try {
            // 대표 이미지 업로드 및 URL 설정
            if (representativeImageFile != null && !representativeImageFile.isEmpty()) {
                String representativeImageUrl = fileUploadService.uploadFile(representativeImageFile);
                categoryChannel.setImageUrl(representativeImageUrl);
            }

            // 배너 이미지 업로드 및 URL 설정
            if (bannerImageFile != null && !bannerImageFile.isEmpty()) {
                String bannerImageUrl = fileUploadService.uploadFile(bannerImageFile);
                categoryChannel.setBannerImg(bannerImageUrl);  // 배너 이미지 필드 설정
            }

            categoryChannelRepository.save(categoryChannel);
        } catch (Exception e) {
            // TODO: 예외 처리 통일되면 추가 예정
            e.printStackTrace();
        }
    }
}
