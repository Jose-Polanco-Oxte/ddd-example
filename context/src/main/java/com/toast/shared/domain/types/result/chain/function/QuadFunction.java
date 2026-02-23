/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.function;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, R> {
    R apply(A a, B b, C c, D d);
}
