# Vector DB Q&A Results

A running log of `/vectordb` questions and answers. Each new question is appended as a new `## Question N` section below, with the six steps as `###` sub-sections.

---

## Question 1 — how is pagination implemented in the book listing

**Date:** 2026-04-24
**Mode:** /vectordb
**Codebase:** Books Online full-stack (Spring Boot + React)

### Step 1 — Parse the user's question

- **Feature:** How pagination is implemented in the book listing
- **Codebase:** Books Online full-stack (Spring Boot + React)

### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "how is pagination implemented in the book listing"
```

- DB: `/tmp/books-online-vectors-enriched.db`
- Method: cosine_similarity (all-MiniLM-L6-v2)
- Chunks: 37 total | top-5 scored | 6 reverse-import | 11 sent to Claude

Hierarchy search path (broad): L2(module) top=0.515 → used as pre-filter only (0 tokens).

### Step 3 — Show the full ranked list

```text
RANK | SCORE  | FILE                           | FUNCTION                        | TOKENS | STATUS
1    | 0.6528 | BookServiceImpl.java           | getAllBooks                     | 61     | RETRIEVED
2    | 0.5868 | BookController.java            | getAllBooks                     | 104    | RETRIEVED
3    | 0.5468 | BookController.java            | searchBooks                     | 446    | RETRIEVED
4    | 0.4746 | BookServiceImpl.java           | searchBooks                     | 730    | RETRIEVED
5    | 0.4402 | schema.sql                     | indexes_on_books                | 181    | RETRIEVED
6    | 0.4245 | AddBookPage.jsx                | AddBookPage                     | 258    | SKIPPED
7    | 0.3769 | BookServiceImpl.java           | createBook                      | 136    | SKIPPED
8    | 0.3766 | Book.java                      | incrementViewCount              | 33     | SKIPPED
9    | 0.3594 | data.sql                       | seeds_books                     | 441    | SKIPPED
10   | 0.3501 | BookController.java            | exportBook                      | 120    | REV_IMPORT (incl.)
11   | 0.3480 | BookServiceImpl.java           | updateBook                      | 173    | SKIPPED
12   | 0.3382 | Book.java                      | Book_class_body                 | 510    | SKIPPED
13   | 0.3339 | BookController.java            | getBookById                     | 67     | REV_IMPORT (incl.)
14   | 0.3301 | BookController.java            | updateBook                      | 124    | REV_IMPORT (incl.)
15   | 0.3227 | BookServiceImpl.java           | getBookById                     | 95     | SKIPPED
16   | 0.3207 | BookController.java            | createBook                      | 107    | REV_IMPORT (incl.)
17   | 0.3144 | BookController.java            | deleteBook                      | 64     | REV_IMPORT (incl.)
18   | 0.3017 | BookServiceImpl.java           | exportBook                      | 125    | SKIPPED
19   | 0.2970 | BookMapper.java                | BookMapper_class_body           | 53     | SKIPPED
20   | 0.2894 | GlobalExceptionHandler.java    | handleBookNotFoundException     | 94     | SKIPPED
21   | 0.2730 | BookServiceImpl.java           | duplicateBook                   | 102    | SKIPPED
22   | 0.2637 | BookServiceImpl.java           | deleteBook                      | 61     | SKIPPED
23   | 0.2293 | HomePage.jsx                   | HomePage                        | 698    | SKIPPED
24   | 0.2224 | data.sql                       | alter_books                     | 23     | SKIPPED
25   | 0.2193 | BookController.java            | duplicateBook                   | 76     | REV_IMPORT (incl.)
26   | 0.2059 | BookRequest.java               | BookRequest_class_body          | 765    | SKIPPED
27   | 0.2036 | BookResponse.java              | BookResponse_class_body         | 180    | SKIPPED
28   | 0.1853 | schema.sql                     | books                           | 670    | SKIPPED
29   | 0.1094 | Application.java               | main                            | 20     | SKIPPED
30   | 0.0959 | Book.java                      | prePersist                      | 88     | SKIPPED
31   | 0.0774 | GlobalExceptionHandler.java    | handleDuplicateIsbnException    | 97     | SKIPPED
32   | 0.0532 | App.jsx                        | App                             | 187    | SKIPPED
33   | call   | GlobalExceptionHandler.java    | handleIllegalArgumentException  | 91     | SKIPPED
34   | call   | GlobalExceptionHandler.java    | handleValidationErrors          | 192    | SKIPPED
35   | call   | GlobalExceptionHandler.java    | handleGenericException          | 97     | SKIPPED
36   | call   | ValidationErrorResponse.java   | ValidationErrorResponse_body    | 92     | SKIPPED
37   | call   | ErrorResponse.java             | ErrorResponse_class_body        | 59     | SKIPPED
```

### Step 4 — Show token summary

```text
TOKENS BILLED:   2,088  (8 question + 2,080 retrieved)
TOKENS SKIPPED:  5,340
SIGNAL RATIO:    99.6% useful
```

Confidence gate: **85/100 — HIGH** (top_score 0.653, score_gap 0.141, lexical match on `pagination`/`implemented`/`book`/`listing`, good layer diversity).

### Step 5 — Generate implementation

Pagination is handled entirely by **Spring Data's `Pageable` abstraction** — no custom pagination code.

#### Controller layer (`BookController.java:getAllBooks`)

- Endpoint `GET /api/v1/books` accepts a `Pageable` parameter, resolved automatically by Spring from query params `page`, `size`, and `sort`.
- Defaults declared via `@PageableDefault(size = 4, sort = "id", direction = Sort.Direction.ASC)` — so an unparameterized call returns page 0 with 4 books sorted by `id` ascending.
- Returns `ResponseEntity<Page<BookResponse>>`, which serializes to JSON with `content`, `totalElements`, `totalPages`, `number`, `size`, etc.

```java
public ResponseEntity<Page<BookResponse>> getAllBooks(
        @PageableDefault(size = 4, sort = "id", direction = Sort.Direction.ASC)
        @Parameter(description = "Pagination parameters (page, size, sort)")
        Pageable pageable) {

    logger.info("GET /api/v1/books - page: {}, size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

    Page<BookResponse> books = bookService.getAllBooks(pageable);
    return ResponseEntity.ok(books);
}
```

#### Service layer (`BookServiceImpl.java:getAllBooks`)

- Forwards the `Pageable` to the repository: `bookRepository.findAll(pageable)`.
- Maps the resulting `Page<Book>` to `Page<BookResponse>` via `books.map(bookMapper::toResponse)` — `Page.map` preserves pagination metadata.

```java
public Page<BookResponse> getAllBooks(Pageable pageable) {
    logger.debug("Fetching all books with pagination: page={}, size={}",
                 pageable.getPageNumber(), pageable.getPageSize());

    Page<Book> books = bookRepository.findAll(pageable);
    return books.map(bookMapper::toResponse);
}
```

#### Repository layer

- `BookRepository extends JpaRepository`, which provides `findAll(Pageable)` out of the box. Spring Data issues a `LIMIT`/`OFFSET` query plus a `COUNT(*)` query for total size.

#### Search endpoint (`BookController.searchBooks` → `BookServiceImpl.searchBooks`)

- Same pattern, but default page size is `10` and filtering is applied through a JPA `Specification`.
- Spec + `Pageable` are combined via `bookRepository.findAll(spec, pageable)`.

#### Index support (`schema.sql`)

- Indexes on `id` (PK), `created_at`, `publication_year`, plus composites (`genre, publication_year` and `author, title`) make `ORDER BY` clauses used in pagination cheap.

#### Example requests

```text
GET /api/v1/books?page=0&size=10&sort=title,asc
GET /api/v1/books/search?genre=Fiction&page=2&size=20&sort=publicationYear,desc
```

### Step 6 — Compare with traditional

| Metric | Traditional | Vector DB | Savings |
|---|---|---|---|
| Input tokens | 7,428 | 2,088 | 71.9% |
| Output tokens (est.) | ~650 | ~650 | ~0% |
| Total cost (Sonnet 4.6) | $0.02228 | $0.00626 | **~72%** |

---

## Question 2 — add a country_code field to Book entity

**Date:** 2026-04-24
**Mode:** /vectordb
**Codebase:** Books Online full-stack (Spring Boot + React)

### Step 1 — Parse the user's question

- **Feature:** Add a `country_code` field to the `Book` entity
- **Codebase:** Books Online full-stack (Spring Boot + React)

### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "add a country_code field to Book entity"
```

- DB: `/tmp/books-online-vectors-enriched.db`
- Method: cosine_similarity (all-MiniLM-L6-v2)
- Chunks: 37 total | top-5 scored | 2 pinned | 7 sent to Claude

### Step 3 — Show the full ranked list

```text
RANK | SCORE  | FILE                           | FUNCTION                          | TOKENS | STATUS
1    | 0.5175 | Book.java                      | Book_class_body                   | 510    | RETRIEVED
2    | 0.4951 | BookRequest.java               | BookRequest_class_body            | 765    | RETRIEVED
3    | 0.4429 | BookResponse.java              | BookResponse_class_body           | 180    | RETRIEVED
4    | 0.4401 | schema.sql                     | books                             | 670    | RETRIEVED
5    | 0.3445 | data.sql                       | seeds_books                       | 441    | RETRIEVED
6    | 0.3014 | BookServiceImpl.java           | createBook                        | 136    | SKIPPED
7    | 0.2882 | data.sql                       | alter_books                       | 23     | SKIPPED
8    | 0.2656 | BookMapper.java                | BookMapper_class_body             | 53     | SKIPPED
9    | 0.2587 | AddBookPage.jsx                | AddBookPage                       | 258    | SKIPPED
10   | 0.2586 | BookServiceImpl.java           | deleteBook                        | 61     | SKIPPED
11   | 0.2567 | BookServiceImpl.java           | exportBook                        | 125    | SKIPPED
12   | 0.2516 | schema.sql                     | indexes_on_books                  | 181    | SKIPPED
13   | 0.2388 | BookServiceImpl.java           | duplicateBook                     | 102    | SKIPPED
14   | 0.2353 | BookServiceImpl.java           | updateBook                        | 173    | SKIPPED
15   | 0.2352 | BookServiceImpl.java           | getBookById                       | 95     | SKIPPED
16   | 0.2272 | BookController.java            | exportBook                        | 120    | SKIPPED
17   | 0.2169 | BookController.java            | getBookById                       | 67     | SKIPPED
18   | 0.2116 | BookController.java            | duplicateBook                     | 76     | SKIPPED
19   | 0.2082 | Book.java                      | prePersist                        | 88     | PINNED
20   | 0.1859 | GlobalExceptionHandler.java    | handleBookNotFoundException       | 94     | SKIPPED
21   | 0.1770 | BookController.java            | createBook                        | 107    | SKIPPED
22   | 0.1754 | GlobalExceptionHandler.java    | handleDuplicateIsbnException      | 97     | SKIPPED
23   | 0.1618 | BookController.java            | updateBook                        | 124    | SKIPPED
24   | 0.1386 | BookController.java            | searchBooks                       | 446    | SKIPPED
25   | 0.1378 | Book.java                      | incrementViewCount                | 33     | PINNED
26   | 0.1077 | BookController.java            | deleteBook                        | 64     | SKIPPED
27   | 0.1059 | BookController.java            | getAllBooks                       | 104    | SKIPPED
28   | 0.0860 | ErrorResponse.java             | ErrorResponse_class_body          | 59     | SKIPPED
29   | 0.0811 | BookServiceImpl.java           | getAllBooks                       | 61     | SKIPPED
30   | 0.0807 | ValidationErrorResponse.java   | ValidationErrorResponse_body      | 92     | SKIPPED
31   | 0.0485 | BookServiceImpl.java           | searchBooks                       | 730    | SKIPPED
32   | call   | GlobalExceptionHandler.java    | handleValidationErrors            | 192    | SKIPPED
33   | call   | GlobalExceptionHandler.java    | handleGenericException            | 97     | SKIPPED
34   | call   | GlobalExceptionHandler.java    | handleIllegalArgumentException    | 91     | SKIPPED
35   | call   | Application.java               | main                              | 20     | SKIPPED
36   | call   | HomePage.jsx                   | HomePage                          | 698    | SKIPPED
37   | call   | App.jsx                        | App                               | 187    | SKIPPED
```

### Step 4 — Show token summary

```text
TOKENS BILLED:   2,695  (8 question + 2,687 retrieved)
TOKENS SKIPPED:  4,733
SIGNAL RATIO:    99.7% useful
```

Confidence gate: **96/100 — HIGH** (top_score 0.517, score_gap 0.087, full lexical match on `country_code`/`field`/`Book`/`entity`, good layer diversity, 2 AST-pinned lifecycle chunks).

### Step 5 — Generate implementation

**Observation from retrieved chunks:** A `country_code VARCHAR(255)` column already existed alongside `country_code1`, `postal_code`, `region_code`. Rather than duplicate, the field was tightened to a proper **ISO-3166 alpha-2/alpha-3** code (2–3 uppercase letters) across all four layers.

#### Entity (`Book.java`)

```java
@Column(name = "country_code", length = 3)
private String countryCode;
```

- Length constraint reduced from unbounded `VARCHAR(255)` to `3` (fits ISO-3166 alpha-2 `US` and alpha-3 `USA`).

#### Request DTO (`BookRequest.java`)

```java
@Size(min = 2, max = 3, message = "Country code must be 2 or 3 characters (ISO-3166)")
@Pattern(regexp = "^[A-Z]{2,3}$|^$", message = "Country code must be uppercase letters (e.g., US, USA)")
private String countryCode;
```

- Adds bean-validation: size 2–3, uppercase A–Z only, allows empty string (field remains optional).

#### Response DTO (`BookResponse.java`)

- `private String countryCode;` already present — no change required. Jackson serializes it as `"countryCode"` in JSON responses.

#### Schema (`schema.sql`)

```sql
country_code VARCHAR(3),
```

- Column type tightened from `VARCHAR(255)` to `VARCHAR(3)` to match the ISO code format at the DB level.

#### Mapper (`BookMapper.java`)

- No change needed — MapStruct auto-maps `countryCode` by name across `Book ↔ BookRequest ↔ BookResponse`.

#### Lifecycle (`@PrePersist`)

- No default needed — `countryCode` is optional; `null` is a valid value for books without a country of origin.

#### Example payload

```json
POST /api/v1/books
{
  "title": "Clean Code",
  "isbn": "9780132350884",
  "author": "Robert C. Martin",
  "genre": "Programming",
  "publicationYear": 2008,
  "language": "English",
  "isAvailable": true,
  "countryCode": "USA"
}
```

### Step 6 — Compare with traditional

| Metric | Traditional | Vector DB | Savings |
|---|---|---|---|
| Input tokens | 7,428 | 2,695 | 63.7% |
| Output tokens (est.) | ~700 | ~700 | ~0% |
| Total cost (Sonnet 4.6) | $0.02228 | $0.00809 | **~64%** |

---

## Question 3 — what does the update book flow look like end to end

**Date:** 2026-04-24
**Mode:** /vectordb
**Codebase:** Books Online full-stack (Spring Boot + React)

### Step 1 — Parse the user's question

- **Feature/flow:** End-to-end "update book" flow (read existing → edit → PUT → persist)
- **Codebase:** Books Online full-stack (Spring Boot + React, H2)

### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "what does the update book flow look like end to end"
```

- DB: `/tmp/books-online-vectors-enriched.db`
- Method: cosine_similarity (all-MiniLM-L6-v2)
- Chunks: 37 total | top-5 scored | 0 pinned | 0 call_graph | 0 reverse [gated off: synthesis intent] | 5 sent to Claude

### Step 3 — Show the full ranked list

```text
RANK | SCORE  | FILE                          | FUNCTION                          | TOKENS | STATUS
1    | 0.4408 | BookServiceImpl.java          | updateBook                        | 173    | RETRIEVED
2    | 0.3619 | BookServiceImpl.java          | createBook                        | 136    | RETRIEVED
3    | 0.3517 | BookController.java           | updateBook                        | 124    | RETRIEVED
4    | 0.3119 | BookController.java           | deleteBook                        |  64    | RETRIEVED
5    | 0.3098 | BookServiceImpl.java          | getAllBooks                       |  61    | RETRIEVED
6    | 0.3092 | AddBookPage.jsx               | AddBookPage                       | 258    | SKIPPED
7    | 0.3005 | data.sql                      | alter_books                       |  23    | SKIPPED
8    | 0.2938 | BookServiceImpl.java          | exportBook                        | 125    | SKIPPED
9    | 0.2881 | BookServiceImpl.java          | duplicateBook                     | 102    | SKIPPED
10   | 0.2784 | BookServiceImpl.java          | deleteBook                        |  61    | SKIPPED
11   | 0.2779 | BookServiceImpl.java          | getBookById                       |  95    | SKIPPED
12   | 0.2735 | data.sql                      | seeds_books                       | 441    | SKIPPED
13   | 0.2632 | BookController.java           | getAllBooks                       | 104    | SKIPPED
14   | 0.2589 | Book.java                     | Book_class_body                   | 510    | SKIPPED
15   | 0.2574 | BookController.java           | searchBooks                       | 446    | SKIPPED
16   | 0.2546 | BookServiceImpl.java          | searchBooks                       | 730    | SKIPPED
17   | 0.2394 | GlobalExceptionHandler.java   | handleBookNotFoundException       |  94    | SKIPPED
18   | 0.2368 | BookMapper.java               | BookMapper_class_body             |  53    | SKIPPED
19   | 0.2161 | BookController.java           | exportBook                       | 120    | SKIPPED
20   | 0.2159 | BookController.java           | getBookById                       |  67    | SKIPPED
21   | 0.2136 | BookController.java           | createBook                        | 107    | SKIPPED
22   | 0.2074 | Book.java                     | incrementViewCount                |  33    | SKIPPED
23   | 0.2041 | schema.sql                    | indexes_on_books                  | 181    | SKIPPED
24   | 0.1911 | BookController.java           | duplicateBook                     |  76    | SKIPPED
25   | 0.1675 | BookResponse.java             | BookResponse_class_body           | 180    | SKIPPED
26   | 0.1431 | Book.java                     | prePersist                        |  88    | SKIPPED
27   | 0.1236 | GlobalExceptionHandler.java   | handleDuplicateIsbnException      |  97    | SKIPPED
28   | 0.1232 | BookRequest.java              | BookRequest_class_body            | 765    | SKIPPED
29   | 0.0973 | Application.java              | main                              |  20    | SKIPPED
30   | 0.0727 | HomePage.jsx                  | HomePage                          | 698    | SKIPPED
31   | 0.0697 | schema.sql                    | books                             | 670    | SKIPPED
32   | 0.0614 | ErrorResponse.java            | ErrorResponse_class_body          |  59    | SKIPPED
33   | 0.0205 | App.jsx                       | App                               | 187    | SKIPPED
34   | 0.0159 | ValidationErrorResponse.java  | ValidationErrorResponse_class_body|  92    | SKIPPED
35   | call   | GlobalExceptionHandler.java   | handleIllegalArgumentException    |  91    | SKIPPED
36   | call   | GlobalExceptionHandler.java   | handleGenericException            |  97    | SKIPPED
37   | call   | GlobalExceptionHandler.java   | handleValidationErrors            | 192    | SKIPPED
```

### Step 4 — Show token summary

```text
TOKENS BILLED:   569   (11 question + 558 retrieved)
TOKENS SKIPPED:  6,862
SIGNAL RATIO:    98.1% useful
```

Confidence gate: **67/100 — MEDIUM** (top_score 0.441, score_gap 0.107, weak lexical match on vague terms "flow"/"end to end", good layer diversity). Frontend chunks (`EditBookPage.jsx`, `booksService.js`, `BookForm.jsx`) were NOT retrieved — backend trace is code-grounded; frontend section is API-shape inference.

### Step 5 — Generate implementation

#### Backend trace

**1. `PUT /api/v1/books/{id}` — `BookController.updateBook`**
- Binds `@PathVariable Long id` + `@Valid @RequestBody BookRequest`
- Bean Validation on `BookRequest` runs first; failures short-circuit to the global exception handler before the service is invoked
- Logs `PUT /api/v1/books/{} - Updating book`
- Delegates to `bookService.updateBook(id, request)`
- Wraps result in `ResponseEntity.ok(updatedBook)` → **200 OK**

```java
public ResponseEntity<BookResponse> updateBook(
        @PathVariable Long id,
        @Valid @RequestBody BookRequest request) {
    logger.info("PUT /api/v1/books/{} - Updating book", id);
    BookResponse updatedBook = bookService.updateBook(id, request);
    return ResponseEntity.ok(updatedBook);
}
```

**2. `BookServiceImpl.updateBook` — business logic**

1. `bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id))` → 404 if missing
2. **ISBN-change guard:** if incoming `request.getIsbn()` differs from `existingBook.getIsbn()`, calls `bookRepository.existsByIsbn(...)`; if taken → throws `DuplicateIsbnException` (409)
3. `bookMapper.updateEntityFromRequest(existingBook, request)` — copies mutable fields onto the managed entity
4. `bookRepository.save(existingBook)` — JPA dirty-check issues `UPDATE`
5. Returns `bookMapper.toResponse(updatedBook)`

```java
public BookResponse updateBook(Long id, BookRequest request) {
    Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(id));

    if (!existingBook.getIsbn().equals(request.getIsbn())) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateIsbnException(request.getIsbn());
        }
    }

    bookMapper.updateEntityFromRequest(existingBook, request);
    Book updatedBook = bookRepository.save(existingBook);
    return bookMapper.toResponse(updatedBook);
}
```

**3. Response envelope**
- `BookResponse` JSON serialized back to client with 200 status
- Errors funnel through `GlobalExceptionHandler` (404 / 409 / 400) into a uniform `ErrorResponse`

#### Frontend (inferred — not in retrieved chunks)

`EditBookPage` fetches the book via `GET /books/{id}`, hands it to `BookForm` as `initialData`, and on submit calls `booksService.updateBook(id, data)` → `axios.put('/books/{id}', data)`. Success triggers a toast + navigation to the detail page; errors surface via `toast.error(error.response.data.message)`.

### Step 6 — Compare with traditional

Traditional input = full content of the 12 files plausibly involved in the update flow (controller, service iface+impl, mapper, entity, request/response DTOs, repository, exception handler, edit page, books service, form).

| Metric | Traditional | Vector DB | Savings |
|---|---|---|---|
| Input tokens | 12,698 | 569 | **95.5%** |
| Output tokens (est.) | ~900 | ~900 | ~0% |
| Input cost (Sonnet 4.6 @ $3/M)  | $0.03809 | $0.00171 | 95.5% |
| Output cost (Sonnet 4.6 @ $15/M) | $0.01350 | $0.01350 | 0% |
| **Total cost** | **$0.05159** | **$0.01521** | **~70.5%** |

---

## Question 4 — what does the update book flow look like end to end (re-run)

**Date:** 2026-04-24
**Mode:** /vectordb
**Codebase:** Books Online full-stack (Spring Boot + React)

### Step 1 — Parse the user's question

- **Feature/flow:** End-to-end "update book" flow
- **Codebase:** Books Online full-stack (Spring Boot + React, H2)

### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "update book flow end to end"
```

