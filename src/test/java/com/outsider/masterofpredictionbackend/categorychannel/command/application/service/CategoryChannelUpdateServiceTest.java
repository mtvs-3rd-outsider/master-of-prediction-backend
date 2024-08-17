package com.outsider.masterofpredictionbackend.categorychannel.command.application.service;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.CategoryChannel;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CategoryChannelUserCounts;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.embedded.CommunityRule;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.stream.Stream;

@SpringBootTest
class CategoryChannelUpdateServiceTest {

    @Autowired
    CategoryChannelUpdateService categoryChannelUpdateService;
    @Autowired
    private CategoryChannelRepository categoryChannelRepository;

    private Long savedCategoryChannelId;
    private CategoryChannel dummyCategoryChannel;

    @BeforeEach
    void setUp() {
        CategoryChannel categoryChannel = new CategoryChannel(
                "이름",
                // TODO: 현재 로그인 된 유저 정보 넣기, 임의로 99 넣음.
                1, // ownerUserId
                "", // image upload 는 도메인 서비스에서 진행
                "설명",
                new CommunityRule(""),
                new CategoryChannelUserCounts(1),
                CategoryChannelStatus.APPLY
        );

        dummyCategoryChannel = categoryChannelRepository.save(categoryChannel);
        savedCategoryChannelId = dummyCategoryChannel.getCategoryChannelId();
    }

    private static Stream<Arguments> updateRequestDTOTestData() {
        return Stream.of(
                // displayName, description, communityRule 변경
                Arguments.of(
                        "카테고리 채널 이름2",
                        "간단한 설명2",
                        "\"{\\n  \\\"rules\\\": [\\n    {\\n      \\\"number\\\": 1,\\n      \\\"rule\\\": \\\"규칙 3\\\"\\n    },\\n    {\\n      \\\"number\\\": 2,\\n      \\\"rule\\\": \\\"규칙 4\\\"\\n    }\\n  ]\\n}\"",
                        new MockMultipartFile("file", "", "text/plain", new byte[0])
                ),
                // communityRule 만 규칙5, 규칙6 으로 문자열 변경
                Arguments.of(
                        null,
                        null,
                        "\"{\\n  \\\"rules\\\": [\\n    {\\n      \\\"number\\\": 1,\\n      \\\"rule\\\": \\\"규칙 5\\\"\\n    },\\n    {\\n      \\\"number\\\": 2,\\n      \\\"rule\\\": \\\"규칙 6\\\"\\n    }\\n  ]\\n}\"",
                        new MockMultipartFile("file", "", "text/plain", new byte[0])
                ),
                // 다 null 인 경우
                Arguments.of(
                        null,
                        null,
                        null,
                        new MockMultipartFile("file", "", "text/plain", new byte[0])
                ),
                Arguments.of(
                        "",
                        null,
                        null,
                        new MockMultipartFile("file", "", "text/plain", new byte[0])
                )
        );
    }

    @ParameterizedTest
    @MethodSource("updateRequestDTOTestData")
    @Transactional
    void updateInfoCategoryChannelTest(
            String displayName,
            String description,
            String communityRule,
            MockMultipartFile file) {

        // savedCategoryChannelId 값을 updateRequestDTOTestData 에 넣는 경우 beforeEach 보다 updateRequestDTOTestData 가 먼저
        // 실행되어 null 값이 들어가므로 여기에서 DTO 생성
        CategoryChannelUpdateRequestDTO updateRequestDTO = new CategoryChannelUpdateRequestDTO(
                savedCategoryChannelId,
                displayName,
                description,
                communityRule
        );

        Assertions.assertDoesNotThrow(
                () -> {
                    categoryChannelUpdateService.updateCategoryChannel(updateRequestDTO, file);
                }
        );

        // 업데이트 후 데이터베이스 정보 읽기
        CategoryChannel updatedCategoryChannel = categoryChannelRepository.findById(savedCategoryChannelId)
                .orElseThrow(() -> new RuntimeException("Category channel not found"));

        Assertions.assertEquals(
                displayName == null? dummyCategoryChannel.getDisplayName() : displayName,
                updatedCategoryChannel.getDisplayName()
        );
        Assertions.assertEquals(
                description == null? dummyCategoryChannel.getDescription() : description,
                updatedCategoryChannel.getDescription()
        );
        Assertions.assertEquals(
                communityRule == null? dummyCategoryChannel.getCommunityRule().getCommunityRule() : communityRule,
                updatedCategoryChannel.getCommunityRule().getCommunityRule()
        );
    }

    @Test
    @Transactional
    public void updateCategoryChannelStatusTest() {

        // APPLY -> APPROVED
        categoryChannelUpdateService.changeCategoryChannelStatus(savedCategoryChannelId, CategoryChannelStatus.APPROVED);

        CategoryChannel updatedCategoryChannel = categoryChannelRepository.findById(savedCategoryChannelId)
                .orElseThrow(() -> new RuntimeException("Category channel not found"));

        Assertions.assertEquals(
                CategoryChannelStatus.APPROVED,
                updatedCategoryChannel.getCategoryChannelStatus()
        );
    }
}