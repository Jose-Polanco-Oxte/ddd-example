/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.security;

import com.toast.security.AuthRegistry;
import com.toast.security.Credentials;
import com.toast.security.Identifier;
import com.toast.security.Principal;
import com.toast.shared.domain.AggregateRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpringAuthRegistryImpl implements AuthRegistry {
    private final ApplicationContext applicationContext;
    
    @Override
    public <P extends Principal, T extends AggregateRoot, C extends Credentials> void link(C credentials, T aggregate, Class<? extends Identifier<P, T, C>> authClass) {
        applicationContext.getBean(authClass).link(credentials, aggregate);
    }
    
    @Override
    public <P extends Principal, T extends AggregateRoot, C extends Credentials> void unlink(P principal, Class<? extends Identifier<P, T, C>> authClass) {
        applicationContext.getBean(authClass).unlink(principal);
    }
    
    @Override
    public <P extends Principal, T extends AggregateRoot, C extends Credentials> P identify(C credentials, Class<? extends Identifier<P, T, C>> authClass) {
        return applicationContext.getBean(authClass).identify(credentials);
    }
    
    @Override
    public <P extends Principal, T extends AggregateRoot, C extends Credentials> T bind(String domainId, Class<? extends Identifier<P, T, C>> authClass) {
        return applicationContext.getBean(authClass).bind(domainId);
    }
}