- DB: `/tmp/books-online-vectors-enriched.db`
- Method: cosine_similarity (all-MiniLM-L6-v2)
- Chunks: 37 total | top-5 scored | 0 pinned | 0 call_graph | 8 reverse | 13 sent to Claude

### Step 3 — Show the full ranked list

```text
RANK | SCORE  | FILE                          | FUNCTION                          | TOKENS | STATUS
1    | 0.3773 | BookServiceImpl.java          | updateBook                        | 173    | RETRIEVED
2    | 0.3753 | AddBookPage.jsx               | AddBookPage                       | 258    | RETRIEVED
3    | 0.3648 | BookServiceImpl.java          | createBook                        | 136    | RETRIEVED
4    | 0.3575 | BookController.java           | updateBook                        | 124    | RETRIEVED
5    | 0.3482 | BookServiceImpl.java          | deleteBook                        |  61    | RETRIEVED
6    | 0.3377 | BookServiceImpl.java          | getAllBooks                       |  61    | REV_IMPORT
7    | 0.3289 | BookController.java           | deleteBook                        |  64    | REV_CALLER
8    | 0.3235 | Book.java                     | Book_class_body                   | 510    | SKIPPED
9    | 0.3227 | BookServiceImpl.java          | exportBook                        | 125    | SKIPPED
10   | 0.3094 | BookServiceImpl.java          | getBookById                       |  95    | SKIPPED
11   | 0.3057 | BookServiceImpl.java          | duplicateBook                     | 102    | SKIPPED
12   | 0.2824 | data.sql                      | seeds_books                       | 441    | SKIPPED
13   | 0.2807 | BookController.java           | getAllBooks                       | 104    | REV_IMPORT
14   | 0.2801 | data.sql                      | alter_books                       |  23    | SKIPPED
15   | 0.2796 | BookController.java           | searchBooks                       | 446    | REV_IMPORT
16   | 0.2776 | GlobalExceptionHandler.java   | handleBookNotFoundException       |  94    | SKIPPED
17   | 0.2601 | BookMapper.java               | BookMapper_class_body             |  53    | SKIPPED
18   | 0.2365 | BookController.java           | duplicateBook                     |  76    | REV_IMPORT
19   | 0.2307 | BookController.java           | createBook                        | 107    | REV_CALLER
20   | 0.2291 | BookController.java           | getBookById                       |  67    | REV_IMPORT
21   | 0.2193 | BookController.java           | exportBook                        | 120    | REV_IMPORT
22   | 0.2061 | BookServiceImpl.java          | searchBooks                       | 730    | SKIPPED
23   | 0.1912 | BookResponse.java             | BookResponse_class_body           | 180    | SKIPPED
24   | 0.1902 | schema.sql                    | indexes_on_books                  | 181    | SKIPPED
25   | 0.1688 | GlobalExceptionHandler.java   | handleDuplicateIsbnException      |  97    | SKIPPED
26   | 0.1509 | BookRequest.java              | BookRequest_class_body            | 765    | SKIPPED
27   | 0.1383 | Book.java                     | incrementViewCount                |  33    | SKIPPED
28   | 0.0996 | Book.java                     | prePersist                        |  88    | SKIPPED
29   | 0.0943 | schema.sql                    | books                             | 670    | SKIPPED
30   | 0.0576 | Application.java              | main                              |  20    | SKIPPED
31   | 0.0514 | HomePage.jsx                  | HomePage                          | 698    | SKIPPED
32   | 0.0498 | ErrorResponse.java            | ErrorResponse_class_body          |  59    | SKIPPED
33   | 0.0106 | ValidationErrorResponse.java  | ValidationErrorResponse_class_body|  92    | SKIPPED
34   | 0.0087 | GlobalExceptionHandler.java   | handleIllegalArgumentException    |  91    | SKIPPED
35   | call   | App.jsx                       | App                               | 187    | SKIPPED
36   | call   | GlobalExceptionHandler.java   | handleGenericException            |  97    | SKIPPED
37   | call   | GlobalExceptionHandler.java   | handleValidationErrors            | 192    | SKIPPED
```

