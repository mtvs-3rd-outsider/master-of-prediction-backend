package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.UpdateChannelUserCountDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
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
public class UpdateMyChannelFollowerCountServiceTest {

    @Autowired
    private UpdateMyChannelFollowerCountService updateMyChannelFollowerCountService;
    @Autowired
    private MyChannelRegistService myChannelRegistService;
    @Autowired
    private MyChannelRepository myChannelRepository;

    private MyChannel myChannel;
    private Long createdChannelId;
    @BeforeEach
    void setUp() {
        // 테스트용 MyChannel 객체를 저장합니다.
        MyChannelRegistRequestDTO registDTO = new MyChannelRegistRequestDTO();
        registDTO.setDisplayName("Original Name");
        registDTO.setBio("Original Bio");
        registDTO.setWebsite("https://original-website.com");
        registDTO.setFollowersCount(100);
        registDTO.setFollowingCount(50);
        registDTO.setUser(1L);
        // Repository를 통해 채널을 저장하고, 이후에 사용할 수 있도록 설정합니다.
        createdChannelId = myChannelRegistService.registMyChannel(registDTO);
    }

    private static Stream<Arguments> getChannelFollowerUpdateInfo() {
        return Stream.of(
                Arguments.of( true, 101),  // 팔로워 증가 테스트
                Arguments.of( false, 99)   // 팔로워 감소 테스트
        );
    }

    @ParameterizedTest(name = "Test follower count update for isPlus={0}")
    @MethodSource("getChannelFollowerUpdateInfo")
    void testUpdateFollowerMyChannel(boolean isPlus, int expectedCount) {
        UpdateChannelUserCountDTO dto = new UpdateChannelUserCountDTO();
        dto.setChannelId(createdChannelId); // 저장된 채널 ID 사용
        dto.setIsPlus(isPlus);

        // 업데이트 수행
        updateMyChannelFollowerCountService.updateFollowerMyChannel(dto);

        // 업데이트된 채널 정보를 DB에서 가져와서 검증
        Optional<MyChannel> updatedChannel = myChannelRepository.findById(createdChannelId);
        Assertions.assertTrue(updatedChannel.isPresent());
        Assertions.assertEquals(expectedCount, updatedChannel.get().getUserCounts().getFollowersCount());
    }


    @Test
    void testUpdateFollowerMyChannel_ChannelNotFound() {
        Optional<MyChannel> originalChannel = myChannelRepository.findById(createdChannelId);
        Assertions.assertTrue(originalChannel.isPresent());
        MyChannel original = originalChannel.get();
        UpdateChannelUserCountDTO dto = new UpdateChannelUserCountDTO();
        dto.setChannelId(999L); // 존재하지 않는 채널 ID 사용
        dto.setIsPlus(true);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            updateMyChannelFollowerCountService.updateFollowerMyChannel(dto);
        });
    }
}
