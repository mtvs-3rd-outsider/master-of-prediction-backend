package com.outsider.masterofpredictionbackend.config;


import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.DeleteUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserService;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.OAuth2SuccessHandler;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.PrincipalOauthUserService;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

import static com.outsider.masterofpredictionbackend.common.constant.StringConstants.*;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class SecurityConfig {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    @Value("${google.redirect.uri}")
    private String redirectUri;
    @Value("${google.redirect.successfulUri}")
    private String successfulUri;
    private final JwtUtil jwtUtil;
    private final UserCommandRepository userCommandRepository;
    private final GetOrFullAuthorizationManager customAuthorizationManager;
    private final UserRegistService userRegistService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    public SecurityConfig(UserRegistService userRegistService, OAuth2SuccessHandler oAuth2SuccessHandler, JwtUtil jwtUtil, UserCommandRepository userMapper, GetOrFullAuthorizationManager customAuthorizationManager, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.userRegistService = userRegistService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
        this.jwtUtil = jwtUtil;
        this.userCommandRepository = userMapper;
        this.customAuthorizationManager = customAuthorizationManager;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults()).
                requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
//                        .requestMatchers("/**").access(customAuthorizationManager)
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable
                ).
                httpBasic(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable).exceptionHandling(
                        handler->handler
                                .accessDeniedHandler(accessDeniedHandler) // 403 발생 시 커스텀 핸들러 사용
                                .authenticationEntryPoint(authenticationEntryPoint) // 401 발생 시 커스텀 핸들러 사용
                )
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oauth2 -> oauth2

                        .authorizationEndpoint(authorization -> authorization
                                .baseUri("/oauth2/authorization"))
                        .redirectionEndpoint(redirection -> redirection
                                .baseUri("/login/oauth2/code/*"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(this.oauth2UserService()))
                        .successHandler(oAuth2SuccessHandler)
                );


        return http.build();
    }

    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        final PrincipalOauthUserService delegate = new PrincipalOauthUserService(passwordEncoder(), userRegistService,userCommandRepository);
        return (userRequest) -> {

            // Delegate to the default implementation for loading a user
            return (CustomUserDetail) delegate.loadUser(userRequest);
        };

    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("https://*.google.com");
        config.addAllowedOrigin("https://lh3.googleusercontent.com");
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://localhost:3000");
        config.addAllowedOrigin("https://monitor.master-of-prediction.shop:3001");
        config.addAllowedOrigin("https://monitor.master-of-prediction.shop");
        config.addAllowedOrigin("https://master-of-prediction.shop");
        config.addAllowedOrigin("https://master-of-prediction.shop");
        config.addAllowedOrigin("https://*.master-of-prediction.shop");
        config.addAllowedOrigin("https://app.master-of-prediction.shop");
        config.addAllowedOrigin("https://master-of-prediction.shop:3334");
        config.addAllowedOrigin("https://master-of-prediction-frontend-psxd.vercel.app/");
        config.addAllowedOrigin("https://master-of-prediction-frontend.vercel.app");
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return ClientRegistration.withRegistrationId("google")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .redirectUri("https://master-of-prediction.shop:8081/login/oauth2/code/google")
                .scope("profile", "email")
                .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                .tokenUri("https://www.googleapis.com/oauth2/v4/token")
                .userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo")
                .userNameAttributeName(IdTokenClaimNames.SUB)
                .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                .clientName("Google")
                .build();
    }

}