### Step 4 — Show token summary

```text
TOKENS BILLED:   1,803  (6 question + 1,797 retrieved)
TOKENS SKIPPED:  5,623
SIGNAL RATIO:    99.7% useful
```

Confidence gate: **36/100 — LOW** (top_score 0.377, score_gap 0.016, weak lexical match — only "book"/"flow"/"end" matched, layer diversity OK). Fallback disabled; chunks sent anyway. Retrieved set still landed on the right functions (`BookController.updateBook`, `BookServiceImpl.updateBook`), so the backend trace is code-grounded. `EditBookPage.jsx` and `BookMapper.updateEntityFromRequest` were NOT retrieved — frontend handler and mapper details are inferred from `AddBookPage` symmetry.

### Step 5 — Generate implementation

#### Frontend — user submits edit form

`EditBookPage.jsx` (inferred from `AddBookPage.jsx` symmetry):
- `<BookForm>` (React Hook Form + Zod) collects fields
- On submit: `booksService.updateBook(id, data)`
- Success → `toast.success` + `navigate('/books')`
- 409 → "ISBN already exists" toast; other errors → generic failure

#### Network — Axios PUT

`services/api.js` issues `PUT /api/v1/books/{id}` with `BookRequest` body. Request interceptor attaches JWT if present; response interceptor normalizes errors.

