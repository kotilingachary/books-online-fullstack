package com.booksonline.mapper;

import com.booksonline.model.dto.request.BookRequest;
import com.booksonline.model.dto.response.BookResponse;
import com.booksonline.model.entity.Book;
import org.springframework.stereotype.Component;

/**
 * Mapper component for converting between Book entity and DTOs
 *
 * Responsibilities:
 * - Convert BookRequest DTO to Book entity
 * - Convert Book entity to BookResponse DTO
 * - Handle field mapping and transformation
 */
@Component
public class BookMapper {

    /**
     * Convert BookRequest DTO to Book entity
     * Used for POST (create) operations
     *
     * @param request BookRequest DTO with user input
     * @return Book entity ready to be persisted
     */
    public Book toEntity(BookRequest request) {
        if (request == null) {
            return null;
        }

        return Book.builder()
                .title(request.getTitle())
                .isbn(request.getIsbn())
                .author(request.getAuthor())
                .genre(request.getGenre())
                .publicationYear(request.getPublicationYear())
                .language(request.getLanguage())
                .publisher(request.getPublisher())
                .pages(request.getPages())
                .description(request.getDescription())
                .coverImageUrl(request.getCoverImageUrl())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0)
                .rating(request.getRating())
                .reviewCount(request.getReviewCount() != null ? request.getReviewCount() : 0)
                .viewCount(0) // Always 0 for new books
                .isAvailable(request.getIsAvailable())
                .countryCode1(request.getCountryCode1())
                .countryCode(request.getCountryCode())
                .languages(request.getLanguages())
                .language2(request.getLanguage2())
                .build();
    }

    /**
     * Update existing Book entity with data from BookRequest DTO
     * Used for PUT (update) operations
     *
     * Note: ISBN is NOT updated (immutable business rule)
     * Note: id, viewCount, createdAt, updatedAt are NOT updated
     *
     * @param entity   Existing Book entity
     * @param request  BookRequest DTO with updated values
     */
    public void updateEntityFromRequest(Book entity, BookRequest request) {
        if (entity == null || request == null) {
            return;
        }

        // Update all fields except id, isbn, viewCount, createdAt, updatedAt
        entity.setTitle(request.getTitle());
        // ISBN is immutable - not updated
        entity.setAuthor(request.getAuthor());
        entity.setGenre(request.getGenre());
        entity.setPublicationYear(request.getPublicationYear());
        entity.setLanguage(request.getLanguage());
        entity.setPublisher(request.getPublisher());
        entity.setPages(request.getPages());
        entity.setDescription(request.getDescription());
        entity.setCoverImageUrl(request.getCoverImageUrl());
        entity.setPrice(request.getPrice());
        entity.setStockQuantity(request.getStockQuantity() != null ? request.getStockQuantity() : 0);
        entity.setRating(request.getRating());
        entity.setReviewCount(request.getReviewCount() != null ? request.getReviewCount() : 0);
        entity.setIsAvailable(request.getIsAvailable());
        entity.setCountryCode1(request.getCountryCode1());
        entity.setCountryCode(request.getCountryCode());
        entity.setLanguages(request.getLanguages());
        entity.setLanguage2(request.getLanguage2());
        // viewCount is not updated from request
    }

    /**
     * Convert Book entity to BookResponse DTO
     * Used for all GET operations
     *
     * @param entity Book entity from database
     * @return BookResponse DTO for API response
     */
    public BookResponse toResponse(Book entity) {
        if (entity == null) {
            return null;
        }

        return BookResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .isbn(entity.getIsbn())
                .author(entity.getAuthor())
                .genre(entity.getGenre())
                .publicationYear(entity.getPublicationYear())
                .language(entity.getLanguage())
                .publisher(entity.getPublisher())
                .pages(entity.getPages())
                .description(entity.getDescription())
                .coverImageUrl(entity.getCoverImageUrl())
                .price(entity.getPrice())
                .stockQuantity(entity.getStockQuantity())
                .rating(entity.getRating())
                .reviewCount(entity.getReviewCount())
                .viewCount(entity.getViewCount())
                .isAvailable(entity.getIsAvailable())
                .countryCode1(entity.getCountryCode1())
                .countryCode(entity.getCountryCode())
                .languages(entity.getLanguages())
                .language2(entity.getLanguage2())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    /**
     * Create a duplicate Book entity from an existing one
     * Used for POST /books/{id}/duplicate operation
     *
     * Modifications for duplicate:
     * - Title appended with " (Copy)"
     * - ISBN cleared (must be entered manually)
     * - Review count reset to 0
     * - View count reset to 0
     * - All other fields copied from original
     *
     * @param original Original book entity
     * @return New book entity with modifications
     */
    public Book createDuplicate(Book original) {
        if (original == null) {
            return null;
        }

        return Book.builder()
                .title(original.getTitle() + " (Copy)")
                .isbn("") // ISBN must be entered manually
                .author(original.getAuthor())
                .genre(original.getGenre())
                .publicationYear(original.getPublicationYear())
                .language(original.getLanguage())
                .publisher(original.getPublisher())
                .pages(original.getPages())
                .description(original.getDescription())
                .coverImageUrl(original.getCoverImageUrl())
                .price(original.getPrice())
                .stockQuantity(original.getStockQuantity())
                .rating(original.getRating())
                .reviewCount(0) // Reset review count
                .viewCount(0)   // Reset view count
                .isAvailable(original.getIsAvailable())
                .countryCode1(original.getCountryCode1())
                .countryCode(original.getCountryCode())
                .languages(original.getLanguages())
                .language2(original.getLanguage2())
                .build();
    }
}
