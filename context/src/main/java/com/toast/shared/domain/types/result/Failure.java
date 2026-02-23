package com.toast.shared.domain.types.result;

import com.toast.shared.domain.types.Error;

import java.util.List;

public record Failure<E extends Error, T>(List<E> errors, T value) implements Result<E, T> {
}
