package com.outsider.masterofpredictionbackend.config;



import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.DeleteUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.UserRegistService;
import com.outsider.masterofpredictionbackend.user.command.domain.aggregate.embeded.Authority;
import com.outsider.masterofpredictionbackend.user.command.domain.repository.UserCommandRepository;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserDetail;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.CustomUserService;
import com.outsider.masterofpredictionbackend.user.command.infrastructure.service.PrincipalOauthUserService;
import com.outsider.masterofpredictionbackend.util.JwtUtil;
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
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
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
@Profile("dev")
public class DevSecurityConfig {

    private final UserCommandRepository userCommandRepository;
    private final UserRegistService userRegistService;
    private final JwtUtil jwtUtil;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final GetOrFullAuthorizationManager customAuthorizationManager;

    public DevSecurityConfig(UserRegistService userRegistService, JwtUtil jwtUtil, UserCommandRepository userMapper, CustomAccessDeniedHandler accessDeniedHandler, CustomAuthenticationEntryPoint authenticationEntryPoint, GetOrFullAuthorizationManager customAuthorizationManager) {

        this.jwtUtil = jwtUtil;
        this.userRegistService = userRegistService;
        this.userCommandRepository = userMapper;
        this.accessDeniedHandler = accessDeniedHandler;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.customAuthorizationManager = customAuthorizationManager;
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/**").access(customAuthorizationManager)
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable
                ).httpBasic(AbstractHttpConfigurer::disable).exceptionHandling(
                        handler->handler
                .accessDeniedHandler(accessDeniedHandler) // 403 발생 시 커스텀 핸들러 사용
                .authenticationEntryPoint(authenticationEntryPoint) // 401 발생 시 커스텀 핸들러 사용
                )
                .addFilterBefore(new JwtAuthFilter( jwtUtil), UsernamePasswordAuthenticationFilter.class);





        return http.build();
    }
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService( ) {
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
        config.addAllowedOrigin("http://125.132.216.190:3301");
        config.addAllowedOrigin("http://125.132.216.190");
        config.addAllowedOrigin("http://master-of-prediction.shop:3334");
        config.addAllowedOrigin("https://master-of-prediction.shop");
        config.addAllowedOrigin("https://master-of-prediction.shop:3334");
        config.addAllowedOrigin("https://monitor.master-of-prediction.shop:3001");
        config.addAllowedOrigin("https://monitor.master-of-prediction.shop");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }



}
