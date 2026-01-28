package com.rideongo.api_gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {
    
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Allow specific origins (REQUIRED when using credentials)
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:*"));
        // OR be explicit:
        // config.setAllowedOrigins(Arrays.asList(
        //     "http://localhost:3000",
        //     "http://localhost:4200",
        //     "http://localhost:5173"
        // ));
        
        // CRITICAL: Cannot use "*" with allowCredentials(true)
        config.setAllowedMethods(Arrays.asList(
            "GET", 
            "POST", 
            "PUT", 
            "DELETE", 
            "OPTIONS", 
            "PATCH"
        ));
        
        // CRITICAL: Cannot use "*" with allowCredentials(true)
        config.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-Requested-With",
            "X-User-Id",
            "X-User-Email",
            "X-Role"
        ));
        
        // Allow credentials
        config.setAllowCredentials(true);
        
        // Max age for preflight
        config.setMaxAge(3600L);
        
        // Expose headers that frontend can read
        config.setExposedHeaders(Arrays.asList(
            "Authorization",
            "X-User-Id",
            "X-User-Email",
            "X-Role"
        ));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsWebFilter(source);
    }
}