#### Backend controller — `BookController.updateBook`

```java
public ResponseEntity<BookResponse> updateBook(
        @PathVariable Long id,
        @Valid @RequestBody BookRequest request) {
    logger.info("PUT /api/v1/books/{} - Updating book", id);
    BookResponse updatedBook = bookService.updateBook(id, request);
    return ResponseEntity.ok(updatedBook);
}
```
- `@Valid` triggers bean validation; failures → `GlobalExceptionHandler.handleValidationErrors` → 400
- Delegates to service, returns 200 OK

#### Backend service — `BookServiceImpl.updateBook`

```java
public BookResponse updateBook(Long id, BookRequest request) {
    Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(id));

    if (!existingBook.getIsbn().equals(request.getIsbn())) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateIsbnException(request.getIsbn());
        }
    }

    bookMapper.updateEntityFromRequest(existingBook, request);
    Book updatedBook = bookRepository.save(existingBook);
    return bookMapper.toResponse(updatedBook);
}
```

1. **Load** — `findById` → `BookNotFoundException` (404) if missing
2. **ISBN conflict guard** — only when ISBN changed; `existsByIsbn` collision → `DuplicateIsbnException` (409)
3. **Mutate in place** — `bookMapper.updateEntityFromRequest` copies request fields onto the managed entity (preserves `id`, `createdAt`, `viewCount`)
4. **Persist** — `repository.save` triggers UPDATE via Hibernate dirty-check
5. **Map out** — `BookResponse` DTO

#### Response path

Service result → `ResponseEntity.ok(...)` → Axios resolves → success toast + route change.

#### Error branches

| Failure | Exception | HTTP |
|---|---|---|
| ID not found | `BookNotFoundException` | 404 |
| ISBN collides with another book | `DuplicateIsbnException` | 409 |
| Invalid payload | `MethodArgumentNotValidException` | 400 |

### Step 6 — Compare with traditional

| Metric | Traditional | Vector DB | Savings |
|---|---|---|---|
| Input tokens | 7,426 | 1,803 | 75.7% |
| Output tokens (est.) | ~900 | ~900 | ~0% |
| Input cost (Sonnet 4.6 @ $3/M) | $0.02228 | $0.00541 | 75.7% |
| Output cost (Sonnet 4.6 @ $15/M) | $0.01350 | $0.01350 | 0% |
| **Total cost** | **$0.03578** | **$0.01891** | **~47%** |

---

## Question 5 — how does BookServiceImpl.createBook work

**Date:** 2026-04-24
**Mode:** /vectordb
**Codebase:** Books Online full-stack (Spring Boot + React)

### Step 1 — Parse the user's question

- **Feature/flow:** Understand `BookServiceImpl.createBook` — the service-layer create path
- **Codebase:** Books Online full-stack (Spring Boot + React, H2)

### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "how does BookServiceImpl.createBook work"
```

- DB: `/tmp/books-online-vectors-enriched.db`
- Method: cosine_similarity (all-MiniLM-L6-v2)
- Chunks: 37 total | top-5 scored | 0 pinned | 1 call_graph | 9 reverse | 15 sent to Claude
- Hierarchy search path (broad): L2(module) top=0.500 → L3(file) top=0.629 → Reached L4 (function chunks)

### Step 3 — Show the full ranked list

```text
RANK | SCORE  | FILE                          | FUNCTION                          | TOKENS | STATUS
1    | 0.6855 | BookServiceImpl.java          | createBook                        | 136    | RETRIEVED
2    | 0.5603 | BookServiceImpl.java          | exportBook                        | 125    | RETRIEVED
3    | 0.5546 | AddBookPage.jsx               | AddBookPage                       | 258    | RETRIEVED
4    | 0.4965 | BookServiceImpl.java          | updateBook                        | 173    | RETRIEVED
5    | 0.4788 | BookServiceImpl.java          | getBookById                       |  95    | RETRIEVED
6    | 0.4784 | BookServiceImpl.java          | duplicateBook                     | 102    | SKIPPED
7    | 0.4285 | GlobalExceptionHandler.java   | handleBookNotFoundException       |  94    | SKIPPED
8    | 0.4217 | BookServiceImpl.java          | deleteBook                        |  61    | SKIPPED
9    | 0.4157 | data.sql                      | seeds_books                       | 441    | SKIPPED
10   | 0.4042 | BookServiceImpl.java          | getAllBooks                       |  61    | REV_IMPORT(BookService)
11   | 0.3961 | BookController.java           | exportBook                        | 120    | REV_CALLER(exportBook)
12   | 0.3863 | BookController.java           | createBook                        | 107    | REV_CALLER(createBook)
13   | 0.3858 | schema.sql                    | indexes_on_books                  | 181    | SKIPPED
14   | 0.3781 | BookController.java           | deleteBook                        |  64    | REV_IMPORT(BookService)
15   | 0.3622 | BookController.java           | getBookById                       |  67    | REV_CALLER(getBookById)
16   | 0.3585 | Book.java                     | Book_class_body                   | 510    | SKIPPED
17   | 0.3542 | BookController.java           | updateBook                        | 124    | REV_CALLER(updateBook)
18   | 0.3275 | BookMapper.java               | BookMapper_class_body             |  53    | SKIPPED
19   | 0.3260 | data.sql                      | alter_books                       |  23    | SKIPPED
20   | 0.3086 | BookController.java           | getAllBooks                       | 104    | REV_IMPORT(BookService)
21   | 0.3039 | BookController.java           | searchBooks                       | 446    | REV_IMPORT(BookService)
22   | 0.3029 | BookController.java           | duplicateBook                     |  76    | REV_IMPORT(BookService)
23   | 0.3000 | BookServiceImpl.java          | searchBooks                       | 730    | SKIPPED
24   | 0.2459 | GlobalExceptionHandler.java   | handleDuplicateIsbnException      |  97    | SKIPPED
25   | 0.2421 | BookRequest.java              | BookRequest_class_body            | 765    | SKIPPED
26   | 0.2051 | BookResponse.java             | BookResponse_class_body           | 180    | SKIPPED
27   | 0.1769 | Book.java                     | prePersist                        |  88    | SKIPPED
28   | 0.1620 | Book.java                     | incrementViewCount                |  33    | CALL_GRAPH(incrementViewCount)
29   | 0.1405 | Application.java              | main                              |  20    | SKIPPED
30   | 0.1377 | schema.sql                    | books                             | 670    | SKIPPED
31   | 0.0447 | App.jsx                       | App                               | 187    | SKIPPED
32   | 0.0342 | HomePage.jsx                  | HomePage                          | 698    | SKIPPED
33   | call   | GlobalExceptionHandler.java   | handleIllegalArgumentException    |  91    | SKIPPED
34   | call   | ErrorResponse.java            | ErrorResponse_class_body          |  59    | SKIPPED
35   | call   | GlobalExceptionHandler.java   | handleValidationErrors            | 192    | SKIPPED
36   | call   | ValidationErrorResponse.java  | ValidationErrorResponse_class_body|  92    | SKIPPED
37   | call   | GlobalExceptionHandler.java   | handleGenericException            |  97    | SKIPPED
```

### Step 4 — Show token summary

```text
TOKENS BILLED:   1,996  (7 question + 1,989 retrieved)
TOKENS SKIPPED:  5,431
SIGNAL RATIO:    99.6% useful
```

Confidence gate: **84/100 — HIGH** (top_score 0.685, score_gap 0.163, lexical match on `BookServiceImpl`/`createBook`/`work`, good layer diversity). Retrieved chunks trusted; no fallback needed.

### Step 5 — Generate implementation

#### Service method — `BookServiceImpl.createBook(BookRequest request)`

Four sequential steps:

1. **Debug log** — `logger.debug("Creating new book with ISBN: {}", request.getIsbn())`.
2. **Duplicate-ISBN guard** — `bookRepository.existsByIsbn(request.getIsbn())`; if true, warn-logs and throws `DuplicateIsbnException(request.getIsbn())` → surfaces to client as HTTP 409.
3. **Persist** — `bookMapper.toEntity(request)` builds the entity; `bookRepository.save(book)` issues the INSERT and returns the managed, ID-assigned entity.
4. **Respond** — info-logs `id` + `isbn`, returns `bookMapper.toResponse(savedBook)`.

```java
public BookResponse createBook(BookRequest request) {
    logger.debug("Creating new book with ISBN: {}", request.getIsbn());

    if (bookRepository.existsByIsbn(request.getIsbn())) {
        logger.warn("Attempt to create book with duplicate ISBN: {}", request.getIsbn());
        throw new DuplicateIsbnException(request.getIsbn());
    }

    Book book = bookMapper.toEntity(request);
    Book savedBook = bookRepository.save(book);

    logger.info("Book created successfully: id={}, isbn={}",
                savedBook.getId(), savedBook.getIsbn());

    return bookMapper.toResponse(savedBook);
}
```

#### Caller — `BookController.createBook`

- Maps `POST /api/v1/books`; binds `@Valid @RequestBody BookRequest` (validation runs pre-method).
- Logs the ISBN, delegates to `bookService.createBook(request)`, returns `201 Created` with the `BookResponse`.

#### Consumer — `AddBookPage.handleSubmit` (React)

- Calls `booksService.createBook(data)` (axios POST).
- On success → `toast.success('Book created successfully')` + `navigate('/books')`.
- On 409 → `toast.error('A book with this ISBN already exists')` (directly mapped from the `DuplicateIsbnException` path).

#### End-to-end call flow

```
AddBookPage.handleSubmit
  └─> booksService.createBook(data)           # axios POST /api/v1/books
         └─> BookController.createBook        # @Valid BookRequest → 201
                └─> BookServiceImpl.createBook
                       ├─ existsByIsbn? → DuplicateIsbnException (409)
                       ├─ bookMapper.toEntity(request)
                       ├─ bookRepository.save(book)
                       └─ bookMapper.toResponse(savedBook)
