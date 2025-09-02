package org.vipunsanjana.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/eureka/**")) // Ignore CSRF for Eureka
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/eureka/**").permitAll() // Allow Eureka without authentication
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
