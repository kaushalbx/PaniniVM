# Natural PVM language roadmap

PVM source is morphologically segmented Sanskrit. Computational meaning must be
derived from ordinary morphology, kāraka relations, verbal valency, and discourse
context. A spelling must not acquire an unrelated programming meaning merely
because a compiler recognizes its source text.

### Imperative derivation fixes (2026-10-05)

The engine now derives कुरु and गृहाण without renderer substitution. The
executable कृ imperative branch retains हि under 3.4.87, applies guṇa before
उ, substitutes उ under 6.4.110, and elides हि under 6.4.106. The ग्रह् + श्ना
branch now applies samprasāraṇa (6.1.16) and pūrvarūpa contraction (6.1.108);
after the existing शानच् substitution, 6.4.105 elides हि. Tests assert the
required rule provenance and complete it-processing, not only the final text.
These are explicitly limited executable branches, not a claim that all roots
covered by the full sūtra texts have complete derivation support.

### Sequential सीमा execution (2026-10-05)

Script execution now installs each सीमा as it is encountered, rather than
using the last declaration retroactively for the entire file. Compiler entry IR
stores each declared range in order; `RandomActiveRange` consumes that typed
range at runtime, so a reusable procedure observes the सीमा active at its call
instead of a frontend-selected final bound. Explicit local bounds retain
precedence. Deterministic interpreter/compiler tests call the same procedure
under one-to-one and two-to-two ranges and obtain 1 then 2. Focused range and
repository compiler coverage pass. Full execution, compiler, and CLI regressions
also pass. Architecture checks now prohibit whole-file range hoisting and
require sequential typed range propagation.

### Sequential अधिकार registration (2026-10-05)

Runtime project registration and compiler module analysis now track the preceding
अधिकार in source order instead of applying the first domain retroactively to
all procedures in the file. Unqualified declarations before any scope remain
unscoped; subsequent declarations use the active domain; explicit genitive
domains override it. Compiler analysis resets the active domain for each source
unit. Direct tests cover these rules. The full execution/compiler/CLI suites
pass, as does the added module scope-isolation test. Historical legacy-order
registration parity is no longer the intended invariant. This change concerns
procedure registration only; sequential सीमा execution remains pending.

### Native default parser retained (2026-10-05)

`PvmScript.parse` now uses native complete-document parsing by default.
The full parser, execution (287 tests), compiler (104 tests), and CLI regression
suites pass with the switch retained. Source spans also preserve अधिकार domain
text. Intentional compatibility tests call `parseLegacy` explicitly; migration
diagnostics use `validateLegacy`, while ordinary validation uses the native
parser and reports invalid source without a hidden compatibility fallback.
Remaining memoization and duplicate-definition fixtures now use explicit
declarations; the memoized participle is segmented as सिध् + क्त rather than
applying क्त again to सिद्ध. Earlier trial/default-status paragraphs below are
historical checkpoints superseded by this one. This completes this parser
migration gate, not the broader natural-language morphology/semantics goal;
sequential अधिकार/range scope and derivational correctness still need audit.

### Native procedure-name source preservation (2026-10-05)

Document ASTs retain operation-name spans separately from declaration-header
spans. Native projection uses those spans for `nameSegmented`, preserving
original whitespace and multiline morphology without including the genitive
domain. Canonical morphological identity remains independent of source layout.
Regression coverage includes both declaration-only headers and complete blocks;
native execution tests and repository compiler coverage pass. This resolves
operation-name formatting, not all remaining migration expectation differences.

### Default-parser migration and fixture cleanup (2026-10-05)

A new default-native trial still exposed execution and compiler failures in
bare-header fixtures, source-format expectations, and intentional legacy checks.
The trial was stopped and the previous default retained; `parseLegacy` now
names that compatibility implementation explicitly, with no silent native-error
fallback. Loop and structured compiler fixtures were migrated to explicit
प्रक्रिया declarations. ल्युट् was removed from already-formed operation nouns
including प्रयत्न, परिचय, अन्तरचक्र, बाह्यचक्र, व्यवकलन, and वर्धन, consistently
in declarations and calls; root-based nominalizations retain their affixes.
The focused condition-loop and structured-bytecode test suites pass after these
edits. Remaining trial failures must still be classified and fixed before the
default switch is retained; this checkpoint does not claim full migration.

### Native compiler path checkpoint (2026-10-05)

Module analysis and IR lowering now accept an explicit source-parser function,
defaulting to the existing parser. Repository example module coverage additionally
lowers via `parseNative` and verifies emitted JVM bytecode. Those checks pass.
The native-compiled two-counter machine also executes with an identical result
map to the default backend, including final त्रीणि. The default parser has not
been switched; broader compiler/runtime migration is still pending.

### Native runtime parity checkpoint (2026-10-05)

The internal script executor can consume already-parsed statements directly,
without a second parse or a change to the default parser. A deterministic
parity test passes for addition, repeated summation, the five-algorithm double
danda suite, the two-counter machine, list operations, and ordinal indexing.
It compares all success fields except diagnostic trace history, which differed
between sequential runs; typed values, output kinds, control signals, and loop
metadata are retained in the comparison. This is six-program runtime evidence,
not an all-program interpreter/compiler parity claim.
Coverage now includes eleven programs, adding factorial, Fibonacci, nested
genitive fields, procedure pipelines, and अपवाद overriding. This exposed and
fixed an existing execution bug: a standalone structured field query returned
its typed value but did not retain it for a subsequent फल reference. The query
now updates typed and display result state while respecting the caller's session
persistence setting. The recursive compiler countdown fixture also now uses
explicit प्रक्रिया declarations and its focused compiler test passes.

### Repository native syntax gate (2026-10-05)

`NativeRepositorySyntaxTest` now parses all 69 current `.pvm` files under
`examples` and `projects` through the native document parser and runtime
projection. The initial audit found three ordinary top-level passages ending
in double danda. Document utterances now accept either danda delimiter, while
procedure bodies retain their explicit closing double danda. The repository
gate and native parser/adapter regression tests pass. This establishes syntax
acceptance, not execution parity or comprehensive Sanskrit grammatical validity;
default-parser migration still requires runtime and compiler verification.
An additional repository-wide comparison passes for declaration identities,
domains, modifiers, body lengths, numeric ranges, and sentence classifications.
It deliberately compares those summaries without ordering or source formatting;
it does not establish AST equivalence, declaration ordering, or runtime results.
A project-loader parity test also passes for all repository sources: native
source order yields the same registered operation/domain identities, visibility,
precedence, and parent-domain lookup as legacy declaration grouping. The loader
still uses the first अधिकार as a file-wide fallback; sequential discourse scope
is not established by this test and remains an explicit semantic audit item.

### Native standalone declaration checkpoint (2026-10-05)

The document grammar now recognizes an explicit प्रक्रिया declaration without
a following body as a `Prakriya` with an empty body, rather than rejecting it
or treating it as an executable quotation. Blocks and standalone declarations
share `prakriyaHeader` and the same case, copula, and qualifier validation.
Header source spans are retained for both forms. The native procedure parser
tests pass, including ordinary quotation disambiguation and existing blocks.
Declaration-only headers also work without a final danda. Native runtime adapter
tests verify retained domain/name identities, नित्य precedence, exact source
text, and rejection of an accusative operation name.
The default script parser remains unchanged; broader migration failures still
need resolution before switching it.

### Typed vikaraṇa checkpoint (2026-10-05)

`TingantaPada.vikarana` now retains a `Vikarana` enum rather than a string.
All current alternatives in the parser grammar retain their source upadeśa
identity. Execution and rendering share its optional gaṇa mapping; tense and
voice affixes are not arbitrarily assigned a gaṇa. Parser coverage checks every
alternative and rejects a post-it-lopa surface as an affix identity. This change
does not add derivation support for every accepted affix or remove existing
surface-rendering shortcuts. The later affix audit removed `श्नाम्` as an
unsupported kryādi alias; the operative upadeśa is `श्ना` under 3.1.81.
The audit follows [क्र्यादिभ्यः श्ना, 3.1.81](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/3/3.1.81.htm).
No `.pvm` examples or projects used the removed alias. Parser tests explicitly
retain `श्ना` and reject `श्नाम्` in the vikaraṇa position; this does not reject
that spelling as an unrelated lexical word in all contexts.
Full parser, execution, compiler, and CLI suites passed after this inventory fix.

### Explicit verbal pada checkpoint (2026-10-05)

