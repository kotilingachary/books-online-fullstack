package com.booksonline.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation error response DTO with field-level error details
 * Matches the ValidationErrorResponse schema in OpenAPI specification
 *
 * Used for 400 Bad Request responses when validation fails
 * Contains array of field-specific validation errors
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationErrorResponse {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private Integer status;

    private String error;

    private String message;

    @Builder.Default
    private List<FieldError> errors = new ArrayList<>();

    private String path;

    /**
     * Inner class representing a single field validation error
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String message;
    }
}
