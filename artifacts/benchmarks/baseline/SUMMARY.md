# Baseline Run 1 — Implementation Cost Summary

**Branch:** feature/sales-count-baseline
**Approach:** Full-context, direct file Reads, no vector retrieval
**Scope:** Sales-count feature (entity + endpoint + Best Sellers filter + UI display)

## Per-stage metrics

| Stage | total_tokens | bytes_read | files_read | tool_uses | duration |
|---|---|---|---|---|---|
| database-designer | 29,053 | 14,821 | 4 | 4 | 58s |
| backend-generator | LOST (content-filter)* | ~80,000–120,000 est. | ~10–15 | 76 | 14m |
| frontend-generator | 48,904 | 12,847 | 8 | 42 | 7m |
| **TOTAL** | **~230k–330k est.** | **~108k–148k bytes** | ~22–27 | 122 | 22m |

*The backend stage agent was killed by Anthropic content-filter just before emitting its final token line. Harness recorded `total_tokens: 0` due to the truncation, but the work happened (76 tool calls, ~14 minutes, 12 files modified). Estimate based on tool-use volume and file sizes.

## What was implemented

**Backend:**
- `Book.salesCount` field + `sales_count` column with CHECK + index
- `incrementSalesCount` atomic JPQL UPDATE in `BookRepository`
- `recordSale` in service; `POST /api/v1/books/{id}/sell` in controller
- Best Sellers via `bestSellers=true` query param on existing search endpoint
- `salesCount` exposed in `BookResponse` DTO
- Seed data: 20 INSERT rows updated with realistic distribution (0–22,100)
- `@EnableJpaAuditing` extracted to `JpaAuditingConfig` for `@WebMvcTest` compat

**Frontend:**
- `recordSale(id)` in `booksService.js`
- "Sold: N copies" in `BookDetailsPage`
- "Mark as Sold" button in `BookDetailsPage`
- "Best Sellers" toggle in `BooksListPage`
- Smoke test for sold count rendering

**Total files touched:** 13 modified + 3 created (16 files).

## Caveat

Implementation-cost benchmark only. Runtime/test pass not validated (per user direction).
