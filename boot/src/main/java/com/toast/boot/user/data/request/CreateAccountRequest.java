/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.data.request;

public record CreateAccountRequest(
        String firstName,
        String lastName,
        int age,
        String email,
        String password
) {
}