Rendering now passes the source tiṅ ending's concrete pada to the derivation
engine for ordinary verbs as well as sanādi verbs. It no longer silently defaults
an ubhayapada root to Parasmaipada when the source supplies an Ātmanepada ending.
Paired regressions derive `डुपचँष् + लट् + त` as `पचते` and the same root with
`तिप्` as `पचति`. The full renderer connector test class passed. Explicit
vikaraṇas without a gaṇa mapping still need integration into derivational
selection; accepting a typed identity alone does not prove it is applied.
The downstream execution, compiler, and CLI verification command passed after
two quotation fixtures were migrated from pre-inflected `अनुमिनु` to segmented
`स्था + णिच्`. Execution tests reran; compiler and CLI tests were up-to-date
from the preceding run. No installed CLI distribution was rebuilt.

### Prakriyā fixture migration (2026-10-05)

Migrated 32 bare multiline reusable-operation declarations across five execution
test classes to explicit `इति प्रक्रिया + सुँ असँ + लट् + तिप्` headers.
Verbal-root action names retain `ल्युट्`, including `वृध्`; already formed noun
names such as `प्रयत्न`, `योजन`, and `निर्णय` take case endings directly, with
matching calls and identity expectations migrated together. Focused prakriyā
execution tests passed. Low-level header-identity fixtures and intentional
legacy/invalid-syntax checks are not silently converted into canonical examples.
This migration does not claim that every other word or numeral in these older
fixtures has completed its grammatical audit.

### Legacy declaration guidance (2026-10-05)

Parsed reusable definitions now retain their original header separately from
the normalized operation name. The validator uses that retained source and
source-offset mapping to suggest explicit `इति प्रक्रिया … अस्ति` declarations
for bare compatibility headers with bodies. Ordinary standalone action nouns
are not migration targets. Parsing and execution compatibility remain unchanged;
this is a warning, not automatic rewriting or a new computational noun meaning.
All 60 focused prakriyā tests passed, including replacement round-trip validation
and the standalone-nominal no-warning regression. Broader verification remains
necessary for consumers that choose to display these new warnings.

### Native document-parser groundwork (2026-10-05)

Factored the `.g4` single-utterance entry into an `ukti` wrapper owning its
optional danda and EOF, and a reusable `utterance` content rule. Quotation,
conditional, loop, and pipeline clauses no longer require EOF internally.
The AST builder consumes the reusable context directly; clause source text no
longer includes the synthetic EOF token. Regenerated parser and full execution
tests passed. A direct shared-token-stream regression covers adjacent clauses.
That regression exposed danda being consumed as a clause connector; it is now
a statement boundary, not an executable sequence relation. Full parser and
execution suites passed after this correction, including the adjacent-clause
shared-token-stream regression.
A whole-document AST is now implemented below; removal of the existing line-based
block parser remains pending.

The lexer now emits `DOUBLE_DANDA` for `॥`, separately from `DANDA` for `।`
and the existing ASCII-period alternative. The single-utterance entry accepts
either terminator, preserving existing body-clause parsing while making block
boundaries available to native document rules. Regression tests check all
terminators and reject a second sentence after a closed single utterance.
Full parser, execution, compiler, and CLI test tasks passed after regeneration.
Generated Java lexer output contains generator-produced trailing whitespace;
the grammar and hand-written changes remain the authoritative implementation.

`prakriyaBlock` and `prakriyaEntry` now recognize one explicit declaration,
single-danda body clauses, and a final double-danda body clause. The public
`PaniniParser.parsePrakriya` API builds a native `Prakriya` from these contexts,
retaining nominal identity, optional genitive domain, precedence/visibility,
and body nodes without serializing or reparsing individual statements. Parser
tests pass for multiline headers/bodies, internal qualifiers, conditionals,
and rejection of missing terminators, empty bodies, extra trailing statements,
bare headers, and non-procedure declarations. This API is not yet the execution
script parser: document-wide scopes, source spans, signatures, legacy migration,
and switching execution/compiler consumers still require implementation.

### Native declaration and loop coverage (2026-10-05)

Added dedicated `.g4` entries and native AST builders for सीमा and अधिकार
declarations. सीमा retains a `ParyantaRangePada` with morphological bounds and
requires a nominative सीमा marker; अधिकार retains its nominal domain and accepts
the lexical or segmented derived marker. These APIs reject inappropriate cases
and unrelated marker nouns. Native prakriyā tests now cover parameter/result
nominal declarations and bounded `यावत् … तावत्` loops across physical lines.
Loop clauses already existed in the grammar and are reused directly, not
duplicated as a special string parser. Runtime evaluation of bounds and semantic
signature checking remain outside grammar. Whole-document parsing is implemented
below; switching
the execution/compiler pipeline to these native declaration nodes remain pending.
Full parser, execution, compiler, and CLI suites passed after these additions.

### Native whole-document AST (2026-10-05)

`PaniniParser.parseDocument` now builds a source-order `ProgramDocument` from one
token stream. Its items retain prakriyā blocks, सीमा declarations, अधिकार scopes,
and ordinary utterances. A final unterminated utterance is accepted; empty input
has an empty document. The grammar explicitly recognizes the प्रक्रिया noun in
block predicates, preventing an ordinary quotation preceding a procedure from
being consumed as a block header. प्रक्रिया remains usable in nominal grammar.
Full parser tests passed, including one-line/multiline equivalence and mixed
declaration ordering. This is not yet a replacement for `PvmScript.parse`:
comment/source-span handling, standalone legacy closers, compatibility migration,
and runtime/compiler adapter integration remain pending. Declaration bounds and
signatures are not evaluated by the grammar.
Full execution, compiler, and CLI suites also passed after regeneration.

Native runtime projection is now available through `PvmScript.fromDocument` and
`parseNative`: declaration order and existing body ASTs are retained, signature
and frequency classification reuse semantic consumers, and bounds use shared
numeric validation. Focused adapter tests passed for retained node identity,
typed parameters, comments, and rejection of descending ranges. The default
`PvmScript.parse` path has not switched yet; source spans and legacy compatibility
still require explicit migration rather than silent parse fallback.

### Derivation grammar preservation (2026-10-05)

The earlier removal of `vyutpattiTinganta` and its helper subtree was reversed
following the user's objection. Lack of an executable caller does not establish
that grammatical derivation input is unnecessary. The entry is retained, but the
user's stricter requirement against invented code notation now makes it reuse
`tingantaPada` rather than an arbitrary component sequence. `अभ्यासः(...)` and
`आदेशः(...)` constructor rules and their lexer tokens are removed. Augment
placement, reduplication, and substitutions belong to the grammatical derivation
engine, not handwritten source constructors. A dedicated derivation API remains
pending. This is not certification of the complete grammar: lexical eligibility,
affix constraints, and remaining symbolic grouping constructs still need auditing.
Native declaration entries remain called by Kotlin.

Native documents now retain per-item source spans, including statement/block
terminators. Parsing no longer trims the input before tokenization, so leading
whitespace does not shift offsets. The runtime projection uses these spans to
retain original statement and block text and explicit prakriyā header metadata,
without reparsing body ASTs. Parser regressions passed for leading whitespace,
multiline blocks, and trailing unterminated clauses. Body-statement spans are now
implemented below; nested-branch spans remain pending. Comment-preserving original-file mapping is
implemented by the following checkpoint.
Execution, compiler, and CLI suites passed after source-preserving adapter changes.

Native parsing now consumes original source directly using the lexer's hidden
comment channel, without deleting comments or normalizing CRLF line boundaries.
Prakriyā header spans come from grammar token boundaries rather than searching
for punctuation in raw text. Regression coverage includes comments containing
danda and periods inside a multiline header, original-file offsets, and retained
header/block text. Default runtime parser replacement remains pending.
Focused native parser and execution adapter tests passed after these changes.
Native documents additionally retain per-prakriyā body-statement spans derived
from clause and delimiter tokens. Runtime projection preserves multiline body
text, comments, and each single/final double danda without reparsing the AST.
Regression coverage includes typed parameter recognition with punctuation in an
internal comment. Spans inside nested conditional branches are still pending;
these body-statement spans do not establish full nested diagnostic coverage.
Focused native parser and adapter tests passed after body-span integration.

A default-native-parser migration trial exposed 36 execution and 34 compiler
test failures, including legacy headers, standalone declaration handling, and
repository example syntax. The trial switch was removed; `parseNative` remains
explicit and the default parser has not migrated. Before switching again,
classify and migrate these cases with semantic parity tests rather than silently
falling back on a native parse error. The `agama` grammar inventory is currently
unreferenced by source entry rules: actual augments are introduced by grammatical
derivation rules (e.g. 7.2.35 and 6.4.71), not freely supplied by the program.
Full parser, execution, compiler, and CLI suites passed; direct derivation-entry
regressions reject missing/multiple roots, misplaced prefixes, and constructor
notation while accepting the shared segmented verbal structure.

## Acceptance rule









A new language construction is ready only when all of these are true:

1. Its rendered sentence is grammatical Sanskrit.
2. The selected dhātu genuinely denotes the operation.
3. Its operands are determined compositionally by morphology and kārakas.
4. Interpreter and compiler produce equivalent typed results.
5. Compiler lowering uses semantic AST data or resolved grammatical bindings,
   not a raw-source substring test.
