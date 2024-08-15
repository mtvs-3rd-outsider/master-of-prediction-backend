package com.outsider.masterofpredictionbackend.categorychannel.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.service.CategoryChannelUploadImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryChannelUploadImageImpl implements CategoryChannelUploadImage {

    @Override
    public String uploadImage(MultipartFile imageFile) {
        return "https://s3.......awdawd.jpg";
    }
}
