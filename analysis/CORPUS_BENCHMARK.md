# PVM corpus grammar benchmark

Run `./gradlew :execution:test --tests dev.panini.execution.KarakaCorpusBenchmarkTest`.
On Windows use `./gradlew.bat`.

The native document parser inventories every `.pvm` file below `examples/`
and `projects/`. Procedure bodies and grammatical control-flow conditions
are traversed without executing programs or expanding runtime repetitions.
Declaration items are counted separately; declaration headers are not yet
reviewed as grammatical clauses. Source spans are half-open character
offsets in the original UTF-8-decoded document (JVM string indices), with
one-based source lines. Missing spans are reported explicitly.

Checked-in expectations:
`execution/src/test/resources/karaka_corpus_expectations.json`.
Observed inventory and analysis:
`execution/build/reports/karaka-corpus.json`.
The runner never writes observations back to expectations.

Each review identifies a file, whitespace-normalized clause text, and
zero-based occurrence among equal clauses in that file. Changed or removed
clauses cause unmatched-review failures. Every participant is checked in
order, including its resolution status, role, case, exact evidence chain,
ambiguity candidates, additional designations, and case alternatives.
An independent rationale and canonical source links accompany the review.
No semantic premises or synthetic lexicon entries are supplied: analysis
uses the execution adapter's grammar-only path on the parser-owned clause.
Runtime variable lookup, procedure invocation semantics, and result values
remain covered by separate execution tests.

New clauses appear as UNREVIEWED. Analyzed does not mean grammatically
reviewed or correct. A passing test proves that the inventory parsed and
the checked-in reviews matched; it does not turn unreviewed clauses into
passes. Unsupported analysis remains visible even for unreviewed clauses.
The summary separates review passes, unreviewed clauses, analyzed frames
with blocking diagnostics, unsupported analysis, parse errors, and missing
spans. Reviews also check the expected blocking diagnostic codes (empty
unless explicitly specified). Parse failures and stale or
mismatching reviews fail the test after the report is written.

Current coverage: 98 files, 453 clauses, 7 reviewed passes, 446 unreviewed.
All 453 clauses now produce analyzer frames and retain source spans.
The clause containing रम् in `examples/algorithms/two_sum.pvm` retains
UNKNOWN_DHATU as a blocking diagnostic because its spelling does not
select one lexical entry. It is not certified as correctly resolved.
The two bare nominal branches in the number-guessing project now retain
their written word spans through implicit nominative lowering. The seven reviewed
clauses cover arithmetic objects, collection objects and placement locus,
a project entry point, a multi-file procedure body, a pronoun recipient,
and an existential subject/locus. They cover 1.4.49 → 2.3.2,
1.4.45 → 2.3.36, 1.4.32 → 2.3.13, and 1.4.54 → 2.3.46 so far.

Selected reviews also declare `wordOrders`, arrays permuting all pada
indices exactly once. Seven distinct variants currently pass. Each runs
through two paths: reorder the original AST occurrences, and reparse the
reordered written morphological fragments. Both are compared against the
independent JSON expectations, including exact chains and diagnostics.
Participants are aligned by original identity for AST variants and by
unique normalized source fragments for reparsed variants. Repeated
identical fragments require an explicit alignment scheme before text
variants can be used. Variant success is separate from the count of actual
reviewed corpus clauses, and original source spans are not represented as
locations in the synthetic reordered text.

The report also includes `jointCaseAlternatives`. Reviewed clauses assert
the corresponding JSON field (empty when omitted). Word-order checks
align the member indices back to the reviewed clause before comparing
these grouped case suggestions, through both AST and reparsed paths.

Next work is to review the remaining constructions, add uncertain and
negative judgments, address the unresolved lexical reading, and
add grammatically valid word-order, voice, and modifier variations. The
separate structured-input benchmark remains responsible for its supplied
semantic-premise regressions; its count must not be combined with corpus
coverage into a claim of independent meaning extraction.

`#` introduces a PVM line comment (the lexer also recognizes `//`). Comment
text is excluded by the parser, not by ad hoc corpus text processing. The
original document retains comments so reported offsets and line numbers
remain accurate. A parser regression covers whole-line and trailing `#`
comments containing otherwise valid-looking Sanskrit clauses.
