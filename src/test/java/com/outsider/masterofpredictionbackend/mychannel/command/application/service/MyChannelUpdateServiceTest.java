package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.aggregate.MyChannel;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Stream;

@SpringBootTest
@Transactional
public class MyChannelUpdateServiceTest {

    @Autowired
    private MyChannelRegistService myChannelRegistService;

    @Autowired
    private MyChannelUpdateService myChannelUpdateService;

    @Autowired
    private MyChannelRepository myChannelRepository;

    private Long createdChannelId;

    @BeforeEach
    void setUp() {
        // 채널을 미리 등록해두고 ID를 저장합니다.
        MyChannelRegistRequestDTO registDTO = new MyChannelRegistRequestDTO();
        registDTO.setDisplayName("Original Name");
        registDTO.setBio("Original Bio");
        registDTO.setWebsite("https://original-website.com");
        registDTO.setFollowersCount(100);
        registDTO.setFollowingCount(50);
        registDTO.setUser(1L);

        createdChannelId = myChannelRegistService.registMyChannel(registDTO);
    }

    private static Stream<Arguments> getUpdatedChannelInfos() {
        return Stream.of(
                Arguments.of("Updated Name", "Updated Bio", "https://updated-website.com"),
                Arguments.of("Another Name", "", ""),
                Arguments.of("Another Name", null, null)
        );
    }

    @ParameterizedTest(name = "Test update of channel with new name {0}")
    @MethodSource("getUpdatedChannelInfos")
    void testUpdateMyChannel(String displayName, String bio, String website) {
        // 업데이트 전 기존 값을 보관
        Optional<MyChannel> originalChannel = myChannelRepository.findById(createdChannelId);
        Assertions.assertTrue(originalChannel.isPresent());
        MyChannel original = originalChannel.get();

        String originalDisplayName = original.getDisplayName();
        String originalBio = original.getBio();
        String originalWebsite = original.getWebsite();

        MyChannelUpdateRequestDTO updateDTO = new MyChannelUpdateRequestDTO();
        updateDTO.setChannelId(createdChannelId);
        updateDTO.setDisplayName(displayName);
        updateDTO.setBio(bio);
        updateDTO.setWebsite(website);

        // 업데이트 수행
        myChannelUpdateService.updateMyChannel(updateDTO);

        // 업데이트된 채널 정보를 DB에서 가져와서 검증
        Optional<MyChannel> updatedChannel = myChannelRepository.findById(createdChannelId);
        Assertions.assertTrue(updatedChannel.isPresent());
        MyChannel myChannel = updatedChannel.get();

        // null이 아닌 필드만 업데이트되었는지 확인
        Assertions.assertEquals(displayName != null ? displayName : originalDisplayName, myChannel.getDisplayName());
        Assertions.assertEquals(bio != null ? bio : originalBio, myChannel.getBio());
        Assertions.assertEquals(website != null ? website : originalWebsite, myChannel.getWebsite());
    }
}
