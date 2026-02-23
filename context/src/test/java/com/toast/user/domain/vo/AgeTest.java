package com.toast.user.domain.vo;

import com.toast.shared.domain.types.result.Result;
import com.toast.user.domain.UserError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AgeTest {
    @Test
    void ofShouldReturnSuccessForValidAge() {
        Result<UserError, Age> result = Age.of(25);
        
        assertTrue(result.isSuccess());
        assertEquals(25, result.get().value());
    }
    
    @Test
    void ofShouldFailForNegativeAge() {
        Result<UserError, Age> result = Age.of(-1);
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidAge error = assertInstanceOf(UserError.InvalidAge.class, errors.getFirst());
        assertEquals("Age cannot be negative", error.reason());
        assertEquals("Invalid age: Age cannot be negative", error.message());
    }
    
    @Test
    void ofShouldFailForAgeGreaterThan130() {
        Result<UserError, Age> result = Age.of(131);
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidAge error = assertInstanceOf(UserError.InvalidAge.class, errors.getFirst());
        assertEquals("Age cannot be greater than 130", error.reason());
        assertEquals("Invalid age: Age cannot be greater than 130", error.message());
    }
    
    @Test
    void ofShouldFailForInvalidAge() {
        Result<UserError, Age> result = Age.of(-9999);
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidAge error = assertInstanceOf(UserError.InvalidAge.class, errors.getFirst());
        assertEquals("Age cannot be negative", error.reason());
        assertEquals("Invalid age: Age cannot be negative", error.message());
    }
    
    @Test
    void loadShouldReturnAgeForValidValue() {
        Age age = Age.load(42);
        
        assertEquals(42, age.value());
    }
    
    @Test
    void loadShouldThrowForInvalidValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Age.load(200));
        
        assertEquals("Invalid age", exception.getMessage());
    }
    
    @Test
    void loadShouldThrowForNegativeValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Age.load(-10));
        
        assertEquals("Invalid age", exception.getMessage());
    }
    
    @Test
    void loadShouldThrowForZeroValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> Age.load(0));
        
        assertEquals("Invalid age", exception.getMessage());
    }
}