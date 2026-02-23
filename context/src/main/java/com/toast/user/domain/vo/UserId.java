package com.toast.user.domain.vo;

import com.toast.shared.domain.vo.DomainId;

import java.util.UUID;

public class UserId extends DomainId {
    protected UserId(UUID value) {
        super(value);
    }
    
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }
    
    public static UserId fromString(String value) {
        return new UserId(UUID.fromString(value));
    }
    
    public static UserId fromUuid(UUID value) {
        return new UserId(value);
    }
}
