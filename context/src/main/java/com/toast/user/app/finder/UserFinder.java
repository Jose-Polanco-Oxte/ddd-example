/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.finder;

import com.toast.user.app.repository.UserReadOnlyRepository;
import com.toast.user.app.response.UserInfoResponse;

import java.util.UUID;

public final class UserFinder {
    private final UserReadOnlyRepository repository;
    
    public UserFinder(UserReadOnlyRepository repository) {
        this.repository = repository;
    }
    
    public UserInfoResponse find(UUID id) {
        return repository.search(id).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
