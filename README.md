# Figma-to-Production Multi-Agent System

## Overview
An intelligent multi-agent system that transforms Figma wireframes into a fully deployed application with React frontend, Java REST API backend, and H2 database.

---

## Quick Startup (running app)

The repo currently contains a generated **Books Online** app. To bring it up locally:

### Prerequisites
- **JDK 21+** (tested on JDK 25 with Lombok 1.18.38)
- **Maven 3.9+**
- **Node.js 18+** (tested on 22.19.0)

### Start backend (port 8080)
```bash
cd backend
mvn spring-boot:run
```
Wait for `Started Application in N seconds`.

### Start frontend (port 5173)
```bash
cd frontend
npm install      # first time only
npm run dev
```

### Or start both at once
```bash
./start.sh       # Mac/Linux
```

### Verify it's up
```bash
curl http://localhost:8080/api/v1/books          # 200, JSON list of 20 seed books
curl -I http://localhost:5173/                   # 200, frontend index.html
```

---

## Access points

| Surface | URL | Notes |
|---|---|---|
| Frontend SPA | http://localhost:5173 | Main app — books list, details, add/edit, search |
| Backend REST API | http://localhost:8080/api/v1/books | Base path for all 8 endpoints |
| Swagger UI | http://localhost:8080/swagger-ui.html | Interactive API explorer |
| OpenAPI spec | http://localhost:8080/v3/api-docs | Raw OpenAPI 3.0 JSON |
| H2 console | http://localhost:8080/h2-console | DB browser — JDBC `jdbc:h2:file:./data/booksdb`, user `sa`, password blank |

### Key API endpoints
| Method | Path | Description |
|---|---|---|
| GET | `/api/v1/books` | List all (paginated) |
| GET | `/api/v1/books/{id}` | Get by ID (increments view count) |
| POST | `/api/v1/books` | Create |
| PUT | `/api/v1/books/{id}` | Update |
| DELETE | `/api/v1/books/{id}` | Delete |
| GET | `/api/v1/books/search` | Advanced search (17+ filter params) |
| POST | `/api/v1/books/{id}/duplicate` | Clone a book |
| GET | `/api/v1/books/{id}/export` | Export book data |

### Frontend env
`frontend/.env`:
```
VITE_API_URL=http://localhost:8080/api/v1
VITE_APP_ENV=development
```

---

## Common commands

### Backend
```bash
cd backend
mvn spring-boot:run                            # run
mvn test                                       # all tests
mvn test -Dtest=BookControllerTest             # one test class
mvn clean test jacoco:report                   # coverage → target/site/jacoco/
mvn clean package -DskipTests                  # build JAR
```

### Frontend
```bash
cd frontend
npm run dev                                    # dev server (HMR)
npm run build                                  # production build → dist/
npm run lint                                   # ESLint (zero-warnings policy)
npm test                                       # Vitest unit tests
npm test -- Button.test.jsx                    # one test file
npm run test:coverage                          # coverage
```

### Stop running servers
```bash
pkill -f spring-boot:run                       # backend
pkill -f vite                                  # frontend
```

---

## Troubleshooting

| Symptom | Fix |
|---|---|
| `Fatal error compiling: TypeTag :: UNKNOWN` | Lombok < 1.18.36 + JDK 24/25. Bump `<lombok.version>` in `backend/pom.xml` to **1.18.38**. |
| `JAVA_HOME` points to JDK 8/11/17 but project needs 21+ | `export JAVA_HOME=$(/usr/libexec/java_home -v 21)` |
| Port 8080 already in use | `lsof -ti:8080 \| xargs kill -9` |
| Port 5173 already in use | Vite auto-picks next free port; or `lsof -ti:5173 \| xargs kill -9` |
| Frontend can't reach backend (CORS / network) | Confirm `VITE_API_URL` in `frontend/.env` matches backend host:port |
| H2 console login fails | JDBC URL must be exactly `jdbc:h2:file:./data/booksdb`, user `sa`, password blank |

---

## Agent Architecture

