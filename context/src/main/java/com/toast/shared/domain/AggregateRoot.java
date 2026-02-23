package com.toast.shared.domain;

import com.toast.shared.domain.vo.DomainId;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {
    private final List<DomainEvent> domainEvents;
    
    protected AggregateRoot() {
        this.domainEvents = new ArrayList<>();
    }
    
    public abstract DomainId id();
    
    public List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(this.domainEvents);
        this.domainEvents.clear();
        return events;
    }
    
    public void recordDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    
    protected abstract PrimitiveAttributes toPrimitives();
}