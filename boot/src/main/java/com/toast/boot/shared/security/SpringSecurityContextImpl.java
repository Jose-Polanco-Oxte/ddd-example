/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.security;

import com.toast.security.Principal;
import com.toast.security.SecurityContext;
import com.toast.security.SecurityException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SpringSecurityContextImpl implements SecurityContext {
    private static final ThreadLocal<Principal> CONTEXT = new ThreadLocal<>();
    
    public void clear() {
        CONTEXT.remove();
    }
    
    @Override
    public Principal getPrincipal() throws SecurityException {
        return Optional.ofNullable(CONTEXT.get())
                       .orElseThrow(() -> new SecurityException("No principal found in security context"));
    }
    
    @Override
    public void setPrincipal(Principal principal) {
        if (principal == null) {
            CONTEXT.remove();
        } else {
            CONTEXT.set(principal);
        }
    }
    
    @Override
    public <T extends Principal> T getPrincipal(Class<T> clazz) throws SecurityException {
        Principal principal = getPrincipal();
        if (clazz.isInstance(principal)) {
            return clazz.cast(principal);
        } else {
            throw new SecurityException("Principal is not of type: " + clazz.getName());
        }
    }
}
