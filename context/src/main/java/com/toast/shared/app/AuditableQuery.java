/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.app;

public interface AuditableQuery<ID> extends Query {
    AuditableQuery<ID> sign(String actorId);
}
