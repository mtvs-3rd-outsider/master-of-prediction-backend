package com.outsider.masterofpredictionbackend.user.command.infrastructure.service;



import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.ProviderInfo;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.domain.service.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.User;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.ProviderInfo;
import com.outsider.masterofpredictionbackend.user.command.domain.service.OAuth2UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRegistService userRegistService; // UserRegistService를 주입받음
    private final UserCommandRepository userCommandRepository;

    @Autowired
    public PrincipalOauthUserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRegistService userRegistService, UserCommandRepository userCommandRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRegistService = userRegistService;
        this.userCommandRepository = userCommandRepository;
    }

    public void checkWithdrawalStatus(Optional<User> user) {
        if (user.isEmpty()) {
            return;
        }
        if (user.get().getWithdrawal()) {
            user.get().setWithdrawal(false);
            userCommandRepository.updateWithdrawalStatusByUser(user.get());
        }
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // OAuth2UserRequest에서 프로필 정보를 가져옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else {
            System.out.println("지원하지 않은 로그인 서비스 입니다.");
        }

        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String providerWithId = provider + "_" + providerId;
        String username = oAuth2UserInfo.getName();
        String password = bCryptPasswordEncoder.encode(UUID.randomUUID().toString());
        String email = oAuth2UserInfo.getEmail();
        String pictureUrl = oAuth2UserInfo.getPictureUrl();

        Optional<User> user = userCommandRepository.findByEmail(email);

        checkWithdrawalStatus(user);

        // 신규 회원일 경우 UserRegistService를 통해 회원 등록
        if (user.isEmpty()) {
            // UserRegistDTO를 사용하여 사용자 등록
            UserRegistDTO userRegistDTO = new UserRegistDTO(email, password, username, Authority.ROLE_USER, new ProviderInfo(provider, providerId));
            Long newUserId = userRegistService.registUser(userRegistDTO);

            Optional<User> newUser = userCommandRepository.findById(newUserId);
            newUser.ifPresent(u -> {
                if (!pictureUrl.isEmpty()) {
                    u.setUserImg(pictureUrl);
                }
            });

            return newUser.map(value -> new CustomUserDetail(value, oAuth2User.getAttributes())).orElse(null);
        }

        // 기존 회원일 경우 CustomUserDetail로 반환
        return user.map(value -> new CustomUserDetail(value, oAuth2User.getAttributes())).orElse(null);
    }
}
