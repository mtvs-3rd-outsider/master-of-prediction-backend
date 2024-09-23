package com.outsider.masterofpredictionbackend.feed.command.application.service;

import com.outsider.masterofpredictionbackend.file.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileService {
    private final MinioService minioService;

    public FileService(MinioService minioService) {
        this.minioService = minioService;
    }

    public String uploadFile(MultipartFile file) throws Exception {
        return minioService.uploadFile(file);
    }
}