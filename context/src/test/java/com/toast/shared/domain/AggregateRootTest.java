/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.shared.domain;

import com.toast.shared.domain.vo.DomainId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AggregateRootTest {
    @Test
    void constructorShouldInitializeEmptyEventsList() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(0, events.size());
    }
    
    @Test
    void recordDomainEventShouldAddEventToList() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event = new TestDomainEvent("aggregate-1");
        
        aggregate.recordDomainEvent(event);
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(1, events.size());
        assertEquals(event, events.getFirst());
    }
    
    @Test
    void recordDomainEventShouldAllowMultipleEvents() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event3 = new TestDomainEvent("aggregate-1");
        
        aggregate.recordDomainEvent(event1);
        aggregate.recordDomainEvent(event2);
        aggregate.recordDomainEvent(event3);
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(3, events.size());
    }
    
    @Test
    void recordDomainEventShouldMaintainOrder() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-2");
        TestDomainEvent event3 = new TestDomainEvent("aggregate-3");
        
        aggregate.recordDomainEvent(event1);
        aggregate.recordDomainEvent(event2);
        aggregate.recordDomainEvent(event3);
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(event1, events.get(0));
        assertEquals(event2, events.get(1));
        assertEquals(event3, events.get(2));
    }
    
    @Test
    void pullDomainEventsShouldReturnAllRecordedEvents() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-2");
        
        aggregate.recordDomainEvent(event1);
        aggregate.recordDomainEvent(event2);
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(2, events.size());
        assertTrue(events.contains(event1));
        assertTrue(events.contains(event2));
    }
    
    @Test
    void pullDomainEventsShouldClearEventsAfterPulling() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event = new TestDomainEvent("aggregate-1");
        aggregate.recordDomainEvent(event);
        
        aggregate.pullDomainEvents();
        List<DomainEvent> secondPull = aggregate.pullDomainEvents();
        
        assertEquals(0, secondPull.size());
    }
    
    @Test
    void pullDomainEventsShouldReturnImmutableList() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event = new TestDomainEvent("aggregate-1");
        aggregate.recordDomainEvent(event);
        
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertThrows(UnsupportedOperationException.class, events::clear);
    }
    
    @Test
    void pullDomainEventsShouldNotAffectInternalStateBeforeClearing() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-2");
        aggregate.recordDomainEvent(event1);
        aggregate.recordDomainEvent(event2);
        
        List<DomainEvent> events = aggregate.pullDomainEvents();
        aggregate.recordDomainEvent(new TestDomainEvent("aggregate-3"));
        
        assertEquals(2, events.size());
    }
    
    @Test
    void recordDomainEventAfterPullShouldAddNewEvent() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        aggregate.recordDomainEvent(event1);
        aggregate.pullDomainEvents();
        
        TestDomainEvent event2 = new TestDomainEvent("aggregate-2");
        aggregate.recordDomainEvent(event2);
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(1, events.size());
        assertEquals(event2, events.getFirst());
    }
    
    @Test
    void multiplePullsShouldReturnEmptyListsAfterFirstPull() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        aggregate.recordDomainEvent(new TestDomainEvent("aggregate-1"));
        
        aggregate.pullDomainEvents();
        List<DomainEvent> secondPull = aggregate.pullDomainEvents();
        List<DomainEvent> thirdPull = aggregate.pullDomainEvents();
        
        assertEquals(0, secondPull.size());
        assertEquals(0, thirdPull.size());
    }
    
    @Test
    void recordDomainEventShouldAcceptSameEventMultipleTimes() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event = new TestDomainEvent("aggregate-1");
        
        aggregate.recordDomainEvent(event);
        aggregate.recordDomainEvent(event);
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertEquals(2, events.size());
        assertEquals(event, events.get(0));
        assertEquals(event, events.get(1));
    }
    
    @Test
    void pullDomainEventsShouldReturnCopyOfEvents() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        TestDomainEvent event1 = new TestDomainEvent("aggregate-1");
        TestDomainEvent event2 = new TestDomainEvent("aggregate-2");
        aggregate.recordDomainEvent(event1);
        aggregate.recordDomainEvent(event2);
        
        List<DomainEvent> firstPull = aggregate.pullDomainEvents();
        aggregate.recordDomainEvent(event1);
        List<DomainEvent> secondPull = aggregate.pullDomainEvents();
        
        assertEquals(2, firstPull.size());
        assertEquals(1, secondPull.size());
    }
    
    @Test
    void toPrimitivesShouldReturnExpectedAttributes() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        
        PrimitiveAttributes primitives = aggregate.toPrimitives();
        
        assertNotNull(primitives);
        assertInstanceOf(TestPrimitives.class, primitives);
    }
    
    @Test
    void recordingEventsAndPullingMultipleTimesShouldWorkCorrectly() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        
        aggregate.recordDomainEvent(new TestDomainEvent("1"));
        aggregate.recordDomainEvent(new TestDomainEvent("2"));
        List<DomainEvent> firstPull = aggregate.pullDomainEvents();
        
        aggregate.recordDomainEvent(new TestDomainEvent("3"));
        List<DomainEvent> secondPull = aggregate.pullDomainEvents();
        
        aggregate.recordDomainEvent(new TestDomainEvent("4"));
        aggregate.recordDomainEvent(new TestDomainEvent("5"));
        List<DomainEvent> thirdPull = aggregate.pullDomainEvents();
        
        assertEquals(2, firstPull.size());
        assertEquals(1, secondPull.size());
        assertEquals(2, thirdPull.size());
    }
    
    @Test
    void pullDomainEventsOnNewAggregateShouldReturnEmptyList() {
        TestAggregateRoot aggregate = new TestAggregateRoot();
        
        List<DomainEvent> events = aggregate.pullDomainEvents();
        
        assertNotNull(events);
        assertEquals(0, events.size());
    }
    
    private static class TestAggregateRoot extends AggregateRoot {
        @Override
        public DomainId id() {
            return null;
        }
        
        @Override
        protected TestPrimitives toPrimitives() {
            return new TestPrimitives("test-data");
        }
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
        public PrimitiveAttributes toPrimitives() {
            return new TestPrimitives("event-data");
        }
    }
    
    private record TestPrimitives(String data) implements PrimitiveAttributes {
        @Override
        public Map<String, Object> toMap() {
            return Map.of("data", data);
        }
    }
}

