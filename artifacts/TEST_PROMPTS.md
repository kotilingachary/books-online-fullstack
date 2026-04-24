# CodeQuantum Retrieval — Test Prompt Matrix

Systematic validation set for the 6-stage pipeline against the live DB at
`/tmp/books-online-vectors-enriched.db` (13 files, 90 chunks, Books Online
fullstack corpus).

Each prompt is designed to probe one or more retrieval stages with a
known-good expected output. Run with:

```bash
python3 ~/.claude/scripts/vectordb_search.py \
  --db /tmp/books-online-vectors-enriched.db \
  "<query>"
```

Acceptance = **expected chunks appear with the correct STATUS tag** AND
the confidence band matches. If a probe fails, the stage diagnosis
column tells you where to look.

---

## Stages and their trigger conditions

```text
Stage 1  hierarchy pre-filter     fires when query_type=broad AND L2 gap > 0.10
Stage 2  cosine similarity        always fires
Stage 3  AST tag pinning          fires when query matches task-type keyword
Stage 4a forward call graph       fires when top-k chunk's `calls` matches a
                                  stored function_name
Stage 4b reverse callers          fires when top-k chunk's function_name
                                  appears in another chunk's `calls`
Stage 4b reverse importers        fires when top-k chunk's class_name (or
                                  *Impl→interface) appears in `imports`
Stage 5  mapper group             fires when a retrieved chunk's file_path
                                  matches %Mapper%
Confidence gate                   5 signals → 0-100 score
```

---

## Group A — Stage isolation (each stage fires alone)

### A1. Stage 2 only — pure cosine, nothing else

**Prompt:** `how is pagination implemented in the book listing`

```text
Expected
────────
Query type     : targeted (no "explain/how does/what is" phrasing)
Hierarchy      : targeted path skipped
Stages firing  : 2 only
Top 1-3        : BookController.getAllBooks, BookServiceImpl.getAllBooks,
                 BookController.searchBooks
Pinned         : 0
Call graph     : 0 (getAllBooks has no interesting calls to other chunks)
Reverse        : 0 or low (getAllBooks is a leaf in both directions here)
Confidence     : HIGH (70+) — clean disambiguation
```

**Pass criteria:** Pagination methods dominate top-3. No noise from
entity or exception layers.

---

### A2. Stage 3 only — AST pinning forces lifecycle chunk

**Prompt:** `add a country_code field to Book entity`

```text
Expected
────────
Query type     : targeted
Stages firing  : 2 + 3 + possibly 4a
Top 1          : Book.java prePersist  OR  Book.java (constructor/field area)
Pinned         : Book.prePersist, Book.preUpdate  (lifecycle:pre_persist,
                 lifecycle:pre_update tags)
Confidence     : HIGH — known-good case, 3 signals align
```

**Pass criteria:** `prePersist` and `preUpdate` appear with STATUS
`PINNED(lifecycle:*)` even if their cosine score is mediocre. This is
the canonical proof that AST pinning works.

---

### A3. Stage 4a only — forward call graph

**Prompt:** `what does the update book flow look like end to end`

```text
Expected
────────
Top 1-5        : BookController.updateBook, BookServiceImpl.updateBook,
                 and related update-path methods
Call graph     : 1-2 — updateBook's `calls` column references mapper /
                 repository methods. Any that match a stored function_name
                 surface as CALL_GRAPH(name).
Reverse        : low — update has few upward callers beyond controller
Confidence     : HIGH
```

**Pass criteria:** At least one row labelled `CALL_GRAPH(...)` appears,
pulling in a callee that was not in the cosine top-k.

---

### A4. Stage 4b reverse callers — `createBook` wrapper case

**Prompt:** `how does BookServiceImpl.createBook work`

```text
Expected
────────
Top 1          : BookServiceImpl.java createBook  (exact hit)
Reverse        : 4 — REV_CALLER(createBook) on BookController.createBook
                 AND AddBookPage.jsx AddBookPage  (JSX caller).
                 Also REV_CALLER(exportBook/getBookById/duplicateBook)
                 from other BookServiceImpl methods in top-k spilling over.
Confidence     : HIGH
```

**Pass criteria:** `BookController.createBook` is pulled in as
`REV_CALLER(createBook)` even though it shares the same function name
as the source. This is the **wrapper-method fix** from this session.

---

### A5. Stage 4b reverse importers — `*Impl`→interface fallback

**Prompt:** `what uses BookService`

