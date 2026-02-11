# System Architecture Document

## Document Information
- **Project:** Books Online - Book Management System
- **Version:** 1.0
- **Date:** 2024-02-11
- **Architect:** AI Generated
- **Status:** Design Phase
- **Document ID:** ARCH-BOOKS-2024-001

---

## 1. Architecture Overview

### 1.1 System Vision

Books Online is architected as a modern, full-stack web application designed for efficient book catalog management. The system employs a **three-tier architecture** separating presentation (React SPA), business logic (Spring Boot API), and data persistence (H2 Database). This separation ensures maintainability, testability, and future scalability while keeping the initial implementation simple and focused on core functionality.

The architecture is designed to handle collections up to 10,000 books with sub-2-second page loads and sub-500ms API responses, supporting up to 50 concurrent users. The stateless backend design allows for future horizontal scaling when migrating from H2 to a production-grade database.

### 1.2 Architecture Style

**Architecture Pattern:** Three-tier architecture (Presentation, Business Logic, Data)

**Characteristics:**
- **Frontend:** Single Page Application (SPA) with client-side routing
- **Backend:** RESTful API following REST constraints
- **Database:** Relational database with JPA/Hibernate ORM
- **Communication:** HTTP/HTTPS with JSON payloads
- **State Management:** Stateless backend (no server-side sessions)
- **Deployment:** Containerized with Docker for consistency

### 1.3 High-Level Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT TIER                             │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │              React SPA (Port 5173)                       │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  UI Components                                     │  │  │
│  │  │  • Pages (Books List, Add/Edit, Search, Details)  │  │  │
│  │  │  • Common (Button, Input, Table, Modal, Toast)    │  │  │
│  │  │  • Layout (Header, Footer)                        │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  State Management & Routing                        │  │  │
│  │  │  • React Router v6 (client-side routing)           │  │  │
│  │  │  • Context API (global state)                      │  │  │
│  │  │  • React Hooks (local state)                       │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  API Client Layer                                  │  │  │
│  │  │  • Axios (HTTP client)                             │  │  │
│  │  │  • Request/Response Interceptors                   │  │  │
│  │  │  • Error Handling                                  │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  UI Frameworks                                     │  │  │
│  │  │  • Tailwind CSS (styling)                          │  │  │
│  │  │  • React Hook Form + Zod (validation)              │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────────────┘
                           │ HTTPS/JSON
                           │ REST API calls
                           │ GET, POST, PUT, DELETE
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                     APPLICATION TIER                            │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │        Spring Boot Application (Port 8080)               │  │
│  │                                                           │  │
│  │  ┌───────────────────────────────────────────────────┐  │  │
│  │  │  Controller Layer (@RestController)              │  │  │
│  │  │  • BookController                                 │  │  │
│  │  │  • Handle HTTP requests/responses                 │  │  │
│  │  │  • Request validation (@Valid)                    │  │  │
│  │  │  • Response formatting (DTOs)                     │  │  │
│  │  │  • HTTP status codes                              │  │  │
│  │  └───────────────┬───────────────────────────────────┘  │  │
│  │                  │                                       │  │
│  │  ┌───────────────▼───────────────────────────────────┐  │  │
│  │  │  Service Layer (@Service)                         │  │  │
│  │  │  • BookService / BookServiceImpl                  │  │  │
│  │  │  • Business logic                                 │  │  │
│  │  │  • Transaction management (@Transactional)        │  │  │
│  │  │  • Data transformation (Entity ↔ DTO)             │  │  │
│  │  │  • ISBN validation and uniqueness checks          │  │  │
│  │  └───────────────┬───────────────────────────────────┘  │  │
│  │                  │                                       │  │
│  │  ┌───────────────▼───────────────────────────────────┐  │  │
│  │  │  Repository Layer (JpaRepository)                 │  │  │
│  │  │  • BookRepository extends JpaRepository<Book>     │  │  │
│  │  │  • Data access abstraction                        │  │  │
│  │  │  • Query methods (findBy*, custom @Query)         │  │  │
│  │  │  • Spring Data JPA magic                          │  │  │
│  │  └───────────────┬───────────────────────────────────┘  │  │
│  └──────────────────┼───────────────────────────────────────┘  │
│                     │                                           │
│  ┌──────────────────▼───────────────────────────────────────┐  │
│  │  Cross-Cutting Concerns                                  │  │
│  │  • Exception Handling (@ControllerAdvice)                │  │
│  │  • Logging (SLF4J + Logback)                             │  │
│  │  • Validation (Bean Validation / JSR-380)                │  │
│  │  • CORS Configuration                                    │  │
│  │  • OpenAPI Documentation (Springdoc)                     │  │
│  └──────────────────────────────────────────────────────────┘  │
└──────────────────────────┬──────────────────────────────────────┘
                           │ JDBC / JPA
                           │ HikariCP Connection Pool
                           │
