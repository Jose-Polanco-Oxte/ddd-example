/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.component;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.result.Result;
import com.toast.shared.domain.types.result.chain.function.TriFunction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public record Chain3<E extends Error, A, B, C>(Result<E, ResultTuples.Tuple3<A, B, C>> current) {
    
    public <D> Chain4<E, A, B, C, D> and(Supplier<Result<E, D>> fn) {
        Result<E, ResultTuples.Tuple4<A, B, C, D>> result = ChainHelper.accumulate(
                current,
                fn,
                (t, d) -> new ResultTuples.Tuple4<>(t._1(), t._2(), t._3(), d)
        );
        return new Chain4<>(result);
    }
    
    public <R> Result<E, R> map(TriFunction<A, B, C, R> fn) {
        return ChainHelper.map(current, t -> fn.apply(t._1(), t._2(), t._3()));
    }
    
    public <R> Result<E, R> flatMap(TriFunction<A, B, C, Result<E, R>> fn) {
        return ChainHelper.flatMap(current, t -> fn.apply(t._1(), t._2(), t._3()));
    }
    
    public <X extends Throwable, R> R mapOrElseThrow(
            TriFunction<A, B, C, R> fn,
            Function<List<E>, X> exFn
    ) throws X {
        return ChainHelper.mapOrElseThrow(current, t -> fn.apply(t._1(), t._2(), t._3()), exFn);
    }
}