```text
Expected
────────
Top 1-5        : BookServiceImpl methods (cosine loves "BookService")
Reverse        : At least 3 REV_IMPORT(BookService) — BookController
                 methods imported BookService (interface), not the Impl.
                 Plus REV_CALLER entries from the service methods.
Confidence     : HIGH
```

**Pass criteria:** REV_IMPORT fires with `BookService` (the interface,
NOT `BookServiceImpl`). Proves the `*Impl` stripping heuristic.

---

### A6. Stage 1 only — broad query, hierarchy pre-filter

**Prompt:** `explain the controller layer`

```text
Expected
────────
Query type     : broad
Hierarchy path : L2 module "controller" picked with confident gap > 0.10
                 → L4 search restricted to controller files
Top 1-5        : Only BookController methods
Pinned         : 0
Call graph     : 0 or low
Reverse        : 0 or low — no cross-layer reverse matches under the
                 narrowed scope
Confidence     : HIGH
```

**Pass criteria:** ZERO non-controller chunks in top-5. If service or
entity methods appear, the hierarchy pre-filter is failing or leaking.

---

### A7. Stage 5 — mapper group (NEGATIVE probe)

**Prompt:** `how are book fields mapped between request and entity`

```text
Expected
────────
Top 1-5        : BookServiceImpl.createBook / updateBook (which reference
                 mapper calls) — NO chunks with file_path matching %Mapper%
                 because Mapper isn't indexed as chunks in this DB.
Stage 5        : does NOT fire (no retrieved chunk has %Mapper% path)
Confidence     : MEDIUM (gap may be small; mapper concept is in the calls
                 column but mapper code itself isn't in DB)
```

**Pass criteria:** No false-positive mapper-group expansion. Header
shows normal call_graph count, no phantom rows. This is a regression
guard for Stage 5's file-path LIKE pattern.

---

## Group B — Stage interactions

### B1. Forward + reverse on same source chunk (dedup check)

**Prompt:** `trace the duplicate book functionality`

```text
Expected
────────
Top 1-2        : BookController.duplicateBook, BookServiceImpl.duplicateBook
Call graph     : may surface getBookById / save via forward
Reverse        : may surface nothing new — BookController.duplicateBook is
                 already retrieved, and AddBookPage does not call duplicate
Confidence     : HIGH

Key check: retrieved_set dedup works. If a chunk is both a callee (4a) AND
a caller (4b), it appears exactly ONCE in output, with a single STATUS tag.
```

**Pass criteria:** No duplicate rows. Counts in header are consistent:
`top-k + pinned + call_graph + reverse + mapper = sent to Claude`.

---

### B2. Broad query that ALSO triggers AST pinning

**Prompt:** `where are entity lifecycle hooks defined`

```text
Expected
────────
Query type     : broad (starts with "where are")
Hierarchy      : may pick L2 "model" or "entity"
Pinned         : Book.prePersist, Book.preUpdate (lifecycle tags)
Confidence     : MEDIUM-HIGH
```

**Pass criteria:** Pinning fires even on a broad query. Hierarchy
pre-filter doesn't suppress Stage 3.

---

### B3. Stage 4a with JPA-interface-method target (no-chunk case)

**Prompt:** `if I change existsByIsbn what breaks`

```text
Expected
────────
Top-k          : BookServiceImpl.createBook, BookServiceImpl.updateBook
                 (they invoke existsByIsbn — cosine picks them up via the
                 string "existsByIsbn" in their code)
Call graph     : existsByIsbn is in their `calls` column but has NO chunk
                 in summaries (it's a JPA interface method, filtered at
                 index time). Stage 4a silently skips. NO crash.
Reverse        : 0 — no chunk exists TO be reverse-looked-up
Confidence     : MEDIUM (weak lexical, no pin)

Real answer comes from forward-from-callers: BookController.createBook
/ updateBook surface via REV_CALLER(createBook|updateBook) because
service methods ARE in top-k.
```

**Pass criteria:** No crash. existsByIsbn gracefully missing from DB.
REV_CALLER chain surfaces the impact set anyway.

---

### B4. Per-source cap enforcement (max_per_chunk = 3)

**Prompt:** `show me all the service layer methods`

```text
Expected
────────
Top 1-5        : 5 different BookServiceImpl methods
Reverse        : Each top-k service method could in theory drag in its
                 own caller. With cap = 3 per source, the TOTAL reverse
                 importers should be ≤ 3 (since they all resolve to the
                 same `BookService` interface, so only 3 distinct
                 importers surface).
                 Reverse callers: capped at 3 per source × up-to-5
                 sources = ≤ 15 max, but deduped.
Confidence     : HIGH
```

