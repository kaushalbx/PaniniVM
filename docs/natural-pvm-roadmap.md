# Natural PVM language roadmap

Updated: 2026-10-08.

PVM source is morphologically segmented Sanskrit. Computational meaning must
derive from morphology, kāraka relations, verbal valency, and discourse context.
Do not assign unrelated programming meanings to source spellings or introduce
symbolic constructors in place of natural-language constructions.

This document tracks current capabilities and remaining work, not individual
implementation attempts. Detailed usage belongs in [the language guide](pvm-language-guide.md).

## Goal scope: natural multi-action sentences

Objectless display commands within prior-action chains resolve the preceding
result in shared lowering; explicit operands are preserved in both backends.
Simple unqualified kṛdanta-genitive फल references now load the latest completed
result for their canonical dhātu, rather than aliasing LastResult. Tests cover
intervening printing, repeated matching actions, and whole dice compilation.
पूर्व-qualified references now select the previous completed action for the named
dhātu in ordinary leaves, conditionals, and loop conditions. Ordinals such as
प्रथम and द्वि + तीय select matching completed results chronologically from one.
Executed parity tests cover intervening prints, copular comparisons, changing
latest/previous results in loops, and stable ordinal selection in bounded loops.
Ordering modifiers are consumed as qualifiers, not extra action operands.
Interpreter binding applies ordering once across earlier discourse and preceding
clauses of the current utterance. Direct parsed-utterance tests distinguish first,
previous, second, and latest across that boundary instead of relying only on
script execution, which already splits clauses into individual turns.
Lexically typed ordinals such as तृतीय now use the shared pūraṇa resolver in
memory queries. Tests cover a missing third result across two discourse scopes
and executed grantha emission selecting the first earlier result across turns.
Ordinal positions remain 64-bit through shared binding and JVM IR. Bounds checks
precede list-index conversion, preventing large numerals from wrapping into a
different valid result; tests include positions above the 32-bit range.
Positional procedure AST rebinding likewise checks the full ordinal against the
argument count before narrowing. A wide typed ordinal remains its own operand
instead of impersonating the first parameter.
Inside procedure bodies, a named-result relation protects its genitive, ordinal
qualifier, and फल from positional/name-based parameter rebinding. AST and
executed parity tests distinguish selecting the first historical result from
using the first procedure argument.
Procedure pipeline argument and render padas use the same relation-aware binding
as ordinary invocations. Named-result genitives, ordinals, and फल remain history
references even when their spelling matches a procedure parameter; the legacy
string argument view follows the bound AST rather than rebinding independently.
Kāraka history relations receive the same procedure-rebinding protection as फल:
their genitive, role noun, and ordering qualifier remain discourse references.
Tests cover invocation and pipeline ASTs and an executed procedure whose first
remembered कर्म participants differ from its first parameter.
Compiled kāraka history uses typed participant-frame recording and selection IR,
not just result-history loads. Display commands containing accusative history
references now use shared relation analysis and ordinary typed leaf planning;
parity tests cover first and second referents in the same command. Mixed
ordinary/history display operands use the existing grammatical binder and retain
written order; parity tests cover ordinary values before and after history
participants. Other verbs and condition queries remain explicitly
unsupported rather than treating role nouns as literal operands.
Source-level display regressions also verify ordinary coordination, missing
previous/ordinal actions, absent participant roles, and modifier case/number
disagreement. Missing references return `INVALID_VALUE` in both backends.
Executed parity tests verify participant-history displays inside procedure call
frames and lazy evaluation in conditional bodies: a skipped display does not
attempt its unavailable lookup. History ordinals in procedure bodies remain
discourse selectors rather than positional parameters.
The compiled runtime now has an atomic completed-action record containing the
typed result and kāraka participant lists, with ordinal and recency selection on
one shared chronology. Tests cover input/result separation, copied binding
containers, absent roles, result-only records, wide ordinals, and execution
isolation.
Structured history values are recursively snapshotted on recording and reading,
including nested lists, coordination, records, and text classification sets.
Tests verify caller mutation cannot rewrite completed results or participants;
cyclic values fail with `INVALID_VALUE` before any frame is published.
Participant loads have ordinal and recency IR instructions and
JVM emission, returning a single typed member or typed coordination. Executed
bytecode tests verify selection, missing wide ordinals, and invalid-role/index
validation. Atomic frame-recording IR now consumes ordered typed participant
values and the successful result together; JVM tests verify ordering and result
alignment, and the verifier checks stack arity and resolved roles. Source
Resolved ordinary leaves now capture typed participants for every resolved
kāraka binding before executing
the action and publish them with its successful result. Capture uses typed
binding resolution with literal fallback and flattens grammatical coordination;
source-bytecode tests distinguish arithmetic inputs from its result across a
later print, and agent/object roles in the same frame. Specialized paths and source history-query lowering
remain pending;
Kāraka-history source analysis now exposes a memory-independent typed relation
containing the referent occurrence, genitive, canonical dhātu, role, and validated
ordering. Interpreter selection and procedure protection share this analysis;
compiler query lowering can consume it without recreating Sanskrit heuristics.
That relation now lowers directly to ordinal or recency participant-load IR,
with agreement and canonical-identity validation. Source-generated action
frames plus parsed query relations execute through JVM tests for first, previous,
second, and latest selection. Full invocation/condition integration remains
pending beyond the verified display path.
Shared ordinal extraction consults typed pūraṇa semantics before attempting
stem evaluation, preserving already-resolved values consistently across memory,
kāraka queries, and positional binding.
An unresolved typed pūraṇa modifier remains an explicit ordering request: it
cannot fall back to the latest result. Shared selection returns no match and
named-result validation rejects the unresolved qualifier; regression tests cover
both list and memory selection.
Procedure argument projection retains explicit ordering even when an ordinal
has no resolved numeric position. Such modifiers are consumed with their result
reference rather than becoming extra arguments or positional parameters, and
invalid ordering references are not evaluated during overload ranking.
Ordered kāraka references also validate modifier case and number before memory
selection and resolve derived dhātus through canonical identity. Executed tests
cover a valid पूर्व-qualified कर्म reference and case/number disagreement.
Unavailable named kāraka references and absent participant relations return
`INVALID_VALUE` instead of becoming literal operands. Tests distinguish previous
participants from latest participants, not merely successful execution.
Repeated identical-looking kāraka words retain source-occurrence identity during
binding. A regression with first and second कर्म references in one command
reproduced structural-map overwriting and now verifies both participant groups.
Typed ordinal positions must be positive before memory selection. Zero and
negative positions, including the smallest 64-bit value, are rejected by shared
validation; parsed-AST execution tests verify `INVALID_VALUE` rather than an
uncaught memory API precondition failure.
Named-history procedure arguments now preserve their grammatical reference
through argument ordering and call frames. Parity tests cover positional and
locative named slots, latest/previous/ordinal selection, intervening printing,
and missing results before body execution. Two named-result operands in one
procedure call are tested independently, including equal-looking फल occurrences
with different ordering qualifiers. Multiple results in ordinary leaves and
conditions remain guarded;
Interpreter overload ranking now uses the resolved named result's value type,
not the spelling फल; a numeric-versus-text overload regression verifies this.
Compiled overload selection still needs dedicated type-aware dispatch and parity.
Module analysis currently rejects duplicate procedure identities; enabling it
requires signature-aware symbols, distinct JVM targets, and linker metadata,
not just changing the call-site ranking.
Ordering qualifiers are checked against फल for case and number using typed sup
candidates; mismatches fail rather than silently selecting a historical value.
Missing named histories now report INVALID_VALUE instead of aliasing unrelated
latest results; tests cover missing latest/previous/ordinal references and loop
condition error propagation. Static grantha emission retains canonical identities
for earlier planned turns, allowing valid cross-turn references without execution.
History publication by all specialized operations still needs verification.
The compiled runtime now has separate typed per-dhātu history with one-based
recency lookup, independent of LastResult and user-variable snapshots. Unit tests
cover nested call-frame lifetime, execution isolation, and missing-reference
errors. RecordActionResult, LoadActionResult, and LoadOrdinalActionResult IR have stack validation and
JVM emission; generated-fixture tests cover typed round trips and skipped branches.
Source lowering now records successful resolved leaves and range choices under
canonical dhātu identities. Generated-source tests cover preservation across later
printing and absence of records after failure. Other specialized lowering paths
still need integration. Source-level history loads are integrated for simple
resolved leaves; broader history queries require further executed-path tests.

