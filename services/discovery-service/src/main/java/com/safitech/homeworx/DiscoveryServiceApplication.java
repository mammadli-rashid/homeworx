package com.safitech.homeworx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class DiscoveryServiceApplication {

    public static void main(String[] args) {
        try {
            log.info("Starting DiscoveryService application...");
            SpringApplication.run(DiscoveryServiceApplication.class, args);
            log.info("DiscoveryService application started successfully.");
        } catch (Exception e) {
            log.error("Error occurred while starting DiscoveryService application!", e);
            throw e;
        }
    }

}