**Pass criteria:** Total reverse rows in header does not explode. No
single BookController method appears more than once even if 5 sources
would all pull it.

---

### B5. Targeted query, cross-module cosine (no pre-filter leak)

**Prompt:** `what exceptions are thrown when book is not found`

```text
Expected
────────
Query type     : targeted
Hierarchy      : skipped
Top 1-3        : GlobalExceptionHandler.handleBookNotFoundException,
                 BookServiceImpl.getBookById (throws BookNotFoundException)
Call graph     : may surface BookNotFoundException chunk IF indexed
                 (likely not — it's a class def, not a function)
Confidence     : HIGH
```

**Pass criteria:** Cross-layer results surface naturally (exception
handler + service method). Hierarchy pre-filter does NOT prematurely
narrow scope on a targeted query.

---

## Group C — Confidence gate bands

### C1. HIGH confidence (70+)

**Prompt:** `add country_code field to Book entity`
(same as A2)

Expected: score ≥ 70. All 5 signals align: top_score ~0.55, gap > 0.05,
"country_code" hits lexical, entity+lifecycle layer diversity, AST pin
hits.

---

### C2. MEDIUM confidence (40-69)

**Prompt:** `error handling logic`

```text
Expected
────────
Top 1-5        : GlobalExceptionHandler methods, plus maybe
                 BookServiceImpl.createBook (has try/catch patterns)
Pinned         : 0
Score gap      : small (generic query)
Lexical        : "error", "handling" — weak hits
Layer diversity: exception only → low
AST pinning    : 0 → low signal
Confidence     : 40-65, WARN printed
```

**Pass criteria:** MEDIUM band triggers, warning appears in output.

---

### C3. LOW confidence (< 40) — out-of-domain query

**Prompt:** `how is payment processing handled`

```text
Expected
────────
Top 1-5        : essentially random — nothing payment-related in DB
Top score      : low (< 0.30)
Lexical        : 0
Layer diversity: low
AST pinning    : 0
Confidence     : < 40, LOW warning (or fallback message if enabled)
```

**Pass criteria:** Gate correctly detects "nothing useful retrieved."
This is the safety net against Claude confidently answering from noise.

---

### C4. LOW confidence — vague English, no concrete nouns

**Prompt:** `how does this project work`

```text
Expected
────────
Query type     : broad
Hierarchy      : may pick L0 repo or L1 frontend/backend
Top 1-5        : mix across layers
Confidence     : LOW — no focused signal
```

**Pass criteria:** LOW flag fires. Output suggests broader file-level
read, not precise chunks.

---

## Group D — Edge cases and regression guards

### D1. Interface methods with 0 input_tokens filtered out

**Sanity SQL, not a prompt:**

```bash
sqlite3 /tmp/books-online-vectors-enriched.db \
  "SELECT COUNT(*) FROM summaries WHERE input_tokens = 0;"
```

**Pass criteria:** `0`. No empty-body chunks should exist in the DB.
Confirms BookRepository interface methods were filtered at index time.

---

### D2. Framework imports correctly skipped

**Sanity SQL:**

```bash
sqlite3 /tmp/books-online-vectors-enriched.db \
  "SELECT imports FROM summaries WHERE file_path LIKE '%BookController%' LIMIT 1;" \
  | tr ',' '\n' | grep -E "^(java|javax|org\.springframework|lombok)\." | wc -l
```

**Pass criteria:** `0`. Skip-prefix list correctly excludes framework
FQNs. `io.swagger.*` currently does leak through — document as a known
gap or add to skip list.

---

### D3. Same function name across layers (collision probe)

**Prompt:** `explain the createBook implementations across layers`

```text
Expected
────────
Top 1-3        : Both BookServiceImpl.createBook AND BookController.createBook
                 surface in top-5 via cosine
Reverse        : adds AddBookPage.jsx via REV_CALLER(createBook)
Confidence     : HIGH
```

**Pass criteria:** Both `createBook` methods (different files, same
name) appear and are NOT collapsed. Hash key is `(file_path, fn_name)`
— this probe validates that.

---

### D4. Query language the DB doesn't know

**Prompt:** `how does the C++ parser integrate`

```text
Expected
────────
Top 1-5        : low-scoring mix
Confidence     : LOW
```

**Pass criteria:** No crash, no false positives from Python script
chunks that mention "parser". Graceful "nothing relevant" result.

---