Continue improving shared-agent क्त्वा/ल्यप् constructions alongside ततः chains.
This is part of the language goal, not complete merely because the initial
addition-and-print example works. Required work includes:

- Dedicated nonfinite semantic heads, without finite binding projections.
- Grammatical affix eligibility and shared-agent resolution, including passive
  clauses and coordinated agents.
- Separate operand ownership, explicit फल result references, and mixed chains
  that preserve existing ततः behavior.
- Reusable procedure calls, nested control flow, early failure, and host-budget
  exhaustion with interpreter/compiler parity.
- General prior-action handling for range exclusion, replacing the specialization.
- Source-preserving diagnostics, natural readable rendering, guide updates, and
  migrated examples verified through the CLI.

## Acceptance criteria

A construction is ready only when:

1. Its segmented source and rendered sentence are grammatically justified.
2. Its dhātu genuinely denotes the intended operation.
3. Its operands follow compositionally from morphology and kārakas.
4. Interpreter and compiler produce equivalent typed results.
5. Lowering consumes semantic AST data or resolved bindings, not source substrings.
6. Rendering, interpretation, IR lowering, and invalid inputs have tests.

A passing repository suite is regression evidence, not proof of complete Sanskrit
coverage. Preserved segmented source indicates an unresolved derivation, not a
successfully generated Sanskrit form.

