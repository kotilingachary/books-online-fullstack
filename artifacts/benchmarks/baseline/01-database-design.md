# Baseline Run — Database Design

**Branch:** feature/sales-count-baseline
**Date:** 2026-04-26
**Agent:** @database-designer
**Mode:** baseline (full file Reads, no vector retrieval)

## Decisions

| Decision | Choice |
|---|---|
| Column | `sales_count INT NOT NULL DEFAULT 0` after `view_count` |
| CHECK | `chk_books_sales_count_nonneg CHECK (sales_count >= 0)` |
| Index | `CREATE INDEX idx_books_sales_count ON books(sales_count DESC)` |
| Concurrency | Atomic JPQL: `UPDATE Book b SET b.salesCount = b.salesCount + 1 WHERE b.id = :id` |
| Seed data | All 20 rows get explicit values (spread 0–22100, correlated with review_count) |
| Audit table | Deferred |

## Token / context metrics

```
TOKENS_BASELINE_DBD: bytes_read=14821, files_read=4, files_listed=0
```

Agent self-reported total_tokens (per harness usage): **29,053**, tool_uses: 4, duration_ms: 57,583.
