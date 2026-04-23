# Vector DB Approach for AI-Assisted Code Generation

## What This Is

A technique for reducing token costs when using an LLM (e.g. Claude) to implement features in a codebase. Instead of reading all relevant files in full before generating code, the codebase is pre-chunked and indexed. At implementation time, only the top-N most relevant chunks are retrieved and sent to the model.

This document captures lessons learned from the Books Online project, where we implemented four fields (`country_code1`, `country_code`, `languages`, `language2`) using both approaches and compared the results. Updated after embedding search was implemented.

---

## How It Works

### 1. Index the codebase

Split source files into logical chunks (by function/method, not arbitrary line count) and store them in a searchable index with metadata:

- File name
- Function/method name
- Token count
- Chunk content

### 2. On each feature request, search the index

Run a relevance search against the query (e.g. "add country_code field to Book entity") and retrieve the top-N highest-scoring chunks.

### 3. Send only retrieved chunks to the model

The model sees only those chunks as context — not full files — and generates the implementation from them.

### 4. Track what was billed vs skipped

```
RANK  SCORE  FILE                   FUNCTION         TOKENS  STATUS
--------------------------------------------------------------------
1     22     BookServiceImpl.java   updateBook          173   RETRIEVED
2     22     BookServiceImpl.java   createBook          136   RETRIEVED
3     16     BookController.java    updateBook          124   RETRIEVED
4     16     BookController.java    createBook          107   RETRIEVED
5     14     BookController.java    exportBook          103   RETRIEVED
6     12     BookController.java    searchBooks         446   skipped
...
27    -7     App.jsx                App                 187   skipped
```

---

## Observed Results (Books Online Project)

Four fields implemented across two sessions — traditional vs Vector DB:

| | `country_code1` (Traditional) | `country_code` (Vector DB / keyword) | `languages` (Traditional) | `language2` (Vector DB / embeddings) |
| --- | --- | --- | --- | --- |
| Search method | Full file reads | Keyword relevance | Full file reads | Cosine similarity (all-MiniLM-L6-v2) |
| Input tokens | ~18,500 | 650 | ~18,910 | 593 |
| Output tokens | ~950 | ~420 | ~380 | ~310 |
| Estimated cost | ~$0.070 | ~$0.008 | ~$0.063 | ~$0.006 |
| Result correctness | Identical | Identical | Identical | Identical |
| Token savings vs traditional | baseline | 85% | baseline | 96.9% |

End result was identical across all 4 fields: wired correctly across all 5 layers (schema → entity → request DTO → response DTO → mapper with all 4 methods covered).

---

## Where It Works Well

- **Large codebases (200+ files).** Traditional costs grow linearly with codebase size. Vector DB stays flat — always retrieves top-N regardless of total chunk count.
- **Repetitive CRUD-style changes.** "Add a field" tasks always touch the same structural layers. The index reliably surfaces entity, DTOs, mapper, and schema chunks.
- **High-frequency feature work.** Each feature request is cheap, so cumulative savings compound quickly across a project.

---

## Where It Has Weaknesses

### Missed dependencies
Low-scoring chunks may contain logic that is actually relevant. In the Books Online case, `Book.java:prePersist` (rank 23) and `Book.java:incrementViewCount` (rank 24) were skipped. For fields with default values or lifecycle logic, skipping those would produce a broken implementation.

**Mitigation:** Always include entity lifecycle methods (prePersist, preUpdate) in a pinned chunk set for entity-modification tasks.

### ~~Keyword scoring is fragile~~ — RESOLVED ✅
`sentence-transformers` (`all-MiniLM-L6-v2`) is installed and active. The script falls back to keyword scoring only if the library is missing. Confirmed working: Feature `language2` used `cosine_similarity (live)`.

**Remaining issue:** The model is loaded fresh on every query call. For high-frequency or batch use, keep the model warm across calls to avoid repeated load overhead.

### No cross-chunk reasoning
The model sees isolated chunks. It cannot infer that if `toEntity()` needs updating, `createDuplicate()` also needs it — unless both score in the top-N independently.

**Mitigation:** For mapper-style classes with multiple related methods, retrieve the full class or group methods as a single chunk.

### Cold start and staleness
The index must be built upfront and kept in sync. Stale embeddings on frequently-changed files cause wrong or missed retrievals.

**Mitigation:** Re-index on every commit, or at minimum on every file save in the active development area.

---

## Scoring: Keyword vs Embeddings

| | Keyword Relevance | True Vector Embeddings (current) |
| --- | --- | --- |
| How it works | Term frequency / overlap count | Semantic similarity in vector space |
| Accuracy | Moderate — breaks on synonyms/abstractions | High — understands intent, not just words |
| Speed | Fast (no model call needed) | Slower — model loads fresh on each call |
| Cost | Free | Free (local `all-MiniLM-L6-v2`) |
| Status | Fallback only | **Active — confirmed working** |
| Production readiness | Proof of concept | Production-worthy (with warm model) |

**Current implementation:** `all-MiniLM-L6-v2` via `sentence-transformers`, loaded live per query. For production, keep model in memory across calls or switch to a persistent embedding service.

---

## Implementation Checklist for Next Project

- [x] Write chunker: split files by function/method boundary, record file + function name + token count
- [x] Choose scoring: cosine similarity via `sentence-transformers` (`all-MiniLM-L6-v2`)
- [x] Build index: SQLite at `/tmp/books-online-vectors.db`
- [x] Write search script: `~/.claude/scripts/vectordb_search.py` — returns top-N with scores and token counts
- [x] Add token tracking: billed vs skipped tokens logged per feature request
- [ ] Define pinned chunks: always-retrieved chunks for common task types (e.g. entity lifecycle methods for entity-modification tasks) — **not yet implemented**
- [ ] Group mapper methods as single chunk: prevents cross-chunk reasoning gaps on `toEntity`/`createDuplicate` — **not yet implemented**
- [ ] Re-index strategy: on commit hook or file-watch — **not yet implemented; index goes stale after each session**
- [ ] Keep embedding model warm: avoid fresh load per query in production
- [ ] Validate: implement a test feature both ways, confirm output is identical ✅ done (4 fields proven)

---

## Token Cost Reference (Claude Sonnet 4.6 as of April 2026)

| Model | Input (per 1M tokens) | Output (per 1M tokens) |
| --- | --- | --- |
| Claude Sonnet 4.6 | $3.00 | $15.00 |
| Claude Haiku 4.5 | $0.80 | $4.00 |
| Claude Opus 4.7 | $15.00 | $75.00 |

At Sonnet 4.6 pricing, the 17,850 token savings per feature = **~$0.054 saved per feature**. At 100 features in a project that is ~$5.40 — modest at small scale, meaningful at large scale or with Opus.

---

## Summary Verdict

| Scenario | Recommendation |
| --- | --- |
| Large codebase, repetitive CRUD changes | Vector DB — clear winner |
| Complex feature touching many unpredictable layers | Traditional — safer |
| Small codebase (<50 files) | Traditional — overhead not worth it |
| Keyword scoring | Fallback only — cosine similarity now active |
| Semantic embeddings (all-MiniLM-L6-v2) | Active and confirmed working |

Embedding search is proven and active. Three remaining gaps before production-ready: pinned chunks for lifecycle methods, grouped mapper chunks, and automated re-indexing on code change.
