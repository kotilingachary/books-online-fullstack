# Entity-Relationship Diagram

**Database:** Books Online - Book Management System
**Version:** 1.0
**Generated:** 2026-02-11
**Normalization:** Third Normal Form (3NF)

---

## Overview

The Books Online database is a **single-entity system** in V1, consisting of one primary table: **books**. This design follows database normalization principles (3NF) and includes comprehensive constraints, indexes, and audit columns.

---

## Entity: books

### Entity Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                          BOOKS                               │
├──────────────────────────────────────────────────────────────┤
│  PK  id                    BIGINT AUTO_INCREMENT             │
│                                                               │
│  ┌─ REQUIRED FIELDS (Book Identification) ─────────────┐    │
│  │  title                 VARCHAR(200) NOT NULL         │    │
│  │  isbn                  VARCHAR(20) NOT NULL UNIQUE   │    │
│  │  author                VARCHAR(100) NOT NULL         │    │
│  │  genre                 VARCHAR(50) NOT NULL          │    │
│  │  publication_year      INT NOT NULL                  │    │
│  │  language              VARCHAR(30) NOT NULL          │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─ OPTIONAL FIELDS (Book Metadata) ────────────────────┐    │
│  │  publisher             VARCHAR(100)                   │    │
│  │  pages                 INT                            │    │
│  │  description           TEXT                           │    │
│  │  cover_image_url       VARCHAR(500)                   │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─ PRICING AND INVENTORY ───────────────────────────────┐    │
│  │  price                 DECIMAL(10, 2)                 │    │
│  │  stock_quantity        INT NOT NULL DEFAULT 0         │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─ RATING AND ENGAGEMENT ───────────────────────────────┐    │
│  │  rating                DECIMAL(2, 1)                  │    │
│  │  review_count          INT NOT NULL DEFAULT 0         │    │
│  │  view_count            INT NOT NULL DEFAULT 0         │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─ AVAILABILITY ────────────────────────────────────────┐    │
│  │  is_available          BOOLEAN NOT NULL DEFAULT TRUE  │    │
│  └──────────────────────────────────────────────────────┘    │
│                                                               │
│  ┌─ AUDIT COLUMNS (System-Managed) ─────────────────────┐    │
│  │  created_at            TIMESTAMP NOT NULL             │    │
│  │  updated_at            TIMESTAMP NOT NULL             │    │
│  └──────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────┘
```

---

## Complete Attribute List

### books Table

| # | Column | Data Type | Nullable | Default | Description |
|---|--------|-----------|----------|---------|-------------|
| 1 | **id** | BIGINT | NO | AUTO | Primary key (auto-generated) |
| 2 | **title** | VARCHAR(200) | NO | - | Book title |
| 3 | **isbn** | VARCHAR(20) | NO | - | ISBN (unique identifier) |
| 4 | **author** | VARCHAR(100) | NO | - | Author name(s) |
| 5 | **genre** | VARCHAR(50) | NO | - | Book genre/category |
| 6 | **publication_year** | INT | NO | - | Year of publication |
| 7 | **language** | VARCHAR(30) | NO | - | Primary language |
| 8 | **publisher** | VARCHAR(100) | YES | NULL | Publisher name |
| 9 | **pages** | INT | YES | NULL | Number of pages |
| 10 | **description** | TEXT | YES | NULL | Book description |
| 11 | **cover_image_url** | VARCHAR(500) | YES | NULL | Cover image URL |
| 12 | **price** | DECIMAL(10, 2) | YES | NULL | Book price |
| 13 | **stock_quantity** | INT | NO | 0 | Stock quantity |
| 14 | **rating** | DECIMAL(2, 1) | YES | NULL | Average rating (0-5) |
| 15 | **review_count** | INT | NO | 0 | Number of reviews |
| 16 | **view_count** | INT | NO | 0 | Number of views |
| 17 | **is_available** | BOOLEAN | NO | TRUE | Availability status |
| 18 | **created_at** | TIMESTAMP | NO | CURRENT_TIMESTAMP | Creation timestamp |
| 19 | **updated_at** | TIMESTAMP | NO | CURRENT_TIMESTAMP | Last update timestamp |

---

## Relationships

### V1: No Relationships (Single-Entity System)

The Books Online V1 database is a **single-entity system** with no foreign key relationships. This design provides:

- ✅ **Simplicity**: Easy to understand and maintain
- ✅ **Performance**: No JOIN operations required
- ✅ **Scalability**: Can easily add related entities in V2

### V2: Planned Relationships (Future)

Future versions will introduce additional entities:

```
┌─────────────┐     1       ∞    ┌─────────────┐
│    users    │──────────────────▶│   orders    │
│             │                   │             │
│  id (PK)    │                   │  id (PK)    │
│  email      │                   │  user_id    │
│  name       │                   └──────┬──────┘
│  role       │                          │
└─────────────┘                          │ 1
                                         │
                                         │
                                         ▼ ∞
