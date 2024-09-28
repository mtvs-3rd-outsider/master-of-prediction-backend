package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelRegistService;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
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
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.*;
@Configuration
@Order(Integer.MAX_VALUE)
public class DummyConfig {
    private final CategoryChannelRegistService categoryChannelRegistService;
    private final UserRegistService userRegistService;
    private final DeleteUserService deleteUserService;
    private final ChannelSubscribeService subscribeService;
    private final UserProfileUpdateService userProfileUpdateService;
    private final UserCommandRepository userCommandRepository;
    private final MyChannelCommandRepository myChannelCommandRepository;
    private final CategoryChannelRepository categoryChannelRepository;
    private final ChannelSubscribeCommandRepository channelSubscribeCommandRepository;
    private final String defaultEmail = DEFAULT_USER_EMAIL;
    private final String defaultUserName = DEFAULT_USER_NAME;
    private final String defaultPassword = DEFAULT_USER_PASSWORD;
    private final KafkaAdmin kafkaAdmin;
    // 리스트를 사용하여 여러 사용자 ID를 저장
    private final List<Long> userIds = new ArrayList<>();
    private final List<Long> categoryChannelIds = new ArrayList<>();  // Track created category channels
    private final List<Long> myChannelIds = new ArrayList<>();  // Track created category channels

    public DummyConfig(CategoryChannelRegistService categoryChannelRegistService, UserCommandRepository userCommandRepository,
                       UserRegistService userRegistService,
                       DeleteUserService deleteUserService, ChannelSubscribeService subscribeService,
                       UserProfileUpdateService userProfileUpdateService, MyChannelCommandRepository myChannelCommandRepository, CategoryChannelRepository categoryChannelRepository, ChannelSubscribeCommandRepository channelSubscribeCommandRepository, KafkaAdmin kafkaAdmin) {
        this.categoryChannelRegistService = categoryChannelRegistService;
        this.userCommandRepository = userCommandRepository;
        this.userRegistService = userRegistService;
        this.deleteUserService = deleteUserService;
        this.subscribeService = subscribeService;
        this.userProfileUpdateService = userProfileUpdateService;
        this.myChannelCommandRepository = myChannelCommandRepository;
        this.categoryChannelRepository = categoryChannelRepository;
        this.channelSubscribeCommandRepository = channelSubscribeCommandRepository;
        this.kafkaAdmin = kafkaAdmin;
    }
    @Bean()
    @Order(Integer.MAX_VALUE)
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
            // Category Channel creation
            if (userIds.size() >= 3) {
                Long ownerId = userIds.get(0); // Set the first user as the owner of the category channel
                List<CategoryChannelRegistRequestDTO> channelsToCreate = new ArrayList<>();

                // 30개의 더미 채널을 생성
                for (int i = 1; i <= 30; i++) {
                    channelsToCreate.add(new CategoryChannelRegistRequestDTO(
                            "Channel " + i,
                            "Description for channel " + i,
                            "[\"No spamming\", \"Be respectful\"]",
                            CategoryChannelStatus.APPLY
                    ));
                }

                for (int i = 0; i < channelsToCreate.size(); i++) {
                    CategoryChannelRegistRequestDTO channelDto = channelsToCreate.get(i);
                    System.out.println("Creating category channel " + (i + 1));
                    MultipartFile representativeImageFile = null; // You can add dummy files if needed
                    MultipartFile bannerImageFile = null;
                    categoryChannelRegistService.registerCategoryChannelWithManualId(channelDto, representativeImageFile, bannerImageFile, 2L, (long) i + 1);
                    categoryChannelIds.add((long) i + 1); // Track category channel ID
                }
            }
            // 특정 사용자에 대한 추가 작업
            if (userIds.size() >= 3) {
                Long subscriberId = userIds.get(0); // 예: 첫 번째 사용자
                Long channelId = userIds.get(1);    // 예: 두 번째 사용자
//                ChannelSubscribeRequestDTO channelSubscribeRequestDTO= new ChannelSubscribeRequestDTO(subscriberId, channelId, true);
//                MyChannelSubscribeId myChannelSubscribeId =  new MyChannelSubscribeId(channelSubscribeRequestDTO.getUserId(), channelSubscribeRequestDTO.getChannelId(), channelSubscribeRequestDTO.getIsUserChannel());
//                Optional<ChannelSubscribe> existMyChannelSubscribeId =channelSubscribeCommandRepository.findById(myChannelSubscribeId);
//                if(existMyChannelSubscribeId.isPresent())
//                {
//                    channelSubscribeService.deleteById(myChannelSubscribeId.getUserId(),myChannelSubscribeId.getChannelId(),true);
//                }
//                subscribeService.manageSubscription(channelSubscribeRequestDTO,"subscribe");

//                ChannelSubscribeRequestDTO channelSubscribeRequestDTO2= new ChannelSubscribeRequestDTO(subscriberId, 1L, false);
//                MyChannelSubscribeId myChannelSubscribeId2 =  new MyChannelSubscribeId(channelSubscribeRequestDTO2.getUserId(), channelSubscribeRequestDTO2.getChannelId(), channelSubscribeRequestDTO2.getIsUserChannel());
//                Optional<ChannelSubscribe> existMyChannelSubscribeId2 =channelSubscribeCommandRepository.findById(myChannelSubscribeId2);
//                if(existMyChannelSubscribeId2.isPresent())
//                {
//                    channelSubscribeService.deleteById(myChannelSubscribeId2.getUserId(),myChannelSubscribeId2.getChannelId(),false);
//                }
//                subscribeService.manageSubscription(channelSubscribeRequestDTO2,"subscribe");

//                // 프로필 업데이트
//                userProfileUpdateService.updateUser(
//                        UserUpdateRequestDTO.builder()
//                                .userId(subscriberId)
//                                .displayName("YY2")
//                                .userName("YY3")
//                                .build()
//                );
            }

