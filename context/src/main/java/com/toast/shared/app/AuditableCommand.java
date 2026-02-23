/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.app;

public interface AuditableCommand<ID> extends Command {
    AuditableCommand<ID> sign(String actorId);
}
