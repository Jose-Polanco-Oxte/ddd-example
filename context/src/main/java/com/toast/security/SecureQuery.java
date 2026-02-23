/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

import com.toast.shared.app.Query;

import java.time.Instant;

public record SecureQuery(
        Query query,
        Principal principal,
        Instant timestamp
) {
    public String trace() {
        return "[query:%s] %s executed by %s".formatted(timestamp, query.getClass().getSimpleName(), principal.getIdentifier());
    }
}
