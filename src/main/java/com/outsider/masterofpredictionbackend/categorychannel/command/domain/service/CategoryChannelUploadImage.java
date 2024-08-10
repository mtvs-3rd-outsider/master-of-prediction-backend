package com.outsider.masterofpredictionbackend.categorychannel.command.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface CategoryChannelUploadImage {

    // TODO: 매개변수 타입은 협의 후 결정(json 에 담는 방식과 파일 따로 받는 방식), 리턴은 저장된 url
    String uploadImage(MultipartFile imageFile);
}