┌──────────────────────────▼──────────────────────────────────────┐
│                        DATA TIER                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │          H2 Database (In-Memory/File-Based)              │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Tables                                            │  │  │
│  │  │  • books (18 columns, 8 indexes)                   │  │  │
│  │  │  • Primary Key: id (BIGINT AUTO_INCREMENT)         │  │  │
│  │  │  • Unique Constraint: isbn                         │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Indexes                                           │  │  │
│  │  │  • idx_title, idx_author, idx_genre               │  │  │
│  │  │  • idx_year, idx_available, idx_created           │  │  │
│  │  │  • idx_genre_year (composite)                     │  │  │
│  │  │  • idx_author_title (composite)                   │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  │  ┌────────────────────────────────────────────────────┐  │  │
│  │  │  Constraints                                       │  │  │
│  │  │  • NOT NULL constraints on required fields        │  │  │
│  │  │  • CHECK constraints (year, price, stock)         │  │  │
│  │  │  • DEFAULT values (timestamps, counters)          │  │  │
│  │  └────────────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘

                    ┌────────────────────┐
                    │  External Systems  │
                    │  (Future V2)       │
                    └────────────────────┘
```

---

## 2. Frontend Architecture

### 2.1 Technology Stack

- **Framework:** React 18.2+ (library for building user interfaces)
- **Build Tool:** Vite 5.x (fast build tool with HMR)
- **Language:** JavaScript (ES6+) with optional TypeScript for V2
- **Router:** React Router v6 (declarative routing)
- **State Management:** React Context API + useState/useReducer
- **Forms:** React Hook Form 7.x + Zod validation
- **HTTP Client:** Axios 1.x
- **Styling:** Tailwind CSS 3.x (utility-first CSS)
- **Testing:** Vitest + React Testing Library
- **Linting:** ESLint + Prettier

### 2.2 Folder Structure

```
frontend/
├── public/
│   ├── favicon.ico
│   ├── logo.svg
│   └── placeholder-book.png          # Default book cover
├── src/
│   ├── components/
│   │   ├── common/                   # Reusable UI components
│   │   │   ├── Button.jsx
│   │   │   ├── Input.jsx
│   │   │   ├── Dropdown.jsx
│   │   │   ├── Textarea.jsx
│   │   │   ├── Card.jsx
│   │   │   ├── Table.jsx
│   │   │   ├── Pagination.jsx
│   │   │   ├── Modal.jsx
│   │   │   ├── Toast.jsx
│   │   │   ├── Loading.jsx          # Spinner component
│   │   │   ├── Skeleton.jsx         # Skeleton loader
│   │   │   ├── Badge.jsx
│   │   │   ├── FileUpload.jsx
│   │   │   └── EmptyState.jsx
│   │   ├── layout/                   # Layout components
│   │   │   ├── Header.jsx
│   │   │   ├── Footer.jsx
│   │   │   └── Layout.jsx           # Main layout wrapper
│   │   └── features/                 # Feature-specific components
│   │       └── books/
│   │           ├── BooksTable.jsx
│   │           ├── BookCard.jsx     # Mobile view
│   │           ├── BookForm.jsx     # Shared form component
│   │           ├── BookDetails.jsx
│   │           ├── DeleteModal.jsx
│   │           ├── FilterBar.jsx
│   │           ├── SearchBar.jsx
│   │           └── AdvancedSearch.jsx
│   ├── pages/                        # Page components (route targets)
│   │   ├── BooksListPage.jsx
│   │   ├── AddBookPage.jsx
│   │   ├── EditBookPage.jsx
│   │   ├── SearchBooksPage.jsx
│   │   ├── BookDetailsPage.jsx
│   │   └── NotFoundPage.jsx
│   ├── services/                     # API service layer
│   │   ├── api.js                   # Axios instance configuration
│   │   └── booksService.js          # Books API calls
│   ├── context/                      # React Context providers
│   │   ├── ToastContext.jsx         # Global toast notifications
│   │   └── BooksContext.jsx         # Books state (optional)
│   ├── hooks/                        # Custom React hooks
│   │   ├── useBooks.js              # Books data fetching
│   │   ├── useDebounce.js           # Debounce hook for search
│   │   ├── useToast.js              # Toast notifications
│   │   └── usePagination.js         # Pagination logic
│   ├── utils/                        # Utility functions
│   │   ├── validators.js            # ISBN, year, price validators
│   │   ├── formatters.js            # Date, currency formatters
│   │   ├── constants.js             # Genre, language constants
│   │   └── helpers.js               # Misc helper functions
│   ├── routes/                       # Route configuration
│   │   └── AppRoutes.jsx            # All application routes
│   ├── styles/                       # Global styles
│   │   └── index.css                # Tailwind imports + custom
│   ├── App.jsx                       # Root component
│   └── main.jsx                      # Entry point
├── .env.example                      # Environment variables template
├── .env.local                        # Local environment variables
├── .eslintrc.json                    # ESLint configuration
├── .prettierrc                       # Prettier configuration
├── vite.config.js                    # Vite configuration
├── tailwind.config.js                # Tailwind configuration
├── package.json
└── README.md
```

### 2.3 Component Architecture

**Atomic Design Principles:**

1. **Atoms:** Basic building blocks (Button, Input, Label, Badge)
2. **Molecules:** Simple component groups (InputField = Label + Input + ErrorMessage)
3. **Organisms:** Complex UI sections (Header, BooksTable, BookForm)
4. **Templates:** Page layouts (Layout wrapper)
5. **Pages:** Actual route pages (BooksListPage, AddBookPage)

**Component Responsibilities:**

- **Presentational Components:** Display UI, receive props, emit events
- **Container Components:** Fetch data, manage state, pass to presentational
- **Common Components:** Highly reusable, no business logic
- **Feature Components:** Domain-specific, contain business logic

### 2.4 State Management Strategy

**Local State (useState):**
- Component-specific UI state (form inputs, modal open/closed, loading flags)
- Temporary data not shared across components

**Global State (Context API):**
- Toast notifications (ToastContext)
- User preferences (theme, language - future)
- Authentication state (future V2)

**Server State (useEffect + useState or React Query future):**
- Books data fetched from API
- Cached in component state with manual refetch logic
- Future: Consider React Query for automatic caching, refetching, pagination

**Form State (React Hook Form):**
- Form inputs managed by react-hook-form
- Validation with Zod schemas
- Reduced re-renders, better performance

### 2.5 Routing Strategy

**Route Structure:**

```javascript
// AppRoutes.jsx
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'

