# Figma-to-Production: Usage Guide

## 🎯 Overview

This multi-agent system transforms Figma wireframes into a fully deployed application through an intelligent pipeline of 11 specialized agents.

---

## 📋 Prerequisites

### Required:
- Figma wireframes (PNG, JPG, or PDF exports)
- Claude Code CLI
- Java 17+ (for backend)
- Node.js 20+ (for frontend)
- Maven (for backend builds)

### Optional:
- Docker & docker-compose (for deployment)
- Git (for version control)

---

## 🚀 Quick Start

### Step 1: Prepare Wireframes

1. Export your Figma wireframes as images (PNG recommended)
2. Place them in the `wireframes/` folder:

```bash
cp ~/Downloads/figma-wireframes/*.png wireframes/
```

**Naming Tips:**
- Use descriptive names: `home-page.png`, `login-screen.png`, `dashboard.png`
- Include all key screens (landing, auth, CRUD screens, etc.)
- Minimum 3-5 screens recommended for meaningful app

### Step 2: Run the Orchestrator

**Option A: Full Pipeline (Recommended)**

Ask Claude Code:
```
Run the orchestrator agent to generate the complete application from my wireframes.
```

Claude will:
1. Read `.claude/agents/00-orchestrator.md`
2. Execute all 10 agents sequentially
3. Generate complete application
4. Provide final report

**Estimated Time:** 20-40 minutes depending on complexity

---

**Option B: Individual Agents (Advanced)**

Run agents one by one for more control:

```
1. Run the wireframe-analyzer agent
2. Run the prd-generator agent
3. Run the user-stories-generator agent
... and so on
```

---

### Step 3: Review Generated Outputs

After orchestrator completes, check:

#### 📄 Documentation (`artifacts/`)
```
artifacts/
├── 01-wireframe-analysis/     # UI component analysis
├── 02-prd/                     # Product requirements
├── 03-user-stories/            # User stories & epics
├── 04-architecture/            # System architecture
├── 05-api-spec/                # OpenAPI specification
└── 06-database-design/         # Database schema
```

#### 💻 Backend (`backend/`)
```
backend/
├── src/main/java/com/app/      # Spring Boot application
├── pom.xml                     # Maven configuration
└── README.md                   # Backend documentation
```

#### 🎨 Frontend (`frontend/`)
```
frontend/
├── src/                        # React application
├── package.json                # npm configuration
└── README.md                   # Frontend documentation
```

#### 🧪 Tests (`tests/` + within backend/frontend)
- Backend: JUnit tests in `backend/src/test/`
- Frontend: Vitest tests in `frontend/src/__tests__/`
- E2E: Playwright tests in `tests/e2e/`

#### 🚀 Deployment (`deployment/`)
```
deployment/
├── docker-compose.yml          # Full stack orchestration
├── Dockerfile.backend          # Backend container
├── Dockerfile.frontend         # Frontend container
└── .github/workflows/          # CI/CD pipeline
```

---

## 🎮 Running Your Application

### Local Development (Recommended)

**Terminal 1 - Backend:**
```bash
cd backend
mvn spring-boot:run
```
Backend runs on: http://localhost:8080
API docs: http://localhost:8080/swagger-ui.html

**Terminal 2 - Frontend:**
```bash
cd frontend
npm install        # First time only
npm run dev
```
Frontend runs on: http://localhost:5173

**Access the application:** Open http://localhost:5173 in your browser

---

### Docker (Full Stack)

```bash
cd deployment
docker-compose up --build
```

**Access:**
- Frontend: http://localhost
- Backend API: http://localhost:8080
- API Docs: http://localhost:8080/swagger-ui.html

**Stop:**
```bash
docker-compose down
```

---

## 🔧 Customization & Iteration

### Regenerate Specific Components

If you want to change something:

**Option 1: Modify and regenerate specific agent**
```
I want to change the frontend styling. Run the frontend-generator agent again.
```

**Option 2: Manual edits**
- Edit generated code directly
- Agents are designed to produce clean, readable code
- Follow existing patterns when adding features

### Common Customizations

#### 1. Add New Entity

**Manual approach:**
1. Update `artifacts/06-database-design/schema.sql` (add table)
2. Run backend-generator agent to regenerate entities
3. Run frontend-generator agent to regenerate UI

**Orchestrator approach:**
```
I want to add a "Product" entity with name, price, and description.
Update the database schema and regenerate affected code.
```

