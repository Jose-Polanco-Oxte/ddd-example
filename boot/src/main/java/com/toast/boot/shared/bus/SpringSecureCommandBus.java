/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.bus;

import org.springframework.stereotype.Service;

@Service
public class SpringSecureCommandBus {
//    private static final String HANDLER_KEY_PREFIX = "handler:";
//    private final Map<String, CommandHandler<?>> handlers = new ConcurrentHashMap<>();
//    private final Executor commandTaskExecutor;
//
//
//    public SpringSecureCommandBus(
//            ApplicationContext applicationContext,
//            @Qualifier("commandTaskExecutor")
//            Executor commandTaskExecutor,
//            SecurityContext securityContext
//    ) {
//        super(securityContext);
//        this.commandTaskExecutor = commandTaskExecutor;
//        applicationContext.getBeansOfType(CommandHandler.class).values().forEach(handler -> {
//            Class<? extends Command> commandClass = getCommandClass(handler);
//            this.handlers.put(computeHandlerKey(commandClass), handler);
//        });
//    }
//
//    private Class<? extends Command> getCommandClass(CommandHandler<?> handler) {
//        @SuppressWarnings("unchecked")
//        Class<? extends Command> clazz = (Class<? extends Command>) ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
//        return clazz;
//    }
//
//    private String computeHandlerKey(Class<? extends Command> command) {
//        return HANDLER_KEY_PREFIX + command.getName() + ":" + command.getPackageName();
//    }
//
//    @Override
//    @Async("commandTaskExecutor")
//    protected void run(SecureCommand action) {
//        @SuppressWarnings("unchecked")
//        CommandHandler<Command> handler = (CommandHandler<Command>) handlers.get(computeHandlerKey(action.command().getClass()));
//        if (handler == null) {
//            throw new RuntimeException("No handler registered for command: " + action.command().getClass().getName());
//        }
//        handler.handle(action.command());
//    }
//
//    @Override
//    public CompletableFuture<Void> awaitDispatch(Command command) throws SecurityException {
//        SecureCommand secureAction = createSecureAction(command);
//        return CompletableFuture.runAsync(() -> run(secureAction), commandTaskExecutor);
//    }
}
