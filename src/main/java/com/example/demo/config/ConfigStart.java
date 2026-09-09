package com.example.demo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigStart implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ConfigStart.class);

    @Override
    public void run(String... args) {
        log.info("Banking application started successfully");
    }
}
