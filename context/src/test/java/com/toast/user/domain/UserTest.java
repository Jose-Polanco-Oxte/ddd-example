/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.domain;

import com.toast.shared.domain.DomainEvent;
import com.toast.user.domain.event.UserCreatedEvent;
import com.toast.user.domain.vo.Age;
import com.toast.user.domain.vo.Name;
import com.toast.user.domain.vo.UserId;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    @Test
    void createShouldReturnUserWithGeneratedId() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age age = Age.load(25);
        
        User user = User.create(id, name, age);
        
        assertNotNull(user);
        assertNotNull(user.id());
        assertEquals(name, user.name());
        assertEquals(age, user.age());
    }
    
    @Test
    void createShouldRecordUserCreatedEvent() {
        UserId id = UserId.generate();
        Name name = Name.load("Jane", "Smith");
        Age age = Age.load(30);
        
        User user = User.create(id, name, age);
        List<DomainEvent> events = user.pullDomainEvents();
        
        assertEquals(1, events.size());
        UserCreatedEvent event = assertInstanceOf(UserCreatedEvent.class, events.getFirst());
        assertEquals("UserCreated", event.eventName());
    }
    
    @Test
    void createShouldGenerateDifferentIdsForDifferentUsers() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age age = Age.load(25);
        
        User user1 = User.create(id, name, age);
        User user2 = User.create(id, name, age);
        
        assertNotEquals(user1.id(), user2.id());
    }
    
    @Test
    void loadShouldReturnUserWithProvidedId() {
        UserId id = UserId.fromUuid(UUID.randomUUID());
        Name name = Name.load("Alice", "Johnson");
        Age age = Age.load(28);
        
        User user = User.load(id, name, age);
        
        assertNotNull(user);
        assertEquals(id, user.id());
        assertEquals(name, user.name());
        assertEquals(age, user.age());
    }
    
    @Test
    void loadShouldNotRecordAnyEvents() {
        UserId id = UserId.fromUuid(UUID.randomUUID());
        Name name = Name.load("Bob", "Brown");
        Age age = Age.load(35);
        
        User user = User.load(id, name, age);
        List<DomainEvent> events = user.pullDomainEvents();
        
        assertEquals(0, events.size());
    }
    
    @Test
    void changeNameShouldUpdateName() {
        UserId id = UserId.generate();
        Name originalName = Name.load("John", "Doe");
        Name newName = Name.load("Jane", "Doe");
        Age age = Age.load(25);
        User user = User.create(id, originalName, age);
        user.pullDomainEvents();
        
        user.changeName(newName);
        
        assertEquals(newName, user.name());
    }
    
    @Test
    void changeNameShouldNotAffectId() {
        UserId id = UserId.generate();
        Name originalName = Name.load("John", "Doe");
        Name newName = Name.load("Jane", "Doe");
        Age age = Age.load(25);
        User user = User.create(id, originalName, age);
        UserId originalId = user.id();
        user.pullDomainEvents();
        
        user.changeName(newName);
        
        assertEquals(originalId, user.id());
    }
    
    @Test
    void changeNameShouldNotAffectAge() {
        UserId id = UserId.generate();
        Name originalName = Name.load("John", "Doe");
        Name newName = Name.load("Jane", "Doe");
        Age age = Age.load(25);
        User user = User.create(id, originalName, age);
        user.pullDomainEvents();
        
        user.changeName(newName);
        
        assertEquals(age, user.age());
    }
    
    @Test
    void changeAgeShouldUpdateAge() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age originalAge = Age.load(25);
        Age newAge = Age.load(26);
        User user = User.create(id, name, originalAge);
        user.pullDomainEvents();
        
        user.changeAge(newAge);
        
        assertEquals(newAge, user.age());
    }
    
    @Test
    void changeAgeShouldNotAffectId() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age originalAge = Age.load(25);
        Age newAge = Age.load(26);
        User user = User.create(id, name, originalAge);
        UserId originalId = user.id();
        user.pullDomainEvents();
        
        user.changeAge(newAge);
        
        assertEquals(originalId, user.id());
    }
    
    @Test
    void changeAgeShouldNotAffectName() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age originalAge = Age.load(25);
        Age newAge = Age.load(26);
        User user = User.create(id, name, originalAge);
        user.pullDomainEvents();
        
        user.changeAge(newAge);
        
        assertEquals(name, user.name());
    }
    
    @Test
    void pullDomainEventsShouldReturnRecordedEvents() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age age = Age.load(25);
        User user = User.create(id, name, age);
        
        List<DomainEvent> events = user.pullDomainEvents();
        
        assertEquals(1, events.size());
        assertInstanceOf(UserCreatedEvent.class, events.getFirst());
    }
    
    @Test
    void pullDomainEventsShouldClearEventsAfterPulling() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age age = Age.load(25);
        User user = User.create(id, name, age);
        
        user.pullDomainEvents();
        List<DomainEvent> secondPull = user.pullDomainEvents();
        
        assertEquals(0, secondPull.size());
    }
    
    @Test
    void pullDomainEventsShouldReturnImmutableList() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age age = Age.load(25);
        User user = User.create(id, name, age);
        
        List<DomainEvent> events = user.pullDomainEvents();
        
        assertThrows(UnsupportedOperationException.class, events::clear);
    }
    
    @Test
    void toPrimitivesShouldReturnUserPrimitives() {
        UserId id = UserId.fromString("00000000-0000-0000-0000-000000000001");
        Name name = Name.load("John", "Doe");
        Age age = Age.load(25);
        User user = User.load(id, name, age);
        
        UserPrimitives primitives = user.toPrimitives();
        
        assertNotNull(primitives);
        assertEquals("00000000-0000-0000-0000-000000000001", primitives.userId());
        assertEquals("John", primitives.firstName());
        assertEquals("Doe", primitives.lastName());
        assertEquals(25, primitives.age());
    }
    
    @Test
    void multipleDifferentOperationsShouldWorkCorrectly() {
        UserId id = UserId.generate();
        Name originalName = Name.load("John", "Doe");
        Age originalAge = Age.load(25);
        User user = User.create(id, originalName, originalAge);
        user.pullDomainEvents();
        
        Name newName = Name.load("Jane", "Smith");
        Age newAge = Age.load(30);
        user.changeName(newName);
        user.changeAge(newAge);
        
        assertEquals(newName, user.name());
        assertEquals(newAge, user.age());
        assertNotNull(user.id());
    }
    
    @Test
    void createWithDifferentAgesShouldReturnCorrectUser() {
        UserId id = UserId.generate();
        UserId otherId = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age youngAge = Age.load(18);
        Age oldAge = Age.load(130);
        
        User youngUser = User.create(id, name, youngAge);
        User oldUser = User.create(otherId, name, oldAge);
        
        assertEquals(youngAge, youngUser.age());
        assertEquals(oldAge, oldUser.age());
    }
    
    @Test
    void changeNameMultipleTimesShouldRetainLatestValue() {
        UserId id = UserId.generate();
        Name name1 = Name.load("John", "Doe");
        Name name2 = Name.load("Jane", "Doe");
        Name name3 = Name.load("Jack", "Smith");
        Age age = Age.load(25);
        User user = User.create(id, name1, age);
        user.pullDomainEvents();
        
        user.changeName(name2);
        user.changeName(name3);
        
        assertEquals(name3, user.name());
    }
    
    @Test
    void changeAgeMultipleTimesShouldRetainLatestValue() {
        UserId id = UserId.generate();
        Name name = Name.load("John", "Doe");
        Age age1 = Age.load(25);
        Age age2 = Age.load(26);
        Age age3 = Age.load(27);
        User user = User.create(id, name, age1);
        user.pullDomainEvents();
        
        user.changeAge(age2);
        user.changeAge(age3);
        
        assertEquals(age3, user.age());
    }
}


