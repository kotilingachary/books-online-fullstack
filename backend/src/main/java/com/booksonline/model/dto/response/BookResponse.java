package com.booksonline.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for book response data
 * Matches the BookResponse schema in OpenAPI specification
 *
 * This DTO is returned in all GET operations and includes all book fields
 * including system-generated fields (id, viewCount, createdAt, updatedAt)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    // System-generated fields
    private Long id;

    // Required fields
    private String title;
    private String isbn;
    private String author;
    private String genre;
    private Integer publicationYear;
    private String language;
    private Boolean isAvailable;

    // Optional metadata fields
    private String publisher;
    private Integer pages;
    private String description;
    private String coverImageUrl;

    // Pricing and inventory
    private BigDecimal price;
    private Integer stockQuantity;

    // Rating and engagement
    private BigDecimal rating;
    private Integer reviewCount;
    private Integer viewCount;

    private String countryCode1;
    private String countryCode;
    private String languages;
    private String language2;
    private String postalCode;

    // Audit fields (ISO 8601 format)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime updatedAt;
}
