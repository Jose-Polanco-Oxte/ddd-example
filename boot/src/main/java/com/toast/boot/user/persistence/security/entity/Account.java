/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.user.persistence.security.entity;

import com.toast.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account implements Principal {
    
    private Long id;
    
    private UUID userId;
    
    private String email;
    
    private String hashedPassword;
    
    private String status;
    
    @Override
    public String getIdentifier() {
        return userId.toString();
    }
}
