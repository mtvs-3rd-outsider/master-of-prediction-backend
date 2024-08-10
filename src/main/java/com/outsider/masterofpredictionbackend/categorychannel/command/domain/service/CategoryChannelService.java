package com.outsider.masterofpredictionbackend.categorychannel.command.domain.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CategoryChannelService {

    private final CategoryChannelRepository categoryChannelRepository;
    private final CategoryChannelUploadImage categoryChannelUploadImage;

    @Autowired
    public CategoryChannelService(CategoryChannelRepository categoryChannelRepository, CategoryChannelUploadImage categoryChannelUploadImage) {
        this.categoryChannelRepository = categoryChannelRepository;
        this.categoryChannelUploadImage = categoryChannelUploadImage;
    }

    /**
     * 카테고리 채널 생성
     * @param categoryChannel
     * @param imageFile 대표 이미지 파일 1개
     * @throws IOException 파일 업로드 실패 시 예외처리
     */
    @Transactional
    public void createCategoryChannel(
            CategoryChannel categoryChannel,
            MultipartFile imageFile) throws IOException, IllegalArgumentException {

        // 이미지 파일 1개 업로드
        String imageUrl = "";
        try {
            imageUrl = categoryChannelUploadImage.uploadImage(imageFile);
            categoryChannel.setImageUrl(imageUrl);
        } catch (Exception e) {
            throw new IOException("파일 업로드 실패", e);
        }

        categoryChannelRepository.save(categoryChannel);
    }
}