            Pageable pageable = PageRequest.of(0, 10);

            // 애플리케이션 종료 시 사용자 삭제
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                userIds.forEach(id -> {
                    if (id != null) {
                        userCommandRepository.deleteById(id);
                        myChannelCommandRepository.deleteById(id);
                        // Hard delete on shutdown
                    }
                });
                categoryChannelIds.forEach(id -> {
                    if (id != null) {
                        categoryChannelRepository.deleteById(id); // Hard delete category channels on shutdown
                    }
                });
                deleteKafkaTopics(adminClient, "dbserver1.*");
                adminClient.close();
            }));
        };
    }
    private void deleteKafkaTopics(AdminClient adminClient, String topicPattern) {
        try {
            // 1. 사용자 생성 토픽만 가져오기
            ListTopicsOptions options = new ListTopicsOptions();
            options.listInternal(false); // Only user-created topics
            ListTopicsResult topicsResult = adminClient.listTopics();

            Set<String> topics = topicsResult.names().get();
            Pattern pattern = Pattern.compile(topicPattern);

            // 2. "dbserver1"로 시작하는 토픽 찾기
            Set<String> topicsToDelete = new HashSet<>();
            for (String topic : topics) {
                if (pattern.matcher(topic).matches()) {
                    topicsToDelete.add(topic);
                }
            }

            if (!topicsToDelete.isEmpty()) {
                // 3. 토픽 삭제
                DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(topicsToDelete);
                deleteTopicsResult.all().get(); // Wait for deletion to complete
                System.out.println("Deleted topics: " + topicsToDelete);

                // 4. 토픽과 관련된 모든 Consumer 그룹의 오프셋 및 메타데이터 삭제
                resetAllConsumerGroupOffsets(adminClient, topicsToDelete);
            } else {
                System.out.println("No topics matched the pattern: " + topicPattern);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void resetAllConsumerGroupOffsets(AdminClient adminClient, Set<String> topicsToDelete) {
        try {
            // 1. 모든 Consumer 그룹 가져오기
            ListConsumerGroupsResult consumerGroupsResult = adminClient.listConsumerGroups();
            Collection<ConsumerGroupListing> consumerGroups = consumerGroupsResult.all().get();

            for (ConsumerGroupListing groupListing : consumerGroups) {
                String groupId = groupListing.groupId();

                // 2. 각 Consumer 그룹에 대한 오프셋 정보 가져오기
                for (String topic : topicsToDelete) {
                    // 토픽 파티션 정보를 가져옴
                    ListConsumerGroupOffsetsOptions options = new ListConsumerGroupOffsetsOptions();
                    ListConsumerGroupOffsetsResult consumerGroupOffsets = adminClient.listConsumerGroupOffsets(groupId, options);

                    Map<TopicPartition, OffsetAndMetadata> offsets = consumerGroupOffsets.partitionsToOffsetAndMetadata().get();

                    // 3. 특정 토픽의 오프셋을 초기화
                    Map<TopicPartition, OffsetAndMetadata> offsetsToReset = new HashMap<>();
                    for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : offsets.entrySet()) {
                        TopicPartition topicPartition = entry.getKey();
                        if (topicPartition.topic().equals(topic)) {
                            // 오프셋을 0으로 설정하거나 원하는 값으로 재설정
                            offsetsToReset.put(topicPartition, new OffsetAndMetadata(0L));
                        }
                    }

                    if (!offsetsToReset.isEmpty()) {
                        // 4. 오프셋 재설정 실행
                        adminClient.alterConsumerGroupOffsets(groupId, offsetsToReset).all().get();
                        System.out.println("Resetting offsets for group: " + groupId + ", topic: " + topic);
                    }
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