6. Rendering, interpretation, IR lowering, and invalid-input behavior have tests.

## Implemented natural constructions

| Meaning | Segmented PVM frame | Readable form |
|---|---|---|
| State placement | `मूल्य + अम् स्थान + ङि स्था + णिच् + लोट् + सिप्` | `मूल्यं स्थाने स्थापय` |
| Collection insertion | `वस्तु + अम् सूची + ङि नि + क्षिप् + लोट् + सिप्` | `वस्तु सूच्यां निक्षिप` |
| Collection formation | `वस्तु१ + अम् वस्तु२ + अम् च सम् + ग्रहँ + श्ना + लोट् + सिप्` | `वस्तु१ वस्तु२ च सङ्गृहाण` |
| Indexed retrieval | `सूची + ङसिँ क्रमाङ्क + ङि मूल्य + अम् ग्रहँ + श्ना + लोट् + सिप्` | `सूच्याः क्रमाङ्के मूल्यं गृहाण` |
| Collection membership | `वस्तु + सुँ सूची + ङि असँ + लट् + तिप्` | `वस्तु सूच्याम् अस्ति` |
| Collection concatenation | `पूर्वसूची + अम् उत्तरसूची + टा सम् + युज् + णिच् + लोट् + सिप्` | `पूर्वसूचीम् उत्तरसूच्या संयोजय` |
| Collection cardinality | `सूची + अम् गण् + णिच् + लोट् + सिप्` | `सूचीं गणय` |
| Collection slice | `सूची + ङस् द्वि + तीय + ङसिँ त्रि + तीय + शस् परि + अन्त + अम् अंश + अम् ग्रहँ + श्ना + लोट् + सिप्` | `सूच्याः द्वितीयात् तृतीयपर्यन्तम् अंशं गृहाण` |
| Truth test | `यदि अवस्था + सुँ असँ + लट् + तिप्` | `यदि अवस्था अस्ति` |
| Negated truth test | `यावत् अवस्था + सुँ न असँ + लट् + तिप्` | `यावत् अवस्था नास्ति` |
| Range exclusion | `सूची + अम् वृज् + णिच् + क्त्वा चिञ् + श्नु + लोट् + सिप्` | `सूचीं वर्जयित्वा चिनु` |

State placement, collection formation, collection insertion, indexed retrieval,
collection membership, collection concatenation, collection cardinality, and
collection slicing now normalize to
backend-neutral semantic operations containing their resolved kāraka
expressions. Runtime actions and compiler IR lowering consume the same objects.
An argumentless `मुद्र्` invocation is also represented explicitly as discourse
anaphora to the preceding result instead of being inferred by a dhātu-prefix
test in the compiler. The loop clause `विजयः न भवति` is shared as a typed
reported-outcome test rather than being independently recognized by each
backend.

The older dative `दा` assignment, causative `क्षिप्` append, instrumental `स्था`
indexing, truth-equality comparison, and ablative-as-hidden-exclusion forms remain
accepted for compatibility, but examples should migrate to the natural frames.

`ततः` is reserved as the structural sequence connective. It is no longer also
parsed as a sentence-internal indeclinable, which ensures that pre-verbal
operands following it belong to the next clause. Use `अथ` or `अनन्तरम्` for an
ordinary lexical “then” inside a clause.

## Remaining language work

### Semantic normalization

- Extend the shared semantic layer, which now recognizes truth predicates,
  range selection/exclusion, the explicit `फल` anaphor, state placement,
  insertion, and retrieval, with typed procedure invocation.
- Build those nodes from resolved grammatical bindings once, then share them
  between the interpreter and compiler.
- Remove the remaining compiler checks for source spellings and route lexical
  identity checks through typed semantic predicates.

### Discourse and declarations

- Make `इति` naming produce a typed discourse referent with stable gender,
  number, and value type.
- Carry declared morphology into later references so identifiers decline by
  their declared lexical head rather than heuristics.
- Represent the active `सीमा` as explicit discourse context in semantic
  normalization instead of separate interpreter/compiler special handling.

### Procedures

- Give procedure parameters grammatical participant declarations and typed
  local referents.
- Compiler call detection now consumes the existing invocation AST directly;
  ordinary leaves, conditional tests and branches, loop tests, and loop-result
  targets now do the same. Compound quotations and nonstandard sequences also
  enter shared planning as parsed program nodes. Rendering is now diagnostic
  only inside the compiler frontend; it is not fed back into the parser.
- Prefer genuine finite verbs for callable actions; retain action noun plus
  `कृ` only where it is an ordinary Sanskrit light-verb construction.
- Define return and early termination through grammatical constructions rather
  than reserved source fragments.

### Collections and structured values

- Add a separately justified empty-list construction if a program genuinely
  needs one; nonempty list construction now uses transitive `सम् + ग्रह्`.
- Define a grammatical frame for removal
  without positional or surface-text conventions.
- Replace implicit `फल` pipelines with typed anaphoric reference while retaining
  `फल` as an ordinary explicit discourse reference.

### Validation and migration

- Diagnose case/valency errors in Sanskrit terms, including the expected kāraka.
- Add interpreter/compiler equivalence tests for every natural frame.
- Migrate examples and projects incrementally; keep compatibility tests for old
  forms until a documented deprecation release.
- Add a corpus gate that rejects newly introduced raw-source semantic checks.

## Current priority

