package com.outsider.masterofpredictionbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
            "https://*.google.com",
            "https://lh3.googleusercontent.com",
            "http://localhost:3000",
            "https://localhost:3000",
            "http://localhost:3001",
            "https://localhost:3001",
            "http://192.168.0.38:3000",
            "https://192.168.0.38:3000",
            "http://192.168.0.38:3001",
            "https://192.168.0.38:3001",
            "http://monitor.master-of-prediction.shop",
            "https://monitor.master-of-prediction.shop",
            "http://monitor.master-of-prediction.shop:3001",
            "https://monitor.master-of-prediction.shop:3001",
            "http://master-of-prediction.shop",
            "https://master-of-prediction.shop",
            "http://*.master-of-prediction.shop",
            "https://*.master-of-prediction.shop",
            "http://app.master-of-prediction.shop",
            "https://app.master-of-prediction.shop",
            "http://admin.master-of-prediction.shop",
            "https://admin.master-of-prediction.shop",
            "http://master-of-prediction.shop:3334",
            "https://master-of-prediction.shop:3334",
            "http://master-of-prediction-frontend-psxd.vercel.app",
            "https://master-of-prediction-frontend-psxd.vercel.app",
            "http://master-of-prediction-frontend.vercel.app",
            "https://master-of-prediction-frontend.vercel.app"
    );

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        config.setAllowCredentials(true);
        ALLOWED_ORIGINS.forEach(config::addAllowedOrigin);
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("*")); 
        config.setExposedHeaders(List.of("*"));
        
        config.setMaxAge(3600L);
        
        source.registerCorsConfiguration("/**", config);
        return source;
    }
} 