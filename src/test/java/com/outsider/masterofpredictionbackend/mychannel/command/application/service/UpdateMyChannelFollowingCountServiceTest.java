package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateChannelUserCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
public class UpdateMyChannelFollowingCountServiceTest {

    @Autowired
    private UpdateMyChannelFollowingCountService updateMyChannelFollowingCountService;

    @Autowired
    private MyChannelRegistService myChannelRegistService;

    @Autowired
    private MyChannelCommandRepository myChannelRepository;

    private Long createdChannelId;

    @BeforeEach
    void setUp() {

        // Repository를 통해 채널을 저장하고, 이후에 사용할 수 있도록 설정합니다.
        createdChannelId = myChannelRegistService.registMyChannel(1L);
    }
    private static Stream<Arguments> getChannelFollowingUpdateInfo() {
        return Stream.of(
                Arguments.of(true, 1),   // 팔로잉 수 증가 테스트
                Arguments.of(false, -1),  // 팔로잉 수 감소 테스트
                Arguments.of(false, -5)  // 음수 팔로잉 수 에러 테스트
        );
    }

    @ParameterizedTest(name = "Test following count update for isPlus={0} with expectedCount={1}")
    @MethodSource("getChannelFollowingUpdateInfo")
    void testUpdateFollowingMyChannel(boolean isPlus, int expectedCount) {
        UpdateChannelUserCountDTO dto = new UpdateChannelUserCountDTO();
        dto.setChannelId(createdChannelId); // 저장된 채널 ID 사용
        dto.setIsPlus(isPlus);

        if (expectedCount < 0) {
            // 음수 값이 발생할 수 있는 경우 예외를 테스트
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                updateMyChannelFollowingCountService.updateFollowingsMyChannel(dto);
            });
        } else {
            // 업데이트 수행
            updateMyChannelFollowingCountService.updateFollowingsMyChannel(dto);

            // 업데이트된 채널 정보를 DB에서 가져와서 검증
            Optional<MyChannel> updatedChannel = myChannelRepository.findById(createdChannelId);
            Assertions.assertTrue(updatedChannel.isPresent());
            Assertions.assertEquals(expectedCount, updatedChannel.get().getUserCounts().getFollowingCount());
        }
    }
    @Test
    void testUpdateFollowingMyChannel_ChannelNotFound() {
        UpdateChannelUserCountDTO dto = new UpdateChannelUserCountDTO();
        dto.setChannelId(999L); // 존재하지 않는 채널 ID 사용
        dto.setIsPlus(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            updateMyChannelFollowingCountService.updateFollowingsMyChannel(dto);
        });
    }
}
