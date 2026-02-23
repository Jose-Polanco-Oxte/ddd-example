/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

import com.toast.shared.domain.AggregateRoot;

public interface AuthRegistry {
    
    <P extends Principal, T extends AggregateRoot, C extends Credentials>
    void link(C credentials, T aggregate, Class<? extends Identifier<P, T, C>> authClass);
    
    <P extends Principal, T extends AggregateRoot, C extends Credentials>
    void unlink(P principal, Class<? extends Identifier<P, T, C>> authClass);
    
    <P extends Principal, T extends AggregateRoot, C extends Credentials>
    P identify(C credentials, Class<? extends Identifier<P, T, C>> authClass);
    
    <P extends Principal, T extends AggregateRoot, C extends Credentials>
    T bind(String domainId, Class<? extends Identifier<P, T, C>> authClass);
    
    default <P extends Principal, T extends AggregateRoot, C extends Credentials>
    P linkAndIdentify(C credentials, T aggregate, Class<? extends Identifier<P, T, C>> authClass) {
        link(credentials, aggregate, authClass);
        return identify(credentials, authClass);
    }
}