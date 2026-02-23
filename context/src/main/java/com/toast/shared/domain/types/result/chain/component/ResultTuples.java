/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result.chain.component;

public final class ResultTuples {
    private ResultTuples() {
    }
    
    public record Tuple2<A, B>(A _1, B _2) {
    }
    
    protected record Tuple3<A, B, C>(A _1, B _2, C _3) {
    }
    
    protected record Tuple4<A, B, C, D>(A _1, B _2, C _3, D _4) {
    }
    
    protected record Tuple5<A, B, C, D, E>(A _1, B _2, C _3, D _4, E _5) {
    }
}
