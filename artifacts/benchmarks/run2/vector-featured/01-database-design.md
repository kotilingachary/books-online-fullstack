# Vector Run — Database Design (is_featured flag)

**Branch:** feature/featured-vector
**Date:** 2026-04-26
**Agent:** @database-designer
**Mode:** vector (retrieval-only; source files not Read unless fallback triggered)

## Decisions

| Decision | Choice |
|---|---|
| Column | `is_featured BOOLEAN NOT NULL DEFAULT FALSE` after `discount` |
| CHECK | None (BOOLEAN is self-constrained) |
| Index | `CREATE INDEX idx_books_is_featured ON books(is_featured)` (plain; H2 does not support partial WHERE-clause indexes) |
| Concurrency | n/a (flag toggled by admin write path, no increment contention) |
| Seed data | 20 rows, 5 marked TRUE: Atomic Habits (123456), 1984 (89012), The 7 Habits (89234), To Kill a Mockingbird (67890), The Martian (45678) — top 5 by view_count |
| Audit table | Deferred |

## Vector retrieval block

| Metric | Value |
|---|---|
| Query | `add is_featured boolean field to Book entity schema` |
| Chunks retrieved | 7 retrieved + 2 pinned = 9 total |
| Retrieved tokens | 2,633 (retrieved) + 138 (pinned) = 2,771 |
| Skipped tokens | 27,472 |
| Savings | 91.2% |
| Confidence | HIGH 84/100 |
| Fallback triggered | YES — data.sql INSERT rows with view_count values were not in chunks (chunk 5 contained only the ALTER TABLE reset line, not the 20 INSERTs); `data.sql` was Read once to obtain view_count values for top-5 selection |

## Token / context metrics

```
TOKENS_VECTOR_FEATURED_DBD: retrieved_tokens=2771, fallback_bytes_read=14924, files_read=1
```

**Harness-reported usage (authoritative):**
- `total_tokens`: **44,641**
- `tool_uses`: 33
- `duration_ms`: 222,810

(Agent self-estimate was ~6,200 — counts only retrieval + edits. Harness number includes prompt overhead, agent definition, fallback Read of data.sql. The harness number is what we use for the A/B comparison.)

## Files modified

- `backend/src/main/resources/schema.sql` — added `is_featured BOOLEAN NOT NULL DEFAULT FALSE` after `discount`, added COMMENT, added `idx_books_is_featured` index, updated stats comment (19 columns, 9 indexes)
- `backend/src/main/resources/data.sql` — added `is_featured` to all 20 INSERT column lists and VALUES; 5 rows set TRUE, 15 set FALSE
