/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.domain.vo;

import com.toast.shared.domain.types.result.Result;
import com.toast.user.domain.UserError;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NameTest {
    @Test
    void ofShouldReturnSuccessForValidName() {
        Result<UserError, Name> result = Name.of("John", "Doe");
        
        assertTrue(result.isSuccess());
        assertEquals("John", result.get().firstName());
        assertEquals("Doe", result.get().lastName());
    }
    
    @Test
    void ofShouldFailForNullFirstName() {
        Result<UserError, Name> result = Name.of(null, "Doe");
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("First name is required", error.reason());
        assertEquals("Invalid name: First name is required", error.message());
    }
    
    @Test
    void ofShouldFailForNullLastName() {
        Result<UserError, Name> result = Name.of("John", null);
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("Last name is required", error.reason());
        assertEquals("Invalid name: Last name is required", error.message());
    }
    
    @Test
    void ofShouldFailForBothNullNames() {
        Result<UserError, Name> result = Name.of(null, null);
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(2, errors.size());
        UserError.InvalidName firstError = assertInstanceOf(UserError.InvalidName.class, errors.get(0));
        UserError.InvalidName secondError = assertInstanceOf(UserError.InvalidName.class, errors.get(1));
        assertEquals("First name is required", firstError.reason());
        assertEquals("Last name is required", secondError.reason());
    }
    
    @Test
    void ofShouldFailForEmptyFirstName() {
        Result<UserError, Name> result = Name.of("", "Doe");
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("First name cannot be empty", error.reason());
        assertEquals("Invalid name: First name cannot be empty", error.message());
    }
    
    @Test
    void ofShouldFailForEmptyLastName() {
        Result<UserError, Name> result = Name.of("John", "");
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("Last name cannot be empty", error.reason());
        assertEquals("Invalid name: Last name cannot be empty", error.message());
    }
    
    @Test
    void ofShouldFailForBlankFirstName() {
        Result<UserError, Name> result = Name.of("   ", "Doe");
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("First name cannot be empty", error.reason());
        assertEquals("Invalid name: First name cannot be empty", error.message());
    }
    
    @Test
    void ofShouldFailForBlankLastName() {
        Result<UserError, Name> result = Name.of("John", "   ");
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("Last name cannot be empty", error.reason());
        assertEquals("Invalid name: Last name cannot be empty", error.message());
    }
    
    @Test
    void ofShouldFailForFirstNameTooLong() {
        String longName = "a".repeat(51);
        Result<UserError, Name> result = Name.of(longName, "Doe");
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("First name is too long", error.reason());
        assertEquals("Invalid name: First name is too long", error.message());
    }
    
    @Test
    void ofShouldFailForLastNameTooLong() {
        String longName = "b".repeat(51);
        Result<UserError, Name> result = Name.of("John", longName);
        
        assertTrue(result.isFailure());
        List<UserError> errors = result.getErrors();
        assertEquals(1, errors.size());
        UserError.InvalidName error = assertInstanceOf(UserError.InvalidName.class, errors.getFirst());
        assertEquals("Last name is too long", error.reason());
        assertEquals("Invalid name: Last name is too long", error.message());
    }
    
    @Test
    void ofShouldSucceedForNamesWithExactly50Characters() {
        String name50Chars = "a".repeat(50);
        Result<UserError, Name> result = Name.of(name50Chars, name50Chars);
        
        assertTrue(result.isSuccess());
        assertEquals(name50Chars, result.get().firstName());
        assertEquals(name50Chars, result.get().lastName());
    }
    
    @Test
    void ofShouldSucceedForSingleCharacterNames() {
        Result<UserError, Name> result = Name.of("J", "D");
        
        assertTrue(result.isSuccess());
        assertEquals("J", result.get().firstName());
        assertEquals("D", result.get().lastName());
    }
    
    @Test
    void loadShouldReturnNameForValidValues() {
        Name name = Name.load("Jane", "Smith");
        
        assertEquals("Jane", name.firstName());
        assertEquals("Smith", name.lastName());
    }
    
    @Test
    void loadShouldThrowForInvalidFirstName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Name.load(null, "Doe"));
        
        assertEquals("Invalid name", exception.getMessage());
    }
    
    @Test
    void loadShouldThrowForInvalidLastName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Name.load("John", null));
        
        assertEquals("Invalid name", exception.getMessage());
    }
    
    @Test
    void loadShouldThrowForEmptyFirstName() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Name.load("", "Doe"));
        
        assertEquals("Invalid name", exception.getMessage());
    }
    
    @Test
    void loadShouldThrowForFirstNameTooLong() {
        String longName = "a".repeat(51);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> Name.load(longName, "Doe"));
        
        assertEquals("Invalid name", exception.getMessage());
    }
    
    @Test
    void equalsShouldReturnTrueForSameValues() {
        Name name1 = Name.load("John", "Doe");
        Name name2 = Name.load("John", "Doe");
        
        assertEquals(name1, name2);
    }
    
    @Test
    void equalsShouldReturnFalseForDifferentFirstNames() {
        Name name1 = Name.load("John", "Doe");
        Name name2 = Name.load("Jane", "Doe");
        
        assertNotEquals(name1, name2);
    }
    
    @Test
    void equalsShouldReturnFalseForDifferentLastNames() {
        Name name1 = Name.load("John", "Doe");
        Name name2 = Name.load("John", "Smith");
        
        assertNotEquals(name1, name2);
    }
    
    @Test
    void equalsShouldReturnFalseForNull() {
        Name name = Name.load("John", "Doe");
        
        assertNotEquals(null, name);
    }
    
    @Test
    void equalsShouldReturnFalseForDifferentType() {
        Name name = Name.load("John", "Doe");
        
        assertNotEquals("John Doe", name);
    }
    
    @Test
    void hashCodeShouldBeSameForEqualNames() {
        Name name1 = Name.load("John", "Doe");
        Name name2 = Name.load("John", "Doe");
        
        assertEquals(name1.hashCode(), name2.hashCode());
    }
    
    @Test
    void toStringShouldContainFirstAndLastName() {
        Name name = Name.load("John", "Doe");
        
        String result = name.toString();
        
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Doe"));
    }
}


