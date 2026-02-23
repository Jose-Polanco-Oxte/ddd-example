/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DomainEventTest {
    @Test
    void constructorShouldSetAggregateId() {
        String aggregateId = "aggregate-123";
        
        TestDomainEvent event = new TestDomainEvent(aggregateId);
        
        assertEquals(aggregateId, event.aggregateId());
    }
    
    @Test
    void constructorShouldGenerateUniqueEventId() {
        String aggregateId = "aggregate-123";
        
        TestDomainEvent event1 = new TestDomainEvent(aggregateId);
        TestDomainEvent event2 = new TestDomainEvent(aggregateId);
        
        assertNotNull(event1.eventId());
        assertNotNull(event2.eventId());
        assertNotEquals(event1.eventId(), event2.eventId());
    }
    
    @Test
    void constructorShouldSetOccurredOnToCurrentTime() {
        Instant before = Instant.now();
        
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        Instant after = Instant.now();
        assertNotNull(event.occurredOn());
        assertTrue(event.occurredOn().isAfter(before.minus(1, ChronoUnit.SECONDS)));
        assertTrue(event.occurredOn().isBefore(after.plus(1, ChronoUnit.SECONDS)));
    }
    
    @Test
    void eventIdShouldReturnGeneratedId() {
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        String eventId = event.eventId();
        
        assertNotNull(eventId);
        assertFalse(eventId.isEmpty());
    }
    
    @Test
    void aggregateIdShouldReturnProvidedValue() {
        String expectedAggregateId = "custom-aggregate-id";
        
        TestDomainEvent event = new TestDomainEvent(expectedAggregateId);
        
        assertEquals(expectedAggregateId, event.aggregateId());
    }
    
    @Test
    void occurredOnShouldReturnEventTimestamp() {
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        Instant occurredOn = event.occurredOn();
        
        assertNotNull(occurredOn);
        assertTrue(occurredOn.isBefore(Instant.now().plus(1, ChronoUnit.SECONDS)));
    }
    
    @Test
    void eventNameShouldReturnCorrectName() {
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        assertEquals("TestEvent", event.eventName());
    }
    
    @Test
    void toPrimitivesShouldReturnEventAttributes() {
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        PrimitiveAttributes attributes = event.toPrimitives();
        
        assertNotNull(attributes);
        assertInstanceOf(TestAttributes.class, attributes);
    }
    
    @Test
    void multipleEventsShouldHaveDifferentEventIds() {
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event3 = new TestDomainEvent("aggregate-1");
        
        assertNotEquals(event1.eventId(), event2.eventId());
        assertNotEquals(event2.eventId(), event3.eventId());
        assertNotEquals(event1.eventId(), event3.eventId());
    }
    
    @Test
    void eventsShouldHaveTimestampsInChronologicalOrder() throws InterruptedException {
        TestDomainEvent event1 = new TestDomainEvent("aggregate-123");
        Thread.sleep(2);
        TestDomainEvent event2 = new TestDomainEvent("aggregate-123");
        
        assertTrue(event1.occurredOn().isBefore(event2.occurredOn()) ||
                           event1.occurredOn().equals(event2.occurredOn()));
    }
    
    @Test
    void eventsWithDifferentAggregateIdsShouldHaveDifferentIds() {
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-2");
        
        assertNotEquals(event1.aggregateId(), event2.aggregateId());
        assertNotEquals(event1.eventId(), event2.eventId());
    }
    
    @Test
    void eventIdShouldBeValidUuidFormat() {
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        String eventId = event.eventId();
        
        assertNotNull(eventId);
        assertTrue(eventId.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
    }
    
    @Test
    void constructorShouldAcceptEmptyAggregateId() {
        TestDomainEvent event = new TestDomainEvent("");
        
        assertEquals("", event.aggregateId());
        assertNotNull(event.eventId());
        assertNotNull(event.occurredOn());
    }
    
    @Test
    void constructorShouldAcceptLongAggregateId() {
        String longAggregateId = "a".repeat(1000);
        
        TestDomainEvent event = new TestDomainEvent(longAggregateId);
        
        assertEquals(longAggregateId, event.aggregateId());
    }
    
    @Test
    void toPrimitivesShouldReturnAttributesWithExpectedData() {
        TestDomainEvent event = new TestDomainEvent("aggregate-123");
        
        TestAttributes attributes = event.toPrimitives();
        Map<String, Object> map = attributes.toMap();
        
        assertEquals("test-data", map.get("data"));
    }
    
    private static class TestDomainEvent extends DomainEvent {
        protected TestDomainEvent(String aggregateId) {
            super(aggregateId);
        }
        
        @Override
        public String eventName() {
            return "TestEvent";
        }
        
        @Override
        public TestAttributes toPrimitives() {
            return new TestAttributes("test-data");
        }
    }
    
    private record TestAttributes(String data) implements PrimitiveAttributes {
        @Override
        public Map<String, Object> toMap() {
            return Map.of("data", data);
        }
    }
}

