/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.bus;

import com.toast.shared.app.Command;
import com.toast.shared.app.CommandBus;
import com.toast.shared.app.CommandHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

@Service
public class SpringCommandBus implements CommandBus {
    private static final String HANDLER_KEY_PREFIX = "handler:";
    private final Map<String, CommandHandler<?>> handlers = new ConcurrentHashMap<>();
    private final Executor commandTaskExecutor;
    
    public SpringCommandBus(ApplicationContext applicationContext, @Qualifier("commandTaskExecutor") Executor commandTaskExecutor) {
        this.commandTaskExecutor = commandTaskExecutor;
        applicationContext.getBeansOfType(CommandHandler.class).values().forEach(handler -> {
            Class<? extends Command> commandClass = getCommandClass(handler);
            this.handlers.put(computeHandlerKey(commandClass), handler);
        });
    }
    
    private Class<? extends Command> getCommandClass(CommandHandler<?> handler) {
        @SuppressWarnings("unchecked")
        Class<? extends Command> clazz = (Class<? extends Command>) ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return clazz;
    }
    
    private String computeHandlerKey(Class<? extends Command> command) {
        return HANDLER_KEY_PREFIX + command.getName() + ":" + command.getPackageName();
    }
    
    private void execute(Command command) {
        @SuppressWarnings("unchecked")
        CommandHandler<Command> handler = (CommandHandler<Command>) handlers.get(computeHandlerKey(command.getClass()));
        if (handler == null) {
            throw new RuntimeException("No handler registered for command: " + command.getClass().getName());
        }
        handler.handle(command);
    }
    
    @Override
    @Async("commandTaskExecutor")
    public void dispatch(Command command) {
        execute(command);
    }
    
    @Override
    public CompletableFuture<Void> awaitDispatch(Command command) {
        return CompletableFuture.runAsync(() -> execute(command), commandTaskExecutor);
    }
}
