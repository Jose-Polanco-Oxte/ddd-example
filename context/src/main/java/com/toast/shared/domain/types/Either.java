package com.toast.shared.domain.types;

import java.util.function.Function;

public interface Either<L, R> {
    <C> C fold(Function<L, C> ifLeft, Function<R, C> ifRight);
}