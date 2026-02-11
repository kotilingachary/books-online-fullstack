package com.booksonline.exception;

/**
 * Exception thrown when attempting to create/update a book with a duplicate ISBN
 * Results in HTTP 409 Conflict response
 */
public class DuplicateIsbnException extends RuntimeException {

    public DuplicateIsbnException(String isbn) {
        super("Book with ISBN " + isbn + " already exists");
    }

    public DuplicateIsbnException(String message, Object... args) {
        super(String.format(message, args));
    }
}