### Execution Flow
```
┌─────────────────────────┐
│  wireframe-analyzer     │  Analyzes Figma wireframes, extracts UI components
└───────────┬─────────────┘
            │
┌───────────▼─────────────┐
│  prd-generator          │  Creates Product Requirements Document
└───────────┬─────────────┘
            │
    ┌───────┴────────┐
    │                │
┌───▼──────────┐ ┌──▼─────────────────┐
│ user-stories │ │ architecture-      │  Generates user stories & system architecture
│ -generator   │ │ designer           │
└──────────────┘ └───┬────────────────┘
                     │
            ┌────────┴────────┐
            │                 │
    ┌───────▼──────────┐ ┌───▼──────────────┐
    │ api-spec-        │ │ database-        │  Generates API specs & database schema
    │ generator        │ │ designer         │
    └───────┬──────────┘ └───┬──────────────┘
            │                │
            └────────┬───────┘
                     │
            ┌────────┴────────┐
            │                 │
    ┌───────▼──────────┐ ┌───▼──────────────┐
    │ backend-         │ │ frontend-        │  Generates complete code
    │ generator        │ │ generator        │
    └───────┬──────────┘ └───┬──────────────┘
            │                │
            └────────┬───────┘
                     │
            ┌────────▼────────┐
            │ test-generator  │  Generates comprehensive test suites
            └────────┬────────┘
                     │
            ┌────────▼────────┐
            │ deployment-     │  Deploys application
            │ agent           │
            └─────────────────┘
```

## Agent Specifications

### 1. wireframe-analyzer
**Purpose:** Analyzes Figma wireframes to extract UI components, flows, and data requirements

**Input:**
- `wireframes/` folder containing wireframe images/screenshots

**Output:**
- `artifacts/01-wireframe-analysis/wireframe-analysis.json`
- `artifacts/01-wireframe-analysis/component-hierarchy.md`
- `artifacts/01-wireframe-analysis/user-flows.md`

**Responsibilities:**
- Parse wireframe images
- Identify UI components (buttons, forms, tables, cards, navigation)
- Map screen transitions and navigation flows
- Extract text labels, placeholders, and content
- Identify data requirements from UI elements
- Create component hierarchy

---

### 2. prd-generator
**Purpose:** Creates comprehensive Product Requirements Document

**Input:**
- `artifacts/01-wireframe-analysis/wireframe-analysis.json`

**Output:**
- `artifacts/02-prd/PRD.md`
- `artifacts/02-prd/features.json`
- `artifacts/02-prd/requirements.json`

**Responsibilities:**
- Define product vision and goals
- List functional requirements based on wireframes
- Define non-functional requirements (performance, security, scalability)
- Define success metrics and KPIs
- Identify constraints and assumptions
- Define user personas

---

### 3. user-stories-generator
**Purpose:** Generates user stories with acceptance criteria

**Input:**
- `artifacts/02-prd/PRD.md`

**Output:**
- `artifacts/03-user-stories/user-stories.json`
- `artifacts/03-user-stories/acceptance-criteria.md`
- `artifacts/03-user-stories/epics.json`

