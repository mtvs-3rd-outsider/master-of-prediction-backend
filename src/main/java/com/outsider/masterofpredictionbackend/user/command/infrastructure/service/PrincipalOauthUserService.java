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

@Service
@Transactional
public class PrincipalOauthUserService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserCommandRepository userCommandRepository;
    @Autowired
    public PrincipalOauthUserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserCommandRepository userCommandRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userCommandRepository = userCommandRepository;
    }
    public void CheckWithdrawalStatus(Optional<User> user) {
        if (user.isEmpty()){
            return;
        }
        if(user.get().getWithdrawal())
        {
            user.get().setWithdrawal(false);
            userCommandRepository.updateWithdrawalStatusByUser(user.get());
        }
    }
    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        //"registraionId" 로 어떤 OAuth 로 로그인 했는지 확인 가능(google,naver등)
         //구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인 완료 -> code를 리턴(OAuth-Clien라이브러리가 받아줌) -> code를 통해서 AcssToken요청(access토큰 받음)
        // => "userRequest"가 감고 있는 정보
        //회원 프로필을 받아야하는데 여기서 사용되는것이 "loadUser" 함수이다 -> 구글 로 부터 회원 프로필을 받을수 있다.


        /**
         *  OAuth 로그인 회원 가입
         */
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo =null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        }
        else{
            System.out.println("지원하지 않은 로그인 서비스 입니다.");
        }

        String provider = oAuth2UserInfo.getProvider(); //google , naver, facebook etc
        String providerId = oAuth2UserInfo.getProviderId();
        String providerWithId = provider + "_" + providerId;
        String username = oAuth2UserInfo.getName();
        String password =  bCryptPasswordEncoder.encode(UUID.randomUUID().toString()); //중요하지 않음 그냥 패스워드 암호화 하
        String email = oAuth2UserInfo.getEmail();
        String pictureUrl = oAuth2UserInfo.getPictureUrl();

        Optional<User> user = userCommandRepository.findByEmail(email);

        CheckWithdrawalStatus(user);
        //처음 서비스를 이용한 회원일 경우
        if(user.isEmpty()) {
            User existUser = userCommandRepository.save(new User(email,password,username,Authority.ROLE_USER, new ProviderInfo(provider,providerId)));

            if(!pictureUrl.isEmpty())
            {
                existUser.setUserImg(pictureUrl);
            }
            return  new CustomUserDetail(existUser, oAuth2User.getAttributes());

        }
        else
        {
            return user.map(value -> new CustomUserDetail(value, oAuth2User.getAttributes())).orElse(null);

        }


    }
}