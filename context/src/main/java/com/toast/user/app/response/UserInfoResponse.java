/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.response;

import com.toast.shared.app.Response;

import java.util.UUID;

public record UserInfoResponse(
        UUID userId,
        String firstName,
        String lastName,
        int age
) implements Response {
}
