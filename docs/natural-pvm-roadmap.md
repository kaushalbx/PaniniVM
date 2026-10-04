# Natural PVM language roadmap

PVM source is morphologically segmented Sanskrit. Computational meaning must be
derived from ordinary morphology, kāraka relations, verbal valency, and discourse
context. A spelling must not acquire an unrelated programming meaning merely
because a compiler recognizes its source text.

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
