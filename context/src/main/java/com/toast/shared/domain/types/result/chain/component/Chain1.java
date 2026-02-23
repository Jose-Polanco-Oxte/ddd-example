package com.toast.shared.domain.types.result.chain.component;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.result.Result;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public record Chain1<E extends Error, A>(Result<E, A> current) {
    
    public <B> Chain2<E, A, B> and(Supplier<Result<E, B>> fn) {
        Result<E, ResultTuples.Tuple2<A, B>> result = ChainHelper.accumulate(
                current,
                fn,
                ResultTuples.Tuple2::new
        );
        return new Chain2<>(result);
    }
    
    public <R> Result<E, R> map(Function<A, R> fn) {
        return ChainHelper.map(current, fn);
    }
    
    public <R> Result<E, R> flatMap(Function<A, Result<E, R>> fn) {
        return ChainHelper.flatMap(current, fn);
    }
    
    public <X extends Throwable, R> R mapOrElseThrow(
            Function<A, R> fn,
            Function<List<E>, X> exFn
    ) throws X {
        return ChainHelper.mapOrElseThrow(current, fn, exFn);
    }
}
