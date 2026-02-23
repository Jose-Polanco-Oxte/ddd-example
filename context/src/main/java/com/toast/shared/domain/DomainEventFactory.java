package com.toast.shared.domain;

import java.time.Instant;

/**
 * A factory interface for creating domain events from primitive values.
 * Each domain event should have a corresponding factory that uses this interface to create an instance of the domain event from primitive values.
 * This allows us to decouple the creation of domain events from the domain event classes themselves,
 * and to ensure that the creation of domain events is consistent across the application.
 */
@FunctionalInterface
public interface DomainEventFactory {
    DomainEvent fromPrimitives(String aggregateId, String eventId, Instant occurredOn, PrimitiveAttributes attributes);
}
