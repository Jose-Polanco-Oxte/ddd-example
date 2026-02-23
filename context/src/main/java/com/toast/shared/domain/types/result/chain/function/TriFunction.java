/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.function;

@FunctionalInterface
public interface TriFunction<A, B, C, R> {
    R apply(A a, B b, C c);
}
