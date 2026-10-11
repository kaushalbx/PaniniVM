# Natural PVM language roadmap

Updated: 2026-10-11.

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

- Native distributive प्रति clauses iterate over a list snapshot in source order,
  with a scoped member referent, nested bodies, and nearest-loop break handling.
  They also compose as conditional branches without eager collection resolution.
- Procedures and native repetitions stop at first body failure; procedure break
  skips later sentences. Native loops consume their nearest break control effect
  without hiding the action value, including in result callbacks.
  Frequency normalization preserves local clause/event-chain scope rather than
  lifting a descendant's count over an enclosing branch or distributive loop.
  Explicit zero/one counts retain loop semantics; count conversion checks the
  current integer IR range rather than truncating a long value.
- Named typed operands retain referent provenance through semantic serialization
  and compiler planning; literal payload spelling does not establish identity.
  Signature shape checks preserve reference identity after typed resolution.
- Operation dispatch jointly checks ambiguous kāraka assignments against typed
  valency without inventing candidates or replacing established roles. Multiple
  valid assignments remain ambiguous; exhausted search reports an analysis limit.
  An ambiguous participant with no accepted role invalidates that operation
  instead of disappearing from an otherwise complete sentence.
- Nominal conditional results retain zero-copula nominal AST leaves rather than
  synthesized दा commands. Typed referents and literals flow directly to results.
  Explicit leaves require nominative morphology; numeral leaves retain numeric
  values independently of grammatical number. Full agreement remains unfinished.
  Low-cardinal number checks share the renderer's core rule; bare primitive
  cardinals infer nominative number, without imposing that rule on ordinals.

- Native whole-document parsing retains procedures, सीमा, अधिकार, control flow,
  source order, multiline text, and comments. Legacy parsing is explicit.
- Affixes, vikaraṇas, connectors, and semantic vocabulary have typed identities.
  Parser acceptance does not imply complete grammatical derivation.
