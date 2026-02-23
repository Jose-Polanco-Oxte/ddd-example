/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.security;

import com.toast.boot.user.security.adapter.filter.TokenAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TokenAuthorizationFilter tokenFilter) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Estamos en modo Stateless con JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                                                       .requestMatchers("/auth/**", "/error/**")
                                                       .permitAll() // Permitir registro/login
                                                       .anyRequest()
                                                       .authenticated()
                )
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
}
