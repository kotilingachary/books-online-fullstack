package com.booksonline.service.impl;

import com.booksonline.exception.BookNotFoundException;
import com.booksonline.exception.DuplicateIsbnException;
import com.booksonline.mapper.BookMapper;
import com.booksonline.model.dto.request.BookRequest;
import com.booksonline.model.dto.response.BookResponse;
import com.booksonline.model.entity.Book;
import com.booksonline.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookServiceImpl
 * Tests service layer business logic with mocked dependencies
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Unit Tests")
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookRequest testBookRequest;
    private BookResponse testBookResponse;

    @BeforeEach
    void setUp() {
        // Setup test data
        testBook = Book.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("978-0132350884")
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .publisher("Prentice Hall")
                .pages(464)
                .price(new BigDecimal("47.99"))
                .stockQuantity(10)
                .isAvailable(true)
                .reviewCount(0)
                .viewCount(0)
                .build();

        testBookRequest = BookRequest.builder()
                .title("Clean Code")
                .isbn("978-0132350884")
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .publisher("Prentice Hall")
                .pages(464)
                .price(new BigDecimal("47.99"))
                .stockQuantity(10)
                .isAvailable(true)
                .build();

        testBookResponse = BookResponse.builder()
                .id(1L)
                .title("Clean Code")
                .isbn("978-0132350884")
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .build();
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("Create book - Success")
    void testCreateBook_Success() {
        // Given
        when(bookRepository.existsByIsbn(testBookRequest.getIsbn())).thenReturn(false);
        when(bookMapper.toEntity(testBookRequest)).thenReturn(testBook);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        // When
        BookResponse result = bookService.createBook(testBookRequest);

        // Then
        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals("978-0132350884", result.getIsbn());
        verify(bookRepository).existsByIsbn(testBookRequest.getIsbn());
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toEntity(testBookRequest);
        verify(bookMapper).toResponse(testBook);
    }

    @Test
    @DisplayName("Create book - Duplicate ISBN throws exception")
    void testCreateBook_DuplicateIsbn_ThrowsException() {
        // Given
        when(bookRepository.existsByIsbn(testBookRequest.getIsbn())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateIsbnException.class, () -> bookService.createBook(testBookRequest));
        verify(bookRepository).existsByIsbn(testBookRequest.getIsbn());
        verify(bookRepository, never()).save(any(Book.class));
    }

    // ==================== READ TESTS ====================

    @Test
    @DisplayName("Get book by ID - Success")
    void testGetBookById_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        // When
        BookResponse result = bookService.getBookById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Clean Code", result.getTitle());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(testBook); // View count increment
        verify(bookMapper).toResponse(testBook);
    }

    @Test
    @DisplayName("Get book by ID - Not found throws exception")
    void testGetBookById_NotFound_ThrowsException() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.getBookById(99L));
        verify(bookRepository).findById(99L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Get book by ID - Increments view count")
    void testGetBookById_IncrementsViewCount() {
        // Given
        testBook.setViewCount(5);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        // When
        bookService.getBookById(1L);

        // Then
        assertEquals(6, testBook.getViewCount());
        verify(bookRepository).save(testBook);
    }

    @Test
    @DisplayName("Get all books - Returns paginated results")
    void testGetAllBooks_ReturnsPaginatedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, 1);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toResponse(any(Book.class))).thenReturn(testBookResponse);

        // When
        Page<BookResponse> result = bookService.getAllBooks(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        verify(bookRepository).findAll(pageable);
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("Update book - Success")
    void testUpdateBook_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        BookRequest updateRequest = BookRequest.builder()
                .title("Clean Code - Updated")
                .isbn("978-0132350884")
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .stockQuantity(15)
                .isAvailable(true)
                .build();

        // When
        BookResponse result = bookService.updateBook(1L, updateRequest);

        // Then
        assertNotNull(result);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(testBook);
        verify(bookMapper).toResponse(testBook);
    }

    @Test
    @DisplayName("Update book - Not found throws exception")
    void testUpdateBook_NotFound_ThrowsException() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.updateBook(99L, testBookRequest));
        verify(bookRepository).findById(99L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("Delete book - Success")
    void testDeleteBook_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        doNothing().when(bookRepository).delete(testBook);

        // When
        bookService.deleteBook(1L);

        // Then
        verify(bookRepository).findById(1L);
        verify(bookRepository).delete(testBook);
    }

    @Test
    @DisplayName("Delete book - Not found throws exception")
    void testDeleteBook_NotFound_ThrowsException() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.deleteBook(99L));
        verify(bookRepository).findById(99L);
        verify(bookRepository, never()).delete(any(Book.class));
    }

    // ==================== SEARCH TESTS ====================

    @Test
    @DisplayName("Search books - With query parameter")
    void testSearchBooks_WithQuery() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, 1);

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);
        when(bookMapper.toResponse(any(Book.class))).thenReturn(testBookResponse);

        // When
        Page<BookResponse> result = bookService.searchBooks("Clean", null, null, null, null, null, null, null, null, null, null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("Search books - With genre filter")
    void testSearchBooks_WithGenre() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> bookList = Arrays.asList(testBook);
        Page<Book> bookPage = new PageImpl<>(bookList, pageable, 1);

        when(bookRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(bookPage);
        when(bookMapper.toResponse(any(Book.class))).thenReturn(testBookResponse);

        // When
        Page<BookResponse> result = bookService.searchBooks(null, "Programming", null, null, null, null, null, null, null, null, null, null, null, pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findAll(any(Specification.class), eq(pageable));
    }

    // ==================== DUPLICATE TESTS ====================

    @Test
    @DisplayName("Duplicate book - Success")
    void testDuplicateBook_Success() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookMapper.toResponse(any(Book.class))).thenReturn(testBookResponse);

        // When
        BookResponse result = bookService.duplicateBook(1L);

        // Then
        assertNotNull(result);
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        verify(bookMapper).toResponse(any(Book.class));
    }

    @Test
    @DisplayName("Duplicate book - Not found throws exception")
    void testDuplicateBook_NotFound_ThrowsException() {
        // Given
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BookNotFoundException.class, () -> bookService.duplicateBook(99L));
        verify(bookRepository).findById(99L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    // ==================== VALIDATION TESTS ====================

    @Test
    @DisplayName("Update book - Stock quantity affects availability")
    void testUpdateBook_StockQuantityAffectsAvailability() {
        // Given
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookMapper.toResponse(testBook)).thenReturn(testBookResponse);

        BookRequest updateRequest = BookRequest.builder()
                .title("Clean Code")
                .isbn("978-0132350884")
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .stockQuantity(0)
                .isAvailable(true)
                .build();

        // When
        bookService.updateBook(1L, updateRequest);

        // Then
        assertFalse(testBook.getIsAvailable());
        verify(bookRepository).save(testBook);
    }
}