- Procedure naming supports इति and नाम with nominative names and a finite
  प्रक्रिया declaration. Named arguments use locative slots. Argument values
  retain their types; compiled frame entry checks resolved parameter types.
  Primitive signature types and declaration markers require underived nouns;
  additional suffixes do not inherit the base's signature meaning. Derived
  result-schema identities survive declaration, lowering, and module metadata.
  Legacy type-guard inference requires a complete single भाव derivation rather
  than finding a भाव suffix inside an otherwise different derived predicate.
  It also checks the complete supported prohibition clause: one negation and
  two accusative nominals, without discarded actions, cases, or participants.
  Compiled legacy type guards accept unknown named operands only where typed
  frame entry enforces the required payload kind, rather than rejecting names
  as text; stored numeric/text regression cases cover acceptance and rejection.
  Signature construction rejects duplicate parameters, multiple result
  declarations, and conflicting legacy type guards rather than erasing contracts.
  Supported declarations consume all participants; a genuine present-tense अस्
  copula is accepted, but negation or an unrelated reporting action is not ignored.
  One-slot/one-result declarations require singular nominatives; dual/plural
  declaration forms are not silently reduced to a singular contract. Singular
  named slots also reject dual/plural slot references in both execution paths;
  this checks reference morphology, not the payload's size or collection type.
  A singular सूची names one collection and does not constrain its member count.
  A declared collection participant still consumes exactly one call argument;
  compiled calls do not manufacture a list from missing or excess operands.
  The list-operations and multifile समवाय examples construct their list explicitly
  and declare a single collection participant with numeric-member summation.
  Parameter assignments update call-owned slots shared by named and positional
  references, without replacing caller bindings. Nested calls and distributive
  members use nearest-binding shadowing. Typed references read current values
  within multi-action execution; literals retain their captured payloads.
  Compiled procedures use shared AST parameter rebinding and positional slot
  aliases. Legacy inferred signatures preserve unused lower positions and
  productive ordinals beyond the first three; inference has an explicit
  4096-slot compiler analysis budget, not a new Sanskrit grammatical restriction.
  Undeclared positional slots no longer default to numbers: their type remains
  unconstrained unless a type guard supplies it. Explicit declarations retain
  their constraints; general typed-valency inference remains unfinished.
  Pipeline validation carries declared type evidence separately from runtime
  arguments, including record results, instead of fabricating dummy payloads.
  Known pipeline operands are checked in every argument position. Type evidence
  uses a shared static classifier in validation and compiler lowering;
  an ordinary nominal reference is not presumed to contain text.
  Ordinary call validation likewise checks resolved argument payloads or literal
  AST evidence, preserving unknown reference types until runtime checking.
  Compiled parameter checks also retain alternate numeral AST evidence rather
  than reclassifying rendered operand names; Āryabhaṭīya numeric/type mismatch
  regression coverage agrees with interpreter rejection.
  Static ordinary-call overload selection also retains unknown references and
  rejects equally ranked candidates rather than selecting a text overload from
  noun spelling; interpreter dispatch still uses current resolved payloads.
  Unknown type evidence keeps compatible overloads open; equally ranked static candidates
  produce an ambiguity diagnostic rather than a registration-order choice.
  General symbolic inference remains unfinished. Equally ranked applicable
  runtime overloads retain an ambiguity diagnostic and fail before body effects;
  call validation and compiled invocation lowering check that diagnostic.
  Interpreted calls resolve named accusative operands from current scope/session
  values before overload ranking; noun spelling does not force text dispatch.
  Compiled dispatch for unresolved runtime argument types remains an audit item.
  Statically selected compiled calls retain the complete procedure definition
  as their method-target key, including domain/source/signature, rather than
  overwriting same-named overloads. Local precedence survives registry creation.
  Exported metadata format 2 also preserves precedence for separately compiled
  libraries; format 1 is rejected with a regeneration diagnostic rather than
  silently changing imported exception dispatch.
  Imported name-conflict checks retain अधिकार identity: separate domains can
  export the same procedure name and qualified calls reach the correct library.
  Separate libraries can also supply distinct parameter/precedence contracts
  under one domain/name. Duplicate imported contracts are rejected; unknown
  argument types retain call-site ambiguity instead of selecting a library by order.
  Schema linking rejects conflicting same-named field contracts across imports
  and local declarations instead of replacing one definition during map merging;
  identical shared schema definitions are accepted. Field-level types remain pending.
  Interpreter registry registration enforces the same no-replacement contract,
  retaining the original schema after rejection; script validation reports conflicts.
  Registry enumeration retains distinct definitions/signatures while removing
  duplicate aliases; inaccessible private definitions do not mask visible ones
  in compatibility name lookup. Signature-aware dispatch remains separate.
  Compiler entry-point and procedure-body lowering retain caller source identity
  for ordinary and pipeline visibility checks; same-file private calls are covered.
  Multi-file coverage also checks a public wrapper calling its own private helper,
  exclusion of that helper from exports, and rejection of direct external access.
  Typed dispatch checks applicability before precedence, so an incompatible
  exception cannot mask an applicable overload; compatible exceptions retain
  their precedence in ordinary calls and structured pipeline stages.
  Failed named-slot binding also makes a candidate inapplicable; it does not
  fall back to positional type matching and mask a correctly named overload.
  Signatureless procedures reject undeclared named slots rather than discarding
  their case-marked labels; validation and both execution paths share this check.
  Compatibility payloads attach to source arguments before named-slot ordering;
  guard checks prefer each resolved operand's own value over positional fallback.
  Compiled multi-procedure pipelines also select stages from declared result
  type evidence and retain ambiguity when that evidence cannot choose an overload.
  Legacy signatureless stages still accept ignored arguments; migrating those
  bodies to explicit participant contracts remains unfinished.
  The sequential तद्धित project example now declares its numeric participants
  and results; its addition/subtraction bodies consume supplied operands rather
  than hardcoded values, with changed-input interpreter/compiler parity coverage.
  Payload classification distinguishes ranges, truth, records, and absence from
  scalar numbers/text. Compiled number/collection contracts accept the existing
  rational and गण payload families as well as integer and सूची values; broader
  source-level type declaration grammar remains unfinished.
  IR verification retains numeric kinds for rational constants and truth kinds
  for truth constants, evenness, and membership predicates through typed frames.
  Extracted members retain an unknown static kind rather than an opaque scalar
  kind, allowing subsequent operations to validate the actual runtime payload.
  Numeric consumers reject text members with INVALID_VALUE instead of coercion
  or an internal state exception.
  Compatibility positional arithmetic retains cardinal literals and declines
  partial normalization when another participant requires ordinary binding.
  Its coordinated accusative form does not erase other cases, negation,
  nonfinite actions, or established member/history roles.
