/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.component;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.result.Result;
import com.toast.shared.domain.types.result.chain.function.PentaFunction;

import java.util.List;
import java.util.function.Function;

public record Chain5<E extends Error, A, B, C, D, F>(Result<E, ResultTuples.Tuple5<A, B, C, D, F>> current) {
    
    public <R> Result<E, R> map(PentaFunction<A, B, C, D, F, R> fn) {
        return ChainHelper.map(current, t -> fn.apply(t._1(), t._2(), t._3(), t._4(), t._5()));
    }
    
    public <R> Result<E, R> flatMap(PentaFunction<A, B, C, D, F, Result<E, R>> fn) {
        return ChainHelper.flatMap(current, t -> fn.apply(t._1(), t._2(), t._3(), t._4(), t._5()));
    }
    
    public <X extends Throwable, R> R mapOrElseThrow(
            PentaFunction<A, B, C, D, F, R> fn,
            Function<List<E>, X> exFn
    ) throws X {
        return ChainHelper.mapOrElseThrow(current, t -> fn.apply(t._1(), t._2(), t._3(), t._4(), t._5()), exFn);
    }
}