<BrowserRouter>
  <Routes>
    {/* Main route - Books List */}
    <Route path="/" element={<BooksListPage />} />

    {/* Books routes */}
    <Route path="/books">
      <Route index element={<Navigate to="/" replace />} />
      <Route path="add" element={<AddBookPage />} />
      <Route path=":id" element={<BookDetailsPage />} />
      <Route path=":id/edit" element={<EditBookPage />} />
    </Route>

    {/* Search route */}
    <Route path="/search" element={<SearchBooksPage />} />

    {/* 404 */}
    <Route path="*" element={<NotFoundPage />} />
  </Routes>
</BrowserRouter>
```

**URL Patterns:**
- `/` - Books List View (main page)
- `/books/add` - Add New Book
- `/books/:id` - Book Details
- `/books/:id/edit` - Edit Book
- `/search` - Advanced Search
- Query parameters for filters: `/?genre=Fiction&search=gatsby`

### 2.6 API Communication Pattern

**Axios Instance Configuration:**

```javascript
// services/api.js
import axios from 'axios'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Request interceptor (future: add auth token)
api.interceptors.request.use(
  (config) => {
    // Add any headers or modify request
    return config
  },
  (error) => Promise.reject(error)
)

// Response interceptor (handle errors globally)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 404) {
      // Handle 404 errors
    }
    if (error.response?.status === 500) {
      // Handle server errors
    }
    return Promise.reject(error)
  }
)

export default api
```

**Books Service:**

```javascript
// services/booksService.js
import api from './api'

