package com.outsider.masterofpredictionbackend.mychannel.command.application.service;

import com.outsider.masterofpredictionbackend.mychannel.command.application.dto.MyChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.mychannel.command.application.service.MyChannelRegistService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

@SpringBootTest
@Transactional
public class MyChannelRegistServiceTest {

    @Autowired
    private MyChannelRegistService myChannelRegistService;

    private static Stream<Arguments> getChannelInfos() {
        return Stream.of(
                Arguments.of("John Doe", "This is John's bio", "https://example.com", 1L),
                Arguments.of("Jane Smith", "This is Jane's bio",  "https://example.org",  1L),
                Arguments.of("Jane Smith", "", "",  1L),
                Arguments.of("Jane Smith", null , null, 1L)
        );
    }

    @ParameterizedTest(name = "Test registration of channel for {0}")
    @MethodSource("getChannelInfos")
    void testRegistMyChannel(String displayName, String bio, String website,  Long user) {
        MyChannelRegistRequestDTO dto = new MyChannelRegistRequestDTO();
        dto.setDisplayName(displayName);
        dto.setBio(bio);
        dto.setWebsite(website);
        dto.setUser(user);
        Assertions.assertDoesNotThrow(() -> myChannelRegistService.registMyChannel(dto));
    }


}
