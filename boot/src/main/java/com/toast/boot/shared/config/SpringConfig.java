/*
 * Copyright (c) 2026. Dir created by Tun4z
 */

package com.toast.boot.shared.config;

import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ApiVersionConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringConfig implements WebMvcConfigurer {
    
    @Override
    public void configureApiVersioning(@NonNull ApiVersionConfigurer configurer) {
        configurer
                .setDefaultVersion("1.0")
                .useRequestHeader("Api-Version");
    }
}
