package com.rideongo.api_gateway.security;

import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomJwtFilter implements WebFilter {
    private final JwtUtils jwtUtils;
    
    private static final List<String> PUBLIC_PATHS = List.of(
        "/users/signin",
        "/users/signup",
        "/users/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        HttpMethod method = exchange.getRequest().getMethod();
        
        log.info("==> CustomJwtFilter - Method: {}, Path: {}", method, path);
        
        // Skip OPTIONS requests (CORS preflight)
        if (HttpMethod.OPTIONS.equals(method)) {
            log.info("==> Skipping OPTIONS request");
            return chain.filter(exchange);
        }
        
        // Skip public endpoints
        if (isPublicPath(path)) {
            log.info("==> Skipping public path: {}", path);
            return chain.filter(exchange);
        }
        
        // Extract JWT token
        String authHeader = exchange.getRequest()
            .getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("==> No Authorization header found for protected path: {}", path);
            return chain.filter(exchange);
        }
        
        String token = authHeader.substring(7);
        
        try {
            log.info("==> Validating JWT token");
            Claims claims = jwtUtils.validateToken(token);
            
            String email = claims.getSubject();
            String role = claims.get("user_role", String.class);
            String userId = claims.get("user_id", String.class);
            
            log.info("==> JWT Valid - UserId: {}, Email: {}, Role: {}", userId, email, role);
            
            // Add custom headers for downstream services
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header("X-User-Id", userId)
                .header("X-User-Email", email)
                .header("X-Role", role)
                .build();
            
            ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();
            
            // Create Spring Security authentication
            List<GrantedAuthority> authorities = 
                List.of(new SimpleGrantedAuthority(role));
            
            Authentication auth = new UsernamePasswordAuthenticationToken(
                email, null, authorities
            );
            
            log.info("==> Authentication created: {}", auth);
            
            return chain.filter(mutatedExchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
                
        } catch (Exception e) {
            log.error("==> JWT validation failed: {}", e.getMessage());
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
    
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::contains);
    }
}