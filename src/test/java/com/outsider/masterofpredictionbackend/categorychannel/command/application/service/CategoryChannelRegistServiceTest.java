package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import jakarta.transaction.Transactional;
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
                                "커뮤니티 룰",
                                CategoryChannelStatus.APPLY
                        ),
                        new MockMultipartFile(
                                "file",                       // 파라미터 이름
                                "testfile.txt",               // 원래 파일 이름
                                "text/plain",                 // MIME 타입
                                "This is a test file.".getBytes() // 파일 내용
                        )

                )
        );
    }

    @ParameterizedTest
    @MethodSource("newCategoryChannel")
    @Transactional
    void newCategoryChannel(CategoryChannelRegistRequestDTO registRequestDTO, MockMultipartFile file) {

        categoryChannelRegistService.registerCategoryChannel(registRequestDTO, file);
    }
}