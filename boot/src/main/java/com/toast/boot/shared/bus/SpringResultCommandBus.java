/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.bus;

import com.toast.shared.app.Command;
import com.toast.shared.app.ResultCommandBus;
import com.toast.shared.app.ResultCommandHandler;
import com.toast.shared.domain.types.Error;
import com.toast.shared.domain.types.Nothing;
import com.toast.shared.domain.types.result.Result;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SpringResultCommandBus implements ResultCommandBus {
    private static final String HANDLER_KEY_PREFIX = "handler:";
    private final Map<String, ResultCommandHandler<?>> handlers = new ConcurrentHashMap<>();
    
    public SpringResultCommandBus(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(ResultCommandHandler.class).values().forEach(handler -> {
            Class<? extends Command> commandClass = getCommandClass(handler);
            handlers.put(computeHandlerKey(commandClass), handler);
        });
    }
    
    private Class<? extends Command> getCommandClass(ResultCommandHandler<?> handler) {
        @SuppressWarnings("unchecked")
        Class<? extends Command> clazz = (Class<? extends Command>) ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return clazz;
    }
    
    private String computeHandlerKey(Class<? extends Command> command) {
        return HANDLER_KEY_PREFIX + command.getName() + ":" + command.getPackageName();
    }
    
    @Override
    public Result<? extends Error, Nothing> dispatch(Command command) {
        @SuppressWarnings("unchecked")
        ResultCommandHandler<Command> handler = (ResultCommandHandler<Command>) handlers.get(computeHandlerKey(command.getClass()));
        if (handler == null) {
            throw new RuntimeException("No handler registered for command: " + command.getClass().getName());
        }
        return handler.handle(command);
    }
}