### D5. Case / punctuation robustness

**Prompt:** `ADD COUNTRY_CODE FIELD TO BOOK ENTITY!!!`

Same as A2. Expected: identical ranking to A2 (case-insensitive
embedding, punctuation is noise to all-MiniLM).

**Pass criteria:** Top-5 overlap with A2 ≥ 4 out of 5.

---

### D6. Very short query

**Prompt:** `isbn`

```text
Expected
────────
Top 1-3        : BookServiceImpl.createBook / updateBook (validate isbn),
                 Book.java constructor / field
Confidence     : MEDIUM (single-word, weak disambiguation)
```

**Pass criteria:** Returns something coherent. Doesn't throw on
sub-3-char tokens (respects `query_word_min_length=3` filter).

---

### D7. Query matching a Python script (corpus mixing)

**Prompt:** `how does the hierarchy rollup algorithm work`

```text
Expected
────────
Top 1-3        : build_hierarchy.py rollup_summary / build_hierarchy
Call graph     : may surface parse_path / ensure_hierarchy_table
Confidence     : HIGH (strong lexical, clean match)
```

**Pass criteria:** Python chunks surface correctly. Cross-language
retrieval works — not just Java.

---

## Group E — Confidence-gate dimensions (individual signal probes)

Probe each confidence signal independently.

### E1. High `top_score` signal

Any precise query like `getBookById method` — top cosine ≥ 0.60.

### E2. High `score_gap` signal

Query with one obvious answer vs noise: `which file is the Spring Boot entry point`
→ Application.java.main should dominate by ~0.15.

### E3. High `lexical_match` signal

Query where query nouns appear verbatim in retrieved code:
`duplicateBook controller endpoint` — "duplicateBook" literally in file.

### E4. High `layer_diversity` signal

Cross-layer task: `trace createBook from UI to database` — retrieves JSX
+ Controller + Service + Entity → 4 distinct layer tags.

### E5. High `ast_pinning` signal

Any `add field` / `schema change` query — AS A2 does.

---

## Group F — Stress / chaos probes

### F1. Very long query (>50 words)

**Prompt:**
```text
I am trying to understand the complete lifecycle of a book object from
the moment a user clicks submit on the add book form in the React
frontend through the controller layer the service layer validation
including the ISBN uniqueness check the mapper transformations and
finally the JPA persistence with lifecycle callbacks that update the
audit timestamps before the entity is written to the database
```

**Pass criteria:** No crash. Query embedding works on long text. Top-5
picks up AddBookPage + Controller + Service + Book.prePersist — a
cross-layer trace.

---

### F2. Query that is pure symbols / gibberish

**Prompt:** `!@#$%^&*()`

**Pass criteria:** LOW confidence. No crash. Confidence gate correctly
refuses to pretend this is a real query.

---

### F3. Empty query (if allowed)

**Prompt:** ``   `` (whitespace only)

**Pass criteria:** Either error-out cleanly OR return "no query"
message. Must not send 90 random chunks to Claude.

---

## Group G — Real-world developer intents

These mirror how engineers actually phrase questions. They're less
about stage isolation and more about "does retrieval do the right thing
on messy, natural queries."

### G1. Multi-intent compound query

**Prompt:** `add country_code field to Book entity and update the controller to accept it`

```text
Expected
────────
Pinned         : Book.prePersist, Book.preUpdate (entity pin fires)
Top            : BookController.createBook / updateBook (controller pin
                 via cosine), BookServiceImpl.createBook / updateBook
Reverse        : REV_CALLER on the service methods pulls controller if
                 not already in top-k
Layer diversity: 3+ → HIGH signal
Confidence     : HIGH
```

**Pass:** Both concerns surface. Layer-diversity signal exceeds 75.

---

### G2. Negation / exclusion (system does NOT handle — document)

**Prompt:** `show me everything except the controller layer`

```text
Expected
────────
Behavior      : System ignores "except" — top-5 WILL include controller
                methods because cosine is attraction-only.
Confidence    : HIGH (false high)
```

**Pass:** Document as a known limitation. Retrieval is monotone; users
asking for exclusions get the opposite of what they want. Future work:
query parser for "not"/"except"/"without".

---

### G3. Security-intent query

**Prompt:** `is there any SQL injection risk in this codebase`

```text
Expected
────────
Top 1-5        : Repository method calls (if any use string concat),
                 service methods doing validation, exception handlers
Call graph     : may surface repository methods via forward
Reverse        : may surface controllers via REV_CALLER
Confidence     : MEDIUM — no literal "SQL" in Spring Data JPA code
                 (methods are interface-derived), lexical signal weak
```

