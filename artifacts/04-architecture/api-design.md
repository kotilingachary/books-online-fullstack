# API Design Document

**Product:** Books Online - Book Management System
**Version:** 1.0
**Generated:** 2026-02-11
**Base URL:** `http://localhost:8080/api/v1`
**API Type:** RESTful JSON API

---

## Table of Contents

1. [API Design Principles](#api-design-principles)
2. [API Conventions](#api-conventions)
3. [Endpoint Specifications](#endpoint-specifications)
4. [Request/Response Formats](#requestresponse-formats)
5. [Error Handling](#error-handling)
6. [Pagination and Filtering](#pagination-and-filtering)
7. [HTTP Status Codes](#http-status-codes)
8. [API Versioning](#api-versioning)
9. [CORS Configuration](#cors-configuration)
10. [API Testing](#api-testing)

---

## API Design Principles

### 1. RESTful Architecture

Our API follows REST (Representational State Transfer) principles:

- **Resource-based URLs**: Endpoints represent resources (nouns), not actions (verbs)
  - ✅ Good: `GET /api/v1/books`
  - ❌ Bad: `GET /api/v1/getBooks`

- **HTTP Methods**: Use standard HTTP methods for CRUD operations
  - `GET` - Retrieve resources (safe, idempotent, cacheable)
  - `POST` - Create new resources (not idempotent)
  - `PUT` - Update existing resources (idempotent)
  - `DELETE` - Remove resources (idempotent)

- **Stateless**: Each request contains all information needed to process it
  - No server-side session state
  - Authentication via headers (future: JWT tokens)

- **Uniform Interface**: Consistent API design across all endpoints
  - Same error format
  - Same pagination format
  - Same naming conventions

### 2. API-First Design

- API specification defined before implementation
- Contract between frontend and backend
- Documentation generated from code (Springdoc OpenAPI)
- Testable via Postman/Swagger UI

### 3. JSON as Data Format

- All requests and responses use `application/json`
- Consistent field naming (camelCase)
- ISO 8601 for dates (`2026-02-11T10:30:00Z`)

### 4. Hypermedia (Future Enhancement)

- V1: Basic REST without HATEOAS
- V2: Add hypermedia links for resource navigation

---

## API Conventions

### 1. URL Structure

```
http://localhost:8080/api/v1/{resource}/{identifier}/{sub-resource}
│                      │       │        │            │
│                      │       │        │            └─ Optional sub-resource
│                      │       │        └─ Resource ID (optional)
│                      │       └─ Resource name (plural noun)
│                      └─ API version
└─ Base URL
```

**Examples:**
- `GET /api/v1/books` - Collection endpoint
- `GET /api/v1/books/123` - Single resource endpoint
- `POST /api/v1/books/123/duplicate` - Action endpoint

### 2. Naming Conventions

#### Resource Names
- Use **plural nouns** for resource collections: `/books`, `/users`, `/orders`
- Use **lowercase** with **hyphens** for multi-word resources: `/book-categories`
- Keep URLs short and intuitive

#### Field Names (JSON)
- Use **camelCase** for JSON field names: `publicationYear`, `coverImageUrl`
- Use descriptive names: `isbn` instead of `id`, `isAvailable` instead of `avail`
- Boolean fields: Prefix with `is` or `has` (`isAvailable`, `hasDiscount`)

### 3. HTTP Methods

| Method | Purpose | Safe | Idempotent | Request Body | Response Body |
|--------|---------|------|------------|--------------|---------------|
| GET | Retrieve resource(s) | ✅ Yes | ✅ Yes | ❌ No | ✅ Yes |
| POST | Create new resource | ❌ No | ❌ No | ✅ Yes | ✅ Yes (created resource) |
| PUT | Update existing resource | ❌ No | ✅ Yes | ✅ Yes | ✅ Yes (updated resource) |
| DELETE | Remove resource | ❌ No | ✅ Yes | ❌ No | ❌ No (or status) |
| PATCH | Partial update | ❌ No | ❌ No | ✅ Yes | ✅ Yes |

**Note:** V1 uses `PUT` for updates. V2 may add `PATCH` for partial updates.

### 4. Request Headers

#### Required Headers

```http
Content-Type: application/json
Accept: application/json
```

#### Optional Headers (Future: V2 Authentication)

```http
Authorization: Bearer <jwt-token>
```

#### CORS Headers (Automatic)

```http
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
```

---

## Endpoint Specifications

### Base URL

```
http://localhost:8080/api/v1
```

### Complete Endpoint Inventory

| # | Method | Endpoint | Description | Status |
|---|--------|----------|-------------|--------|
| 1 | GET | `/books` | Get all books (paginated) | V1 |
| 2 | GET | `/books/{id}` | Get single book by ID | V1 |
| 3 | POST | `/books` | Create new book | V1 |
| 4 | PUT | `/books/{id}` | Update existing book | V1 |
| 5 | DELETE | `/books/{id}` | Delete book | V1 |
| 6 | GET | `/books/search` | Advanced search with filters | V1 |
| 7 | POST | `/books/{id}/duplicate` | Duplicate existing book | V1 |
| 8 | GET | `/books/{id}/export` | Export book data (JSON/CSV/PDF) | V1 |

---

### 1. Get All Books (Paginated)

**Endpoint:** `GET /api/v1/books`

**Description:** Retrieve a paginated list of all books

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `page` | Integer | No | `0` | Page number (0-indexed) |
| `size` | Integer | No | `4` | Number of items per page (1-100) |
| `sort` | String | No | `id,asc` | Sort field and direction (e.g., `title,asc`, `publicationYear,desc`) |

**Example Request:**

```http
GET /api/v1/books?page=0&size=4&sort=title,asc HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Example Response: 200 OK**

```json
{
  "content": [
    {
      "id": 1,
      "title": "Clean Code",
      "isbn": "978-0132350884",
      "author": "Robert C. Martin",
      "publisher": "Prentice Hall",
      "genre": "Programming",
      "publicationYear": 2008,
      "pages": 464,
      "language": "English",
      "price": 44.99,
      "stockQuantity": 15,
      "description": "A handbook of agile software craftsmanship...",
      "coverImageUrl": "https://example.com/covers/clean-code.jpg",
      "rating": 4.7,
      "reviewCount": 1250,
      "viewCount": 523,
      "isAvailable": true,
      "createdAt": "2026-01-15T10:30:00Z",
      "updatedAt": "2026-02-10T14:20:00Z"
    },
    {
      "id": 2,
      "title": "Design Patterns",
      "isbn": "978-0201633610",
      "author": "Erich Gamma",
      "publisher": "Addison-Wesley",
      "genre": "Programming",
      "publicationYear": 1994,
      "pages": 416,
      "language": "English",
      "price": 54.99,
      "stockQuantity": 8,
      "description": "Elements of reusable object-oriented software...",
      "coverImageUrl": "https://example.com/covers/design-patterns.jpg",
      "rating": 4.6,
      "reviewCount": 890,
      "viewCount": 412,
      "isAvailable": true,
      "createdAt": "2026-01-16T11:00:00Z",
      "updatedAt": "2026-02-09T16:45:00Z"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    },
    "pageNumber": 0,
    "pageSize": 4,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "totalElements": 47,
  "totalPages": 12,
  "last": false,
  "first": true,
  "size": 4,
  "number": 0,
  "sort": {
    "sorted": true,
    "unsorted": false,
    "empty": false
  },
  "numberOfElements": 4,
  "empty": false
}
```

**Response Fields Explanation:**

- `content`: Array of book objects for current page
- `pageable`: Pagination metadata
- `totalElements`: Total number of books across all pages
- `totalPages`: Total number of pages
- `first`: Is this the first page?
- `last`: Is this the last page?
- `number`: Current page number (0-indexed)
- `size`: Page size
- `numberOfElements`: Number of elements on current page

---

### 2. Get Book by ID

**Endpoint:** `GET /api/v1/books/{id}`

**Description:** Retrieve a single book by its ID. Increments view count.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID |

**Example Request:**

```http
GET /api/v1/books/123 HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Example Response: 200 OK**

```json
{
  "id": 123,
  "title": "The Pragmatic Programmer",
  "isbn": "978-0135957059",
  "author": "David Thomas, Andrew Hunt",
  "publisher": "Addison-Wesley",
  "genre": "Programming",
  "publicationYear": 2019,
  "pages": 352,
  "language": "English",
  "price": 49.99,
  "stockQuantity": 20,
  "description": "From journeyman to master. Your journey to mastery...",
  "coverImageUrl": "https://example.com/covers/pragmatic-programmer.jpg",
  "rating": 4.8,
  "reviewCount": 1500,
  "viewCount": 750,
  "isAvailable": true,
  "createdAt": "2026-01-10T09:00:00Z",
  "updatedAt": "2026-02-11T10:15:00Z"
}
```

**Error Response: 404 Not Found**

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 123",
  "path": "/api/v1/books/123"
}
```

---

### 3. Create New Book

**Endpoint:** `POST /api/v1/books`

**Description:** Create a new book. ISBN must be unique.

**Request Body:** (JSON)

```json
{
  "title": "Clean Architecture",
  "isbn": "978-0134494166",
  "author": "Robert C. Martin",
  "publisher": "Prentice Hall",
  "genre": "Programming",
  "publicationYear": 2017,
  "pages": 432,
  "language": "English",
  "price": 39.99,
  "stockQuantity": 25,
  "description": "A craftsman's guide to software structure and design",
  "coverImageUrl": "https://example.com/covers/clean-architecture.jpg",
  "rating": 4.6,
  "reviewCount": 0,
  "isAvailable": true
}
```

**Required Fields:**
- `title` (String, max 200 chars)
- `isbn` (String, 10-20 chars, unique)
- `author` (String, max 100 chars)
- `genre` (String, max 50 chars)
- `publicationYear` (Integer, 1000-2100)
- `language` (String, max 30 chars)
- `isAvailable` (Boolean)

**Optional Fields:**
- `publisher`, `pages`, `price`, `stockQuantity`, `description`, `coverImageUrl`, `rating`, `reviewCount`

**System-Managed Fields (Auto-Generated):**
- `id`, `viewCount`, `createdAt`, `updatedAt`

**Example Request:**

```http
POST /api/v1/books HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
  "title": "Clean Architecture",
  "isbn": "978-0134494166",
  "author": "Robert C. Martin",
  "genre": "Programming",
  "publicationYear": 2017,
  "language": "English",
  "isAvailable": true
}
```

**Success Response: 201 Created**

```json
{
  "id": 456,
  "title": "Clean Architecture",
  "isbn": "978-0134494166",
  "author": "Robert C. Martin",
  "publisher": null,
  "genre": "Programming",
  "publicationYear": 2017,
  "pages": null,
  "language": "English",
  "price": null,
  "stockQuantity": 0,
  "description": null,
  "coverImageUrl": null,
  "rating": null,
  "reviewCount": 0,
  "viewCount": 0,
  "isAvailable": true,
  "createdAt": "2026-02-11T10:30:00Z",
  "updatedAt": "2026-02-11T10:30:00Z"
}
```

**Error Response: 400 Bad Request (Validation Error)**

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "title",
      "message": "Title is required"
    },
    {
      "field": "isbn",
      "message": "ISBN must be between 10 and 20 characters"
    }
  ],
  "path": "/api/v1/books"
}
```

**Error Response: 409 Conflict (Duplicate ISBN)**

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Book with ISBN 978-0134494166 already exists",
  "path": "/api/v1/books"
}
```

---

### 4. Update Book

**Endpoint:** `PUT /api/v1/books/{id}`

**Description:** Update an existing book. ISBN cannot be changed.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to update |

**Request Body:** (JSON - same as Create, but ISBN is ignored if provided)

```json
{
  "title": "Clean Architecture (Updated Edition)",
  "author": "Robert C. Martin",
  "genre": "Software Engineering",
  "publicationYear": 2017,
  "language": "English",
  "price": 44.99,
  "stockQuantity": 30,
  "isAvailable": true
}
```

**Example Request:**

```http
PUT /api/v1/books/456 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
Accept: application/json

{
  "title": "Clean Architecture (Updated Edition)",
  "author": "Robert C. Martin",
  "genre": "Software Engineering",
  "publicationYear": 2017,
  "language": "English",
  "price": 44.99,
  "isAvailable": true
}
```

**Success Response: 200 OK**

```json
{
  "id": 456,
  "title": "Clean Architecture (Updated Edition)",
  "isbn": "978-0134494166",
  "author": "Robert C. Martin",
  "publisher": null,
  "genre": "Software Engineering",
  "publicationYear": 2017,
  "pages": null,
  "language": "English",
  "price": 44.99,
  "stockQuantity": 30,
  "description": null,
  "coverImageUrl": null,
  "rating": null,
  "reviewCount": 0,
  "viewCount": 0,
  "isAvailable": true,
  "createdAt": "2026-02-11T10:30:00Z",
  "updatedAt": "2026-02-11T11:15:00Z"
}
```

**Error Response: 404 Not Found**

```json
{
  "timestamp": "2026-02-11T11:15:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 456",
  "path": "/api/v1/books/456"
}
```

---

### 5. Delete Book

**Endpoint:** `DELETE /api/v1/books/{id}`

**Description:** Permanently delete a book from the database

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to delete |

**Example Request:**

```http
DELETE /api/v1/books/456 HTTP/1.1
Host: localhost:8080
```

**Success Response: 204 No Content**

```
(Empty body)
```

**Error Response: 404 Not Found**

```json
{
  "timestamp": "2026-02-11T11:20:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 456",
  "path": "/api/v1/books/456"
}
```

---

### 6. Advanced Search

**Endpoint:** `GET /api/v1/books/search`

**Description:** Search books with multiple filters

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `q` | String | No | Quick search (title, author, ISBN) |
| `title` | String | No | Filter by title (partial match, case-insensitive) |
| `author` | String | No | Filter by author (partial match) |
| `isbn` | String | No | Filter by ISBN (exact match) |
| `publisher` | String | No | Filter by publisher (partial match) |
| `genre` | String | No | Filter by genre (exact match) |
| `language` | String | No | Filter by language (exact match) |
| `minYear` | Integer | No | Minimum publication year |
| `maxYear` | Integer | No | Maximum publication year |
| `minPrice` | Decimal | No | Minimum price |
| `maxPrice` | Decimal | No | Maximum price |
| `inStock` | Boolean | No | Filter books with stock > 0 |
| `onSale` | Boolean | No | Filter books with discount |
| `newReleases` | Boolean | No | Filter books from last year |
| `isAvailable` | Boolean | No | Filter by availability |
| `sort` | String | No | Sort by field (e.g., `title,asc`, `price,desc`, `rating,desc`) |
| `page` | Integer | No | Page number (default: 0) |
| `size` | Integer | No | Page size (default: 10) |

**Example Request: Quick Search**

```http
GET /api/v1/books/search?q=clean+code&sort=rating,desc&page=0&size=10 HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Example Request: Advanced Search with Multiple Filters**

```http
GET /api/v1/books/search?genre=Programming&minYear=2010&maxYear=2023&minPrice=30&maxPrice=60&inStock=true&sort=publicationYear,desc&page=0&size=10 HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Success Response: 200 OK**

```json
{
  "content": [
    {
      "id": 1,
      "title": "Clean Code",
      "isbn": "978-0132350884",
      "author": "Robert C. Martin",
      "genre": "Programming",
      "publicationYear": 2008,
      "price": 44.99,
      "rating": 4.7,
      "isAvailable": true
    }
  ],
  "pageable": { ... },
  "totalElements": 15,
  "totalPages": 2,
  "last": false,
  "first": true,
  "size": 10,
  "number": 0
}
```

---

### 7. Duplicate Book

**Endpoint:** `POST /api/v1/books/{id}/duplicate`

**Description:** Create a copy of an existing book with modified title and cleared ISBN

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to duplicate |

**Example Request:**

```http
POST /api/v1/books/123/duplicate HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Success Response: 201 Created**

```json
{
  "id": 789,
  "title": "The Pragmatic Programmer (Copy)",
  "isbn": "",
  "author": "David Thomas, Andrew Hunt",
  "publisher": "Addison-Wesley",
  "genre": "Programming",
  "publicationYear": 2019,
  "pages": 352,
  "language": "English",
  "price": 49.99,
  "stockQuantity": 20,
  "description": "From journeyman to master. Your journey to mastery...",
  "coverImageUrl": "https://example.com/covers/pragmatic-programmer.jpg",
  "rating": 4.8,
  "reviewCount": 0,
  "viewCount": 0,
  "isAvailable": true,
  "createdAt": "2026-02-11T11:30:00Z",
  "updatedAt": "2026-02-11T11:30:00Z"
}
```

**Notes:**
- Title is appended with " (Copy)"
- ISBN is cleared (must be entered manually)
- Review count and view count reset to 0
- All other fields copied from original
- Frontend navigates to edit form to allow user to set new ISBN

---

### 8. Export Book Data

**Endpoint:** `GET /api/v1/books/{id}/export`

**Description:** Export book data in various formats (JSON, CSV, PDF)

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to export |

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `format` | String | No | `json` | Export format: `json`, `csv`, `pdf` |

**Example Request: Export as JSON**

```http
GET /api/v1/books/123/export?format=json HTTP/1.1
Host: localhost:8080
Accept: application/json
```

**Success Response: 200 OK (JSON)**

```json
{
  "id": 123,
  "title": "The Pragmatic Programmer",
  "isbn": "978-0135957059",
  "author": "David Thomas, Andrew Hunt",
  ...
}
```

**Example Request: Export as CSV**

```http
GET /api/v1/books/123/export?format=csv HTTP/1.1
Host: localhost:8080
Accept: text/csv
```

**Success Response: 200 OK (CSV)**

```csv
id,title,isbn,author,publisher,genre,publicationYear,pages,language,price,stockQuantity,description,coverImageUrl,rating,reviewCount,viewCount,isAvailable,createdAt,updatedAt
123,"The Pragmatic Programmer","978-0135957059","David Thomas, Andrew Hunt","Addison-Wesley","Programming",2019,352,"English",49.99,20,"From journeyman to master...","https://example.com/covers/pragmatic-programmer.jpg",4.8,1500,750,true,"2026-01-10T09:00:00Z","2026-02-11T10:15:00Z"
```

**Example Request: Export as PDF**

```http
GET /api/v1/books/123/export?format=pdf HTTP/1.1
Host: localhost:8080
Accept: application/pdf
```

**Success Response: 200 OK (PDF)**

```
Content-Type: application/pdf
Content-Disposition: attachment; filename="book-123-the-pragmatic-programmer.pdf"

(Binary PDF data)
```

**PDF Format:**
- Cover image (if available)
- Title, author, ISBN prominently displayed
- All metadata in formatted table
- Description section
- Footer with export date

---

## Request/Response Formats

### Request Format

#### Content-Type

All POST/PUT requests must include:

```http
Content-Type: application/json
```

#### Request Body Structure

```json
{
  "field1": "value",
  "field2": 123,
  "field3": true,
  "nestedObject": {
    "subField": "value"
  }
}
```

#### Validation Rules

| Rule | Example | Error Message |
|------|---------|---------------|
| Required fields | `title`, `isbn`, `author` | "Title is required" |
| String length | `title` (max 200) | "Title must be less than 200 characters" |
| Number range | `publicationYear` (1000-2100) | "Publication year must be between 1000 and 2100" |
| Pattern matching | `isbn` (10-20 chars) | "ISBN must be between 10 and 20 characters" |
| Unique constraint | `isbn` | "Book with ISBN XXX already exists" |

### Response Format

#### Success Response Structure

```json
{
  "data": { ... }
}
```

For single resources:

```json
{
  "id": 123,
  "title": "Book Title",
  ...
}
```

For collections (paginated):

```json
{
  "content": [ ... ],
  "pageable": { ... },
  "totalElements": 100,
  "totalPages": 10,
  ...
}
```

#### Field Types

| Field Type | JSON Type | Example | Format |
|------------|-----------|---------|--------|
| Long/Integer | Number | `123` | Integer |
| String | String | `"Hello"` | Text |
| Boolean | Boolean | `true` or `false` | true/false |
| BigDecimal | Number | `49.99` | Decimal with 2 places |
| LocalDateTime | String | `"2026-02-11T10:30:00Z"` | ISO 8601 (UTC) |

#### Null Handling

- Optional fields may be `null` or omitted
- Required fields must never be `null`
- Empty strings represented as `null` (not `""`)

```json
{
  "publisher": null,        // ✅ Correct (optional field)
  "description": null,      // ✅ Correct (optional field)
  "title": null             // ❌ Wrong (required field)
}
```

---

## Error Handling

### Error Response Structure

All errors return a consistent format:

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 123",
  "path": "/api/v1/books/123"
}
```

For validation errors:

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "title",
      "rejectedValue": "",
      "message": "Title is required"
    },
    {
      "field": "publicationYear",
      "rejectedValue": 3000,
      "message": "Publication year must be between 1000 and 2100"
    }
  ],
  "path": "/api/v1/books"
}
```

### Error Categories

| HTTP Status | Error Type | When | Example |
|-------------|------------|------|---------|
| 400 | Bad Request | Invalid input data | Missing required field, invalid format |
| 404 | Not Found | Resource doesn't exist | Book ID not in database |
| 409 | Conflict | Duplicate resource | ISBN already exists |
| 500 | Internal Server Error | Server-side failure | Database connection error |

### Common Error Scenarios

#### 1. Validation Error (400)

**Trigger:** Required field missing, invalid format, constraint violation

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "errors": [
    {
      "field": "isbn",
      "rejectedValue": "123",
      "message": "ISBN must be between 10 and 20 characters"
    }
  ],
  "path": "/api/v1/books"
}
```

#### 2. Not Found Error (404)

**Trigger:** Requested resource ID doesn't exist

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 999",
  "path": "/api/v1/books/999"
}
```

#### 3. Conflict Error (409)

**Trigger:** Duplicate ISBN

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Book with ISBN 978-0132350884 already exists",
  "path": "/api/v1/books"
}
```

#### 4. Internal Server Error (500)

**Trigger:** Unexpected server-side failure

```json
{
  "timestamp": "2026-02-11T10:30:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/books"
}
```

### Error Handling Best Practices

1. **Client-side**: Check status code first, then parse error message
2. **Display user-friendly messages**: Convert technical errors to user-readable text
3. **Validation**: Validate on frontend before sending to reduce 400 errors
4. **Retry logic**: Implement retry for 500 errors (with exponential backoff)
5. **Logging**: Log all errors for debugging

---

## Pagination and Filtering

### Pagination

Spring Data JPA provides automatic pagination support.

#### Query Parameters

| Parameter | Type | Default | Range | Description |
|-----------|------|---------|-------|-------------|
| `page` | Integer | `0` | 0 to ∞ | Page number (0-indexed) |
| `size` | Integer | `4` | 1-100 | Items per page |
| `sort` | String | `id,asc` | - | Sort field and direction |

#### Sort Parameter Format

```
?sort=field,direction
```

**Examples:**
- `?sort=title,asc` - Sort by title ascending
- `?sort=publicationYear,desc` - Sort by year descending
- `?sort=rating,desc&sort=title,asc` - Multi-field sort (rating desc, then title asc)

#### Pagination Response

```json
{
  "content": [ ... ],          // Current page data
  "pageable": {
    "pageNumber": 0,           // Current page (0-indexed)
    "pageSize": 4,             // Items per page
    "offset": 0,               // Starting index
    "sort": { ... }
  },
  "totalElements": 47,         // Total items across all pages
  "totalPages": 12,            // Total number of pages
  "last": false,               // Is this the last page?
  "first": true,               // Is this the first page?
  "size": 4,                   // Page size
  "number": 0,                 // Current page number
  "numberOfElements": 4,       // Items on current page
  "empty": false               // Is current page empty?
}
```

#### Frontend Pagination Logic

```javascript
const totalPages = response.data.totalPages;
const currentPage = response.data.number;
const hasNextPage = !response.data.last;
const hasPrevPage = !response.data.first;

// Navigate to next page
const nextPage = currentPage + 1;
if (hasNextPage) {
  fetchBooks(nextPage, pageSize);
}

// Navigate to previous page
const prevPage = currentPage - 1;
if (hasPrevPage) {
  fetchBooks(prevPage, pageSize);
}
```

### Filtering and Search

#### Quick Search (Single Field)

```
GET /api/v1/books/search?q=clean+code
```

Searches across: title, author, ISBN

#### Advanced Search (Multiple Fields)

```
GET /api/v1/books/search?genre=Programming&minYear=2010&maxYear=2023&inStock=true&sort=rating,desc
```

#### Filter Combinations (AND Logic)

All filters use AND logic:

```
genre=Programming AND minYear=2010 AND maxYear=2023 AND inStock=true
```

#### Filter Types

| Filter Type | Parameters | Logic |
|-------------|------------|-------|
| Exact Match | `genre`, `language`, `isAvailable` | Field = value |
| Partial Match | `title`, `author`, `publisher` | Field LIKE '%value%' (case-insensitive) |
| Range | `minYear/maxYear`, `minPrice/maxPrice` | Field BETWEEN min AND max |
| Boolean | `inStock`, `onSale`, `newReleases` | Computed condition |

---

## HTTP Status Codes

### Success Codes (2xx)

| Code | Status | When | Response Body |
|------|--------|------|---------------|
| 200 | OK | Successful GET, PUT | Resource data |
| 201 | Created | Successful POST | Created resource data |
| 204 | No Content | Successful DELETE | Empty |

### Client Error Codes (4xx)

| Code | Status | When | Response Body |
|------|--------|------|---------------|
| 400 | Bad Request | Validation failure, invalid input | Error details with field-level errors |
| 404 | Not Found | Resource ID doesn't exist | Error message |
| 409 | Conflict | Duplicate constraint violation (e.g., ISBN) | Error message |

### Server Error Codes (5xx)

| Code | Status | When | Response Body |
|------|--------|------|---------------|
| 500 | Internal Server Error | Unexpected server failure | Error message |

### Status Code Usage Examples

#### Successful Operations

```http
POST /api/v1/books → 201 Created
GET /api/v1/books/123 → 200 OK
PUT /api/v1/books/123 → 200 OK
DELETE /api/v1/books/123 → 204 No Content
```

#### Error Scenarios

```http
GET /api/v1/books/999 → 404 Not Found (ID doesn't exist)
POST /api/v1/books → 400 Bad Request (Missing required field)
POST /api/v1/books → 409 Conflict (ISBN already exists)
GET /api/v1/books → 500 Internal Server Error (Database down)
```

---

## API Versioning

### V1 Strategy: URI Path Versioning

All endpoints prefixed with `/api/v1/`:

```
/api/v1/books
/api/v1/books/123
/api/v1/books/search
```

### Why URI Versioning?

✅ **Pros:**
- Simple and explicit
- Easy to route in backend
- Clear in browser/Postman
- Caching-friendly

❌ **Cons:**
- URL changes when version changes

### Future Versioning (V2)

When breaking changes are introduced:

1. **Create new endpoints**: `/api/v2/books`
2. **Maintain V1**: Keep `/api/v1/books` for backward compatibility
3. **Deprecation period**: 6 months before V1 removal
4. **Documentation**: Clearly mark V1 as deprecated

**Example V2 Changes:**
- Add authentication (JWT tokens)
- HATEOAS links
- GraphQL alternative
- WebSocket support for real-time updates

---

## CORS Configuration

### Development CORS

**Allowed Origin:** `http://localhost:5173` (Vite dev server)

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

### Production CORS

**Allowed Origin:** Production frontend URL (e.g., `https://booksonline.com`)

```java
.allowedOrigins("https://booksonline.com")
```

### CORS Headers (Automatic)

```http
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Allow-Credentials: true
```

---

## API Testing

### 1. Swagger UI (Automatic Documentation)

**URL:** `http://localhost:8080/swagger-ui.html`

**Features:**
- Interactive API documentation
- Try endpoints directly from browser
- See request/response examples
- View schemas and validation rules

**Generated by:** Springdoc OpenAPI

### 2. Postman Collection

Create a Postman collection with:

#### Environment Variables

```json
{
  "baseUrl": "http://localhost:8080/api/v1",
  "bookId": "123"
}
```

#### Sample Requests

**Collection: Books API**

1. **Get All Books**
   - Method: GET
   - URL: `{{baseUrl}}/books?page=0&size=4`

2. **Get Book by ID**
   - Method: GET
   - URL: `{{baseUrl}}/books/{{bookId}}`

3. **Create Book**
   - Method: POST
   - URL: `{{baseUrl}}/books`
   - Body: Raw JSON

```json
{
  "title": "Test Book",
  "isbn": "978-1234567890",
  "author": "Test Author",
  "genre": "Programming",
  "publicationYear": 2024,
  "language": "English",
  "isAvailable": true
}
```

4. **Update Book**
   - Method: PUT
   - URL: `{{baseUrl}}/books/{{bookId}}`
   - Body: Raw JSON

5. **Delete Book**
   - Method: DELETE
   - URL: `{{baseUrl}}/books/{{bookId}}`

6. **Search Books**
   - Method: GET
   - URL: `{{baseUrl}}/books/search?genre=Programming&minYear=2010`

### 3. Curl Examples

```bash
# Get all books
curl -X GET "http://localhost:8080/api/v1/books?page=0&size=4" \
     -H "Accept: application/json"

# Get book by ID
curl -X GET "http://localhost:8080/api/v1/books/123" \
     -H "Accept: application/json"

# Create book
curl -X POST "http://localhost:8080/api/v1/books" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
       "title": "Test Book",
       "isbn": "978-1234567890",
       "author": "Test Author",
       "genre": "Programming",
       "publicationYear": 2024,
       "language": "English",
       "isAvailable": true
     }'

# Update book
curl -X PUT "http://localhost:8080/api/v1/books/123" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
       "title": "Updated Book Title",
       "author": "Updated Author",
       "genre": "Programming",
       "publicationYear": 2024,
       "language": "English",
       "isAvailable": true
     }'

# Delete book
curl -X DELETE "http://localhost:8080/api/v1/books/123"

# Search books
curl -X GET "http://localhost:8080/api/v1/books/search?genre=Programming&minYear=2010&maxYear=2023&sort=rating,desc" \
     -H "Accept: application/json"
```

---

## Summary

### API Design Highlights

✅ **RESTful Design**: Resource-based URLs with standard HTTP methods
✅ **Consistent Conventions**: Uniform naming, error format, pagination
✅ **Comprehensive Error Handling**: Field-level validation errors with clear messages
✅ **Pagination Support**: Spring Data JPA pagination for all list endpoints
✅ **Advanced Search**: Multiple filter types (exact, partial, range, boolean)
✅ **Sorting**: Multi-field sorting support
✅ **CORS Enabled**: Frontend can access API from different origin
✅ **Auto-Documentation**: Swagger UI generated from code
✅ **Export Functionality**: JSON, CSV, PDF export formats
✅ **Versioning Strategy**: URI path versioning (`/api/v1/`)

### Total Endpoints: 8

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/books` | List all books (paginated) |
| GET | `/api/v1/books/{id}` | Get single book |
| POST | `/api/v1/books` | Create new book |
| PUT | `/api/v1/books/{id}` | Update book |
| DELETE | `/api/v1/books/{id}` | Delete book |
| GET | `/api/v1/books/search` | Advanced search |
| POST | `/api/v1/books/{id}/duplicate` | Duplicate book |
| GET | `/api/v1/books/{id}/export` | Export book data |

### Frontend Integration

1. **Axios Configuration**: Create base instance with `baseURL`
2. **Error Handling**: Use interceptors for global error handling
3. **Loading States**: Show spinners during API calls
4. **Toast Notifications**: Display success/error messages from API responses
5. **Pagination**: Use `page`, `size`, `sort` parameters
6. **Search**: Build query strings from form inputs

---

**Ready for implementation!**
