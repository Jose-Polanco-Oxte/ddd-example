/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.security.adapter;

import com.toast.security.Credentials;

public record UserCredentials(String email, String password) implements Credentials {
}
