/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.persistence.security.repository;

import com.toast.boot.user.persistence.security.entity.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository {
    Optional<Account> findById(Long id);
    
    Optional<Account> findByEmail(String email);
    
    boolean existsByEmail(String email);
    
    Optional<Account> findByUserId(UUID userId);
    
    void save(Account account);
    
    void deleteById(Long userId);
}
