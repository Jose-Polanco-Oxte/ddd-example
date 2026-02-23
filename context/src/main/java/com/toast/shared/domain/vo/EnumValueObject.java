package com.toast.shared.domain.vo;

import java.util.Set;

public abstract class EnumValueObject<T extends Enum<T>> {
    private final T value;
    
    protected EnumValueObject(T value, T[] validValues) {
        this.value = value;
        Set<T> validValuesSet = Set.of(validValues);
        checkValueIsValid(value, validValuesSet);
    }
    
    public void checkValueIsValid(T value, Set<T> validValues) {
        if (!validValues.contains(value)) {
            throwErrorForInvalidValue(value);
        }
    }
    
    protected abstract void throwErrorForInvalidValue(T value);
    
    public T value() {
        return value;
    }
}
