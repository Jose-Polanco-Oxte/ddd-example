package com.toast.user.domain.event;

import com.toast.shared.domain.DomainEvent;
import com.toast.shared.domain.PrimitiveAttributes;
import com.toast.user.domain.vo.Name;
import com.toast.user.domain.vo.UserId;

import java.util.Map;
import java.util.Objects;

public class UserCreatedEvent extends DomainEvent {
    private final Name name;
    
    public UserCreatedEvent(UserId aggregateId, Name name) {
        super(aggregateId.toString());
        this.name = name;
    }
    
    @Override
    public String eventName() {
        return "UserCreated";
    }
    
    @Override
    public UserCreatedAttributes toPrimitives() {
        return new UserCreatedAttributes(name.firstName(), name.lastName());
    }
    
    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                       "name=" + name +
                       super.toString() +
                       '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserCreatedEvent that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(name, that.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
    
    public record UserCreatedAttributes(String firstName, String lastName) implements PrimitiveAttributes {
        @Override
        public Map<String, Object> toMap() {
            return Map.of(
                    "firstName", firstName,
                    "lastName", lastName
            );
        }
    }
}
