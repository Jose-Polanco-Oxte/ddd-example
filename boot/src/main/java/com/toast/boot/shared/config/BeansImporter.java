/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.config;

import com.toast.security.SecureCommandBus;
import com.toast.security.SecureQueryBus;
import com.toast.security.SecurityContext;
import com.toast.shared.app.CommandBus;
import com.toast.shared.app.QueryBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansImporter {
    
    @Bean
    public SecureCommandBus secureCommandBus(CommandBus commandBus, SecurityContext securityContext) {
        return new SecureCommandBus(commandBus, securityContext);
    }
    
    @Bean
    public SecureQueryBus secureQueryBus(QueryBus queryBus, SecurityContext securityContext) {
        return new SecureQueryBus(queryBus, securityContext);
    }
}
