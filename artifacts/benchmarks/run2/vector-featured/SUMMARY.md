# Vector Run 2 — Implementation Cost Summary (is_featured)

**Branch:** feature/featured-vector
**Date:** 2026-04-26
**Approach:** Vector retrieval (`/tmp/books.db`) + manual append (frontend retrieval gap fallback)
**Scope:** Featured-flag feature (column + admin PATCH endpoint + UI badge + list filter)

## Per-stage metrics (harness-authoritative)

| Stage | total_tokens | tool_uses | duration_ms |
|---|---:|---:|---:|
| @database-designer | 44,641 | 33 | 222,810 |
| @backend-generator | 84,883 | 58 | 417,315 |
| @frontend-generator | 43,061 | 41 | 178,192 |
| **TOTAL** | **172,585** | 132 | 818,317 (~14 min) |

## Vector retrieval per stage

| Stage | Query | Retrieved tokens | Skipped tokens | Confidence | Savings claim |
|---|---|---:|---:|---|---:|
| Database | `add is_featured boolean field to Book entity schema` | 2,771 (7+2 pinned) | 27,472 | HIGH 84/100 | 91.2% |
| Backend | `add featured field to Book entity controller service repository mapper` | 2,285 (11) | 26,739 | HIGH 91/100 | 92.1% |
| Frontend | `add featured toggle to book details page and book list page` | 1,454 (7) | 26,558 | **MEDIUM 50/100** | 94.8% |

**The retrieval-only savings claim looks impressive (91-95%). The harness-authoritative numbers tell a different story** — see Comparison below.

## Fallback Reads (counted toward vector arm cost)

| Stage | Files | Bytes |
|---|---:|---:|
| Database | 1 (`data.sql` — chunks didn't include 20 INSERT rows for top-5 selection) | 14,924 |
| Backend | 10 (BookService, BookRepository, BookResponse, Book.java, BookMapper, BookServiceImpl, BookController, Application, BookControllerTest, SecurityConfig) | 70,138 |
| Frontend | 4 manual-append (BookDetailsPage, BooksListPage, booksService, api.js) + ~5 minor | 22,479 + ~9,200 |
| **Total** | **20** | **~116,741** |

**Reason chunks weren't enough:** the indexed chunks for `Book.java`, `BookMapper`, `BookController`, `BookServiceImpl` were class-body **summaries** (method signatures only), not full source. Edits required full source. This is a retrieval-precision gap for "modify in place" tasks.

## What was implemented

### Backend (8 files modified, 1 created)
- `Book.isFeatured` field with `@Builder.Default false`, prePersist null-guard
- `BookRepository.findByIsFeaturedTrue(Pageable)` query method
- `BookService.setFeatured(Long, boolean)` interface method
- `BookServiceImpl.setFeatured` impl with `@Transactional`, `BookNotFoundException` on miss
- `BookController` `@PatchMapping("/{id}/featured")` body `{"featured": true|false}`, returns updated `BookResponse`
- `BookMapper` mapped in all 4 methods (`toResponse`, `toEntity`, `updateEntityFromRequest`, `createDuplicate`)
- `BookResponse.isFeatured` exposed
- `JpaAuditingConfig` extracted (same fix as baseline)
- 2 controller tests with `@Import(SecurityConfig.class)` + `@WithMockUser` + CSRF

### Frontend (3 modified, 2 created)
- `setFeatured(id, featured)` in `booksService.js`
- "★ Featured" badge + "Toggle Featured" button on `BookDetailsPage` (in-flight disable, state update from response)
- "Show Featured Only" checkbox on `BooksListPage` (client-side filter), "★" cover badge per featured card
- 3 smoke tests on `BookDetailsPage.test.jsx`
- `.eslintrc.cjs` created (was missing on this branch — same gap as baseline)

**Total files touched:** 11 modified + 4 created (15 files — same fan-out as baseline, structurally isomorphic).

## Verification

- `mvn -q compile` → exit 0
- `mvn test -Dtest=BookControllerTest#testSetFeatured_*` → **2/2 PASS**
- `npm run lint` → exit 0
- **Browser smoke (live):** `GET /api/v1/books/1` returns `isFeatured=false`. `PATCH /api/v1/books/1/featured {"featured":true}` returns 200 + `isFeatured=true`. Spring DevTools hot-reloaded the new schema.

## Pre-run fixes applied (from `00-pre-run-fixes.md`)

| Gap | Fix | Outcome |
|---|---|---|
| `seed-data.sql` duplicates | `DELETE FROM summaries WHERE file_path LIKE '%seed-data.sql'` | 186 → 184 chunks |
| Frontend retrieval gap | Manual append of 4 frontend files | Cross-language gap CONFIRMED — only 1 frontend chunk in top-7, MEDIUM confidence on frontend query |
| Backend content-filter kill | "Table-only, max 500 words" prompt | No content-filter kill on either arm of run2 |

## Caveats

- The 91-95% retrieval-only savings figures **mislead in isolation** — they ignore the fallback Reads needed to actually edit code. With fallbacks counted, the vector arm uses MORE tokens than the baseline arm (172,585 vs 123,631).
- Index granularity is the root cause: chunks are class-body summaries, not full file content. For "modify in place" tasks the agent needs full source. This wasn't visible in the prior single-field benchmark because adding a column to an entity is small enough to do from a class-body summary alone.
- The frontend cross-language retrieval gap reproduced exactly: cosine ranks Java higher than JSX even when the query is JSX-focused.
