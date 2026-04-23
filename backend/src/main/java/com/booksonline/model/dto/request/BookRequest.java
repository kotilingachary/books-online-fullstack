package com.booksonline.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO for book creation and update requests
 * Matches the BookRequest schema in OpenAPI specification
 *
 * Validation annotations:
 * - @NotNull: Field cannot be null
 * - @NotBlank: String cannot be null, empty, or whitespace
 * - @Size: String length constraints
 * - @Min/@Max: Numeric range constraints
 * - @DecimalMin/@DecimalMax: Decimal range constraints
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookRequest {

    // Required Fields

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    private String title;

    @NotBlank(message = "ISBN is required")
    @Size(min = 10, max = 20, message = "ISBN must be between 10 and 20 characters")
    @Pattern(regexp = "^[0-9-]+$", message = "ISBN must contain only digits and hyphens")
    private String isbn;

    @NotBlank(message = "Author is required")
    @Size(min = 1, max = 100, message = "Author must be between 1 and 100 characters")
    private String author;

    @NotBlank(message = "Genre is required")
    @Size(min = 1, max = 50, message = "Genre must be between 1 and 50 characters")
    private String genre;

    @NotNull(message = "Publication year is required")
    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2100, message = "Publication year cannot exceed 2100")
    private Integer publicationYear;

    @NotBlank(message = "Language is required")
    @Size(min = 1, max = 30, message = "Language must be between 1 and 30 characters")
    private String language;

    @NotNull(message = "Availability status is required")
    private Boolean isAvailable;

    // Optional Fields

    @Size(max = 100, message = "Publisher must not exceed 100 characters")
    private String publisher;

    @Min(value = 1, message = "Pages must be at least 1")
    private Integer pages;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @Size(max = 500, message = "Cover image URL must not exceed 500 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "Cover image URL must be a valid HTTP/HTTPS URL or empty")
    private String coverImageUrl;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    @DecimalMax(value = "9999.99", inclusive = true, message = "Price cannot exceed 9999.99")
    @Digits(integer = 4, fraction = 2, message = "Price must have at most 4 digits before decimal and 2 after")
    private BigDecimal price;

    @Min(value = 0, message = "Stock quantity must be non-negative")
    private Integer stockQuantity;

    @DecimalMin(value = "0.0", inclusive = true, message = "Rating must be between 0.0 and 5.0")
    @DecimalMax(value = "5.0", inclusive = true, message = "Rating must be between 0.0 and 5.0")
    @Digits(integer = 1, fraction = 1, message = "Rating must have format X.X (e.g., 4.7)")
    private BigDecimal rating;

    @Min(value = 0, message = "Review count must be non-negative")
    private Integer reviewCount;

    private String countryCode1;

    private String countryCode;

    private String languages;

    private String language2;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    private String postalCode;

    @Size(max = 10, message = "Region code must not exceed 10 characters")
    private String regionCode;
}