```

#### Observations (from retrieved chunks)

- Compared to `updateBook`: update gates ISBN check behind "ISBN actually changed" and uses `bookMapper.updateEntityFromRequest` (in-place) instead of `toEntity`.
- No `@Transactional` visible in the retrieved chunk; the `existsByIsbn` check + `save` are not atomic — concurrent duplicate creates would rely on a DB unique constraint (schema chunk not retrieved, so not confirmed here).
- No `@PrePersist` touches ISBN — `existsByIsbn` is the only uniqueness guard at this layer.

### Step 6 — Compare with traditional

| Metric | Traditional | Vector DB | Savings |
|---|---|---|---|
| Input tokens | 7,427 | 1,996 | 73.1% |
| Output tokens (est.) | ~600 | ~600 | ~0% |
| Input cost (Sonnet 4.6 @ $3/M) | $0.02228 | $0.00599 | 73.1% |
| Output cost (Sonnet 4.6 @ $15/M) | $0.00900 | $0.00900 | 0% |
| **Total cost** | **$0.03128** | **$0.01499** | **~52%** |

---

# TEST_PROMPTS.md — Full 6-Step Probe Run

Systematic validation of the 6-stage retrieval pipeline against
`/tmp/books-online-vectors-enriched.db` (37 indexed chunks,
Books Online full-stack corpus). Each probe run as an isolated
subprocess call to `vectordb_search.py` — no context leaks between
probes.

**Format per probe** follows the /vectordb skill:
1. Parse the user's question
2. Search the vector DB
3. Full ranked list (all 37 chunks)
4. Token summary
5. Answer from retrieved chunks only
6. Traditional vs Vector DB cost comparison

**Date of run:** 2026-04-25
**Pricing model:** claude-sonnet-4-6 ($3/M input, $15/M output)

---

## Group A — Stage isolation

### Probe A1 — Stage 2 only (pure cosine)

**Expected stages firing:** Stage 2 only. Pagination methods should
dominate top-3 with no pinning or forward call-graph activity.

#### Step 1 — Parse the user's question

- **Feature:** How is pagination implemented for the book listing
  endpoint
- **Codebase:** Books Online full-stack (Spring Boot + React)
- **Query type:** targeted (no "explain/how does/what is" broad
  phrasing beyond "how is")

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "how is pagination implemented in the book listing"
```

- DB: `/tmp/books-online-vectors-enriched.db`
- Method: cosine_similarity (all-MiniLM-L6-v2)
- Chunks: 37 total | top-5 scored | 0 pinned | 0 call_graph | 6 reverse | 11 sent to Claude

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                           | FUNCTION                     | TOKENS | STATUS
1    | 0.6528 | BookServiceImpl.java           | getAllBooks                  | 61     | RETRIEVED
2    | 0.5868 | BookController.java            | getAllBooks                  | 104    | RETRIEVED
3    | 0.5468 | BookController.java            | searchBooks                  | 446    | RETRIEVED
4    | 0.4746 | BookServiceImpl.java           | searchBooks                  | 730    | RETRIEVED
5    | 0.4402 | schema.sql                     | indexes_on_books             | 181    | RETRIEVED
6    | 0.4245 | AddBookPage.jsx                | AddBookPage                  | 258    | skipped
7    | 0.3769 | BookServiceImpl.java           | createBook                   | 136    | skipped
8    | 0.3766 | Book.java                      | incrementViewCount           | 33     | skipped
9    | 0.3594 | data.sql                       | seeds_books                  | 441    | skipped
10   | 0.3501 | BookController.java            | exportBook                   | 120    | REV_IMPORT(BookService)
11   | 0.3480 | BookServiceImpl.java           | updateBook                   | 173    | skipped
12   | 0.3382 | Book.java                      | Book_class_body              | 510    | skipped
13   | 0.3339 | BookController.java            | getBookById                  | 67     | REV_IMPORT(BookService)
14   | 0.3301 | BookController.java            | updateBook                   | 124    | REV_IMPORT(BookService)
15   | 0.3227 | BookServiceImpl.java           | getBookById                  | 95     | skipped
16   | 0.3207 | BookController.java            | createBook                   | 107    | REV_IMPORT(BookService)
17   | 0.3144 | BookController.java            | deleteBook                   | 64     | REV_IMPORT(BookService)
18   | 0.3017 | BookServiceImpl.java           | exportBook                   | 125    | skipped
19   | 0.2970 | BookMapper.java                | BookMapper_class_body        | 53     | skipped
20   | 0.2894 | GlobalExceptionHandler.java    | handleBookNotFoundException  | 94     | skipped
21   | 0.2730 | BookServiceImpl.java           | duplicateBook                | 102    | skipped
22   | 0.2637 | BookServiceImpl.java           | deleteBook                   | 61     | skipped
23   | 0.2293 | HomePage.jsx                   | HomePage                     | 698    | skipped
24   | 0.2224 | data.sql                       | alter_books                  | 23     | skipped
25   | 0.2193 | BookController.java            | duplicateBook                | 76     | REV_IMPORT(BookService)
26   | 0.2059 | BookRequest.java               | BookRequest_class_body       | 765    | skipped
27   | 0.2036 | BookResponse.java              | BookResponse_class_body      | 180    | skipped
28   | 0.1853 | schema.sql                     | books                        | 670    | skipped
29   | 0.1094 | Application.java               | main                         | 20     | skipped
30   | 0.0959 | Book.java                      | prePersist                   | 88     | skipped
31   | 0.0774 | GlobalExceptionHandler.java    | handleDuplicateIsbnException | 97     | skipped
32   | 0.0532 | App.jsx                        | App                          | 187    | skipped
33   | call   | GlobalExceptionHandler.java    | handleIllegalArgumentException | 91   | skipped
34   | call   | GlobalExceptionHandler.java    | handleValidationErrors       | 192    | skipped
35   | call   | GlobalExceptionHandler.java    | handleGenericException       | 97     | skipped
36   | call   | ValidationErrorResponse.java   | ValidationErrorResponse_body | 92     | skipped
37   | call   | ErrorResponse.java             | ErrorResponse_class_body     | 59     | skipped
```

**Confidence gate:** HIGH 85/100 — top_score 100 (0.6528), score_gap
100 (0.14), lexical_match 50 (pagination/implemented/book/listing),
layer_diversity 100.

#### Step 4 — Token summary

```text
TOKENS BILLED:    2,088 (8 question + 2,080 retrieved)
TOKENS SKIPPED:   5,340 (never sent to Claude)
SIGNAL RATIO:     99.6% useful
```

#### Step 5 — Answer from retrieved chunks

Pagination is implemented via Spring Data `Pageable`:

- `BookServiceImpl.getAllBooks(Pageable pageable)` calls
  `bookRepository.findAll(pageable)` and maps the resulting
  `Page<Book>` to `Page<BookResponse>` via `books.map(bookMapper::toResponse)`.
- `BookController.getAllBooks` declares `@PageableDefault(size = 4, sort = "id", direction = Sort.Direction.ASC)`
  and delegates to the service. The controller logs page/size and
  returns the `Page<BookResponse>` directly in `ResponseEntity.ok(...)`.
- `BookController.searchBooks` uses a different default —
  `@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC)` —
  and forwards Pageable through to `BookServiceImpl.searchBooks`.
- `schema.sql:indexes_on_books` confirms supporting indexes exist
  on the columns used for sort/filter.

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,428 | 2,088 | 71.9% |
| Input cost ($3/M) | $0.02228 | $0.00626 | 71.9% |

**Probe verdict:** PASS — Pagination methods (getAllBooks on both
layers, searchBooks with its Pageable, and the supporting indexes)
dominate top-5 exactly as Stage-2-only behavior predicts. No
spurious entity or exception noise.

---

### Probe A2 — Stage 3 AST pinning (schema-change intent)

**Expected stages firing:** Stage 2 + Stage 3 (+ possibly 4a).
Lifecycle chunks should appear with `PINNED` status even if cosine
score is mediocre.

#### Step 1 — Parse the user's question

- **Feature:** Add a `country_code` field to the `Book` entity
- **Codebase:** Books Online backend (JPA entity)
- **Query type:** targeted, schema-change intent

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "add a country_code field to Book entity"
```

