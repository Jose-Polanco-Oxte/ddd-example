/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.controller;

import com.toast.boot.user.data.request.CreateAccountRequest;
import com.toast.boot.user.data.request.LoginAccountRequest;
import com.toast.boot.user.persistence.security.entity.Account;
import com.toast.boot.user.security.adapter.UserCredentials;
import com.toast.boot.user.security.adapter.authenticator.jwt.JwtPairResponse;
import com.toast.boot.user.security.adapter.authenticator.jwt.JwtRefreshResponse;
import com.toast.boot.user.security.adapter.authenticator.jwt.JwtTokenService;
import com.toast.boot.user.security.adapter.authenticator.jwt.UserJwtIdentifier;
import com.toast.security.AuthRegistry;
import com.toast.shared.app.CommandBus;
import com.toast.user.app.request.command.CreateUserCommand;
import com.toast.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(value = "/auth", version = "1.0")
@RequiredArgsConstructor
public class AuthController {
    private final AuthRegistry authRegistry;
    private final JwtTokenService tokenService;
    private final CommandBus commandBus;
    
    @PostMapping(value = "/signup", version = "2.0")
    public ResponseEntity<JwtPairResponse> signUp(@RequestBody CreateAccountRequest request) {
        UUID uuid = UUID.randomUUID();
        CompletableFuture<Void> promise = commandBus.awaitDispatch(
                new CreateUserCommand(
                        uuid,
                        request.firstName(),
                        request.lastName(),
                        request.age())
        );
        promise.join();
        UserCredentials credentials = new UserCredentials(request.email(), request.password());
        User domainContext = authRegistry.bind(uuid.toString(), UserJwtIdentifier.class);
        Account account = authRegistry.linkAndIdentify(credentials, domainContext, UserJwtIdentifier.class);
        return ResponseEntity.ok(tokenService.generatePairTokens(account));
    }
    
    @PostMapping("/signin")
    public ResponseEntity<JwtPairResponse> signIn(@RequestBody LoginAccountRequest request) {
        UserCredentials credentials = new UserCredentials(request.email(), request.password());
        Account account = authRegistry.identify(credentials, UserJwtIdentifier.class);
        return ResponseEntity.ok(tokenService.generatePairTokens(account));
    }
    
    @PostMapping("/refresh")
    public ResponseEntity<JwtRefreshResponse> refreshToken(@RequestHeader("Authorization") String bearerToken) {
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = bearerToken.substring(7);
        return ResponseEntity.ok(tokenService.refreshToken(token));
    }
}