**Pass:** System doesn't crash on security framing. Returns methods
that handle raw input (createBook, updateBook, searchBooks). Honest
MEDIUM confidence.

---

### G4. Architectural / opinion query (should land LOW)

**Prompt:** `why is BookServiceImpl a class instead of a static function collection`

```text
Expected
────────
Top 1-5        : BookServiceImpl methods (cosine attracts to the name)
Confidence     : LOW-to-MEDIUM — lexical match on proper nouns but
                 semantic intent (design rationale) can't be answered
                 from code alone
```

**Pass:** Gate correctly flags that code can't answer "why design"
questions. Warns Claude.

---

### G5. Cross-file dependency (frontend → backend)

**Prompt:** `which frontend components hit the books API`

```text
Expected
────────
Top 1-5        : AddBookPage.jsx, App.jsx, maybe BookServiceImpl
Reverse        : REV_IMPORT — frontend files import booksService (a
                 module, not indexed as chunks here), so this probes
                 how gracefully the importer resolves missing targets
Confidence     : MEDIUM
```

**Pass:** Frontend chunks surface. System doesn't error when
importer-side class (`booksService`) isn't in summaries.

---

### G6. Test-term penalty (config penalty validation)

**Prompt:** `test book creation flow`

```text
Expected
────────
Penalty firing : "test" pattern in query words triggers score -5 against
                 any chunk with "test" in text. Books Online has no test
                 chunks in DB → penalty should NOT suppress valid code.
Top 1-5        : Normal createBook retrieval as in A4
Confidence     : HIGH or MEDIUM
```

**Pass:** Penalty doesn't accidentally demote production code that
happens to contain the word "test" in a comment or string.

---

### G7. Query containing file path / code syntax

**Prompt:** `BookController.java::updateBook signature`

```text
Expected
────────
Top 1          : BookController.updateBook (filename + method name match
                 both lexical and cosine)
Confidence     : HIGH
```

**Pass:** Special chars (`.`, `::`) don't break query embedding or
lexical matcher. Robust to code-style phrasings developers naturally
type.

---

### G8. Interleaved code and English

**Prompt:** `when getBookById(id) throws BookNotFoundException who handles it`

```text
Expected
────────
Top 1-3        : BookServiceImpl.getBookById,
                 GlobalExceptionHandler.handleBookNotFoundException
Call graph     : may pull BookNotFoundException constructor if indexed
Confidence     : HIGH — strong lexical (two proper nouns)
```

**Pass:** Both the thrower and the handler surface. Layer diversity ≥ 2.

---

### G9. Typo in query

**Prompt:** `creatBook servce` (two typos)

```text
Expected
────────
Top 1-5        : Still retrieves createBook service methods — embeddings
                 are robust to small edit distance
Lexical score  : low (typo'd words miss exact match)
Confidence     : HIGH or MEDIUM (top_score carries)
```

**Pass:** Typo tolerance via embedding fuzziness. No hard failure on
misspelled proper nouns.

---

### G10. Non-English query (corpus is English)

**Prompt:** `comment fonctionner la creation de livre` (French)

```text
Expected
────────
Top 1-5        : MiniLM is multilingual-capable for common terms.
                 May land on createBook via the proper noun bleed, but
                 lexical = 0.
Confidence     : LOW-MEDIUM
```

**Pass:** No crash. Reasonable MEDIUM result or clean LOW fallback.
Documents the real behavior (untrained on French code corpora).

---

### G11. Ambiguous pronoun, no subject

**Prompt:** `how does it validate`

```text
Expected
────────
Top 1-5        : scattered — no anchor noun
Confidence     : LOW
```

**Pass:** LOW gate fires. System doesn't pretend to know what "it" is.

---

### G12. Two-hop trace request (one-hop limit probe)

**Prompt:** `trace from AddBookPage.jsx to Book.prePersist`

```text
Expected
────────
Top 1          : AddBookPage.jsx (cosine pulls it)
Call graph     : AddBookPage calls createBook → BookServiceImpl.createBook
                 surfaces (one hop, Stage 4a)
                 But prePersist is TWO hops away (service.save → JPA → @PrePersist)
                 — won't surface via forward graph
Pinned         : Book.prePersist MAY fire via AST pinning if query parses
                 as schema/lifecycle task
Confidence     : MEDIUM — partial trace
```