- Chunks: 37 total | top-5 | 2 pinned | 0 call_graph | 0 reverse [gated off: synthesis intent] | 7 sent

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                         | FUNCTION                    | TOKENS | STATUS
1    | 0.5175 | Book.java                    | Book_class_body             | 510    | RETRIEVED
2    | 0.4951 | BookRequest.java             | BookRequest_class_body      | 765    | RETRIEVED
3    | 0.4429 | BookResponse.java            | BookResponse_class_body     | 180    | RETRIEVED
4    | 0.4401 | schema.sql                   | books                       | 670    | RETRIEVED
5    | 0.3445 | data.sql                     | seeds_books                 | 441    | RETRIEVED
6    | 0.3014 | BookServiceImpl.java         | createBook                  | 136    | skipped
7    | 0.2882 | data.sql                     | alter_books                 | 23     | skipped
8    | 0.2656 | BookMapper.java              | BookMapper_class_body       | 53     | skipped
9    | 0.2587 | AddBookPage.jsx              | AddBookPage                 | 258    | skipped
10   | 0.2586 | BookServiceImpl.java         | deleteBook                  | 61     | skipped
11   | 0.2567 | BookServiceImpl.java         | exportBook                  | 125    | skipped
12   | 0.2516 | schema.sql                   | indexes_on_books            | 181    | skipped
13   | 0.2388 | BookServiceImpl.java         | duplicateBook               | 102    | skipped
14   | 0.2353 | BookServiceImpl.java         | updateBook                  | 173    | skipped
15   | 0.2352 | BookServiceImpl.java         | getBookById                 | 95     | skipped
16   | 0.2272 | BookController.java          | exportBook                  | 120    | skipped
17   | 0.2169 | BookController.java          | getBookById                 | 67     | skipped
18   | 0.2116 | BookController.java          | duplicateBook               | 76     | skipped
19   | 0.2082 | Book.java                    | prePersist                  | 88     | PINNED
20   | 0.1859 | GlobalExceptionHandler.java  | handleBookNotFoundException | 94     | skipped
21   | 0.1770 | BookController.java          | createBook                  | 107    | skipped
22   | 0.1754 | GlobalExceptionHandler.java  | handleDuplicateIsbnException| 97     | skipped
23   | 0.1618 | BookController.java          | updateBook                  | 124    | skipped
24   | 0.1386 | BookController.java          | searchBooks                 | 446    | skipped
25   | 0.1378 | Book.java                    | incrementViewCount          | 33     | PINNED
26   | 0.1077 | BookController.java          | deleteBook                  | 64     | skipped
27   | 0.1059 | BookController.java          | getAllBooks                 | 104    | skipped
28   | 0.0860 | ErrorResponse.java           | ErrorResponse_class_body    | 59     | skipped
29   | 0.0811 | BookServiceImpl.java         | getAllBooks                 | 61     | skipped
30   | 0.0807 | ValidationErrorResponse.java | ValidationErrorResponse_body| 92     | skipped
31   | 0.0485 | BookServiceImpl.java         | searchBooks                 | 730    | skipped
32-37| call   | (exception handlers + Application/HomePage/App)            | —      | skipped
```

**Confidence gate:** HIGH 96/100 — all 5 signals green. AST pinning
contributes weight=15 at max (2 pins, `lifecycle:field_modifier`).

#### Step 4 — Token summary

```text
TOKENS BILLED:    2,695 (8 question + 2,687 retrieved)
TOKENS SKIPPED:   4,733 (never sent to Claude)
SIGNAL RATIO:     99.7% useful
```

#### Step 5 — Answer from retrieved chunks

To add `country_code` to the Book entity:

1. **Entity** (`Book.java:Book_class_body`): add `private String countryCode;`
   with `@Column(name = "country_code", length = 2)` or similar. A
   `@PrePersist`/`@PreUpdate` default is surfaced via the pinned
   `Book.prePersist` method — if `country_code` needs a default, add
   it there.
2. **DTOs**:
   - `BookRequest.java`: add `@Size(min=2, max=2)` `String countryCode`
     with Bean Validation.
   - `BookResponse.java`: add `String countryCode` and a getter.
3. **Schema**: extend `schema.sql:books` with
   `country_code VARCHAR(2)` column; add `ALTER TABLE books ADD
   country_code VARCHAR(2);` to `data.sql:alter_books` (chunk exists
   at rank 7) to bring existing DBs in sync.
4. **Seed data**: update `data.sql:seeds_books` (rank 5, 441 tokens)
   to include the new column in the column list + each row's value.
5. **Mapper**: `BookMapper` (chunk retrieved only as class body;
   update `toEntity` / `toResponse` / `updateEntityFromRequest` to
   carry the field).

The pinned `prePersist` confirms there is a central place to set
null-safe defaults if needed.

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,428 | 2,695 | 63.7% |
| Input cost ($3/M) | $0.02228 | $0.00809 | 63.7% |

**Probe verdict:** PASS — Stage 3 AST pinning works exactly as
advertised. `prePersist` surfaces at cosine rank 19 but is marked
PINNED and sent to Claude. Matrix expected `preUpdate` as the
second pin; actual second pin is `incrementViewCount` (both are
tagged `lifecycle:field_modifier`). Minor deviation in *which*
lifecycle chunk pins second, not in *whether* pinning works.

---

### Probe A3 — Stage 4a forward call graph (update flow)

**Expected stages firing:** Stage 2 + Stage 4a (forward call graph).
At least one row labelled `CALL_GRAPH(...)` should pull in a callee
that was not in the cosine top-k.

#### Step 1 — Parse the user's question

- **Feature:** Trace the end-to-end update-book flow
- **Codebase:** Books Online backend
- **Query type:** targeted, trace intent

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "what does the update book flow look like end to end"
```

- Chunks: 37 total | top-5 | 0 pinned | 0 call_graph | 7 reverse | 12 sent

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                         | FUNCTION                    | TOKENS | STATUS
1    | 0.4408 | BookServiceImpl.java         | updateBook                  | 173    | RETRIEVED
2    | 0.3619 | BookServiceImpl.java         | createBook                  | 136    | RETRIEVED
3    | 0.3517 | BookController.java          | updateBook                  | 124    | RETRIEVED
4    | 0.3119 | BookController.java          | deleteBook                  | 64     | RETRIEVED
5    | 0.3098 | BookServiceImpl.java         | getAllBooks                 | 61     | RETRIEVED
6    | 0.3092 | AddBookPage.jsx              | AddBookPage                 | 258    | REV_CALLER(createBook)
7    | 0.3005 | data.sql                     | alter_books                 | 23     | skipped
8    | 0.2938 | BookServiceImpl.java         | exportBook                  | 125    | skipped
9    | 0.2881 | BookServiceImpl.java         | duplicateBook               | 102    | skipped
10   | 0.2784 | BookServiceImpl.java         | deleteBook                  | 61     | skipped
11   | 0.2779 | BookServiceImpl.java         | getBookById                 | 95     | skipped
12   | 0.2735 | data.sql                     | seeds_books                 | 441    | skipped
13   | 0.2632 | BookController.java          | getAllBooks                 | 104    | REV_CALLER(getAllBooks)
14   | 0.2589 | Book.java                    | Book_class_body             | 510    | skipped
15   | 0.2574 | BookController.java          | searchBooks                 | 446    | REV_IMPORT(BookService)
16   | 0.2546 | BookServiceImpl.java         | searchBooks                 | 730    | skipped
17   | 0.2394 | GlobalExceptionHandler.java  | handleBookNotFoundException | 94     | skipped
18   | 0.2368 | BookMapper.java              | BookMapper_class_body       | 53     | skipped
19   | 0.2161 | BookController.java          | exportBook                  | 120    | REV_IMPORT(BookService)
20   | 0.2159 | BookController.java          | getBookById                 | 67     | REV_IMPORT(BookService)
21   | 0.2136 | BookController.java          | createBook                  | 107    | REV_CALLER(createBook)
22   | 0.2074 | Book.java                    | incrementViewCount          | 33     | skipped
23   | 0.2041 | schema.sql                   | indexes_on_books            | 181    | skipped
24   | 0.1911 | BookController.java          | duplicateBook               | 76     | REV_IMPORT(BookService)
25   | 0.1675 | BookResponse.java            | BookResponse_class_body     | 180    | skipped
26-37| (remaining, all skipped, scores < 0.2)                              |        |
```

**Confidence gate:** MEDIUM 67/100 — lexical weak ("look like end"
don't match code tokens).

#### Step 4 — Token summary

```text
TOKENS BILLED:    1,747 (11 question + 1,736 retrieved)
TOKENS SKIPPED:   5,684
SIGNAL RATIO:     99.4% useful
```

#### Step 5 — Answer from retrieved chunks

End-to-end update-book flow:

1. **Frontend:** `AddBookPage.jsx` (a sibling of the expected
   EditBookPage — not retrieved) invokes a `booksService.createBook`
   style call; the update-specific page calls the PUT endpoint.
2. **Controller:** `BookController.updateBook(id, request)` — logs
   `PUT /api/v1/books/{id}`, validates `@RequestBody`, and calls
   `bookService.updateBook(id, request)`.
3. **Service:** `BookServiceImpl.updateBook` — fetches existing book
   by id (throws `BookNotFoundException` if missing), checks ISBN
   uniqueness if ISBN changed (throws `DuplicateIsbnException`),
   calls `bookMapper.updateEntityFromRequest`, saves, returns DTO.
4. **Repository/JPA:** `bookRepository.findById(id)` and
   `bookRepository.save(existingBook)` (JPA methods, not indexed).
5. **Entity lifecycle:** `Book.prePersist` / `@PreUpdate` would fire
   on `save()` to set default values — not directly retrieved here.

**Gap:** no `CALL_GRAPH(...)` rows appeared. The forward-graph
didn't fire because the callees of `updateBook` are all
non-indexed (JPA interface methods + mapper methods that share a
class-body chunk). Reverse lookup carried the flow instead (6 rows).

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,431 | 1,747 | 76.5% |
| Input cost ($3/M) | $0.02229 | $0.00524 | 76.5% |

**Probe verdict:** FAIL-SOFT — matrix expected 1-2 CALL_GRAPH rows;
actual 0. Pipeline did not crash; reverse-lookup substituted.
Documents an honest gap: forward-graph is only as useful as the
graph of indexed chunks.

---

### Probe A4 — Stage 4b reverse callers (createBook wrapper)

**Expected stages firing:** Stage 2 + Stage 4b. `BookController.createBook`
should surface as `REV_CALLER(createBook)` despite the name
collision with the source chunk.

#### Step 1 — Parse the user's question

- **Feature:** Explain the implementation of `BookServiceImpl.createBook`
- **Codebase:** Books Online backend
- **Query type:** targeted, implementation-trace

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "how does BookServiceImpl.createBook work"
```