- अधिकार and सीमा take effect sequentially, including across procedure calls.
  Procedure-domain matching follows registered ancestor chains; registration
  rejects cyclic or conflicting parent edges without mutating existing relations.
  Module analysis validates all source/import edges before map merging, so duplicate
  child keys cannot hide conflicting parents; cross-library cycles are rejected too.
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
  Substantival ordinal extraction shares that frame and lowers to one-based
  indexing; lexical and productive pūraṇa selectors retain their grammatical role.
  Member/history roles survive same-named procedure parameters and prior-action
  clause projection.
  Positional-parameter inference and AST rebinding share role exclusions:
  member/history ordinals and known procedure-call identities are not arguments.
- क्त्वा/ल्यप् chains preserve operand ownership and shared agents. Objectless
  display can use the preceding result. ततः and mixed chains remain supported.
  Implicit display-object insertion identifies the finite head by AST identity,
  preserving subjects after the verb instead of assuming verb-final word order.
  Staged sequences normalize successful results without explicit typed payloads
  instead of forwarding a stale value from an earlier stage.
  A stage with no result clears implicit operand forwarding rather than injecting
  an earlier stage's payload into the next invocation; discourse lookup is separate.
  Turn conversion associates payload/truth with the final result's identity,
  not the insertion order of a separate typed map or an earlier truth value.
- Named action results and kāraka histories support latest, previous, and
  one-based ordinal selection. Typed completed-action snapshots separate results
  from participants and retain structured values. The explicit फल anaphor
  requires an underived noun; further derivations do not alias the preceding
  result or trigger implicit-pipeline rewrite advice.
  Unqualified फल procedure operands use the latest completed action's typed
  payload during interpreted overload selection; qualified named-action results
  retain their own history selectors. Missing action results produce an explicit
  diagnostic before procedure body effects instead of becoming literal text.
  Successful action turns and structured results update the same typed latest-result
  slot; unqualified procedure फल reads that slot before legacy action memory.
  Source-order regressions cover nominal results replacing actions and vice versa.
  Ordinary unqualified फल expressions prefer that same slot too, while explicit
  clause-chain overrides and preceding-clause IDs retain their local scope.
  General antecedent resolution and enclosing continuation ownership remain unfinished.
  Direct-assignment advice requires a matching immediately preceding action,
  an unqualified result, and a complete imperative assignment. It preserves
  target derivation instead of discarding history selectors or extra modifiers.
- Supported ordinal retrieval/history paths check case, number, partial gender,
  derivation, and wide numeric bounds. Derived nouns do not silently inherit their
  base's literal, ordinal, or selector meaning.
  Additional nominal derivation, including compound-member suffixes, is retained
  during named parameter rebinding rather than collapsed into an unmodified name.
  Ordinary value lookup likewise distinguishes derived forms from plain bases.
- Conditions use typed truth values; structural comparisons do not compare display
  strings. Underived सत्य/असत्य provide literal truth.
  Non-truth loop conditions are invalid, not successful false termination.
  Copular state/outcome shortcuts consume a complete simple predicate; extra
  participants, modifiers, prefixed/derived verbs, and repeated negation go
  through ordinary analysis rather than disappearing. Loop state lookup uses
  the validated semantic operation, not a single-noun spelling fallback.
  Conditional evaluation preserves failures/suspension/control effects rather
  than selecting a branch from an earlier successful truth in the same result list.
- Numeric prohibitions have shared typed parameter/literal operands and explicit
  compiler IR checks for runtime values. Unsupported procedure prohibitions are
  not partially recognized when they contain extra participants or repeated
  negation; the supported numeric form consumes its complete clause. They are
  rejected rather than silently ignored. Procedure-name affixes do not imply
  memoization: calls execute normally, including effects and result validation.
  Both backends require declared result kinds to be produced during the call;
  an inherited discourse result does not satisfy the contract. Nested calls
  may supply a fresh result, including one equal to the caller's prior value.
  Compiled normal returns check declared schema identity and exact field sets
  through explicit IR, including schemas imported from module metadata.
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
  The new ordinal example exposes unresolved उद् + हृत्य → उद्धृत्य rendering;
  explicit ordinal-affix eligibility also needs to reject blocked डट् after द्वि.
