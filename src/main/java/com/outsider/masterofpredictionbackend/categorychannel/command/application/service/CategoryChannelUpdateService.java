package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.service.CategoryChannelUploadImage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryChannelUpdateService {

    private final CategoryChannelRepository categoryChannelRepository;
    private final CategoryChannelUploadImage categoryChannelUploadImage;

    @Autowired
    public CategoryChannelUpdateService(CategoryChannelRepository categoryChannelRepository, CategoryChannelUploadImage categoryChannelUploadImage) {
        this.categoryChannelRepository = categoryChannelRepository;
        this.categoryChannelUploadImage = categoryChannelUploadImage;
    }

    /**
     * 카테고리 채널 정보를 업데이트.
     *
     * 이름, 소유유저id, 간단한 설명, 규칙, 대표 이미지를 수정 가능
     * 대표 이미지 변경하지 않는 경우 MultipartFile 은 빈 객체로 오도록 구현
     *
     * @param updateRequestDTO 카테고리 채널 id 를 포함한 정보 수정 정보가 담긴 DTO, 변경 안하는 변수는 null
     * @param imageFile 대표 이미지 1개, 변경 안한다면 빈 객체
     * @exception RuntimeException 이미지 업로드 실패 시 발생
     */
    @Transactional
    public void updateCategoryChannel(CategoryChannelUpdateRequestDTO updateRequestDTO,
                                      MultipartFile imageFile) {

        CategoryChannel categoryChannel = categoryChannelRepository.findById(updateRequestDTO.getId())
                .orElseThrow(() ->  new RuntimeException("Category channel not found"));

        categoryChannel.updateCategoryChannelBasedOnDTO(updateRequestDTO);

        if (!imageFile.isEmpty()) {
            try {
                String imageUrl = categoryChannelUploadImage.uploadImage(imageFile);
                categoryChannel.setImageUrl(imageUrl);
            } catch (Exception e) {
                // TODO: 예외처리 통일되면 추가 예정
                throw new RuntimeException("Error while uploading image", e);
            }
        }

        categoryChannelRepository.save(categoryChannel);
    }

    /**
     * 카테고리 채널 상태를 변경
     *
     * 사용자가 비활성화하거나(정책 변경 가능성 있음), 관리자가 승인 및 거절 할 때 사용할 메서드
     *
     * @param id 카테고리 채널 id
     * @param categoryChannelStatus 카테고리 채널 상태
     */
    @Transactional
    public void changeCategoryChannelStatus(long id, CategoryChannelStatus categoryChannelStatus) {
        CategoryChannel categoryChannel = categoryChannelRepository.findById(id)
                .orElseThrow(() ->  new RuntimeException("Category channel not found"));

        categoryChannel.setCategoryChannelStatus(categoryChannelStatus);

        categoryChannelRepository.save(categoryChannel);
    }

}
