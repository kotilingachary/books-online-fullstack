# Books Online - Backend API

REST API for Books Online Book Management System built with Spring Boot.

## Technology Stack

- **Framework:** Spring Boot 3.2.2
- **Language:** Java 17
- **Build Tool:** Maven 3.9+
- **Database:** H2 (in-memory/file-based)
- **ORM:** Spring Data JPA + Hibernate 6.x
- **API Documentation:** Springdoc OpenAPI (Swagger UI)
- **Security:** Spring Security (V1: Open access, V2: JWT)
- **Testing:** JUnit 5, Mockito, MockMvc

## Project Structure

```
backend/
├── src/main/java/com/booksonline/
│   ├── Application.java                          # Main Spring Boot application
│   ├── config/                                   # Configuration classes
│   │   ├── OpenApiConfig.java                    # Swagger/OpenAPI config
│   │   └── SecurityConfig.java                   # Security configuration
│   ├── controller/                               # REST Controllers
│   │   └── BookController.java                   # Book endpoints
│   ├── service/                                  # Business logic
│   │   ├── BookService.java                      # Service interface
│   │   └── impl/
│   │       └── BookServiceImpl.java              # Service implementation
│   ├── repository/                               # Data access
│   │   └── BookRepository.java                   # JPA repository
│   ├── model/
│   │   ├── entity/                               # JPA Entities
│   │   │   └── Book.java                         # Book entity
│   │   └── dto/                                  # Data Transfer Objects
│   │       ├── request/
│   │       │   └── BookRequest.java              # Create/update request DTO
│   │       └── response/
│   │           ├── BookResponse.java             # Book response DTO
│   │           ├── ErrorResponse.java            # Error response DTO
│   │           └── ValidationErrorResponse.java  # Validation error DTO
│   ├── exception/                                # Exception handling
│   │   ├── GlobalExceptionHandler.java           # @ControllerAdvice
│   │   ├── BookNotFoundException.java            # 404 exception
│   │   └── DuplicateIsbnException.java           # 409 exception
│   ├── mapper/                                   # Entity-DTO mappers
│   │   └── BookMapper.java                       # Book mapper
│   └── util/                                     # Utility classes
├── src/main/resources/
│   ├── application.properties                    # Application configuration
│   ├── schema.sql                                # Database schema DDL
│   └── data.sql                                  # Seed data
├── src/test/java/                                # Unit and integration tests
├── pom.xml                                       # Maven dependencies
└── README.md                                     # This file
```

## Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- (Optional) Docker for containerized deployment

## Getting Started

### 1. Build the Project

```bash
cd backend
mvn clean install
```

### 2. Run the Application

```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

### 3. Access Swagger UI

Open your browser and navigate to:

**http://localhost:8080/swagger-ui.html**

This provides interactive API documentation where you can test all endpoints.

### 4. Access H2 Database Console

For debugging and viewing database contents:

**http://localhost:8080/h2-console**

- JDBC URL: `jdbc:h2:file:./data/booksdb`
- Username: `sa`
- Password: (leave blank)

## API Endpoints

Base URL: `http://localhost:8080/api/v1`

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /books | List all books (paginated) |
| GET    | /books/{id} | Get book by ID (increments view count) |
| POST   | /books | Create new book |
| PUT    | /books/{id} | Update book |
| DELETE | /books/{id} | Delete book |
| GET    | /books/search | Advanced search with filters |
| POST   | /books/{id}/duplicate | Duplicate book |
| GET    | /books/{id}/export | Export book data |

### Example Requests

#### Create a Book

```bash
curl -X POST http://localhost:8080/api/v1/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Clean Code",
    "isbn": "978-0132350884",
    "author": "Robert C. Martin",
    "genre": "Programming",
    "publicationYear": 2008,
    "language": "English",
    "price": 44.99,
    "isAvailable": true
  }'
```

#### Search Books

```bash
# Search by genre
curl "http://localhost:8080/api/v1/books/search?genre=Programming&page=0&size=10"

# Quick search
curl "http://localhost:8080/api/v1/books/search?q=clean+code"

# Advanced search with multiple filters
curl "http://localhost:8080/api/v1/books/search?genre=Programming&minYear=2000&maxYear=2023&minPrice=20&maxPrice=60"
```

