# Natural PVM language roadmap

Updated: 2026-10-09.

## Goal

PVM source is morphologically segmented Sanskrit. Computational meaning must
derive from morphology, kāraka relations, verbal valency, and discourse context,
not arbitrary spelling conventions or symbolic constructors.

This roadmap records current capabilities and unfinished work. Usage and examples
belong in [the language guide](pvm-language-guide.md); sentence-level list issues
belong in [the list grammar audit](natural-list-grammar-audit.md).

## Acceptance criteria

A construction is ready when:

1. Segmented source and rendered Sanskrit are grammatically justified.
2. The dhātu genuinely denotes the intended operation.
3. Operands follow compositionally from morphology and kārakas.
4. Interpreter and compiler produce equivalent typed results.
5. Lowering consumes semantic AST data or resolved bindings, not source substrings.
6. Rendering, interpretation, IR lowering, and invalid inputs have tests.

Green tests prove covered regressions, not complete Sanskrit correctness.
Preserved raw segments indicate an unresolved derivation, not successful rendering.

## Current baseline

- Native whole-document parsing retains procedures, सीमा, अधिकार, control flow,
  source order, multiline text, and comments. Legacy parsing is explicit.
- Affixes, vikaraṇas, connectors, and semantic vocabulary have typed identities.
  Parser acceptance does not imply complete grammatical derivation.
- Procedure naming supports इति and नाम with nominative names and a finite
  प्रक्रिया declaration. Named arguments use locative slots. Argument values
  retain their types; compiled frame entry checks resolved parameter types.
- अधिकार and सीमा take effect sequentially, including across procedure calls.
- Shared semantic frames cover state placement, collection formation/insertion,
  retrieval, joining, membership, cardinality, slicing, and final-member extraction.
  Positions are one-based; coordinated operands must resolve completely.
- Genitive member declarations create ordered सूची values, optionally constrained
  to सङ्ख्या or शब्द members, with independent names introduced by इति or नाम.
  Constraints survive supported collection operations, codecs, and history.
- Genitive whole + plural accusative सङ्ख्या expresses number-member summation.
  Both backends require a list, reject text/nested members, and check overflow.
  Empty numeric lists sum to zero; arbitrary arithmetic operands are not flattened.
- Final-member extraction consumes typed selector meaning, not resolved spelling.
  Member/history roles survive same-named procedure parameters and prior-action
  clause projection.
- क्त्वा/ल्यप् chains preserve operand ownership and shared agents. Objectless
  display can use the preceding result. ततः and mixed chains remain supported.
- Named action results and kāraka histories support latest, previous, and
  one-based ordinal selection. Typed completed-action snapshots separate results
  from participants and retain structured values.
- Supported ordinal retrieval/history paths check case, number, partial gender,
  derivation, and wide numeric bounds. Derived nouns do not silently inherit their
  base's literal, ordinal, or selector meaning.
  Additional nominal derivation, including compound-member suffixes, is retained
  during named parameter rebinding rather than collapsed into an unmodified name.
  Ordinary value lookup likewise distinguishes derived forms from plain bases.
- Conditions use typed truth values; structural comparisons do not compare display
  strings. Underived सत्य/असत्य provide literal truth.
- Numeric prohibitions have shared typed parameter/literal operands and explicit
  compiler IR checks for runtime values. Unsupported procedure prohibitions are
  rejected rather than silently ignored; guards precede memoized-result lookup.
- Anonymous discourse stays in memory; named sessions opt into persistence.
  Input validation separates numeric bounds from text constraints.
- Supported rendering uses grammatical derivation engines and retains affix
  provenance. Unresolved forms remain explicit rather than being patched by
  word-specific replacements.

## Remaining work

### 1. Grammar, derivation, and agreement

- Replace the remaining restricted अस् present-tense fallback with verified
  derivation; complete homonymous-root selection by identity, gaṇa, and meaning.
- Enforce lexical pada and valency independently of operation requirements.
  In particular, executable भू transitivity currently obstructs natural bhāve
  analysis. Integrate remaining typed vikaraṇas into derivational selection.
- Complete uncovered verb/kṛt/sanādi rules, including 7.3.36 and 7.3.78.
- Complete feminine eligibility/exceptions, productive affix chains, and
  masculine/neuter possessive strong, dual, and plural paradigms.
- Extend gender/person agreement, bhāve participant eligibility, and voice
  inference beyond यक्. Resolve trailing-only च coordination contextually.
  Do not equate grammatical वचन with scalar/pair/list runtime types.
- Preserve numeral-expression structure through normalization and alternate
  notations; broaden malformed, case, number, and gender coverage.