The shared semantic-normalization layer now covers state placement, collection
formation, collection insertion, indexed retrieval, membership, concatenation,
cardinality, and slicing through resolved kāraka bindings. All checked-in examples use the natural placement and collection
frames; the old dative assignment and causative-list forms remain only in
compatibility tests. Explicit and implicit result flow now share a typed
`LastResult` reference (with `फल` retained as its Sanskrit surface anaphor).
Procedure overload guards read `त्व` from parsed taddhita morphology instead
of searching source strings, and repetition is excluded from kāraka extraction
by its AST category rather than spelling overlap.
Resolved procedure arguments now retain their declared parameter, source pada
and value, and positional/named/pipeline origin. Natural named calls use a
locative slot (`दक्षिणे द्वि`); the older genitive label remains a compatibility
form. Next, replace remaining compiler-only lexical conventions with shared
semantic nodes and improve grammatical diagnostics. Ordinal procedure operands
(`प्रथम`, `द्वितीय`, and so on) and their arithmetic meaning are now normalized
into backend-independent semantic nodes before compiler lowering, as is
`समवाय` collection-parameter summation. Ordinals are read from typed
`SankhyaPuranaPada` morphology (including the traditional first-ordinal
segmentation `प्रथ् + अमच्`) rather than by splitting source text.
Copular comparison predicates (`सम`, `न्यून`, `अधिक`) and the collective
procedure operand `समवाय` likewise carry explicit lexical identities; kāraka
binding and argument substitution no longer infer these meanings from raw text.
Procedure type checking now receives the resolved `फल` anaphor from its source
pāda as well; the compiler no longer discovers prior-result arguments by
cutting and comparing their serialized source.
The discourse nouns `फल`, `विजय`, and `सीमा` now have typed lexical identities.
A parsed `सीमा` declaration enters one shared `PvmDiscourseContext`, which both
the interpreter and compiler consume instead of independently scanning for a
special range declaration.
Truth values (`सत्य`, `असत्य`), common outcome predicates, and procedure
signature vocabulary (`मान`, `परिणाम`, `सङ्ख्या`, `शब्द`, `सूची`) are also
resolved through lexical AST identities. Signature semantics therefore depend
on parsed nominative declarations and typed vocabulary, not surface comparison.
Structured-result schemas no longer depend on a name ending in `परिणाम` or on
an overloaded possessive `मतुप्` construction. They are declared by the
compositional sentence “X and Y are fields of Z”: coordinated nominatives,
genitive schema ownership, plural `क्षेत्र`, and plural `अस्` agreement.
Record construction likewise no longer interprets alternating accusative
value/name pairs followed by a bare `मतुप्` nominal. Each field is now an
independent finite copular assertion: genitive possessive owner, nominative
field subject, nominative value predicate, and singular `अस्`. Assertions merge
by their grammatically identified owner in both interpreter and compiler.
Standalone attribute reads are finite clauses as well: a genitive possessive
chain identifies the record, an accusative field is the object, and imperative
`ग्रह्` denotes retrieval. The older predicate-less fragment `गुणवतः मूल्यम्`
is no longer classified as an executable field read.
Non-accusative field endings can no longer be used as hidden output-format
controls on retrieval; `ग्रह्` requires its field object in dvitīyā.
The object's case now marks only its grammatical relation to `ग्रह्`; it does
not silently redecline the retrieved value. Retrieval therefore preserves the
stored typed value for subsequent computation and presentation.
Compiled procedure calls now retain `ResolvedPrakriyaArgument` objects through
lowering. Prior-result identity and referent names are taken from their parsed
nominals by the shared semantic model, rather than rediscovered by splitting
serialized argument text in the compiler.
State, reported-outcome, and range-exclusion referents likewise use the typed
prātipadika reference key. Their identity is independent of external case and
is no longer obtained by trimming serialized `+ sup` source fragments.
Canonical conditional pipelines now state the displayed object explicitly as
`फलम्`; argumentless `मुद्र्` remains accepted only as compatibility ellipsis
for an immediately preceding pipeline result.
Segmented nominal branch values are retained as parsed pādas on the invocation
AST. Readable rendering therefore declines forms such as `विजय + सुँ` to
`विजयः` without leaking raw segmentation or reparsing source text.
Katapayādi, Āryabhaṭīya, and bhūtasaṅkhyā AST operands now render their parsed
case on the code-word instead of echoing the segmented source notation.
The ad hoc `पञ्चन् + दशत` encoding of fifty has been removed from canonical
structured-value source in favor of the attested numeral stem `पञ्चाशत्`.
The private doubling helper now bears the honestly derived action name
`गण + ल्युट्` (`गणनम्`); the former `द्विगुणन + ल्युट्` redundantly attached
an action-noun suffix to an already nominalized base.
Canonical rule declarations now use the grammatical lexemes `अधिकार`, `नित्य`,
`अन्तरङ्ग`, and `अपवाद`. Their older pseudo-segmentations remain compatibility
inputs but no longer appear in the conflict-resolution project.
That project also uses `गण + ल्युट्` (`गणनम्`) consistently and marks the
feminine agreement of `नित्या प्रक्रिया` explicitly with `टाप्`.
All canonical examples and projects now spell the imperative light verb as
`डुकृञ् + उ + लोट् + सिप्` (`कुरु`); the underspecified `कृ + लोट्` form is
retained only in compatibility coverage.
Canonical inheritance projects now use lexical `अधिकार`/`अपवाद` markers and
the valid action noun `गणनम्`; broken rendered forms such as `का`, `वादः`, and
`गोणनम्` no longer originate from checked-in project source.
All canonical reusable procedures now use an explicit finite declaration,
`नाम इति प्रक्रिया अस्ति`. A bare nominative action noun remains accepted only
as a legacy header and no longer silently opens a block in checked-in programs.
Already-nominal procedure names (`गणित`, `समवाय`, `सीमा`, and `जटिलगणित`)
no longer take the action-forming suffix `ल्युट्`. Their calls now use ordinary
instrumentals—`गणितेन`, `समवायेन`, `सीमया`, and `जटिलगणितेन`—while preserving
the same typed procedure identities in interpreted and compiled projects.
Productive `क्त` stems are now marked as declinable a-stems by the derivation
layer and receive ordinary nominative and instrumental endings in readable
PVM. The morphology project uses the supported derivation `भू + क्त` (`भूतः`,
`भूतेन`) rather than presenting an already-derived surface as if it were a
dhātu. Extending correct `क्त` formation to further seṭ/aniṭ roots remains a
derivational-coverage task; it must be implemented by grammatical rules rather
than renderer word substitutions.
Canonical reusable-procedure pipelines no longer encode execution with the
artificial tail `पूर्वस्य परस्य एका कुरु`. Each stage is now a genuine
instrumental (`गणनेन`, `वियोजनेन`) under its genitive domain, and `ततः`
compositionally supplies stage order. The old directive remains a compatibility
parse only. Kṛdanta rendering also carries parsed upasargas into derivation, so
`वि + युज् + णिच् + ल्युट्` yields `वियोजनम्`/`वियोजनेन` rather than silently
losing `वि`.
Bare nominal conditional branches now retain a parsed nominative pada instead
of only an untyped source string. Interpreter rendering and compiler lowering
share the case-independent `Pratipadika.semanticKey()`, and numeric branch
nominals remain typed `Sankhya` constants rather than becoming words through
`substringBefore('+')` source surgery.
Reusable-pipeline arguments now remain available as parsed `Pada` objects on
the pipeline AST. Compiler argument resolution reads each prātipadika's
`semanticKey()` and no longer removes case morphology by truncating a serialized
`+` expression. The legacy string list remains only as a compatibility
projection for older runtime interfaces.
Copular structured-field assertions now retain their parsed predicate pada.
One shared `assertionValue` path supplies typed numbers, truth values, and
nominal words to both interpreter storage and compiler `BuildRecord` lowering.
The compiler no longer discovers numeric fields by splitting and reevaluating
the serialized `valueStem`, and interpreter attribute reads now preserve the
same typed value instead of reconstructing a word at lookup time.
Structured owner, field, and predicate identities now come from exhaustive
typed-pada handling and `Pratipadika.semanticKey()`. Kṛdanta, unādi, compound,
and prefixed identities are no longer collapsed to their first serialized
morpheme; numeral systems retain their parsed stems or code words.
Reusable-procedure call frames now consume the already-resolved argument
objects directly. Arguments such as `युज् + ल्युट्` and
`वि + युज् + ल्युट्` remain distinct even though they share a verbal root;
runtime binding no longer pairs values by taking text before the first `+`.
Shared leaf planning now creates symbolic operands from parsed prātipadika
identities as well. Prefixed and derived instruments are no longer reduced to
their first written segment before interpreter/compiler operation resolution.
Procedure prohibition guards now evaluate every retained numeral stem, so
different compounds that begin alike are not conflated. Procedure argument
extraction also recognizes parsed Bhūtasamkhyā accusatives; for example,
`भूतसङ्ख्या नेत्र + वेद + अम्` remains one typed value (42) rather than falling
through a generic source-suffix trimmer.
Validator diagnostics now retain the complete derived operation identity when
locating an invalid call, rather than highlighting only its first dhātu or
upasarga. Feminine nominal rendering likewise reads a lexical mūla directly
from the typed prātipadika and uses the derived base for non-mūla forms; it no
longer guesses the base by truncating serialized morphology.
Conditional and collection-filter actions now require a typed `Satya` result;
the rendered word `सत्यम्` can no longer masquerade as a boolean. Negation is
recognized through the parsed `AvyayaFunction.NISHEDHA` category in shared
normalization and compiler lowering, rather than a second literal check for
`न`. Copular comparison actions similarly trust the operation selected from
the typed predicate morphology instead of re-reading the rendered words
`समम्` and `न्यूनम्` inside the runtime action.
The bounded-loop nouns `प्रयत्न` and `अन्त` now have shared lexical AST
identities. Attempt and `पर्यन्त` boundary validation uses those identities
instead of embedding independent string comparisons in the parser builder.
The lexical prior-result noun `फल` is now canonicalized by the AST expression
builder to the internal `LastResult` reference before compiler IR lowering.
Compiler operands use `LoadLastResult`, and neither IR lowering nor generated
runtime lookup accepts a second spelling-based `फल` alias. Piped compiled
procedure calls inject the canonical reference directly as well.
Resolved procedure arguments now derive prior-result status from their typed
`PIPE` origin. Type checking therefore accepts a preceding numeric result
without requiring the injected operand to be falsely named `फल`.
Executable clause relations now expose typed `SequenceConnector` identities:
`SAMUCCAYA` for `च` and `ANANTARYA` for `ततः`. Pipeline classification,
execution, binding, and compiler lowering consume the typed relation; raw
connector surfaces remain only for faithful rendering and compatibility.
Leaf-planner symbolic discovery now excludes truth constants through
`MulaPratipadikaIdentity.SATYA` and `ASATYA`; it no longer reserves variables
by comparing their source names with `सत्य` and `असत्य`.
Leaf-planner symbolic kāraka discovery now resolves sup morphology through
`SupAffix` and `Vibhakti`; accusative and instrumental recognition no longer
enumerates raw singular, dual, and plural suffix spellings.
Language-level verb recognition now resolves a parsed dhātu through its
Dhātupāṭha entry and compares a stable `CanonicalDhatuIdentity`. Copular,
choice, exclusion, display, arithmetic, assignment, procedure-call, and
structured-value semantics no longer maintain local lists of mūla/upadeśa
spellings; structured clauses also resolve tiṅ endings through `TingAffix`.
Readable-text derivation now consumes those canonical dhātu identities for
absolutives, kṛdantas, and irregular imperative surfaces too, keeping rendering
and execution aligned when a source uses either a mūla or an upadeśa spelling.
The multi-file division procedure now supplies the actual root in its segmented
causative (`भज् + णिच्`), leaving guṇa to derivation instead of redundantly
encoding the already-strengthened `भाज्` before `णिच्`.
Range exclusion and readable derivation now identify `क्त्वा`, `ल्युट्`, and
`णिच्` through the core `KrtAffix` and `SanadiAffix` inventories rather than
embedding their upadeśa strings in semantic conditions.
A corpus-level architecture test now guards the shared normalizer, kāraka
extractor, structured-value recognizer, direct-assignment recognizer, and
compiler frontend against dispatch on serialized Sanskrit or raw mūla spelling.
Procedure declarations and body binding now identify parameter and result
referents through `Pratipadika.semanticKey()`. Derived names such as
`युज् + ल्युट्` retain their complete morphology across case changes instead of
being reconstructed by splitting serialized source text.
Abstract-state taddhitas now carry the typed `BHAVA` class (`त्व` and `तल्`).
Procedure type guards consume that class rather than assigning programming
meaning by comparing the suffix text with `त्व`.
Procedure arity and parameter-type failures now use one shared Sanskrit
diagnostic in interpretation, validation, and compiler lowering. Diagnostics
name the declared procedure/participant and say how many `मान` values or which
Sanskrit value type the grammatical call requires.
Operation-resolution failures now name missing and incompatible kārakas with
their traditional Sanskrit labels (`कर्मन्`, `करण`, and so on). Shape,
unresolved-reference, member-count, and saṃjñā failures likewise use grammatical
Sanskrit diagnostics rather than leaking internal enum names and English prose.
Procedure and domain headers now use an AST-built morphological key. Upasargas,
dhātu, sanādi, kṛt/unādi, compounds, strī suffixes, and relevant taddhitas are
composed from typed fields; misleading or differently spaced `sourceText`
cannot change the registered procedure identity.
Adhikāra scopes now retain both the segmented declaration (needed for taddhita
inheritance) and a structural, case-independent `domainIdentity`. Project and
module registries consume the latter directly instead of stripping sup endings
from serialized header text.
Reusable `Prakriya` AST nodes now carry parsed `nameIdentity` and
`domainIdentity` fields alongside their display source. The interpreter project
loader, static validator, and multi-file compiler consume those identities
directly and no longer strip sup endings from procedure-header strings.
Procedure registry matching now compares these canonical operation/domain keys
directly. Domain inheritance maps relate the same keys, so registry dispatch no
longer normalizes or removes suffix-looking fragments during semantic lookup.
Compiler-local symbols now use the already parsed operation identity. The old
`substringAfter("ङस्")` convention—which could mistake a root or derived name
containing those characters for a domain separator—has been removed.
The legacy call-frame constructor that reordered arguments by normalized source
strings has been removed. Production and tests now pass
`ResolvedPrakriyaArgument` objects, and prohibition guards resolve bound
nominals through `semanticKey()` rather than `pratipadika.sourceText`.
Positional procedure resolution now retains existing `PrakriyaArgument`
objects directly, including their parsed pāda, origin, and typed value. It no
longer projects AST operands to strings and searches for matching text before
building the call frame; pipeline binding likewise prefers parsed nominal keys.
Named locative/genitive slots now return their actual accusative value pādas and
declared `PrakriyaParameter` objects. Invocation resolution preserves attached
values by AST object identity, using term text only as a legacy fallback when a
caller supplied no parsed operand.
`स्वं रूपम्` evaluation now accepts a parsed `Pratipadika` and obtains its
case-independent identity from the AST. The execution binder no longer removes
suffix-looking text from a serialized word; the raw-string compatibility API
treats its input as an exact lexical identity, so a genuine word ending in a
sequence such as `+ अम्` cannot be silently shortened.
Explicit procedure-marker parsing now retains the declaration pādas alongside
their presentation text. Qualified declarations and taddhita receiver-method
headers derive operation and domain identities directly from those existing
nodes; they are no longer serialized and parsed a second time before registry
construction.
Adhikāra recognition now returns its segmented presentation and structural
domain identity together from one parsed header. Script construction no longer
parses the same अधिकार declaration once to classify it and again to recover
its registry key.
The registry's legacy `stripSupSuffix` text utility has been removed entirely.
Tests and registry fixtures now take `nameIdentity` from the parsed `Prakriya`
AST, preventing suffix-shaped lexical material from being mistaken for case.
Named and positional procedure-argument resolution now preserves parsed pādas
even through its direct AST API. Parsed nominal identity takes precedence over
legacy term text for `फल` anaphora and binding names.
Niṣedha guards now consume typed runtime values or values derived from retained
argument pādas. They no longer split normalized source strings and ask the
number engine to reinterpret them during semantic guard evaluation.
The lotto example now expresses random selection as `सङ्ख्यां चिनु`, exclusion
as `क्रमं वर्जयित्वा`, and its temporary चयन referent as parsed
`चि + ल्युट्`. Its readable form therefore derives `चयनम्`/`चयने` correctly,
and an execution regression verifies six distinct main values plus a distinct
bonus within the declared range.
Range selection no longer interprets an arbitrary nonnumeric ablative as an
exclusion list. Exclusion must be stated compositionally as an accusative object
of `वृज् + णिच् + क्त्वा` (`क्रमं वर्जयित्वा`). The simple dice example now
uses the Divādi present stem `दिव् + श्यन्` (`अक्षं दीव्य`) rather than an
unmotivated causative.
Gaṇa-qualified dhātu lookup now prefers the unique executable specialization
when the lexical Dhātupāṭha row and runtime entry share a root. This lets
`दिव् + श्यन्` resolve by its explicit Divādi morphology without falling back
to a causative merely to disambiguate the operation.
Compiler pipeline arguments now require their retained `SubantaPada` nodes and
derive referents with `semanticKey()`. The last compiler-side handwritten sup
suffix list and arbitrary string-stemming fallback have been deleted.
Procedure calls now require the declared operation noun in instrumental case
as the grammatical means of `कृ`. The matcher no longer accepts an accusative
root by deleting a textual `ल्युट्` suffix from registered operation names.
Procedure-body pipeline binding now substitutes parameter references only when
an argument has a parsed nominal pāda. A legacy string-only pipeline argument
is retained for display but cannot acquire semantic identity through whitespace
normalization.
Named procedure-argument resolution no longer accepts or reparses `karmaText`.
Positional, named, empty, and invalid-arity calls all flow through the retained
argument pādas, and the synthetic string-only `fromTerms` constructor is gone.
Antaratama overload ranking now consumes value types derived from parsed pādas
or known injected values, rather than asking a numeral parser to classify
serialized argument terms. Structured pipeline invocation now accepts preserved
`PrakriyaArgument` objects instead of a list of strings; subsequent stages carry
the preceding typed result with explicit `PIPE` origin.
Registry argument terms for ordinary subantas are now projected from
`Pratipadika.semanticKey()` rather than `sourceText`, so compatibility fields,
diagnostics, and downstream frames retain the same referent as the AST.
Tests, the number-guessing project, and the hundred-prisoners project now invoke
plain nominal procedure names in the instrumental (`योग + टा`, `प्रयत्न + टा`)
rather than the obsolete accusative form. The prisoners example also declares
procedure names in the nominative and expresses the box-number comparison as an
explicit conditional, instead of relying on a standalone declarative equality
to mutate implicit `फल`. Its aggregate success flag is now monotone: a later
successful prisoner cannot overwrite an earlier failure.

