package com.toast.shared.app;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.Nothing;
import com.toast.shared.domain.types.result.Result;

public interface ResultCommandHandler<C extends Command> {
    Result<? extends Error, Nothing> handle(C command);
}
