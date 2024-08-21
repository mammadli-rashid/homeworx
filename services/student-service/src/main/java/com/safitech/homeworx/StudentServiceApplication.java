package com.safitech.homeworx;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Slf4j
@EnableDiscoveryClient
@EnableJpaAuditing
@SpringBootApplication
public class StudentServiceApplication {

    public static void main(String[] args) {
        try {
            log.info("Starting StudentService application...");
            SpringApplication.run(StudentServiceApplication.class, args);
            log.info("StudentService application started successfully.");
        } catch (Exception e) {
            log.error("Error occurred while starting StudentService application!", e);
            throw e;
        }
    }

}
