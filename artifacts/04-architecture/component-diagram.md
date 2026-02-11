# Component Architecture Diagram

**Product:** Books Online - Book Management System
**Version:** 1.0
**Generated:** 2026-02-11
**Purpose:** Visual representation of frontend and backend component structure

---

## Table of Contents

1. [Frontend Component Architecture](#frontend-component-architecture)
2. [Backend Component Architecture](#backend-component-architecture)
3. [End-to-End Data Flow](#end-to-end-data-flow)
4. [Component Interaction Patterns](#component-interaction-patterns)
5. [State Management Flow](#state-management-flow)

---

## Frontend Component Architecture

### Component Hierarchy (Atomic Design Pattern)

```
src/
├── App.jsx (Root)
│   ├── Layout Component
│   │   ├── Header
│   │   │   ├── Logo (Atom)
│   │   │   ├── Navigation (Molecule)
│   │   │   └── SearchBar (Molecule)
│   │   └── Footer
│   │       ├── Copyright (Atom)
│   │       └── Links (Molecule)
│   │
│   └── AppRoutes
│       ├── BooksListPage (Page/Template)
│       │   ├── PageHeader (Organism)
│       │   │   ├── Title (Atom)
│       │   │   ├── AddButton (Atom)
│       │   │   └── QuickSearch (Molecule)
│       │   ├── FiltersPanel (Organism)
│       │   │   ├── GenreFilter (Molecule)
│       │   │   ├── AuthorFilter (Molecule)
│       │   │   ├── YearFilter (Molecule)
│       │   │   └── ClearFilters (Atom)
│       │   ├── BooksTable (Organism)
│       │   │   ├── TableHeader (Molecule)
│       │   │   │   ├── SortableColumn (Atom) x 6
│       │   │   │   └── ActionsColumn (Atom)
│       │   │   ├── TableBody (Molecule)
│       │   │   │   └── BookRow (Molecule) x N
│       │   │   │       ├── CoverImage (Atom)
│       │   │   │       ├── TextCell (Atom) x 4
│       │   │   │       └── ActionButtons (Molecule)
│       │   │   │           ├── EditButton (Atom)
│       │   │   │           └── DeleteButton (Atom)
│       │   │   └── LoadingSkeleton (Molecule)
│       │   └── Pagination (Organism)
│       │       ├── PageInfo (Atom)
│       │       ├── PrevButton (Atom)
│       │       ├── PageNumbers (Molecule)
│       │       └── NextButton (Atom)
│       │
│       ├── AddBookPage (Page/Template)
│       │   ├── PageHeader (Organism)
│       │   │   ├── Title (Atom)
│       │   │   ├── Breadcrumbs (Molecule)
│       │   │   └── BackButton (Atom)
│       │   └── BookForm (Organism)
│       │       ├── FormSection: Required Fields
│       │       │   ├── TextInput (Molecule) x 4
│       │       │   │   ├── Label (Atom)
│       │       │   │   ├── Input (Atom)
│       │       │   │   └── Error (Atom)
│       │       │   ├── SelectInput (Molecule) x 2
│       │       │   └── NumberInput (Molecule)
│       │       ├── FormSection: Optional Fields
│       │       │   ├── TextInput (Molecule) x 2
│       │       │   ├── NumberInput (Molecule) x 3
│       │       │   └── TextArea (Molecule)
│       │       ├── FormSection: Cover Image
│       │       │   ├── ImageUpload (Molecule)
│       │       │   │   ├── DropZone (Atom)
│       │       │   │   ├── Preview (Atom)
│       │       │   │   └── RemoveButton (Atom)
│       │       │   └── UploadButton (Atom)
│       │       └── FormActions (Molecule)
│       │           ├── CancelButton (Atom)
│       │           ├── SaveButton (Atom)
│       │           └── LoadingSpinner (Atom)
│       │
│       ├── EditBookPage (Page/Template)
│       │   ├── PageHeader (Organism)
│       │   └── BookForm (Organism) [Reused, pre-populated]
│       │
│       ├── BookDetailsPage (Page/Template)
│       │   ├── PageHeader (Organism)
│       │   │   ├── Title (Atom)
│       │   │   ├── Breadcrumbs (Molecule)
│       │   │   └── BackButton (Atom)
│       │   ├── BookDetails (Organism)
│       │   │   ├── CoverSection (Molecule)
│       │   │   │   ├── LargeCover (Atom)
│       │   │   │   └── Placeholder (Atom)
│       │   │   ├── InfoSection (Molecule)
│       │   │   │   ├── Title (Atom)
│       │   │   │   ├── Author (Atom)
│       │   │   │   ├── Rating (Molecule)
│       │   │   │   │   ├── Stars (Atom) x 5
│       │   │   │   │   └── ReviewCount (Atom)
│       │   │   │   ├── PriceBox (Molecule)
│       │   │   │   │   ├── CurrentPrice (Atom)
│       │   │   │   │   ├── OriginalPrice (Atom)
│       │   │   │   │   └── Discount (Atom)
│       │   │   │   └── AvailabilityBadge (Atom)
│       │   │   ├── MetadataGrid (Molecule)
│       │   │   │   └── MetadataItem (Atom) x 8
│       │   │   │       ├── Label (Atom)
│       │   │   │       └── Value (Atom)
│       │   │   ├── DescriptionSection (Molecule)
│       │   │   │   ├── Heading (Atom)
│       │   │   │   └── Text (Atom)
│       │   │   └── MetadataBar (Molecule)
│       │   │       ├── BookId (Atom)
│       │   │       ├── CreatedAt (Atom)
│       │   │       ├── UpdatedAt (Atom)
│       │   │       └── ViewCount (Atom)
│       │   └── ActionButtons (Organism)
│       │       ├── EditButton (Atom)
│       │       ├── DuplicateButton (Atom)
│       │       ├── ExportButton (Atom)
│       │       │   └── ExportMenu (Molecule)
│       │       │       ├── JSON (Atom)
│       │       │       ├── CSV (Atom)
│       │       │       └── PDF (Atom)
│       │       └── DeleteButton (Atom)
│       │
│       └── SearchBooksPage (Page/Template)
│           ├── PageHeader (Organism)
│           └── AdvancedSearchForm (Organism)
│               ├── QuickSearch (Molecule)
│               ├── TextFieldsSection (Molecule)
│               │   ├── TitleInput (Molecule)
│               │   ├── AuthorInput (Molecule)
│               │   ├── ISBNInput (Molecule)
│               │   └── PublisherInput (Molecule)
│               ├── DropdownSection (Molecule)
│               │   ├── GenreSelect (Molecule)
│               │   ├── LanguageSelect (Molecule)
│               │   └── AvailabilitySelect (Molecule)
│               ├── RangeSection (Molecule)
│               │   ├── YearRange (Molecule)
│               │   │   ├── MinYear (Atom)
│               │   │   └── MaxYear (Atom)
│               │   └── PriceRange (Molecule)
│               │       ├── MinPrice (Atom)
│               │       └── MaxPrice (Atom)
│               ├── BooleanSection (Molecule)
│               │   ├── InStockCheckbox (Molecule)
│               │   ├── OnSaleCheckbox (Molecule)
│               │   └── NewReleasesCheckbox (Molecule)
│               ├── SortSection (Molecule)
│               │   └── SortSelect (Molecule)
│               ├── ActionButtons (Molecule)
│               │   ├── SearchButton (Atom)
│               │   └── ClearButton (Atom)
│               └── ResultsTable (Organism) [Reused BooksTable]
│
└── Common Components (Shared)
    ├── Atoms
    │   ├── Button
    │   ├── Input
    │   ├── Select
    │   ├── Checkbox
    │   ├── Label
    │   ├── Badge
    │   ├── Spinner
    │   ├── Icon
    │   └── Image
    ├── Molecules
    │   ├── FormField (Label + Input + Error)
    │   ├── SearchInput (Input + Icon)
    │   ├── Dropdown (Select + Label + Error)
    │   └── Toast (Icon + Message + CloseButton)
    └── Organisms
        ├── Modal
        │   ├── Overlay (Atom)
        │   ├── ModalContent (Molecule)
        │   │   ├── Header (Molecule)
        │   │   ├── Body (Molecule)
        │   │   └── Footer (Molecule)
        │   └── CloseButton (Atom)
        └── ConfirmModal
            ├── Modal [Reused]
            ├── Title (Atom)
            ├── Message (Atom)
            ├── BookInfo (Molecule)
            └── Actions (Molecule)
                ├── CancelButton (Atom)
                └── ConfirmButton (Atom)
```

---

### Component Tree Visualization

```
                        ┌─────────────┐
                        │   App.jsx   │
                        └──────┬──────┘
                               │
                ┌──────────────┴──────────────┐
                │                             │
          ┌─────▼─────┐              ┌────────▼────────┐
          │  Layout   │              │   AppRoutes     │
          └─────┬─────┘              └────────┬────────┘
                │                             │
       ┌────────┴────────┐          ┌─────────┼─────────┬─────────┬──────────┐
       │                 │          │         │         │         │          │
  ┌────▼────┐      ┌────▼────┐ ┌───▼───┐ ┌──▼───┐ ┌───▼───┐ ┌───▼───┐ ┌────▼────┐
  │ Header  │      │ Footer  │ │ List  │ │ Add  │ │ Edit  │ │Detail │ │ Search  │
  └────┬────┘      └─────────┘ │ Page  │ │ Page │ │ Page  │ │ Page  │ │  Page   │
       │                        └───┬───┘ └──┬───┘ └───┬───┘ └───┬───┘ └────┬────┘
  ┌────┼────┬────────┐             │        │         │         │          │
  │    │    │        │             │        │         │         │          │
┌─▼┐ ┌─▼──┐ ┌───────▼───┐   ┌─────▼──┐ ┌───▼────┐ ┌──▼───┐ ┌──▼────┐ ┌───▼────┐
│Logo││Nav││SearchBar│   │BooksTable││BookForm││BookForm││Details││Advanced│
└───┘└────┘└───────────┘   └────────┘└────────┘└──────┘└───────┘│ Search │
                                  │         │        │        │   └────────┘
                            ┌─────┼─────┐   │    ┌───┼────┐   │
                            │     │     │   │    │   │    │   │
                        ┌───▼┐ ┌──▼──┐ ┌▼───▼┐┌──▼─┐│  ┌─▼───▼┐
                        │Table││Pagina││Form││Form││  │Action│
                        │Header││tion ││Section││Section││  │Buttons│
                        └─────┘└─────┘└────┘└────┘  └──────┘
```

---

### Frontend Component Details

#### 1. **Atoms** (Basic Building Blocks)

| Component | Props | Purpose |
|-----------|-------|---------|
| `Button` | `variant, size, onClick, disabled, loading, children` | Reusable button with variants (primary, secondary, danger) |
| `Input` | `type, name, value, onChange, placeholder, error` | Text input field |
| `Select` | `options, value, onChange, placeholder, error` | Dropdown select |
| `Label` | `htmlFor, required, children` | Form label |
| `Badge` | `variant, children` | Status badge (success, warning, danger, info) |
| `Spinner` | `size, color` | Loading spinner |
| `Icon` | `name, size, color` | Icon component (using icon library) |
| `Image` | `src, alt, fallback, className` | Image with fallback |

#### 2. **Molecules** (Component Combinations)

| Component | Composition | Purpose |
|-----------|-------------|---------|
| `FormField` | Label + Input + Error | Complete form input with validation |
| `SearchInput` | Input + Icon | Search input with search icon |
| `Dropdown` | Select + Label + Error | Complete dropdown with label and error |
| `Toast` | Icon + Message + CloseButton | Notification toast |
| `BookRow` | CoverImage + TextCell x 4 + ActionButtons | Single book table row |
| `Rating` | Stars x 5 + ReviewCount | Star rating display |
| `PriceBox` | CurrentPrice + OriginalPrice + Discount | Price display with discount |

#### 3. **Organisms** (Complex Components)

| Component | Purpose | Key Features |
|-----------|---------|--------------|
| `BooksTable` | Display paginated book list | Sorting, filtering, row click, skeleton loading |
| `BookForm` | Add/Edit book form | Validation, image upload, error handling |
| `BookDetails` | Display full book information | Two-column layout, metadata grid, actions |
| `AdvancedSearchForm` | Complex search interface | Multiple filter types, clear filters, sort |
| `Modal` | Reusable modal dialog | Backdrop, ESC to close, click outside to close |
| `ConfirmModal` | Confirmation dialog | Extends Modal, shows book details, confirm/cancel |

#### 4. **Pages/Templates** (Route Components)

| Page | Route | Main Organisms |
|------|-------|----------------|
| `BooksListPage` | `/` | BooksTable, Pagination, FiltersPanel |
| `AddBookPage` | `/books/add` | BookForm |
| `EditBookPage` | `/books/:id/edit` | BookForm (pre-populated) |
| `BookDetailsPage` | `/books/:id` | BookDetails, ActionButtons |
| `SearchBooksPage` | `/books/search` | AdvancedSearchForm, ResultsTable |

---

## Backend Component Architecture

### Layered Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          PRESENTATION LAYER                                 │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │                     REST Controllers (@RestController)                 │ │
│  │  ┌──────────────────────────────────────────────────────────────┐    │ │
│  │  │  BookController.java                                          │    │ │
│  │  │  - GET    /api/v1/books              → getAllBooks()          │    │ │
│  │  │  - GET    /api/v1/books/{id}         → getBookById()          │    │ │
│  │  │  - POST   /api/v1/books              → createBook()           │    │ │
│  │  │  - PUT    /api/v1/books/{id}         → updateBook()           │    │ │
│  │  │  - DELETE /api/v1/books/{id}         → deleteBook()           │    │ │
│  │  │  - GET    /api/v1/books/search       → searchBooks()          │    │ │
│  │  │  - POST   /api/v1/books/{id}/duplicate → duplicateBook()      │    │ │
│  │  │  - GET    /api/v1/books/{id}/export  → exportBook()           │    │ │
│  │  └──────────────────────────────────────────────────────────────┘    │ │
│  │                                  │                                     │ │
│  │                            Uses DTOs                                   │ │
│  │                                  │                                     │ │
│  │  ┌──────────────────────┬────────┴──────┬──────────────────────┐     │ │
│  │  │                      │               │                      │     │ │
│  │  │  BookRequest.java    │ BookResponse  │  BookSearchRequest   │     │ │
│  │  │  (Input DTO)         │  (Output DTO) │  (Search Params)     │     │ │
│  │  └──────────────────────┴───────────────┴──────────────────────┘     │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────┬────────────────────────────────────────┘
                                     │
                                     │ Calls
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                            BUSINESS LOGIC LAYER                             │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │                        Service Layer (@Service)                       │ │
│  │  ┌──────────────────────────────────────────────────────────────┐    │ │
│  │  │  BookService.java (Interface)                                 │    │ │
│  │  │  - List<Book> getAllBooks(Pageable)                          │    │ │
│  │  │  - Book getBookById(Long id)                                 │    │ │
│  │  │  - Book createBook(Book book)                                │    │ │
│  │  │  - Book updateBook(Long id, Book book)                       │    │ │
│  │  │  - void deleteBook(Long id)                                  │    │ │
│  │  │  - List<Book> searchBooks(SearchCriteria criteria)           │    │ │
│  │  │  - Book duplicateBook(Long id)                               │    │ │
│  │  │  - byte[] exportBook(Long id, String format)                 │    │ │
│  │  └──────────────────────────────────────────────────────────────┘    │ │
│  │                                  △                                     │ │
│  │                                  │ Implements                          │ │
│  │  ┌───────────────────────────────┴───────────────────────────────┐   │ │
│  │  │  BookServiceImpl.java (@Service)                              │   │ │
│  │  │  - Business logic implementation                              │   │ │
│  │  │  - Validation (ISBN uniqueness, required fields)              │   │ │
│  │  │  - Orchestration (increment view count, check availability)   │   │ │
│  │  │  - Exception handling (BookNotFoundException, etc.)           │   │ │
│  │  │  - Entity ↔ DTO mapping via BookMapper                       │   │ │
│  │  └────────────────────────────────────────────────────────────────   │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────┬────────────────────────────────────────┘
                                     │
                                     │ Uses
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              DATA ACCESS LAYER                              │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │                  Repository Layer (JpaRepository)                     │ │
│  │  ┌──────────────────────────────────────────────────────────────┐    │ │
│  │  │  BookRepository.java (Interface)                              │    │ │
│  │  │  extends JpaRepository<Book, Long>                           │    │ │
│  │  │                                                               │    │ │
│  │  │  - Automatic CRUD methods (inherited)                        │    │ │
│  │  │    • save(Book) → INSERT/UPDATE                              │    │ │
│  │  │    • findById(Long) → SELECT by ID                           │    │ │
│  │  │    • findAll(Pageable) → SELECT with pagination              │    │ │
│  │  │    • deleteById(Long) → DELETE                               │    │ │
│  │  │    • existsById(Long) → EXISTS check                         │    │ │
│  │  │                                                               │    │ │
│  │  │  - Custom query methods                                      │    │ │
│  │  │    • findByIsbn(String isbn)                                 │    │ │
│  │  │    • findByTitleContainingIgnoreCase(String title)           │    │ │
│  │  │    • findByAuthorContainingIgnoreCase(String author)         │    │ │
│  │  │    • findByGenre(String genre)                               │    │ │
│  │  │    • findByPublicationYearBetween(int startYear, endYear)    │    │ │
│  │  │    • findByIsAvailableTrue()                                 │    │ │
│  │  │                                                               │    │ │
│  │  │  - Custom @Query methods                                     │    │ │
│  │  │    • @Query("SELECT b FROM Book b WHERE ...")                │    │ │
│  │  │    • searchBooksAdvanced(Specification<Book>)                │    │ │
│  │  └──────────────────────────────────────────────────────────────┘    │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────┬────────────────────────────────────────┘
                                     │
                                     │ Manages
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                              PERSISTENCE LAYER                              │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │                         JPA Entity (@Entity)                          │ │
│  │  ┌──────────────────────────────────────────────────────────────┐    │ │
│  │  │  Book.java                                                    │    │ │
│  │  │  @Entity                                                      │    │ │
│  │  │  @Table(name = "books", indexes = {...})                     │    │ │
│  │  │                                                               │    │ │
│  │  │  Fields:                                                      │    │ │
│  │  │  - @Id @GeneratedValue Long id                               │    │ │
│  │  │  - @Column(nullable = false) String title                    │    │ │
│  │  │  - @Column(unique = true) String isbn                        │    │ │
│  │  │  - String author, genre, publisher                           │    │ │
│  │  │  - Integer publicationYear, pages, stockQuantity             │    │ │
│  │  │  - String language, description, coverImageUrl               │    │ │
│  │  │  - BigDecimal price                                          │    │ │
│  │  │  - BigDecimal rating                                         │    │ │
│  │  │  - Integer reviewCount, viewCount                            │    │ │
│  │  │  - Boolean isAvailable                                       │    │ │
│  │  │  - @CreationTimestamp LocalDateTime createdAt                │    │ │
│  │  │  - @UpdateTimestamp LocalDateTime updatedAt                  │    │ │
│  │  └──────────────────────────────────────────────────────────────┘    │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────┬────────────────────────────────────────┘
                                     │
                                     │ Persists to
                                     ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                                DATABASE LAYER                               │
│  ┌───────────────────────────────────────────────────────────────────────┐ │
│  │                       H2 Database (In-Memory/File)                    │ │
│  │  ┌──────────────────────────────────────────────────────────────┐    │ │
│  │  │  books table                                                  │    │ │
│  │  │  - 18 columns (7 required, 11 optional, 4 system-managed)    │    │ │
│  │  │  - Primary Key: id (BIGINT AUTO_INCREMENT)                   │    │ │
│  │  │  - Unique Constraint: isbn                                   │    │ │
│  │  │  - 8 Indexes for performance:                                │    │ │
│  │  │    • idx_title ON (title)                                    │    │ │
│  │  │    • idx_author ON (author)                                  │    │ │
│  │  │    • idx_genre ON (genre)                                    │    │ │
│  │  │    • idx_year ON (publication_year)                          │    │ │
│  │  │    • idx_available ON (is_available)                         │    │ │
│  │  │    • idx_created ON (created_at)                             │    │ │
│  │  │    • idx_genre_year ON (genre, publication_year)             │    │ │
│  │  │    • idx_author_title ON (author, title)                     │    │ │
│  │  └──────────────────────────────────────────────────────────────┘    │ │
│  └───────────────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

### Backend Package Structure

```
src/main/java/com/booksonline/
│
├── BooksonlineApplication.java (Main @SpringBootApplication)
│
├── controller/ (Presentation Layer)
│   ├── BookController.java
│   │   - @RestController
│   │   - @RequestMapping("/api/v1/books")
│   │   - @CrossOrigin(origins = "http://localhost:5173")
│   │   - Handles HTTP requests
│   │   - Returns ResponseEntity<BookResponse>
│   │   - Uses @Valid for input validation
│   └── advice/
│       └── GlobalExceptionHandler.java
│           - @RestControllerAdvice
│           - Handles exceptions globally
│           - Returns consistent error responses
│
├── service/ (Business Logic Layer)
│   ├── BookService.java (Interface)
│   │   - Defines business operations
│   └── impl/
│       └── BookServiceImpl.java
│           - @Service
│           - @Transactional
│           - Implements business logic
│           - Orchestrates repository calls
│           - Maps Entity ↔ DTO via BookMapper
│
├── repository/ (Data Access Layer)
│   └── BookRepository.java
│       - extends JpaRepository<Book, Long>
│       - Custom query methods
│       - Spring Data JPA auto-implementation
│
├── model/
│   ├── entity/
│   │   └── Book.java
│   │       - @Entity
│   │       - @Table(name = "books")
│   │       - JPA entity with annotations
│   │       - Lombok @Data, @Builder, @NoArgsConstructor, @AllArgsConstructor
│   │
│   └── dto/
│       ├── BookRequest.java
│       │   - Input DTO for POST/PUT
│       │   - @Valid validation annotations
│       │   - @NotNull, @NotBlank, @Size, @Min, @Max
│       ├── BookResponse.java
│       │   - Output DTO for GET responses
│       │   - Excludes sensitive/internal fields
│       └── BookSearchRequest.java
│           - Search criteria DTO
│           - Optional fields for filtering
│
├── mapper/
│   └── BookMapper.java
│       - @Mapper(componentModel = "spring") [MapStruct]
│       - Entity ↔ DTO conversion
│       - toEntity(BookRequest) → Book
│       - toResponse(Book) → BookResponse
│
├── exception/
│   ├── BookNotFoundException.java
│   ├── DuplicateIsbnException.java
│   └── InvalidBookDataException.java
│
├── config/
│   ├── CorsConfig.java
│   │   - @Configuration
│   │   - CORS configuration for frontend access
│   ├── OpenApiConfig.java
│   │   - @Configuration
│   │   - Springdoc OpenAPI configuration
│   └── JpaConfig.java
│       - @Configuration
│       - @EnableJpaAuditing
│       - JPA/Hibernate configuration
│
└── util/
    ├── validators/
    │   ├── IsbnValidator.java
    │   └── BookValidator.java
    └── constants/
        └── ApiConstants.java
```

---

## End-to-End Data Flow

### Example: Create New Book Flow

```
┌──────────┐         ┌──────────┐         ┌──────────┐         ┌──────────┐         ┌──────────┐
│  React   │         │  Axios   │         │Controller│         │ Service  │         │Repository│
│Component │         │  Client  │         │          │         │          │         │          │
└────┬─────┘         └────┬─────┘         └────┬─────┘         └────┬─────┘         └────┬─────┘
     │                    │                    │                    │                    │
     │ 1. Form Submit     │                    │                    │                    │
     │ handleSubmit()     │                    │                    │                    │
     ├───────────────────►│                    │                    │                    │
     │                    │ 2. POST Request    │                    │                    │
     │                    │ /api/v1/books      │                    │                    │
     │                    │ Body: BookRequest  │                    │                    │
     │                    ├───────────────────►│                    │                    │
     │                    │                    │ 3. @Valid Check    │                    │
     │                    │                    │ Validate DTO       │                    │
     │                    │                    ├──────────┐         │                    │
     │                    │                    │          │         │                    │
     │                    │                    │◄─────────┘         │                    │
     │                    │                    │ 4. Call Service    │                    │
     │                    │                    │ createBook(request)│                    │
     │                    │                    ├───────────────────►│                    │
     │                    │                    │                    │ 5. Business Logic  │
     │                    │                    │                    │ - Check ISBN unique│
     │                    │                    │                    │ - Map DTO→Entity   │
     │                    │                    │                    │ - Set defaults     │
     │                    │                    │                    ├──────────┐         │
     │                    │                    │                    │          │         │
     │                    │                    │                    │◄─────────┘         │
     │                    │                    │                    │ 6. Save Entity     │
     │                    │                    │                    │ repository.save()  │
     │                    │                    │                    ├───────────────────►│
     │                    │                    │                    │                    │ 7. SQL INSERT
     │                    │                    │                    │                    │ Hibernate Query
     │                    │                    │                    │                    ├──────────┐
     │                    │                    │                    │                    │          │
     │                    │                    │                    │                    │◄─────────┘
     │                    │                    │                    │ 8. Return Entity   │
     │                    │                    │                    │◄───────────────────┤
     │                    │                    │ 9. Map Entity→DTO  │                    │
     │                    │                    │ Return BookResponse│                    │
     │                    │                    │◄───────────────────┤                    │
     │                    │ 10. HTTP 201       │                    │                    │
     │                    │ Created            │                    │                    │
     │                    │ Body: BookResponse │                    │                    │
     │                    │◄───────────────────┤                    │                    │
     │ 11. Success        │                    │                    │                    │
     │ Show Toast         │                    │                    │                    │
     │ Navigate to List   │                    │                    │                    │
     │◄───────────────────┤                    │                    │                    │
     │                    │                    │                    │                    │
```

---

### Example: View Book Details Flow

```
┌──────────┐         ┌──────────┐         ┌──────────┐         ┌──────────┐         ┌──────────┐
│  React   │         │  Axios   │         │Controller│         │ Service  │         │Repository│
│Component │         │  Client  │         │          │         │          │         │          │
└────┬─────┘         └────┬─────┘         └────┬─────┘         └────┬─────┘         └────┬─────┘
     │                    │                    │                    │                    │
     │ 1. useEffect()     │                    │                    │                    │
     │ Fetch book details │                    │                    │                    │
     ├───────────────────►│                    │                    │                    │
     │                    │ 2. GET Request     │                    │                    │
     │                    │ /api/v1/books/123  │                    │                    │
     │                    ├───────────────────►│                    │                    │
     │                    │                    │ 3. Parse ID        │                    │
     │                    │                    │ @PathVariable      │                    │
     │                    │                    ├──────────┐         │                    │
     │                    │                    │          │         │                    │
     │                    │                    │◄─────────┘         │                    │
     │                    │                    │ 4. Call Service    │                    │
     │                    │                    │ getBookById(123L)  │                    │
     │                    │                    ├───────────────────►│                    │
     │                    │                    │                    │ 5. Find Entity     │
     │                    │                    │                    │ findById(123L)     │
     │                    │                    │                    ├───────────────────►│
     │                    │                    │                    │                    │ 6. SQL SELECT
     │                    │                    │                    │                    │ WHERE id = 123
     │                    │                    │                    │                    ├──────────┐
     │                    │                    │                    │                    │          │
     │                    │                    │                    │                    │◄─────────┘
     │                    │                    │                    │ 7. Optional<Book>  │
     │                    │                    │                    │◄───────────────────┤
     │                    │                    │                    │ 8. Increment View  │
     │                    │                    │                    │ book.viewCount++   │
     │                    │                    │                    │ save(book)         │
     │                    │                    │                    ├───────────────────►│
     │                    │                    │                    │                    │ 9. SQL UPDATE
     │                    │                    │                    │                    │ view_count++
     │                    │                    │                    │                    ├──────────┐
     │                    │                    │                    │                    │          │
     │                    │                    │                    │                    │◄─────────┘
     │                    │                    │ 10. Map Entity→DTO │                    │
     │                    │                    │ Return BookResponse│                    │
     │                    │                    │◄───────────────────┤                    │
     │                    │ 11. HTTP 200 OK    │                    │                    │
     │                    │ Body: BookResponse │                    │                    │
     │                    │◄───────────────────┤                    │                    │
     │ 12. Update State   │                    │                    │                    │
     │ setBook(data)      │                    │                    │                    │
     │ Render Details     │                    │                    │                    │
     │◄───────────────────┤                    │                    │                    │
     │                    │                    │                    │                    │
```

---

## Component Interaction Patterns

### 1. **Parent → Child Communication** (Props)

```jsx
// Parent Component
function BooksListPage() {
  const [books, setBooks] = useState([]);

  return (
    <Layout>
      <PageHeader title="Books List" />
      <BooksTable
        books={books}              // ← Pass data down
        onEdit={handleEdit}        // ← Pass callback down
        onDelete={handleDelete}    // ← Pass callback down
      />
    </Layout>
  );
}

// Child Component
function BooksTable({ books, onEdit, onDelete }) {
  return (
    <table>
      {books.map(book => (
        <BookRow
          key={book.id}
          book={book}
          onEdit={onEdit}      // ← Forward callback
          onDelete={onDelete}  // ← Forward callback
        />
      ))}
    </table>
  );
}
```

### 2. **Child → Parent Communication** (Callbacks)

```jsx
// Child Component
function BookRow({ book, onEdit, onDelete }) {
  return (
    <tr>
      <td>{book.title}</td>
      <td>{book.author}</td>
      <td>
        <button onClick={() => onEdit(book.id)}>    {/* ← Call parent callback */}
          Edit
        </button>
        <button onClick={() => onDelete(book.id)}>  {/* ← Call parent callback */}
          Delete
        </button>
      </td>
    </tr>
  );
}
```

### 3. **Global State Sharing** (Context API)

```jsx
// ToastContext.jsx
export const ToastContext = createContext();

export function ToastProvider({ children }) {
  const [toasts, setToasts] = useState([]);

  const showToast = (message, type) => {
    setToasts([...toasts, { id: Date.now(), message, type }]);
  };

  return (
    <ToastContext.Provider value={{ toasts, showToast }}>
      {children}
    </ToastContext.Provider>
  );
}

// Usage in any component
function AddBookPage() {
  const { showToast } = useContext(ToastContext);

  const handleSubmit = async () => {
    try {
      await createBook(formData);
      showToast('Book created successfully!', 'success');  // ← Use global state
    } catch (error) {
      showToast('Failed to create book', 'error');
    }
  };
}
```

### 4. **Service Layer Communication** (Axios)

```jsx
// services/booksService.js
export const booksService = {
  getAllBooks: (page = 0, size = 4) =>
    api.get(`/books?page=${page}&size=${size}`),

  getBookById: (id) =>
    api.get(`/books/${id}`),

  createBook: (bookData) =>
    api.post('/books', bookData),

  updateBook: (id, bookData) =>
    api.put(`/books/${id}`, bookData),

  deleteBook: (id) =>
    api.delete(`/books/${id}`)
};

// Component usage
function BooksListPage() {
  useEffect(() => {
    const fetchBooks = async () => {
      const response = await booksService.getAllBooks(page, pageSize);
      setBooks(response.data.content);
    };
    fetchBooks();
  }, [page]);
}
```

---

## State Management Flow

### Application State Categories

```
┌─────────────────────────────────────────────────────────────┐
│                    APPLICATION STATE                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. SERVER STATE (Cached from API)                         │
│     ├── Books List (paginated)                             │
│     ├── Book Details (single)                              │
│     └── Search Results                                     │
│                                                             │
│  2. UI STATE (Component-level)                             │
│     ├── Loading states (isLoading)                         │
│     ├── Error states (error)                               │
│     ├── Modal visibility (isModalOpen)                     │
│     ├── Form input values (formData)                       │
│     └── Pagination (currentPage, pageSize)                 │
│                                                             │
│  3. FORM STATE (React Hook Form)                           │
│     ├── Input values                                       │
│     ├── Validation errors                                  │
│     ├── Dirty/touched fields                               │
│     └── Submit state                                       │
│                                                             │
│  4. GLOBAL STATE (Context API)                             │
│     ├── Toast notifications                                │
│     └── User preferences (future)                          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### State Flow Example: Add New Book

```
1. User fills form
   ↓
2. React Hook Form tracks state
   ├── Input values
   ├── Validation errors
   └── Form dirty state
   ↓
3. User clicks "Save"
   ↓
4. Form validation (Zod schema)
   ├── Valid? → Continue
   └── Invalid? → Show errors
   ↓
5. Component sets isLoading = true
   ↓
6. Call booksService.createBook(data)
   ↓
7. Axios POST to /api/v1/books
   ↓
8. Backend processes request
   ↓
9. Response received
   ├── Success (201)
   │   ├── Update local state (add book to list)
   │   ├── Show success toast (Context)
   │   └── Navigate to list page
   │
   └── Error (4xx/5xx)
       ├── Show error toast (Context)
       └── Keep form open with values
```

---

## Summary

This component architecture provides:

✅ **Frontend**: Atomic design with clear component hierarchy
✅ **Backend**: Clean layered architecture (Controller → Service → Repository → Entity → Database)
✅ **Separation of Concerns**: Each layer has distinct responsibilities
✅ **Reusability**: Common components (atoms/molecules) reused across pages
✅ **Testability**: Isolated components and services easy to unit test
✅ **Scalability**: Clear structure supports adding new features
✅ **Maintainability**: Logical organization aids understanding and modification

**Total Component Count:**
- Frontend: ~50 components (12 atoms, 18 molecules, 8 organisms, 5 pages, 7 common)
- Backend: 8 main classes (1 controller, 1 service interface, 1 service impl, 1 repository, 1 entity, 3 DTOs)

**Data Flow:** React Component → Axios → Controller → Service → Repository → Database (and back)

---

**Next Steps:**
- Implement atomic components first (Button, Input, etc.)
- Build up to molecules, organisms, then pages
- Start with backend entity and repository
- Add service layer with business logic
- Implement REST controller endpoints
- Connect frontend to backend APIs
