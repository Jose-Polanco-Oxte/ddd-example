/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.request.query;

import com.toast.shared.app.AuditableQuery;

import java.util.UUID;

public record GetUserByIdQuery(UUID userId) implements AuditableQuery<UUID> {
    public GetUserByIdQuery() {
        this(null);
    }
    
    @Override
    public AuditableQuery<UUID> sign(String actorId) {
        return new GetUserByIdQuery(UUID.fromString(actorId));
    }
}