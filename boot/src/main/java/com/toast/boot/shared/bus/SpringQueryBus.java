/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.bus;

import com.toast.shared.app.Query;
import com.toast.shared.app.QueryBus;
import com.toast.shared.app.QueryHandler;
import com.toast.shared.app.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SpringQueryBus implements QueryBus {
    private static final String HANDLER_KEY_PREFIX = "query-handler:";
    private final Map<String, QueryHandler<?, ?>> handlers = new ConcurrentHashMap<>();
    
    public SpringQueryBus(ApplicationContext applicationContext) {
        applicationContext.getBeansOfType(QueryHandler.class).values().forEach(handler -> {
            Class<? extends Query> queryClass = getQueryClass(handler);
            this.handlers.put(computeHandlerKey(queryClass), handler);
        });
    }
    
    private Class<? extends Query> getQueryClass(QueryHandler<?, ?> handler) {
        @SuppressWarnings("unchecked")
        Class<? extends Query> clazz = (Class<? extends Query>) ((ParameterizedType) handler.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
        return clazz;
    }
    
    private String computeHandlerKey(Class<? extends Query> query) {
        return HANDLER_KEY_PREFIX + query.getName() + ":" + query.getPackageName();
    }
    
    @Override
    public <R extends Response> R ask(Query query) {
        @SuppressWarnings("unchecked")
        QueryHandler<Query, R> handler = (QueryHandler<Query, R>) handlers.get(computeHandlerKey(query.getClass()));
        if (handler == null) {
            throw new RuntimeException("No handler registered for query: " + query.getClass().getName());
        }
        return handler.handle(query);
    }
}
