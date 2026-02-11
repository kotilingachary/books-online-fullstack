# Data Requirements Analysis

## Project: Books Online - Book Management System
**Analysis Date:** 2024-02-10

---

## Identified Entities

### 1. Book (Primary Entity)

**Description:** Core entity representing a book in the library management system.

**Attributes:**

| Field | Type | Constraints | Source Screen | Notes |
|-------|------|-------------|---------------|-------|
| id | Long/Integer | PRIMARY KEY, AUTO_INCREMENT | All screens | System-generated unique identifier |
| title | String(200) | NOT NULL | Add/Edit/List/Details/Search | Book title |
| isbn | String(20) | UNIQUE, NOT NULL | Add/Edit/Details/Search | International Standard Book Number, read-only after creation |
| author | String(100) | NOT NULL | Add/Edit/List/Details/Search | Primary author name |
| publisher | String(100) | NULL | Add/Edit/Details/Search | Publishing company |
| genre | String(50) | NOT NULL | Add/Edit/List/Details/Search | Book category (Fiction, Non-Fiction, Dystopian, Romance, Mystery, Science, Biography, Fantasy) |
| publicationYear | Integer | NOT NULL, CHECK (year >= 1000 AND year <= current year) | Add/Edit/List/Details/Search | Year published |
| pages | Integer | NULL, CHECK (pages > 0) | Add/Edit/Details | Number of pages |
| language | String(30) | NOT NULL | Add/Edit/Details/Search | Book language (English, Spanish, French, German, Italian, Portuguese, Chinese, Japanese, Korean, Arabic) |
| price | Decimal(10,2) | NULL, CHECK (price >= 0) | Add/Edit/Details/Search | Book price in currency |
| stockQuantity | Integer | NULL, DEFAULT 0, CHECK (stockQuantity >= 0) | Add/Edit/Details | Available copies |
| description | Text | NULL | Add/Edit/Details | Full book description/summary |
| coverImageUrl | String(500) | NULL | All screens | Path/URL to book cover image |
| rating | Decimal(2,1) | NULL, CHECK (rating >= 0 AND rating <= 5.0) | Details | Average user rating (0.0 to 5.0) |
| reviewCount | Integer | NULL, DEFAULT 0 | Details | Number of user reviews |
| viewCount | Integer | NULL, DEFAULT 0 | Details | Number of times book details were viewed |
| isAvailable | Boolean | NOT NULL, DEFAULT true | Details | Computed: stockQuantity > 0 |
| createdAt | Timestamp | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Add/Edit/Details | Record creation timestamp |
| updatedAt | Timestamp | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE | Update/Details | Last modification timestamp |

---

## CRUD Operations Mapping

### Create Operations

**Screen:** Add Book Form (02_add_book_form.svg)

**Data Flow:**
1. User uploads book cover image → Store file, get coverImageUrl
2. User fills required fields: title, isbn, author, genre, publicationYear, language
3. User fills optional fields: publisher, pages, price, stockQuantity, description
4. System validates all inputs
5. System sets: createdAt = NOW(), updatedAt = NOW(), isAvailable = (stockQuantity > 0), viewCount = 0, reviewCount = 0, rating = NULL
6. System inserts new Book record
7. System redirects to Books List View

**Validation Rules:**
- ISBN must be unique in database
- ISBN format: Valid ISBN-10 or ISBN-13 format
- Title, ISBN, Author, Genre, Year, Language are required
- Publication year must be between 1000 and current year
- Price must be >= 0 if provided
- Stock quantity must be >= 0 if provided
- Pages must be > 0 if provided

---

### Read Operations

#### 1. List Books (Books List View - 01_books_list_view.svg)

**Query:**
```sql
SELECT id, title, author, genre, publicationYear, coverImageUrl
FROM books
WHERE (filters applied)
ORDER BY (sort field)
LIMIT (page size) OFFSET (page * page size)
```

**Filters:**
- Genre dropdown filter (exact match)
- Author dropdown filter (exact match)
- Year dropdown filter (exact match)
- Quick search (text search on title, author, isbn)

**Display Fields:** Cover thumbnail, Title, Author, Genre, Year, Actions (Edit, Delete)

**Pagination:** Default 4 books per page

---

#### 2. View Book Details (Book Details - 05_book_details_delete.svg)

**Query:**
```sql
SELECT * FROM books WHERE id = :bookId
```

**Post-Read Action:**
```sql
UPDATE books SET viewCount = viewCount + 1, updatedAt = NOW() WHERE id = :bookId
```

**Display Fields:** All 18 fields

**Derived Display:**
- Availability badge: "In Stock" if isAvailable = true
- Discount calculation: If sale price differs from base price
- Rating visualization: Star rating (★★★★☆)

---

#### 3. Advanced Search (Search Books - 03_search_books.svg)

