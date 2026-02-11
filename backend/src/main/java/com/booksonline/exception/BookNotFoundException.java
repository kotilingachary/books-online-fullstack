package com.booksonline.exception;

/**
 * Exception thrown when a book is not found by ID
 * Results in HTTP 404 Not Found response
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}
