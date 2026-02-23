/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.security;

import com.toast.security.SecurityContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SecurityCleanupInterceptor implements HandlerInterceptor {
    private final SecurityContext securityContext;
    
    public SecurityCleanupInterceptor(SecurityContext securityContext) {
        this.securityContext = securityContext;
    }
    
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) {
        securityContext.clear();
    }
}
