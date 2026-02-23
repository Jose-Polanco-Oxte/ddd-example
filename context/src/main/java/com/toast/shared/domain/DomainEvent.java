package com.toast.shared.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public abstract class DomainEvent {
    private final String aggregateId;
    private final String eventId;
    private final Instant occurredOn;
    
    protected DomainEvent(String aggregateId) {
        this.aggregateId = aggregateId;
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
    }
    
    public String aggregateId() {
        return aggregateId;
    }
    
    public String eventId() {
        return eventId;
    }
    
    public Instant occurredOn() {
        return occurredOn;
    }
    
    public abstract String eventName();
    
    public abstract PrimitiveAttributes toPrimitives();
    
    @Override
    public String toString() {
        return "DomainEvent{" +
                       "aggregateId='" + aggregateId + '\'' +
                       ", eventId='" + eventId + '\'' +
                       ", occurredOn=" + occurredOn +
                       '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DomainEvent that)) return false;
        return Objects.equals(aggregateId, that.aggregateId) && Objects.equals(eventId, that.eventId) && Objects.equals(occurredOn, that.occurredOn);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(aggregateId, eventId, occurredOn);
    }
}

