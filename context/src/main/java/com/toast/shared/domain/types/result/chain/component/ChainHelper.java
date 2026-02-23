/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.component;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.Nothing;
import com.toast.shared.domain.types.result.Result;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ChainHelper {
    private ChainHelper() {
    }
    
    static <T, U, R, E extends Error> Result<E, R> accumulate(
            Result<E, T> previous,
            Supplier<Result<E, U>> fn,
            BiFunction<T, U, R> combiner
    ) {
        Result<E, U> next = fn.get();
        
        if (previous.isSuccess() && next.isSuccess()) {
            return Result.success(combiner.apply(previous.get(), next.get()));
        }
        Result<E, Nothing> emptyResult = Result.success();
        emptyResult.combine(previous);
        emptyResult.combine(next);
        return Result.failure(emptyResult.getErrors());
    }
    
    static <E extends Error, T, R> Result<E, R> map(
            Result<E, T> current,
            Function<T, R> fn
    ) {
        if (current.isSuccess()) return Result.success(fn.apply(current.get()));
        return Result.failure(current.getErrors());
    }
    
    static <X extends Throwable, E extends Error, T, R> R mapOrElseThrow(
            Result<E, T> current,
            Function<T, R> fn,
            Function<List<E>, X> exFn
    ) throws X {
        if (current.isSuccess()) return fn.apply(current.get());
        throw exFn.apply(current.getErrors());
    }
    
    static <E extends Error, T, R> Result<E, R> flatMap(
            Result<E, T> current,
            Function<T, Result<E, R>> fn
    ) {
        if (current.isSuccess()) return fn.apply(current.get());
        return Result.failure(current.getErrors());
    }
}