┌─────────────┐                   ┌─────────────┐
│    books    │                   │ order_items │
│             │                   │             │
│  id (PK)    │◀──────────────────│  id (PK)    │
│  title      │         ∞         │  order_id   │
│  isbn       │                   │  book_id    │
│  author     │                   │  quantity   │
└─────────────┘                   └─────────────┘

Legend:
  1 ──────▶ ∞  One-to-Many relationship
  PK           Primary Key
  FK           Foreign Key
```

**Planned V2 Entities:**
- `users` - User accounts and authentication
- `orders` - Book orders
- `order_items` - Order line items (many-to-many bridge)
- `reviews` - Book reviews (1:N with books)
- `categories` - Book categories (1:N with books)

---

## Constraints

### Primary Key

| Table | Column | Type |
|-------|--------|------|
| books | id | BIGINT AUTO_INCREMENT |

### Unique Constraints

| Table | Column(s) | Purpose |
|-------|-----------|---------|
| books | isbn | Ensure ISBN uniqueness |

### Check Constraints (Data Validation)

| # | Constraint Name | Column | Rule | Purpose |
|---|-----------------|--------|------|---------|
| 1 | chk_books_isbn_length | isbn | LENGTH(isbn) BETWEEN 10 AND 20 | Validate ISBN format |
| 2 | chk_books_title_length | title | LENGTH(title) BETWEEN 1 AND 200 | Validate title length |
| 3 | chk_books_author_length | author | LENGTH(author) BETWEEN 1 AND 100 | Validate author length |
| 4 | chk_books_genre_length | genre | LENGTH(genre) BETWEEN 1 AND 50 | Validate genre length |
| 5 | chk_books_language_length | language | LENGTH(language) BETWEEN 1 AND 30 | Validate language length |
| 6 | chk_books_year_range | publication_year | year BETWEEN 1000 AND 2100 | Validate year range |
| 7 | chk_books_pages_positive | pages | pages IS NULL OR pages > 0 | Pages must be positive |
| 8 | chk_books_price_nonnegative | price | price IS NULL OR price >= 0 | Price cannot be negative |
| 9 | chk_books_stock_nonnegative | stock_quantity | stock_quantity >= 0 | Stock cannot be negative |
| 10 | chk_books_rating_range | rating | rating IS NULL OR rating BETWEEN 0.0 AND 5.0 | Rating 0-5 stars |
| 11 | chk_books_review_count_nonnegative | review_count | review_count >= 0 | Review count cannot be negative |
| 12 | chk_books_view_count_nonnegative | view_count | view_count >= 0 | View count cannot be negative |

**Total Check Constraints:** 12

---

## Indexes

### Index Strategy

Indexes are created on columns frequently used in:
- **WHERE clauses**: Filtering and searching
- **ORDER BY clauses**: Sorting results
- **JOIN conditions**: (Future V2)

### Index List

| # | Index Name | Table | Column(s) | Type | Purpose |
|---|------------|-------|-----------|------|---------|
| 1 | PRIMARY | books | id | UNIQUE | Primary key (auto-created) |
| 2 | UNIQUE | books | isbn | UNIQUE | ISBN uniqueness (auto-created) |
| 3 | idx_books_title | books | title | NON-UNIQUE | Title searches |
| 4 | idx_books_author | books | author | NON-UNIQUE | Author searches |
| 5 | idx_books_genre | books | genre | NON-UNIQUE | Genre filtering |
| 6 | idx_books_publication_year | books | publication_year | NON-UNIQUE | Year filtering/sorting |
| 7 | idx_books_is_available | books | is_available | NON-UNIQUE | Availability filtering |
| 8 | idx_books_created_at | books | created_at | NON-UNIQUE | Creation date sorting |
| 9 | idx_books_genre_year | books | genre, publication_year | COMPOSITE | Common search pattern |
| 10 | idx_books_author_title | books | author, title | COMPOSITE | Common search pattern |

**Total Indexes:** 10 (2 unique + 8 performance indexes)

### Composite Index Benefits

**idx_books_genre_year (genre, publication_year):**
- Optimizes queries like: `WHERE genre = 'Programming' AND publication_year BETWEEN 2010 AND 2023`
- Supports genre-only queries: `WHERE genre = 'Programming'`

**idx_books_author_title (author, title):**
- Optimizes queries like: `WHERE author = 'Martin' AND title LIKE '%Clean%'`
- Supports author-only queries: `WHERE author = 'Martin'`

---

## Normalization Analysis

### Third Normal Form (3NF) Compliance

The `books` table is in **Third Normal Form (3NF)**:

#### 1st Normal Form (1NF) ✅
- **No repeating groups**: Each column contains atomic values
- **No multi-valued attributes**: Author names stored as single VARCHAR (if multiple authors, comma-separated or use JOIN table in V2)
- **Unique rows**: Primary key (id) ensures uniqueness

#### 2nd Normal Form (2NF) ✅
- **All non-key attributes depend on the entire primary key**: Since we have a single-column primary key (id), all attributes depend on it
- **No partial dependencies**: N/A (single-column PK)

#### 3rd Normal Form (3NF) ✅
- **No transitive dependencies**: All non-key attributes depend directly on the primary key
- **Example analysis**:
  - `title` depends on `id` (not on any other non-key attribute)
  - `author` depends on `id` (not on title or genre)
  - `price` depends on `id` (not on pages or rating)
  - No attribute depends on another non-key attribute

### Denormalization Decisions

**Why not separate tables for authors, genres, publishers?**

**V1 Design Decision:** Keep simple for MVP
- ✅ **Simplicity**: Single table is easier to query
- ✅ **Performance**: No JOINs required
- ✅ **Flexibility**: Allows for multiple authors per book (comma-separated)

**V2 Enhancement:** Normalize further if needed
- Create `authors` table with many-to-many relationship
- Create `genres` table with foreign key
- Create `publishers` table with foreign key

---

## Data Types

### Data Type Choices and Rationale

| Column | Data Type | Rationale |
|--------|-----------|-----------|
| id | BIGINT | Large range (2^63 - 1), supports millions of books |
| title | VARCHAR(200) | Most book titles < 200 chars, indexed for search |
| isbn | VARCHAR(20) | ISBN-13 format (13 digits), ISBN-10 (10 digits), hyphens |
| author | VARCHAR(100) | Author names typically < 100 chars |
| genre | VARCHAR(50) | Genre names typically < 50 chars |
| publication_year | INT | Range 1000-2100, 4-byte integer sufficient |
| language | VARCHAR(30) | Language names typically < 30 chars ("English", "Spanish") |
| publisher | VARCHAR(100) | Publisher names typically < 100 chars |
| pages | INT | Page count up to 2,147,483,647 (4-byte int) |
| description | TEXT | Unlimited length for long descriptions |
| cover_image_url | VARCHAR(500) | URL length up to 500 chars |
| price | DECIMAL(10, 2) | Max $99,999,999.99, 2 decimal places |
| stock_quantity | INT | Inventory count up to 2 billion |
| rating | DECIMAL(2, 1) | Range 0.0-5.0, one decimal place |
| review_count | INT | Review count up to 2 billion |
| view_count | INT | View count up to 2 billion |
| is_available | BOOLEAN | TRUE/FALSE flag |
| created_at | TIMESTAMP | Date/time with millisecond precision |
| updated_at | TIMESTAMP | Date/time with millisecond precision |

### VARCHAR vs TEXT

- **VARCHAR(n)**: Fixed maximum length, indexed, faster for searches
- **TEXT**: Unlimited length, not indexed (or partially indexed), slower

**Choice**: Use VARCHAR for indexed fields, TEXT for descriptions

---

## Query Performance

### Indexed Query Examples (Fast)

```sql
-- ✅ FAST: Uses idx_books_title
SELECT * FROM books WHERE title LIKE 'Clean%';

