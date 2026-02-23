/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.security;

import com.toast.shared.app.AuditableCommand;
import com.toast.shared.app.Command;
import com.toast.shared.app.CommandBus;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public final class SecureCommandBus {
    private final CommandBus commandBus;
    private final SecurityContext securityContext;
    
    public SecureCommandBus(CommandBus commandBus, SecurityContext securityContext) {
        this.commandBus = commandBus;
        this.securityContext = securityContext;
    }
    
    public <ID> void dispatch(AuditableCommand<ID> command) throws SecurityException {
        SecureCommand secureAction = createSecureAction(signCommand(command));
        System.out.println("Dispatching secure command: " + secureAction);
        System.out.println(secureAction.trace());
        // Here you can add additional security checks or logging if needed
        commandBus.dispatch(secureAction.command());
    }
    
    public void dispatch(Command command) throws SecurityException {
        SecureCommand secureAction = createSecureAction(command);
        System.out.println("Dispatching secure command: " + secureAction);
        System.out.println(secureAction.trace());
        // Here you can add additional security checks or logging if needed
        commandBus.dispatch(secureAction.command());
    }
    
    private <ID> AuditableCommand<ID> signCommand(AuditableCommand<ID> command) {
        Principal principal = securityContext.getPrincipal();
        return command.sign(principal.getIdentifier());
    }
    
    private SecureCommand createSecureAction(Command command) throws SecurityException {
        Principal principal = securityContext.getPrincipal();
        return new SecureCommand(command, principal, Instant.now());
    }
    
    public CompletableFuture<Void> awaitDispatch(Command command) throws SecurityException {
        SecureCommand secureAction = createSecureAction(command);
        System.out.println("Awaiting dispatch of secure command: " + secureAction);
        System.out.println(secureAction.trace());
        // Here you can add additional security checks or logging if needed
        return commandBus.awaitDispatch(secureAction.command());
    }
}