**Pass:** Documents the one-hop limit honestly. Shows that AST pinning
is the fallback for multi-hop lifecycle traces.

---

### G13. Performance / runtime intent

**Prompt:** `is getAllBooks slow with many books`

```text
Expected
────────
Top 1-3        : BookServiceImpl.getAllBooks, BookController.getAllBooks
Call graph     : repository.findAll() (not indexed — JPA) missed; may
                 surface searchBooks (pagination neighbor)
Confidence     : MEDIUM
```

**Pass:** Surfaces the function. System can't actually answer "is it
slow" but provides the code for Claude to reason about.

---

### G14. Git / temporal intent (system has no git awareness)

**Prompt:** `what changed in BookController recently`

```text
Expected
────────
Top 1-5        : BookController methods — cosine attraction to name
Confidence     : MEDIUM — no temporal filtering
```

**Pass:** Returns BookController methods without hallucinating change
history. Documents that retrieval is snapshot-based, not
commit-range-based.

---

### G15. Forward call graph one-hop limit (A→B→C does NOT spider)

**Prompt:** `what happens inside AddBookPage when the user clicks submit`

```text
Expected
────────
Top 1          : AddBookPage.jsx AddBookPage
Call graph (4a): One hop — AddBookPage calls createBook → pulls
                 BookServiceImpl.createBook (if not already in top-k)
Second hop     : BookServiceImpl.createBook itself calls existsByIsbn,
                 toEntity, save, toResponse. NONE of these should
                 surface through call graph. Stage 4a runs ONLY on
                 pre_stage4_keys (the original top-k + pinned),
                 never on chunks added by 4a itself.
Reverse (4b)   : ditto — only anchored on pre_stage4_keys
Confidence     : MEDIUM-HIGH
```

**Pass criteria:** Chunks two hops away (`existsByIsbn`, `toEntity`,
`save`) do NOT appear as CALL_GRAPH rows. The retrieved set is
bounded to one hop. Header's `N call_graph` count stays small (≤ 3).
Without this guard, the pipeline would spider the entire transitive
call closure.

**How to verify:** Inspect the output. Count `CALL_GRAPH(...)`
tags — they should only resolve callees of the top-5 (and of pinned
chunks), never callees of other CALL_GRAPH rows.

---

### G16. Config-file concepts (meta-query about the system itself)

**Prompt:** `what are the confidence weights and thresholds`

```text
Expected
────────
Top 1-3        : vectordb_search.py get_confidence_params,
                 vectordb_search.py compute_confidence_full (or similar —
                 Python chunks that mention "confidence" + "weight")
Lexical        : "confidence" and "weights" hit Python chunks
Call graph     : may surface load_config / is_confident
Reverse        : low — scoring helpers aren't called from many places
Confidence     : MEDIUM — query is real but target isn't a business
                 function; it's the retrieval system itself
Note           : vectordb_config.json is NOT indexed as a chunk (it's
                 a config file, not a function). So the query lands on
                 the *Python code that reads the config*, not the JSON
                 itself.
```

**Pass criteria:** System gracefully handles meta-queries about its own
code. Top results are Python helpers (`get_confidence_params`,
`compute_confidence_full`, `load_config`) not Books Online chunks.
Documents a limitation: answers about config *values* require the
config file to be read separately — retrieval only finds *code that
handles config*, not config itself.

---

### G17. Implementation vs documentation intent

Two probes with the same noun but different verbs. The retrieval
pipeline doesn't distinguish intent — both return the same chunks —
but the test documents whether that's acceptable for each framing.

**Prompt A (documentation intent):** `explain the book entity schema`

```text
Expected
────────
Top 1-3        : Book.java prePersist, Book.java preUpdate, Book.java
                 incrementViewCount  (all the chunks we have for the
                 entity)
Pinned         : Book.prePersist, Book.preUpdate (lifecycle tags)
Confidence     : HIGH
```

**Prompt B (implementation intent):** `change the book entity schema to add an author_email column`

```text
Expected
────────
Top 1-3        : Same as above — Book.java chunks
Pinned         : Same lifecycle chunks
Additionally   : Maybe AddBookPage.jsx (frontend form) via layer
                 diversity, BookController createBook/updateBook via
                 cosine (they consume the schema)
Confidence     : HIGH
```

**Pass criteria:** Both return Book.java chunks with lifecycle pinning.
Prompt B additionally pulls cross-layer chunks (controller/frontend)
that an implementation task would need. Retrieval is verb-agnostic —
that's acceptable because Claude gets the full code either way and
formats its answer per the user's framing. **Documents that the system
treats "explain" and "change" identically on the retrieval side.**

