# Run 2 — A/B Comparison: Baseline vs Vector DB

**Date:** 2026-04-26
**Repo:** Books Online (fullstack)
**Hypothesis under test:** Vector retrieval cuts implementation-time input tokens by 70–90% on full-stack feature work.

## Setup

| Aspect | Baseline arm | Vector arm |
|---|---|---|
| Branch | `feature/wishlist-baseline` | `feature/featured-vector` |
| Feature | Wishlist count (`POST /books/{id}/wishlist` + count display) | Featured flag (`PATCH /books/{id}/featured` + badge + list filter) |
| Method | `@database-designer` → `@backend-generator` → `@frontend-generator` with full file Reads | Same 3 agents, with vector retrieval (`/tmp/books.db`) + manual append for frontend |
| Files touched | 11 modified + 4 created (15 total) | 11 modified + 4 created (15 total) — **isomorphic** |
| Commits | `29e8020` | `c085910` |
| Both arms branch from | `main` (commit `afa2c54`) | `main` (same) |
| App running during benchmark | yes (Spring DevTools hot-reloaded both schemas) | yes |

The features are intentionally isomorphic in shape — same fan-out, same number of files, same layer count — to control for "this feature was easier" bias.

## Headline numbers (harness-authoritative `total_tokens`)

| Stage | Baseline | Vector | Δ Vector vs Baseline | Vector "savings" claim |
|---|---:|---:|---:|---:|
| @database-designer | 34,314 | 44,641 | **+30%** | (claimed 91.2%) |
| @backend-generator | 54,131 | 84,883 | **+57%** | (claimed 92.1%) |
| @frontend-generator | 35,186 | 43,061 | **+22%** | (claimed 94.8%) |
| **TOTAL** | **123,631** | **172,585** | **+39%** | — |
| Tool uses | 90 | 132 | +47% | — |
| Wall time | 9.97 min | 13.64 min | +37% | — |

**The vector arm cost MORE tokens than the baseline arm in every stage.** The 91-95% savings claims from the retrieval scripts measure only "tokens in retrieved chunks vs tokens in the full corpus" — they don't count the orchestrator's prompt overhead, the agent prompt, or any fallback Reads. When you measure end-to-end via the harness, the savings disappear (and reverse).

## Why the savings claim and the harness number disagree

Three forces work against the retrieval claim once we measure end-to-end:

1. **Class-body chunks ≠ full source.** The vector index stores method *signatures* and class-body summaries, not full file contents. To actually edit code, the agent must Read the full file. The vector arm performed **20 fallback Reads totaling ~117 KB**:
   - Database: 1 Read (14,924 B) — chunks didn't include the 20 INSERT rows for top-5 selection
   - Backend: 10 Reads (70,138 B) — every editable file had to be Read in full
   - Frontend: 4 manual-append + ~5 minor (~31,679 B)

2. **Cross-language retrieval gap (reproduced from prior run).** The frontend query `add featured toggle to book details page and book list page` returned **6 of 7 chunks from Java** and only 1 from JSX (`booksService.getBookById`, 15 tokens). Confidence dropped to MEDIUM 50/100. Cosine semantic search ranks proper-noun similarity (`book`, `controller`) over file-type matches.

3. **Prompt overhead is fixed.** Each agent receives a multi-KB system prompt + agent definition + task description regardless of arm. The retrieval-only savings figure ignores this fixed cost; the harness `total_tokens` includes it.

## Per-stage forensic breakdown

### Database stage (vector +30%)
- Vector retrieved 9 chunks (2,771 tokens, 91.2% claimed savings, HIGH 84/100)
- Pinned `Book.prePersist` and `Book.incrementViewCount` correctly (validates AST pinning)
- BUT: `data.sql` chunk only contained the ALTER TABLE reset line, not the 20 INSERTs. Agent had to Read `data.sql` (14,924 B) to pick top-5 by view_count
- Net: small retrieval win + one fallback Read = +30% vs baseline

### Backend stage (vector +57%) — biggest divergence
- Vector retrieved 11 chunks (2,285 tokens, 92.1% claimed savings, HIGH 91/100)
- All four `BookMapper` methods correctly retrieved (validates "mapper group" stage)
- `Book.prePersist` PINNED, `incrementViewCount` via CALL_GRAPH
- BUT: chunks were class-body method-signature summaries. Agent had to Read all 10 editable files (70,138 B) to make in-place edits
- The retrieval surfaced *what* to edit; the agent still had to read the full source to know *where* and *how*
- Net: +57% over baseline. The fallback cost outweighed the retrieval savings 30:1