#### 2. Change Styling

Edit `frontend/src/styles/` or modify Tailwind classes in components.

#### 3. Add Authentication Provider

Modify `backend/src/main/java/com/app/security/` for OAuth, SAML, etc.

#### 4. Change Database

Update `backend/src/main/resources/application.properties`:
```properties
# Switch to PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
```

Add PostgreSQL dependency to `pom.xml`.

---

## 🧪 Testing

### Backend Tests

```bash
cd backend

# Run all tests
mvn test

# Run specific test
mvn test -Dtest=UserServiceTest

# Generate coverage report
mvn jacoco:report
# Open: target/site/jacoco/index.html
```

### Frontend Tests

```bash
cd frontend

# Run all tests
npm test

# Run in watch mode
npm test -- --watch

# Generate coverage
npm run test:coverage
```

### End-to-End Tests

```bash
cd tests/e2e

# Install Playwright (first time only)
npm install
npx playwright install

# Run E2E tests
npx playwright test

# Run with UI
npx playwright test --ui
```

---

## 📊 Monitoring & Debugging

### Backend Logs

```bash
cd backend
mvn spring-boot:run

# Logs output to console
# Check application.properties for log levels
```

### Frontend Debugging

- Open browser DevTools (F12)
- Check Console for errors
- Use React DevTools extension
- Check Network tab for API calls

### Database Access

H2 Console (development):
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (leave empty)
```

---

## 🐛 Troubleshooting

### Common Issues

#### 1. Backend won't start

**Error:** Port 8080 already in use
```bash
# Find process using port 8080
lsof -i :8080

# Kill it
kill -9 <PID>
```

**Error:** Maven build fails
```bash
# Clean and rebuild
cd backend
mvn clean install -DskipTests
```

#### 2. Frontend won't start

**Error:** Port 5173 already in use
```bash
# Kill process
lsof -i :5173
kill -9 <PID>
```

**Error:** Module not found
```bash
# Reinstall dependencies
cd frontend
rm -rf node_modules package-lock.json
npm install
```

#### 3. API calls fail (CORS)

Check backend `CorsConfig.java` allows frontend origin:
```java
.allowedOrigins("http://localhost:5173")
```

#### 4. Authentication issues

- Check JWT secret is consistent
- Clear browser localStorage
- Check token expiration time

---

## 📈 Next Steps After Generation

### Week 1: Validation & Testing
- [ ] Review all generated documentation
- [ ] Test all user flows manually
- [ ] Run automated test suites
- [ ] Fix any failing tests

### Week 2: Customization
- [ ] Customize styling and branding
- [ ] Add company logo and colors
- [ ] Refine UI/UX based on feedback
- [ ] Add custom business logic

### Week 3: Enhancement
- [ ] Add advanced features not in wireframes
- [ ] Implement email notifications
- [ ] Add analytics/logging
- [ ] Performance optimization

### Week 4: Deployment Prep
- [ ] Security audit
- [ ] Load testing
- [ ] Documentation review
- [ ] Prepare production environment

### Week 5: Production Deployment
- [ ] Deploy to cloud (AWS, Azure, GCP)
- [ ] Setup monitoring (Prometheus, Grafana)
- [ ] Configure CDN for frontend
- [ ] Setup backups and disaster recovery

---

## 🤝 Agent Interaction Patterns

### How Agents Communicate

Agents communicate through **file artifacts**:

```
Wireframe Analyzer
    ↓ (writes wireframe-analysis.json)
PRD Generator (reads wireframe-analysis.json)
    ↓ (writes PRD.md)
User Stories Generator (reads PRD.md)
    ↓ (writes user-stories.json)
... and so on
```

### Extending the System

**Add a new agent:**

1. Create `.claude/agents/XX-new-agent.md`
2. Define inputs (which artifacts it reads)
3. Define outputs (which artifacts it creates)
4. Specify execution instructions
5. Update orchestrator to include new agent in pipeline

**Example: Analytics Agent**
```markdown
# AGENT: Analytics Integration Generator

## INPUTS
- artifacts/04-architecture/architecture.md

## OUTPUTS
- backend/src/main/java/com/app/analytics/
- frontend/src/analytics/