---

### G18. Pure code-invocation + English trail

**Prompt:** `when getBookById(id) is called what happens`

```text
Expected
────────
Top 1          : BookServiceImpl.getBookById (exact token hit)
Call graph     : getBookById calls `incrementViewCount` on the entity
                 → Book.java incrementViewCount surfaces as
                 CALL_GRAPH(incrementViewCount) if indexed
Reverse        : BookController.getBookById surfaces as
                 REV_CALLER(getBookById)
Confidence     : HIGH
```

**Pass criteria:** Parenthesized call `getBookById(id)` doesn't confuse
query embedding or keyword scoring — `(id)` is tokenizer noise, and
`getBookById` as a discrete token still matches. The forward call
graph exposes the method's effect (incrementViewCount), and reverse
shows its entry point (controller).

**Contrast with G8:** G8 probes exception-handler linkage (`throws X
who handles it`). G18 probes forward traversal (`is called what
happens`). Similar syntactic shape, different stage expected to
dominate — G8 tests cross-layer cosine, G18 tests Stage 4a.

---

### G19. Very generic noun clash ("Entity", "Service")

**Prompt:** `what does the Entity do`

```text
Expected
────────
Top 1-5        : Book.java (class marked @Entity)
Reverse import : class name "Entity" would false-match ANY import
                 containing ".Entity" — but Book.java's filename-stem
                 is "Book", not "Entity", so the probe uses "Book".
                 No false expansion.
Confidence     : MEDIUM
```

**Pass:** Heuristic protection holds. Generic-sounding query doesn't
cause an import-match explosion because the source-chunk's derived
class is the concrete class name, not the abstract concept.

---

## Group H — Operational / config toggles

These validate that config changes take effect without code edits.

### H1. Reverse lookup disabled

```bash
# Edit ~/.claude/scripts/vectordb_config.json
# Set reverse_lookup.enabled = false

python3 ~/.claude/scripts/vectordb_search.py \
  --db /tmp/books-online-vectors-enriched.db \
  "how does BookServiceImpl.createBook work"
```

**Expected:** Header shows `0 reverse`. All REV_* tags disappear.
Forward call graph still fires.

**Pass:** Config switch works. Baseline for A/B comparing with A4.

---

### H2. Reverse callers only (no importers)

```bash
# reverse_lookup.include_importers = false
```

**Expected:** REV_IMPORT tags disappear. REV_CALLER still fires.

**Pass:** Sub-toggles work independently.

---

### H3. Confidence fallback enabled

```bash
# confidence.fallback_on_low_confidence = true
```

**Prompt:** `how is payment processing handled` (same as C3)

**Expected:** LOW confidence triggers a fallback message suggesting
file-read instead of chunk retrieval.

**Pass:** Fallback path engages only when gate is LOW.

---

### H4. Skip-import prefix additions

```bash
# Add "io.swagger." to skip_import_prefixes
# Re-backfill imports for affected files
```

**Sanity SQL:**

```bash
sqlite3 /tmp/books-online-vectors-enriched.db \
  "SELECT imports FROM summaries WHERE imports LIKE '%io.swagger%' LIMIT 1;"
```

**Expected:** After re-backfill, `io.swagger.*` imports removed.

**Pass:** Config-driven filter updates propagate after a re-index.

---

### H5. top_k change

```bash
# top_k = 10
```

**Prompt:** any from Group A.

**Expected:** Header shows `top-10 scored`. More chunks in RETRIEVED
status. Reverse lookup still bounded by max_per_chunk.

**Pass:** Retrieval knob responds. Reverse doesn't over-expand.

---

## Group I — Do-I-need-to-rebuild-the-DB scenarios

A quick mental checklist, not a prompt set. Useful to keep alongside
the test matrix.

