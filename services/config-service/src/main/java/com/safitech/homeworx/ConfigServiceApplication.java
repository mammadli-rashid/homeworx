package com.safitech.homeworx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@Slf4j
@EnableConfigServer
@SpringBootApplication
public class ConfigServiceApplication {

    public static void main(String[] args) {
        try {
            log.info("Starting ConfigService application...");
            SpringApplication.run(ConfigServiceApplication.class, args);
            log.info("ConfigService application started successfully.");
        } catch (Exception e) {
            log.error("Error occurred while starting ConfigService application!", e);
            throw e;
        }
    }

}
