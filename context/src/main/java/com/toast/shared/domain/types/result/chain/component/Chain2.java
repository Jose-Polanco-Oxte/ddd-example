/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.component;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.result.Result;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public record Chain2<E extends Error, A, B>(Result<E, ResultTuples.Tuple2<A, B>> current) {
    
    public <C> Chain3<E, A, B, C> and(Supplier<Result<E, C>> fn) {
        Result<E, ResultTuples.Tuple3<A, B, C>> result = ChainHelper.accumulate(
                current,
                fn,
                (t, c) -> new ResultTuples.Tuple3<>(t._1(), t._2(), c)
        );
        return new Chain3<>(result);
    }
    
    public <R> Result<E, R> map(BiFunction<A, B, R> fn) {
        return ChainHelper.map(current, t -> fn.apply(t._1(), t._2()));
    }
    
    public <R> Result<E, R> flatMap(BiFunction<A, B, Result<E, R>> fn) {
        return ChainHelper.flatMap(current, t -> fn.apply(t._1(), t._2()));
    }
    
    public <X extends Throwable, R> R mapOrElseThrow(BiFunction<A, B, R> fn, Function<List<E>, X> exFn) throws X {
        return ChainHelper.mapOrElseThrow(current, t -> fn.apply(t._1(), t._2()), exFn);
    }
}
