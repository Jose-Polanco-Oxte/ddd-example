/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.listener;

import com.toast.user.domain.event.UserCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SendWelcomeEmailListener {
    
    @EventListener
    @Async("commandTaskExecutor")
    public void on(UserCreatedEvent event) {
        System.out.println("[" + Thread.currentThread().getName() + "] Sending Email...");
    }
}
