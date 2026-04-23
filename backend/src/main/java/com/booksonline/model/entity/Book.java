package com.booksonline.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * JPA Entity representing the books table
 * Maps to database schema defined in schema.sql
 *
 * This entity uses Lombok annotations to reduce boilerplate:
 * - @Data: Generates getters, setters, toString, equals, and hashCode
 * - @Builder: Provides a builder pattern for object creation
 * - @NoArgsConstructor: Generates no-args constructor (required by JPA)
 * - @AllArgsConstructor: Generates all-args constructor
 *
 * @EntityListeners enables JPA auditing for createdAt and updatedAt fields
 */
@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Book {

    // =================================================================
    // Primary Key
    // =================================================================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =================================================================
    // Required Fields (Book Identification)
    // =================================================================

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Column(nullable = false, length = 30)
    private String language;

    // =================================================================
    // Optional Fields (Book Metadata)
    // =================================================================

    @Column(length = 100)
    private String publisher;

    @Column
    private Integer pages;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    // =================================================================
    // Pricing and Inventory
    // =================================================================

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    // =================================================================
    // Rating and Engagement
    // =================================================================

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "review_count", nullable = false)
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(name = "country_code1")
    private String countryCode1;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "languages")
    private String languages;

    @Column(name = "language2")
    private String language2;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    // =================================================================
    // Availability
    // =================================================================

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private Boolean isAvailable = true;

    // =================================================================
    // Audit Columns (Auto-managed by Spring Data JPA Auditing)
    // =================================================================

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // =================================================================
    // JPA Lifecycle Callbacks
    // =================================================================

    /**
     * Automatically set availability based on stock quantity before persisting
     * Called before INSERT operations
     */
    @PrePersist
    public void prePersist() {
        if (this.stockQuantity == null) {
            this.stockQuantity = 0;
        }
        if (this.reviewCount == null) {
            this.reviewCount = 0;
        }
        if (this.viewCount == null) {
            this.viewCount = 0;
        }
        if (this.isAvailable == null) {
            this.isAvailable = true;
        }
    }

    /**
     * Increment view count when book is accessed
     * This method should be called explicitly in the service layer
     */
    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }
}
