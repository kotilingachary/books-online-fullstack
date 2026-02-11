# Books Online - Test Plan

## Test Generation Summary

**Project:** Books Online - Book Management System
**Generated:** 2026-02-11
**Test Agent:** Test Generator (09-test-generator)
**Status:** Tests Generated ✅

---

## 1. Test Strategy

### 1.1 Testing Pyramid
```
         /\
        /E2E\          - 10% E2E tests (user flows)
       /------\
      /  API   \       - 30% Integration tests (controller layer)
     /----------\
    /   UNIT     \     - 60% Unit tests (service, repository, components)
   /--------------\
```

### 1.2 Test Coverage Goals
- **Backend**: Minimum 80% code coverage
- **Frontend**: Minimum 70% code coverage
- **E2E**: Cover all critical user flows

### 1.3 Testing Types
1. **Unit Tests** - Test individual components/functions in isolation
2. **Integration Tests** - Test API endpoints and component integration
3. **E2E Tests** - Test complete user workflows
4. **Manual Tests** - Exploratory testing for UX

---

## 2. Backend Tests (Java/JUnit 5)

### 2.1 Service Layer Tests
**File:** `backend/src/test/java/com/booksonline/service/impl/BookServiceImplTest.java`
**Test Count:** 18 tests
**Framework:** JUnit 5 + Mockito

#### Test Coverage:

**Create Operations (3 tests)**
- ✅ Create book successfully
- ✅ Create book with duplicate ISBN throws DuplicateIsbnException
- ✅ Create book validates required fields

**Read Operations (4 tests)**
- ✅ Get book by ID successfully
- ✅ Get book by ID not found throws BookNotFoundException
- ✅ Get book by ID increments view count
- ✅ Get all books returns paginated results

**Update Operations (2 tests)**
- ✅ Update book successfully
- ✅ Update book not found throws BookNotFoundException

**Delete Operations (2 tests)**
- ✅ Delete book successfully
- ✅ Delete book not found throws BookNotFoundException

**Search Operations (2 tests)**
- ✅ Search books with query parameter
- ✅ Search books with genre filter

**Duplicate Operations (2 tests)**
- ✅ Duplicate book successfully
- ✅ Duplicate book not found throws BookNotFoundException

**Business Logic (3 tests)**
- ✅ Stock quantity affects availability
- ✅ View count increment is atomic
- ✅ Validation rules enforced

### 2.2 Controller Layer Tests
**File:** `backend/src/test/java/com/booksonline/controller/BookControllerTest.java`
**Test Count:** 16 tests
**Framework:** Spring MockMvc + @WebMvcTest

#### Test Coverage:

**POST /api/v1/books (3 tests)**
- ✅ Create book returns 201 Created
- ✅ Validation error returns 400 Bad Request
- ✅ Duplicate ISBN returns 409 Conflict

**GET /api/v1/books (2 tests)**
- ✅ Get all books with pagination returns 200 OK
- ✅ Pagination parameters respected

**GET /api/v1/books/{id} (2 tests)**
- ✅ Get book by ID returns 200 OK
- ✅ Book not found returns 404 Not Found

**PUT /api/v1/books/{id} (2 tests)**
- ✅ Update book returns 200 OK
- ✅ Update non-existent book returns 404 Not Found

**DELETE /api/v1/books/{id} (2 tests)**
- ✅ Delete book returns 204 No Content
- ✅ Delete non-existent book returns 404 Not Found

**GET /api/v1/books/search (3 tests)**
- ✅ Search with query parameter
- ✅ Search by genre filter
- ✅ Search with year range

**POST /api/v1/books/{id}/duplicate (2 tests)**
- ✅ Duplicate book returns 201 Created
- ✅ Duplicate non-existent book returns 404 Not Found

**CORS Tests (1 test)**
- ✅ CORS preflight request allowed

---

## 3. Frontend Tests (Vitest/React Testing Library)

### 3.1 Test Configuration
**Files:**
- `frontend/vitest.config.js` - Vitest configuration
- `frontend/src/test/setup.js` - Test setup with jsdom

**Test Dependencies Added:**
- vitest ^1.2.0
- @testing-library/react ^14.1.2
- @testing-library/jest-dom ^6.2.0
- @testing-library/user-event ^14.5.2
- @vitest/ui ^1.2.0
- @vitest/coverage-v8 ^1.2.0
- jsdom ^23.2.0

### 3.2 Component Tests
**File:** `frontend/src/components/common/__tests__/Button.test.jsx`
**Test Count:** 8 tests
**Framework:** Vitest + React Testing Library

#### Button Component Coverage:
- ✅ Renders with correct text
- ✅ Calls onClick handler when clicked
- ✅ Is disabled when disabled prop is true
- ✅ Applies primary variant by default
- ✅ Applies secondary variant when specified
- ✅ Applies danger variant when specified
- ✅ Applies custom className
- ✅ Does not call onClick when disabled

### 3.3 Additional Tests to Generate
**Recommended component tests:**
- Input.test.jsx - Form input validation
- Card.test.jsx - Card rendering
- Modal.test.jsx - Modal open/close behavior
- Pagination.test.jsx - Pagination navigation

**Recommended page tests:**
- BooksListPage.test.jsx - Book list rendering and filtering
- AddBookPage.test.jsx - Form submission
- SearchPage.test.jsx - Search functionality

---

## 4. E2E Tests (Playwright)

