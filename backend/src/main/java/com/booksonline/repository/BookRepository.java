package com.booksonline.repository;

import com.booksonline.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for Book entity
 *
 * Extends:
 * - JpaRepository: Provides CRUD operations, pagination, and sorting
 * - JpaSpecificationExecutor: Provides dynamic query capabilities for advanced search
 *
 * Spring Data JPA automatically implements this interface at runtime
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

    /**
     * Check if a book with the given ISBN exists
     * Used to enforce ISBN uniqueness constraint
     *
     * @param isbn ISBN to check
     * @return true if book with ISBN exists, false otherwise
     */
    boolean existsByIsbn(String isbn);

    /**
     * Check if a book with the given ISBN exists, excluding a specific book ID
     * Used for update operations to allow same ISBN for the book being updated
     *
     * @param isbn ISBN to check
     * @param id   Book ID to exclude from check
     * @return true if another book with the same ISBN exists
     */
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    /**
     * Find book by ISBN
     * Useful for quick lookups without needing the database ID
     *
     * @param isbn ISBN to search for
     * @return Optional containing book if found
     */
    Optional<Book> findByIsbn(String isbn);

    /**
     * Quick search across multiple fields (title, author, ISBN)
     * Case-insensitive partial match
     *
     * @param searchTerm Search term to look for
     * @param pageable   Pagination information
     * @return Page of books matching the search
     */
    @Query("SELECT b FROM Book b WHERE " +
           "LOWER(b.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.author) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(b.isbn) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Book> quickSearch(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Find books by genre
     * Exact match, case-insensitive
     *
     * @param genre    Genre to filter by
     * @param pageable Pagination information
     * @return Page of books in the specified genre
     */
    Page<Book> findByGenreIgnoreCase(String genre, Pageable pageable);

    /**
     * Find books by author
     * Partial match, case-insensitive
     *
     * @param author   Author name to search for
     * @param pageable Pagination information
     * @return Page of books by the author
     */
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);

    /**
     * Find books by title
     * Partial match, case-insensitive
     *
     * @param title    Title to search for
     * @param pageable Pagination information
     * @return Page of books matching the title
     */
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Find available books (in stock)
     *
     * @param isAvailable Availability status
     * @param pageable    Pagination information
     * @return Page of available books
     */
    Page<Book> findByIsAvailable(Boolean isAvailable, Pageable pageable);

    /**
     * Find books published in a specific year range
     *
     * @param minYear  Minimum publication year (inclusive)
     * @param maxYear  Maximum publication year (inclusive)
     * @param pageable Pagination information
     * @return Page of books published in the year range
     */
    Page<Book> findByPublicationYearBetween(Integer minYear, Integer maxYear, Pageable pageable);

    /**
     * Find books by genre and publication year range
     * Composite query for common search pattern
     *
     * @param genre    Genre to filter by
     * @param minYear  Minimum publication year
     * @param maxYear  Maximum publication year
     * @param pageable Pagination information
     * @return Page of books matching genre and year range
     */
    Page<Book> findByGenreIgnoreCaseAndPublicationYearBetween(
            String genre, Integer minYear, Integer maxYear, Pageable pageable);
}
