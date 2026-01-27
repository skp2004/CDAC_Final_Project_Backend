package com.rideongo.api_gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;
//import static org.springframework.security.config.web.server.ServerHttpSecurity.

//Java configuration class
@Configuration 
/*
 * Enables Spring Security WebFlux support to a Configuration class.
 * Can then create here ServerHttpSecurity Bean instance (equivalent to HttpSecurity) 
 * Enables reactive Spring Security 
 */

@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private final CustomJwtFilter customJwtFilter;

    @Bean
     SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http
            ) {

        return 
        		//Disable CSRF protection (stateless authentication)
        		http.csrf(ServerHttpSecurity.CsrfSpec::disable)
        		//Disable Basic Authentication
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)                
              //Disable formLogin 
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((exchange, ex2) -> {
                            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        })
                    )
                //specfiy authorization rules (pathMatchers equivalent to  requestMatchers)
                .authorizeExchange(auth -> auth
//                		.pathMatchers(HttpMethod.GET, "/doctors","/demo").permitAll()
                        .pathMatchers("/users/signin").permitAll() // UMS login/register
                        .pathMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN")
                        .pathMatchers("/bms/**").hasAnyRole("ADMIN")
//                    	// only patient can book the appointment
//        				.pathMatchers(HttpMethod.POST, "/appointments").hasRole("PATIENT")
//        				//  patient or doctor can cancel the appointment
//        				.pathMatchers(HttpMethod.PATCH, "/appointments").hasRole("PATIENT")
//        				// admin | patient can check specific patient details, specific appointment details
//        				.pathMatchers(HttpMethod.GET, "/patients/*","/appointments/patients/**").hasAnyRole("ADMIN","PATIENT")
//        				// admin | doctor can check specific doc details, specific appointment details
//        				.pathMatchers(HttpMethod.GET, "/doctors/*","/appointments/doctors/**").hasAnyRole("ADMIN","DOCTOR")
//        				// only doctor can change appointment status to complete & add some diag tests
        				.pathMatchers(HttpMethod.POST, "/appointments/mark-complete-with-tests").hasRole("DOCTOR")
        				// authenticate any other remaining request        		
                        .anyExchange().authenticated()
                        )
                
               .addFilterAt(customJwtFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
