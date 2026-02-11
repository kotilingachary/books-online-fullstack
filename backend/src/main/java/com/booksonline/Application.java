package com.booksonline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main Spring Boot Application class for Books Online - Book Management System
 *
 * @SpringBootApplication enables:
 *   - @Configuration: Allows bean definitions
 *   - @EnableAutoConfiguration: Enables Spring Boot auto-configuration
 *   - @ComponentScan: Scans for components in this package and sub-packages
 *
 * @EnableJpaAuditing enables automatic auditing for @CreatedDate and @LastModifiedDate
 */
@SpringBootApplication
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
