package com.outsider.masterofpredictionbackend.user.query.usersearch;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class UserQueryServiceIntegrationTest {

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private UserSearchRepository userSearchRepository;

    @BeforeEach
    void setUp() {
        // 테스트 데이터를 Elasticsearch에 저장
        UserSearchModel user1 = new UserSearchModel(
                1L,
                "John Doe",
                "Gold",
                "john_img.jpg",
                "johndoe"
        );

        UserSearchModel user2 = new UserSearchModel(
                2L,
                "Johnny Appleseed",
                "Silver",
                "johnny_img.jpg",
                "johnny"
        );

        UserSearchModel user3 = new UserSearchModel(
                3L,
                "Jane Smith",
                "Platinum",
                "jane_img.jpg",
                "janesmith"
        );

        userSearchRepository.save(user1);
        userSearchRepository.save(user2);
        userSearchRepository.save(user3);

        // Elasticsearch 인덱싱 완료를 기다림
        // 인덱싱 완료를 기다리기 위해 최대 10초까지 대기
        await().atMost(10, SECONDS).until(() -> userSearchRepository.count() == 3);
    }
    @AfterEach
    void tearDown() {
        // 테스트 후 데이터 정리
        userSearchRepository.deleteAll();
    }
    @DisplayName("디스플레이 이름 검색 테스트")
    @Test
    void testSearchByDisplayName() {
        // Given
        String displayName = "John";
        Pageable pageable = PageRequest.of(0, 10);

        // When
        Page<UserSearchModel> result = userQueryService.searchByDisplayName(displayName, pageable);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(2);
        System.out.println(result.getContent());
        assertThat(result.getContent())
                .extracting(UserSearchModel::getDisplayName)
                .containsExactlyInAnyOrder("John Doe", "Johnny Appleseed");
    }

//    @Test
//    void testSearchByUserName() {
//        // Given
//        String userName = "janesmith";
//        Pageable pageable = PageRequest.of(0, 5);
//
//        // When
//        Page<UserSearchModel> result = userQueryService.searchByUserName(userName, pageable);
//
//        // Then
//        assertThat(result.getTotalElements()).isEqualTo(1);
//
//        UserSearchModel user = result.getContent().get(0);
//        assertThat(user.getDisplayName()).isEqualTo("Jane Smith");
//        assertThat(user.getTier()).isEqualTo("Platinum");
//        assertThat(user.getUserImg()).isEqualTo("jane_img.jpg");
//        assertThat(user.getUserName()).isEqualTo("janesmith");
//    }
}
