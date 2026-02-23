package com.toast.user.domain;

import com.toast.shared.domain.types.Error;

public sealed interface UserError extends Error {
    record InvalidAge(String reason) implements UserError {
        @Override
        public String message() {
            return "Invalid age: " + reason;
        }
    }
    
    record InvalidName(String reason) implements UserError {
        @Override
        public String message() {
            return "Invalid name: " + reason;
        }
    }
}