# ⚡ Quickstart Guide

Get from Figma wireframes to deployed application in under 5 minutes of setup.

## Step 1: Add Wireframes (1 min)

```bash
# Copy your Figma wireframe exports to the wireframes folder
cp ~/Downloads/*.png wireframes/
```

**Tip:** Name them descriptively: `home.png`, `login.png`, `dashboard.png`

---

## Step 2: Run Orchestrator (1 min)

Using Claude Code, invoke the orchestrator agent:

```
@orchestrator
```

Or ask Claude:

```
Run the orchestrator agent to generate my application.
```

---

## Step 3: Wait (20-40 min)

The orchestrator will:
- ✅ Analyze wireframes
- ✅ Generate PRD & user stories
- ✅ Design architecture
- ✅ Generate complete backend (Spring Boot)
- ✅ Generate complete frontend (React)
- ✅ Create tests
- ✅ Create deployment configs

**Go grab coffee!** ☕

---

## Step 4: Run Your App (2 min)

**Option A: Local Development**

Terminal 1:
```bash
cd backend && mvn spring-boot:run
```

Terminal 2:
```bash
cd frontend && npm install && npm run dev
```

Open: http://localhost:5173

---

**Option B: Docker (Full Stack)**

```bash
cd deployment && docker-compose up --build
```

Open: http://localhost

---

## Step 5: Review & Iterate (ongoing)

Check generated files:
```bash
# Documentation
cat artifacts/02-prd/PRD.md

# Architecture
cat artifacts/04-architecture/architecture.md

# Execution report
cat EXECUTION_REPORT.md
```

---

## Next Steps

1. Test the application
2. Review generated code
3. Customize styling
4. Add custom business logic
5. Deploy to production

---

## Troubleshooting

**Backend won't start?**
```bash
lsof -i :8080  # Check port
mvn clean install  # Rebuild
```

**Frontend won't start?**
```bash
cd frontend
rm -rf node_modules && npm install
```

**Need help?**
- See USAGE_GUIDE.md for detailed instructions
- See README.md for architecture overview

---

## That's It! 🎉

You now have:
- ✅ Complete React frontend
- ✅ Complete Spring Boot backend
- ✅ Database schema
- ✅ API documentation
- ✅ Tests
- ✅ Deployment configs

Start building!