### 4.1 Test Scenarios (To Be Generated)

**User Story: Browse Books (US-001)**
- Navigate to books list
- Verify 20 sample books displayed
- Verify pagination controls
- Test page navigation

**User Story: Add New Book (US-007)**
- Navigate to Add Book page
- Fill required fields
- Submit form
- Verify success message
- Verify book appears in list

**User Story: Search Books (US-011)**
- Navigate to Search page
- Enter search term
- Verify filtered results
- Clear search
- Verify all books shown

**User Story: Edit Book (US-017)**
- Click Edit button on a book
- Modify title
- Save changes
- Verify book updated

**User Story: Delete Book (US-029-031)**
- Click Delete button
- Verify confirmation modal
- Confirm deletion
- Verify book removed from list

---

## 5. Test Execution

### 5.1 Backend Tests
```bash
# Run all backend tests
cd backend
JAVA_HOME=/opt/homebrew/Cellar/openjdk@21/21.0.8/libexec/openjdk.jdk/Contents/Home mvn test

# Run with coverage
JAVA_HOME=/opt/homebrew/Cellar/openjdk@21/21.0.8/libexec/openjdk.jdk/Contents/Home mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### 5.2 Frontend Tests
```bash
# Install test dependencies
cd frontend
npm install

# Run all tests
npm test

# Run with coverage
npm run test:coverage

# Run with UI
npm run test -- --ui
```

### 5.3 E2E Tests
```bash
# Install Playwright (when generated)
cd tests/e2e
npm install
npx playwright install

# Run E2E tests
npx playwright test

# Run with UI
npx playwright test --ui
```

---

## 6. Test Data

### 6.1 Sample Books (20 books preloaded)
- Programming: 6 books (Clean Code, Design Patterns, etc.)
- Science Fiction: 3 books (Dune, The Martian, Neuromancer)
- Fiction: 2 books (To Kill a Mockingbird, 1984)
- Business: 2 books (Good to Great, The Lean Startup)
- Self-Help: 2 books (Atomic Habits, 7 Habits)
- History: 2 books (Sapiens, Guns of August)
- Biography: 1 book (Steve Jobs)

### 6.2 Test Users
**V1:** No authentication (all endpoints open)
**V2:** JWT-based authentication (to be implemented)

---

## 7. Quality Gates

### 7.1 Continuous Integration
**All tests must pass before:**
- Merging pull requests
- Deploying to staging
- Deploying to production

### 7.2 Coverage Requirements
- **Backend:** Minimum 80% line coverage
- **Frontend:** Minimum 70% line coverage
- **Critical paths:** 100% coverage

### 7.3 Performance Requirements
- Unit tests: Complete in <30 seconds
- Integration tests: Complete in <2 minutes
- E2E tests: Complete in <5 minutes

---

## 8. Testing Tools & Dependencies

### 8.1 Backend
| Tool | Version | Purpose |
|------|---------|---------|
| JUnit 5 | 5.x | Test framework |
| Mockito | 5.x | Mocking framework |
| Spring MockMvc | 6.x | API testing |
| H2 Database | 2.x | Test database |
| JaCoCo | 0.8.x | Coverage reporting |

### 8.2 Frontend
| Tool | Version | Purpose |
|------|---------|---------|
| Vitest | 1.2.0 | Test framework |
| React Testing Library | 14.1.2 | Component testing |
| jest-dom | 6.2.0 | DOM matchers |
| user-event | 14.5.2 | User interaction |
| jsdom | 23.2.0 | DOM environment |

### 8.3 E2E
| Tool | Version | Purpose |
|------|---------|---------|
| Playwright | 1.x | Browser automation |
| - | - | (To be configured) |

---

## 9. Test Reports

### 9.1 Backend Coverage Report
Location: `backend/target/site/jacoco/index.html`
Format: HTML report with line/branch coverage

### 9.2 Frontend Coverage Report
Location: `frontend/coverage/index.html`
Format: HTML report with statement/branch/function coverage

### 9.3 Test Results
- Console output with test counts
- JUnit XML reports for CI integration
- HTML reports for human review

---

## 10. Next Steps

### 10.1 Immediate
- [x] Generate backend unit tests
- [x] Generate backend integration tests
- [x] Configure frontend test environment
- [x] Generate sample frontend component tests
- [ ] Run backend tests to verify
- [ ] Run frontend tests to verify

### 10.2 Future Enhancements
- [ ] Generate complete frontend component test suite
- [ ] Generate frontend page integration tests
- [ ] Setup Playwright and generate E2E tests
- [ ] Configure CI/CD pipeline with test automation
- [ ] Add visual regression tests
- [ ] Add performance tests
- [ ] Add accessibility tests (axe-core)
- [ ] Add API contract tests

---

## 11. Test Maintenance

### 11.1 Best Practices
- Keep tests independent and isolated
- Use descriptive test names
- Follow AAA pattern (Arrange, Act, Assert)
- Mock external dependencies
- Test both happy paths and edge cases
- Maintain test data fixtures
- Update tests when requirements change

### 11.2 Code Reviews
All test code should be reviewed for:
- Correctness of assertions
- Coverage of edge cases
- Readability and maintainability
- Performance (avoid slow tests)
- Proper use of mocking

---

**Test Plan Version:** 1.0
**Last Updated:** 2026-02-11
**Generated by:** Multi-Agent SDLC System - Test Generator Agent