## Implemented baseline

### Native syntax and discourse

- Whole-document native parsing is the default. Procedures, सीमा, अधिकार,
  loops, conditionals, and pipelines retain AST nodes and source order.
- Explicit reusable declarations use a finite प्रक्रिया clause; operation
  names and domains have structural, case-independent identities.
- अधिकार registration and सीमा activation are sequential. A procedure sees
  the range active when it is called; later declarations are not hoisted.
- Source spans retain original multiline text and comments. Legacy parsing is
  explicit rather than a hidden fallback after native parse failure.
- Vikaraṇas, tiṅ/sup affixes, connectors, and semantic vocabulary have typed
  identities. Acceptance by the parser does not imply full derivation support.

### Shared semantics and execution

- State placement, collection formation/insertion/retrieval, membership,
  concatenation, cardinality, slicing, and final-member extraction use shared
  semantic operations and resolved grammatical operands.
- Collection positions are one-based. Retrieval and slicing accept actual
  collections and validate numeric positions/bounds.
- Kāraka extraction without a frame relation retains all shared sup candidates;
  an operation's participant roles select a case only when the match is unique.
  A resolved grammatical relation cannot be reassigned to fit an operation.
- Coordinated nominal members bind individually; only members resolving to the
  same kāraka are grouped. Different member roles are not unioned into one operand.
- Explicit coordinated agents use combined grammatical number for verb-agreement
  diagnostics. Mixed-role groups and uncoordinated adjacent nouns are not combined.
  Native parsing retains repeated-च nominal coordination (A च B च) as an AST
  group; parsed-source integration tests cover active/passive agreement.
- यक् constructions require ātmanepada independently of the root's lexical pada.
  Passive number agreement follows the resolved nominative object, including
  explicit coordination, not an instrumental agent or secondary accusative object.
- Bhāve agreement diagnostics require प्रथमपुरुष एकवचन independently of
  instrumental agents; this does not yet validate every bhāve construction.
- Frame voice detection consumes parsed णिच् and यक् identities, not letters or
  affix-looking substrings in display source. यक् selects passive voice even on
  णिच्-derived verbs with intransitive base roots; causative morphology stays in
  the AST. Other voice formations and detailed causee-role conditioning remain to audit.
- Result flow uses typed LastResult references. Explicit फल remains a natural
  discourse anaphor; stored structured-field values retain their runtime types.
- Prior-action क्त्वा/ल्यप् morphology lowers shared-agent action chains for both
  backends. Each action owns its operands; prior-action ordering does not imply
  operand injection. ततः remains available, including in mixed chains.
  Understood nominative agents retain explicit coordination in either clause;
  shared-agent identity checks preserve member multiplicity rather than using sets.
  Nominal identity retains derivational suffixes; base nouns and their derivatives
  are not presumed coreferential. Temporary binding heads rebuild morphology from
  typed root/affix fields, not display source text.
  Explicit prefixed ल्यप् renders through the canonical क्त्वा replacement path;
  heavy-syllable णिच् deletion is supported. Remaining light-syllable replacements
  stay unresolved rather than presenting mechanical sandhi as a derived form.
  Lowered-source analysis tests check dual main-verb agreement without imposing
  finite agreement on nonfinite heads. Conditional parity tests exercise both
  branches and verify that an unselected failing prior action does not execute.
  Script execution expands prior actions before reusable-procedure dispatch;
  argumentless main procedure calls have interpreter/compiler parity tests.
  Negative-result failure tests verify that a prior action cannot continue into
  the main procedure. Compiled numeric results reject missing Sanskrit rendering
  rather than silently substituting decimal text.
