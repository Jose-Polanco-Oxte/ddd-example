package com.toast.shared.domain.vo;

public abstract class ValueObject<T> {
    private final T value;
    
    protected ValueObject(T value) {
        this.value = value;
        ensureValueIsNotNull(value);
    }
    
    private void ensureValueIsNotNull(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
    }
    
    public T value() {
        return value;
    }
    
    @Override
    public String toString() {
        return value.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ValueObject<?> that = (ValueObject<?>) o;
        
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
