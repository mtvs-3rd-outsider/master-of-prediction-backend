package com.outsider.masterofpredictionbackend.utils;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

public class ImageStorageServiceTest {

    private final String testImageName = "test.jpg";
    private final String uploadDir = "src/main/resources/static/images/";
    private MockMultipartFile mockFile;

    @BeforeEach
    void setUp() throws IOException{
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);

        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        mockFile = new MockMultipartFile("file", testImageName, "image/jpeg",
                new ByteArrayInputStream(imageBytes));
    }


    @Test
    @DisplayName("사진 저장 테스트")
    void testSaveImage() throws IOException {
        UUID uuid = UUID.randomUUID();
        Mockito.mockStatic(UUID.class);
        Mockito.when(UUID.randomUUID()).thenReturn(uuid);

        String result = ImageStorageService.saveImage(mockFile);

        StringBuilder sb = new StringBuilder();
        String separator = "_";
        sb.append(uuid);
        sb.append(separator);
        sb.append(testImageName);
        Path expectedPath = Paths.get(uploadDir + sb);

        // 파일 생성 확인
        Assertions.assertTrue(Files.exists(expectedPath), "파일이 생성되어야 합니다.");
        Assertions.assertEquals(sb.toString(), result);

        Files.deleteIfExists(expectedPath);
    }

    @Test
    @DisplayName("사진 삭제 테스트")
    void testDeleteImage() throws IOException {
        String fileName = ImageStorageService.saveImage(mockFile);
        Path path = Paths.get(uploadDir + fileName);
        if (!Files.exists(path)){
            Assertions.fail("파일 생성 초기화 실패.");
        }
        ImageStorageService.deleteImage(fileName);
        Assertions.assertFalse(Files.exists(path), "파일이 삭제되어야 합니다.");
    }
}
