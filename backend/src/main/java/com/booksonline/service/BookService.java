package com.booksonline.service;

import com.booksonline.model.dto.request.BookRequest;
import com.booksonline.model.dto.response.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

/**
 * Service interface for Book business logic
 * Defines all operations for book management
 */
public interface BookService {

    /**
     * Get all books with pagination and sorting
     *
     * @param pageable Pagination and sorting parameters
     * @return Page of BookResponse DTOs
     */
    Page<BookResponse> getAllBooks(Pageable pageable);

    /**
     * Get a single book by ID and increment view count
     *
     * @param id Book ID
     * @return BookResponse DTO
     * @throws com.booksonline.exception.BookNotFoundException if book not found
     */
    BookResponse getBookById(Long id);

    /**
     * Create a new book
     *
     * @param request BookRequest DTO with book data
     * @return Created BookResponse DTO
     * @throws com.booksonline.exception.DuplicateIsbnException if ISBN already exists
     */
    BookResponse createBook(BookRequest request);

    /**
     * Update an existing book
     *
     * @param id      Book ID to update
     * @param request BookRequest DTO with updated data
     * @return Updated BookResponse DTO
     * @throws com.booksonline.exception.BookNotFoundException if book not found
     */
    BookResponse updateBook(Long id, BookRequest request);

    /**
     * Delete a book by ID
     *
     * @param id Book ID to delete
     * @throws com.booksonline.exception.BookNotFoundException if book not found
     */
    void deleteBook(Long id);

    /**
     * Advanced search with multiple filter criteria
     *
     * @param q             Quick search term (title, author, ISBN)
     * @param title         Title filter (partial match)
     * @param author        Author filter (partial match)
     * @param isbn          ISBN filter (exact match)
     * @param publisher     Publisher filter (partial match)
     * @param genre         Genre filter (exact match)
     * @param language      Language filter (exact match)
     * @param minYear       Minimum publication year
     * @param maxYear       Maximum publication year
     * @param minPrice      Minimum price
     * @param maxPrice      Maximum price
     * @param inStock       Filter for books with stock > 0
     * @param isAvailable   Availability filter
     * @param pageable      Pagination and sorting parameters
     * @return Page of BookResponse DTOs matching the search criteria
     */
    Page<BookResponse> searchBooks(
            String q,
            String title,
            String author,
            String isbn,
            String publisher,
            String genre,
            String language,
            Integer minYear,
            Integer maxYear,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Boolean inStock,
            Boolean isAvailable,
            Pageable pageable
    );

    /**
     * Duplicate an existing book
     * Creates a copy with title appended " (Copy)" and ISBN cleared
     *
     * @param id Book ID to duplicate
     * @return Created BookResponse DTO for the duplicate
     * @throws com.booksonline.exception.BookNotFoundException if book not found
     */
    BookResponse duplicateBook(Long id);

    /**
     * Export book data (future implementation for CSV/PDF)
     *
     * @param id     Book ID to export
     * @param format Export format (json, csv, pdf)
     * @return BookResponse DTO (for JSON format)
     * @throws com.booksonline.exception.BookNotFoundException if book not found
     */
    BookResponse exportBook(Long id, String format);
}
