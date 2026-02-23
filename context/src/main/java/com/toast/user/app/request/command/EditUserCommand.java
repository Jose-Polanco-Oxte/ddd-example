/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.user.app.request.command;

import com.toast.shared.app.Command;

import java.util.UUID;

public record EditUserCommand(
        UUID userId,
        String firstName,
        String lastName,
        int age
) implements Command {
}
