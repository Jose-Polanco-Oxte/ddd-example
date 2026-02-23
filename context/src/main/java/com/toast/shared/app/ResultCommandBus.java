/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.app;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.Nothing;
import com.toast.shared.domain.types.result.Result;

public interface ResultCommandBus {
    Result<? extends Error, Nothing> dispatch(Command command);
}
