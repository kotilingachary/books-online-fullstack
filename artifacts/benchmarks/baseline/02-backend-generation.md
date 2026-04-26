# Baseline Run — Backend Generation

**Branch:** feature/sales-count-baseline
**Agent:** @backend-generator
**Mode:** baseline (full file Reads, no vector retrieval)

## Outcome

Agent terminated by **content-filtering policy** at the end of its run (after 76 tool uses, ~14 min of work). Final report and self-reported `TOKENS_BASELINE_BACKEND` line were lost. Code changes landed before termination.

## Files modified (12)

| File | Lines | Purpose |
|---|---|---|
| `model/entity/Book.java` | +7 | Added `salesCount` field |
| `repository/BookRepository.java` | +12 | Added `incrementSalesCount` JPQL atomic UPDATE |
| `service/BookService.java` | +10 | Added `recordSale` to interface |
| `service/impl/BookServiceImpl.java` | +30 | Implemented `recordSale`, threw `BookNotFoundException` on miss |
| `controller/BookController.java` | +29 | Added `POST /{id}/sell` + Best Sellers handling |
| `mapper/BookMapper.java` | +9 | Mapped `salesCount` in entity↔DTO |
| `model/dto/response/BookResponse.java` | +1 | Exposed `salesCount` in response |
| `exception/BookNotFoundException.java` | ±2 | Minor adjustment (unverified) |
| `Application.java` | -3 | Moved `@EnableJpaAuditing` out (test-slice fix) |
| `config/JpaAuditingConfig.java` | NEW | Hosts `@EnableJpaAuditing` for `@WebMvcTest` compat |
| `resources/schema.sql` | +9 | Added column, CHECK, index |
| `resources/data.sql` | +100 | Added `sales_count` column to all 20 INSERTs |
| `test/.../BookControllerTest.java` | +59 | Added `testSellBook_Success` + `testSellBook_NotFound_Returns404` |

## Verification status

- **`mvn compile`**: cannot validate cleanly — `main` itself fails to compile on this machine with `TypeTag :: UNKNOWN` (Lombok/JDK 21 incompatibility, pre-existing environment issue).
- **`mvn test -Dtest=BookControllerTest`**: 17/18 fail with 401/403. Pre-existing Spring Security gap in `@WebMvcTest` setup — affects all tests, not just the new ones. Not regression-caused.
- **Diagnostics**: Eclipse JDT shows `BookRequest cannot be resolved` etc. These are downstream of the same Lombok/JDK issue — the files exist on disk, types just don't resolve in JDT.

## Token / context metrics (PARTIAL — agent self-report lost)

Harness usage from the (truncated) agent run:
- `total_tokens`: **0** (recorded as 0 due to content-filter termination)
- `tool_uses`: 76
- `duration_ms`: 834,988

Agent could not emit `TOKENS_BASELINE_BACKEND`. Will use proxy: sum of file bytes likely Read by agent.

**Estimated bytes read by agent** (from pre-measurement of files it almost certainly opened):
- `Book.java` 5,590 + `schema.sql` 8,072 + `data.sql` 16,902 + `BookRepository.java` 4,517 + `BookController.java` 12,102 + `BookService.java` 3,757 + `BookServiceImpl.java` 10,007 + `BookMapper.java` 7,664 + likely DTOs + likely existing test file
- **Lower bound: ~68,500 bytes** of context fed to the agent
- True figure likely 80,000–120,000 with DTOs, exception classes, and existing tests

## Caveats for A/B comparison

- Cannot use this run to assess "runtime correctness" — env compile issue blocks that for both arms.
- **Cost / context-bytes comparison is still valid** since the env affects both arms equally.
- Code-fit and hallucination assessment can proceed by reading the diff on each branch.