```text
┌──────────────────────────────────────────┬────────────────────────┐
│               If you change               │   Re-run the notebook?  │
├──────────────────────────────────────────┼────────────────────────┤
│ vectordb_config.json weights/thresholds  │ NO                     │
├──────────────────────────────────────────┼────────────────────────┤
│ skip_import_prefixes                     │ NO (backfill script    │
│                                          │  re-runs extract_imports)│
├──────────────────────────────────────────┼────────────────────────┤
│ retrieval code (vectordb_search.py)      │ NO                     │
├──────────────────────────────────────────┼────────────────────────┤
│ reverse_lookup config                    │ NO                     │
├──────────────────────────────────────────┼────────────────────────┤
│ AST tag definitions (ast_tagger.py)      │ YES (need to re-tag    │
│ — new lifecycle tags, new layer tags     │  chunks, re-write tags  │
│                                          │  column)                │
├──────────────────────────────────────────┼────────────────────────┤
│ Embedding model (e.g. MiniLM → larger)   │ YES (all embeddings    │
│                                          │  invalid)               │
├──────────────────────────────────────────┼────────────────────────┤
│ enriched_summary formula                 │ YES                    │
├──────────────────────────────────────────┼────────────────────────┤
│ New column in summaries                  │ NO if ALTER+backfill    │
│                                          │  path exists (as done    │
│                                          │  for `imports`)          │
├──────────────────────────────────────────┼────────────────────────┤
│ Indexing a DIFFERENT codebase            │ YES                    │
├──────────────────────────────────────────┼────────────────────────┤
│ /tmp/ got wiped (reboot)                 │ YES (or restore from    │
│                                          │  backup copy)            │
└──────────────────────────────────────────┴────────────────────────┘
```

For running the Group A–H tests above, **nothing in the notebook needs
to re-run** — the DB has 90 chunks + 19 hierarchy nodes + backfilled
imports already.

### Sub-note: the `imports` column specifically

The `imports` column was added in this session for Stage 4b. It was
backfilled into the **existing live DB** by `/tmp/backfill_imports.py`
(re-cloned source → `extract_imports()` → `UPDATE summaries SET
imports=?`). This means the live DB is complete for testing.

**However**, the wiring for `imports` in the **indexing path** is NOT
complete:

```text
┌─────────────────────────────────────┬───────────────────────────────┐
│             Code path                │     Calls extract_imports?    │
├─────────────────────────────────────┼───────────────────────────────┤
│ ast_tagger.py::extract_imports      │ Defined, exported             │
├─────────────────────────────────────┼───────────────────────────────┤
│ Notebook Step 10 (AST tagging)      │ ❌ NOT wired                   │
├─────────────────────────────────────┼───────────────────────────────┤
│ Notebook Step 13 (DB write)         │ ❌ NOT writing imports column  │
├─────────────────────────────────────┼───────────────────────────────┤
│ reindex_changed.py (per-commit)     │ ❌ NOT wired                   │
├─────────────────────────────────────┼───────────────────────────────┤
│ /tmp/backfill_imports.py (one-off)  │ ✅ used for current DB         │
└─────────────────────────────────────┴───────────────────────────────┘
```

**Consequence:**

- **Running TEST_PROMPTS.md against current DB:** no notebook run needed.
- **Rebuilding the Books Online DB from scratch via notebook:** will
  produce a DB with empty `imports` → Stage 4b REV_IMPORT won't fire.
  Workaround: re-run `/tmp/backfill_imports.py` after notebook.
- **Indexing a new codebase via notebook:** same issue; same workaround.

This is a known pending task (see `project_state.md` memory). To fully
close the loop, the notebook (Step 10 or 13) and `reindex_changed.py`
need one small change each — call `extract_imports(file_path, full_code,
language)` once per file and include the result in the row being
written. Deferred until after use-case validation.

---

## How to record results

For each probe, capture in a spreadsheet or `TEST_RESULTS.md`:

```text
probe_id | query | top3 files | pinned | cg | rev | conf | pass/fail | notes
```

A **regression suite** = Group A + Group C1-C3 + D1 + D3. Run those
before any change to the retrieval code.

---

## Coverage map (what each group proves)

```text
┌───────┬─────────────────────────────────────────────────────────────────┐
│ Group │                         What it validates                         │
├───────┼─────────────────────────────────────────────────────────────────┤
│   A   │ Each stage fires correctly in isolation                          │
├───────┼─────────────────────────────────────────────────────────────────┤
│   B   │ Stage interactions (dedup, order, cap enforcement)                │
├───────┼─────────────────────────────────────────────────────────────────┤
│   C   │ Confidence gate bands (HIGH / MED / LOW)                         │
├───────┼─────────────────────────────────────────────────────────────────┤
│   D   │ Edge cases (collisions, case, cross-language, filter correctness)│
├───────┼─────────────────────────────────────────────────────────────────┤
│   E   │ Individual confidence signals probed one at a time               │
├───────┼─────────────────────────────────────────────────────────────────┤
│   F   │ Stress (long / empty / gibberish queries — no-crash guarantees)  │
└───────┴─────────────────────────────────────────────────────────────────┘
```
