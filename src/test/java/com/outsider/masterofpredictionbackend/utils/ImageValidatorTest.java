package com.outsider.masterofpredictionbackend.utils;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ImageValidatorTest {

    @Test
    void testImageSizeThrow() {
        byte[] largeContent = new byte[4 * 1024 * 1024]; // 4MB
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "large_image.jpg",
                "image/jpeg",
                largeContent
        );

        assertThrows(InvalidImageException.class, () -> ImageValidator.validateImageFile(mockFile));
    }

    @Test
    void testTypeThrow() {
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.txt",
                "text/plain",
                "not an image".getBytes()
        );

        assertThrows(InvalidImageException.class, () -> ImageValidator.validateImageFile(mockFile));
    }

    @Test
    void testvalidateImageFormatThrow(){
        MockMultipartFile mockFile = new MockMultipartFile(
                "image",
                "image.txt",
                "text/jpeg",
                "not an image".getBytes()
        );

        assertThrows(InvalidImageException.class, () -> ImageValidator.validateImageFile(mockFile));
    }


    @Test
    void testValidateImageFile_success() throws IOException {
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

        // BufferedImage를 바이트 배열로 변환
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();

        // MockMultipartFile로 변환
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg",
                new ByteArrayInputStream(imageBytes));

        // 실제 ImageIO.read 호출
        BufferedImage image = ImageIO.read(file.getInputStream());

        // 이미지가 null이 아닌지 확인 (정상적인 이미지)
        assertNotNull(image);
        assertDoesNotThrow(() -> ImageValidator.validateImageFile(file));
    }

}
