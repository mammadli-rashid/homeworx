package com.safitech.homeworx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Slf4j
@EnableDiscoveryClient
@EnableMongoRepositories
@SpringBootApplication
public class TeacherMicroservice {

    public static void main(String[] args) {
        try {
            log.info("Starting TeacherMicroservice application...");
            SpringApplication.run(TeacherMicroservice.class, args);
            log.info("TeacherMicroservice application started successfully.");
        } catch (Exception e) {
            log.error("Error occurred while starting TeacherMicroservice application!", e);
            throw e;
        }
    }

}