- Procedure arguments retain parsed pādas, declared participants, typed values,
  and positional/named/pipeline origin. Natural named slots use locative case.
- Record schemas use coordinated field declarations; record construction uses
  finite copular field assertions; retrieval requires an accusative object of ग्रह्.
- Runtime and compiled comparisons/membership share structural typed equality.
  Boolean conditions require typed truth values, not truth-looking strings.
- Input validation checks declared types, choices, and bounds before/after host
  dispatch as appropriate. Text is not constrained by numeric bounds.
- Anonymous script/CLI discourse stays in memory. Explicit named sessions opt
  into persistence; historical snapshots retain immutable, shared storage.
- Compiler active-range IR has a distinct RANGE kind and checks operand shapes.
- CLI process tests drain child output concurrently and include captured output
  in timeout diagnostics, avoiding the wait-before-read pipe-deadlock pattern.

### Derivation and readable rendering

- कुरु, गृहाण, ordinary/causative स्था, derived avyayas, चयन, and derived-noun
  declension use grammatical engines rather than word-specific renderer replacements.
- Prefixes, sanādi/kṛt affixes, case, number, and supplied gender are preserved
  through the supported rendering paths. Failed derivations retain full source.
- Nominal requests retain source-affix identities. Phonological merges preserve
  consumed affix provenance without assigning a compound member's affixes to its head.
- Possessive मतुप् is distinguished from comparative वति; possessives no longer
  use a hand-built renderer paradigm.
- Feminine requests must actually introduce the requested affix. Request-only
  टाप्, ङीप्, and ङीन् licensing bypasses have been removed from the audited rules.
- The U-it branch of 4.1.6 receives markers from actual possessive derivation,
  including sthānin properties.
- Feminine agreement does not add a second feminine affix to an already-feminine
  lexical noun. Numeral-head gender is used in the supported agreement paths.
- Compound requests propagate outer case, number, and supplied gender. Tested
  avyayībhāva cases respect sup deletion and the a-final ablative exception.

## Remaining work

- Broaden compound-boundary phonology beyond the verified numeral domain while
  preserving prior transformations and nipātana. Numeral-marked consonant
  boundaries now apply traced 8.2.39 and 8.4.55; the dice range renders
  `षट्पर्यन्तं`, with its readable artifact regenerated through the CLI. Tests
  distinguish voiceless and voiced followers. Do not reopen already transformed
  members indiscriminately or introduce word-specific renderer replacements.

### 1. Grammatical derivation and agreement

- Replace the remaining Adādi अस् present-tense fallback with verified derivation.
  Its current gaṇa/sanādi restrictions do not establish complete paradigms.
- Complete homonymous-root selection using lexical identity, gaṇa, and meaning;
  do not borrow a conjugation from a different homonym.
- Enforce verbal pada/valency validity consistently at execution binding.
  Integrate typed vikaraṇas that do not yet participate in derivational selection.
  Separate lexical valency from operation requirements: executable भू currently
  declares transitivity for state instantiation, which obstructs natural bhāve analysis.
- Extend partial verb-rule implementations, including uncovered roots/exceptions
  in 7.3.36 and 7.3.78, and additional kṛt/sanādi paradigms.
- Complete feminine-rule eligibility and exceptions: other उक् markers under
  4.1.6, the conditioned अञ् branch of 4.1.73, unsupported feminine affixes,
  and productive affix chains.
- Verify masculine/neuter possessive strong cases and dual/plural paradigms.
- Resolve ambiguous lexical gender and agreement person; complete bhāve
  participant eligibility and support beyond the current यक् voice inference.
  Disambiguate trailing-only च coordination without absorbing unrelated operands
  or changing numeral-head and structured-field constructions.
  Extend coordinated agreement beyond the resolved-agent number check. Do not
  equate grammatical वचन with a runtime scalar/pair/list type.
