# Security Architecture Document

**Product:** Books Online - Book Management System
**Version:** 1.0
**Generated:** 2026-02-11
**Security Level:** Basic (V1) → Enhanced (V2)

---

## Table of Contents

1. [Security Overview](#security-overview)
2. [V1 Security Measures (Current)](#v1-security-measures-current)
3. [V2 Security Enhancements (Future)](#v2-security-enhancements-future)
4. [OWASP Top 10 Protection](#owasp-top-10-protection)
5. [Data Protection](#data-protection)
6. [API Security](#api-security)
7. [Frontend Security](#frontend-security)
8. [Database Security](#database-security)
9. [Deployment Security](#deployment-security)
10. [Security Testing](#security-testing)
11. [Incident Response](#incident-response)

---

## Security Overview

### Security Posture by Version

| Version | Security Level | Authentication | Authorization | Data Encryption | Audit Logging |
|---------|----------------|----------------|---------------|-----------------|---------------|
| **V1** | Basic | ❌ None | ❌ None | ⚠️ HTTPS (prod) | ❌ No |
| **V2** | Enhanced | ✅ JWT | ✅ RBAC | ✅ HTTPS + DB encryption | ✅ Yes |
| **V3** | Advanced | ✅ OAuth2/OIDC | ✅ Fine-grained | ✅ End-to-end | ✅ SIEM integration |

### V1 Security Philosophy

**Books Online V1** is a **single-user development/demo application** with **basic security measures**:

- **No Authentication**: Open access to all endpoints (suitable for local development only)
- **No Authorization**: All users have full CRUD access
- **Focus**: Core functionality and data integrity
- **Deployment**: Development environment only (localhost)

⚠️ **WARNING**: V1 is NOT production-ready. V2 security enhancements required for production deployment.

---

## V1 Security Measures (Current)

### 1. Input Validation

#### Backend Validation (Spring Boot)

All user inputs validated using Bean Validation (JSR-380):

```java
@Entity
@Table(name = "books")
public class Book {

    @NotNull(message = "Title is required")
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 200, message = "Title must be less than 200 characters")
    private String title;

    @NotNull(message = "ISBN is required")
    @Size(min = 10, max = 20, message = "ISBN must be between 10 and 20 characters")
    @Column(unique = true)
    private String isbn;

    @NotNull(message = "Author is required")
    @NotBlank(message = "Author cannot be blank")
    @Size(max = 100, message = "Author must be less than 100 characters")
    private String author;

    @NotNull(message = "Publication year is required")
    @Min(value = 1000, message = "Publication year must be at least 1000")
    @Max(value = 2100, message = "Publication year must be at most 2100")
    private Integer publicationYear;

    @DecimalMin(value = "0.0", message = "Price must be positive")
    @DecimalMax(value = "9999.99", message = "Price must be less than 10000")
    private BigDecimal price;
}
```

**Validation Points:**
- ✅ Required fields checked (`@NotNull`, `@NotBlank`)
- ✅ String length validated (`@Size`)
- ✅ Number ranges enforced (`@Min`, `@Max`)
- ✅ Format constraints (`@Pattern` for ISBN if needed)
- ✅ Unique constraints (database level)

#### Frontend Validation (React + Zod)

```javascript
import { z } from 'zod';

const bookSchema = z.object({
  title: z.string()
    .min(1, 'Title is required')
    .max(200, 'Title must be less than 200 characters'),

  isbn: z.string()
    .min(10, 'ISBN must be at least 10 characters')
    .max(20, 'ISBN must be at most 20 characters'),

  author: z.string()
    .min(1, 'Author is required')
    .max(100, 'Author must be less than 100 characters'),

  publicationYear: z.number()
    .int('Year must be an integer')
    .min(1000, 'Year must be at least 1000')
    .max(2100, 'Year must be at most 2100'),

  price: z.number()
    .positive('Price must be positive')
    .max(9999.99, 'Price must be less than 10000')
    .optional(),
});
```

**Benefits:**
- ✅ Client-side validation before API call
- ✅ Reduces unnecessary server requests
- ✅ Better user experience (instant feedback)
- ✅ Same validation rules as backend

### 2. SQL Injection Prevention

#### JPA Parameterized Queries

Spring Data JPA automatically uses parameterized queries:

```java
// ✅ SAFE - JPA generates parameterized query
List<Book> findByTitleContainingIgnoreCase(String title);

// ✅ SAFE - @Query with named parameter
@Query("SELECT b FROM Book b WHERE b.author = :author")
List<Book> findByAuthor(@Param("author") String author);

// ✅ SAFE - Criteria API (type-safe)
Specification<Book> spec = (root, query, cb) ->
    cb.equal(root.get("genre"), genre);
```

**Never use string concatenation for SQL:**

```java
// ❌ VULNERABLE to SQL injection
String sql = "SELECT * FROM books WHERE title = '" + userInput + "'";

// ✅ SAFE - Use JPA or parameterized queries
@Query("SELECT b FROM Book b WHERE b.title = :title")
```

**Protection Level:**
- ✅ JPA/Hibernate automatically parameterizes all queries
- ✅ No raw SQL string concatenation
- ✅ Type-safe Criteria API for dynamic queries
- ✅ Named parameters in custom @Query methods

### 3. Cross-Site Scripting (XSS) Prevention

#### Backend Protection

Spring Boot default settings provide XSS protection:

```java
// ✅ Jackson ObjectMapper escapes HTML by default
@RestController
public class BookController {

    // JSON serialization automatically escapes special characters
    @GetMapping("/books/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(bookMapper.toResponse(book));
    }
}
```

**If user input contains:**
```
<script>alert('XSS')</script>
```

**Stored as:**
```
&lt;script&gt;alert('XSS')&lt;/script&gt;
```

#### Frontend Protection (React)

React's JSX automatically escapes content:

```jsx
// ✅ SAFE - React escapes by default
function BookDetails({ book }) {
  return (
    <div>
      <h1>{book.title}</h1>  {/* Automatically escaped */}
      <p>{book.description}</p>
    </div>
  );
}

// ❌ DANGEROUS - Only use if you trust the HTML
function UnsafeComponent({ htmlContent }) {
  return <div dangerouslySetInnerHTML={{ __html: htmlContent }} />;
}
```

**Protection Level:**
- ✅ React JSX escapes all content by default
- ✅ Backend Jackson serialization escapes HTML entities
- ✅ No `dangerouslySetInnerHTML` in codebase
- ✅ Content Security Policy headers (production)

### 4. CORS (Cross-Origin Resource Sharing)

#### Development CORS Configuration

```java
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173")  // Vite dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

**Production CORS** (environment-specific):

```java
.allowedOrigins(
    System.getenv("FRONTEND_URL")  // https://booksonline.com
)
```

**CORS Headers Sent:**

```http
Access-Control-Allow-Origin: http://localhost:5173
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Allow-Credentials: true
```

**Protection Level:**
- ✅ Only allowed origins can access API
- ✅ Preflight requests handled (OPTIONS)
- ✅ Prevents unauthorized cross-origin access
- ⚠️ V1: Single allowed origin (localhost:5173)
- ✅ V2: Environment-based allowed origins

### 5. HTTPS Enforcement (Production Only)

#### V1 Development (HTTP)

```
http://localhost:8080/api/v1/books
```

- ⚠️ HTTP only (development)
- No SSL certificate required
- Localhost-only access

#### V2 Production (HTTPS)

```
https://api.booksonline.com/api/v1/books
```

**Spring Boot HTTPS Configuration:**

```yaml
# application-prod.yml
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:keystore.p12
    key-store-password: ${SSL_KEYSTORE_PASSWORD}
    key-store-type: PKCS12
    key-alias: booksonline
```

**Force HTTPS Redirect:**

```java
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.requiresChannel()
            .anyRequest()
            .requiresSecure();  // Force HTTPS
        return http.build();
    }
}
```

**Protection Level:**
- ⚠️ V1: HTTP only (development)
- ✅ V2: HTTPS required (production)
- ✅ V2: Automatic HTTP → HTTPS redirect
- ✅ V2: SSL/TLS 1.2+ only

### 6. Error Handling (No Information Leakage)

#### Global Exception Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFound(BookNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message(ex.getMessage())  // ✅ User-friendly message
            .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        // ✅ Log full stack trace server-side
        log.error("Unexpected error occurred", ex);

        // ❌ Don't expose stack trace to client
        ErrorResponse error = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("Internal Server Error")
            .message("An unexpected error occurred")  // ✅ Generic message
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

**What NOT to expose:**
- ❌ Stack traces
- ❌ Database error details
- ❌ Internal file paths
- ❌ Framework version details

**What to expose:**
- ✅ User-friendly error message
- ✅ HTTP status code
- ✅ Validation errors (field-level)
- ✅ Timestamp

### 7. Rate Limiting (Future: V2)

V1 does not include rate limiting. V2 will add:

```java
// V2: Rate limiting with Bucket4j
@RateLimiter(name = "bookApi", fallbackMethod = "rateLimitFallback")
@GetMapping("/books")
public ResponseEntity<Page<Book>> getAllBooks() {
    // ...
}
```

**V2 Rate Limits:**
- 100 requests per minute per IP
- 1000 requests per hour per IP
- 429 Too Many Requests response when exceeded

---

## V2 Security Enhancements (Future)

### 1. Authentication (JWT-based)

#### Spring Security + JWT

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Using JWT, not session-based
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()  // Login/register
                .requestMatchers("/api/v1/books/**").authenticated()  // Requires JWT
            )
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // No sessions
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
```

#### Authentication Flow

```
1. User sends credentials (POST /api/v1/auth/login)
   ↓
2. Backend validates credentials
   ↓
3. If valid, generate JWT token
   {
     "sub": "user@example.com",
     "roles": ["ROLE_USER"],
     "iat": 1675270800,
     "exp": 1675357200
   }
   ↓
4. Frontend stores JWT (localStorage or httpOnly cookie)
   ↓
5. Frontend includes JWT in all API requests:
   Authorization: Bearer <jwt-token>
   ↓
6. Backend validates JWT on each request
   ↓
7. If valid, process request; if invalid, return 401 Unauthorized
```

#### JWT Token Structure

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY3NTI3MDgwMCwiZXhwIjoxNjc1MzU3MjAwfQ.signature
│                                      │                                                                                      │
│         HEADER (Base64)              │                    PAYLOAD (Base64)                                                  │  SIGNATURE
```

**JWT Claims:**
- `sub`: Subject (user email)
- `roles`: User roles (ROLE_USER, ROLE_ADMIN)
- `iat`: Issued at (timestamp)
- `exp`: Expiration (timestamp)

### 2. Authorization (Role-Based Access Control)

#### User Roles

| Role | Permissions |
|------|-------------|
| **ROLE_USER** | Read books, view details |
| **ROLE_LIBRARIAN** | Read, create, update books |
| **ROLE_ADMIN** | Full CRUD + delete + export |

#### Role-Based Endpoint Protection

```java
@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    // Anyone authenticated can read
    @PreAuthorize("hasAnyRole('USER', 'LIBRARIAN', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks() { ... }

    // Only librarian and admin can create
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody BookRequest request) { ... }

    // Only admin can delete
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) { ... }
}
```

### 3. Password Security

#### Password Hashing (BCrypt)

```java
@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());

        // ✅ Hash password with BCrypt (cost factor 12)
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public boolean validatePassword(String rawPassword, String hashedPassword) {
        // ✅ BCrypt comparison (constant-time)
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
```

**BCrypt Configuration:**

```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);  // Cost factor 12 (2^12 iterations)
}
```

**Password Requirements:**
- Minimum 8 characters
- At least 1 uppercase letter
- At least 1 lowercase letter
- At least 1 digit
- At least 1 special character

### 4. Audit Logging

#### Log Security Events

```java
@Aspect
@Component
public class AuditAspect {

    @AfterReturning(pointcut = "@annotation(auditable)", returning = "result")
    public void logAudit(JoinPoint joinPoint, Auditable auditable, Object result) {
        String username = SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();

        String action = auditable.action();
        String resource = joinPoint.getSignature().getName();

        auditLogRepository.save(AuditLog.builder()
            .username(username)
            .action(action)
            .resource(resource)
            .timestamp(LocalDateTime.now())
            .ipAddress(getCurrentIpAddress())
            .userAgent(getCurrentUserAgent())
            .build());
    }
}
```

**Logged Events:**
- User login/logout
- Book creation/update/deletion
- Failed authentication attempts
- Authorization failures
- Export actions

### 5. CSRF Protection (Session-based alternative)

If using session-based auth instead of JWT:

```java
http.csrf()
    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
```

**JWT-based auth (V2) does NOT need CSRF protection** because:
- Tokens stored in localStorage (not cookies)
- No automatic cookie submission
- Token explicitly added to Authorization header

---

## OWASP Top 10 Protection

### How Books Online Protects Against OWASP Top 10 (2021)

| # | Threat | V1 Protection | V2 Protection |
|---|--------|---------------|---------------|
| **A01** | Broken Access Control | ⚠️ No auth | ✅ JWT + RBAC |
| **A02** | Cryptographic Failures | ⚠️ HTTP (dev) | ✅ HTTPS + encrypted DB |
| **A03** | Injection | ✅ JPA parameterized queries | ✅ Same + prepared statements |
| **A04** | Insecure Design | ✅ Input validation | ✅ Security by design |
| **A05** | Security Misconfiguration | ⚠️ Dev defaults | ✅ Prod hardening |
| **A06** | Vulnerable Components | ✅ Updated dependencies | ✅ Automated scanning |
| **A07** | Authentication Failures | ❌ No auth | ✅ JWT + bcrypt + MFA |
| **A08** | Software/Data Integrity | ✅ Validation | ✅ Code signing + SRI |
| **A09** | Logging Failures | ⚠️ Basic logging | ✅ Comprehensive audit logs |
| **A10** | Server-Side Request Forgery | ✅ No external requests | ✅ URL validation |

### Detailed Protections

#### A03: Injection

**SQL Injection:**
- ✅ JPA/Hibernate parameterized queries
- ✅ No raw SQL string concatenation
- ✅ Criteria API for dynamic queries

**NoSQL Injection:**
- N/A (using SQL database)

**Command Injection:**
- ✅ No system command execution
- ✅ No Runtime.exec() calls

#### A04: Insecure Design

**Input Validation:**
- ✅ Bean Validation (@NotNull, @Size, @Min, @Max)
- ✅ Frontend Zod validation
- ✅ Whitelist approach (allow known-good, reject everything else)

**Business Logic Security:**
- ✅ ISBN uniqueness enforced
- ✅ View count increment (prevent manipulation)
- ✅ Stock quantity validation

#### A06: Vulnerable and Outdated Components

**Dependency Management:**

```xml
<!-- pom.xml -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.2</version>  <!-- Latest stable version -->
</parent>
```

**Dependency Scanning:**

```bash
# Maven dependency check
mvn org.owasp:dependency-check-maven:check

# npm audit (frontend)
npm audit
npm audit fix
```

**Update Strategy:**
- Quarterly dependency updates
- Immediate security patches
- Automated Dependabot alerts (GitHub)

---

## Data Protection

### 1. Data Classification

| Data Type | Classification | Protection Level |
|-----------|----------------|------------------|
| Book metadata (title, author, ISBN) | Public | Low |
| Book prices, stock | Internal | Medium |
| User credentials (V2) | Confidential | High |
| Audit logs (V2) | Confidential | High |

### 2. Data at Rest (V2)

#### Database Encryption

```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/booksonline?ssl=true&sslmode=require
    hikari:
      connection-init-sql: SET SESSION CHARACTERISTICS AS TRANSACTION ISOLATION LEVEL READ COMMITTED
```

**PostgreSQL Encryption (V2):**
- ✅ Transparent Data Encryption (TDE)
- ✅ Encrypted backups
- ✅ Column-level encryption for sensitive fields

#### File System Encryption

- ✅ OS-level disk encryption (LUKS, BitLocker, FileVault)
- ✅ Cover images stored in encrypted volume

### 3. Data in Transit

#### V1 (Development)
- ⚠️ HTTP (localhost only)

#### V2 (Production)
- ✅ TLS 1.2+ enforced
- ✅ Strong cipher suites only
- ✅ HSTS header (HTTP Strict Transport Security)

```java
// V2: Security headers
http.headers()
    .httpStrictTransportSecurity()
        .includeSubDomains(true)
        .maxAgeInSeconds(31536000);  // 1 year
```

### 4. Data Retention

**V1:**
- Books retained indefinitely
- No automated cleanup

**V2:**
- Audit logs retained for 90 days
- Deleted books soft-deleted (marked as deleted, not physically removed)
- GDPR compliance (right to be forgotten)

### 5. Backup and Recovery

**Backup Strategy:**

```bash
# Database backup (daily)
pg_dump -U booksonline_user -d booksonline > backup_$(date +%Y%m%d).sql

# Encrypted backup
pg_dump -U booksonline_user -d booksonline | gzip | openssl enc -aes-256-cbc -salt -out backup_$(date +%Y%m%d).sql.gz.enc
```

**Retention Policy:**
- Daily backups: 7 days
- Weekly backups: 4 weeks
- Monthly backups: 12 months

**Recovery Testing:**
- Quarterly restore test
- Document RTO (Recovery Time Objective): 4 hours
- Document RPO (Recovery Point Objective): 24 hours

---

## API Security

### 1. API Authentication (V2)

```http
GET /api/v1/books HTTP/1.1
Host: api.booksonline.com
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 2. API Rate Limiting (V2)

```java
@Configuration
public class RateLimitConfig {

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.of("bookApi", RateLimiterConfig.custom()
            .limitForPeriod(100)              // 100 requests
            .limitRefreshPeriod(Duration.ofMinutes(1))  // per minute
            .timeoutDuration(Duration.ofSeconds(5))
            .build());
    }
}
```

**Rate Limit Response (429):**

```http
HTTP/1.1 429 Too Many Requests
Content-Type: application/json
Retry-After: 60
X-RateLimit-Limit: 100
X-RateLimit-Remaining: 0
X-RateLimit-Reset: 1675270860

{
  "error": "Rate limit exceeded",
  "message": "Too many requests. Please try again in 60 seconds."
}
```

### 3. API Versioning

- ✅ URI versioning (`/api/v1/`)
- ✅ Backward compatibility maintained
- ✅ Deprecation warnings for old versions

### 4. Content-Type Validation

```java
@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<Book> createBook(@RequestBody BookRequest request) {
    // Only accepts application/json
    // Only produces application/json
}
```

---

## Frontend Security

### 1. XSS Prevention

```jsx
// ✅ SAFE - React escapes by default
<h1>{book.title}</h1>

// ❌ DANGEROUS - Never use unless absolutely necessary
<div dangerouslySetInnerHTML={{ __html: untrustedHTML }} />
```

### 2. Content Security Policy (CSP)

```html
<!-- V2: CSP headers -->
<meta http-equiv="Content-Security-Policy"
      content="
        default-src 'self';
        script-src 'self' 'unsafe-inline' 'unsafe-eval';
        style-src 'self' 'unsafe-inline';
        img-src 'self' data: https:;
        font-src 'self';
        connect-src 'self' http://localhost:8080;
      ">
```

### 3. Secure Storage

**V1:**
- No sensitive data stored client-side

**V2:**
- ✅ JWT stored in httpOnly cookie (preferred) OR localStorage
- ❌ Never store passwords client-side
- ✅ Clear storage on logout

```javascript
// V2: Secure JWT storage
// Option 1: httpOnly cookie (recommended)
// - Set by backend, not accessible via JavaScript
// - Automatic inclusion in requests

// Option 2: localStorage (with caution)
localStorage.setItem('jwt', token);  // ⚠️ Accessible to XSS

// Clear on logout
localStorage.removeItem('jwt');
```

### 4. Dependencies Security

```bash
# Check for vulnerabilities
npm audit

# Fix vulnerabilities
npm audit fix

# Update dependencies
npm update
```

**Automated Scanning:**
- Dependabot alerts (GitHub)
- Snyk integration (optional)

---

## Database Security

### 1. Connection Security

#### V1 Development (H2)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:booksonline
    username: sa
    password:  # Empty (dev only)
```

#### V2 Production (PostgreSQL)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/booksonline?ssl=true&sslmode=require
    username: booksonline_user
    password: ${DB_PASSWORD}  # Environment variable
    hikari:
      maximum-pool-size: 10
      connection-timeout: 30000
```

**Environment Variables (not hardcoded):**

```bash
export DB_PASSWORD=<strong-random-password>
export JWT_SECRET=<256-bit-secret-key>
```

### 2. Least Privilege

```sql
-- V2: Database user with minimal permissions
CREATE USER booksonline_user WITH PASSWORD 'strong_password';

-- Grant only necessary permissions
GRANT SELECT, INSERT, UPDATE, DELETE ON books TO booksonline_user;
GRANT USAGE, SELECT ON SEQUENCE books_id_seq TO booksonline_user;

-- No DROP, CREATE, ALTER permissions
```

### 3. Connection Pooling

```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 10        # Max connections
      minimum-idle: 5              # Min idle connections
      connection-timeout: 30000    # 30 seconds
      idle-timeout: 600000         # 10 minutes
      max-lifetime: 1800000        # 30 minutes
```

### 4. SQL Injection Prevention

- ✅ JPA/Hibernate parameterized queries
- ✅ No dynamic SQL string concatenation
- ✅ PreparedStatement under the hood

---

## Deployment Security

### 1. Docker Security

#### Dockerfile Best Practices

```dockerfile
# Use official base image
FROM eclipse-temurin:17-jre-alpine

# Run as non-root user
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy only necessary files
COPY target/booksonline-backend.jar app.jar

# Expose port (non-privileged)
EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

#### Docker Compose Security

```yaml
version: '3.8'

services:
  backend:
    image: booksonline-backend:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_PASSWORD=${DB_PASSWORD}  # From .env file
    ports:
      - "8080:8080"
    networks:
      - booksonline-network
    restart: unless-stopped
    security_opt:
      - no-new-privileges:true  # Prevent privilege escalation
    read_only: true             # Read-only filesystem
    tmpfs:
      - /tmp                    # Writable temp directory

networks:
  booksonline-network:
    driver: bridge
```

### 2. Environment Variables

```bash
# .env file (never commit to git)
DB_PASSWORD=strong_random_password_here
JWT_SECRET=256_bit_secret_key_here
FRONTEND_URL=https://booksonline.com
```

**.gitignore:**

```
.env
.env.local
.env.*.local
application-prod.yml
keystore.p12
```

### 3. Secrets Management (V2)

```yaml
# docker-compose with secrets
services:
  backend:
    secrets:
      - db_password
      - jwt_secret

secrets:
  db_password:
    file: ./secrets/db_password.txt
  jwt_secret:
    file: ./secrets/jwt_secret.txt
```

### 4. Reverse Proxy (Production)

```nginx
# nginx.conf
server {
    listen 443 ssl http2;
    server_name api.booksonline.com;

    ssl_certificate /etc/letsencrypt/live/api.booksonline.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/api.booksonline.com/privkey.pem;

    # Security headers
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-Frame-Options "DENY" always;
    add_header X-XSS-Protection "1; mode=block" always;

    # Proxy to backend
    location /api/ {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

---

## Security Testing

### 1. Automated Security Testing

#### Backend Security Tests

```java
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSqlInjectionPrevention() throws Exception {
        String maliciousInput = "'; DROP TABLE books; --";

        mockMvc.perform(get("/api/v1/books/search")
                .param("title", maliciousInput))
            .andExpect(status().isOk())  // Should not crash
            .andExpect(jsonPath("$.content").isArray());  // Should return empty array
    }

    @Test
    public void testXssPrevention() throws Exception {
        String xssPayload = "<script>alert('XSS')</script>";

        BookRequest request = new BookRequest();
        request.setTitle(xssPayload);
        // ... set other required fields

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated());

        // Verify stored value is escaped
        Book savedBook = bookRepository.findAll().get(0);
        assertNotEquals(xssPayload, savedBook.getTitle());
        assertTrue(savedBook.getTitle().contains("&lt;script&gt;"));
    }

    @Test
    public void testInputValidation() throws Exception {
        BookRequest request = new BookRequest();
        // Missing required fields

        mockMvc.perform(post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors").isArray());
    }
}
```

### 2. OWASP ZAP Scanning

```bash
# Run OWASP ZAP against API
docker run -t owasp/zap2docker-stable zap-baseline.py \
    -t http://localhost:8080/api/v1 \
    -r zap_report.html
```

### 3. Dependency Vulnerability Scanning

```bash
# Maven OWASP Dependency Check
mvn org.owasp:dependency-check-maven:check

# npm audit
npm audit

# Snyk (optional)
snyk test
```

### 4. Penetration Testing Checklist

- [ ] SQL Injection attempts
- [ ] XSS payload injection
- [ ] Authentication bypass (V2)
- [ ] Authorization bypass (V2)
- [ ] CSRF attacks (V2 session-based)
- [ ] Rate limit testing
- [ ] Input validation bypass
- [ ] Error message information disclosure
- [ ] Directory traversal
- [ ] File upload vulnerabilities (if implemented)

---

## Incident Response

### 1. Security Incident Plan

#### Detection

- **Monitoring**: Log analysis, anomaly detection
- **Alerts**: Failed authentication (V2), unusual API patterns, error spikes

#### Response Team

- **Incident Commander**: Tech Lead
- **Security Lead**: DevOps Engineer
- **Communications**: Product Owner

#### Response Steps

1. **Identify**: What type of incident? (Data breach, DDoS, unauthorized access)
2. **Contain**: Isolate affected systems, block malicious IPs
3. **Eradicate**: Remove malicious code, patch vulnerabilities
4. **Recover**: Restore from backups, verify system integrity
5. **Lessons Learned**: Post-mortem report, update security measures

### 2. Security Contacts

- **Report vulnerabilities**: security@booksonline.com
- **Response time**: 24 hours acknowledgment, 72 hours resolution

### 3. Disclosure Policy

- Responsible disclosure: 90-day window before public disclosure
- Acknowledgment of security researchers
- Hall of fame for contributors

---

## Summary

### V1 Security Status (Current)

| Category | Status | Notes |
|----------|--------|-------|
| Input Validation | ✅ Good | Bean Validation + Zod |
| SQL Injection Protection | ✅ Good | JPA parameterized queries |
| XSS Protection | ✅ Good | React JSX + Jackson escaping |
| CORS | ✅ Configured | Localhost only |
| HTTPS | ⚠️ HTTP (dev) | Localhost development |
| Authentication | ❌ None | Open access |
| Authorization | ❌ None | No RBAC |
| Audit Logging | ❌ None | No security events logged |
| Rate Limiting | ❌ None | No throttling |

**V1 Verdict:** ✅ **Safe for development/demo, NOT production-ready**

### V2 Security Roadmap

| Enhancement | Priority | Estimated Effort |
|-------------|----------|------------------|
| JWT Authentication | High | 2 weeks |
| RBAC Authorization | High | 1 week |
| HTTPS Enforcement | High | 3 days |
| Rate Limiting | Medium | 1 week |
| Audit Logging | Medium | 1 week |
| Password Security (BCrypt) | High | 3 days |
| Database Encryption | Medium | 1 week |
| Dependency Scanning | Low | 2 days |

**Total V2 Security Effort:** ~7-8 weeks

### Security Checklist (Production Deployment)

#### Pre-Production

- [ ] Enable HTTPS (TLS 1.2+)
- [ ] Implement JWT authentication
- [ ] Add RBAC authorization
- [ ] Enable rate limiting
- [ ] Configure audit logging
- [ ] Encrypt database connections
- [ ] Use strong passwords (BCrypt)
- [ ] Scan dependencies for vulnerabilities
- [ ] Perform penetration testing
- [ ] Update CORS allowed origins
- [ ] Enable security headers (HSTS, CSP, X-Frame-Options)
- [ ] Remove debug/dev endpoints
- [ ] Configure secure session management
- [ ] Set up monitoring and alerts
- [ ] Backup and disaster recovery plan

#### Post-Production

- [ ] Monitor security logs daily
- [ ] Apply security patches within 7 days
- [ ] Quarterly dependency updates
- [ ] Annual penetration testing
- [ ] Incident response drills
- [ ] Security training for team

---

**Security is a continuous process, not a one-time implementation. Stay vigilant!**
