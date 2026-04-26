package com.booksonline.controller;

import com.booksonline.exception.BookNotFoundException;
import com.booksonline.model.dto.request.BookRequest;
import com.booksonline.model.dto.response.BookResponse;
import com.booksonline.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * REST Controller for Book Management
 * Implements all endpoints defined in the OpenAPI specification
 *
 * Base URL: /api/v1/books
 *
 * Endpoints:
 * - GET    /books                    - List all books (paginated)
 * - GET    /books/{id}               - Get book by ID
 * - POST   /books                    - Create new book
 * - PUT    /books/{id}               - Update book
 * - DELETE /books/{id}               - Delete book
 * - GET    /books/search             - Advanced search
 * - POST   /books/{id}/duplicate     - Duplicate book
 * - GET    /books/{id}/export        - Export book data
 */
@RestController
@RequestMapping("/api/v1/books")
@Tag(name = "Books", description = "Book management operations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // =================================================================
    // GET /books - List all books
    // =================================================================

    @GetMapping
    @Operation(summary = "List all books", description = "Retrieve a paginated list of all books")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successfully retrieved book list"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @PageableDefault(size = 4, sort = "id", direction = Sort.Direction.ASC)
            @Parameter(description = "Pagination parameters (page, size, sort)")
            Pageable pageable) {

        logger.info("GET /api/v1/books - page: {}, size: {}",
                    pageable.getPageNumber(), pageable.getPageSize());

        Page<BookResponse> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(books);
    }

    // =================================================================
    // GET /books/{id} - Get book by ID
    // =================================================================

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieve a single book by its ID. Increments view count.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book found"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable
            @Parameter(description = "Book ID", example = "123")
            Long id) {

        logger.info("GET /api/v1/books/{}", id);

        BookResponse book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    // =================================================================
    // POST /books - Create new book
    // =================================================================

    @PostMapping
    @Operation(summary = "Create new book", description = "Create a new book entry")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Book created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "409", description = "ISBN already exists")
    })
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Book data to create",
                content = @Content(schema = @Schema(implementation = BookRequest.class))
            )
            BookRequest request) {

        logger.info("POST /api/v1/books - Creating book with ISBN: {}", request.getIsbn());

        BookResponse createdBook = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBook);
    }

    // =================================================================
    // PUT /books/{id} - Update book
    // =================================================================

    @PutMapping("/{id}")
    @Operation(summary = "Update book", description = "Update an existing book's information")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Book updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable
            @Parameter(description = "Book ID to update", example = "456")
            Long id,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated book data",
                content = @Content(schema = @Schema(implementation = BookRequest.class))
            )
            BookRequest request) {

        logger.info("PUT /api/v1/books/{} - Updating book", id);

        BookResponse updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(updatedBook);
    }

    // =================================================================
    // DELETE /books/{id} - Delete book
    // =================================================================

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete book", description = "Permanently delete a book")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Book deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Void> deleteBook(
            @PathVariable
            @Parameter(description = "Book ID to delete", example = "456")
            Long id) {

        logger.info("DELETE /api/v1/books/{}", id);

        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // =================================================================
    // GET /books/search - Advanced search
    // =================================================================

    @GetMapping("/search")
    @Operation(summary = "Advanced search books", description = "Search books with multiple filter criteria")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Search results")
    })
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(required = false)
            @Parameter(description = "Quick search across title, author, ISBN")
            String q,

            @RequestParam(required = false)
            @Parameter(description = "Filter by title (partial match, case-insensitive)")
            String title,

            @RequestParam(required = false)
            @Parameter(description = "Filter by author (partial match, case-insensitive)")
            String author,

            @RequestParam(required = false)
            @Parameter(description = "Filter by ISBN (exact match)")
            String isbn,

            @RequestParam(required = false)
            @Parameter(description = "Filter by publisher (partial match)")
            String publisher,

            @RequestParam(required = false)
            @Parameter(description = "Filter by genre (exact match)")
            String genre,

            @RequestParam(required = false)
            @Parameter(description = "Filter by language (exact match)")
            String language,

            @RequestParam(required = false)
            @Parameter(description = "Minimum publication year")
            Integer minYear,

            @RequestParam(required = false)
            @Parameter(description = "Maximum publication year")
            Integer maxYear,

            @RequestParam(required = false)
            @Parameter(description = "Minimum price")
            BigDecimal minPrice,

            @RequestParam(required = false)
            @Parameter(description = "Maximum price")
            BigDecimal maxPrice,

            @RequestParam(required = false)
            @Parameter(description = "Filter books with stock > 0")
            Boolean inStock,

            @RequestParam(required = false)
            @Parameter(description = "Filter by availability status")
            Boolean isAvailable,

            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)
            @Parameter(description = "Pagination parameters")
            Pageable pageable) {

        logger.info("GET /api/v1/books/search - q: {}, genre: {}, author: {}", q, genre, author);

        Page<BookResponse> results = bookService.searchBooks(
                q, title, author, isbn, publisher, genre, language,
                minYear, maxYear, minPrice, maxPrice, inStock, isAvailable, pageable
        );

        return ResponseEntity.ok(results);
    }

    // =================================================================
    // POST /books/{id}/duplicate - Duplicate book
    // =================================================================

    @PostMapping("/{id}/duplicate")
    @Operation(summary = "Duplicate book", description = "Create a copy of an existing book")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Book duplicated successfully"),
        @ApiResponse(responseCode = "404", description = "Original book not found")
    })
    public ResponseEntity<BookResponse> duplicateBook(
            @PathVariable
            @Parameter(description = "Book ID to duplicate", example = "123")
            Long id) {

        logger.info("POST /api/v1/books/{}/duplicate", id);

        BookResponse duplicatedBook = bookService.duplicateBook(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(duplicatedBook);
    }

    // =================================================================
    // POST /books/{id}/wishlist - Add book to wishlist
    // =================================================================

    @PostMapping("/{id}/wishlist")
    @Operation(summary = "Add book to wishlist", description = "Increment the wishlist count for a book")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Wishlist count incremented"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponse> addToWishlist(
            @PathVariable
            @Parameter(description = "Book ID", example = "123")
            Long id) {

        logger.info("POST /api/v1/books/{}/wishlist", id);

        BookResponse updatedBook = bookService.addToWishlist(id);
        return ResponseEntity.ok(updatedBook);
    }

    // =================================================================
    // GET /books/{id}/export - Export book data
    // =================================================================

    @GetMapping("/{id}/export")
    @Operation(summary = "Export book data", description = "Export book data in various formats")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Export successful"),
        @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<BookResponse> exportBook(
            @PathVariable
            @Parameter(description = "Book ID to export", example = "123")
            Long id,

            @RequestParam(defaultValue = "json")
            @Parameter(description = "Export format (json, csv, pdf)")
            String format) {

        logger.info("GET /api/v1/books/{}/export?format={}", id, format);

        BookResponse book = bookService.exportBook(id, format);
        if (book == null) {
            throw new BookNotFoundException(id);
        }
        return ResponseEntity.ok(book);
    }
}
