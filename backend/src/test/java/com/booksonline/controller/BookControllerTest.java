package com.booksonline.controller;

import com.booksonline.exception.BookNotFoundException;
import com.booksonline.exception.DuplicateIsbnException;
import com.booksonline.model.dto.request.BookRequest;
import com.booksonline.model.dto.response.BookResponse;
import com.booksonline.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Integration tests for BookController
 * Tests REST API endpoints with mocked service layer
 */
@WebMvcTest(BookController.class)
@DisplayName("BookController Integration Tests")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookRequest testBookRequest;
    private BookResponse testBookResponse;

    @BeforeEach
    void setUp() {
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
                .publisher("Prentice Hall")
                .pages(464)
                .price(new BigDecimal("47.99"))
                .stockQuantity(10)
                .isAvailable(true)
                .reviewCount(0)
                .viewCount(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ==================== CREATE TESTS ====================

    @Test
    @DisplayName("POST /api/v1/books - Create book successfully")
    void testCreateBook_Success() throws Exception {
        // Given
        when(bookService.createBook(any(BookRequest.class))).thenReturn(testBookResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.isbn").value("978-0132350884"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.genre").value("Programming"));

        verify(bookService).createBook(any(BookRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/books - Validation error for missing required fields")
    void testCreateBook_ValidationError_MissingFields() throws Exception {
        // Given
        BookRequest invalidRequest = BookRequest.builder()
                .title("") // Empty title
                .isbn("")  // Empty ISBN
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(bookService, never()).createBook(any(BookRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/books - Duplicate ISBN returns 409 Conflict")
    void testCreateBook_DuplicateIsbn_ReturnsConflict() throws Exception {
        // Given
        when(bookService.createBook(any(BookRequest.class)))
                .thenThrow(new DuplicateIsbnException("978-0132350884"));

        // When & Then
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("ISBN 978-0132350884 already exists"));

        verify(bookService).createBook(any(BookRequest.class));
    }

    // ==================== READ TESTS ====================

    @Test
    @DisplayName("GET /api/v1/books - Get all books with pagination")
    void testGetAllBooks_WithPagination() throws Exception {
        // Given
        List<BookResponse> bookList = Arrays.asList(testBookResponse);
        Page<BookResponse> bookPage = new PageImpl<>(bookList, PageRequest.of(0, 4), 1);
        when(bookService.getAllBooks(any())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books")
                        .param("page", "0")
                        .param("size", "4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));

        verify(bookService).getAllBooks(any());
    }

    @Test
    @DisplayName("GET /api/v1/books/{id} - Get book by ID successfully")
    void testGetBookById_Success() throws Exception {
        // Given
        when(bookService.getBookById(1L)).thenReturn(testBookResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.isbn").value("978-0132350884"));

        verify(bookService).getBookById(1L);
    }

    @Test
    @DisplayName("GET /api/v1/books/{id} - Book not found returns 404")
    void testGetBookById_NotFound_Returns404() throws Exception {
        // Given
        when(bookService.getBookById(99L)).thenThrow(new BookNotFoundException(99L));

        // When & Then
        mockMvc.perform(get("/api/v1/books/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Book with ID 99 not found"));

        verify(bookService).getBookById(99L);
    }

    // ==================== UPDATE TESTS ====================

    @Test
    @DisplayName("PUT /api/v1/books/{id} - Update book successfully")
    void testUpdateBook_Success() throws Exception {
        // Given
        BookResponse updatedResponse = BookResponse.builder()
                .id(1L)
                .title("Clean Code - Updated")
                .isbn("978-0132350884")
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .stockQuantity(15)
                .isAvailable(true)
                .build();

        when(bookService.updateBook(eq(1L), any(BookRequest.class))).thenReturn(updatedResponse);

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

        // When & Then
        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code - Updated"))
                .andExpect(jsonPath("$.stockQuantity").value(15));

        verify(bookService).updateBook(eq(1L), any(BookRequest.class));
    }

    @Test
    @DisplayName("PUT /api/v1/books/{id} - Update non-existent book returns 404")
    void testUpdateBook_NotFound_Returns404() throws Exception {
        // Given
        when(bookService.updateBook(eq(99L), any(BookRequest.class)))
                .thenThrow(new BookNotFoundException(99L));

        // When & Then
        mockMvc.perform(put("/api/v1/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookRequest)))
                .andExpect(status().isNotFound());

        verify(bookService).updateBook(eq(99L), any(BookRequest.class));
    }

    // ==================== DELETE TESTS ====================

    @Test
    @DisplayName("DELETE /api/v1/books/{id} - Delete book successfully")
    void testDeleteBook_Success() throws Exception {
        // Given
        doNothing().when(bookService).deleteBook(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService).deleteBook(1L);
    }

    @Test
    @DisplayName("DELETE /api/v1/books/{id} - Delete non-existent book returns 404")
    void testDeleteBook_NotFound_Returns404() throws Exception {
        // Given
        doThrow(new BookNotFoundException(99L)).when(bookService).deleteBook(99L);

        // When & Then
        mockMvc.perform(delete("/api/v1/books/99"))
                .andExpect(status().isNotFound());

        verify(bookService).deleteBook(99L);
    }

    // ==================== SEARCH TESTS ====================

    @Test
    @DisplayName("GET /api/v1/books/search - Search with query parameter")
    void testSearchBooks_WithQueryParameter() throws Exception {
        // Given
        List<BookResponse> bookList = Arrays.asList(testBookResponse);
        Page<BookResponse> bookPage = new PageImpl<>(bookList, PageRequest.of(0, 10), 1);
        when(bookService.searchBooks(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books/search")
                        .param("q", "Clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title").value("Clean Code"));

        verify(bookService).searchBooks(eq("Clean"), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("GET /api/v1/books/search - Search by genre")
    void testSearchBooks_ByGenre() throws Exception {
        // Given
        List<BookResponse> bookList = Arrays.asList(testBookResponse);
        Page<BookResponse> bookPage = new PageImpl<>(bookList, PageRequest.of(0, 10), 1);
        when(bookService.searchBooks(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books/search")
                        .param("genre", "Programming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].genre").value("Programming"));

        verify(bookService).searchBooks(any(), eq("Programming"), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("GET /api/v1/books/search - Search with year range")
    void testSearchBooks_WithYearRange() throws Exception {
        // Given
        List<BookResponse> bookList = Arrays.asList(testBookResponse);
        Page<BookResponse> bookPage = new PageImpl<>(bookList, PageRequest.of(0, 10), 1);
        when(bookService.searchBooks(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(bookPage);

        // When & Then
        mockMvc.perform(get("/api/v1/books/search")
                        .param("minYear", "2000")
                        .param("maxYear", "2010"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        verify(bookService).searchBooks(any(), any(), any(), any(), eq(2000), eq(2010), any(), any(), any(), any(), any(), any(), any(), any());
    }

    // ==================== DUPLICATE TESTS ====================

    @Test
    @DisplayName("POST /api/v1/books/{id}/duplicate - Duplicate book successfully")
    void testDuplicateBook_Success() throws Exception {
        // Given
        BookResponse duplicatedResponse = BookResponse.builder()
                .id(2L)
                .title("Clean Code (Copy)")
                .isbn(null) // ISBN cleared
                .author("Robert C. Martin")
                .genre("Programming")
                .publicationYear(2008)
                .language("English")
                .build();

        when(bookService.duplicateBook(1L)).thenReturn(duplicatedResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/books/1/duplicate"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.title").value("Clean Code (Copy)"))
                .andExpect(jsonPath("$.isbn").doesNotExist());

        verify(bookService).duplicateBook(1L);
    }

    @Test
    @DisplayName("POST /api/v1/books/{id}/duplicate - Duplicate non-existent book returns 404")
    void testDuplicateBook_NotFound_Returns404() throws Exception {
        // Given
        when(bookService.duplicateBook(99L)).thenThrow(new BookNotFoundException(99L));

        // When & Then
        mockMvc.perform(post("/api/v1/books/99/duplicate"))
                .andExpect(status().isNotFound());

        verify(bookService).duplicateBook(99L);
    }

    // ==================== CORS TESTS ====================

    @Test
    @DisplayName("CORS - Preflight request allowed")
    void testCors_PreflightRequest() throws Exception {
        mockMvc.perform(options("/api/v1/books")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://localhost:5173"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}