- Integrate comparative वति grammar/derivation and natural compound source
  syntax. Remove symbolic compound grouping only with a grammatical replacement;
  compound-type selection must not always assume tatpuruṣa.
- Resolve shared भ्याम्/भ्यस्/ओस् cases from context without erasing ambiguity.

### 2. Compositional semantics and discourse

- Current priority is semantic analysis; sandhi work is deferred.
- Record field declarations preserve written order and reject repeated field
  identities instead of silently deduplicating the declared contract.
  They require the underived present plural copula and reject unconsumed
  participants rather than ignoring their grammatical relations.
  Positive field assertions likewise require the present copula and consume the
  whole supported clause; negation and extra participants cannot become ignored
  text. Negative/tense-sensitive record facts still need explicit semantics.
  Field assertions update the named record without replacing the preceding
  action's फल in either backend; explicit field retrieval publishes a new result.
  Owner lookup removes only an outer possessive affix and retains preceding
  derivation; field names retain their derivational identity in declarations,
  assertions, and retrieval rather than aliasing the base stem.
  Asserted nominal field values also preserve reference identity, so nested
  traversal does not redirect a derived nominal to the base record.
  Nested genitive retrieval now lowers through explicit field-load and
  record-reference IR operations, including longer chains. Missing intermediate
  references fail; an absent final field yields लोप in both backends.
  The same invocation path handles retrieval inside procedure bodies, branches,
  and repetition. Interpreter calls retain a call-owned snapshot of visible
  records across body sentences instead of clearing it for every sentence.
  Retrieval recognizes a complete contiguous genitive chain and accusative field
  under imperative ग्रह्; negation, unrelated participants, and unsupported verb
  morphology are rejected rather than dropped during chain extraction.
  Embedded genitive fields project directly from AST into case-preserving typed
  operands for ordinary actions and procedure arguments in both backends.
  Instrumental procedure names retain their method-call role rather than being
  mistaken for field keys. Compiler overload selection for dynamically unknown
  field types still follows the existing ambiguity limitation.
  Multiple field operands and nested operand chains have backend parity tests;
  missing numeric fields fail instead of being coerced. The nested-record project
  demonstrates direct arithmetic on a genitive field phrase.
  Projection preserves external negation and the field's case; disconnected
  phrases reject explicitly rather than falling back to literal operands.
  Field comparisons resolve before branch/loop tests in compiler IR. Interpreter
  field conditionals use normal AST branch execution, not the legacy whole-clause
  evaluator; only the reached condition resolves its field operands.
  Bounded-loop parity covers zero/two iterations and missing intermediate
  references; retrieval, operands, and conditions share one field-chain lowerer.
  Copular truth-field predicates resolve the selected field (not a same-named
  outer variable), preserve negation, and require सत्य/असत्य. Branch and bounded
  loop tests cover true/false fields and numeric-field rejection.
  Imported metadata validates field identities and repeated schema/parent
  declarations before map construction, preventing silent contract replacement.
- Action-history references are distinct from general pronouns. Shared resolvers
  discover standalone/coordinated फल and kāraka heads in source order, preserving
  node identity, modifiers, and parameter-binding protection. Named results use
  explicit per-occurrence IR operands for single/multiple references; legacy
  LastResult substitution has been removed. Operand slots use scoped binding
  frames, avoid names already present in the phrase, and do not enter snapshots.
  Assignment parity verifies temporary frames preserve owning parameter cells
  and same-named caller values.
  Parity covers display, arithmetic,
  procedure argument values/order, ततः, conditional laziness, and bounded/unbounded
  loop reloads. Invalid history arguments fail before procedure body effects;
  case/number disagreement is rejected. Missing reached antecedents fail; unselected conditions do not
  load history. Latest results change after actions; chronological ordinals stay
  fixed. General kāraka-history operands beyond display still need IR support.