export const booksService = {
  // List books with pagination and filters
  getBooks: (params) => api.get('/books', { params }),

  // Get single book by ID
  getBook: (id) => api.get(`/books/${id}`),

  // Create new book
  createBook: (data) => api.post('/books', data),

  // Update book
  updateBook: (id, data) => api.put(`/books/${id}`, data),

  // Delete book
  deleteBook: (id) => api.delete(`/books/${id}`),

  // Search books
  searchBooks: (params) => api.get('/books/search', { params }),

  // Duplicate book
  duplicateBook: (id) => api.post(`/books/${id}/duplicate`),

  // Export book data
  exportBook: (id, format) => api.get(`/books/${id}/export`, {
    params: { format },
    responseType: 'blob'
  })
}
```

---

## 3. Backend Architecture

### 3.1 Technology Stack

- **Language:** Java 17 LTS
- **Framework:** Spring Boot 3.2.x
- **Build Tool:** Maven 3.9+
- **ORM:** Spring Data JPA + Hibernate 6.x
- **Database:** H2 2.2.x (in-memory for development)
- **API Docs:** Springdoc OpenAPI 3.0 (Swagger UI)
- **Testing:** JUnit 5, Mockito, MockMvc
- **Logging:** SLF4J + Logback

### 3.2 Folder Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/booksonline/
│   │   │   ├── Application.java              # Main Spring Boot application
│   │   │   ├── config/                       # Configuration classes
│   │   │   │   ├── WebConfig.java            # CORS, MVC config
│   │   │   │   ├── OpenApiConfig.java        # Springdoc configuration
│   │   │   │   └── DatabaseConfig.java       # H2 configuration (optional)
│   │   │   ├── controller/                   # REST Controllers
│   │   │   │   └── BookController.java
│   │   │   ├── service/                      # Business logic
│   │   │   │   ├── BookService.java          # Interface
│   │   │   │   └── impl/
│   │   │   │       └── BookServiceImpl.java  # Implementation
│   │   │   ├── repository/                   # Data access
│   │   │   │   └── BookRepository.java
│   │   │   ├── model/
│   │   │   │   ├── entity/                   # JPA Entities
│   │   │   │   │   └── Book.java
│   │   │   │   └── dto/                      # Data Transfer Objects
│   │   │   │       ├── request/
│   │   │   │       │   ├── BookCreateRequest.java
│   │   │   │       │   └── BookUpdateRequest.java
│   │   │   │       └── response/
│   │   │   │           ├── BookResponse.java
│   │   │   │           └── PagedResponse.java
│   │   │   ├── exception/                    # Exception handling
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── BookNotFoundException.java
│   │   │   │   ├── DuplicateIsbnException.java
│   │   │   │   └── InvalidDataException.java
│   │   │   ├── mapper/                       # Entity <-> DTO mappers
│   │   │   │   └── BookMapper.java
│   │   │   └── util/                         # Utility classes
│   │   │       ├── IsbnValidator.java
│   │   │       └── Constants.java
│   │   └── resources/
│   │       ├── application.properties         # Default properties
│   │       ├── application-dev.properties     # Dev profile
│   │       ├── application-prod.properties    # Prod profile
│   │       └── data.sql                       # Seed data (optional)
│   └── test/
│       └── java/com/booksonline/
│           ├── controller/
│           │   └── BookControllerTest.java
│           ├── service/
│           │   └── BookServiceTest.java
│           └── repository/
│               └── BookRepositoryTest.java
├── pom.xml
└── README.md
```

### 3.3 Layered Architecture

**1. Controller Layer (@RestController)**
- **Responsibility:** Handle HTTP requests/responses
- **Annotations:** @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
- **Functions:**
  - Accept HTTP requests
  - Validate request data (@Valid)
  - Call service layer methods
  - Return DTOs (never entities)
  - Map HTTP status codes

**2. Service Layer (@Service)**
- **Responsibility:** Implement business logic
- **Annotations:** @Service, @Transactional
- **Functions:**
  - Business rule enforcement
  - Transaction management
  - Data transformation (Entity ↔ DTO)
  - Call repository methods
  - Validation beyond simple format checks

**3. Repository Layer (JpaRepository)**
- **Responsibility:** Data access abstraction
- **Extends:** JpaRepository<Book, Long>
- **Functions:**
  - CRUD operations (provided by Spring Data JPA)
  - Custom query methods
  - No business logic

**4. Model Layer**
- **Entities:** Database table representations (@Entity)
- **DTOs:** API request/response objects
- **Purpose:** Decouple API contract from database schema

**Layer Communication Flow:**
```
HTTP Request → Controller → Service → Repository → Database
                     ↓
                   DTO ← Mapper ← Entity ← JPA/Hibernate
                     ↓
HTTP Response ← Controller
```

### 3.4 API Design Conventions

**Base URL:** `http://localhost:8080/api/v1`

**Endpoints Pattern:**
- `/api/v1/books` - Book collection operations
- `/api/v1/books/{id}` - Single book operations
- `/api/v1/books/search` - Search operations
- `/api/v1/books/{id}/duplicate` - Duplicate operation
- `/api/v1/books/{id}/export` - Export operation

**HTTP Methods:**
- `GET` - Retrieve data (list or single)
- `POST` - Create new resource
- `PUT` - Update entire resource
- `PATCH` - Partial update (future)
- `DELETE` - Delete resource

