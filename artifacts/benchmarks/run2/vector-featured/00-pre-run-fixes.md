# Vector Run — Pre-Run Fixes

**Branch:** feature/featured-vector
**Date:** 2026-04-26
**DB:** `/tmp/books.db`

This file documents the three gaps from the 2026-04-26 sales-count vector run and how they were addressed before this run.

## Gap 1: Duplicate seed-data chunks in vector DB

### Before
```
sqlite3 /tmp/books.db "SELECT COUNT(*) FROM summaries;"
→ 186

sqlite3 /tmp/books.db "SELECT file_path, COUNT(*) FROM summaries WHERE file_path LIKE '%data.sql' OR file_path LIKE '%seed-data.sql' GROUP BY file_path;"
→ /content/cqs_repos/repo_furxui_f/artifacts/06-database-design/seed-data.sql|2
→ /content/cqs_repos/repo_furxui_f/backend/src/main/resources/data.sql|2
```

Two copies of the same content indexed under different paths — the artifact-side `seed-data.sql` is the canonical-source-of-truth doc, but the runtime SQL `data.sql` is what actually loads. Both have 2 chunks.

### Fix
```bash
sqlite3 /tmp/books.db "DELETE FROM summaries WHERE file_path LIKE '%seed-data.sql';"
```

### After
```
Total chunks: 184 (was 186)
Remaining seed match: backend/src/main/resources/data.sql|2 (canonical only)
```

**Result:** dedup applied. -2 chunks. Future queries about seed data will not split retrieval budget across two near-identical results.

## Gap 2: Frontend retrieval gap

### Last run's failure
- `BookDetailsPage.jsx` topped at rank 6 → SKIPPED (top-5 cutoff)
- `BooksListPage.jsx` topped at rank 9 → SKIPPED
- `booksService.js` and `api.js` never appeared in any of 8 queries

### Current DB state (relevant frontend files)
```
frontend/src/pages/BookDetailsPage.jsx         | 5 chunks
frontend/src/pages/BooksListPage.jsx           | 6 chunks
frontend/src/services/booksService.js          | 8 chunks
```

The chunks ARE indexed — the issue last run was that **cosine relevance** placed Java entity/controller chunks above JSX chunks for cross-language queries. The chunks aren't missing, they just rank lower.

### Fix (manual prompt-append, per plan)
For the frontend stage of the vector arm, after running the cosine search, **manually append the full text of these four files** as `## Provided context (frontend)`:
- `frontend/src/pages/BookDetailsPage.jsx`
- `frontend/src/pages/BooksListPage.jsx`
- `frontend/src/services/booksService.js`
- `frontend/src/services/api.js`

Their byte-cost will be **counted toward the vector arm's input tokens** in the comparison — this keeps the A/B fair. The hypothesis being tested is "vector retrieval saves tokens vs full-context for backend layers; for frontend the savings disappear once we have to fall back to full files." If the hypothesis holds, the layer-by-layer breakdown in `COMPARISON.md` will show ~70% Java savings and ~0% JSX savings.

## Gap 3: Backend agent content-filter kill

### Last run's failure
At tool 76, the @backend-generator agent was killed by Anthropic content-filter, losing the `total_tokens` line. Likely trigger: long final-report block.

### Fix
The agent prompt for **both the baseline and vector arms** was rewritten with:
- `## CRITICAL: Final report format` block stating "table only, max 500 words, no narrative recap"
- Explicit instruction: "If you start to write a `## Summary` section, stop and replace it with the table"

**Validation:** The baseline arm's @backend-generator (run earlier today, commit `29e8020`) completed cleanly with `total_tokens=54,131` captured. The mitigation works.

## Vector retrieval queries planned for this run

| Stage | Query | Notes |
|---|---|---|
| Database | `add is_featured boolean field to Book entity schema` | Should pin Book.prePersist + preUpdate; should retrieve Book.java, schema.sql, data.sql |
| Backend | `add featured field to Book entity controller service repository mapper` | Should retrieve Book.java, BookController, BookServiceImpl, BookRepository, BookMapper, BookResponse |
| Frontend | `add featured toggle to book details page and book list page` | Plus manual append of 4 frontend files (per Gap 2 fix) |

## Capture per query
- `top_score` (highest cosine in result set)
- `confidence` (HIGH / MEDIUM / LOW from gate)
- list of RETRIEVED files
- total retrieved tokens (sum of `input_tokens` for RETRIEVED chunks)

## Capture per stage
- harness `total_tokens`, `tool_uses`, `duration_ms`
- bytes manually appended (frontend stage only)
