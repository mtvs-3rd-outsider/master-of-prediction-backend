package com.outsider.masterofpredictionbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        
        // 모든 출처 허용 (개발 환경)
        config.setAllowedOriginPatterns(Arrays.asList(
            "http://*:3000", "https://*:3000",
            "http://*:3001", "https://*:3001",
            "http://*.master-of-prediction.shop", "https://*.master-of-prediction.shop",
            "http://localhost:[*]", "https://localhost:[*]",
            "https://*.google.com",
            "https://lh3.googleusercontent.com",
            "https://*.vercel.app"
        ));
        
        config.setAllowCredentials(true);
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setExposedHeaders(Arrays.asList("*"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return source;
    }
} 