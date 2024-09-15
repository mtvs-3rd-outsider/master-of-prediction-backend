package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.stream.Stream;

@SpringBootTest
class CategoryChannelRegistServiceTest {

    @Autowired
    CategoryChannelRegistService categoryChannelRegistService;

    private static Stream<Arguments> newCategoryChannel() {
        return Stream.of(
                Arguments.of(
                        new CategoryChannelRegistRequestDTO(
                                "카테고리 채널 이름",
                                "간단한 설명",
                                "\"{\\n  \\\"rules\\\": [\\n    {\\n      \\\"number\\\": 1,\\n      \\\"rule\\\": \\\"규칙 1\\\"\\n    },\\n    {\\n      \\\"number\\\": 2,\\n      \\\"rule\\\": \\\"규칙 2\\\"\\n    }\\n  ]\\n}\"",
                                CategoryChannelStatus.APPLY
                        ),
                        new MockMultipartFile(
                                "representativeImage",                       // 대표 이미지 파일 파라미터 이름
                                "representativeImage.jpg",               // 원래 파일 이름
                                "image/jpeg",                 // MIME 타입
                                "This is a representative image file.".getBytes() // 파일 내용
                        ),
                        new MockMultipartFile(
                                "bannerImage",                       // 배너 이미지 파일 파라미터 이름
                                "bannerImage.jpg",               // 원래 파일 이름
                                "image/jpeg",                 // MIME 타입
                                "This is a banner image file.".getBytes() // 파일 내용
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("newCategoryChannel")
    @Transactional
    void newCategoryChannel(CategoryChannelRegistRequestDTO registRequestDTO, MockMultipartFile representativeImageFile, MockMultipartFile bannerImageFile) {

        Assertions.assertDoesNotThrow(
                () -> {
                    categoryChannelRegistService.registerCategoryChannel(registRequestDTO, representativeImageFile, bannerImageFile, 99L); // userId 예시로 99L 사용
                }
        );
    }
}
