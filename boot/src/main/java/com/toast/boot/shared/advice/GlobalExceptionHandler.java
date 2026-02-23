/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.advice;

import com.toast.boot.shared.data.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleGeneral(Exception ex) {
        log.error("An unexpected error occurred", ex);
        return ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred. Please try again later.", null);
    }
}
