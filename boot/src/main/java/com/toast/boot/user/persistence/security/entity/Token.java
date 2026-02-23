/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.persistence.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {
    private UUID id;
    
    private Long accountId;
    
    private String value;
    
    private Instant expiresAt;
}
