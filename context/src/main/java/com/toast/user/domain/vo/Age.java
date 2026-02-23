package com.toast.user.domain.vo;

import com.toast.shared.domain.types.result.Result;
import com.toast.shared.domain.vo.ValueObject;
import com.toast.user.domain.UserError;

public final class Age extends ValueObject<Integer> {
    Age(Integer value) {
        super(value);
    }
    
    public static Result<UserError, Age> of(int value) {
        return Result.<UserError, Integer>check(value)
                       .filter(Age::isNotNegative, new UserError.InvalidAge("Age cannot be negative"))
                       .filter(Age::isValidAge, new UserError.InvalidAge("Age cannot be greater than 130"))
                       .mapTo(() -> new Age(value));
    }
    
    public static Age load(int value) {
        return of(value).orElseThrow(() -> new IllegalArgumentException("Invalid age"));
    }
    
    private static boolean isNotNegative(int value) {
        return value > 0;
    }
    
    private static boolean isValidAge(int value) {
        return value <= 130;
    }
}
