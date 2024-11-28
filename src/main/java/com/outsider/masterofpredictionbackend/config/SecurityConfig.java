package com.outsider.masterofpredictionbackend.config;

import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.OAuth2SuccessHandler;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.PrincipalOauthUserService;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final JwtUtil jwtUtil;
    private final UserCommandRepository userCommandRepository;
    private final GetOrFullAuthorizationManager customAuthorizationManager;
    private final UserRegistService userRegistService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;
    private final Environment environment;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));

        // prod 프로필에서만 HTTPS 요구
        if (isProdProfile()) {
            http.requiresChannel(channel -> channel
                    .anyRequest().requiresSecure()
            );
        }

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/v1/betting-products").permitAll()
                        .requestMatchers(permittedEndpoints())
                        .permitAll()
                        .requestMatchers("/**").access(customAuthorizationManager)
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(handler -> handler
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(authenticationEntryPoint)
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
        final PrincipalOauthUserService delegate = new PrincipalOauthUserService(
                passwordEncoder(),
                userRegistService,
                userCommandRepository);
        return userRequest -> (CustomUserDetail) delegate.loadUser(userRequest);
    }

    private boolean isProdProfile() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("prod".equals(profile)) {
                return true;
            }
        }
        return false;
    }

    private static String[] permittedEndpoints() {
        return new String[]{
                "/api/v1/auth/login",
                "/api/v1/auth/admin/login",
                "/api/v1/auth/register",
                "/api/v1/auth/signup/email",
                "/api/v1/auth/signup/emailAuth",
                "/api/v1/auth/forgot-password",
                "/api/v1/auth/reset-password",
                "/api/v1/feeds",
                "/api/v1/feeds/{feedId}/**",
                "/api/v1/feeds/**",
                "/api/v1/betting-products/sse",
                "/api/v1/betting-products"
        };
    }
}