- Add general सर्वनाम antecedent resolution using grammatical agreement,
  kāraka role, and scope; retain ambiguity rather than choosing the latest value.
  Explicit/implicit coordinated AST operands share projection and preserve case
  and declaration protection. Repeated च source tests cover all seven cases.
  General final-च attachment remains unresolved: blanket parser precedence
  incorrectly groups modifiers and genitive relations; grammatical analysis is needed.
  Implemented local subset: underived तद् resolves declared procedure participants
  and foreach members through shared agreement analysis. Cells retain identity,
  declaration AST, scope, सुप् number candidates, and established lexical gender
  alternatives. Named/positional aliases share a cell; equal payloads do not.
  Unknown evidence never supplies agreement or removes a competing candidate.
  Missing, ambiguous, and incomplete antecedents are errors, not literal text.
  AST projection preserves case; runtime aliases and compiled slot references
  preserve live identity through assignment. Pronoun domains stop at procedure
  boundaries but include lexical foreach scopes, not dynamic caller participants.
  Verified interpreter/compiler paths: ordinary and coordinated operands, named
  procedure arguments, assignments, conditional/loop predicates, implicit branch
  results, foreach members/wholes, ordinal/final-member extraction, and prior-action
  chains. Pronoun-bearing sequences execute through stage binding rather than
  bypassing reference resolution in whole-sequence evaluation. Tests
  verify live pronoun reassignment across क्त्वा/ततः; explicit display objects
  suppress duplicate preceding-result injection in stage and whole-sequence paths.
  Display-object detection covers ordinary, coordinated, numeral, and ordinal AST
  operands by accusative morphology; non-display result pipelines are unaffected.
  Tests also
  cover temporary-name isolation, nested calls, and singular-list ambiguity.
  The guide and CLI-verified `examples/control_flow/local_pronouns.pvm` document
  this subset; readable text is generated by the CLI.
  Remaining: stable grammatical discourse records for other declarations
  (`SambhashanaContext` currently stores payload/history only), broader सर्वनाम
  senses, and general attachment. Preserve declaration-order visibility and
  identity across new execution/IR paths; do not infer number from collection
  cardinality, guess unknown gender, or reinterpret derived nouns as pronouns.
  Direct single-invocation AST evaluation also binds local pronouns before
  execution/publication; assignment and missing-antecedent tests cover this API.
  Raw-string evaluation parses once and delegates to that AST entry point, with
  matching local-reference and invalid-input tests. Direct linear sequence ASTs
  share one alias per antecedent identity, so later stages observe assignments
  before turn publication. Mixed named/pronoun references to a selected identity
  use that same turn-local key, preserving writes in either direction rather than
  reading stale environment values. Protected grammatical labels stay unchanged.
  Direct AST evaluation of conditionals, loops, foreach, and sequences containing
  them uses script control-flow execution without reparsing. Conditional tests
  cover lazy unselected pronouns and selected-branch local assignment. Direct
  loop/foreach API tests verify live predicate mutation, pre-member whole lookup,
  and member-scope isolation. Explicit scope ASTs use the same execution boundary;
  scope/body traversal stops on failure, suspension, or control signals instead of
  evaluating later statements. Top-level executable sentences stop on failure or
  suspension; legacy procedure-break unwinding still permits top-level continuation
  and needs separation from proper return semantics. Condition-mode/injected-binding entry paths and complete enclosing
  continuation ownership still need audit.
  Session-owned in-memory suspensions retain lexical cell/alias views across
  repeated approval or acceptance pauses, not capabilities; resumed publication
  updates the original pronoun antecedent. Enclosing procedure continuation and
  persistent continuation ownership remain separate unfinished work.
  Suspension ownership and replay tracking use weak token identity, not structural
  equality; equal continuation values cannot share cells or consume one another.
  Session-backed lifecycle coverage verifies local pronoun publication preserves
  caller values, hides internal aliases, and rejects replay without another write.
  Captured lexical frames retain their originating session key; cross-session
  resume is rejected before token consumption, preserving the legitimate retry.
- Project library identities use deterministic project-relative paths rather
  than basenames; nested equal-named files retain separate internal visibility.
  Loader/visibility tests cover equal-named internal procedures in separate files.
  Unreadable libraries fail loading with source-path context instead of silently
  disappearing from the declaration registry.
  Parsed-library cache reuse requires identical source text; same-size edits
  with unchanged timestamps cannot preserve obsolete procedure bodies.