-- ✅ FAST: Uses idx_books_author
SELECT * FROM books WHERE author = 'Robert C. Martin';

-- ✅ FAST: Uses idx_books_genre_year (composite index)
SELECT * FROM books
WHERE genre = 'Programming'
  AND publication_year BETWEEN 2010 AND 2023;

-- ✅ FAST: Uses idx_books_is_available
SELECT * FROM books WHERE is_available = TRUE;

-- ✅ FAST: Uses idx_books_created_at
SELECT * FROM books ORDER BY created_at DESC LIMIT 10;
```

### Non-Indexed Query Examples (Slower)

```sql
-- ⚠️ SLOW: No index on publisher
SELECT * FROM books WHERE publisher = 'Prentice Hall';

-- ⚠️ SLOW: No index on description (TEXT field)
SELECT * FROM books WHERE description LIKE '%software%';

-- ⚠️ SLOW: No index on price
SELECT * FROM books WHERE price BETWEEN 30 AND 60;

-- ⚠️ SLOW: Function on indexed column (can't use index)
SELECT * FROM books WHERE UPPER(title) = 'CLEAN CODE';
```

**Optimization Tip:** Add indexes if these queries become common:
```sql
CREATE INDEX idx_books_publisher ON books(publisher);
CREATE INDEX idx_books_price ON books(price);
```

---

## Database Constraints Summary

### Summary Table

| Constraint Type | Count | Purpose |
|-----------------|-------|---------|
| Primary Key | 1 | Unique identifier (id) |
| Unique | 1 | ISBN uniqueness |
| Check | 12 | Data validation |
| NOT NULL | 10 | Required fields |
| Default | 4 | Default values (stock_quantity, review_count, view_count, is_available) |
| Timestamps | 2 | Audit trail (created_at, updated_at) |

**Total Constraints:** 30 (comprehensive data integrity)

---

## Schema Evolution (V1 → V2)

### V1 (Current): Single Entity

```
books
```

### V2 (Future): Multi-Entity with Relationships

```
users ──▶ orders ──▶ order_items ──▶ books
                                      ▲
                                      │
                          reviews ────┘
                          categories ─┘
```

**Migration Path:**
1. Add `users` table with authentication
2. Add `orders` and `order_items` for purchase history
3. Add `reviews` table (1:N with books)
4. Add `categories` table (1:N with books)
5. Add `book_authors` bridge table (many-to-many)

---

## Database Maintenance

### Regular Maintenance Tasks

**Daily:**
- Monitor query performance
- Check error logs

**Weekly:**
- Update database statistics: `ANALYZE TABLE books;`
- Check index usage: `SHOW INDEX FROM books;`

**Monthly:**
- Review slow query log
- Optimize tables: `OPTIMIZE TABLE books;`
- Review and add indexes for new query patterns

**Quarterly:**
- Full backup and restore test
- Review normalization (consider further normalization or denormalization)

---

## Summary

### Database Design Metrics

| Metric | Value |
|--------|-------|
| **Total Tables** | 1 |
| **Total Columns** | 18 |
| **Required Fields** | 7 |
| **Optional Fields** | 7 |
| **System-Managed Fields** | 4 |
| **Primary Keys** | 1 |
| **Foreign Keys** | 0 (V1), 4+ (V2) |
| **Unique Constraints** | 1 (ISBN) |
| **Check Constraints** | 12 |
| **Total Indexes** | 10 |
| **Composite Indexes** | 2 |
| **Normalization Level** | 3NF ✅ |

### Design Principles Applied

- ✅ **Third Normal Form (3NF)**: No redundancy, all attributes depend on PK
- ✅ **Comprehensive Constraints**: 12 CHECK constraints for data validation
- ✅ **Strategic Indexing**: 8 performance indexes on commonly queried columns
- ✅ **Audit Trail**: created_at and updated_at on all tables
- ✅ **Data Integrity**: NOT NULL, UNIQUE, CHECK constraints enforce rules
- ✅ **Scalability**: Design allows for easy V2 expansion

---

**Next Steps:**
1. Review schema with development team
2. Test all constraints with sample data
3. Generate JPA entities from schema
4. Create database migration scripts (Flyway/Liquibase)
5. Plan V2 enhancements (users, orders, reviews)
