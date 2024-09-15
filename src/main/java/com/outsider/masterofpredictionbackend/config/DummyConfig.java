package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.dto.ChannelSubscribeRequestDTO;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.application.service.ChannelSubscribeService;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.ChannelSubscribe;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.aggregate.embeded.MyChannelSubscribeId;
import com.outsider.masterofpredictionbackend.channelsubscribe.command.domain.repository.ChannelSubscribeCommandRepository;
import com.outsider.masterofpredictionbackend.mychannel.command.domain.repository.MyChannelCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.dto.UserUpdateRequestDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.DeleteUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserProfileUpdateService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsOptions;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.*;

@Configuration
public class DummyConfig {

    private final UserRegistService userRegistService;
    private final DeleteUserService deleteUserService;
    private final ChannelSubscribeService subscribeService;
    private final UserProfileUpdateService userProfileUpdateService;
    private final UserCommandRepository userCommandRepository;
    private final MyChannelCommandRepository myChannelCommandRepository;
    private final ChannelSubscribeCommandRepository channelSubscribeCommandRepository;
    private final String defaultEmail = DEFAULT_USER_EMAIL;
    private final String defaultUserName = DEFAULT_USER_NAME;
    private final String defaultPassword = DEFAULT_USER_PASSWORD;
    private final KafkaAdmin kafkaAdmin;
    // 리스트를 사용하여 여러 사용자 ID를 저장
    private final List<Long> userIds = new ArrayList<>();

    public DummyConfig(UserCommandRepository userCommandRepository,
                       UserRegistService userRegistService,
                       DeleteUserService deleteUserService, ChannelSubscribeService subscribeService,
                       UserProfileUpdateService userProfileUpdateService, MyChannelCommandRepository myChannelCommandRepository, ChannelSubscribeCommandRepository channelSubscribeCommandRepository, KafkaAdmin kafkaAdmin) {
        this.userCommandRepository = userCommandRepository;
        this.userRegistService = userRegistService;
        this.deleteUserService = deleteUserService;
        this.subscribeService = subscribeService;
        this.userProfileUpdateService = userProfileUpdateService;
        this.myChannelCommandRepository = myChannelCommandRepository;
        this.channelSubscribeCommandRepository = channelSubscribeCommandRepository;
        this.kafkaAdmin = kafkaAdmin;
    }
    @Bean
    @Transactional
    public CommandLineRunner init(ChannelSubscribeService channelSubscribeService) {
        return args -> {
            AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());

            // Delete Kafka topics that start with "dbserver1"
            // 사용자 생성 목록 정의
            List<UserRegistDTO> usersToCreate = List.of(
                    new UserRegistDTO(defaultEmail, defaultPassword, defaultUserName, Authority.ROLE_USER),
                    new UserRegistDTO(defaultEmail + "1", defaultPassword, defaultUserName, Authority.ROLE_USER),
                    new UserRegistDTO(defaultEmail + "2", defaultPassword, "User", Authority.ROLE_USER)
            );

            // 사용자 생성 전에 기존 사용자 삭제
            for (UserRegistDTO dto : usersToCreate) {
                String emailToCheck = dto.getEmail();
                userCommandRepository.findByEmail(emailToCheck).ifPresent(existingUser -> {
                    System.out.println("기존 사용자 삭제: " + existingUser.getId());
                    userCommandRepository.deleteById(existingUser.getId()); // Hard delete
                    myChannelCommandRepository.deleteById(existingUser.getId());
                });
            }

            // 사용자 생성 로직
            for (int i = 0; i < usersToCreate.size(); i++) {
                UserRegistDTO dto = usersToCreate.get(i);
                System.out.println("생성 " + (i + 1));
                Long userId = userRegistService.registManualIdUser(dto, (long) i+1);
                userIds.add(userId);
            }

            // 특정 사용자에 대한 추가 작업
            if (userIds.size() >= 3) {
                Long subscriberId = userIds.get(0); // 예: 첫 번째 사용자
                Long channelId = userIds.get(1);    // 예: 두 번째 사용자
                ChannelSubscribeRequestDTO channelSubscribeRequestDTO= new ChannelSubscribeRequestDTO(subscriberId, channelId, true);
                MyChannelSubscribeId myChannelSubscribeId =  new MyChannelSubscribeId(channelSubscribeRequestDTO.getUserId(), channelSubscribeRequestDTO.getChannelId(), channelSubscribeRequestDTO.getIsUserChannel());
                Optional<ChannelSubscribe> existMyChannelSubscribeId =channelSubscribeCommandRepository.findById(myChannelSubscribeId);
                if(existMyChannelSubscribeId.isPresent())
                {
                    channelSubscribeService.deleteById(myChannelSubscribeId.getUserId(),myChannelSubscribeId.getChannelId(),true);
                }
                subscribeService.manageSubscription(channelSubscribeRequestDTO,"subscribe");



                // 프로필 업데이트
                userProfileUpdateService.updateUser(
                        UserUpdateRequestDTO.builder()
                                .userId(subscriberId)
                                .displayName("YY2")
                                .userName("YY2")
                                .build()
                );
            }

            Pageable pageable = PageRequest.of(0, 10);

            // 애플리케이션 종료 시 사용자 삭제
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                userIds.forEach(id -> {
                    if (id != null) {
                        userCommandRepository.deleteById(id); // Hard delete on shutdown
                    }
                });

                deleteKafkaTopics(adminClient, "dbserver1.*");
                adminClient.close();
            }));
        };
    }
    private void deleteKafkaTopics(AdminClient adminClient, String topicPattern) {
        try {
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false); // Only user-created topics
            ListTopicsResult topicsResult = adminClient.listTopics(options);

            Set<String> topics = topicsResult.names().get();
            Pattern pattern = Pattern.compile(topicPattern);

            // Find topics starting with "dbserver1"
            Set<String> topicsToDelete = new HashSet<>();
            for (String topic : topics) {
                if (pattern.matcher(topic).matches()) {
                    topicsToDelete.add(topic);
                }
            }

            if (!topicsToDelete.isEmpty()) {
                DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(topicsToDelete);
                deleteTopicsResult.all().get(); // Wait for deletion to complete
                System.out.println("Deleted topics: " + topicsToDelete);
            } else {
                System.out.println("No topics matched the pattern: " + topicPattern);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
