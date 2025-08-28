package com.digitalbevy.githubapp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // disable CSRF since frontend POST requests are same-origin
            .authorizeHttpRequests()
            .requestMatchers("/api/repos/**").permitAll() // allow all requests to /api/repos
            .anyRequest().authenticated() // other endpoints require auth if you add them
            .and()
            .httpBasic(); // optional, can remove if you don’t use basic auth

        return http.build();
    }
}
