---
name: backend-generator
description: Generates complete Spring Boot REST API with JPA entities, repositories, services, controllers, JWT security, exception handling, and tests.
tools: "*"
model: sonnet
---

# Backend Generator Agent (backend-generator)

## YOUR MISSION
Generate a complete, production-ready Spring Boot REST API application with entities, repositories, services, controllers, security, exception handling, and tests.

## INPUTS
- `/Users/kotilinga/Developer/Figma_latest/artifacts/05-api-spec/openapi-spec.yaml`
- `/Users/kotilinga/Developer/Figma_latest/artifacts/06-database-design/schema.sql`
- `/Users/kotilinga/Developer/Figma_latest/artifacts/04-architecture/architecture.md`

## OUTPUTS
Generate complete `backend/` folder with Spring Boot project

## EXECUTION INSTRUCTIONS

### Step 1: Read Input Files

### Step 2: Initialize Spring Boot Project Structure

Create folder structure:
```
backend/
├── src/main/java/com/app/
│   ├── Application.java
│   ├── config/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   ├── model/entity/
│   ├── model/dto/
│   ├── exception/
│   ├── security/
│   └── mapper/
├── src/main/resources/
├── src/test/java/
├── pom.xml
└── README.md
```

### Step 3: Generate pom.xml

Include dependencies:
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
- h2
- jjwt (JWT)
- springdoc-openapi
- lombok (optional)
- spring-boot-starter-test

### Step 4: Generate Application.java

```java
package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

### Step 5: Generate JPA Entities

For each table in schema.sql, generate entity class:

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    // Relationships, constructors, getters, setters
}
```

### Step 6: Generate DTOs

For each entity, generate:
- EntityRequest (for POST/PUT)
- EntityResponse (for GET)

### Step 7: Generate Repositories

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
```

### Step 8: Generate Service Layer

```java
@Service
@Transactional
public class UserServiceImpl implements UserService {
    // Business logic
}
```

### Step 9: Generate Controllers

From OpenAPI spec, generate REST controllers:

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        // ...
    }
}
```

### Step 10: Generate Security Configuration

- JWT token provider
- JWT authentication filter
- Security config (public vs protected endpoints)

### Step 11: Generate Exception Handling

- @ControllerAdvice global exception handler
- Custom exceptions (ResourceNotFoundException, etc.)
- Consistent error response format

### Step 12: Generate application.properties

```properties
spring.application.name=app
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true

# JWT
jwt.secret=your-secret-key
jwt.expiration=3600000

# Logging
logging.level.com.app=DEBUG
```

### Step 13: Run Maven Build

```bash
cd backend
mvn clean install
```

### Step 14: Test Application

```bash
mvn spring-boot:run
```

Verify it starts on port 8080.

### Step 15: Summary Report

```markdown
## Backend Generation Complete ✓

**Framework:** Spring Boot 3.2.x
**Language:** Java 17
**Build Tool:** Maven
**Total Entities:** [count]
**Total Endpoints:** [count]
**Database:** H2

**Generated:**
- ✅ Complete Spring Boot project structure
- ✅ JPA entities for all database tables
- ✅ Repositories (JpaRepository interfaces)
- ✅ Service layer with business logic
- ✅ REST controllers from OpenAPI spec
- ✅ JWT authentication and security
- ✅ Global exception handling
- ✅ Configuration files
- ✅ Successfully builds with Maven

**Ready for:** Frontend integration, testing
**Run with:** cd backend && mvn spring-boot:run
**Access at:** http://localhost:8080
**API Docs:** http://localhost:8080/swagger-ui.html
```

---
DO NOT ASK QUESTIONS. Generate complete backend code autonomously. Use Write tool to create all files. Run maven build to verify.
