# Books Online API Documentation

**Version:** 1.0.0
**Base URL:** `http://localhost:8080/api/v1`
**Last Updated:** 2026-02-11

---

## Table of Contents

1. [Introduction](#introduction)
2. [Getting Started](#getting-started)
3. [Authentication](#authentication)
4. [Endpoints Overview](#endpoints-overview)
5. [Detailed API Reference](#detailed-api-reference)
6. [Common Use Cases](#common-use-cases)
7. [Error Handling](#error-handling)
8. [Best Practices](#best-practices)
9. [Code Examples](#code-examples)
10. [Changelog](#changelog)

---

## Introduction

Welcome to the Books Online API documentation! This REST API provides comprehensive book management capabilities for the Books Online application.

### What can you do with this API?

- 📚 **Browse Books**: Retrieve paginated lists of books with sorting
- 🔍 **Advanced Search**: Search books with multiple filter criteria
- ➕ **Create Books**: Add new books to the catalog
- ✏️ **Update Books**: Modify existing book information
- 🗑️ **Delete Books**: Remove books from the system
- 📋 **Duplicate Books**: Create copies for quick entry
- 📥 **Export Data**: Download book data in JSON, CSV, or PDF formats

### Key Features

- **RESTful Design**: Standard HTTP methods (GET, POST, PUT, DELETE)
- **JSON Format**: All requests and responses use JSON
- **Pagination**: Efficient handling of large datasets
- **Advanced Filtering**: Multiple search parameters
- **Error Handling**: Consistent error responses with validation details
- **CORS Enabled**: Frontend-friendly cross-origin support

---

## Getting Started

### Prerequisites

- API Base URL: `http://localhost:8080/api/v1` (development)
- Content-Type: `application/json`
- No authentication required in V1 (open access)

### Quick Start

```bash
# List all books
curl -X GET "http://localhost:8080/api/v1/books?page=0&size=4" \
     -H "Accept: application/json"

# Get a specific book
curl -X GET "http://localhost:8080/api/v1/books/1" \
     -H "Accept: application/json"

# Create a new book
curl -X POST "http://localhost:8080/api/v1/books" \
     -H "Content-Type: application/json" \
     -d '{
       "title": "Clean Code",
       "isbn": "978-0132350884",
       "author": "Robert C. Martin",
       "genre": "Programming",
       "publicationYear": 2008,
       "language": "English",
       "isAvailable": true
     }'
```

### Response Format

All successful responses return JSON data:

```json
{
  "id": 1,
  "title": "Clean Code",
  "isbn": "978-0132350884",
  "author": "Robert C. Martin",
  ...
}
```

Paginated responses include metadata:

```json
{
  "content": [...],
  "totalElements": 47,
  "totalPages": 12,
  "number": 0,
  "size": 4,
  "first": true,
  "last": false
}
```

---

## Authentication

### V1 (Current): No Authentication

All endpoints are **open access** in V1. No authentication headers required.

**Use Case:** Development, demos, proof-of-concept

### V2 (Future): JWT Authentication

V2 will implement JWT-based authentication:

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**Planned Features:**
- User registration and login
- Role-based access control (RBAC)
- Token expiration and refresh
- Secure endpoints

---

## Endpoints Overview

### Books Management

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/books` | List all books (paginated) | 200 OK |
| GET | `/books/{id}` | Get single book by ID | 200 OK |
| POST | `/books` | Create new book | 201 Created |
| PUT | `/books/{id}` | Update existing book | 200 OK |
| DELETE | `/books/{id}` | Delete book | 204 No Content |

### Search

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| GET | `/books/search` | Advanced search with filters | 200 OK |

### Actions

| Method | Endpoint | Description | Status Code |
|--------|----------|-------------|-------------|
| POST | `/books/{id}/duplicate` | Duplicate existing book | 201 Created |
| GET | `/books/{id}/export` | Export book data (JSON/CSV/PDF) | 200 OK |

---

## Detailed API Reference

### 1. List All Books

**Endpoint:** `GET /books`

**Description:** Retrieve a paginated list of all books with optional sorting.

**Query Parameters:**

| Parameter | Type | Required | Default | Description |
|-----------|------|----------|---------|-------------|
| `page` | Integer | No | 0 | Page number (0-indexed) |
| `size` | Integer | No | 4 | Items per page (1-100) |
| `sort` | String | No | id,asc | Sort field and direction |

**Sort Examples:**
- `sort=title,asc` - Sort by title ascending
- `sort=publicationYear,desc` - Sort by year descending
- `sort=rating,desc` - Sort by rating descending

**Example Request:**

```bash
curl -X GET "http://localhost:8080/api/v1/books?page=0&size=4&sort=title,asc" \
     -H "Accept: application/json"
```

**Example Response (200 OK):**

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
      "description": "A handbook of agile software craftsmanship",
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
      "description": "Elements of reusable object-oriented software",
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
    "pageNumber": 0,
    "pageSize": 4,
    "offset": 0
  },
  "totalElements": 47,
  "totalPages": 12,
  "last": false,
  "first": true,
  "size": 4,
  "number": 0,
  "numberOfElements": 2,
  "empty": false
}
```

**Pagination Logic:**

```javascript
// Frontend pagination helper
const response = await axios.get('/api/v1/books', {
  params: { page: 0, size: 4 }
});

const {
  content,           // Array of books
  totalElements,     // Total books across all pages
  totalPages,        // Total number of pages
  number,            // Current page (0-indexed)
  first,             // Is this the first page?
  last               // Is this the last page?
} = response.data;

const hasNextPage = !last;
const hasPrevPage = !first;
```

---

### 2. Get Book by ID

**Endpoint:** `GET /books/{id}`

**Description:** Retrieve a single book by its unique ID. Automatically increments the view count.

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID |

**Example Request:**

```bash
curl -X GET "http://localhost:8080/api/v1/books/123" \
     -H "Accept: application/json"
```

**Example Response (200 OK):**

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

**Error Response (404 Not Found):**

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

**Endpoint:** `POST /books`

**Description:** Create a new book entry. ISBN must be unique.

**Request Headers:**
```
Content-Type: application/json
Accept: application/json
```

**Request Body (JSON):**

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

**Example Request (Minimal):**

```bash
curl -X POST "http://localhost:8080/api/v1/books" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
       "title": "Clean Architecture",
       "isbn": "978-0134494166",
       "author": "Robert C. Martin",
       "genre": "Programming",
       "publicationYear": 2017,
       "language": "English",
       "isAvailable": true
     }'
```

**Example Request (Complete):**

```bash
curl -X POST "http://localhost:8080/api/v1/books" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
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
       "description": "A craftsman'\''s guide to software structure and design",
       "coverImageUrl": "https://example.com/covers/clean-architecture.jpg",
       "rating": 4.6,
       "reviewCount": 0,
       "isAvailable": true
     }'
```

**Success Response (201 Created):**

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

**Validation Error (400 Bad Request):**

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
      "field": "isbn",
      "rejectedValue": "123",
      "message": "ISBN must be between 10 and 20 characters"
    }
  ],
  "path": "/api/v1/books"
}
```

**Duplicate ISBN Error (409 Conflict):**

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

**Endpoint:** `PUT /books/{id}`

**Description:** Update an existing book. **Note:** ISBN cannot be changed (immutable).

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to update |

**Request Body:** Same structure as Create, but ISBN is ignored if provided

**Example Request:**

```bash
curl -X PUT "http://localhost:8080/api/v1/books/456" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
       "title": "Clean Architecture (Updated Edition)",
       "author": "Robert C. Martin",
       "genre": "Software Engineering",
       "publicationYear": 2017,
       "language": "English",
       "price": 44.99,
       "stockQuantity": 30,
       "isAvailable": true
     }'
```

**Success Response (200 OK):**

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

**Error Response (404 Not Found):**

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

**Endpoint:** `DELETE /books/{id}`

**Description:** Permanently delete a book from the database.

**⚠️ Warning:** This operation is irreversible!

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to delete |

**Example Request:**

```bash
curl -X DELETE "http://localhost:8080/api/v1/books/456"
```

**Success Response (204 No Content):**

```
(Empty body - no content returned)
```

**Error Response (404 Not Found):**

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

**Endpoint:** `GET /books/search`

**Description:** Search books with multiple filter criteria.

**Query Parameters:**

#### Quick Search
| Parameter | Type | Description |
|-----------|------|-------------|
| `q` | String | Search across title, author, ISBN |

#### Text Filters (Partial Match, Case-Insensitive)
| Parameter | Type | Description |
|-----------|------|-------------|
| `title` | String | Filter by title |
| `author` | String | Filter by author |
| `isbn` | String | Filter by ISBN (exact match) |
| `publisher` | String | Filter by publisher |

#### Exact Match Filters
| Parameter | Type | Description |
|-----------|------|-------------|
| `genre` | String | Filter by genre |
| `language` | String | Filter by language |
| `isAvailable` | Boolean | Filter by availability |

#### Range Filters
| Parameter | Type | Description |
|-----------|------|-------------|
| `minYear` | Integer | Minimum publication year |
| `maxYear` | Integer | Maximum publication year |
| `minPrice` | Decimal | Minimum price |
| `maxPrice` | Decimal | Maximum price |

#### Boolean Filters
| Parameter | Type | Description |
|-----------|------|-------------|
| `inStock` | Boolean | Filter books with stock > 0 |
| `onSale` | Boolean | Filter books with discount |
| `newReleases` | Boolean | Filter books from last year |

#### Pagination & Sorting
| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `sort` | String | id,asc | Sort field and direction |
| `page` | Integer | 0 | Page number |
| `size` | Integer | 10 | Items per page |

**Filter Logic:** All filters use **AND** logic

**Example 1: Quick Search**

```bash
curl -X GET "http://localhost:8080/api/v1/books/search?q=clean+code&sort=rating,desc" \
     -H "Accept: application/json"
```

**Example 2: Genre + Year Range**

```bash
curl -X GET "http://localhost:8080/api/v1/books/search?genre=Programming&minYear=2010&maxYear=2023&sort=publicationYear,desc" \
     -H "Accept: application/json"
```

**Example 3: Price Range + In Stock**

```bash
curl -X GET "http://localhost:8080/api/v1/books/search?minPrice=30&maxPrice=60&inStock=true&sort=price,asc" \
     -H "Accept: application/json"
```

**Example 4: Multiple Filters**

```bash
curl -X GET "http://localhost:8080/api/v1/books/search?genre=Programming&language=English&minYear=2015&maxYear=2023&minPrice=20&maxPrice=100&isAvailable=true&sort=rating,desc&page=0&size=10" \
     -H "Accept: application/json"
```

**Success Response (200 OK):**

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

**Endpoint:** `POST /books/{id}/duplicate`

**Description:** Create a copy of an existing book with modifications.

**Modifications Applied:**
- Title appended with " (Copy)"
- ISBN cleared (empty string)
- Review count reset to 0
- View count reset to 0
- All other fields copied from original

**Typical Workflow:**
1. User clicks "Duplicate" button
2. Backend creates copy with modifications
3. Frontend receives duplicated book
4. Frontend navigates to edit form
5. User enters new unique ISBN
6. User saves the book

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to duplicate |

**Example Request:**

```bash
curl -X POST "http://localhost:8080/api/v1/books/123/duplicate" \
     -H "Accept: application/json"
```

**Success Response (201 Created):**

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

**Error Response (404 Not Found):**

```json
{
  "timestamp": "2026-02-11T11:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with id: 123",
  "path": "/api/v1/books/123/duplicate"
}
```

---

### 8. Export Book Data

**Endpoint:** `GET /books/{id}/export`

**Description:** Export book data in multiple formats (JSON, CSV, PDF).

**Path Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `id` | Long | Yes | Book ID to export |

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `format` | String | json | Export format: `json`, `csv`, `pdf` |

**Format Details:**

#### JSON Format
- Complete book object
- Standard JSON structure
- `Content-Type: application/json`

#### CSV Format
- Spreadsheet-compatible
- All fields as columns
- `Content-Type: text/csv`
- `Content-Disposition: attachment; filename="book-{id}.csv"`

#### PDF Format
- Formatted document with:
  - Cover image (if available)
  - Title, author, ISBN prominently displayed
  - All metadata in formatted table
  - Description section
  - Footer with export date
- `Content-Type: application/pdf`
- `Content-Disposition: attachment; filename="book-{id}-{title}.pdf"`

**Example 1: Export as JSON**

```bash
curl -X GET "http://localhost:8080/api/v1/books/123/export?format=json" \
     -H "Accept: application/json"
```

**Response:**

```json
{
  "id": 123,
  "title": "The Pragmatic Programmer",
  "isbn": "978-0135957059",
  "author": "David Thomas, Andrew Hunt",
  ...
}
```

**Example 2: Export as CSV**

```bash
curl -X GET "http://localhost:8080/api/v1/books/123/export?format=csv" \
     -H "Accept: text/csv" \
     -o book-123.csv
```

**Response:**

```csv
id,title,isbn,author,publisher,genre,publicationYear,pages,language,price,stockQuantity,description,coverImageUrl,rating,reviewCount,viewCount,isAvailable,createdAt,updatedAt
123,"The Pragmatic Programmer","978-0135957059","David Thomas, Andrew Hunt","Addison-Wesley","Programming",2019,352,"English",49.99,20,"From journeyman to master...","https://example.com/covers/pragmatic-programmer.jpg",4.8,1500,750,true,"2026-01-10T09:00:00Z","2026-02-11T10:15:00Z"
```

**Example 3: Export as PDF**

```bash
curl -X GET "http://localhost:8080/api/v1/books/123/export?format=pdf" \
     -H "Accept: application/pdf" \
     -o book-123.pdf
```

**Response:**

```
Content-Type: application/pdf
Content-Disposition: attachment; filename="book-123-the-pragmatic-programmer.pdf"

(Binary PDF data)
```

---

## Common Use Cases

### Use Case 1: Display Book List on Homepage

```javascript
// Fetch first page of books (4 items)
const response = await axios.get('/api/v1/books', {
  params: {
    page: 0,
    size: 4,
    sort: 'rating,desc'  // Show highest rated books first
  }
});

const books = response.data.content;
const totalBooks = response.data.totalElements;
```

### Use Case 2: Search Books by Genre and Year

```javascript
// Find Programming books from 2010-2023
const response = await axios.get('/api/v1/books/search', {
  params: {
    genre: 'Programming',
    minYear: 2010,
    maxYear: 2023,
    sort: 'publicationYear,desc',
    page: 0,
    size: 20
  }
});
```

### Use Case 3: Create New Book with Validation

```javascript
try {
  const response = await axios.post('/api/v1/books', {
    title: 'Clean Code',
    isbn: '978-0132350884',
    author: 'Robert C. Martin',
    genre: 'Programming',
    publicationYear: 2008,
    language: 'English',
    isAvailable: true
  });

  console.log('Book created:', response.data.id);
} catch (error) {
  if (error.response.status === 400) {
    // Validation error
    const errors = error.response.data.errors;
    errors.forEach(e => console.error(`${e.field}: ${e.message}`));
  } else if (error.response.status === 409) {
    // Duplicate ISBN
    console.error('ISBN already exists');
  }
}
```

### Use Case 4: Update Book Stock Quantity

```javascript
// Update stock after purchase
const bookId = 123;
const currentBook = await axios.get(`/api/v1/books/${bookId}`);

const updatedBook = {
  ...currentBook.data,
  stockQuantity: currentBook.data.stockQuantity - 1
};

await axios.put(`/api/v1/books/${bookId}`, updatedBook);
```

### Use Case 5: Duplicate Book for New Edition

```javascript
// Duplicate and modify for new edition
const originalId = 123;
const response = await axios.post(`/api/v1/books/${originalId}/duplicate`);

// Navigate to edit form with duplicated book
const duplicatedBook = response.data;
// User enters new ISBN and saves
```

### Use Case 6: Export Book Details as PDF

```javascript
// Download book as PDF
const bookId = 123;
const response = await axios.get(`/api/v1/books/${bookId}/export`, {
  params: { format: 'pdf' },
  responseType: 'blob'  // Important for binary data
});

// Create download link
const url = window.URL.createObjectURL(new Blob([response.data]));
const link = document.createElement('a');
link.href = url;
link.setAttribute('download', `book-${bookId}.pdf`);
document.body.appendChild(link);
link.click();
```

### Use Case 7: Implement Infinite Scroll

```javascript
let currentPage = 0;
const pageSize = 10;
let hasMore = true;

async function loadMoreBooks() {
  if (!hasMore) return;

  const response = await axios.get('/api/v1/books', {
    params: {
      page: currentPage,
      size: pageSize,
      sort: 'createdAt,desc'
    }
  });

  const books = response.data.content;
  hasMore = !response.data.last;
  currentPage++;

  // Append books to UI
  appendBooksToUI(books);
}

// Load more on scroll
window.addEventListener('scroll', () => {
  if (window.innerHeight + window.scrollY >= document.body.offsetHeight) {
    loadMoreBooks();
  }
});
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

### Validation Errors

Validation errors include field-level details:

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

### HTTP Status Codes

| Status Code | Meaning | When |
|-------------|---------|------|
| 200 OK | Success | GET, PUT successful |
| 201 Created | Resource created | POST successful |
| 204 No Content | Success with no body | DELETE successful |
| 400 Bad Request | Invalid input | Validation failure |
| 404 Not Found | Resource not found | Invalid ID |
| 409 Conflict | Duplicate resource | ISBN already exists |
| 500 Internal Server Error | Server error | Unexpected failure |

### Frontend Error Handling

```javascript
// Axios interceptor for global error handling
axios.interceptors.response.use(
  response => response,
  error => {
    const status = error.response?.status;
    const message = error.response?.data?.message;

    switch (status) {
      case 400:
        // Validation error - show field errors
        showValidationErrors(error.response.data.errors);
        break;
      case 404:
        // Not found - show toast
        showToast('Book not found', 'error');
        break;
      case 409:
        // Conflict - show specific message
        showToast(message, 'error');
        break;
      case 500:
        // Server error - show generic message
        showToast('An unexpected error occurred', 'error');
        break;
      default:
        showToast('An error occurred', 'error');
    }

    return Promise.reject(error);
  }
);
```

---

## Best Practices

### 1. Always Validate on Frontend

Validate user input before sending to API to reduce unnecessary requests:

```javascript
import { z } from 'zod';

const bookSchema = z.object({
  title: z.string().min(1).max(200),
  isbn: z.string().min(10).max(20),
  author: z.string().min(1).max(100),
  genre: z.string().min(1).max(50),
  publicationYear: z.number().int().min(1000).max(2100),
  language: z.string().min(1).max(30),
  isAvailable: z.boolean()
});

// Validate before sending
try {
  const validData = bookSchema.parse(formData);
  await createBook(validData);
} catch (error) {
  // Show validation errors to user
}
```

### 2. Use Pagination for Large Lists

Always use pagination to avoid overwhelming the frontend:

```javascript
// Bad: Fetch all books at once
const allBooks = await axios.get('/api/v1/books?size=1000');

// Good: Fetch with pagination
const booksPage = await axios.get('/api/v1/books?page=0&size=20');
```

### 3. Debounce Search Requests

Reduce API calls during search by debouncing:

```javascript
import { debounce } from 'lodash';

const searchBooks = debounce(async (query) => {
  const response = await axios.get('/api/v1/books/search', {
    params: { q: query }
  });
  setSearchResults(response.data.content);
}, 500);  // Wait 500ms after user stops typing
```

### 4. Handle Loading States

Always show loading indicators during API calls:

```javascript
const [loading, setLoading] = useState(false);
const [books, setBooks] = useState([]);

async function fetchBooks() {
  setLoading(true);
  try {
    const response = await axios.get('/api/v1/books');
    setBooks(response.data.content);
  } catch (error) {
    // Handle error
  } finally {
    setLoading(false);
  }
}
```

### 5. Cache Responses When Appropriate

Cache book details to reduce duplicate requests:

```javascript
const bookCache = new Map();

async function getBook(id) {
  if (bookCache.has(id)) {
    return bookCache.get(id);
  }

  const response = await axios.get(`/api/v1/books/${id}`);
  bookCache.set(id, response.data);
  return response.data;
}
```

---

## Code Examples

### React Axios Service

```javascript
// services/booksService.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/v1';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
});

export const booksService = {
  // List all books
  getAllBooks: (page = 0, size = 4, sort = 'id,asc') =>
    api.get('/books', { params: { page, size, sort } }),

  // Get single book
  getBookById: (id) =>
    api.get(`/books/${id}`),

  // Create new book
  createBook: (bookData) =>
    api.post('/books', bookData),

  // Update book
  updateBook: (id, bookData) =>
    api.put(`/books/${id}`, bookData),

  // Delete book
  deleteBook: (id) =>
    api.delete(`/books/${id}`),

  // Search books
  searchBooks: (params) =>
    api.get('/books/search', { params }),

  // Duplicate book
  duplicateBook: (id) =>
    api.post(`/books/${id}/duplicate`),

  // Export book
  exportBook: (id, format = 'json') =>
    api.get(`/books/${id}/export`, {
      params: { format },
      responseType: format === 'pdf' || format === 'csv' ? 'blob' : 'json'
    })
};
```

### React Component Example

```javascript
// components/BooksList.jsx
import { useState, useEffect } from 'react';
import { booksService } from '../services/booksService';

function BooksList() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchBooks();
  }, [page]);

  async function fetchBooks() {
    setLoading(true);
    try {
      const response = await booksService.getAllBooks(page, 4, 'title,asc');
      setBooks(response.data.content);
      setTotalPages(response.data.totalPages);
    } catch (error) {
      console.error('Failed to fetch books:', error);
    } finally {
      setLoading(false);
    }
  }

  async function handleDelete(id) {
    if (!confirm('Are you sure you want to delete this book?')) return;

    try {
      await booksService.deleteBook(id);
      fetchBooks();  // Refresh list
    } catch (error) {
      console.error('Failed to delete book:', error);
    }
  }

  return (
    <div>
      <h1>Books List</h1>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          <ul>
            {books.map(book => (
              <li key={book.id}>
                {book.title} by {book.author}
                <button onClick={() => handleDelete(book.id)}>Delete</button>
              </li>
            ))}
          </ul>

          <div>
            <button
              disabled={page === 0}
              onClick={() => setPage(page - 1)}
            >
              Previous
            </button>
            <span>Page {page + 1} of {totalPages}</span>
            <button
              disabled={page >= totalPages - 1}
              onClick={() => setPage(page + 1)}
            >
              Next
            </button>
          </div>
        </>
      )}
    </div>
  );
}

export default BooksList;
```

---

## Changelog

### Version 1.0.0 (2026-02-11)

**Initial Release:**
- 8 REST API endpoints
- Books CRUD operations
- Advanced search with multiple filters
- Pagination and sorting support
- Export functionality (JSON, CSV, PDF)
- Duplicate book feature
- Comprehensive error handling
- No authentication (open access)

**Endpoints:**
- GET `/books` - List all books
- GET `/books/{id}` - Get book by ID
- POST `/books` - Create new book
- PUT `/books/{id}` - Update book
- DELETE `/books/{id}` - Delete book
- GET `/books/search` - Advanced search
- POST `/books/{id}/duplicate` - Duplicate book
- GET `/books/{id}/export` - Export book data

**Roadmap (V2):**
- JWT-based authentication
- Role-based access control
- User management endpoints
- Rate limiting
- Audit logging
- Enhanced search (full-text)
- Recommendations engine

---

## Support

**Documentation:** https://booksonline.com/docs
**API Support:** support@booksonline.com
**GitHub Issues:** https://github.com/booksonline/api/issues

For more information, visit the [OpenAPI Specification](/api/v1/swagger-ui.html) or download the [YAML file](./openapi-spec.yaml).

---

**Generated by:** Books Online API Team
**Last Updated:** 2026-02-11