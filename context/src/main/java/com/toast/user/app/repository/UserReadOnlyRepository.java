/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.repository;

import com.toast.user.app.response.UserInfoResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserReadOnlyRepository {
    Optional<UserInfoResponse> search(UUID id);
    
    List<UserInfoResponse> all();
}
