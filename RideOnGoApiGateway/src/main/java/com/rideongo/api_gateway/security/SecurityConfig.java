package com.rideongo.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration 
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomJwtFilter customJwtFilter;
    
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
        		 .csrf(ServerHttpSecurity.CsrfSpec::disable)
                 
            
            // Disable Basic Auth
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            
            // Disable Form Login
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
            
            // Custom exception handling
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((exchange, e) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                })
                .accessDeniedHandler((exchange, denied) -> {
                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                })
            )
            
            // Authorization rules
            .authorizeExchange(auth -> auth
                // CRITICAL: Allow ALL OPTIONS requests (CORS preflight)
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                
                // Public endpoints
                .pathMatchers(HttpMethod.POST, "/users/signin").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/signup").permitAll()
                .pathMatchers(HttpMethod.POST, "/users/register").permitAll()
                
                // Admin-only endpoints
                .pathMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .pathMatchers(HttpMethod.POST, "/users/admin/signin").permitAll()
                .pathMatchers(HttpMethod.GET,"/bms/**").hasRole("ADMIN")
                
                // Doctor-only endpoints
                .pathMatchers(HttpMethod.POST, "/appointments/mark-complete-with-tests")
                    .hasRole("DOCTOR")
                
                // All other requests need authentication
                .anyExchange().authenticated()
            )
            
            // Add JWT filter
            .addFilterAt(customJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            
            .build();
    }
}