/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error
) {
    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(new ApiResponse<>(true, data, null));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> accepted(T data) {
        return ResponseEntity.accepted().body(new ApiResponse<>(true, data, null));
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus status) {
        return ResponseEntity.status(status).body(new ApiResponse<>(true, data, null));
    }
    
    public static <T> ApiResponse<T> failure(String message, List<ApiError.ApiErrorDetail> details) {
        ApiError error = new ApiError(details, message, String.valueOf(System.currentTimeMillis()));
        return new ApiResponse<>(false, null, error);
    }
    
    public static <T> ResponseEntity<ApiResponse<T>> failure(HttpStatus status, String message, List<ApiError.ApiErrorDetail> details) {
        ApiError error = new ApiError(details, message, String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.status(status).body(new ApiResponse<>(false, null, error));
    }
    
    public static ApiError.ApiErrorDetail errorDetail(String code, String field, String message) {
        return new ApiError.ApiErrorDetail(code, field, message);
    }
}
