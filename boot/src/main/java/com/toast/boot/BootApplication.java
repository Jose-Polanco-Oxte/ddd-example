package com.toast.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:ide-bean-definitions.xml")
public class BootApplication {
    static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
}
