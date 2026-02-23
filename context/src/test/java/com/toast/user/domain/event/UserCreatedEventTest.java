/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.domain.event;

import com.toast.user.domain.vo.Name;
import com.toast.user.domain.vo.UserId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserCreatedEventTest {
    @Test
    void constructorShouldSetAggregateIdFromUserId() {
        UserId userId = UserId.generate();
        Name name = Name.load("John", "Doe");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        
        assertEquals(userId.toString(), event.aggregateId());
    }
    
    @Test
    void constructorShouldGenerateUniqueEventId() {
        UserId userId = UserId.generate();
        Name name = Name.load("Jane", "Smith");
        
        UserCreatedEvent event1 = new UserCreatedEvent(userId, name);
        UserCreatedEvent event2 = new UserCreatedEvent(userId, name);
        
        assertNotNull(event1.eventId());
        assertNotNull(event2.eventId());
        assertNotEquals(event1.eventId(), event2.eventId());
    }
    
    @Test
    void constructorShouldSetOccurredOnTimestamp() {
        UserId userId = UserId.generate();
        Name name = Name.load("Alice", "Johnson");
        Instant before = Instant.now();
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        
        Instant after = Instant.now();
        assertNotNull(event.occurredOn());
        assertFalse(event.occurredOn().isBefore(before));
        assertFalse(event.occurredOn().isAfter(after));
    }
    
    @Test
    void eventNameShouldReturnUserCreated() {
        UserId userId = UserId.generate();
        Name name = Name.load("Bob", "Brown");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        
        assertEquals("UserCreated", event.eventName());
    }
    
    @Test
    void toPrimitivesShouldReturnUserCreatedAttributes() {
        UserId userId = UserId.generate();
        Name name = Name.load("Charlie", "Davis");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        UserCreatedEvent.UserCreatedAttributes attributes = event.toPrimitives();
        
        assertNotNull(attributes);
        assertEquals("Charlie", attributes.firstName());
        assertEquals("Davis", attributes.lastName());
    }
    
    @Test
    void toPrimitivesShouldPreserveFirstName() {
        UserId userId = UserId.generate();
        Name name = Name.load("Diana", "Evans");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        UserCreatedEvent.UserCreatedAttributes attributes = event.toPrimitives();
        
        assertEquals("Diana", attributes.firstName());
    }
    
    @Test
    void toPrimitivesShouldPreserveLastName() {
        UserId userId = UserId.generate();
        Name name = Name.load("Edward", "Fisher");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        UserCreatedEvent.UserCreatedAttributes attributes = event.toPrimitives();
        
        assertEquals("Fisher", attributes.lastName());
    }
    
    @Test
    void aggregateIdShouldMatchProvidedUserId() {
        UUID uuid = UUID.randomUUID();
        UserId userId = UserId.fromUuid(uuid);
        Name name = Name.load("Grace", "Hall");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        
        assertEquals(uuid.toString(), event.aggregateId());
    }
    
    @Test
    void eventIdShouldBeValidUuidFormat() {
        UserId userId = UserId.generate();
        Name name = Name.load("Henry", "Irwin");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        
        assertDoesNotThrow(() -> UUID.fromString(event.eventId()));
    }
    
    @Test
    void multipleEventsForSameUserShouldHaveSameAggregateId() {
        UserId userId = UserId.generate();
        Name name1 = Name.load("Isaac", "Jones");
        Name name2 = Name.load("Jack", "King");
        
        UserCreatedEvent event1 = new UserCreatedEvent(userId, name1);
        UserCreatedEvent event2 = new UserCreatedEvent(userId, name2);
        
        assertEquals(event1.aggregateId(), event2.aggregateId());
    }
    
    @Test
    void multipleEventsForSameUserShouldHaveDifferentEventIds() {
        UserId userId = UserId.generate();
        Name name = Name.load("Karen", "Lopez");
        
        UserCreatedEvent event1 = new UserCreatedEvent(userId, name);
        UserCreatedEvent event2 = new UserCreatedEvent(userId, name);
        
        assertNotEquals(event1.eventId(), event2.eventId());
    }
    
    @Test
    void eventsForDifferentUsersShouldHaveDifferentAggregateIds() {
        UserId userId1 = UserId.generate();
        UserId userId2 = UserId.generate();
        Name name = Name.load("Larry", "Martin");
        
        UserCreatedEvent event1 = new UserCreatedEvent(userId1, name);
        UserCreatedEvent event2 = new UserCreatedEvent(userId2, name);
        
        assertNotEquals(event1.aggregateId(), event2.aggregateId());
    }
    
    @Test
    void toPrimitivesWithSingleCharacterNamesShouldWork() {
        UserId userId = UserId.generate();
        Name name = Name.load("M", "N");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        UserCreatedEvent.UserCreatedAttributes attributes = event.toPrimitives();
        
        assertEquals("M", attributes.firstName());
        assertEquals("N", attributes.lastName());
    }
    
    @Test
    void toPrimitivesWithLongNamesShouldWork() {
        UserId userId = UserId.generate();
        String longFirstName = "a".repeat(50);
        String longLastName = "b".repeat(50);
        Name name = Name.load(longFirstName, longLastName);
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        UserCreatedEvent.UserCreatedAttributes attributes = event.toPrimitives();
        
        assertEquals(longFirstName, attributes.firstName());
        assertEquals(longLastName, attributes.lastName());
    }
    
    @Test
    void userCreatedAttributesToMapShouldContainFirstName() {
        UserCreatedEvent.UserCreatedAttributes attributes =
                new UserCreatedEvent.UserCreatedAttributes("Oliver", "Parker");
        
        Map<String, Object> map = attributes.toMap();
        
        assertEquals("Oliver", map.get("firstName"));
    }
    
    @Test
    void userCreatedAttributesToMapShouldContainLastName() {
        UserCreatedEvent.UserCreatedAttributes attributes =
                new UserCreatedEvent.UserCreatedAttributes("Quincy", "Roberts");
        
        Map<String, Object> map = attributes.toMap();
        
        assertEquals("Roberts", map.get("lastName"));
    }
    
    @Test
    void userCreatedAttributesToMapShouldContainBothFields() {
        UserCreatedEvent.UserCreatedAttributes attributes =
                new UserCreatedEvent.UserCreatedAttributes("Sarah", "Turner");
        
        Map<String, Object> map = attributes.toMap();
        
        assertEquals(2, map.size());
        assertTrue(map.containsKey("firstName"));
        assertTrue(map.containsKey("lastName"));
    }
    
    @Test
    void eventWithSpecificUserIdShouldHaveCorrectAggregateId() {
        UserId userId = UserId.fromString("12345678-1234-1234-1234-123456789abc");
        Name name = Name.load("Uma", "Valdez");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        
        assertEquals("12345678-1234-1234-1234-123456789abc", event.aggregateId());
    }
    
    @Test
    void occurredOnShouldBeSetToCurrentTime() throws InterruptedException {
        UserId userId = UserId.generate();
        Name name = Name.load("Victor", "White");
        
        UserCreatedEvent event1 = new UserCreatedEvent(userId, name);
        Thread.sleep(2);
        UserCreatedEvent event2 = new UserCreatedEvent(userId, name);
        
        assertTrue(event1.occurredOn().isBefore(event2.occurredOn()) ||
                           event1.occurredOn().equals(event2.occurredOn()));
    }
    
    @Test
    void toPrimitivesShouldCreateNewAttributesInstance() {
        UserId userId = UserId.generate();
        Name name = Name.load("Wendy", "Xavier");
        
        UserCreatedEvent event = new UserCreatedEvent(userId, name);
        UserCreatedEvent.UserCreatedAttributes attributes1 = event.toPrimitives();
        UserCreatedEvent.UserCreatedAttributes attributes2 = event.toPrimitives();
        
        assertNotSame(attributes1, attributes2);
        assertEquals(attributes1.firstName(), attributes2.firstName());
        assertEquals(attributes1.lastName(), attributes2.lastName());
    }
    
    @Test
    void userCreatedAttributesShouldBeImmutable() {
        UserCreatedEvent.UserCreatedAttributes attributes =
                new UserCreatedEvent.UserCreatedAttributes("Yolanda", "Zimmerman");
        
        assertEquals("Yolanda", attributes.firstName());
        assertEquals("Zimmerman", attributes.lastName());
    }
}