### Frontend stage (vector +22%) — cross-language gap reproduced
- Vector retrieved 7 chunks (1,454 tokens, 94.8% claimed savings, **MEDIUM 50/100**)
- 6 of 7 chunks were Java backend; only 1 frontend chunk (15 tokens)
- Per pre-run plan, manually appended 4 frontend files (22,479 B)
- Confidence gate correctly flagged MEDIUM — the gate is honest
- Net: even with the "cheap" retrieval contribution, manual-append + agent overhead pushed total above baseline

## Per-layer comparison (where IS vector helpful?)

| Layer | Baseline tokens | Vector tokens | Δ |
|---|---:|---:|---:|
| Java backend (database + backend stages) | 88,445 | 129,524 | +46% |
| JSX frontend (frontend stage) | 35,186 | 43,061 | +22% |

**Even on Java**, where the prior single-field benchmark showed strong wins (85-96.9% on adding a single column), the multi-file feature work shows vector arm losing. The reason: a single-field add (`country_code1`, `language2`) needs only the entity + DTO + mapper — small files, small edits, retrieval gets you most of the way. A full feature (controller endpoint + service method + repository query + tests + DTO + mapper + entity) needs *full* source for at least 5-6 files, which the chunks don't provide.

## Comparison vs prior run (sales-count, 2026-04-26)

| Metric | Sales-count baseline | Sales-count vector | Wishlist baseline (run2) | Featured vector (run2) |
|---|---:|---:|---:|---:|
| Total input tokens | ~30k–37k (est) | ~7,933 (retrieval-only) | **123,631** (full) | **172,585** (full) |
| Backend agent killed by content-filter? | YES | n/a | NO | NO |
| What was measured? | retrieval-only token claim | retrieval-only token claim | harness end-to-end | harness end-to-end |

The prior run (and the founding `vector-db-approach.md` claim) **measured the wrong thing**: tokens in retrieved chunks. Run 2 measures the right thing: harness-reported `total_tokens` for each agent run. The numbers reverse direction.

## What is a fair version of the savings claim?

If the goal is "send fewer tokens to Claude per request":
- Per-request retrieval savings are real (91-95% on this benchmark, matching prior runs)
- BUT each feature requires multiple agent invocations + multiple fallback Reads
- End-to-end, the **savings are negative** for full-feature work on this codebase

If the goal is "reduce monthly API spend on this project":
- Vector retrieval would only help if (a) chunks contained full source not summaries, OR (b) the task could complete without seeing full source (e.g., a single-field add to an entity)

## Recommendations

1. **Re-chunk at higher granularity for "modify in place" tasks.** Index whole files (not just method signatures) for files smaller than ~3KB. For files larger than 3KB, keep method-level chunks BUT include the full file contents in a separate chunk with `chunk_type='full_file'` for fallback.

2. **Frontend cross-language gap needs a structural fix.** Pinning the 4 frontend files via the manual-append fallback works but is fragile. Either (a) per-language embedding passes with separate cosine pools, or (b) extend `vectordb_search.py` with a `--prefer-language=jsx` flag for frontend tasks.

3. **Stop citing single-percentage retrieval savings.** The 91-95% number is real but misleading without the fallback-read denominator. Future benchmarks should report the harness number.

4. **The features that benefit from vector retrieval are small in scope.** Adding ONE field to ONE entity (the original `country_code1` / `language2` benchmark) is the sweet spot. Adding an endpoint, service method, controller test, mapper updates, AND UI is too broad for chunk-only context.

## What this run validated (positive)

- AST pinning of lifecycle methods works (Book.prePersist + incrementViewCount appeared in retrieval correctly)
- Mapper-group stage works (all 4 BookMapper methods retrieved in backend query)
- CALL_GRAPH stage works (incrementViewCount surfaced as call-graph entry on the backend query)
- Confidence gate is honest (correctly dropped to MEDIUM 50/100 on the cross-language frontend query)
- The "table-only final report" backend agent prompt mitigation worked — both arms completed without content-filter kill

## What this run validated (negative)

- The headline 89% / 91% / 92% / 95% retrieval savings claims are **per-query**, not **per-feature**. Per-feature savings on this codebase are negative.
- Frontend retrieval gap is structural, not data-quality (the chunks ARE indexed; cosine just ranks Java higher).
- Class-body signature chunks are insufficient for "modify in place" feature work.

## Files in this run

```
artifacts/benchmarks/run2/
├── COMPARISON.md                    ← this file
├── baseline-wishlist/
│   ├── 01-database-design.md
│   ├── 02-backend-generation.md
│   ├── 03-frontend-generation.md
│   └── SUMMARY.md
└── vector-featured/
    ├── 00-pre-run-fixes.md
    ├── 01-database-design.md
    ├── 02-backend-generation.md
    ├── 03-frontend-generation.md
    └── SUMMARY.md
```
