package com.booksonline.service.impl;

import com.booksonline.exception.BookNotFoundException;
import com.booksonline.exception.DuplicateIsbnException;
import com.booksonline.mapper.BookMapper;
import com.booksonline.model.dto.request.BookRequest;
import com.booksonline.model.dto.response.BookResponse;
import com.booksonline.model.entity.Book;
import com.booksonline.repository.BookRepository;
import com.booksonline.service.BookService;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of BookService
 * Contains all business logic for book operations
 *
 * @Transactional ensures database transactions are handled properly
 */
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        logger.debug("Fetching all books with pagination: page={}, size={}",
                     pageable.getPageNumber(), pageable.getPageSize());

        Page<Book> books = bookRepository.findAll(pageable);
        return books.map(bookMapper::toResponse);
    }

    @Override
    @Transactional
    public BookResponse getBookById(Long id) {
        logger.debug("Fetching book by id: {}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Increment view count
        book.incrementViewCount();
        bookRepository.save(book);

        logger.debug("Book found and view count incremented: id={}, viewCount={}",
                     id, book.getViewCount());

        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse createBook(BookRequest request) {
        logger.debug("Creating new book with ISBN: {}", request.getIsbn());

        // Check for duplicate ISBN
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            logger.warn("Attempt to create book with duplicate ISBN: {}", request.getIsbn());
            throw new DuplicateIsbnException(request.getIsbn());
        }

        Book book = bookMapper.toEntity(request);
        Book savedBook = bookRepository.save(book);

        logger.info("Book created successfully: id={}, isbn={}",
                    savedBook.getId(), savedBook.getIsbn());

        return bookMapper.toResponse(savedBook);
    }

    @Override
    public BookResponse updateBook(Long id, BookRequest request) {
        logger.debug("Updating book: id={}", id);

        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // Check if ISBN changed and if new ISBN is already taken
        if (!existingBook.getIsbn().equals(request.getIsbn())) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                logger.warn("Attempt to update book with duplicate ISBN: {}", request.getIsbn());
                throw new DuplicateIsbnException(request.getIsbn());
            }
        }

        bookMapper.updateEntityFromRequest(existingBook, request);
        Book updatedBook = bookRepository.save(existingBook);

        logger.info("Book updated successfully: id={}", id);

        return bookMapper.toResponse(updatedBook);
    }

    @Override
    public void deleteBook(Long id) {
        logger.debug("Deleting book: id={}", id);

        if (!bookRepository.existsById(id)) {
            throw new BookNotFoundException(id);
        }

        bookRepository.deleteById(id);

        logger.info("Book deleted successfully: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookResponse> searchBooks(
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
            Pageable pageable) {

        logger.debug("Searching books with filters: q={}, title={}, author={}, genre={}",
                     q, title, author, genre);

        // Build dynamic specification for advanced search
        Specification<Book> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Quick search (q parameter) - searches across title, author, ISBN
            if (q != null && !q.isBlank()) {
                String searchTerm = "%" + q.toLowerCase() + "%";
                Predicate titleMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), searchTerm);
                Predicate authorMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author")), searchTerm);
                Predicate isbnMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("isbn")), searchTerm);
                predicates.add(criteriaBuilder.or(titleMatch, authorMatch, isbnMatch));
            }

            // Text filters (partial match, case-insensitive)
            if (title != null && !title.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"));
            }

            if (author != null && !author.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author")),
                        "%" + author.toLowerCase() + "%"));
            }

            if (publisher != null && !publisher.isBlank()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("publisher")),
                        "%" + publisher.toLowerCase() + "%"));
            }

            // Exact match filters
            if (isbn != null && !isbn.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("isbn"), isbn));
            }

            if (genre != null && !genre.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("genre")),
                        genre.toLowerCase()));
            }

            if (language != null && !language.isBlank()) {
                predicates.add(criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("language")),
                        language.toLowerCase()));
            }

            // Range filters
            if (minYear != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("publicationYear"), minYear));
            }

            if (maxYear != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("publicationYear"), maxYear));
            }

            if (minPrice != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(
                        root.get("price"), minPrice));
            }

            if (maxPrice != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(
                        root.get("price"), maxPrice));
            }

            // Boolean filters
            if (inStock != null && inStock) {
                predicates.add(criteriaBuilder.greaterThan(root.get("stockQuantity"), 0));
            }

            if (isAvailable != null) {
                predicates.add(criteriaBuilder.equal(root.get("isAvailable"), isAvailable));
            }

            // Combine all predicates with AND logic
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Book> books = bookRepository.findAll(spec, pageable);

        logger.debug("Search completed: found {} books", books.getTotalElements());

        return books.map(bookMapper::toResponse);
    }

    @Override
    public BookResponse duplicateBook(Long id) {
        logger.debug("Duplicating book: id={}", id);

        Book originalBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        Book duplicateBook = bookMapper.createDuplicate(originalBook);
        Book savedDuplicate = bookRepository.save(duplicateBook);

        logger.info("Book duplicated successfully: originalId={}, newId={}",
                    id, savedDuplicate.getId());

        return bookMapper.toResponse(savedDuplicate);
    }

    @Override
    @Transactional(readOnly = true)
    public BookResponse exportBook(Long id, String format) {
        logger.debug("Exporting book: id={}, format={}", id, format);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        // For V1, only JSON export is implemented
        // CSV and PDF export can be added in future versions
        if ("json".equalsIgnoreCase(format) || format == null) {
            return bookMapper.toResponse(book);
        }

        // Future: Implement CSV and PDF export
        throw new IllegalArgumentException("Export format not supported: " + format);
    }

    @Override
    public BookResponse addToWishlist(Long id) {
        logger.debug("Adding book to wishlist: id={}", id);

        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        book.incrementWishlistCount();
        Book savedBook = bookRepository.save(book);

        logger.info("Wishlist count incremented: id={}, wishlistCount={}",
                    id, savedBook.getWishlistCount());

        return bookMapper.toResponse(savedBook);
    }
}
