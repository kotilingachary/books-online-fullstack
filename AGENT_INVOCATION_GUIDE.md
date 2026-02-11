# Agent Invocation Guide

## How to Invoke Agents in Claude Code

All agents in this system are implemented as **Claude Code subagents** with proper YAML frontmatter. You can invoke them in multiple ways.

---

## Method 1: Direct Mention (Recommended)

Use the `@` symbol followed by the agent name:

```
@orchestrator
```

```
@wireframe-analyzer
```

```
@backend-generator
```

This is the cleanest and most direct way to invoke agents.

---

## Method 2: Natural Language Request

Ask Claude Code to run the agent:

```
Run the orchestrator agent
```

```
Please execute the wireframe-analyzer agent
```

```
I need you to run the prd-generator agent
```

Claude Code will understand and invoke the appropriate agent.

---

## Method 3: Explicit Task Tool (Advanced)

For more control, you can use the Task tool directly:

```
Use the Task tool with the orchestrator subagent to generate my application from wireframes.
```

---

## Available Agents

### 1. orchestrator
**Invoke with:** `@orchestrator`

**What it does:** Coordinates all 10 specialized agents in sequence, validates outputs, handles errors, generates final report.

**Use when:** You want to generate a complete application from wireframes end-to-end.

---

### 2. wireframe-analyzer
**Invoke with:** `@wireframe-analyzer`

**What it does:** Analyzes Figma wireframes to extract UI components, navigation flows, data requirements.

**Use when:** You only want to analyze wireframes without generating the full application.

---

### 3. prd-generator
**Invoke with:** `@prd-generator`

**What it does:** Creates comprehensive Product Requirements Document.

**Use when:** Wireframe analysis is complete and you need a PRD.

**Prerequisites:** Run `@wireframe-analyzer` first.

---

### 4. user-stories-generator
**Invoke with:** `@user-stories-generator`

**What it does:** Generates user stories with acceptance criteria, prioritization, sprint plans.

**Use when:** PRD is complete and you need user stories.

**Prerequisites:** Run `@prd-generator` first.

---

### 5. architecture-designer
**Invoke with:** `@architecture-designer`

**What it does:** Designs comprehensive system architecture, tech stack, component structure.

**Use when:** PRD is complete and you need architecture documentation.

**Prerequisites:** Run `@prd-generator` first.

---

### 6. api-spec-generator
**Invoke with:** `@api-spec-generator`

**What it does:** Generates complete OpenAPI 3.0 REST API specification.

**Use when:** Architecture is defined and you need API specifications.

**Prerequisites:** Run `@architecture-designer` first.

---

### 7. database-designer
**Invoke with:** `@database-designer`

**What it does:** Designs normalized database schema with tables, relationships, constraints.

**Use when:** Architecture is defined and you need database design.

**Prerequisites:** Run `@architecture-designer` first.

---

### 8. backend-generator
**Invoke with:** `@backend-generator`

**What it does:** Generates complete Spring Boot REST API application.

**Use when:** API spec and database schema are ready and you want backend code.

**Prerequisites:** Run `@api-spec-generator` and `@database-designer` first.

**Duration:** 10-15 minutes (longest agent)

---

### 9. frontend-generator
**Invoke with:** `@frontend-generator`

**What it does:** Generates complete React application with components, pages, routing.

**Use when:** API spec is ready and you want frontend code.

**Prerequisites:** Run `@api-spec-generator` first.

**Duration:** 10-15 minutes

---

### 10. test-generator
**Invoke with:** `@test-generator`

**What it does:** Generates comprehensive test suites (backend, frontend, E2E).

**Use when:** Backend and frontend code are generated and you need tests.

**Prerequisites:** Run `@backend-generator` and `@frontend-generator` first.

---

### 11. deployment-agent
**Invoke with:** `@deployment-agent`

**What it does:** Generates deployment configurations (Docker, CI/CD).

**Use when:** Application is complete and you need deployment setup.

**Prerequisites:** Run `@backend-generator` and `@frontend-generator` first.

---

## Typical Workflows

### Full Pipeline (Recommended for First Time)

```
@orchestrator
```

Wait 30-50 minutes. Done!

---

### Incremental Development

```
# Step 1: Analyze wireframes
@wireframe-analyzer

# Step 2: Review wireframe analysis, then generate PRD
@prd-generator

# Step 3: Review PRD, then generate user stories
@user-stories-generator

# Step 4: Generate architecture
@architecture-designer

# Step 5: Generate API spec
@api-spec-generator

# Step 6: Generate database design
@database-designer

# Step 7: Generate backend (takes ~10-15 min)
@backend-generator

# Step 8: Generate frontend (takes ~10-15 min)
@frontend-generator

# Step 9: Generate tests
@test-generator

# Step 10: Generate deployment configs
@deployment-agent
```

---

### Selective Regeneration

Already have most artifacts but want to regenerate specific parts:

```
# Regenerate just the backend
@backend-generator

# Regenerate just the frontend with new styling
@frontend-generator

# Regenerate tests after code changes
@test-generator
```

---

## Agent Dependencies

