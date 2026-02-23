/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain.types.result;

import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.Nothing;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ResultTest {
    @Test
    void successShouldCreateSuccessResultWithValue() {
        Result<TestError, String> result = Result.success("test");
        
        assertTrue(result.isSuccess());
        assertFalse(result.isFailure());
        assertEquals("test", result.get());
    }
    
    @Test
    void checkShouldCreateSuccessResultWithValue() {
        Result<TestError, Integer> result = Result.check(42);
        
        assertTrue(result.isSuccess());
        assertEquals(42, result.get());
    }
    
    @Test
    void successWithoutValueShouldCreateSuccessResultWithNothing() {
        Result<TestError, Nothing> result = Result.success();
        
        assertTrue(result.isSuccess());
        assertNotNull(result.get());
        assertInstanceOf(Nothing.class, result.get());
    }
    
    @Test
    void failureShouldCreateFailureResultWithSingleError() {
        TestError error = new TestError("error message");
        
        Result<TestError, String> result = Result.failure(error);
        
        assertTrue(result.isFailure());
        assertFalse(result.isSuccess());
        assertEquals(1, result.getErrors().size());
        assertEquals(error, result.getErrors().getFirst());
    }
    
    @Test
    void failureShouldCreateFailureResultWithMultipleErrors() {
        TestError error1 = new TestError("error1");
        TestError error2 = new TestError("error2");
        List<TestError> errors = List.of(error1, error2);
        
        Result<TestError, String> result = Result.failure(errors);
        
        assertTrue(result.isFailure());
        assertEquals(2, result.getErrors().size());
        assertTrue(result.getErrors().contains(error1));
        assertTrue(result.getErrors().contains(error2));
    }
    
    @Test
    void failureShouldCreateFailureResultWithErrorAndValue() {
        TestError error = new TestError("error");
        
        Result<TestError, String> result = Result.failure(error, "value");
        
        assertTrue(result.isFailure());
        assertEquals(1, result.getErrors().size());
        assertEquals(error, result.getErrors().getFirst());
    }
    
    @Test
    void failureShouldCreateFailureResultFromAnotherResult() {
        Result<TestError, String> originalResult = Result.failure(new TestError("error"));
        
        Result<TestError, Nothing> result = Result.failure(originalResult);
        
        assertTrue(result.isFailure());
        assertEquals(1, result.getErrors().size());
        IllegalStateException stateException = assertThrows(IllegalStateException.class, result::get);
        assertSame("Cannot get value from a Failure result", stateException.getMessage());
    }
    
    @Test
    void combineAllShouldReturnSuccessWhenAllResultsAreSuccess() {
        Result<TestError, Nothing> result1 = Result.success();
        Result<TestError, Nothing> result2 = Result.success();
        Result<TestError, Nothing> result3 = Result.success();
        
        Result<TestError, Nothing> combined = Result.combineAll(result1, result2, result3);
        
        assertTrue(combined.isSuccess());
    }
    
    @Test
    void combineAllShouldReturnFailureWhenOneResultFails() {
        Result<TestError, Nothing> result1 = Result.success();
        Result<TestError, Nothing> result2 = Result.failure(new TestError("error"));
        Result<TestError, Nothing> result3 = Result.success();
        
        Result<TestError, Nothing> combined = Result.combineAll(result1, result2, result3);
        
        assertTrue(combined.isFailure());
        assertEquals(1, combined.getErrors().size());
    }
    
    @Test
    void combineAllShouldAccumulateAllErrors() {
        Result<TestError, Nothing> result1 = Result.failure(new TestError("error1"));
        Result<TestError, Nothing> result2 = Result.failure(new TestError("error2"));
        Result<TestError, Nothing> result3 = Result.failure(new TestError("error3"));
        
        Result<TestError, Nothing> combined = Result.combineAll(result1, result2, result3);
        
        assertTrue(combined.isFailure());
        assertEquals(3, combined.getErrors().size());
    }
    
    @Test
    void combineAllShouldHandleEmptyArray() {
        Result<TestError, Nothing> combined = Result.combineAll();
        
        assertTrue(combined.isSuccess());
    }
    
    @Test
    void getValueShouldReturnForSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        assertEquals("value", result.get());
    }
    
    @Test
    void getShouldThrowForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        assertThrows(IllegalStateException.class, result::get);
    }
    
    @Test
    void getErrorsShouldReturnErrorsForFailure() {
        TestError error = new TestError("error");
        Result<TestError, String> result = Result.failure(error);
        
        List<TestError> errors = result.getErrors();
        
        assertEquals(1, errors.size());
        assertEquals(error, errors.getFirst());
    }
    
    @Test
    void getErrorsShouldThrowForSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        assertThrows(IllegalStateException.class, result::getErrors);
    }
    
    @Test
    void getOrElseShouldReturnValueForSuccess() {
        Result<TestError, String> result = Result.success("original");
        
        String value = result.getOrElse(() -> "alternative");
        
        assertEquals("original", value);
    }
    
    @Test
    void getOrElseShouldReturnAlternativeForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        String value = result.getOrElse(() -> "alternative");
        
        assertEquals("alternative", value);
    }
    
    @Test
    void getErrorsOrElseShouldReturnErrorsForFailure() {
        TestError error = new TestError("error");
        Result<TestError, String> result = Result.failure(error);
        
        List<TestError> errors = result.getErrorsOrElse(ArrayList::new);
        
        assertEquals(1, errors.size());
        assertEquals(error, errors.getFirst());
    }
    
    @Test
    void getErrorsOrElseShouldReturnAlternativeForSuccess() {
        Result<TestError, String> result = Result.success("value");
        List<TestError> alternative = List.of(new TestError("alternative"));
        
        List<TestError> errors = result.getErrorsOrElse(() -> alternative);
        
        assertEquals(alternative, errors);
    }
    
    @Test
    void orElseThrowShouldReturnValueForSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        String value = result.orElseThrow(() -> new RuntimeException("error"));
        
        assertEquals("value", value);
    }
    
    @Test
    void orElseThrowShouldThrowExceptionForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> result.orElseThrow(() -> new RuntimeException("custom error")));
        
        assertEquals("custom error", exception.getMessage());
    }
    
    @Test
    void ifSuccessShouldExecuteActionForSuccess() {
        Result<TestError, String> result = Result.success("value");
        AtomicBoolean executed = new AtomicBoolean(false);
        
        result.ifSuccess(value -> executed.set(true));
        
        assertTrue(executed.get());
    }
    
    @Test
    void ifSuccessShouldNotExecuteActionForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        AtomicBoolean executed = new AtomicBoolean(false);
        
        result.ifSuccess(value -> executed.set(true));
        
        assertFalse(executed.get());
    }
    
    @Test
    void ifFailureShouldExecuteActionForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        AtomicBoolean executed = new AtomicBoolean(false);
        
        result.ifFailure(err -> executed.set(true));
        
        assertTrue(executed.get());
    }
    
    @Test
    void ifFailureShouldNotExecuteActionForSuccess() {
        Result<TestError, String> result = Result.success("value");
        AtomicBoolean executed = new AtomicBoolean(false);
        
        result.ifFailure(err -> executed.set(true));
        
        assertFalse(executed.get());
    }
    
    @Test
    void isSuccessShouldReturnTrueForSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        assertTrue(result.isSuccess());
    }
    
    @Test
    void isSuccessShouldReturnFalseForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        assertFalse(result.isSuccess());
    }
    
    @Test
    void isFailureShouldReturnTrueForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        assertTrue(result.isFailure());
    }
    
    @Test
    void isFailureShouldReturnFalseForSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        assertFalse(result.isFailure());
    }
    
    @Test
    void foldShouldExecuteOnSuccessForSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        String folded = result.fold(
                err -> "failure",
                value -> "success: " + value
        );
        
        assertEquals("success: value", folded);
    }
    
    @Test
    void foldShouldExecuteOnFailureForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        String folded = result.fold(
                errors -> "failure: " + errors.size(),
                val -> "success"
        );
        
        assertEquals("failure: 1", folded);
    }
    
    @Test
    void combineShouldReturnSecondResultWhenBothAreSuccess() {
        Result<TestError, String> result1 = Result.success("first");
        Result<TestError, Integer> result2 = Result.success(42);
        
        Result<TestError, Integer> combined = result1.combine(result2);
        
        assertTrue(combined.isSuccess());
        assertEquals(42, combined.get());
    }
    
    @Test
    void combineShouldReturnFailureWhenFirstFails() {
        Result<TestError, String> result1 = Result.failure(new TestError("error1"));
        Result<TestError, Integer> result2 = Result.success(42);
        
        Result<TestError, Integer> combined = result1.combine(result2);
        
        assertTrue(combined.isFailure());
        assertEquals(1, combined.getErrors().size());
        assertEquals("error1", combined.getErrors().getFirst().message());
    }
    
    @Test
    void combineShouldReturnFailureWhenSecondFails() {
        Result<TestError, String> result1 = Result.success("value");
        Result<TestError, Integer> result2 = Result.failure(new TestError("error2"));
        
        Result<TestError, Integer> combined = result1.combine(result2);
        
        assertTrue(combined.isFailure());
        assertEquals(1, combined.getErrors().size());
        assertEquals("error2", combined.getErrors().getFirst().message());
    }
    
    @Test
    void combineShouldAccumulateErrorsWhenBothFail() {
        Result<TestError, String> result1 = Result.failure(new TestError("error1"));
        Result<TestError, Integer> result2 = Result.failure(new TestError("error2"));
        
        Result<TestError, Integer> combined = result1.combine(result2);
        
        assertTrue(combined.isFailure());
        assertEquals(2, combined.getErrors().size());
    }
    
    @Test
    void combineShouldWorkWithSupplier() {
        Result<TestError, String> result1 = Result.success("value");
        
        Result<TestError, Integer> combined = result1.combine(() -> Result.success(42));
        
        assertTrue(combined.isSuccess());
        assertEquals(42, combined.get());
    }
    
    @Test
    void mapToShouldTransformSuccessValue() {
        Result<TestError, String> result = Result.success("value");
        
        Result<TestError, Integer> mapped = result.mapTo(() -> 42);
        
        assertTrue(mapped.isSuccess());
        assertEquals(42, mapped.get());
    }
    
    @Test
    void mapToShouldNotExecuteSupplierForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        AtomicBoolean executed = new AtomicBoolean(false);
        
        Result<TestError, Integer> mapped = result.mapTo(() -> {
            executed.set(true);
            return 42;
        });
        
        assertTrue(mapped.isFailure());
        assertFalse(executed.get());
    }
    
    @Test
    void mapShouldTransformSuccessValue() {
        Result<TestError, String> result = Result.success("test");
        
        Result<TestError, Integer> mapped = result.map(String::length);
        
        assertTrue(mapped.isSuccess());
        assertEquals(4, mapped.get());
    }
    
    @Test
    void mapShouldPreserveFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        Result<TestError, Integer> mapped = result.map(String::length);
        
        assertTrue(mapped.isFailure());
        assertEquals(1, mapped.getErrors().size());
    }
    
    @Test
    void mapShouldNotExecuteFunctionForFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        AtomicBoolean executed = new AtomicBoolean(false);
        
        result.map(v -> {
            executed.set(true);
            return v.length();
        });
        
        assertFalse(executed.get());
    }
    
    @Test
    void flatMapShouldTransformAndFlattenSuccess() {
        Result<TestError, String> result = Result.success("test");
        
        Result<TestError, Integer> flatMapped = result.flatMap(
                v -> Result.success(v.length())
        );
        
        assertTrue(flatMapped.isSuccess());
        assertEquals(4, flatMapped.get());
    }
    
    @Test
    void flatMapShouldPropagateFailureFromMapper() {
        Result<TestError, String> result = Result.success("test");
        
        Result<TestError, Integer> flatMapped = result.flatMap(
                value -> Result.failure(new TestError("mapper error"))
        );
        
        assertTrue(flatMapped.isFailure());
        assertEquals("mapper error", flatMapped.getErrors().getFirst().message());
    }
    
    @Test
    void flatMapShouldPreserveOriginalFailure() {
        Result<TestError, String> result = Result.failure(new TestError("original error"));
        
        Result<TestError, Integer> flatMapped = result.flatMap(
                value -> Result.success(42)
        );
        
        assertTrue(flatMapped.isFailure());
        assertEquals("original error", flatMapped.getErrors().getFirst().message());
    }
    
    @Test
    void mustBeShouldKeepSuccessWhenPredicateIsTrue() {
        Result<TestError, Integer> result = Result.success(10);
        
        Result<TestError, Integer> checked = result.mustBe(v -> v > 5, new TestError("too small"));
        
        assertTrue(checked.isSuccess());
        assertEquals(10, checked.get());
    }
    
    @Test
    void mustBeShouldFailWhenPredicateIsFalse() {
        Result<TestError, Integer> result = Result.success(3);
        
        Result<TestError, Integer> checked = result.mustBe(v -> v > 5, new TestError("too small"));
        
        assertTrue(checked.isFailure());
        assertEquals("too small", checked.getErrors().getFirst().message());
    }
    
    @Test
    void mustBeShouldAddErrorToExistingFailureWithValue() {
        Result<TestError, Integer> result = Result.failure(new TestError("first error"), 3);
        
        Result<TestError, Integer> checked = result.mustBe(v -> v > 5, new TestError("second error"));
        
        assertTrue(checked.isFailure());
        assertEquals(2, checked.getErrors().size());
    }
    
    @Test
    void mustBeShouldNotAddErrorWhenFailureHasNoValue() {
        Result<TestError, Integer> result = Result.failure(new TestError("error"));
        
        Result<TestError, Integer> checked = result.mustBe(v -> v > 5, new TestError("additional"));
        
        assertTrue(checked.isFailure());
        assertEquals(1, checked.getErrors().size());
    }
    
    @Test
    void filterShouldKeepSuccessWhenPredicateIsTrue() {
        Result<TestError, Integer> result = Result.success(10);
        
        Result<TestError, Integer> filtered = result.filter(v -> v > 5, new TestError("filtered out"));
        
        assertTrue(filtered.isSuccess());
        assertEquals(10, filtered.get());
    }
    
    @Test
    void filterShouldFailWhenPredicateIsFalse() {
        Result<TestError, Integer> result = Result.success(3);
        
        Result<TestError, Integer> filtered = result.filter(v -> v > 5, new TestError("filtered out"));
        
        assertTrue(filtered.isFailure());
        assertEquals("filtered out", filtered.getErrors().getFirst().message());
    }
    
    @Test
    void filterShouldPreserveFailure() {
        Result<TestError, Integer> result = Result.failure(new TestError("original error"));
        
        Result<TestError, Integer> filtered = result.filter(v -> v > 5, new TestError("filter error"));
        
        assertTrue(filtered.isFailure());
        assertEquals(1, filtered.getErrors().size());
        assertEquals("original error", filtered.getErrors().getFirst().message());
    }
    
    @Test
    void toNothingShouldConvertSuccessToNothingSuccess() {
        Result<TestError, String> result = Result.success("value");
        
        Result<TestError, Nothing> nothing = result.toNothing();
        
        assertTrue(nothing.isSuccess());
        assertInstanceOf(Nothing.class, nothing.get());
    }
    
    @Test
    void toNothingShouldConvertFailureToNothingFailure() {
        Result<TestError, String> result = Result.failure(new TestError("error"));
        
        Result<TestError, Nothing> nothing = result.toNothing();
        
        assertTrue(nothing.isFailure());
        assertEquals(1, nothing.getErrors().size());
    }
    
    @Test
    void castShouldCastResultToAnotherErrorType() {
        Result<TestError, String> result = Result.success("value");
        
        Result<AnotherTestError, String> casted = result.cast();
        
        assertTrue(casted.isSuccess());
        assertEquals("value", casted.get());
    }
    
    @Test
    void successShouldWorkWithNullValue() {
        Result<TestError, String> result = Result.success(null);
        
        assertTrue(result.isSuccess());
        assertNull(result.get());
    }
    
    @Test
    void chainedOperationsShouldWorkCorrectly() {
        Result<TestError, Integer> result = Result.<TestError, Integer>success(5)
                                                    .filter(v -> v > 0, new TestError("negative"))
                                                    .map(v -> v * 2)
                                                    .flatMap(v -> Result.success(v + 10));
        
        assertTrue(result.isSuccess());
        assertEquals(20, result.get());
    }
    
    @Test
    void chainedOperationsShouldStopAtFirstFailure() {
        AtomicInteger mapCalls = new AtomicInteger(0);
        AtomicInteger flatMapCalls = new AtomicInteger(0);
        
        Result<TestError, Integer> result = Result.<TestError, Integer>success(5)
                                                    .filter(v -> v > 10, new TestError("too small"))
                                                    .map(v -> {
                                                        mapCalls.incrementAndGet();
                                                        return v * 2;
                                                    })
                                                    .flatMap(v -> {
                                                        flatMapCalls.incrementAndGet();
                                                        return Result.success(v + 10);
                                                    });
        
        assertTrue(result.isFailure());
        assertEquals(0, mapCalls.get());
        assertEquals(0, flatMapCalls.get());
    }
    
    @Test
    void multipleCombineShouldAccumulateAllErrors() {
        Result<TestError, Nothing> result1 = Result.failure(new TestError("error1"));
        Result<TestError, Nothing> result2 = Result.failure(new TestError("error2"));
        Result<TestError, Nothing> result3 = Result.failure(new TestError("error3"));
        
        Result<TestError, Nothing> combined = result1.combine(result2).combine(result3);
        
        assertTrue(combined.isFailure());
        assertEquals(3, combined.getErrors().size());
    }
    
    @Test
    void ifSuccessShouldReceiveCorrectValue() {
        Result<TestError, String> result = Result.success("test value");
        AtomicBoolean correctValue = new AtomicBoolean(false);
        
        result.ifSuccess(v -> {
            if ("test value".equals(v)) {
                correctValue.set(true);
            }
        });
        
        assertTrue(correctValue.get());
    }
    
    @Test
    void ifFailureShouldReceiveCorrectErrors() {
        TestError error1 = new TestError("error1");
        TestError error2 = new TestError("error2");
        Result<TestError, String> result = Result.failure(List.of(error1, error2));
        AtomicBoolean correctErrors = new AtomicBoolean(false);
        
        result.ifFailure(errors -> {
            if (errors.size() == 2 && errors.contains(error1) && errors.contains(error2)) {
                correctErrors.set(true);
            }
        });
        
        assertTrue(correctErrors.get());
    }
    
    private record TestError(String message) implements Error {
    }
    
    private record AnotherTestError(String message) implements Error {
    }
}





