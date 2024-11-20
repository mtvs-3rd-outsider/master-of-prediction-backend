package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.categorychannel.command.application.dto.CategoryChannelRegistRequestDTO;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelApprovalService;
import com.outsider.masterofpredictionbackend.categorychannel.command.application.service.CategoryChannelRegistService;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.aggregate.enumtype.CategoryChannelStatus;
import com.outsider.masterofpredictionbackend.categorychannel.command.domain.repository.CategoryChannelRepository;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.FeedCreateDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.dto.UserDTO;
import com.outsider.masterofpredictionbackend.feed.command.application.service.FeedCreateService;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.embedded.Channel;
import com.outsider.masterofpredictionbackend.feed.command.domain.aggregate.enumtype.ChannelType;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Order(200)
public class CategoryChannelConfig {

    private final CategoryChannelRegistService categoryChannelRegistService;
    private final CategoryChannelRepository categoryChannelRepository;
    private final CategoryChannelApprovalService categoryChannelApprovalService;
    private final FeedCreateService feedService;
    @Autowired
    public CategoryChannelConfig(CategoryChannelRegistService categoryChannelRegistService,
                                 CategoryChannelRepository categoryChannelRepository,
                                 CategoryChannelApprovalService categoryChannelApprovalService, FeedCreateService feedService) {
        this.categoryChannelRegistService = categoryChannelRegistService;
        this.categoryChannelRepository = categoryChannelRepository;
        this.categoryChannelApprovalService = categoryChannelApprovalService;
        this.feedService = feedService;
    }

    @Bean
    public CommandLineRunner createAndApproveCategoryChannels() {
        return args -> {
            List<String> predefinedCategories = List.of("정치", "전쟁", "경제", "스포츠");

            for (int i = 0; i < predefinedCategories.size(); i++) {
                String categoryName = predefinedCategories.get(i);

                boolean exists = categoryChannelRepository.existsByDisplayName(categoryName);
                if (exists) {
                    System.out.println("카테고리 채널 '" + categoryName + "'이(가) 이미 존재합니다. 생성하지 않습니다.");
                    continue;
                }

                CategoryChannelRegistRequestDTO channelDto = new CategoryChannelRegistRequestDTO(
                        categoryName,
                        "Description for " + categoryName,
                        "[\"No spamming\", \"Be respectful\"]",
                        CategoryChannelStatus.APPLY
                );

                System.out.println("Creating category channel: " + categoryName);

                MultipartFile representativeImageFile = null;
                MultipartFile bannerImageFile = null;

                Long ownerId = 1L;

                Long channelId = categoryChannelRegistService.registerCategoryChannelWithManualId(
                        channelDto, representativeImageFile, bannerImageFile, ownerId, (long) (i + 1)
                );

                    categoryChannelApprovalService.changeCategoryChannelStatus(channelId, CategoryChannelStatus.APPROVED);
                    System.out.println("Approved category channel: " + categoryName);
                // 게시글 생성
                createFeedForChannel(channelId, categoryName);
            }
        };
    }
    private void createFeedForChannel(Long channelId, String categoryName) throws Exception {
        FeedCreateDTO feedCreateDTO = new FeedCreateDTO();
        feedCreateDTO.setTitle("Welcome to " + categoryName + " Channel");
        feedCreateDTO.setContent("This is a sample post for the " + categoryName + " channel.");

        // UserDTO 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUserName("admin");
        userDTO.setDisplayName("Admin User");
        userDTO.setPoints(BigDecimal.valueOf(1000));
        userDTO.setAuthority(Authority.ROLE_USER);
        feedCreateDTO.setUser(userDTO);

        // Channel 설정
        Channel channel = new Channel(channelId, ChannelType.CATEGORYCHANNEL);
        feedCreateDTO.setChannel(channel);

        // 파일과 유튜브 링크 설정
        List<MultipartFile> files = new ArrayList<>(); // 업로드할 파일 리스트, 더미 파일일 경우 빈 리스트
        List<String> youtubeUrls = List.of("https://www.youtube.com/watch?v=sample_video");

        Long feedId = feedService.createFeed(feedCreateDTO, files, youtubeUrls);
        System.out.println("Created feed with ID: " + feedId + " for category channel: " + categoryName);
    }
}
