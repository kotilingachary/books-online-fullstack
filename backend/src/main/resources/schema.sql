-- =================================================================
-- Database Schema for Books Online - Book Management System
-- Generated: 2026-02-11
-- Database: H2 (Development) / PostgreSQL (Production)
-- Normalization: Third Normal Form (3NF)
-- Version: 1.0
-- =================================================================

-- =================================================================
-- Drop tables if they exist (for development reloading)
-- =================================================================
DROP TABLE IF EXISTS books;

-- =================================================================
-- Table: books
-- Description: Book catalog with all book information
-- Primary Entity: Book
-- Relationships: None (single-entity system in V1)
-- =================================================================
CREATE TABLE books (
    -- Primary Key
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Required Fields (Book Identification)
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    publication_year INT NOT NULL,
    language VARCHAR(30) NOT NULL,

    -- Optional Fields (Book Metadata)
    publisher VARCHAR(100),
    pages INT,
    description TEXT,
    cover_image_url VARCHAR(500),

    -- Pricing and Inventory
    price DECIMAL(10, 2),
    stock_quantity INT NOT NULL DEFAULT 0,

    -- Rating and Engagement
    rating DECIMAL(2, 1),
    review_count INT NOT NULL DEFAULT 0,
    view_count INT NOT NULL DEFAULT 0,

    -- Country Codes
    country_code1 VARCHAR(255),
    country_code VARCHAR(255),
    languages VARCHAR(255),
    language2 VARCHAR(255),
    postal_code VARCHAR(20),

    -- Availability
    is_available BOOLEAN NOT NULL DEFAULT TRUE,

    -- Audit Columns (Auto-managed)
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- =============================================================
    -- Constraints
    -- =============================================================

    -- ISBN Format Validation (10-20 characters)
    CONSTRAINT chk_books_isbn_length CHECK (LENGTH(isbn) >= 10 AND LENGTH(isbn) <= 20),

    -- Title Length Validation
    CONSTRAINT chk_books_title_length CHECK (LENGTH(title) >= 1 AND LENGTH(title) <= 200),

    -- Author Length Validation
    CONSTRAINT chk_books_author_length CHECK (LENGTH(author) >= 1 AND LENGTH(author) <= 100),

    -- Genre Length Validation
    CONSTRAINT chk_books_genre_length CHECK (LENGTH(genre) >= 1 AND LENGTH(genre) <= 50),

    -- Language Length Validation
    CONSTRAINT chk_books_language_length CHECK (LENGTH(language) >= 1 AND LENGTH(language) <= 30),

    -- Publication Year Range (1000-2100)
    CONSTRAINT chk_books_year_range CHECK (publication_year >= 1000 AND publication_year <= 2100),

    -- Pages Must Be Positive
    CONSTRAINT chk_books_pages_positive CHECK (pages IS NULL OR pages > 0),

    -- Price Must Be Non-Negative
    CONSTRAINT chk_books_price_nonnegative CHECK (price IS NULL OR price >= 0),

    -- Stock Quantity Must Be Non-Negative
    CONSTRAINT chk_books_stock_nonnegative CHECK (stock_quantity >= 0),

    -- Rating Range (0.0-5.0)
    CONSTRAINT chk_books_rating_range CHECK (rating IS NULL OR (rating >= 0.0 AND rating <= 5.0)),

    -- Review Count Must Be Non-Negative
    CONSTRAINT chk_books_review_count_nonnegative CHECK (review_count >= 0),

    -- View Count Must Be Non-Negative
    CONSTRAINT chk_books_view_count_nonnegative CHECK (view_count >= 0)
);

-- =================================================================
-- Comments on Table and Columns
-- =================================================================
COMMENT ON TABLE books IS 'Book catalog containing all book information for the Books Online system';

COMMENT ON COLUMN books.id IS 'Unique identifier for the book (auto-generated)';
COMMENT ON COLUMN books.title IS 'Book title (max 200 characters)';
COMMENT ON COLUMN books.isbn IS 'International Standard Book Number (unique, 10-20 characters)';
COMMENT ON COLUMN books.author IS 'Book author name(s) (max 100 characters)';
COMMENT ON COLUMN books.genre IS 'Book genre/category (max 50 characters)';
COMMENT ON COLUMN books.publication_year IS 'Year of publication (1000-2100)';
COMMENT ON COLUMN books.language IS 'Primary language of the book (max 30 characters)';
COMMENT ON COLUMN books.publisher IS 'Publisher name (optional, max 100 characters)';
COMMENT ON COLUMN books.pages IS 'Number of pages (optional, positive integer)';
COMMENT ON COLUMN books.description IS 'Book description/summary (optional, unlimited text)';
COMMENT ON COLUMN books.cover_image_url IS 'URL to book cover image (optional, max 500 characters)';
COMMENT ON COLUMN books.price IS 'Book price (optional, non-negative decimal with 2 decimal places)';
COMMENT ON COLUMN books.stock_quantity IS 'Available stock quantity (default 0, non-negative)';
COMMENT ON COLUMN books.rating IS 'Average rating 0.0-5.0 (optional, 1 decimal place)';
COMMENT ON COLUMN books.review_count IS 'Number of reviews (default 0, non-negative)';
COMMENT ON COLUMN books.view_count IS 'Number of times viewed (default 0, auto-incremented on GET)';
COMMENT ON COLUMN books.is_available IS 'Availability status (default TRUE)';
COMMENT ON COLUMN books.created_at IS 'Record creation timestamp (auto-generated)';
COMMENT ON COLUMN books.updated_at IS 'Record last update timestamp (auto-updated)';

-- =================================================================
-- Performance Indexes
-- =================================================================

-- Index for title searches (partial match, case-insensitive)
CREATE INDEX idx_books_title ON books(title);

-- Index for author searches (partial match, case-insensitive)
CREATE INDEX idx_books_author ON books(author);

-- Index for genre filtering (exact match)
CREATE INDEX idx_books_genre ON books(genre);

-- Index for publication year filtering and sorting
CREATE INDEX idx_books_publication_year ON books(publication_year);

-- Index for availability filtering
CREATE INDEX idx_books_is_available ON books(is_available);

-- Index for creation date sorting
CREATE INDEX idx_books_created_at ON books(created_at);

-- Composite Index for common search queries (genre + year)
CREATE INDEX idx_books_genre_year ON books(genre, publication_year);

-- Composite Index for common search queries (author + title)
CREATE INDEX idx_books_author_title ON books(author, title);

-- =================================================================
-- Database Statistics
-- =================================================================
-- Total Tables: 1 (books)
-- Total Columns: 18 (7 required, 11 optional/system-managed)
-- Total Indexes: 8 (1 unique index on isbn + 8 performance indexes)
-- Total Constraints: 13 (CHECK constraints for data validation)
-- Normalization: Third Normal Form (3NF)
-- =================================================================

-- =================================================================
-- Schema Verification Query
-- =================================================================
-- Run this query to verify the schema was created successfully:
-- SELECT table_name, column_name, data_type, is_nullable, column_default
-- FROM information_schema.columns
-- WHERE table_name = 'books'
-- ORDER BY ordinal_position;
-- =================================================================

-- =================================================================
-- Index Verification Query
-- =================================================================
-- Run this query to verify all indexes were created:
-- SELECT index_name, table_name, column_name
-- FROM information_schema.indexes
-- WHERE table_name = 'books'
-- ORDER BY index_name;
-- =================================================================