**Query with Multiple Criteria:**
```sql
SELECT * FROM books
WHERE
  (:title IS NULL OR title LIKE CONCAT('%', :title, '%'))
  AND (:author IS NULL OR author LIKE CONCAT('%', :author, '%'))
  AND (:isbn IS NULL OR isbn = :isbn)
  AND (:publisher IS NULL OR publisher LIKE CONCAT('%', :publisher, '%'))
  AND (:genre IS NULL OR genre = :genre)
  AND (:language IS NULL OR language = :language)
  AND (:availability IS NULL OR isAvailable = :availability)
  AND (:yearFrom IS NULL OR publicationYear >= :yearFrom)
  AND (:yearTo IS NULL OR publicationYear <= :yearTo)
  AND (:minPrice IS NULL OR price >= :minPrice)
  AND (:maxPrice IS NULL OR price <= :maxPrice)
  AND (:inStockOnly = false OR stockQuantity > 0)
  AND (:onSale = false OR (/* sale condition */))
  AND (:newReleases = false OR publicationYear >= YEAR(CURRENT_DATE) - 1)
ORDER BY :sortBy
```

**Search Criteria:**
- Basic: Title, Author, ISBN, Publisher (partial match, case-insensitive)
- Filters: Genre, Language, Availability (exact match)
- Ranges: Publication Year (from/to), Price (min/max)
- Checkboxes: In Stock Only, On Sale, New Releases
- Sort By: Title, Author, Year (Newest/Oldest), Price (Low/High), Rating

---

### Update Operations

**Screen:** Update Book Form (04_update_book_form.svg)

**Data Flow:**
1. System loads existing book data: `SELECT * FROM books WHERE id = :bookId`
2. System pre-fills all form fields with current values
3. System sets ISBN field to read-only (cannot be changed)
4. User modifies desired fields
5. User can upload new cover image (optional)
6. System validates modified fields
7. System updates only changed fields
8. System sets: updatedAt = NOW()
9. System updates isAvailable based on new stockQuantity
10. System commits update
11. System redirects to Book Details or Books List View

**Update Query:**
```sql
UPDATE books
SET
  title = :title,
  author = :author,
  publisher = :publisher,
  genre = :genre,
  publicationYear = :publicationYear,
  pages = :pages,
  language = :language,
  price = :price,
  stockQuantity = :stockQuantity,
  description = :description,
  coverImageUrl = :coverImageUrl,
  isAvailable = (:stockQuantity > 0),
  updatedAt = NOW()
WHERE id = :bookId
```

**Read-Only Fields:** isbn, id, createdAt

**System-Managed Fields:** updatedAt, isAvailable

---

### Delete Operations

**Screen:** Book Details with Delete Modal (05_book_details_delete.svg)

**Data Flow:**
1. User clicks "Delete Book" button on Book Details screen
2. System displays delete confirmation modal with book details (title, ISBN)
3. User confirms deletion by clicking "Delete Permanently"
4. System executes soft or hard delete
5. System redirects to Books List View

**Hard Delete (Recommended for this system):**
```sql
DELETE FROM books WHERE id = :bookId
```

**Soft Delete (Alternative):**
```sql
UPDATE books SET isDeleted = true, deletedAt = NOW() WHERE id = :bookId
```

**Confirmation Data Displayed:**
- Book Title
- Book ISBN
- Warning: "This action cannot be undone"

---

## Data Validation Requirements

### Field-Level Validation

| Field | Validation Rule | Error Message |
|-------|----------------|---------------|
| title | Required, 1-200 chars | "Title is required and must be between 1-200 characters" |
| isbn | Required, unique, valid ISBN-10/13 format | "ISBN is required and must be a valid ISBN-10 or ISBN-13 format" |
| isbn (uniqueness) | Must not exist in database | "This ISBN already exists in the system" |
| author | Required, 1-100 chars | "Author is required and must be between 1-100 characters" |
| publisher | Optional, max 100 chars | "Publisher must be less than 100 characters" |
| genre | Required, must be in predefined list | "Genre is required. Please select a valid genre." |
| publicationYear | Required, integer, 1000 <= year <= current year | "Publication year must be between 1000 and {currentYear}" |
| pages | Optional, integer > 0 | "Pages must be a positive number" |
| language | Required, must be in predefined list | "Language is required. Please select a valid language." |
| price | Optional, decimal >= 0 | "Price must be a positive number" |
| stockQuantity | Optional, integer >= 0 | "Stock quantity must be zero or positive" |
| description | Optional, max 5000 chars | "Description must be less than 5000 characters" |
| coverImageUrl | Optional, valid URL or file path | "Invalid image URL" |

### Business Rules

1. **Stock Availability:**
   - If stockQuantity > 0 → isAvailable = true
   - If stockQuantity = 0 → isAvailable = false

2. **ISBN Immutability:**
   - ISBN can be set during creation
   - ISBN cannot be changed after creation (read-only in update form)

3. **Automatic Timestamps:**
   - createdAt set on INSERT
   - updatedAt updated on every UPDATE

4. **View Count:**
   - Incremented every time Book Details screen is accessed
   - Never decremented

