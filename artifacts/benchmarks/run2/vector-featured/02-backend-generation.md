# Backend Generation — Vector Retrieval Arm (run2/vector-featured)

Feature: `is_featured` admin-toggle field, end-to-end.

## Files Modified

| File | Change |
|------|--------|
| `model/entity/Book.java` | Added `isFeatured` field + `@Builder.Default`, null-guard in `prePersist` |
| `model/dto/response/BookResponse.java` | Added `private Boolean isFeatured` |
| `repository/BookRepository.java` | Added `findByIsFeaturedTrue(Pageable)` query method |
| `service/BookService.java` | Added `setFeatured(Long id, boolean featured)` interface method |
| `service/impl/BookServiceImpl.java` | Implemented `setFeatured` with `@Transactional` |
| `controller/BookController.java` | Added `@PatchMapping("/{id}/featured")` endpoint, `Map<String, Boolean>` body |
| `mapper/BookMapper.java` | Mapped `isFeatured` in all 4 methods: `toEntity`, `toResponse`, `updateEntityFromRequest`, `createDuplicate` |
| `Application.java` | Removed `@EnableJpaAuditing` (moved to `JpaAuditingConfig`) |
| `config/JpaAuditingConfig.java` | Created — holds `@EnableJpaAuditing` so `@WebMvcTest` slices load cleanly |
| `BookControllerTest.java` | Added `@Import(SecurityConfig.class)`, added `testSetFeatured_Success`, `testSetFeatured_NotFound_Returns404` |

## Verification

```
compile:  mvn -q compile test-compile  → exit 0 (no output)
tests:    mvn test -Dtest="BookControllerTest#testSetFeatured_Success+testSetFeatured_NotFound_Returns404"
          [INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
          [INFO] BUILD SUCCESS
```

## Vector Retrieval Block

| Attribute | Value |
|-----------|-------|
| Query | `isFeatured backend Book entity mapper controller service` |
| Chunks retrieved | 11 |
| Retrieved tokens (billed) | 2,285 |
| Skipped tokens (full-context would cost) | ~28,900 |
| Savings % | ~92.1% |
| Confidence | HIGH 91/100 |

## Fallback Reads (files not in retrieval chunks)

| File | Bytes read | Reason |
|------|-----------|--------|
| `BookService.java` | 4,090 | Method signature style for new interface method |
| `BookControllerTest.java` | 17,564 | Required to see test pattern before adding new tests |
| `Application.java` | 789 | Confirm `@EnableJpaAuditing` placement |
| `BookResponse.java` | 1,734 | See existing field list to add `isFeatured` correctly |
| `BookRepository.java` | 4,712 | See import structure before adding new method |
| `Book.java` | 5,790 | Full entity source (chunks had body summary, not full text) |
| `BookMapper.java` | 7,915 | Full mapper source needed to apply all 4 edits |
| `BookServiceImpl.java` | 10,525 | Full impl source to locate insertion point |
| `BookController.java` | 13,169 | Full controller source to add PATCH endpoint |
| `SecurityConfig.java` | 3,850 | Debug `@WebMvcTest` 403 — confirmed CSRF disabled, needed `@Import` fix |
| **Total fallback bytes** | **70,138** | **10 files** |

Note: Chunks for `BookMapper`, `BookController`, `BookServiceImpl`, and `Book.java` were class-body summaries (method signatures only), not full source — full reads were required to apply edits. This is a retrieval precision gap for this feature class.

---

TOKENS_VECTOR_FEATURED_BACKEND: retrieved_tokens=2285, fallback_bytes_read=70138, files_read_fallback=10

**Harness-reported usage (authoritative):**

| Metric | Value |
|--------|-------|
| `total_tokens` | **84,883** |
| `tool_uses` | 58 |
| `duration_ms` | 417,315 |

(Agent self-estimate of ~18,000 was retrieval-only and ignored prompt overhead + fallback Reads. Harness number is what we use for the A/B comparison. **Note: this is HIGHER than the baseline backend stage (54,131) — the 92% retrieval savings were eaten by 10 fallback Reads (70,138 bytes) because chunks were class-body summaries, not full source.**)
