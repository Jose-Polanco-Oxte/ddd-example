/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectTest {
    @Test
    void constructorShouldAcceptValidValue() {
        TestValueObject valueObject = new TestValueObject("test");
        
        assertEquals("test", valueObject.value());
    }
    
    @Test
    void constructorShouldThrowForNullValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TestValueObject(null));
        
        assertEquals("Value cannot be null", exception.getMessage());
    }
    
    @Test
    void valueShouldReturnStoredValue() {
        TestValueObject valueObject = new TestValueObject(42);
        
        assertEquals(42, valueObject.value());
    }
    
    @Test
    void equalsShouldReturnTrueForSameValue() {
        TestValueObject vo1 = new TestValueObject("test");
        TestValueObject vo2 = new TestValueObject("test");
        
        assertEquals(vo1, vo2);
    }
    
    @Test
    void equalsShouldReturnFalseForDifferentValues() {
        TestValueObject vo1 = new TestValueObject("test1");
        TestValueObject vo2 = new TestValueObject("test2");
        
        assertNotEquals(vo1, vo2);
    }
    
    @Test
    void equalsShouldReturnFalseForNull() {
        TestValueObject vo = new TestValueObject("test");
        
        assertNotEquals(null, vo);
    }
    
    @Test
    void equalsShouldReturnFalseForDifferentClass() {
        TestValueObject vo = new TestValueObject("test");
        AnotherValueObject other = new AnotherValueObject("test");
        
        assertNotEquals(vo, other);
    }
    
    @Test
    void hashCodeShouldBeSameForEqualValues() {
        TestValueObject vo1 = new TestValueObject("test");
        TestValueObject vo2 = new TestValueObject("test");
        
        assertEquals(vo1.hashCode(), vo2.hashCode());
    }
    
    @Test
    void hashCodeShouldBeDifferentForDifferentValues() {
        TestValueObject vo1 = new TestValueObject("test1");
        TestValueObject vo2 = new TestValueObject("test2");
        
        assertNotEquals(vo1.hashCode(), vo2.hashCode());
    }
    
    @Test
    void toStringShouldReturnValueAsString() {
        TestValueObject vo = new TestValueObject("test");
        
        assertEquals("test", vo.toString());
    }
    
    @Test
    void toStringShouldReturnValueAsStringForIntegerValue() {
        TestValueObject vo = new TestValueObject(123);
        
        assertEquals("123", vo.toString());
    }
    
    @Test
    void shouldWorkWithDifferentTypes() {
        TestValueObject voString = new TestValueObject("string");
        TestValueObject voInt = new TestValueObject(100);
        TestValueObject voDouble = new TestValueObject(3.14);
        
        assertEquals("string", voString.value());
        assertEquals(100, voInt.value());
        assertEquals(3.14, voDouble.value());
    }
    
    private static class TestValueObject extends ValueObject<Object> {
        TestValueObject(Object value) {
            super(value);
        }
    }
    
    private static class AnotherValueObject extends ValueObject<Object> {
        AnotherValueObject(Object value) {
            super(value);
        }
    }
}