5. **Rating and Reviews:**
   - Rating calculated from user reviews (future feature)
   - reviewCount = number of submitted reviews
   - If no reviews, rating = NULL

---

## Entity Relationships

### Current System (No relationships)
The wireframes show a single-entity system with no foreign keys or relationships to other entities.

### Future Expansion Possibilities

1. **Book ↔ Author (Many-to-Many)**
   - Separate Author entity for books with multiple authors
   - Join table: book_authors

2. **Book ↔ Category/Genre (Many-to-One)**
   - Separate Genre entity with id and name
   - Book.genreId → Genre.id

3. **Book ↔ Review (One-to-Many)**
   - Review entity: id, bookId, userId, rating, comment, createdAt
   - Book.rating computed from AVG(Review.rating)
   - Book.reviewCount = COUNT(Review)

4. **Book ↔ Publisher (Many-to-One)**
   - Separate Publisher entity: id, name, country, foundedYear
   - Book.publisherId → Publisher.id

5. **Book ↔ Language (Many-to-One)**
   - Separate Language entity: id, name, iso_code
   - Book.languageId → Language.id

6. **Book ↔ Inventory (One-to-Many)**
   - InventoryTransaction entity for tracking stock changes
   - Fields: id, bookId, type (add/remove/sale/return), quantity, date

---

## Enumerated Values

### Genre Options (Dropdown)
- Fiction
- Non-Fiction
- Dystopian
- Romance
- Mystery
- Science
- Biography
- Fantasy

**Recommendation:** Store as ENUM in database or separate lookup table

---

### Language Options (Dropdown)
- English
- Spanish
- French
- German
- Italian
- Portuguese
- Chinese
- Japanese
- Korean
- Arabic

**Recommendation:** Store as ENUM in database or separate lookup table with ISO codes

---

### Availability Filter Options
- All
- Available (In Stock)
- Out of Stock

**Mapped to:** isAvailable field (true/false/null)

---

## Data Volume Estimates

Based on wireframe pagination showing "Showing 1-4 of 24 books":

- **Expected Data Volume:** Small to medium (10-10,000 books)
- **Pagination:** 4 books per page (configurable)
- **Search Performance:** Full-text search recommended for title, author, description fields
- **Image Storage:** External storage (S3, local file system) with URL references

---

## Database Indexes Recommended

### Primary Indexes
```sql
PRIMARY KEY (id)
UNIQUE INDEX idx_isbn ON books(isbn)
```

### Performance Indexes
```sql
INDEX idx_title ON books(title)
INDEX idx_author ON books(author)
INDEX idx_genre ON books(genre)
INDEX idx_year ON books(publicationYear)
INDEX idx_available ON books(isAvailable)
INDEX idx_created ON books(createdAt)
```

### Composite Indexes
```sql
INDEX idx_genre_year ON books(genre, publicationYear)
INDEX idx_author_title ON books(author, title)
```

---

## Data Migration and Seeding

### Seed Data Requirements

For development and testing, create seed data with:
- At least 24 books (to show pagination)
- Mix of genres (2-3 books per genre)
- Mix of publication years (1950-2024)
- Mix of languages (primarily English, some others)
- Mix of availability (some in stock, some out of stock)
- Various price points ($5.99 - $49.99)
- Sample book covers (placeholder images)

### Sample Seed Books
1. **1984** by George Orwell - Dystopian, 1949, English
2. **To Kill a Mockingbird** by Harper Lee - Fiction, 1960, English
3. **The Great Gatsby** by F. Scott Fitzgerald - Fiction, 1925, English
4. **Pride and Prejudice** by Jane Austen - Romance, 1813, English
5. (20 more...)

---

## Data Export Requirements

From Book Details screen, "Export Data" button should support:

**Export Formats:**
- JSON: Complete book object with all 18 fields
- CSV: Tabular format for spreadsheet import
- PDF: Formatted document with cover image and details

**Export File Naming:**
- Pattern: `book_{isbn}_{date}.{format}`
- Example: `book_9780451524935_2024-02-10.json`

---

## Data Import Requirements (Future)

**Bulk Import Format:** CSV with columns matching book fields

**Required Columns:** title, isbn, author, genre, publicationYear, language

**Optional Columns:** publisher, pages, price, stockQuantity, description, coverImageUrl

**Import Validation:**
- Check for duplicate ISBNs
- Validate all data types
- Validate enumerated values (genre, language)
- Report validation errors with row numbers

---

## Summary

- **Total Entities:** 1 (Book)
- **Total Fields:** 18 per book
- **CRUD Operations:** Full Create, Read, Update, Delete support
- **Data Validation:** 14 field-level validations, 5 business rules
- **Indexing Strategy:** 1 primary key, 1 unique constraint, 8 performance indexes
- **Relationships:** None (single-entity system)
- **Future Expansion:** 6 potential entity relationships identified

All data requirements are derived directly from the 5 wireframe screens and support the complete user flows identified in the analysis.
