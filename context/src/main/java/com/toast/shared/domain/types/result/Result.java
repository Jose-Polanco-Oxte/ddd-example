package com.toast.shared.domain.types.result;

import com.toast.shared.domain.types.Either;
import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.Nothing;
import com.toast.shared.domain.types.result.chain.component.Chain1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public sealed interface Result<E extends Error, T> extends Either<List<E>, T> permits Failure, Success {
    static <E extends Error, T> Result<E, T> success(T value) {
        return new Success<>(value);
    }
    
    static <E extends Error, T> Result<E, T> check(T value) {
        return new Success<>(value);
    }
    
    static <E extends Error> Result<E, Nothing> success() {
        return new Success<>(new Nothing());
    }
    
    static <E extends Error, T> Result<E, Nothing> failure(Result<E, T> result) {
        return new Failure<>(new ArrayList<>(result.getErrors()), new Nothing());
    }
    
    static <E extends Error, A> Chain1<E, A> chain(Result<E, A> r) {
        return new Chain1<>(r);
    }
    
    static <E extends Error, T> Result<E, T> failure(E error) {
        return new Failure<>(new ArrayList<>(List.of(error)), null);
    }
    
    static <E extends Error, T> Result<E, T> failure(List<E> error) {
        return new Failure<>(new ArrayList<>(error), null);
    }
    
    static <E extends Error, T> Result<E, T> failure(E error, T value) {
        return new Failure<>(new ArrayList<>(List.of(error)), value);
    }
    
    static <E extends Error, T> Result<E, T> failure(List<E> error, T value) {
        return new Failure<>(error, value);
    }
    
    @SafeVarargs
    static <E extends Error> Result<E, Nothing> combineAll(Result<E, Nothing>... results) {
        Result<E, Nothing> finalResult = Result.success(new Nothing());
        for (var res : results) {
            finalResult = finalResult.combine(res);
        }
        return finalResult;
    }
    
    default T get() {
        if (this instanceof Success<E, T>(T value)) {
            return value;
        }
        throw new IllegalStateException("Cannot get value from a Failure result");
    }
    
    default List<E> getErrors() {
        if (this instanceof Failure<E, T>(List<E> errors, var _)) {
            return errors;
        }
        throw new IllegalStateException("Cannot get errors from a Success result");
    }
    
    default T getOrElse(Supplier<T> other) {
        return (this instanceof Success<E, T>(T value)) ? value : other.get();
    }
    
    default List<E> getErrorsOrElse(Supplier<List<E>> other) {
        return (this instanceof Failure<E, T>(List<E> errors, var _)) ? errors : other.get();
    }
    
    default <X extends RuntimeException> T orElseThrow(Supplier<X> exceptionSupplier) throws X {
        if (this instanceof Failure) {
            throw exceptionSupplier.get();
        }
        return ((Success<E, T>) this).value();
    }
    
    default <X extends RuntimeException> void ifFailureThrows(Supplier<X> exceptionSupplier) throws X {
        if (this instanceof Failure) {
            throw exceptionSupplier.get();
        }
    }
    
    default void ifSuccess(Consumer<T> action) {
        if (this instanceof Success<E, T>(T value)) {
            action.accept(value);
        }
    }
    
    default void ifSuccess(Runnable action) {
        if (this instanceof Success) {
            action.run();
        }
    }
    
    default void ifFailure(Consumer<List<E>> action) {
        if (this instanceof Failure<E, T>(List<E> errors, var _)) {
            action.accept(errors);
        }
    }
    
    default boolean isSuccess() {
        return this instanceof Success;
    }
    
    default boolean isFailure() {
        return this instanceof Failure;
    }
    
    default <R> R fold(Function<List<E>, R> onFailure, Function<T, R> onSuccess) {
        return (this instanceof Success<E, T>(T value))
                       ? onSuccess.apply(value)
                       : onFailure.apply(((Failure<E, T>) this).errors());
    }
    
    default <X extends RuntimeException> void executeOrElseThrow(Consumer<T> onSuccess, Function<List<E>, X> onFailure) throws X {
        if (this instanceof Success<E, T>(T value)) {
            onSuccess.accept(value);
        } else {
            throw onFailure.apply(((Failure<E, T>) this).errors());
        }
    }
    
    default <X extends RuntimeException> void executeOrElseThrow(Runnable runnable, Function<List<E>, X> onFailure) throws X {
        if (this instanceof Success<E, T>(T value)) {
            runnable.run();
        } else {
            throw onFailure.apply(((Failure<E, T>) this).errors());
        }
    }
    
    default <X extends RuntimeException, R> R mapOrElseThrow(Function<T, R> onSuccess, Function<List<E>, X> onFailure) throws X {
        if (this instanceof Success<E, T>(T value)) {
            return onSuccess.apply(value);
        } else {
            throw onFailure.apply(((Failure<E, T>) this).errors());
        }
    }
    
    default <U> Result<E, U> combine(Result<E, U> other) {
        if (this instanceof Success && other instanceof Success<E, U> s) {
            return s;
        }
        
        List<E> allErrors = new ArrayList<>();
        if (this instanceof Failure<E, ?> f1) allErrors.addAll(f1.errors());
        if (other instanceof Failure<E, ?> f2) allErrors.addAll(f2.errors());
        
        U otherValue;
        if ((other instanceof Success<E, U>(U value))) {
            otherValue = value;
        } else {
            assert other instanceof Failure<E, U>;
            otherValue = ((Failure<E, U>) other).value();
        }
        
        return new Failure<>(allErrors, otherValue);
    }
    
    default <U> Result<E, U> combine(Supplier<Result<E, U>> otherSupplier) {
        return combine(otherSupplier.get());
    }
    
    default <U> Result<E, U> mapTo(Supplier<U> supplier) {
        return map(_ -> supplier.get());
    }
    
    default Result<E, Nothing> ifSuccessCompute(Runnable runnable) {
        if (this instanceof Success) {
            runnable.run();
            return Result.success();
        }
        return new Failure<>(((Failure<E, T>) this).errors(), new Nothing());
    }
    
    default <U> Result<E, U> map(Function<? super T, ? extends U> mapper) {
        if (this instanceof Success<E, T>(T value)) return success(mapper.apply(value));
        return new Failure<>(((Failure<E, T>) this).errors(), null);
    }
    
    default <U> Result<E, U> flatMap(Function<T, Result<E, U>> mapper) {
        if (this instanceof Success<E, T>(T value)) {
            return mapper.apply(value);
        }
        @SuppressWarnings("unchecked")
        Result<E, U> failure = (Result<E, U>) this;
        return failure;
    }
    
    default Result<E, T> mustBe(Predicate<T> predicate, E error) {
        if (this instanceof Success<E, T>(T value)) {
            return predicate.test(value) ? this : failure(error, value);
        }
        
        Failure<E, T> f = (Failure<E, T>) this;
        if (f.value() == null) return f;
        
        if (!predicate.test(f.value())) {
            List<E> newErrors = new ArrayList<>(f.errors());
            newErrors.add(error);
            return new Failure<>(newErrors, f.value());
        }
        return f;
    }
    
    default Result<E, T> filter(Predicate<T> predicate, E error) {
        if (this instanceof Failure<E, T> f) return f;
        T val = ((Success<E, T>) this).value();
        return predicate.test(val) ? this : failure(error, val);
    }
    
    default Result<E, Nothing> toNothing() {
        if (this instanceof Success) {
            return Result.success();
        }
        return new Failure<>(((Failure<E, T>) this).errors(), new Nothing());
    }
    
    @SuppressWarnings("unchecked")
    default <E2 extends Error> Result<E2, T> cast() {
        return (Result<E2, T>) this;
    }
}