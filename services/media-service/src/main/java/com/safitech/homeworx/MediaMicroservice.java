package com.safitech.homeworx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Slf4j
@EnableDiscoveryClient
@EnableMongoAuditing
@SpringBootApplication
public class MediaMicroservice {

    public static void main(String[] args) {
        try {
            log.info("Starting MediaService application...");
            SpringApplication.run(MediaMicroservice.class, args);
            log.info("MediaService application started successfully.");
        } catch (Exception e) {
            log.error("Error occurred while starting MediaService application!", e);
            throw e;
        }
    }

}