```
wireframe-analyzer
    ↓
prd-generator
    ↓
    ├─→ user-stories-generator
    └─→ architecture-designer
            ↓
            ├─→ api-spec-generator
            └─→ database-designer
                    ↓
            ├─→ backend-generator
            └─→ frontend-generator
                    ↓
                test-generator
                    ↓
            deployment-agent
```

**Important:** Always run prerequisite agents before dependent agents. The orchestrator handles this automatically.

---

## Monitoring Agent Execution

### Check Progress

Agents will output progress updates as they work:

```
## Wireframe Analysis Complete ✓

**Screens Analyzed:** 5
**Components Identified:** 47
**Entities Identified:** 4
...
```

### Check Outputs

After an agent completes, verify its outputs:

```bash
# Wireframe analyzer outputs
ls artifacts/01-wireframe-analysis/

# PRD outputs
ls artifacts/02-prd/

# Backend outputs
ls backend/src/main/java/com/app/

# Frontend outputs
ls frontend/src/
```

---

## Error Handling

### If an Agent Fails

1. **Check error message** - Agent will explain what went wrong
2. **Verify prerequisites** - Ensure required input files exist
3. **Check input files** - Ensure they're valid (JSON, YAML, etc.)
4. **Retry** - Simply invoke the agent again: `@agent-name`

### Common Issues

**"Input file not found"**
- Run prerequisite agents first
- Check artifacts/ folder for expected files

**"Build failed"**
- Check Maven/npm errors
- Fix code issues manually
- Re-run agent if needed

**"Timeout"**
- Large applications may take longer
- Wait and retry
- Consider splitting work into phases

---

## Best Practices

### 1. Use Orchestrator for First Generation
```
@orchestrator
```
Let it handle all dependencies and sequencing.

### 2. Use Individual Agents for Iteration
After first generation, use specific agents to regenerate parts:
```
@frontend-generator
```

### 3. Review Outputs Between Agents
When running incrementally, review outputs before continuing:
```
@wireframe-analyzer
# Review artifacts/01-wireframe-analysis/
# If good, continue:
@prd-generator
```

### 4. Keep Artifacts Folder Clean
Don't manually edit artifact files. If you need changes, regenerate the agent.

### 5. Version Control
```bash
git add .
git commit -m "Generated application with orchestrator"
```

---

## Agent Configuration

Agents read configuration from `.claude/config.json`:

```json
{
  "techStack": {
    "frontend": {
      "framework": "React",
      "styling": "Tailwind CSS"
    },
    "backend": {
      "framework": "Spring Boot",
      "database": "H2"
    }
  }
}
```

Modify this file to customize tech stack choices.

---

## Troubleshooting

### Agent Not Found

If Claude doesn't recognize the agent name:

1. Check agent files exist: `ls .claude/agents/`
2. Verify frontmatter format is correct
3. Try natural language: "Run the orchestrator agent"

### Agent Produces Wrong Output

1. Check input files are correct
2. Review agent prompt in `.claude/agents/XX-agent-name.md`
3. Modify prompt if needed
4. Re-run agent

### Agent Takes Too Long

Code generation agents (backend-generator, frontend-generator) can take 10-15 minutes. This is normal.

For very large applications:
- Consider simplifying wireframes
- Generate in phases
- Use incremental approach

---

## Advanced Usage

### Parallel Execution

Some agents can run in parallel (they have no dependencies):

```
# These can run simultaneously:
@user-stories-generator
@architecture-designer

# These can run simultaneously:
@api-spec-generator
@database-designer

# These can run simultaneously:
@backend-generator
@frontend-generator
```

### Custom Agents

To add your own agent:

1. Create `.claude/agents/XX-my-agent.md`
2. Add YAML frontmatter:
```yaml
---
name: my-agent
description: What your agent does
tools: "*"
model: sonnet
---
```
3. Write agent instructions
4. Invoke with `@my-agent`

---

## Quick Reference

| Agent | Command | Duration | Prerequisites |
|-------|---------|----------|---------------|
| orchestrator | `@orchestrator` | 30-50 min | Wireframes in wireframes/ |
| wireframe-analyzer | `@wireframe-analyzer` | 3-5 min | Wireframes in wireframes/ |
| prd-generator | `@prd-generator` | 3-5 min | wireframe-analysis.json |
| user-stories-generator | `@user-stories-generator` | 3-5 min | PRD.md |
| architecture-designer | `@architecture-designer` | 5-7 min | PRD.md, user-stories.json |
| api-spec-generator | `@api-spec-generator` | 3-5 min | architecture.md |
| database-designer | `@database-designer` | 3-5 min | architecture.md, openapi-spec.yaml |
| backend-generator | `@backend-generator` | 10-15 min | openapi-spec.yaml, schema.sql |
| frontend-generator | `@frontend-generator` | 10-15 min | openapi-spec.yaml, wireframe-analysis.json |
| test-generator | `@test-generator` | 5-10 min | backend/, frontend/ |
| deployment-agent | `@deployment-agent` | 2-3 min | backend/, frontend/ |

---

**Happy generating!** 🚀
