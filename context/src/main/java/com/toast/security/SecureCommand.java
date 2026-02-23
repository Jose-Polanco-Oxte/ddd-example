/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

import com.toast.shared.app.Command;

import java.time.Instant;

public record SecureCommand(
        Command command,
        Principal principal,
        Instant timestamp
) {
    public String trace() {
        return "[command:%s] %s executed by %s".formatted(timestamp, command.getClass().getSimpleName(), principal.getIdentifier());
    }
}
