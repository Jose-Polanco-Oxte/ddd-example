/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.controller;

import com.toast.boot.shared.data.ApiResponse;
import com.toast.boot.user.data.request.CreateAccountRequest;
import com.toast.security.SecureCommandBus;
import com.toast.security.SecureQueryBus;
import com.toast.shared.app.QueryBus;
import com.toast.user.app.request.command.CreateUserCommand;
import com.toast.user.app.request.query.GetUserByIdQuery;
import com.toast.user.app.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/user", version = "1.0")
@RequiredArgsConstructor
public class UserController {
    private final SecureCommandBus secureCommandBus;
    private final SecureQueryBus secureQueryBus;
    private final QueryBus queryBus;
    
    @PostMapping
    public ResponseEntity<ApiResponse<UUID>> createUser(@RequestBody CreateAccountRequest createAccountRequest) {
        var command = new CreateUserCommand(
                UUID.randomUUID(),
                createAccountRequest.firstName(),
                createAccountRequest.lastName(),
                createAccountRequest.age()
        );
        secureCommandBus.dispatch(command);
        return ApiResponse.accepted(command.userId());
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserById() {
        return ApiResponse.success(secureQueryBus.ask(new GetUserByIdQuery()));
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserById(@PathVariable UUID userId) {
        return ApiResponse.success(queryBus.ask(new GetUserByIdQuery(userId)));
    }
}
