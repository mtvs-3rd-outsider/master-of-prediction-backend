package com.outsider.masterofpredictionbackend.categorychannel.command.domain.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Stream;

public class CategoryChannelServiceTests {

    @Mock
    private CategoryChannelRepository categoryChannelRepository;

    @Mock
    private CategoryChannelUploadImage categoryChannelUploadImage;

    @InjectMocks
    private CategoryChannelService categoryChannelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
    }

    private static Stream<Arguments> newCategoryChannel() {
        return Stream.of(
                Arguments.of(new CategoryChannel(
                                "카테고리 채널 이름",
                                99,
                                "url",
                                "간단한 설명",
                                new CommunityRule("\"\\\"{\\\\n  \\\\\\\"rules\\\\\\\": [\\\\n    {\\\\n      \\\\\\\"number\\\\\\\": 1,\\\\n      \\\\\\\"rule\\\\\\\": \\\\\\\"규칙 1\\\\\\\"\\\\n    },\\\\n    {\\\\n      \\\\\\\"number\\\\\\\": 2,\\\\n      \\\\\\\"rule\\\\\\\": \\\\\\\"규칙 2\\\\\\\"\\\\n    }\\\\n  ]\\\\n}\\\"\","),
                                new CategoryChannelUserCounts(1),
                                CategoryChannelStatus.APPLY
                        ), new MockMultipartFile(
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
    void testCreateCategoryChannel(
            CategoryChannel categoryChannel,
            MultipartFile imageFile
    ) {

        Assertions.assertDoesNotThrow(
                () -> categoryChannelService.createCategoryChannel(
                        categoryChannel,
                        imageFile
                )
        );
    }

}