**Standard Response Format:**
```json
{
  "id": 1,
  "title": "The Great Gatsby",
  "isbn": "978-0-7432-7356-5",
  "author": "F. Scott Fitzgerald",
  ...
}
```

**Paginated Response Format:**
```json
{
  "content": [...],
  "page": 0,
  "size": 4,
  "totalElements": 24,
  "totalPages": 6,
  "first": true,
  "last": false
}
```

**Error Response Format:**
```json
{
  "timestamp": "2024-02-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "ISBN is required and must be a valid format",
  "path": "/api/v1/books",
  "details": [
    {
      "field": "isbn",
      "message": "must match ISBN-10 or ISBN-13 format"
    }
  ]
}
```

---

## 4. Database Architecture

### 4.1 Database Strategy

**Development:** H2 in-memory or file-based
- **URL:** `jdbc:h2:mem:booksdb` (in-memory) or `jdbc:h2:file:./data/booksdb` (file-based)
- **Console:** Enabled at `/h2-console` for debugging
- **Advantages:** Zero configuration, fast startup, embedded
- **Disadvantages:** Limited features, not suitable for production scale

**Production (Future):** PostgreSQL 14+ or MySQL 8+
- Migration path documented
- Connection pooling: HikariCP (default in Spring Boot)
- Schema versioning: Flyway or Liquibase

### 4.2 Schema Design Principles

1. **Normalization:** 3NF (Third Normal Form)
2. **Naming Convention:** snake_case for columns, camelCase in Java
3. **Primary Keys:** BIGINT AUTO_INCREMENT (id column)
4. **Audit Columns:** created_at, updated_at on all tables
5. **Constraints:** NOT NULL, UNIQUE, CHECK, DEFAULT
6. **Indexes:** On frequently queried columns
7. **Relationships:** Foreign keys with appropriate ON DELETE behavior (future V2)

### 4.3 Book Entity Schema

```sql
CREATE TABLE books (
    -- Primary Key
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    -- Required Fields (User Input)
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) NOT NULL UNIQUE,
    author VARCHAR(100) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    publication_year INT NOT NULL,
    language VARCHAR(30) NOT NULL,

    -- Optional Fields (User Input)
    publisher VARCHAR(100),
    pages INT CHECK (pages > 0),
    price DECIMAL(10, 2) CHECK (price >= 0),
    stock_quantity INT DEFAULT 0 CHECK (stock_quantity >= 0),
    description TEXT,
    cover_image_url VARCHAR(500),

    -- System-Managed Fields
    rating DECIMAL(2, 1) CHECK (rating >= 0 AND rating <= 5.0),
    review_count INT DEFAULT 0,
    view_count INT DEFAULT 0,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT chk_year CHECK (publication_year >= 1000 AND publication_year <= YEAR(CURRENT_DATE))
);

-- Indexes for Performance
CREATE INDEX idx_title ON books(title);
CREATE INDEX idx_author ON books(author);
CREATE INDEX idx_genre ON books(genre);
CREATE INDEX idx_year ON books(publication_year);
CREATE INDEX idx_available ON books(is_available);
CREATE INDEX idx_created ON books(created_at);
CREATE INDEX idx_isbn ON books(isbn);  -- Unique constraint already creates index

-- Composite Indexes for Common Queries
CREATE INDEX idx_genre_year ON books(genre, publication_year);
CREATE INDEX idx_author_title ON books(author, title);
```

**Index Strategy Rationale:**
- `idx_title`: Quick search, sorting by title
- `idx_author`: Filter by author, sorting
- `idx_genre`: Filter by genre dropdown
- `idx_year`: Filter by publication year, sorting
- `idx_available`: Filter in-stock vs out-of-stock books
- `idx_genre_year`: Advanced search combining genre and year
- `idx_author_title`: Search by author then sort by title

### 4.4 JPA Entity Definition

```java
@Entity
@Table(name = "books")
@EntityListeners(AuditingEntityListener.class)
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(nullable = false, length = 100)
    private String author;

    @Column(length = 100)
    private String publisher;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @Column
    private Integer pages;

    @Column(nullable = false, length = 30)
    private String language;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url", length = 500)
    private String coverImageUrl;

    @Column(precision = 2, scale = 1)
    private BigDecimal rating;

    @Column(name = "review_count")
    private Integer reviewCount = 0;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Lifecycle callback to compute isAvailable
    @PrePersist
    @PreUpdate
    public void updateAvailability() {
        this.isAvailable = (this.stockQuantity != null && this.stockQuantity > 0);
    }

    // Getters and setters...
}
```

