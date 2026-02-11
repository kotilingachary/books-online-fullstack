package com.booksonline.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Standard error response DTO
 * Matches the ErrorResponse schema in OpenAPI specification
 *
 * Used for all error responses (404, 409, 500, etc.)
 * Provides consistent error format across the API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Integer status;

    private String error;

    private String message;

    private String path;
}
