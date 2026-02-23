package com.toast.shared.domain.vo;

import java.util.Objects;
import java.util.UUID;

public abstract class DomainId extends ValueObject<UUID> {
    protected DomainId(UUID value) {
        Objects.requireNonNull(value, "DomainId value cannot be null");
        super(value);
    }
}
