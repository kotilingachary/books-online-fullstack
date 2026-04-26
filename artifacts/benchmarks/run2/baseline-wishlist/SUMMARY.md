# Baseline Run 2 — Implementation Cost Summary (Wishlist Count)

**Branch:** feature/wishlist-baseline
**Date:** 2026-04-26
**Approach:** Full-context, direct file Reads, no vector retrieval
**Scope:** Wishlist-count feature (column + atomic increment endpoint + UI button + count display)

## Per-stage metrics

| Stage | total_tokens | tool_uses | duration_ms |
|---|---:|---:|---:|
| @database-designer | 18,420* | 9 (harness) / 7 (self) | 136,327 (harness) / 42,100 (self) |
| @backend-generator | **54,131** | 42 | 290,488 |
| @frontend-generator | **35,186** | 39 | 171,549 |
| **TOTAL** | **107,737** | 90 | 598,364 (~10 min) |

*Self-reported. Harness `total_tokens` was `34,314` for the database stage; using the agent's self-report which subtracts framework-overhead tokens. Discrepancy noted; for the A/B comparison we use **harness numbers as authoritative** below.

## Per-stage metrics (harness-authoritative — used for A/B)

| Stage | total_tokens | tool_uses | duration_ms |
|---|---:|---:|---:|
| @database-designer | 34,314 | 9 | 136,327 |
| @backend-generator | 54,131 | 42 | 290,488 |
| @frontend-generator | 35,186 | 39 | 171,549 |
| **TOTAL** | **123,631** | 90 | 598,364 |

## What was implemented

### Backend (8 files modified, 1 created)
- `Book.salesCount`-style field `wishlistCount` + `wishlist_count` column with CHECK + DESC index
- `incrementWishlistCount` atomic JPQL UPDATE in `BookRepository` (mirrors `incrementViewCount` pattern)
- `addToWishlist` in `BookService` interface; impl throws `BookNotFoundException` when update affects 0 rows
- `POST /api/v1/books/{id}/wishlist` in `BookController`, returns 200 + updated `BookResponse`
- `wishlistCount` exposed in `BookResponse` DTO; mapped in all `BookMapper` methods (`toResponse`, `toEntity`, `createDuplicate`)
- Seed data: 20 INSERT rows with `wishlist_count` values spread 0–3,890 (correlated with view_count)
- `@EnableJpaAuditing` extracted to `JpaAuditingConfig` for `@WebMvcTest` compat (same fix as prior sales-count run)
- 2 new controller tests with `@WithMockUser` + CSRF: success + 404 paths

### Frontend (3 modified, 3 created)
- `addToWishlist(id)` in `booksService.js`
- "Wishlisted: N times" pill + "Add to Wishlist" button on `BookDetailsPage` (in-flight disable, success flash)
- "♡ N" count + "♡ Add" button on each `BooksListPage` card (optimistic count bump, flash highlight)
- Smoke test for click → count-update flow on details page
- `.eslintrc.cjs` created (was missing on this branch — same gap as prior baseline)

**Total files touched:** 11 modified + 4 created (15 files).

## Verification

- `mvn -q compile` → exit 0 (Lombok/JDK 25 `TypeTag :: UNKNOWN` warnings only, pre-existing)
- `mvn test -Dtest=BookControllerTest#testAddToWishlist_*` → **2/2 PASS**
- `npm run lint` → exit 0, zero warnings
- **Browser smoke (live):** `POST /api/v1/books/1/wishlist` returned 200, count went 312 → 313 on book 1. `GET /api/v1/books/1` confirms `wishlistCount` field present in response. Running app picked up new code without restart (Spring DevTools hot-reload).

## Comparison vs prior baseline (sales-count, 2026-04-26)

| Metric | Sales-count baseline | Wishlist baseline |
|---|---:|---:|
| Total tokens | ~230k–330k est. (backend lost) | **123,631** (full capture) |
| Tool uses | 122 | 90 |
| Duration | 22 min | 10 min |
| Backend agent killed by content-filter? | YES (at tool 76) | **NO** (table-only summary worked) |

The "table-only final report" mitigation worked: backend agent completed cleanly with full token capture (54,131). This is the first end-to-end baseline run on this repo with **no lost tokens**.

## Caveats

- Implementation-cost benchmark only. Test pass rate = 2 new + lint clean; full regression suite not run.
- Database stage harness vs self-report discrepancy of 16k tokens — the harness number includes prompt overhead (this benchmark, the agent definition, the system prompt). Using harness numbers consistently across both arms keeps the A/B fair.
