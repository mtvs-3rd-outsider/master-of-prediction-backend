package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.service.CategoryChannelUploadImage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryChannelRegistService {

    private final CategoryChannelRepository categoryChannelRepository;
    private final CategoryChannelUploadImage categoryChannelUploadImage;

    @Autowired
    public CategoryChannelRegistService(CategoryChannelRepository categoryChannelRepository, CategoryChannelUploadImage categoryChannelUploadImage) {
        this.categoryChannelRepository = categoryChannelRepository;
        this.categoryChannelUploadImage = categoryChannelUploadImage;
    }

    /**
     * 카테고리 채널 생성.
     *
     * DTO 에서 displayName, description, communityRule(JSON 형식 문자열) 정보를 받고, 이미지 파일을 받음.
     * 유저 인증 로직은 토큰을 통해 유저 정보 가져오고, 이미지 업로드는 MinIO 사용할 예정.
     *
     * @param registRequestDTO 카테고리 채널 등록 신청시 정보가 담긴 DTO
     * @param imageFile 대표 이미지 1개
     * @exception RuntimeException 이미지 업로드 실패 시 발생
     */
    @Transactional
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
            String imageUrl = categoryChannelUploadImage.uploadImage(imageFile);
            categoryChannel.setImageUrl(imageUrl);
            categoryChannelRepository.save(categoryChannel);
        } catch (Exception e) {
            // TODO: 예외처리 통일되면 추가 예정
            throw new RuntimeException("Error while uploading image", e);
        }
    }
}