## Database Schema

The application uses a single `books` table with 18 columns:

**Primary Key:**
- `id` - BIGINT AUTO_INCREMENT

**Required Fields:**
- `title` - VARCHAR(200)
- `isbn` - VARCHAR(20) UNIQUE
- `author` - VARCHAR(100)
- `genre` - VARCHAR(50)
- `publication_year` - INT
- `language` - VARCHAR(30)
- `is_available` - BOOLEAN

**Optional Fields:**
- `publisher` - VARCHAR(100)
- `pages` - INT
- `description` - TEXT
- `cover_image_url` - VARCHAR(500)
- `price` - DECIMAL(10, 2)
- `stock_quantity` - INT
- `rating` - DECIMAL(2, 1)
- `review_count` - INT

**System-Managed:**
- `view_count` - INT
- `created_at` - TIMESTAMP
- `updated_at` - TIMESTAMP

### Indexes

8 indexes for query performance:
- `idx_books_title`
- `idx_books_author`
- `idx_books_genre`
- `idx_books_publication_year`
- `idx_books_is_available`
- `idx_books_created_at`
- `idx_books_genre_year` (composite)
- `idx_books_author_title` (composite)

## Configuration

Key configuration properties in `application.properties`:

```properties
# Database
spring.datasource.url=jdbc:h2:file:./data/booksdb
spring.jpa.hibernate.ddl-auto=update

# Server
server.port=8080

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Logging
logging.level.com.booksonline=INFO
```

## Testing

Run all tests:

```bash
mvn test
```

Run tests with coverage:

```bash
mvn clean test jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

## Building for Production

Create an executable JAR:

```bash
mvn clean package -DskipTests
```

The JAR file will be in `target/books-online-backend-1.0.0.jar`

Run the JAR:

```bash
java -jar target/books-online-backend-1.0.0.jar
```

## Docker Support

Build Docker image:

```bash
docker build -t books-online-backend .
```

Run container:

```bash
docker run -p 8080:8080 books-online-backend
```

## Architecture

### Layered Architecture

```
Controller Layer (@RestController)
    ↓
Service Layer (@Service)
    ↓
Repository Layer (JpaRepository)
    ↓
Database (H2)
```

### Design Patterns

- **MVC Pattern**: Separation of concerns
- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic encapsulation
- **DTO Pattern**: Decouple API from entities
- **Dependency Injection**: Loose coupling via Spring

### Key Features

- **Pagination**: All list endpoints support Spring Data pagination
- **Advanced Search**: JPA Specifications for dynamic queries
- **Exception Handling**: Global @ControllerAdvice for consistent errors
- **Validation**: Bean Validation (JSR-380) on DTOs
- **Auditing**: Automatic created_at/updated_at timestamps
- **API Documentation**: Auto-generated Swagger UI
- **CORS**: Configured for frontend integration

## Sample Data

The application includes 20 sample books across 7 genres:
- Programming (6 books)
- Science Fiction (3 books)
- Fiction (2 books)
- Business (2 books)
- Self-Help (2 books)
- History (2 books)
- Biography (1 book)

Sample data is loaded from `data.sql` on application startup.

## Common Issues

### Port 8080 Already in Use

```bash
# Find process using port 8080
lsof -ti:8080

# Kill the process
kill -9 <PID>
```

### Database Locked Error

If you see "Database is already closed" or "Database may be already in use":
- Close all connections to H2 console
- Restart the application

### Maven Build Fails

```bash
# Clean and rebuild
mvn clean install -U

# Skip tests if needed
mvn clean install -DskipTests
```

## Future Enhancements (V2)

- JWT-based authentication and authorization
- PostgreSQL migration for production
- CSV and PDF export functionality
- Bulk import from CSV
- User reviews and ratings
- Analytics dashboard
- Caching with Redis
- Integration with Google Books API

## API Documentation

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs
- **Full API Spec**: See `../artifacts/05-api-spec/api-documentation.md`

## License

MIT License

## Support

For issues or questions, please create an issue in the repository.
