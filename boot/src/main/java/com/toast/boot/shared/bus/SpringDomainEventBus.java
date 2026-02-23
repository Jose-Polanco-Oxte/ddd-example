/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.bus;

import com.toast.shared.app.EventBus;
import com.toast.shared.domain.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringDomainEventBus implements EventBus {
    private final ApplicationEventPublisher publisher;
    
    public SpringDomainEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    @Override
    public void publish(List<? extends DomainEvent> events) {
        events.forEach(publisher::publishEvent);
    }
}
