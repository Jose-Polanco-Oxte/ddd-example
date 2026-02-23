package com.toast.shared.domain.types.result;

import com.toast.shared.domain.types.Error;

public record Success<E extends Error, T>(T value) implements Result<E, T> {
}
