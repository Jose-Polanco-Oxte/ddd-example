package com.toast.shared.app;

import com.toast.shared.domain.DomainEvent;

import java.util.List;

public interface EventBus {
    void publish(List<? extends DomainEvent> events);
}
