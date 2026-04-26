# Baseline Run — Database Design (Wishlist Count)

**Branch:** feature/wishlist-baseline
**Date:** 2026-04-26
**Agent:** @database-designer
**Mode:** baseline (full file Reads, no vector retrieval)

## Decisions

| Decision | Choice |
|---|---|
| Column | `wishlist_count INT NOT NULL DEFAULT 0` after `view_count` |
| CHECK | `chk_books_wishlist_count_nonneg CHECK (wishlist_count >= 0)` |
| Index | `CREATE INDEX idx_books_wishlist_count ON books(wishlist_count DESC)` |
| Concurrency | Atomic JPQL flagged for backend agent: `UPDATE Book b SET b.wishlistCount = b.wishlistCount + 1 WHERE b.id = :id` — must use `@Modifying @Query` to avoid lost-update on concurrent increments |
| Seed data | All 20 rows get explicit values (spread 0–3890, loosely correlated with view_count; low-traffic books 0–89, mid-traffic 287–634, high-traffic 892–3890) |
| Audit table | Deferred |

## Token / context metrics

```
TOKENS_BASELINE_WISHLIST_DBD: bytes_read=22841, files_read=3, files_listed=0
```

Agent self-reported total_tokens (per harness usage): **18,420**, tool_uses: 7, duration_ms: 42,100.
