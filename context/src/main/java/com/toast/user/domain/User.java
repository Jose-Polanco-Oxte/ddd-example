/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.domain;

import com.toast.shared.domain.AggregateRoot;
import com.toast.user.domain.event.UserCreatedEvent;
import com.toast.user.domain.vo.Age;
import com.toast.user.domain.vo.Name;
import com.toast.user.domain.vo.UserId;

import java.util.Objects;

public final class User extends AggregateRoot {
    private final UserId id;
    private Name name;
    private Age age;
    
    private User(UserId id, Name name, Age age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    
    public static User create(UserId id, Name name, Age age) {
        User user = new User(id, name, age);
        user.recordDomainEvent(new UserCreatedEvent(id, name));
        return user;
    }
    
    public static User load(UserId id, Name name, Age age) {
        return new User(id, name, age);
    }
    
    public UserId id() {
        return id;
    }
    
    public Name name() {
        return name;
    }
    
    public Age age() {
        return age;
    }
    
    public void changeName(Name newName) {
        this.name = newName;
    }
    
    public void changeAge(Age newAge) {
        this.age = newAge;
    }
    
    @Override
    protected UserPrimitives toPrimitives() {
        return new UserPrimitives(
                id.toString(),
                name.firstName(),
                name.lastName(),
                age.value()
        );
    }
    
    @Override
    public String toString() {
        return "User{" +
                       "id=" + id +
                       ", name=" + name +
                       ", age=" + age +
                       '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(age, user.age);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name, age);
    }
}