## INSTRUCTIONS
Generate Google Analytics integration code...
```

---

## 📚 Understanding Each Agent

### 1. Wireframe Analyzer
**What it does:** Analyzes images, extracts UI components, identifies entities
**Output:** JSON with screens, components, navigation flows
**Duration:** 2-5 minutes

### 2. PRD Generator
**What it does:** Creates professional Product Requirements Document
**Output:** Comprehensive PRD with features, requirements, success metrics
**Duration:** 3-5 minutes

### 3. User Stories Generator
**What it does:** Generates user stories with acceptance criteria
**Output:** User stories, epics, sprint plan
**Duration:** 3-5 minutes

### 4. Architecture Designer
**What it does:** Designs system architecture, tech stack, component structure
**Output:** Architecture docs, tech stack decisions, diagrams
**Duration:** 5-7 minutes

### 5. API Spec Generator
**What it does:** Creates OpenAPI 3.0 specification for REST API
**Output:** openapi-spec.yaml with all endpoints
**Duration:** 3-5 minutes

### 6. Database Designer
**What it does:** Designs normalized database schema
**Output:** SQL DDL, ER diagrams, seed data
**Duration:** 3-5 minutes

### 7. Backend Generator
**What it does:** Generates complete Spring Boot application
**Output:** Full Java codebase with entities, repos, services, controllers
**Duration:** 10-15 minutes (longest agent)

### 8. Frontend Generator
**What it does:** Generates complete React application
**Output:** Full React codebase with components, pages, routing
**Duration:** 10-15 minutes

### 9. Test Generator
**What it does:** Creates unit, integration, and E2E tests
**Output:** Comprehensive test suites for backend and frontend
**Duration:** 5-10 minutes

### 10. Deployment Agent
**What it does:** Creates deployment configurations
**Output:** Docker files, docker-compose, CI/CD pipelines
**Duration:** 2-3 minutes

### 11. Orchestrator
**What it does:** Coordinates all agents, validates outputs, generates report
**Output:** EXECUTION_REPORT.md, complete application
**Duration:** Sum of all agents (30-50 minutes total)

---

## 💡 Best Practices

### For Best Results:

1. **High-Quality Wireframes**
   - Clear, legible images
   - Consistent naming
   - Include all key screens
   - Show navigation flows

2. **Review Early Outputs**
   - Check PRD before continuing
   - Validate architecture decisions
   - Ensure user stories match expectations

3. **Iterative Refinement**
   - Generate → Review → Refine → Regenerate
   - Don't expect 100% perfection first time
   - Use agents as starting point, not final product

4. **Version Control**
   - Initialize git repo: `git init`
   - Commit after each major change
   - Use branches for experimentation

5. **Documentation**
   - Keep wireframes folder updated
   - Document custom changes
   - Maintain CHANGELOG.md

---

## 🎓 Learning Resources

### Spring Boot (Backend)
- https://spring.io/guides
- https://docs.spring.io/spring-boot/docs/current/reference/html/

### React (Frontend)
- https://react.dev/
- https://vitejs.dev/

### OpenAPI
- https://swagger.io/specification/

### Docker
- https://docs.docker.com/

---

## 🙋 FAQ

**Q: Can I use different tech stack?**
A: Yes, but you'll need to modify agent prompts. Edit `.claude/config.json` and agent files.

**Q: How do I add authentication with OAuth?**
A: Modify backend security config. Agents generate JWT by default.

**Q: Can I regenerate just the frontend?**
A: Yes! Run: "Run the frontend-generator agent"

**Q: What if orchestrator fails midway?**
A: Check EXECUTION_REPORT.md for error. You can resume by running remaining agents individually.

**Q: How do I deploy to AWS/Azure/GCP?**
A: Modify deployment agent or use generated Docker images with cloud container services.

**Q: Can I use PostgreSQL instead of H2?**
A: Yes, update application.properties and pom.xml. Database schema remains same.

---

## 📞 Support

For issues:
1. Check EXECUTION_REPORT.md for errors
2. Check agent output logs
3. Review troubleshooting section above
4. Modify and re-run specific agents

---

## 🎉 Success Stories

After generation, you'll have:
- ✅ Professional documentation (PRD, architecture, user stories)
- ✅ Production-ready REST API (Spring Boot)
- ✅ Modern responsive UI (React + Tailwind)
- ✅ Complete database schema (H2, configurable)
- ✅ Comprehensive test suites
- ✅ Deployment configurations (Docker, CI/CD)
- ✅ API documentation (Swagger UI)

**You can immediately:**
- Show to stakeholders
- Start development
- Deploy to production
- Iterate and enhance

---

**Generated by Figma-to-Production Multi-Agent System**
