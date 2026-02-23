/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.app;

public interface QueryBus {
    <R extends Response> R ask(Query query);
}
