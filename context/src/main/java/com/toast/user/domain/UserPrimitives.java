package com.toast.user.domain;

import com.toast.shared.domain.PrimitiveAttributes;

import java.util.Map;

public record UserPrimitives(
        String userId,
        String firstName,
        String lastName,
        int age
) implements PrimitiveAttributes {
    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "userId", userId,
                "firstName", firstName,
                "lastName", lastName,
                "age", age
        );
    }
}
