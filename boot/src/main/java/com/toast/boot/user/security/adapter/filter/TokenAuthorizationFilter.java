/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.security.adapter.filter;

import com.toast.boot.user.persistence.security.entity.Account;
import com.toast.boot.user.security.adapter.authenticator.jwt.JwtTokenService;
import com.toast.security.SecurityContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class TokenAuthorizationFilter extends OncePerRequestFilter {
    
    private final SecurityContext securityContext;
    private final JwtTokenService tokenService;
    private final HandlerExceptionResolver resolver;
    
    public TokenAuthorizationFilter(
            SecurityContext securityContext,
            JwtTokenService tokenService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver
    ) {
        this.securityContext = securityContext;
        this.tokenService = tokenService;
        this.resolver = resolver;
    }
    
    
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String requestURI = request.getRequestURI();
            log.debug("TokenAuthorizationFilter processing request: {}", requestURI);
            
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                String jwtToken = token.substring(7);
                log.debug("Token found, attempting to authenticate");
                Account account = tokenService.parseAccessToken(jwtToken);
                securityContext.setPrincipal(account);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(account, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } else {
                log.debug("No Bearer token found in Authorization header");
            }
            
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            log.error("Error en filtro: {}", ex.getMessage());
            resolver.resolveException(request, response, null, ex);
        } finally {
            securityContext.clear();
        }
    }
}
