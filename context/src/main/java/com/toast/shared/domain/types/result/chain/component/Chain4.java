/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.component;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.result.Result;
import com.toast.shared.domain.types.result.chain.function.QuadFunction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public record Chain4<E extends Error, A, B, C, D>(Result<E, ResultTuples.Tuple4<A, B, C, D>> current) {
    
    public <F> Chain5<E, A, B, C, D, F> and(Supplier<Result<E, F>> fn) {
        Result<E, ResultTuples.Tuple5<A, B, C, D, F>> result = ChainHelper.accumulate(
                current,
                fn,
                (t, f) -> new ResultTuples.Tuple5<>(t._1(), t._2(), t._3(), t._4(), f)
        );
        return new Chain5<>(result);
    }
    
    public <R> Result<E, R> map(QuadFunction<A, B, C, D, R> fn) {
        return ChainHelper.map(current, t -> fn.apply(t._1(), t._2(), t._3(), t._4()));
    }
    
    public <R> Result<E, R> flatMap(QuadFunction<A, B, C, D, Result<E, R>> fn) {
        return ChainHelper.flatMap(current, t -> fn.apply(t._1(), t._2(), t._3(), t._4()));
    }
    
    public <X extends Throwable, R> R mapOrElseThrow(
            QuadFunction<A, B, C, D, R> fn,
            Function<List<E>, X> exFn
    ) throws X {
        return ChainHelper.mapOrElseThrow(current, t -> fn.apply(t._1(), t._2(), t._3(), t._4()), exFn);
    }
}

