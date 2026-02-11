package com.booksonline.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger configuration for API documentation
 *
 * Swagger UI will be available at: http://localhost:8080/swagger-ui.html
 * OpenAPI JSON: http://localhost:8080/v3/api-docs
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI booksOnlineOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Books Online API")
                        .description("""
                                REST API for Books Online - Book Management System

                                ## Overview
                                Books Online is a comprehensive book management system that allows users to:
                                - Browse and search books with advanced filtering
                                - Create, read, update, and delete book records
                                - Export book data in multiple formats (JSON, CSV, PDF)
                                - Duplicate existing books for quick entry

                                ## Version Information
                                - **V1 (Current)**: No authentication - Open access for development/demo
                                - **V2 (Future)**: JWT-based authentication with role-based access control

                                ## Data Format
                                - All requests and responses use `application/json`
                                - Dates in ISO 8601 format (UTC): `2026-02-11T10:30:00Z`
                                - Field names in camelCase

                                ## Pagination
                                List endpoints support pagination with query parameters:
                                - `page` (default: 0) - Page number (0-indexed)
                                - `size` (default: 4) - Items per page
                                - `sort` (default: id,asc) - Sort field and direction
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Books Online API Support")
                                .email("support@booksonline.com")
                                .url("https://booksonline.com/support"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort + "/api/v1")
                                .description("Development server (H2 database)"),
                        new Server()
                                .url("https://api.booksonline.com/v1")
                                .description("Production server (PostgreSQL database)")
                ));
    }
}
