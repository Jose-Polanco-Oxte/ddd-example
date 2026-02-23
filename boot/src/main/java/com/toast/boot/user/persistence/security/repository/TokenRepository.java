/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.persistence.security.repository;

import com.toast.boot.user.persistence.security.entity.Token;

import java.util.Optional;
import java.util.UUID;

public interface TokenRepository {
    void save(Token token);
    
    Optional<Token> findById(UUID id);
    
    void revoke(UUID id);
}
