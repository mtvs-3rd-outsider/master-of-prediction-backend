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
                        getDescriptionByCategory(categoryName), // 주제별 설명
                        getRulesByCategory(categoryName),      // 주제별 규칙
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
    private String getRulesByCategory(String categoryName) {
        switch (categoryName) {
            case "정치":
                return "[\"상대방을 존중하세요\", \"근거 있는 주장을 펼치세요\", \"허위 정보를 퍼뜨리지 마세요\"]";
            case "전쟁":
                return "[\"감정적인 표현을 자제하세요\", \"민감한 주제에 대해 존중하며 토론하세요\", \"사실에 기반한 정보를 공유하세요\"]";
            case "경제":
                return "[\"스팸 게시물을 금지합니다\", \"허위 투자 정보를 공유하지 마세요\", \"모두가 이해할 수 있는 표현을 사용하세요\"]";
            case "스포츠":
                return "[\"선수나 팀에 대한 비방을 금지합니다\", \"건전한 스포츠 정신을 유지하세요\", \"과도한 상업적 홍보를 삼가세요\"]";
            default:
                return "[\"모든 구성원을 존중하세요\", \"유익한 정보를 공유하세요\"]";
        }
    }

    private String getDescriptionByCategory(String categoryName) {
        switch (categoryName) {
            case "정치":
                return "정치 채널입니다. 대선, 정책, 사회적 이슈 등 다양한 정치적 주제를 토론하고 의견을 나눌 수 있습니다.";
            case "전쟁":
                return "전쟁 채널입니다. 세계 각국의 분쟁, 군사적 갈등, 국제 관계 등에 대해 논의하는 공간입니다.";
            case "경제":
                return "경제 채널입니다. 글로벌 경제 동향, 금융 정책, 투자와 관련된 정보를 공유하고 이야기할 수 있습니다.";
            case "스포츠":
                return "스포츠 채널입니다. 다양한 스포츠 경기, 선수들, 대회 소식을 공유하며 팬들과 소통하는 공간입니다.";
            default:
                return "카테고리 채널입니다. 해당 주제에 대한 다양한 의견과 정보를 나눌 수 있는 공간입니다.";
        }
    }

    private void createFeedForChannel(Long channelId, String categoryName) throws Exception {
        FeedCreateDTO feedCreateDTO = new FeedCreateDTO();

        // 주제별 제목 및 내용 설정
        String title = getFeedTitleByCategory(categoryName);
        String content = getFeedContentByCategory(categoryName);

        feedCreateDTO.setTitle(title);
        feedCreateDTO.setContent(content);

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

        // 파일 리스트와 유튜브 링크는 비워둠 (사용하지 않음)
        List<MultipartFile> files = new ArrayList<>();
        List<String> youtubeUrls = new ArrayList<>();

        Long feedId = feedService.createFeed(feedCreateDTO, files, youtubeUrls);
        System.out.println("Created feed with ID: " + feedId + " for category channel: " + categoryName);
    }
    private String getFeedTitleByCategory(String categoryName) {
        switch (categoryName) {
            case "정치":
                return "2024 대선을 앞둔 주요 이슈 정리";
            case "전쟁":
                return "러시아-우크라이나 분쟁: 최신 상황과 분석";
            case "경제":
                return "세계 경제 위기: 금리 인상과 인플레이션의 영향";
            case "스포츠":
                return "2024 파리 올림픽: 주목해야 할 선수와 경기";
            default:
                return "Welcome to " + categoryName + " Channel";
        }
    }
    private String getFeedContentByCategory(String categoryName) {
        switch (categoryName) {
            case "정치":
                return "2024년 대선을 앞두고 주요 후보들의 공약과 정책을 분석해보았습니다. " +
                        "당신은 어떤 후보를 지지하시나요? 의견을 남겨주세요!";
            case "전쟁":
                return "러시아와 우크라이나 간의 분쟁이 여전히 지속되고 있습니다. " +
                        "전쟁이 가져오는 국제적 영향과 향후 시나리오를 함께 이야기해보세요.";
            case "경제":
                return "금리 인상과 인플레이션으로 인해 세계 경제가 위기를 맞고 있습니다. " +
                        "각국의 대응 방안과 한국 경제에 미치는 영향을 다뤄봅니다.";
            case "스포츠":
                return "2024년 파리 올림픽에서는 어떤 종목이 가장 흥미로울까요? " +
                        "주목할 선수들과 경기 일정을 공유합니다.";
            default:
                return "This is a sample post for the " + categoryName + " channel.";
        }
    }

}
