package com.outsider.masterofpredictionbackend.feed.command.infrastructure.service;

import com.outsider.masterofpredictionbackend.feed.command.domain.service.ExternalFileService;
import com.outsider.masterofpredictionbackend.file.MinioService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinioFileService implements ExternalFileService {
    private final MinioService minioService;

    public MinioFileService(MinioService minioService) {
        this.minioService = minioService;
    }

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        return minioService.uploadFile(file);
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) throws Exception {
        List<String> fileUrls = new ArrayList<>();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileUrl = minioService.uploadFile(file);
                fileUrls.add(fileUrl);
            }
        }
        return fileUrls;
    }
}