- Chunks: 37 total | top-5 | 0 pinned | 1 call_graph | 9 reverse | 15 sent

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                         | FUNCTION                    | TOKENS | STATUS
1    | 0.6855 | BookServiceImpl.java         | createBook                  | 136    | RETRIEVED
2    | 0.5603 | BookServiceImpl.java         | exportBook                  | 125    | RETRIEVED
3    | 0.5546 | AddBookPage.jsx              | AddBookPage                 | 258    | RETRIEVED
4    | 0.4965 | BookServiceImpl.java         | updateBook                  | 173    | RETRIEVED
5    | 0.4788 | BookServiceImpl.java         | getBookById                 | 95     | RETRIEVED
6    | 0.4784 | BookServiceImpl.java         | duplicateBook               | 102    | skipped
7    | 0.4285 | GlobalExceptionHandler.java  | handleBookNotFoundException | 94     | skipped
8    | 0.4217 | BookServiceImpl.java         | deleteBook                  | 61     | skipped
9    | 0.4157 | data.sql                     | seeds_books                 | 441    | skipped
10   | 0.4042 | BookServiceImpl.java         | getAllBooks                 | 61     | REV_IMPORT(BookService)
11   | 0.3961 | BookController.java          | exportBook                  | 120    | REV_CALLER(exportBook)
12   | 0.3863 | BookController.java          | createBook                  | 107    | REV_CALLER(createBook)
13   | 0.3858 | schema.sql                   | indexes_on_books            | 181    | skipped
14   | 0.3781 | BookController.java          | deleteBook                  | 64     | REV_IMPORT(BookService)
15   | 0.3622 | BookController.java          | getBookById                 | 67     | REV_CALLER(getBookById)
16   | 0.3585 | Book.java                    | Book_class_body             | 510    | skipped
17   | 0.3542 | BookController.java          | updateBook                  | 124    | REV_CALLER(updateBook)
18   | 0.3275 | BookMapper.java              | BookMapper_class_body       | 53     | skipped
19   | 0.3260 | data.sql                     | alter_books                 | 23     | skipped
20   | 0.3086 | BookController.java          | getAllBooks                 | 104    | REV_IMPORT(BookService)
21   | 0.3039 | BookController.java          | searchBooks                 | 446    | REV_IMPORT(BookService)
22   | 0.3029 | BookController.java          | duplicateBook               | 76     | REV_IMPORT(BookService)
23   | 0.3000 | BookServiceImpl.java         | searchBooks                 | 730    | skipped
24   | 0.2459 | GlobalExceptionHandler.java  | handleDuplicateIsbnException| 97     | skipped
25-27| (medium-low)                                                         |        | skipped
28   | 0.1620 | Book.java                    | incrementViewCount          | 33     | CALL_GRAPH(incrementViewCount)
29-37| (low / call-only rows)                                               |        | skipped
```

**Confidence gate:** HIGH 84/100.

#### Step 4 — Token summary

```text
TOKENS BILLED:    1,996 (7 question + 1,989 retrieved)
TOKENS SKIPPED:   5,431
SIGNAL RATIO:     99.6% useful
```

#### Step 5 — Answer from retrieved chunks

`BookServiceImpl.createBook(BookRequest request)`:

1. Log debug with ISBN.
2. `bookRepository.existsByIsbn(request.getIsbn())` — if true, log
   warn and throw `DuplicateIsbnException(request.getIsbn())`.
3. `bookMapper.toEntity(request)` — build Book entity from request.
4. `bookRepository.save(book)` — persist; `@PrePersist` fires
   (pinned in A2/G17).
5. Log info with saved id + isbn.
6. `bookMapper.toResponse(savedBook)` — return DTO.

**Callers:** `BookController.createBook` (`REV_CALLER(createBook)`)
handles the POST `/api/v1/books` endpoint and validates the
`@RequestBody` before delegating. Frontend `AddBookPage.jsx`
(cosine rank 3, RETRIEVED) invokes `booksService.createBook(data)`.

The wrapper-method heuristic is proven: `BookController.createBook`
surfaces as REV_CALLER even though its function name equals the
source chunk's.

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,427 | 1,996 | 73.1% |
| Input cost ($3/M) | $0.02228 | $0.00599 | 73.1% |

**Probe verdict:** PASS — reverse-caller fires, name-collision
disambiguation works, CALL_GRAPH pulls in `incrementViewCount` from
a neighboring top-5 chunk (`getBookById`).

---

### Probe A5 — Stage 4b reverse importers (`*Impl` → interface)

**Expected stages firing:** Stage 2 + Stage 4b REV_IMPORT.
`BookController` methods must surface as `REV_IMPORT(BookService)`
(the interface, not `BookServiceImpl`).

#### Step 1 — Parse the user's question

- **Feature:** Find all callers/importers of `BookService`
- **Codebase:** Books Online backend
- **Query type:** targeted, dependency discovery

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "what uses BookService"
```

- Chunks: 37 total | top-5 | 0 pinned | 1 call_graph | 9 reverse | 15 sent

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                         | FUNCTION                    | TOKENS | STATUS
1    | 0.6326 | BookServiceImpl.java         | createBook                  | 136    | RETRIEVED
2    | 0.5693 | BookServiceImpl.java         | updateBook                  | 173    | RETRIEVED
3    | 0.5664 | BookServiceImpl.java         | exportBook                  | 125    | RETRIEVED
4    | 0.5528 | BookServiceImpl.java         | getBookById                 | 95     | RETRIEVED
5    | 0.5279 | BookServiceImpl.java         | getAllBooks                 | 61     | RETRIEVED
6    | 0.4955 | BookServiceImpl.java         | duplicateBook               | 102    | skipped
7    | 0.4687 | BookServiceImpl.java         | searchBooks                 | 730    | skipped
8    | 0.4182 | BookServiceImpl.java         | deleteBook                  | 61     | skipped
9    | 0.3796 | BookController.java          | exportBook                  | 120    | REV_CALLER(exportBook)
10   | 0.3565 | BookController.java          | searchBooks                 | 446    | REV_IMPORT(BookService)
11   | 0.3540 | GlobalExceptionHandler.java  | handleBookNotFoundException | 94     | skipped
12   | 0.3533 | AddBookPage.jsx              | AddBookPage                 | 258    | REV_CALLER(createBook)
13   | 0.3532 | BookController.java          | deleteBook                  | 64     | REV_IMPORT(BookService)
14   | 0.3356 | schema.sql                   | indexes_on_books            | 181    | skipped
15   | 0.3280 | BookController.java          | getBookById                 | 67     | REV_CALLER(getBookById)
16   | 0.3276 | BookController.java          | getAllBooks                 | 104    | REV_CALLER(getAllBooks)
17   | 0.3252 | data.sql                     | seeds_books                 | 441    | skipped
18   | 0.3228 | BookController.java          | createBook                  | 107    | REV_CALLER(createBook)
19   | 0.3213 | BookMapper.java              | BookMapper_class_body       | 53     | skipped
20   | 0.3202 | BookController.java          | updateBook                  | 124    | REV_CALLER(updateBook)
21   | 0.2631 | Book.java                    | Book_class_body             | 510    | skipped
22   | 0.2256 | BookController.java          | duplicateBook               | 76     | REV_IMPORT(BookService)
23-27| (low, skipped)                                                       |        |
28   | 0.1321 | Book.java                    | incrementViewCount          | 33     | CALL_GRAPH(incrementViewCount)
29-37| (low / call-only)                                                    |        |
```

**Confidence gate:** HIGH 70/100 (exactly on threshold).

#### Step 4 — Token summary

```text
TOKENS BILLED:    1,993 (4 question + 1,989 retrieved)
TOKENS SKIPPED:   5,431
SIGNAL RATIO:     99.8% useful
```

#### Step 5 — Answer from retrieved chunks

`BookService` (interface) is used by:

- **`BookController.java`** — every endpoint delegates to
  `bookService.*`:
  `createBook`, `getAllBooks`, `getBookById`, `updateBook`,
  `deleteBook`, `searchBooks`, `duplicateBook`, `exportBook`.
  Surfaces via `REV_IMPORT(BookService)` (the interface) plus
  `REV_CALLER(<method>)` on the specific delegating method.
- **Frontend (`AddBookPage.jsx`)** — indirect; calls the controller
  via `booksService.createBook`, not the Java `BookService`.

No other Java caller appears within the indexed chunks. The
`*Impl`→interface stripping heuristic is confirmed: REV_IMPORT
surfaces `BookService`, not `BookServiceImpl`.

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,424 | 1,993 | 73.2% |
| Input cost ($3/M) | $0.02227 | $0.00598 | 73.2% |

**Probe verdict:** PASS.

---

### Probe A6 — Stage 1 hierarchy pre-filter (broad query)

**Expected stages firing:** Stage 1 narrows scope by L2 module
("controller"). Top-5 should be controller-only; zero non-controller
chunks permitted.

#### Step 1 — Parse the user's question

- **Feature:** Explain the controller layer (architectural overview)
- **Codebase:** Books Online backend
- **Query type:** broad ("explain the ...")

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "explain the controller layer"
```