The hundred-prisoners program now completes a bounded 100-by-50 random trial
without semantic diagnostics. A nominative copular truth predicate such as
`सर्वजय + सुँ असँ + लट् + तिप्` is normalized identically in `यदि` conditions
and `यावत्` loops; it no longer falls through to the equality and ordering
overloads of `अस्` and spuriously requests करण, कर्मन्, or अपादान. A nested,
bounded two-prisoner regression covers the procedure/loop/conditional boundary.
Anonymous script and project executions now keep their shared discourse in
memory instead of serializing the entire growing conversation after every
sentence. An explicitly supplied session key retains the former durable
behavior. Regression tests cover both policies. This removes quadratic file
rewrites without weakening anaphora within the running script; profiling shows
that in-memory `resultHistory` and kriyā-memory copying is the remaining large
loop cost, so that history must next become structurally shared or be reduced
by an AST-derived liveness policy rather than an arbitrary fixed cap.
CLI file execution now treats its script discourse as ephemeral: it remains in
memory for the complete file and is never committed to the session store.
Explicitly named API sessions remain the opt-in durability mechanism. Pipeline
procedure stages inherit this policy instead of silently restoring persistence.
A CLI regression verifies that successful ordinary execution creates no files.
A live JFR profile then located the remaining
cost in duplicate historical maps and repeated kriyā-id indexing. Turn-qualified
results now live only in chronological `resultHistory` (latest invocation and
named bindings remain in their maps), and `KriyaMemory` maintains its identity
set incrementally instead of rebuilding it from every previous frame on every
sentence. Grammatical historical selection remains authoritative; technical
turn identifiers are no longer redundantly copied into the ordinary namespace.
Chronological result and kriyā histories now use immutable 128-element chunks.
Appending a turn copies only the bounded tail chunk and a short chunk index;
older conversation snapshots retain exact list semantics, including arbitrary
ordinal access, without copying every earlier entry. Focused core and execution
lifecycle tests cover snapshot isolation and boundary indexing. With cached
grammatical analyses and reusable execution blueprints, the complete
hundred-prisoners program now finishes in roughly thirteen seconds in an
end-to-end Gradle CLI run (including JVM/Gradle startup) and prints the trial
outcome. Further performance work should benchmark the application process
separately from build startup and compare interpreter, compiled backend, and
direct primitive lowering without weakening Sanskrit history semantics.

