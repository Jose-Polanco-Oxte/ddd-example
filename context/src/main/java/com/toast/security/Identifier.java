/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

import com.toast.shared.domain.AggregateRoot;

/**
 * Authenticator is responsible for authenticating credentials and providing access to the principal and aggregate root.
 *
 * @param <P> Principal type that represents the authenticated user or entity.
 * @param <T> AggregateRoot type that represents the domain entity associated with the principal.
 * @param <C> Credentials type that represents the credentials used for authentication.
 */
public interface Identifier<P extends Principal, T extends AggregateRoot, C extends Credentials> {
    P identify(C credentials);
    
    void link(C credentials, T aggregate);
    
    void unlink(P principal);
    
    T bind(String domainId);
}