**Responsibilities:**
- Generate user stories (As a... I want... So that...)
- Create detailed acceptance criteria for each story
- Prioritize stories (MoSCoW: Must/Should/Could/Won't)
- Estimate story points
- Group stories into epics
- Define story dependencies

---

### 4. architecture-designer
**Purpose:** Designs complete system architecture

**Input:**
- `artifacts/02-prd/PRD.md`
- `artifacts/03-user-stories/user-stories.json`

**Output:**
- `artifacts/04-architecture/architecture.md`
- `artifacts/04-architecture/component-diagram.md`
- `artifacts/04-architecture/tech-stack.json`
- `artifacts/04-architecture/deployment-architecture.md`

**Responsibilities:**
- Design overall system architecture
- Define tech stack (React, Java Spring Boot, H2)
- Design component structure (frontend & backend)
- Define communication patterns (REST APIs)
- Design security architecture (authentication, authorization)
- Design scalability approach
- Define error handling strategy
- Design logging and monitoring approach

---

### 5. api-spec-generator
**Purpose:** Generates OpenAPI specification for REST APIs

**Input:**
- `artifacts/04-architecture/architecture.md`
- `artifacts/03-user-stories/user-stories.json`

**Output:**
- `artifacts/05-api-spec/openapi-spec.yaml`
- `artifacts/05-api-spec/api-documentation.md`
- `artifacts/05-api-spec/endpoints-summary.json`

**Responsibilities:**
- Generate complete OpenAPI 3.0 specification
- Define all REST endpoints (GET, POST, PUT, DELETE, PATCH)
- Define request/response models and schemas
- Define error responses (4xx, 5xx)
- Add authentication/authorization specifications
- Add validation rules for request bodies
- Document rate limiting and pagination
- Generate human-readable API documentation

---

### 6. database-designer
**Purpose:** Designs database schema and migrations

**Input:**
- `artifacts/04-architecture/architecture.md`
- `artifacts/05-api-spec/openapi-spec.yaml`

**Output:**
- `artifacts/06-database-design/schema.sql`
- `artifacts/06-database-design/er-diagram.md`
- `artifacts/06-database-design/migrations/`
- `artifacts/06-database-design/seed-data.sql`

**Responsibilities:**
- Design normalized database schema (3NF)
- Create entity-relationship diagrams
- Define tables, columns, and data types
- Define relationships (1-to-1, 1-to-many, many-to-many)
- Define constraints (PK, FK, UNIQUE, NOT NULL, CHECK)
- Create indexes for performance optimization
- Generate migration scripts
- Create seed data for development/testing
- Add audit columns (created_at, updated_at)

---

### 7. backend-generator
**Purpose:** Generates complete Java Spring Boot REST API

**Input:**
- `artifacts/05-api-spec/openapi-spec.yaml`
- `artifacts/06-database-design/schema.sql`
- `artifacts/04-architecture/architecture.md`

**Output:**
- `backend/` - Complete Java Spring Boot project

**Structure:**
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/app/
│   │   │   ├── Application.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── service/
│   │   │   ├── repository/
│   │   │   ├── model/entity/
│   │   │   ├── model/dto/
│   │   │   ├── exception/
│   │   │   ├── mapper/
│   │   │   └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── schema.sql
│   └── test/
├── pom.xml (or build.gradle)
└── README.md
```

**Responsibilities:**
- Generate Spring Boot project structure
- Generate JPA entities from database schema
- Generate DTOs for API requests/responses
- Generate repositories (JpaRepository interfaces)
- Generate service layer with business logic
- Generate REST controllers from OpenAPI spec
- Add global exception handling (@ControllerAdvice)
- Add validation (@Valid, custom validators)
- Add logging (SLF4J)
- Configure H2 database connection
- Add CORS configuration
- Add API documentation (Springdoc OpenAPI)
- Generate unit tests for services
- Generate integration tests for controllers

---

### 8. frontend-generator
**Purpose:** Generates complete React application

**Input:**
- `artifacts/05-api-spec/openapi-spec.yaml`
- `artifacts/01-wireframe-analysis/wireframe-analysis.json`
- `artifacts/04-architecture/architecture.md`

**Output:**
- `frontend/` - Complete React project

**Structure:**
```
frontend/
├── public/
├── src/
│   ├── components/
│   │   ├── common/
│   │   ├── layout/
│   │   └── features/
│   ├── pages/
│   ├── services/
│   │   └── api/
│   ├── hooks/
│   ├── context/
│   ├── utils/
│   ├── styles/
│   ├── types/
│   ├── App.jsx
│   └── main.jsx
├── package.json
├── vite.config.js
└── README.md
```

**Responsibilities:**
- Generate React project (using Vite)
- Generate reusable UI components based on wireframes
- Generate page components with routing
- Generate API client services (axios/fetch)
- Add state management (Context API or Zustand)
- Add routing (React Router v6)
- Add form validation (React Hook Form + Zod)
- Add error boundary and error handling
- Add loading states and skeletons
- Style components (Tailwind CSS or styled-components)
- Add responsive design
- Generate component tests (Vitest + React Testing Library)
- Add TypeScript types from API spec

---

### 9. test-generator
**Purpose:** Generates comprehensive test suites

**Input:**
- `backend/` - Generated backend code
- `frontend/` - Generated frontend code
- `artifacts/03-user-stories/user-stories.json`

**Output:**
- `backend/src/test/` - Backend tests
- `frontend/src/__tests__/` - Frontend tests
- `tests/e2e/` - End-to-end tests
- `tests/test-plan.md`
- `tests/coverage-report/`

**Responsibilities:**
- Generate unit tests for backend services (JUnit 5, Mockito)
- Generate integration tests for REST controllers (MockMvc)
- Generate repository tests (@DataJpaTest)
- Generate unit tests for React components
- Generate integration tests for React pages
- Generate custom hook tests
- Generate E2E tests for user flows (Playwright or Cypress)
- Generate test data fixtures
- Add test coverage reporting (JaCoCo for Java, Vitest coverage)
- Generate performance tests (optional)
- Create test documentation

---

### 10. deployment-agent
**Purpose:** Prepares application for deployment

**Input:**
- `backend/` - Generated backend
- `frontend/` - Generated frontend
- `artifacts/06-database-design/schema.sql`

**Output:**
- `deployment/Dockerfile.backend`
- `deployment/Dockerfile.frontend`
- `deployment/docker-compose.yml`
- `deployment/kubernetes/` (optional)
- `deployment/.github/workflows/` (CI/CD)
- `deployment/deployment-guide.md`

**Responsibilities:**
- Create Dockerfiles for backend and frontend
- Create docker-compose for local development
- Generate Kubernetes manifests (Deployment, Service, Ingress)
- Create CI/CD pipeline (GitHub Actions)
- Add health check endpoints
- Add readiness/liveness probes
- Configure environment variables
- Add monitoring setup (Prometheus, Grafana - optional)
- Generate deployment scripts
- Create deployment documentation
- Add rollback strategy

---

### 11. orchestrator
**Purpose:** Coordinates all agents and manages the workflow

**Input:**
- User command to start the pipeline

**Output:**
- Execution logs
- Progress tracking
- Summary report

**Responsibilities:**
- Execute agents in correct dependency order
- Pass outputs from one agent as inputs to next
- Handle errors and provide retry logic
- Validate outputs at each stage
- Track overall progress
- Generate summary report with metrics
- Allow selective re-execution of specific agents
- Provide rollback capability

---

## Usage

### Quick Start
```bash
# 1. Place your Figma wireframes in the wireframes/ folder
cp /path/to/wireframes/*.png wireframes/

# 2. Run the orchestrator agent with Claude Code
# Simply type: @orchestrator
# Or: "Run the orchestrator agent"

# 3. Find your generated application:
# - PRD and docs: artifacts/
# - Backend: backend/
# - Frontend: frontend/
# - Tests: tests/
# - Deployment: deployment/
```

### Running Individual Agents
Each agent can be run independently for iterative development:

```bash
# Run specific agent via Claude Code
# Example: "Run the prd-generator agent"
```

### Configuration
Edit `.agents/config.json` to customize:
- Tech stack versions
- Code style preferences
- Testing framework choices
- Deployment target (Docker, Kubernetes, Cloud)

---

## Agent Implementation

Each agent is implemented as a specialized Claude Code Task with:
- Clear input/output contracts
- Validation criteria
- Error handling
- Progress reporting
- Rollback capability

Agents are defined in `.agents/` directory with detailed prompts.

---

## Tech Stack

### Frontend
- React 18+
- Vite
- React Router v6
- React Hook Form + Zod
- Axios
- Tailwind CSS
- Vitest + React Testing Library

### Backend
- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- H2 Database
- Maven/Gradle
- Springdoc OpenAPI
- JUnit 5 + Mockito

### DevOps
- Docker
- docker-compose
- Kubernetes (optional)
- GitHub Actions

---

## Best Practices

1. **Always start with good wireframes** - The quality of final output depends on wireframe clarity
2. **Review PRD before proceeding** - Validate requirements early
3. **Iterate on architecture** - Refine architecture before code generation
4. **Run tests frequently** - Use test-generator to ensure quality
5. **Use orchestrator for full pipeline** - Ensures consistency and dependencies

---

## Troubleshooting

### Common Issues
- **Agent fails:** Check input artifacts are present and valid
- **Code generation errors:** Review API spec and database schema for consistency
- **Test failures:** Regenerate tests after code changes
- **Deployment issues:** Verify Docker is installed and running

---

## Future Enhancements
- Add GraphQL API option
- Add microservices architecture option
- Add PostgreSQL/MySQL database options
- Add mobile app generation (React Native)
- Add infrastructure as code (Terraform)
- Add monitoring and observability setup

---

## License
MIT

---

## Support
For issues and questions, refer to agent-specific documentation in `.agents/` directory.
