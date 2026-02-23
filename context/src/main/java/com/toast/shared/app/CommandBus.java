/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.app;

import java.util.concurrent.CompletableFuture;

public interface CommandBus {
    void dispatch(Command command);
    
    CompletableFuture<Void> awaitDispatch(Command command);
}