## Current audit checkpoint — 2026-10-04

### Review of proposed grammar claims

Source inspection, not a claim of complete Sanskrit coverage:

1. Do not simply remove `IT` from `agama`: इट् is a legitimate augment
   (7.2.35). The lexer also uses that spelling for a tiṅ ending. Preserve
   contextual augment/ending identity; audit the ending's upadeśa notation
   separately rather than deleting the augment.
2. णिच् is already a parsed sanādi affix with raw-upadeśa introduction,
   it-processing, and derivation-trace tests. Coverage remains restricted.
   `dhatuMula` accepts arbitrary identifiers, so canonical-root validation and
   legacy surface-stem routing still need auditing; grammar alone cannot
   distinguish an unknown root from a pre-derived stem.
3. शक् already occurs in both svādi and divādi dhātupāṭha entries. Lexical
   presence does not prove executable derivation coverage. The proposed
   "eight kṛts" were not named, so their individual absence is unverified.
4. The cited 4.1.79 is गोत्रावयवात्, a feminine gotra-derived formation,
   not a general root-to-nominal rule. Do not add a blanket
   `pratipadikaMula -> dhatuMula` alternative on this basis.
5. Upasargas qualify the verbal construction; they are not kārakas.
   Preserve their morphological identities and compositional effects rather
   than recasting prefixes as argument roles.
6. च is a connective, not itself a sandhi operation. Boundary sandhi belongs
   to derivation/rendering. इति already has one lexer token and contextual
   uses, including quotation and clause relations; audit AST senses rather
   than duplicating tokens.
7. अधिक and ऊन are lexical stems, not outputs of तरप्/तमप् alone. Degree
   suffixes extend a supplied stem (e.g. अधिकतर), not replace lexical
   comparison meanings. Audit morphology-based comparison routing separately.
8. `vyutpattiTinganta` is a separate parser entry, not reachable from `ukti`,
   and no handwritten caller was found. Decide whether to expose it as a
   derivation-only API or remove it. `DANDA` already accepts ASCII `.`;
   documenting or restricting that compatibility policy remains a decision.

