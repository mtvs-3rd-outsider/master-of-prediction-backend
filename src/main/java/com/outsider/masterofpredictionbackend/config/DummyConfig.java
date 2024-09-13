package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service.ChannelSubscribeService;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.DeleteUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserProfileUpdateService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.*;

@Configuration
public class DummyConfig {

    private final UserRegistService userRegistService;
    private final DeleteUserService deleteUserService;
    private final ChannelSubscribeService subscribeService;
    private final UserProfileUpdateService userProfileUpdateService;
    private final UserCommandRepository userCommandRepository;

    private final String defaultEmail = DEFAULT_USER_EMAIL;
    private final String defaultUserName = DEFAULT_USER_NAME;
    private final String defaultPassword = DEFAULT_USER_PASSWORD;

    // 리스트를 사용하여 여러 사용자 ID를 저장
    private final List<Long> userIds = new ArrayList<>();

    public DummyConfig(UserCommandRepository userCommandRepository,
                       UserRegistService userRegistService,
                       DeleteUserService deleteUserService,
                       ChannelSubscribeService subscribeService,
                       UserProfileUpdateService userProfileUpdateService) {
        this.userCommandRepository = userCommandRepository;
        this.userRegistService = userRegistService;
        this.deleteUserService = deleteUserService;
        this.subscribeService = subscribeService;
        this.userProfileUpdateService = userProfileUpdateService;
    }

    @Bean
    @Transactional
    public ApplicationRunner init() {
        return args -> {
            // 사용자 생성 목록 정의
            List<UserRegistDTO> usersToCreate = List.of(
                    new UserRegistDTO(defaultEmail, defaultPassword, defaultUserName, Authority.ROLE_USER),
                    new UserRegistDTO(defaultEmail + "1", defaultPassword, defaultUserName, Authority.ROLE_USER),
                    new UserRegistDTO(defaultEmail + "2", defaultPassword, "User", Authority.ROLE_USER)
            );

            // 사용자 생성 로직
            for (int i = 0; i < usersToCreate.size(); i++) {
                UserRegistDTO dto = usersToCreate.get(i);
                String emailToCheck = dto.getEmail();
                if (userCommandRepository.findByEmail(emailToCheck).isEmpty()) {
                    System.out.println("생성 " + (i + 1));
                    Long userId = userRegistService.registUser(dto);
                    userIds.add(userId);
                }
            }

            // 특정 사용자에 대한 추가 작업
            if (userIds.size() >= 3) {
                Long subscriberId = userIds.get(0); // 예: 첫 번째 사용자
                Long channelId = userIds.get(1);    // 예: 두 번째 사용자
                subscribeService.manageSubscription(new ChannelSubscribeRequestDTO(subscriberId, channelId, true));

                // 프로필 업데이트
                userProfileUpdateService.updateUser(
                        UserUpdateRequestDTO.builder()
                                .userId(subscriberId)
                                .displayName("YY2")
                                .build()
                );
            }

            // 애플리케이션 종료 시 사용자 삭제
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                userIds.forEach(id -> {
                    if (id != null) {
                        deleteUserService.deleteUser(id);
                    }
                });
            }));
        };
    }
}
