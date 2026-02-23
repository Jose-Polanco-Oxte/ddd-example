/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.security.adapter.authenticator.jwt;

public record JwtPairResponse(
        String accessToken,
        String refreshToken
) {
}