References: [7.2.35](https://sanskritlibrary.org/grammatical/data/A.7.2.35.html),
[1.4.59](https://sanskritlibrary.org/grammatical/data/A.1.4.59.html),
[4.1.79](https://ashtadhyayi.com/sutraani/4/1/79),
[5.3.55](https://ashtadhyayi.com/sutraani/5/3/55), and
[5.3.57](https://ashtadhyayi.com/sutraani/5/3/57).

This review changes documentation only; none of these proposed grammar
mutations has been applied. The preceding input-choice validation passed
focused action and CLI tests.

Implemented and verified in the current worktree (not yet committed):

- Source-root binding now uses canonical lexical root/upadeśa forms, separately
  from historical surface-alias lookup. Both finite and non-finite bindings
  reject inflected/nominal aliases such as तिष्ठति and स्थानम्. Root identity
  and explicit-gaṇa regressions pass. Full root disambiguation and remaining
  rendering fallbacks are still pending.
  Compiler and CLI suites passed; all 260 execution tests passed after CLI
  regeneration of the 44 readable example artifacts. The initial execution
  run exposed stale त्रीनि spellings from the preceding numeral correction;
  regenerated files now use त्रीणि and satisfy the reproducibility check.
  The source-root index now retains all candidates instead of overwriting
  collisions in registration order. A root without sufficient distinguishing
  morphology resolves only when its executable lexical identity is unique.
  A lexicon-wide regression checks every indexed spelling; explicit vikaraṇa
  selection retains its gaṇa constraint and rejects contradictory gaṇas.
  Execution, compiler, and CLI suites passed after this collision-handling
  change, including all five focused source-root/gaṇa regressions.
- Tiṅanta derivation requests can now retain the selected gaṇa. The renderer
  passes this identity downstream, and क्षिप no longer needs a hardcoded
  imperative: tudādi derives क्षिप while divādi derives क्षिप्य. Tests cover
  both and rejection of an incompatible gaṇa.
  This exposed a legacy mismatch in two-counter-machine comparisons: सिप्
  is supplied for the executable ātmanepada विद्. The renderer no longer
  borrows विद्धि from a different homonym; its raw fallback remains visible.
  The predicates were subsequently migrated below. Enforcing pada validity
  at execution binding remains pending, along with removing the other
  hardcoded verb forms.
  Tiṅanta-engine tests, connector rendering tests, and the example architecture
  checks passed after CLI regeneration. This is focused verification, not a
  full-suite checkpoint for the new derivation-request field.
- The two-counter-machine example now uses copular न्यून predicates with
  nominative numeric subjects and ablative standards, removing its legacy
  विद् commands and negation-based loop test. Compiler condition lowering
  consumes these two kāraka values directly in LESS_THAN IR, rather than
  trying to load the adjective as a program variable. Predicate rendering
  now recognizes neuter numeral subjects and derives न्यूनम् accordingly.
  Symbolic leaf planning now models the nominative subject and ablative
  standard of this grammatical construction, preventing an unknown variable
  from incorrectly selecting the legacy collection-membership overload.
  Compiler IR and structured-backend suites plus rendering and example
  architecture checks passed. The compiled machine retains its expected
  result of three and seven loop iterations.
  General pada validation and other legacy comparison examples remain open.
- Copular ordering now consumes retained predicate identity: न्यून selects
  LESS_THAN and अधिक selects GREATER_THAN in runtime and compiler IR.
  Unsupported identities fail instead of silently defaulting to less-than;
  equality requires सम. Predicate identity is independent of mutable runtime
  values. Focused action regressions cover malformed and rebound operands.
  The three focused action tests, full compiler suite, and all 264 execution
  tests passed. An architecture assertion was updated to recognize the
  expanded nominative/ablative symbolic-planning filter rather than its old
  source-code spelling.
- Equality now has a shared typed structural definition for copular runtime
  comparisons and JVM equality instructions. Numeric and boolean rendering
  variants do not affect equality; text is distinct from numbers; lists and
  groups retain kind/order/length; records compare schema and field values,
  not only their displayed schema name. Rational cross-products use exact
  arithmetic to avoid overflow. Copular equality requires a single subject
  value and single standard value rather than inventing existential semantics
  for coordinated operands. Collection membership was subsequently migrated
  to the shared comparator below.
  Four core equality regressions, four focused predicate-action tests, and
  the full execution/compiler suites passed after this change.
- Runtime and compiled collection membership now share typed structural
  equality. Group collections are unpacked like lists; natural locative
  membership requires one actual collection and one subject value, rejecting
  scalar locations rather than silently treating them as collections.
  Focused collection-action and compiler IR tests cover numeric spelling
  variants, text-versus-number separation, and differing record fields;
  example execution/rendering architecture checks passed.
  The full execution and compiler suites also passed after this membership
  change. Legacy accusative/instrumental membership frames remain compatibility
  paths; their syntax and quantifier semantics still need migration.
- Added `examples/collections/membership.pvm` with nominative subjects and
  a locative collection, covering present and absent members inside यदि.
  Its readable artifact is CLI-generated. An end-to-end test verifies both
  truth outcomes, interpreter parity, and absence of interpreter evaluation
  calls in generated code. Structured-backend and example architecture suites
  passed. Standalone present-tense clauses remain declarations; the first
  test version exposed that semantic boundary, so evaluation is demonstrated
  in an explicit conditional rather than silently changing declarative intent.

- Final-member extraction uses a genitive collection and accusative member:
  `सूची + ङस् अन्तिम + अम् उद् + हृ + लोट् + सिप्`.
  Runtime and compiler share `CollectionExtraction`; the stored collection
  remains unchanged. Unsupported member selectors and empty collections fail.
- Natural indexed retrieval and slicing unpack both `Suchi` and `Gana` and
  reject scalar source collections. Positions and bounds must each resolve to
  one number. Slice offset conversion no longer overflows at `Int.MIN_VALUE`.
- One-based locative ordinal indexing is verified through both execution
  paths. `examples/collections/ordinal_index.pvm` demonstrates it, and readable
  `.txt` files are generated through the CLI rather than edited manually.
- Input actions enforce type, choices, and range on host responses. Reversed
  bounds yield language failures; numeric ranges never constrain text input.
  Malformed explicit numeric bounds now fail before host dispatch rather than
  silently inheriting the active range. Both bound roles have regressions for
  unresolved references, text values, and multiple numeric members.
  Text and choice values retain their declared type, including numeric-looking
  strings such as `००७`.
  Choice references remain runtime data and must resolve: an unresolved member
  now produces a value failure instead of silently narrowing the allowed set.
- Prefix rendering composes every boundary from the verb outward, using
  existing 8.3.23, 8.4.58, 8.4.62, and savarṇa-dīrgha rules rather than a
  duplicate character table. Rendering regressions cover `सङ्गृहाण`,
  `संयोजय`, `सन्निक्षिप`, and `उद्धर`.
- The language guide now describes explicit प्रक्रिया declarations, matching
  instrumental calls, grammatical private qualifiers, current input and loop
  syntax, one-based collection positions, and installed-CLI rebuild behavior.

Verification: the earlier accumulated checkpoint passed 1,204 tests across
core, actions, derivation, execution, compiler, and CLI. Subsequent changes
have focused collection, compiler IR, ordinal parity, and rendering tests;
that earlier count is not a claim that every later edit was broadly retested.

The later accumulated checkpoint on 2026-10-04 passed:

`gradlew.bat :core:test :actions:test :ashtadhyayi:test :derivation:test :execution:test :compiler:test :cli:test --no-daemon`

JUnit reports contain 1,739 tests with zero failures and errors: core 105,
actions 87, ashtadhyayi 501, derivation 646, execution 264, compiler 102,
and CLI 34. The build finished in 1m 13s; this is build/test duration, not a
program-performance benchmark. `git diff --check` also passed. This checkpoint
does not establish full Sanskrit coverage or completion of the roadmap, and
does not rebuild the installed CLI distribution.

Subsequent agreement work: predicative adjectives now inherit the gender of
the noun counted by a numeral subject, using neuter only for a bare numeric
value. Feminine agreed a-stem adjectives use the strī-affix engine instead of
attempting to decline an unformed masculine stem. Feminine numeral one now
derives its टाप् stem before declension, yielding एका rather than a raw एक
fallback. The numeral suite and focused renderer tests passed; broader
coverage of numeral gender/number agreement remains unfinished.

Explicit strī suffixes on a counted nominal now determine numeral gender
before lexical lookup. Regression coverage includes `एका बाला गुप्ता अस्ति`.
The underlying audit exposed duplicate टाप् introduction when declining an
already-formed feminine stem: 4.1.4 now respects the stem's recorded formation
provenance. Independent stem derivation and sup-declension assertions plus
the renderer tests and full derivation suite passed.
The full ashtadhyayi suite and example execution/reproducible-rendering
architecture checks also passed after the provenance fix.

The feminine-suffix audit found that readable generation substituted टाप्
for explicit डाप्/चाप् and ignored other unsupported feminine morphology.
Unsupported suffixes and multi-suffix chains now retain the full segmented
nominal source instead of fabricating a different derivation. Tests cover
डाप्, चाप्, ऊङ्, तिच्, and a suffix chain; renderer and example architecture
checks passed. Actual rule-based support and structured diagnostics for these
unresolved forms remain pending; preserving source is not a completion claim.

The final nominal-rendering fallback now retains the complete segmented source,
including its case and number, when derivation fails. It no longer presents a
bare stem as a successfully declined word. A regression covers invalid
`द्वि + सुँ`; focused renderer and architecture checks passed. Regenerating
all 45 readable examples exposed remaining unsupported numeral forms, including
twenty/thirty accusatives, fifty nominative, and `षष्` accusatives. These need
paradigm/source analysis, not surface-form substitution.

The six audit identified source-number errors rather than missing षष्
declension: four accusatives across `double_danda_problems`,
`min_subset_sum_diff`, and `modulo_exponentiation` supplied singular अम्.
They now supply plural शस्. The existing numeral-paradigm regressions verify
nominative and accusative forms for five through ten across all three genders;
that suite passed, and the algorithm readable files were regenerated by CLI.
Execution architecture checks also passed after the migration.
The higher-numeral investigation found two renderer errors: lexical feminine
numerals were forced to neuter, and singular nominal quantities were rejected
solely because their numeric value exceeded one. Primitive inflection classes
now select intrinsic gender before referent agreement; higher cardinals default
to singular without prohibiting inflected plural quantities. The number-policy
test passed. Regeneration now derives twenty accusative and fifty nominative.
An explicit thirty-accusative regression remains failing: the engine produces
`त्रिंशत्म्` instead of `त्रिंशतम्`. This reveals a consonant-stem/sup vowel
joining defect; it must be fixed in derivation, not hidden by a numeral table.
The regenerated average example currently exposes that defect. The last focused
renderer run is therefore not green; broad verification remains pending.

Follow-up: the thirty failure came from 6.1.107 applying without a vowel-final
stem condition. अमि पूर्वः now checks that condition, so consonant stems
retain the अ of अम् through normal derivation. Regressions verify सरितम् and
त्रिंशतम् without 6.1.107, while मतिम् still records it. The focused renderer,
full derivation, and full ashtadhyayi suites passed. All 45 readable examples
were regenerated; the average statement now contains विंशतिं त्रिंशतं.
This supersedes the preceding failing-run note, not the broader outstanding
morphology and agreement audit.

Primitive numeral inflection classes now expose lexical gender once in core;
the renderer and public `SankhyaGenerator.decline` API both consume it. API
regressions verify twenty, thirty, fifty, and hundred across all requested
referent genders. The full numeral suite and focused renderer/architecture
checks passed. The guide's stale claim that every numeral from three onward
requires plural has been corrected. Compound higher-numeral classification
and comprehensive case/number coverage remain unfinished.

Predicate agreement now also consults primitive numeral-noun lexical gender
before defaulting to neuter or taking a neighbouring referent's gender.
Regressions verify `विंशतिः समा अस्ति` and `शतं समम् अस्ति`; the focused
renderer suite passed. Inspection of compound numerals found that normalized
`SankhyaPratipadika` retains value and source text, not a typed expression tree.
The numeral expression builder does retain additive/multiplicative structure;
connecting its head morphology to declension is still pending, rather than
guessing a compound's gender from the numeric value or rendered suffix.

The canonical cardinal expression builder's existing `headPrimitive()` now
supplies intrinsic gender to the public declension API and renderer predicate
agreement. All normalized cardinal subantas route through that API, rather
than bypassing it when no explicit gender override is present. Regressions
derive त्रयोविंशतिम्, पञ्चत्रिंशतम्, and द्विशतम्; the full numeral suite and
focused renderer suite passed. This handles canonical generated expressions;
retaining the original source expression tree (including alternative ऊन
constructions) through normalization remains an explicit provenance gap.

The public declension API now additionally accepts a supplied `SankhyaExpression`.
It derives that construction and selects its own head, instead of rebuilding
a canonical expression from its value. `SankhyaPada` readable generation uses
this overload on the evaluated source stems. A regression derives
एकोनविंशतिम् from explicit एक/ऊन/विंशति, preserving its feminine twenty
head despite the resulting value nineteen. The full numeral and focused
renderer suites passed before the additional source-boundary regression;
the source-boundary regression and example architecture checks also passed.
normalization of generic subantas and decoded numeral notations still needs
explicit construction provenance rather than a value-only reconstruction.

The next broad checkpoint exposed two legacy renderer-test assumptions:
singular दश had previously appeared as undeclined दश, and accusative
त्रिंशत् had previously lost its ending. The field-render test now supplies
दशन् + जस्, and the consonant-boundary sandhi test uses nominative
त्रिंशत् + सुँ so that it still tests an actual final त् boundary rather
than expecting accusative morphology to disappear. These are test-source
migrations; runtime field-assignment tests were not rewritten to mask errors.

Broad checkpoint after these corrections (2026-10-05):
`:core:test :actions:test :ashtadhyayi:test :derivation:test :sankhya:test
:parser:test :execution:test :compiler:test :cli:test` passed in 44 seconds
(55 tasks; 2 executed, 53 up-to-date). Current reports cover 1,832 tests:
core 105, actions 87, ashtadhyayi 502, derivation 646, sankhya 62, parser 23,
execution 271, compiler 102, CLI 34. This is regression evidence, not proof
of complete Sanskrit morphology or a runtime benchmark. Installed CLI
distributions were not rebuilt by this checkpoint.

Source-expression gender now propagates to numeral and predicate agreement,
not only to declension. An end-to-end ऊन regression exposed a parser gap:
`sankhyaStem` accepted IDENTIFIER but not the reserved UNA/ADHIKA tokens,
although the numeral evaluator already supported these constructions. The
rule now admits those existing morphological tokens; no new surface syntax
or arithmetic symbols were introduced. Parser, renderer, and execution
architecture tests passed, including parsed `एकोनविंशतिः समा अस्ति`.
The preceding broad checkpoint predates
this grammar edit.

End-to-end assignment regressions now verify the connected UNA/ADHIKA grammar
in both execution backends: एक/ऊन/विंशति stores 19 and
द्वि/विंशति/अधिक/शत stores 122. The focused compiler regression passed and
checks that emitted methods do not call runtime `evaluate`. This extends
evidence beyond readable generation; full invalid-construction diagnostics,
original expression provenance in normalized nodes, and wider inflection
coverage remain pending. The guide now documents the verified constructions.

Malformed UNA/ADHIKA numeral markers now report their missing left/right
operands explicitly instead of falling through to a generic primitive-stem
error. The optional अभि prefix cannot consume the entire remainder. Tests
cover leading, trailing, and standalone अधिक/ऊन/न्यून plus existing
positive-and-below-base invariants; no implicit operand is introduced.
The numeral suite and renderer/example architecture checks passed after the
diagnostic change. This does not yet provide source-spanned typed diagnostics
through every compiler/CLI path.

Verbal readable fallback now preserves the complete `TingantaPada.sourceText`
when derivation fails or the ending is unresolved, rather than returning only
the root and dropping prefixes/sanādi/lakāra/tiṅ. An explicit vikaraṇa whose
gaṇa has no matching root entry no longer falls back to a different gaṇa.
Regressions cover an unknown prefixed causative and incompatible स्था + श्नु.
The renderer suite passed, 45 readable examples were regenerated by CLI, and
execution architecture checks passed. Hardcoded successful verb surfaces,
full pada validation, and typed rendering diagnostics remain pending; visible
segmented fallback is not a successful Sanskrit derivation.

Direct derivation audit of the hardcoded स्थापय shortcut: a new regression
requests स्था (bhvādi) + णिच् + लोट् + सिप् from `TingantaEngine` and
currently fails with "No complete sanādi derivation plan". Do not remove the
shortcut until the proper chain is implemented and verified. Rule 7.3.36
requires पुगागम before णिच् for eligible ā-final roots; no executable
implementation was located. Next work is raw augment introduction, marker
processing, and compositional stem/ending derivation, not another surface
replacement. The newly added direct regression is intentionally still red;
the earlier broad green checkpoint predates it.
Reference: [7.3.36 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.36.htm).

Follow-up implementation: the ā-final portion of 7.3.36 now introduces a raw
पुक् augment (annotated पुँक् for nasal-vowel it processing), targeting the
root for placement through 1.1.46. स्था causative routing now enters the
sanādi engine; the direct स्थापय regression passed and the renderer's
hardcoded स्थापय arm was removed. The regression additionally checks raw
augment introduction and placement, not merely final surface equality.
The named non-ā-final roots of 7.3.36 and their exceptions remain uncovered;
this implementation is explicitly partial, not a complete rule claim.

The first broader run caught over-application to गण after its intermediate
vṛddhi (गणापय instead of गणय). Eligibility now requires retained lexical
ā-final root identity as well as the operative final vowel; a vowel produced
later by vṛddhi does not establish this lexical class. Existing गणय regressions
are retained unchanged. The full ashtadhyayi suite and focused renderer and
execution architecture suites passed after this correction. The full
derivation suite passed in the preceding run; broader verification of the
final eligibility change remains pending.

Expanded स्था verification confirms स्थापयति, स्थापयतः, स्थापयन्ति,
स्थापयसि, and स्थापयामि, each with exactly one 7.3.36 application and
complete it-processing. The full derivation suite passed after the final
eligibility change. The ordinary-root negative-control regression then
exposed a separate missing stem rule: noncausative स्था + लट् currently
produces स्थति instead of तिष्ठति. The combined regression is still red
on that assertion; do not weaken it to accept the incorrect form. Next work
must implement the relevant ordinary present-stem substitution with proper
suffix conditions so it does not disturb णिच् causatives.

The स्था member of 7.3.78 now replaces the root with तिष्ठ only before an
adjacent शप्/श stem-forming suffix. An intervening णिच् therefore prevents
the ordinary-root substitution. Both lexical ष्ठा and the executable स्थाञँ
entry are recognized; the direct तिष्ठति regression, paired ordinary/causative
rendering, full ashtadhyayi, and full derivation suites passed.
The other roots enumerated by 7.3.78 remain unimplemented in this rule.
Source: [7.3.78 Kāśikā](https://avg-sanskrit.org/sutras/7-3-78.html).

Post-verb-change artifact checkpoint: all 45 readable examples were regenerated
through CLI, then execution architecture checks and the entire
`StructuredBytecodeCompilerTest` suite passed. The reusable-language guide now
documents the verified ordinary/causative स्था distinction and complete-source
fallback policy, with explicit limits on coverage of the two sūtras. This
checkpoint does not rebuild the installed CLI or prove the remaining renderer
shortcuts (कुरु, गृहाण, and अस् forms) are derived.

Direct audits of the remaining imperative shortcuts now have independent
regressions. कृ (tanādi) + लोट् + सिप् produces कृउ instead of कुरु;
ग्रह् (kryādi) + लोट् + सिप् produces ग्रह्णीहि instead of गृहाण. Both
tests are currently red; renderer shortcuts remain until their actual
root/vikaraṇa/ending transformations are implemented. No incorrect engine
surface has been accepted as the expectation. Next implementation must trace
the tanādi stem alternation and the grah samprasāraṇa/imperative ending chain
separately; raw-root binding and existing operation semantics are unchanged.

The गृहाण source audit identifies a missing affix substitution before the
samprasāraṇa work: 3.1.83 हलः श्नः शानज्झौ replaces the whole श्ना with
शानच् after a consonant-final root before हि. The current engine instead
retains श्ना and applies 6.4.113, explaining the incorrect ग्रह्णीहि trace.
The intended chain also needs 6.1.16 and 6.1.108; no executable definitions
were located for these three rules. Raw शानच् introduction must preserve
it-processing and distinguish this vikaraṇa substitution from participial
शानच्: the existing 7.2.82 matcher currently selects शानच् by spelling and
could otherwise introduce an inappropriate मुक्. This is a concrete
provenance/conditioning requirement, not permission to collapse the entire
chain into a hardcoded गृहाण surface.
Sources: [गृहाण derivation](https://avg-sanskrit.org/2011/11/page/2/?ertthndxbcvs=yes),
[6.1.16 Kāśikā](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.16.htm).

3.1.83 now performs a fresh-upadeśa whole-affix substitution: adjacent
consonant-final root + श्ना + हि introduces raw शानच् through the existing
designation policy rather than pre-applying आन. The focused गृहाण regression
still fails during downstream derivation; this is an intermediate implementation,
not completed गृहाण support. Next work remains resolving the vikaraṇa's
provenance-sensitive processing, samprasāraṇa, and final ending treatment.

3.1.81 now respects substituted-affix identity and its recorded application,
preventing duplicate श्ना introduction after 3.1.83. 7.2.82 excludes the
imperative vikaraṇa substitute by provenance rather than spelling. An
independent regression confirms fresh raw शानच् and distinguishes its
processed आन from participial आन; it passed. The complete गृहाण test has
advanced past the duplicate-ID failure but remains red with a downstream
required-value failure. कुरु is also still red; these partial fixes do not
constitute a green full derivation checkpoint.

The two-counter example's two accusatives now use `द्वि + औट्`, and its three
accusative uses `त्रि + शस्`. Its readable artifact was regenerated through
the CLI rather than edited independently. Execution architecture checks and
the focused compiled two-counter regression passed: result three, seven loop
iterations, with JVM conditional and backward branches still verified.

Remaining work:

- Input-type detection now classifies retained nominal identities in `Pada`
  and `Reference` operands rather than their mutable rendered values; choice
  members still resolve as runtime data. Full typed morphological identity
  and legacy surface-alias removal remain unfinished. Conflicting number,
  boolean, choice, or explicit text
  declarations now fail before host dispatch instead of choosing a type by
  implementation priority; focused action and CLI verification follows each
  change.
- Extend member selectors compositionally beyond `अन्तिम`, including ordinal
  selection, without silently ignoring selector morphology.
- Complete prefix vowel sandhi with its lexical and grammatical exceptions.
- Finish the numeral-rendering audit: accusative neuter `त्रि + शस्` exposed
  an overly broad accusative-plural ṇatva guard. Its removal and regression
  tests passed the full ashtadhyayi and derivation suites and focused rendering
  tests; further numeral paradigms and generated examples still need auditing.
- Audit remaining collection frames for runtime/compiler agreement and
  migrate legacy examples only after their grammatical meanings are verified.
- Re-run a broad verification checkpoint after these further changes. Keep
  performance timings tied to an actual command and outcome: random prisoners
  trials can take different execution paths, so the earlier thirteen-second
  measurement is not a universal run-time guarantee.