- Complete prefix/compound phonology and pending-deletion scheduling, including
  the scope of 8.2.9, without overwriting prior transformations or nipātana.
  Preserve permitted optional derivations and their provenance.
- Integrate comparative वति grammar/derivation and natural compound source
  syntax. Remove symbolic compound grouping only with a grammatical replacement;
  compound-type selection must not always assume tatpuruṣa.
- Resolve shared भ्याम्/भ्यस्/ओस् cases from context without erasing ambiguity.

### 2. Compositional semantics and discourse

- Give declared referents stable gender, number, and value types; validate later
  inflected references and naming agreement. Preserve quotation/reporting
  semantics rather than turning every इति statement into variable storage.
- Extend general modifier/head and whole/member attachment beyond specialized
  frames. Add further member types, quantified phrases such as
  सूच्याः सर्वाः सङ्ख्याः, and ordinal extraction.
- Complete grammatical participant declarations, local references, returns,
  implicit branch results, and early termination. Implicit nominal branches
  currently contain synthesized दा invocations and need a dedicated result audit.
- Define derived-predicate/object semantics and dedicated unsupported-derivation
  diagnostics; an unresolved derived subject must not alias its plain base.
- Replace finite projections for prior actions with dedicated nonfinite heads.
  Extend shared-agent analysis to passive clauses and generalize range-exclusion
  prior actions. Cover procedure calls, nested control flow, early failure, and
  host-budget exhaustion across mixed chains.
- Resolve numeric/collection सम् + युज् overloads from typed operands.
  Justify empty-collection and removal constructions independently.
- Extend history queries beyond the verified display paths, including multiple
  references and conditions. Verify history publication by specialized operations.
- Add compiler type-aware overload dispatch: module symbols, JVM targets, and
  linker metadata must distinguish signatures, not merely change call-site ranking.
- Audit nonnumeric prohibitions and legacy argument-type guard inference.
  Preserve value-based checking through nested calls and all call forms.
- Audit remaining collection/record frames for backend equivalence and remove
  residual surface aliases only after shared semantic replacements exist.

### 3. Diagnostics, migration, and evidence

- Add source-spanned Sanskrit diagnostics for eligibility, valency, ambiguity,
  unsupported derivation, and runtime type failures; extend spans into nested bodies.
- Expose the retained derivation entry through an appropriately scoped API.
- Migrate legacy examples/fixtures after grammar and execution parity are verified.
  Keep intentional compatibility coverage until a documented deprecation decision.
- Generate readable .txt companions through CLI --render-readable, never by hand.
- Expand positive/negative grammar tests and end-to-end interpreter/compiler tests.
- Observe CLI subprocess timeouts under full-build contention; serialized process
  tests and concurrent output draining do not prove every timeout's cause.
- Benchmark interpreter, compiled backend, direct primitive lowering, and meaningful
  Python equivalents separately from build/startup time.

## Compatibility boundaries

- समवाय argument packing is legacy, restricted to an underived accusative form
  without an explicitly declared same-named parameter. Prefer ordinary typed list
  parameters and grammatical member summation. Multi-argument list packing still
  needs migration; a single scalar is not implicitly a list.
- Older dative assignment, causative collection forms, some genitive named slots,
  argumentless display ellipsis, and bare procedure headers are not canonical
  models for new syntax.
- ASCII period remains a DANDA alternative pending an explicit migration decision.
- Retain vyutpattiTinganta as a derivation entry, not an invented source constructor.
- Preserve इट् as a legitimate augment. Upasargas are prefixes, not kārakas;
  च is a connective, not a sandhi operation. अधिक/ऊन are not generated by degree
  suffixes alone.

## Grammatical references

- [Bhāva/karman derivation](https://ashtadhyayi.com/laghukaumudi/30),
  [1.3.13](https://ashtadhyayi.com/sutraani/1/3/13),
  [3.4.69](https://ashtadhyayi.com/sutraani/3/4/69)
- [Number: 1.4.22](https://ashtadhyayi.com/sutraani/1/4/22)
- [Feminine eligibility: 4.1.4](https://ashtadhyayi.com/sutraani/4/1/4),
  [4.1.6](https://ashtadhyayi.github.io/suutra/4.1/),
  [4.1.73](https://ashtadhyayi-lite.github.io/sutra/4.1.73.html)
- [Avyayībhāva inflection: 2.4.83](https://ashtadhyayi.com/sutraani/2/4/83)
- [Causative conditions: 7.3.36](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.36.htm),
  [Present stems: 7.3.78](https://avg-sanskrit.org/sutras/7-3-78.html)
- [मतुप् substitutions: 8.2](https://ashtadhyayi.github.io/suutra/8.2/),
  [Optional ह् assimilation: 8.4.62](https://www.ashtadhyayi.com/sutraani/sk119)
