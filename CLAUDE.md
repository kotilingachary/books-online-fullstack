# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Backend (Spring Boot + Maven)
```bash
cd backend
mvn spring-boot:run                          # Start backend on port 8080
mvn test                                     # Run all tests
mvn test -Dtest=BookControllerTest           # Run single test class
mvn test -Dtest=BookControllerTest#testName  # Run single test method
mvn clean test jacoco:report                 # Tests with coverage (target/site/jacoco/)
mvn clean package -DskipTests               # Build JAR
```

### Frontend (React + Vite)
```bash
cd frontend
npm run dev          # Start dev server on port 5173
npm run build        # Production build
npm run lint         # ESLint (zero-warnings policy)
npm test             # Vitest unit tests
npm test -- Button.test.jsx    # Run single test file
npm run test:coverage          # Tests with coverage
```

### Combined startup
```bash
./start.sh   # Starts both backend and frontend (Mac/Linux)
```

## Architecture

**Full-stack book management app:** React SPA → Spring Boot REST API → H2 database.

### Backend (`backend/src/main/java/com/booksonline/`)
Layered architecture: Controller → Service → Repository → Entity.

- `controller/` — REST endpoints; `BookController` handles all `/api/v1/books` routes
- `service/` — business logic interfaces + `impl/` implementations
- `repository/` — `BookRepository` extends `JpaRepository`, includes JPA Specifications for search
- `model/entity/` — `Book` JPA entity (18 columns, single table)
- `model/dto/` — request/response DTOs decoupled from the entity
- `mapper/` — entity ↔ DTO conversion
- `exception/` — custom exceptions + `GlobalExceptionHandler` for unified error responses
- `config/` — OpenAPI (Swagger) and Spring Security config

Database is H2 file-based at `./data/booksdb`. Schema initialized via `schema.sql`, seeded with 20 books via `data.sql` on every startup. H2 console at `http://localhost:8080/h2-console` (user: `sa`, password: blank).

Swagger UI at `http://localhost:8080/swagger-ui.html`.

### Frontend (`frontend/src/`)
React Router v6 SPA with 7 routes/pages.

- `pages/` — one component per route (Home, BooksList, AddBook, EditBook, BookDetails, Search, NotFound)
- `components/common/` — reusable UI primitives; `components/layout/` — page wrapper
- `services/api.js` — single Axios instance; reads `VITE_API_URL` from `.env`; has request interceptor (JWT-ready) and response interceptor (error normalization)
- `context/` — React Context for shared state
- `hooks/` — custom hooks
- Form validation via React Hook Form + Zod

### API
Base URL: `http://localhost:8080/api/v1/books`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/books` | List all (paginated) |
| GET | `/books/{id}` | Get by ID (increments view count) |
| POST | `/books` | Create |
| PUT | `/books/{id}` | Update |
| DELETE | `/books/{id}` | Delete |
| GET | `/books/search` | Advanced search (17+ filter params) |
| POST | `/books/{id}/duplicate` | Clone a book |
| GET | `/books/{id}/export` | Export book data |

### Testing
- **Backend:** `@WebMvcTest` + MockMvc for controller tests; `@ExtendWith(MockitoExtension.class)` for service unit tests
- **Frontend:** Vitest + React Testing Library; test files live in `__tests__/` alongside the component

## Prerequisites
- Java 17+ (`JAVA_HOME` must point to 17)
- Maven 3.9+
- Node.js 18+

## Environment
`frontend/.env` must define:
```
VITE_API_URL=http://localhost:8080/api/v1
```
