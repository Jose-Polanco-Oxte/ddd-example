/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.app;

public interface QueryHandler<Q extends Query, R extends Response> {
    R handle(Q query);
}