- Preserve source numeral-expression structure through generic normalization and
  decoded notations; expand case/number/gender and malformed-construction coverage.
- Finish prefix sandhi and general pending-deletion scheduling rather than adding
  local spelling fixes. Audit the full scope/exclusions of 8.2.9.
- Integrate comparative वति source grammar and derivation with real provenance.
- Define natural compound source expressions and grammatical compound-type
  selection; the renderer still assumes tatpuruṣa for its compound AST path.
- Resolve shared भ्याम्/भ्यस्/ओस् cases from grammatical context. Identical
  surfaces must not erase case ambiguity.

### 2. Compositional semantics and discourse

- Give इति-named referents stable declared gender, number, and value types;
  later references must use declared morphology rather than spelling heuristics.
- Extend shared typed semantic nodes across remaining procedure/control-flow
  conventions and collection frames; remove residual legacy surface aliases.
- Complete grammatical participant declarations, typed local references, returns,
  and early termination without reserved source-fragment shortcuts.
- Replace finite execution-binding projections for prior actions with dedicated
  nonfinite semantic heads; extend shared-agent resolution to passive main clauses.
  Generalize the existing range-exclusion prior-action specialization and resolve
  numeric/collection overload ambiguity for सम् + युज् from typed operands.
- Extend member selectors beyond अन्तिम, including ordinal extraction.
- Justify empty-collection and removal constructions independently.
- Audit collection/record frames for interpreter/compiler equivalence and keep
  grammatical cases separate from presentation controls.

### 3. Validation, migration, and evidence

- Check for recurrence of CLI process-test timeouts after concurrent output
  draining; not every prior timeout has a proven cause.
- Add source-spanned Sanskrit diagnostics for grammatical eligibility, case,
  valency, ambiguity, unsupported derivation, and runtime type failures.
- Extend spans into nested branches and expose the retained derivation entry
  through an appropriately scoped API.
- Audit remaining symbolic grouping and compatibility syntax. Preserve इट् as
  a legitimate augment; upasargas are prefixes, not kārakas; च is a connective,
  not a sandhi operation. अधिक/ऊन are not generated by degree suffixes alone.
- Expand positive/negative morphology tests and end-to-end interpreter/compiler
  equivalence tests for every accepted natural construction.
- Migrate remaining legacy fixtures and examples only after grammatical meaning
  and execution parity are verified. Keep deliberate compatibility tests until
  a documented deprecation decision.
- Regenerate readable .txt artifacts through the CLI, never by hand.
- Benchmark interpreter, compiled backend, direct primitive lowering, and meaningful
  Python equivalents separately from build/startup time.

## Compatibility boundaries

- Older dative assignment, causative collection forms, some genitive named slots,
  and argumentless display ellipsis remain compatibility paths, not preferred syntax.
- Bare legacy procedure headers must not become canonical examples.
- ASCII period remains a DANDA compatibility alternative; changing that policy
  requires an explicit migration decision.
- vyutpattiTinganta is retained as a grammatical derivation entry; invented
  अभ्यासः/आदेशः source constructors are not restored.

## Grammatical references

- [Bhāva/karman derivation — लघुसिद्धान्तकौमुदी](https://ashtadhyayi.com/laghukaumudi/30)
- [1.3.13 — भावकर्मणोः](https://ashtadhyayi.com/sutraani/1/3/13)
- [3.4.69 — लः कर्मणि च भावे चाकर्मकेभ्यः](https://ashtadhyayi.com/sutraani/3/4/69)
- [1.4.22 — singular and dual number](https://ashtadhyayi.com/sutraani/1/4/22)
- [4.1.4 — टाप् eligibility](https://ashtadhyayi.com/sutraani/4/1/4)
- [4.1.6 — उगितश्च](https://ashtadhyayi.github.io/suutra/4.1/)
- [4.1.73 — ङीन् and commentary](https://ashtadhyayi-lite.github.io/sutra/4.1.73.html)
- [2.4.83 — avyayībhāva outer inflection](https://ashtadhyayi.com/sutraani/2/4/83)
- [7.3.36 — causative augment conditions](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.36.htm)
- [7.3.78 — present-stem substitutions](https://avg-sanskrit.org/sutras/7-3-78.html)
- [8.2 — मतुप् substitution rule text](https://ashtadhyayi.github.io/suutra/8.2/)