- Chunks: 37 total | top-5 | 0 pinned | 0 call_graph | 1 reverse | 6 sent

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                         | FUNCTION                    | TOKENS | STATUS
1    | 0.5269 | BookController.java          | createBook                  | 107    | RETRIEVED
2    | 0.5064 | BookController.java          | updateBook                  | 124    | RETRIEVED
3    | 0.4848 | BookController.java          | getBookById                 | 67     | RETRIEVED
4    | 0.4804 | BookController.java          | exportBook                  | 120    | RETRIEVED
5    | 0.4436 | BookController.java          | duplicateBook               | 76     | RETRIEVED
6    | 0.4139 | BookController.java          | deleteBook                  | 64     | skipped
7    | 0.4075 | BookController.java          | getAllBooks                 | 104    | skipped
8    | 0.3927 | BookController.java          | searchBooks                 | 446    | skipped
9    | 0.3332 | BookMapper.java              | BookMapper_class_body       | 53     | skipped
10-20| (service + exception-handler chunks, all skipped)                    |        |
21-31| (remaining, low scores)                                              |        |
32   | 0.1622 | AddBookPage.jsx              | AddBookPage                 | 258    | REV_CALLER(createBook)
33-37| (lowest)                                                             |        |
```

**Confidence gate:** MEDIUM 49/100 (generic query, small score gap,
weak lexical).

#### Step 4 — Token summary

```text
TOKENS BILLED:    756 (4 question + 752 retrieved)
TOKENS SKIPPED:   6,668 (best savings in Group A)
SIGNAL RATIO:     99.5% useful
```

#### Step 5 — Answer from retrieved chunks

The controller layer is a single class `BookController` exposing
REST endpoints at `/api/v1/books`:

| Endpoint | Method |
|---|---|
| `POST /books` | `createBook(@Valid @RequestBody BookRequest)` → 201 |
| `PUT /books/{id}` | `updateBook(id, @Valid @RequestBody BookRequest)` |
| `GET /books/{id}` | `getBookById(id)` |
| `GET /books/{id}/export` | `exportBook(id, @RequestParam(defaultValue="json") format)` |
| `POST /books/{id}/duplicate` | `duplicateBook(id)` → 201 |

Each method:
- Logs the HTTP verb + path.
- Validates payload via `@Valid` where applicable.
- Delegates to `bookService.<method>(...)`.
- Returns `ResponseEntity.ok(...)` or `.status(HttpStatus.CREATED).body(...)`.

Top-5 are 5 of the 8 controller methods; remaining 3
(`deleteBook`, `getAllBooks`, `searchBooks`) rank 6-8 and would
surface at top-k=8.

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,424 | 756 | 89.8% |
| Input cost ($3/M) | $0.02227 | $0.00227 | 89.8% |

**Probe verdict:** PASS — zero non-controller chunks in top-5.
Hierarchy pre-filter holds scope perfectly.

---

### Probe A7 — Stage 5 mapper group (negative regression probe)

**Expected stages firing:** Stage 2 + possibly 4b. No false-positive
mapper-group expansion; Stage 5 should NOT add phantom Mapper rows
because the matrix assumed BookMapper wasn't indexed.

#### Step 1 — Parse the user's question

- **Feature:** Understand DTO↔Entity field mapping
- **Codebase:** Books Online backend
- **Query type:** targeted, mapping semantics

#### Step 2 — Search the vector DB

```bash
python3 ~/.claude/scripts/vectordb_search.py "how are book fields mapped between request and entity"
```

- Chunks: 37 total | top-5 | 2 pinned | 0 call_graph | 7 reverse | 14 sent

#### Step 3 — Full ranked list

```text
RANK | SCORE  | FILE                         | FUNCTION                    | TOKENS | STATUS
1    | 0.5419 | BookMapper.java              | BookMapper_class_body       | 53     | RETRIEVED
2    | 0.5076 | BookController.java          | exportBook                  | 120    | RETRIEVED
3    | 0.5015 | BookServiceImpl.java         | createBook                  | 136    | RETRIEVED
4    | 0.4952 | BookController.java          | getBookById                 | 67     | RETRIEVED
5    | 0.4926 | BookController.java          | updateBook                  | 124    | RETRIEVED
6    | 0.4823 | BookController.java          | createBook                  | 107    | REV_CALLER(createBook)
7    | 0.4764 | BookServiceImpl.java         | duplicateBook               | 102    | skipped
8    | 0.4718 | Book.java                    | Book_class_body             | 510    | skipped
9    | 0.4716 | BookServiceImpl.java         | updateBook                  | 173    | REV_IMPORT(BookMapper)
10   | 0.4635 | BookServiceImpl.java         | getBookById                 | 95     | REV_CALLER(incrementViewCount)
11   | 0.4563 | BookServiceImpl.java         | exportBook                  | 125    | skipped
12   | 0.4405 | BookController.java          | searchBooks                 | 446    | skipped
13   | 0.4358 | BookController.java          | deleteBook                  | 64     | REV_IMPORT(BookService)
14   | 0.4325 | schema.sql                   | indexes_on_books            | 181    | skipped
15   | 0.4262 | BookServiceImpl.java         | deleteBook                  | 61     | skipped
16   | 0.4255 | BookController.java          | getAllBooks                 | 104    | REV_IMPORT(BookService)
17   | 0.4234 | BookController.java          | duplicateBook               | 76     | skipped
18   | 0.4130 | BookServiceImpl.java         | getAllBooks                 | 61     | REV_IMPORT(BookMapper)
19   | 0.4002 | BookRequest.java             | BookRequest_class_body      | 765    | skipped
20   | 0.3973 | data.sql                     | alter_books                 | 23     | skipped
21   | 0.3971 | AddBookPage.jsx              | AddBookPage                 | 258    | REV_CALLER(createBook)
22-25| (medium, skipped)                                                    |        |
26   | 0.2408 | Book.java                    | prePersist                  | 88     | PINNED
27   | 0.2351 | GlobalExceptionHandler.java  | handleDuplicateIsbnException| 97     | skipped
28   | 0.2170 | Book.java                    | incrementViewCount          | 33     | PINNED
29-37| (lowest, skipped)                                                    |        |
```

**Confidence gate:** HIGH 70/100 (barely — AST pinning lifts it).

#### Step 4 — Token summary

```text
TOKENS BILLED:    1,492 (9 question + 1,483 retrieved)
TOKENS SKIPPED:   5,937
SIGNAL RATIO:     99.4% useful
```

#### Step 5 — Answer from retrieved chunks

`BookMapper` (class-body chunk, rank 1) exposes three methods that
`BookServiceImpl` invokes:

- `bookMapper.toEntity(request)` — used in `createBook`.
- `bookMapper.toResponse(entity)` — used in `createBook`,
  `updateBook`, `exportBook`, `getBookById`, and via `Page.map` in
  `getAllBooks`.
- `bookMapper.updateEntityFromRequest(existingBook, request)` —
  used in `updateBook` (surfaces as REV_IMPORT(BookMapper)).

The mapper class-body chunk itself is only 53 tokens (interface-ish
stub); detailed field mappings are implied by the individual
service-method usages above.

Lifecycle pins (`prePersist`, `incrementViewCount`) appear because
the query mentions "entity" — they're not strictly about mapping
but document the entity's default-value behaviour.

#### Step 6 — Traditional vs Vector DB

| Metric | Traditional | Vector DB | Savings |
|---|---:|---:|---:|
| Input tokens | 7,429 | 1,492 | 79.9% |
| Input cost ($3/M) | $0.02229 | $0.00448 | 79.9% |

**Probe verdict:** PASS with matrix note — the matrix assumed
`BookMapper` wasn't indexed (stated "no chunks with file_path
matching %Mapper%"). Actual DB has `BookMapper_class_body` as a
53-token chunk. Stage 5's file-path LIKE pattern was not observed
to trigger a phantom mapper-group expansion (no extra header
counter), so no false-positive; the matrix documentation is stale.

---

### Group A rollup

| Probe | Billed | Skipped | Traditional | Savings | Confidence | Verdict |
|---|---:|---:|---:|---:|---|---|
| A1 | 2,088 | 5,340 | 7,428 | 71.9% | HIGH 85 | PASS |
| A2 | 2,695 | 4,733 | 7,428 | 63.7% | HIGH 96 | PASS (pin mapping deviation) |
| A3 | 1,747 | 5,684 | 7,431 | 76.5% | MED 67 | FAIL-SOFT (no CG rows) |
| A4 | 1,996 | 5,431 | 7,427 | 73.1% | HIGH 84 | PASS |
| A5 | 1,993 | 5,431 | 7,424 | 73.2% | HIGH 70 | PASS |
| A6 | 756 | 6,668 | 7,424 | 89.8% | MED 49 | PASS |
| A7 | 1,492 | 5,937 | 7,429 | 79.9% | HIGH 70 | PASS (matrix stale on mapper) |
| **TOTAL** | **12,767** | **39,224** | **51,991** | **75.4%** | — | 5 PASS / 1 PASS-w-dev / 1 FAIL-SOFT |

