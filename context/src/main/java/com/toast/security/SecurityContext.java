/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

public interface SecurityContext {
    Principal getPrincipal() throws SecurityException;
    
    void setPrincipal(Principal principal);
    
    <T extends Principal> T getPrincipal(Class<T> clazz) throws SecurityException;
    
    void clear();
}
