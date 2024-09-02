package com.outsider.masterofpredictionbackend.config;



import com.outsider.masterofpredictionbackend.user.command.application.dto.UserRegistDTO;
import com.outsider.masterofpredictionbackend.user.command.application.service.DeleteUserService;
import com.outsider.masterofpredictionbackend.user.command.application.service.RegistUserService;
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
@Profile("dev")
public class DevSecurityConfig {
    private final RegistUserService registUserService;
    private final DeleteUserService deleteUserService;
    private Long id ;

    private final String email= DEFAULT_USER_EMAIL;
    private final String userName= DEFAULT_USER_NAME;
    private final String password = DEFAULT_USER_PASSWORD;
    private final JwtUtil jwtUtil;
    private final CustomUserService customUserService;
    UserCommandRepository userCommandRepository;

    public DevSecurityConfig(RegistUserService registUserService, DeleteUserService deleteUserService, JwtUtil jwtUtil, CustomUserService customUserService, UserCommandRepository userMapper) {
        this.registUserService = registUserService;
        this.deleteUserService = deleteUserService;
        this.jwtUtil = jwtUtil;
        this.customUserService = customUserService;
        this.userCommandRepository = userMapper;
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
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable
                ).httpBasic(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtAuthFilter( jwtUtil), UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService( ) {
        final PrincipalOauthUserService delegate = new PrincipalOauthUserService(passwordEncoder(), userCommandRepository);
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
        config.addAllowedOrigin("https://monitor.master-of-prediction.shop:3001");
        config.addAllowedOrigin("https://monitor.master-of-prediction.shop");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("*"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }


    @Bean
    @Transactional
    public ApplicationRunner init() {
        return args -> {
            if (userCommandRepository.findByEmail(email).isEmpty()) {

                UserRegistDTO dto = new UserRegistDTO(
                        email,
                        password,
                        userName,
                        Authority.ROLE_USER
                );

                // when
                id= registUserService.registUser(dto);


            }
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                deleteUserService.deleteUser(id);
            }));
        };
    }
}