---

## 5. Integration Architecture

### 5.1 Frontend-Backend Integration

**Communication Protocol:** REST over HTTP/HTTPS
**Data Format:** JSON
**Authentication:** None in V1 (future: JWT tokens in Authorization header)

**Request Flow:**
1. User interacts with React UI
2. React component calls service method (booksService.getBooks())
3. Axios sends HTTP request to Spring Boot API
4. Spring Boot controller receives request
5. Controller calls service layer
6. Service calls repository
7. Repository queries H2 database via JPA
8. Data flows back up: Repository → Service → Controller
9. Controller returns DTO as JSON
10. Axios receives response
11. React component updates state and re-renders

### 5.2 Error Handling Strategy

**Backend Error Handling:**

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            404,
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateIsbn(DuplicateIsbnException ex) {
        ErrorResponse error = new ErrorResponse(
            LocalDateTime.now(),
            409,
            "Conflict",
            ex.getMessage(),
            request.getRequestURI()
        );
        return ResponseEntity.status(409).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        // Map to ErrorResponse with details
        return ResponseEntity.status(400).body(error);
    }
}
```

**Frontend Error Handling:**

```javascript
// Axios interceptor
api.interceptors.response.use(
  (response) => response,
  (error) => {
    const errorMessage = error.response?.data?.message || 'An error occurred'

    if (error.response?.status === 404) {
      toast.error('Book not found')
      navigate('/') // Redirect to list
    } else if (error.response?.status === 409) {
      toast.error('ISBN already exists')
    } else if (error.response?.status === 500) {
      toast.error('Server error. Please try again.')
    } else {
      toast.error(errorMessage)
    }

    return Promise.reject(error)
  }
)
```

---

## 6. Deployment Architecture

### 6.1 Development Environment

```
┌─────────────────┐     ┌─────────────────┐
│   Frontend      │────▶│    Backend      │
│  Vite Dev       │     │  Spring Boot    │
│  Port: 5173     │     │  Port: 8080     │
│  Hot Reload     │     │  DevTools       │
└─────────────────┘     └────────┬────────┘
                                 │
                        ┌────────▼────────┐
                        │  H2 Database    │
                        │  In-Memory      │
                        │  Console: 8080  │
                        │  /h2-console    │
                        └─────────────────┘
```

**Development Workflow:**
1. Developer runs `npm run dev` (frontend)
2. Developer runs `mvn spring-boot:run` (backend)
3. Frontend proxies API requests to `localhost:8080`
4. Hot module replacement for instant frontend updates
5. Spring Boot DevTools for automatic backend restarts

### 6.2 Production Environment (Docker)

**Docker Architecture:**

```
┌──────────────────────────────────────────────────────────┐
│                    Docker Compose                         │
│                                                           │
│  ┌────────────────────┐       ┌────────────────────┐    │
│  │  Frontend          │       │   Backend          │    │
│  │  Container         │───────│   Container        │    │
│  │  (nginx:alpine)    │       │   (openjdk:17)     │    │
│  │  Port: 80          │       │   Port: 8080       │    │
│  │  Static files      │       │   Spring Boot JAR  │    │
│  └────────────────────┘       └───────┬────────────┘    │
│                                        │                  │
│                               ┌────────▼────────┐        │
│                               │  H2 Database    │        │
│                               │  File-based     │        │
│                               │  Volume mount   │        │
│                               └─────────────────┘        │
└──────────────────────────────────────────────────────────┘
```

**docker-compose.yml:**

```yaml
version: '3.8'

services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "80:80"
    depends_on:
      - backend
    environment:
      - VITE_API_URL=http://backend:8080/api/v1

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - SPRING_DATASOURCE_URL=jdbc:h2:file:/data/booksdb
    volumes:
      - db-data:/data

volumes:
  db-data:
