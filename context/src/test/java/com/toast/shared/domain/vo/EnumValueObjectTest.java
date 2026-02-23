/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.vo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnumValueObjectTest {
    @Test
    void constructorShouldAcceptValidValue() {
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE1, TestEnum.values());
        
        assertEquals(TestEnum.VALUE1, valueObject.value());
    }
    
    @Test
    void constructorShouldAcceptAllValidValues() {
        TestEnumValueObject vo1 = new TestEnumValueObject(TestEnum.VALUE1, TestEnum.values());
        TestEnumValueObject vo2 = new TestEnumValueObject(TestEnum.VALUE2, TestEnum.values());
        TestEnumValueObject vo3 = new TestEnumValueObject(TestEnum.VALUE3, TestEnum.values());
        
        assertEquals(TestEnum.VALUE1, vo1.value());
        assertEquals(TestEnum.VALUE2, vo2.value());
        assertEquals(TestEnum.VALUE3, vo3.value());
    }
    
    @Test
    void constructorShouldThrowForInvalidValue() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1, TestEnum.VALUE2};
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE3, validValues));
        
        assertEquals("Invalid value: VALUE3", exception.getMessage());
    }
    
    @Test
    void constructorShouldAcceptSubsetOfEnumValues() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1};
        
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE1, validValues);
        
        assertEquals(TestEnum.VALUE1, valueObject.value());
    }
    
    @Test
    void constructorShouldRejectValueNotInSubset() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1};
        
        assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE2, validValues));
    }
    
    @Test
    void valueShouldReturnStoredEnumValue() {
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE2, TestEnum.values());
        
        assertEquals(TestEnum.VALUE2, valueObject.value());
    }
    
    @Test
    void checkValueIsValidShouldNotThrowForValidValue() {
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE1, TestEnum.values());
        
        assertDoesNotThrow(() -> valueObject.checkValueIsValid(TestEnum.VALUE1,
                java.util.Set.of(TestEnum.values())));
    }
    
    @Test
    void checkValueIsValidShouldThrowForInvalidValue() {
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE1, TestEnum.values());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> valueObject.checkValueIsValid(TestEnum.VALUE3,
                        java.util.Set.of(TestEnum.VALUE1, TestEnum.VALUE2)));
        
        assertEquals("Invalid value: VALUE3", exception.getMessage());
    }
    
    @Test
    void constructorWithEmptyValidValuesShouldThrowForAnyValue() {
        TestEnum[] emptyValidValues = new TestEnum[]{};
        
        assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE1, emptyValidValues));
    }
    
    @Test
    void constructorWithSingleValidValueShouldAcceptOnlyThatValue() {
        TestEnum[] singleValidValue = new TestEnum[]{TestEnum.VALUE2};
        
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE2, singleValidValue);
        
        assertEquals(TestEnum.VALUE2, valueObject.value());
    }
    
    @Test
    void constructorWithSingleValidValueShouldRejectOtherValues() {
        TestEnum[] singleValidValue = new TestEnum[]{TestEnum.VALUE2};
        
        assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE1, singleValidValue));
    }
    
    @Test
    void constructorWithMultipleValidValuesShouldWorkCorrectly() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1, TestEnum.VALUE3};
        
        TestEnumValueObject vo1 = new TestEnumValueObject(TestEnum.VALUE1, validValues);
        TestEnumValueObject vo3 = new TestEnumValueObject(TestEnum.VALUE3, validValues);
        
        assertEquals(TestEnum.VALUE1, vo1.value());
        assertEquals(TestEnum.VALUE3, vo3.value());
    }
    
    @Test
    void constructorWithMultipleValidValuesShouldRejectOthers() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1, TestEnum.VALUE3};
        
        assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE2, validValues));
    }
    
    @Test
    void differentEnumTypesShouldWork() {
        AnotherEnumValueObject valueObject = new AnotherEnumValueObject(
                AnotherEnum.OPTION_A, AnotherEnum.values());
        
        assertEquals(AnotherEnum.OPTION_A, valueObject.value());
    }
    
    @Test
    void differentEnumTypesShouldValidateCorrectly() {
        AnotherEnum[] validValues = new AnotherEnum[]{AnotherEnum.OPTION_A};
        
        assertThrows(IllegalArgumentException.class,
                () -> new AnotherEnumValueObject(AnotherEnum.OPTION_B, validValues));
    }
    
    @Test
    void errorMessageShouldContainEnumValueName() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1};
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE2, validValues));
        
        assertTrue(exception.getMessage().contains("VALUE2"));
    }
    
    @Test
    void multipleInstancesWithSameValueShouldWork() {
        TestEnum[] validValues = TestEnum.values();
        
        TestEnumValueObject vo1 = new TestEnumValueObject(TestEnum.VALUE1, validValues);
        TestEnumValueObject vo2 = new TestEnumValueObject(TestEnum.VALUE1, validValues);
        
        assertEquals(TestEnum.VALUE1, vo1.value());
        assertEquals(TestEnum.VALUE1, vo2.value());
    }
    
    @Test
    void constructorShouldValidateImmediately() {
        TestEnum[] validValues = new TestEnum[]{TestEnum.VALUE1};
        
        assertThrows(IllegalArgumentException.class,
                () -> new TestEnumValueObject(TestEnum.VALUE3, validValues));
    }
    
    @Test
    void valueShouldNotBeNull() {
        TestEnumValueObject valueObject = new TestEnumValueObject(TestEnum.VALUE1, TestEnum.values());
        
        assertNotNull(valueObject.value());
    }
    
    private enum TestEnum {
        VALUE1,
        VALUE2,
        VALUE3
    }
    
    private enum AnotherEnum {
        OPTION_A,
        OPTION_B,
        OPTION_C
    }
    
    private static class TestEnumValueObject extends EnumValueObject<TestEnum> {
        TestEnumValueObject(TestEnum value, TestEnum[] validValues) {
            super(value, validValues);
        }
        
        @Override
        protected void throwErrorForInvalidValue(TestEnum value) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }
    
    private static class AnotherEnumValueObject extends EnumValueObject<AnotherEnum> {
        AnotherEnumValueObject(AnotherEnum value, AnotherEnum[] validValues) {
            super(value, validValues);
        }
        
        @Override
        protected void throwErrorForInvalidValue(AnotherEnum value) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
    }
}

