package com.outsider.masterofpredictionbackend.file;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucketName}")
    private String bucketName;

    public void uploadFile(MultipartFile file) throws Exception {
        // 버킷이 존재하는지 확인하고, 존재하지 않으면 생성
        boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!isExist) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }

        // 파일 업로드
        minioClient.putObject(
                PutObjectArgs.builder().bucket(bucketName).object(file.getOriginalFilename()).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
    }

    public String getFileUrl(String filename) throws Exception {
        // Pre-signed URL 생성
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(filename)
                        .expiry(24 * 60 * 60) // 24시간 유효
                        .build());
    }
}