```

### 6.3 CI/CD Pipeline (GitHub Actions)

**Pipeline Stages:**

1. **Lint and Test (on PR)**
   - Run ESLint on frontend
   - Run unit tests (Vitest, JUnit)
   - Check code coverage
   - Run integration tests

2. **Build (on merge to main)**
   - Build frontend (npm run build)
   - Build backend (mvn clean package)
   - Build Docker images
   - Tag with commit SHA

3. **Deploy to Staging**
   - Deploy to staging environment
   - Run smoke tests
   - Manual approval gate

4. **Deploy to Production**
   - Deploy to production
   - Health check verification
   - Rollback on failure

---

## 7. Non-Functional Architecture

### 7.1 Performance

**Frontend Performance:**
- Code splitting: Lazy load routes with React.lazy()
- Bundle optimization: Tree shaking, minification
- Image optimization: Lazy loading, WebP format, responsive images
- Caching: Service worker (future), browser caching headers

**Backend Performance:**
- Database connection pooling: HikariCP (min 5, max 20)
- Query optimization: Proper indexes, avoid N+1 queries
- Pagination: Limit result sets (default 4, max 100 per page)
- Response compression: Gzip enabled

**Performance Targets:**
- Page load: <2 seconds (Time to Interactive)
- API response: <500ms (P95)
- Search: <1 second for 10,000 books

### 7.2 Scalability

**Current Architecture (V1):**
- Single application instance
- H2 database (not suitable for horizontal scaling)

**Future Scalability (V2+):**
- Stateless backend → multiple instances behind load balancer
- PostgreSQL with read replicas
- CDN for static assets and images
- Redis for caching frequently accessed data
- Horizontal pod autoscaling (Kubernetes)

### 7.3 Security

**V1 Security Measures:**
- HTTPS in production (SSL/TLS certificates)
- CORS configuration (allow frontend origin only)
- SQL injection prevention (JPA parameterized queries)
- XSS prevention (React's built-in escaping, CSP headers)
- Input validation (client-side Zod, server-side Bean Validation)
- File upload validation (type, size limits)
- Secure headers (X-Frame-Options, X-Content-Type-Options, etc.)

**V2 Security Enhancements:**
- JWT-based authentication
- Role-based access control (RBAC)
- Rate limiting to prevent abuse
- Password hashing (BCrypt)
- CSRF protection
- Audit logging

### 7.4 Monitoring & Logging

**Logging:**
- **Backend:** SLF4J + Logback
  - Log levels: ERROR, WARN, INFO, DEBUG
  - Structured JSON logs for production
  - Log rotation and retention policies
- **Frontend:** Console logging (development only)

**Monitoring (Future):**
- Health check endpoints: `/actuator/health`
- Metrics: `/actuator/metrics` (Spring Boot Actuator)
- APM: Application Performance Monitoring (New Relic, DataDog)
- Error tracking: Sentry or similar
- Uptime monitoring: Pingdom, UptimeRobot

---

## 8. Technology Decisions & Rationale

| Technology | Alternative Considered | Decision Rationale |
|------------|------------------------|-------------------|
| **React** | Vue, Angular, Svelte | Most popular, large ecosystem, team familiarity, excellent documentation |
| **Vite** | Create React App, Webpack | 10-100x faster builds, modern dev experience, native ESM, better DX |
| **Tailwind CSS** | Bootstrap, Material-UI, CSS Modules | Utility-first enables rapid development, highly customizable, small bundle with purging |
| **Spring Boot** | Node.js + Express, Django, FastAPI | Enterprise-grade, robust ecosystem, excellent ORM, strong typing, team expertise |
| **Spring Data JPA** | MyBatis, JDBC Template | Reduces boilerplate significantly, type-safe queries, pagination support, relationship management |
| **H2** | PostgreSQL, MySQL, SQLite | Zero configuration for development, embedded, fast startup, easy to reset |
| **JWT (V2)** | Session-based auth | Stateless (scales horizontally), standard, mobile-friendly, decoupled auth |
| **Maven** | Gradle | More mature, declarative, wider adoption in enterprise, XML familiarity |
| **Docker** | VM, bare metal | Consistent environments, easy deployment, isolation, portability |
| **React Hook Form** | Formik, Redux Form | Better performance (fewer re-renders), smaller bundle, excellent TypeScript support |
| **Axios** | Fetch API, ky | Interceptors, automatic JSON transformation, better error handling, timeout support |

---

## 9. Design Patterns Used

### Frontend Patterns
1. **Component Composition:** Build complex UIs from simple components
2. **Render Props / Custom Hooks:** Share stateful logic across components
3. **Container-Presentational:** Separate data fetching from UI rendering
4. **Provider Pattern:** Context API for global state
5. **Higher-Order Components:** Enhance components with additional functionality (future)

### Backend Patterns
1. **MVC (Model-View-Controller):** Separation of concerns
2. **Repository Pattern:** Data access abstraction
3. **Service Layer Pattern:** Business logic encapsulation
4. **DTO Pattern:** Decouple API from entities
5. **Factory Pattern:** Object creation (Spring beans)
6. **Singleton Pattern:** Spring bean scope (default)
7. **Dependency Injection:** Core Spring principle, loose coupling
8. **Strategy Pattern:** Different export formats (JSON, CSV, PDF)

---

## 10. Quality Attributes

| Attribute | Implementation Strategy | Measurement |
|-----------|------------------------|-------------|
| **Testability** | Unit tests (>80% backend, >70% frontend), integration tests, MockMvc, React Testing Library | Coverage reports, test execution time |
| **Maintainability** | Clean code, SOLID principles, comprehensive documentation, consistent naming | Code review metrics, technical debt |
| **Reliability** | Error handling, validation, logging, graceful degradation | Error rate <0.1%, uptime >99.5% |
| **Performance** | Efficient queries, caching, pagination, code splitting | Page load <2s, API <500ms (P95) |
| **Usability** | Responsive design, intuitive UI, error messages, loading states | User satisfaction >4.0/5.0, task completion rate |
| **Security** | Input validation, HTTPS, SQL injection prevention, XSS protection | Zero critical vulnerabilities, security audit pass |
| **Scalability** | Stateless design, connection pooling, indexes | Support 50 concurrent users, 10K books |
| **Portability** | Docker containers, environment variables, configurable database | Deploy to any platform with Docker |

---

## 11. Architecture Constraints

### Technical Constraints
1. **H2 Database:**
   - Not suitable for production high-load scenarios
   - Limited concurrent connections
   - No distributed capabilities
   - Must migrate to PostgreSQL/MySQL for production

2. **Single Server Deployment:**
   - No load balancing in V1
   - No horizontal scaling
   - Single point of failure

3. **Stateless Design:**
   - No server-side session storage
   - All state in client or database

4. **No Authentication:**
   - Open access (anyone can add/edit/delete)
   - Suitable only for trusted environments

### Business Constraints
1. **Budget:** Open-source technologies only, no paid licenses
2. **Timeline:** 8-week development for V1
3. **Team Size:** 1-2 developers assumed
4. **Scope:** Must stay within V1 PRD scope

### Regulatory Constraints
1. **Data Privacy:** No personal data stored (GDPR not applicable for V1)
2. **Accessibility:** Should meet WCAG 2.1 Level AA (best effort for V1)

---

## 12. Future Architecture Considerations

### V2 Enhancements (3-6 months)
1. **Authentication & Authorization:**
   - JWT-based authentication
   - User registration/login
   - Role-based access control (Admin, Librarian, Viewer)

2. **Database Migration:**
   - PostgreSQL 14+ for production
   - Flyway for schema versioning
   - Read replicas for scaling

3. **Advanced Features:**
   - User reviews and ratings (CRUD)
   - Bulk import from CSV
   - Analytics dashboard
   - Book borrowing system

### V3 Evolution (6-12 months)
1. **Microservices:**
   - Split monolith if needed (Books Service, Users Service, Reviews Service)
   - API Gateway for routing

2. **Caching Layer:**
   - Redis for session storage and data caching
   - Reduce database load

3. **Message Queue:**
   - RabbitMQ/Kafka for async processing
   - Email notifications, export generation

4. **External Integrations:**
   - Google Books API for ISBN lookup
   - Cloudinary/S3 for image storage
   - Payment processing (Stripe)

5. **Mobile App:**
   - React Native mobile app
   - Shared API with web

---

## 13. Architecture Risk Assessment

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| H2 data loss (corruption, crash) | High | Medium | File-based persistence, regular exports, migration path to PostgreSQL documented |
| Performance degradation at scale | High | Medium | Database indexes from start, load testing with 10K books, optimize queries proactively |
| Security vulnerabilities | High | Low | OWASP best practices, input validation, security review, penetration testing |
| Browser compatibility issues | Medium | Low | Test on all major browsers, use polyfills, avoid experimental features |
| Tight coupling between layers | Medium | Low | Follow layered architecture strictly, use DTOs, dependency injection |
| Scalability bottlenecks | High | Medium | Stateless design, document PostgreSQL migration, plan for horizontal scaling |
| Technical debt accumulation | Medium | High | Code reviews, refactoring sprints, maintain test coverage, documentation |

---

## Document Approval

| Role | Name | Date | Signature |
|------|------|------|-----------|
| Architect | AI Generated | 2024-02-11 | |
| Tech Lead | TBD | | |
| Backend Lead | TBD | | |
| Frontend Lead | TBD | | |
| DevOps Lead | TBD | | |

---

## Revision History

| Version | Date | Author | Changes |
|---------|------|--------|---------|
| 1.0 | 2024-02-11 | AI Generated | Initial architecture design based on PRD and user stories |

---

**END OF ARCHITECTURE DOCUMENT**