- Parameter identities are unique in source declarations, shared signatures,
  call-owned slots, and imported metadata; duplicate names cannot merge positional
  slots or silently overwrite named values.
  Declared names and named/positional operand references retain derivation, so a
  base parameter and a suffixed parameter remain distinct through body binding.
  Runtime argument terms, compiled pipeline loads, and guard lookup use the same
  reference identity; forwarded derived values cannot read the base binding.
- Give declared referents stable gender, number, and value types; validate later
  inflected references and naming agreement. Preserve quotation/reporting
  semantics rather than turning every इति statement into variable storage.
- Extend general modifier/head and whole/member attachment beyond specialized
  frames. Add further member types, quantified phrases such as
  सूच्याः सर्वाः सङ्ख्याः, and ordinal extraction.
- Complete grammatical participant declarations, local references, returns,
  nominal-result agreement, and early termination. Audit result publication and
  history across all structured and direct binding entry points.
  Define grammatical introduction and visibility of new procedure-local names;
  parameter slots alone do not make every assignment in a call local. Audit
  scoped bindings across suspension/resumption and failure boundaries.
  Action-level approval/acceptance continuations retain earlier assignment
  records. Resumed session turns supplied with their local scope update owned
  slots and preserve caller names through the same publication path as normal
  turns. Automatic enclosing procedure continuation ownership remains unfinished.
  Audit early control effects against result contracts and define field-level
  type constraints independently of schema identity and field membership.
- Define derived-predicate/object semantics and dedicated unsupported-derivation
  diagnostics; an unresolved derived subject must not alias its plain base.
- Replace finite projections for prior actions with dedicated nonfinite heads.
  Extend shared-agent analysis to passive clauses and generalize range-exclusion
  prior actions. Cover procedure calls, nested control flow, early failure, and
  host-budget exhaustion across mixed chains.
- Complete numeric/collection सम् + युज् overload dispatch from typed operands.
  Collection joining now requires actual collection payloads in both roles;
  inherited numeric/group saṃjñās alone do not establish payload type.
  Justify empty-collection and removal constructions independently.
- Complete general kāraka-history operand IR beyond display; named-result
  arithmetic, multiple references, conditions, and procedure arguments are covered
  above. Audit history publication by specialized operations independently.
  Verified assignment boundary: remembered multiple participants assigned with
  दा become a Suchi in the interpreter. A scalar Reference to a packed Gana is
  not equivalent. Compiler coverage retains explicit rejection until dynamic
  participant expansion preserves operation valency and assignment packing.
  Foundation implemented: PackOperands IR expands explicitly marked grammatical
  group wrappers one level, keeps ordinary/nested collections intact, and packs
  one value as scalar or several as Suchi. Verifier and emitted-JVM tests cover
  order, nested payloads, invalid wrappers, and empty groups. Latest/ordinal
  history loads now offer an explicit group wrapper even for a single collection
  or Gana-valued participant; emitted-JVM tests verify one-level packing preserves
  that payload. Still wire expanded action-frame recording and assignment lowering.
  Interpreter history now captures detached participant payloads before each
  action and publishes them only on success, matching compiled snapshots after
  reassignment. They survive repeated approval suspension and V3 memory
  persistence/session restart. V2 memories remain readable via legacy frame
  reconstruction; their missing historical payloads cannot be recovered.
  Loading rejects duplicate action identities/participant roles and invalid
  collection counts rather than silently replacing history or overallocating.
  Tests cover named-value parity and nested mutable-payload detachment.
  Binding validates host/injected structured values by identity before caches;
  cyclic graphs return INVALID_VALUE, while shared acyclic subgraphs remain valid.
  Execution also rejects cyclic participant snapshots before action effects.
- Add compiler type-aware overload dispatch: module symbols, JVM targets, and
  linker metadata must distinguish signatures, not merely change call-site ranking.
- Audit nonnumeric prohibitions and legacy argument-type guard inference.
  Preserve value-based checking through nested calls and all call forms.
  Replace legacy ordinal-derived numeric parameter guesses with valency-based
  type inference or explicit declarations; ordinal position alone does not
  establish a runtime value type.
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
- Legacy यु evenness dispatch accepts exactly one integer independently of
  literal/reference representation. Its canonical Sanskrit predicate remains
  to be justified; this compatibility operation is not a grammar model.
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
