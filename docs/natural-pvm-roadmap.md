# Natural PVM language roadmap

Updated: 2026-10-12.

The division library's derived procedure name and corresponding fixtures now
retain `वि + भज् + णिच् + ल्युट्`, rather than treating surface `विभाज्` as a
root. Its executable body likewise uses segmented `वि + भज् + णिच्`.
End-to-end parity covers a derived-name call with a nonzero divisor and the
same procedure's zero-divisor prohibition. Readable library text is CLI-generated.
Derivational reference: [विभाजन dictionary entry](https://library.pustak.org/index.php/dictionary/word_meaning/%E0%A4%B5%E0%A4%BF%E0%A4%AD%E0%A4%BE%E0%A4%9C%E0%A4%A8).

Implicit `समवाय` argument gathering is removed from binder, compiler signature
inference, normalization, and emission. It is an ordinary user-chosen name.
Collection procedures declare one list parameter and use the genitive-whole /
accusative-members construction (`मान + ङस् सङ्ख्या + शस् …`). Legacy aggregate
tests are migrated to that construction; parity tests vary the parameter name
to ensure that collection meaning comes from the relation, not the spelling.

Declared parameter names take precedence over lexical ordinal positions:
`प्रथम` may name a slot declared second. Interpreter/compiler parity now tests
this explicitly. Compiler positional arithmetic no longer bypasses shared AST
parameter rebinding; ordinal spelling alone must not override a declared name.
The old global ordinal-arithmetic normalizer/compiler shortcut is removed.
Arithmetic follows the same resolved dhātu operations after parameter binding.
The multi-file division example now supplies segmented `वि + भज्`. The competing
fraction/proportion overload had the identical signature and is no longer
registered for that same sentence; a distinct grammatical proportion design
remains pending. Its host action implementation is retained, not exposed as
an indistinguishable source operation. Readable library text was regenerated
through the CLI.

Purpose-link analysis now requires a parsed nonfinite `KrtAffix.TUMUN` identity,
not a substring in clause text. The native lexer and editor completion use
the catalog's canonical `तुमुँन्` notation. Prior-action links likewise require
typed क्त्वा/ल्यप् derivations, including execution's lowered prior-action node.
These semantic links follow structural sequence edges rather than flattened
frame adjacency, so alternative branches are not linked as consecutive actions.
Shared-agent links compare full nominal morphology and grammatical number,
preserve coordinated members, and do not silently merge distinct explicit
agents. Utterance diagnostics expose structural conflicts; this is not yet
general pronoun/coreference resolution. Purpose morphology alone no longer
supplies an omitted shared agent. Diagnostic enforcement across consumers
and general discourse attachment remain unfinished.
Resolved operations can declare typed control signals. The vi-sthā operation
declares `BREAK_LOOP`, and compiler break lowering reads that declaration rather
than matching the display name `विजयः`. Source-level procedure return syntax is
still not implemented: return propagation/IR tests prove internal machinery,
not the existence of a grammatical return-producing source operation.
Runtime successful results must match the operation's declared control signal;
missing, different, and undeclared signals fail before result/history bindings
are recorded. Primitive compiler optimization cannot erase declared control
effects. This validates operation contracts, not arbitrary external side effects
already performed inside a faulty action.
Conditional frame traversal retains all branch exits, including the false-path
bypass of an if without else, when linking the following sentence. Nested
branches no longer collapse to the last textual alternative. This is a
structural discourse graph, not a replacement for compiler control-flow IR.
Grammatical traversal now includes while tests, independently of the legacy
binding projection that leaves tests to orchestration. Loop analysis retains
test/body/exhaustion/result frames and explicit loop-back and completion links,
without shifting following frame identities. Exit sets conservatively include
body termination; precise break/return reachability and zero-iteration modeling
for every loop form still need consolidation.
Negation in nested loop/conditional predicates is no longer classified as a
procedure-wide prohibition; declaration-level guards require a standalone
invocation. Native and compatibility parsing share that scope-sensitive check.

Grammatical typing: native sup nodes expose all `SupAffix` candidates (including
syncretic endings), tiṅ nodes expose `TingAffix`, and root nodes expose ordered
sanādi identities without dropping unknown entries. Procedure headers,
distributive iteration, copular agreement, nominal declaration recognition,
kāraka parameter projection, and supported causative checks now use these typed views.
Source strings remain for compatibility and rendering; migration of other
grammatical consumers to typed identities is still pending.
The former two-entry `KrtPratyayaIdentity` catalog is a source-compatibility alias
of `KrtAffix`; kṛt AST views now share one identity. Lexical action-noun recognition
uses typed prefix/affix identities and does not erase sanādi derivation.
Result and participant-history references share action-nominal validation:
supported ल्युट्/घञ् forms retain history semantics, while other kṛt meanings and
unsupported further derivations cannot alias the root's completed-action history.
This does not yet distinguish every prefixed/sanādi action in history queries.
Canonical क्तवतुँ is required in affix notation by the lexer, typed catalog, and
editor completion. Unmarked क्तवतु is not an alternative affix upadeśa.
Compatibility procedure qualifiers now enforce recognized nominative singular
morphology like native headers. Unsupported derivations or extra nominal
modifiers cannot silently enable precedence/visibility flags or be discarded.
Native and compatibility headers share one typed AST qualifier classifier rather
than maintaining separate lexical/affix recognition policies.
Migrated execution consumers have an architecture regression guard against
decoding sup/tiṅ strings again. Leaf-planner symbolic restoration retains source
sup candidates until the resolved binding supplies its kāraka, then selects only a
unique compatible slot. Case classification checks every candidate. Readable
rendering still contains first-candidate selection and needs a separate audit.
Copular operation signatures constrain recognized lexical predicates, so a
syncretic standard cannot make न्यून/अधिक select equality or सम select ordering.
Regression tests check the selected instrumental/ablative slot and live-value
interpreter/compiler parity. Unrecognized predicates still reach existing invalid
predicate diagnostics; this is not general nominal-predicate interpretation.
Runtime ambiguous bindings retain contextual sup-slot evidence through semantic
serialization. Role assignment specializes typed operands to the selected slot,
matching compiler restoration instead of leaving a provisional first-slot marker.
Record-field access retains the source sup node and all candidate slots instead
of selecting the first interpretation of a shared ending; field declarations
and retrieval checks consume typed case and tiṅ identities.
Kāraka inference accepts typed slot collections directly, preserving any contextual
narrowing instead of decoding their shared spelling again. Binding extraction uses
that API; collection-specific singular genitive requirements remain explicit.
Pronoun and antecedent number evidence uses typed sup candidates too. Projection
preserves the original sup node, including shared case slots; unknown endings
cannot establish number agreement or select an antecedent on their own.
Sup-bearing nominal and numeric AST forms share a typed `SupBearingPada`
interface. Guard, memory-qualifier, and record case analysis consume this common
morphology, avoiding encoding-specific lists that omit otherwise equivalent padas.
Sup slot compatibility compares case and number jointly and treats unknown endings
as missing evidence. History-qualifier regressions cover encoded numeric targets,
with no payload-based inference of grammatical number.
Supported feminine suffixes now have `StriAffix` identities. Gender evidence,
ordinal/numeral normalization, ordering qualifiers, procedure qualifiers, and
action-history references require recognized feminine affixes rather than merely
a `StriVikara` wrapper. This does not validate every stem/affix combination;
unknown derivations cannot prove feminine agreement or unchanged ordinal meaning.
Taddhita nodes expose individual affix identities for the supported possessive,
abstract, comparative, and superlative suffixes; semantic families resolve via
those identities. Initial-vṛddhi support keeps an explicit separate domain, so
recognition does not silently authorize new derivation behavior. Legacy मत्
notation is a compatibility alias, not an additional original affix.
Finite, nominal-kṛt, and nonfinite-kṛt nodes now expose ordered `Upasarga`
identities without dropping unclassified entries; collection extraction uses
typed उद् checks. This is source-identity typing, not sandhi normalization or
complete prefix-combination semantics. The grammar's अन्तर् prefix needs a
separate contextual gati analysis rather than automatic prādi classification
(see [1.4.58–65](https://ashtadhyayi.github.io/suutra/1.4/)).
Operation definitions accept typed upasarga/sanādi constraints; recognized
markers match by identity and contradictory canonical constraints are rejected.
All executable dhātu prefix/sanādi trigger definitions now use the typed API.
Unreachable तुल्-as-prefix comparison and स-as-prefix addition dispatch paths,
plus the nonsegmented सम constraint, were removed; a catalog test rejects such
markers in executable definitions. String storage remains a compatibility/
serialization boundary. Host-created unclassified constraints retain exact
matching, but are not admitted as grammatical upasargas.
Procedure argument validation now uses typed case evidence and rejects unsupported
operand cases before positional-call shortcuts can discard them. Accusative
operands and singular locative/genitive labels retain their existing semantics;
other participant roles require a future grammatical procedure-call design.
One written parameter slot and one accusative operand now attach uniquely in
either order, including across the procedure noun/verb. Multi-slot attachment
still depends on explicit local pairing; no general free-order mapping is claimed.
Field-operand projection preserves record method phrases without stopping at
them, so later field-valued arguments are resolved too. A full source regression
checks interpreter/compiler parity for a method followed by its field argument.
Procedure matching rejects dual/plural procedure and domain references rather
than collapsing them to a singular registry entry. Invocation currently supports
one instrumental singular procedure and one optional genitive singular domain;
multi-procedure/domain semantics remain unsupported, not grammatically invalid.
Native procedure declarations enforce the same singular name/domain contract for
both नाम and इति headers, rather than registering plural references as one symbol.
The compatibility header-identity extractor follows this contract too and no
longer chooses the nearest of competing domains or discards an extra noun.
Procedure matching rejects competing known instrumental names and retains
operands before or after the procedure noun/finite verb. Interpreter/compiler
tests cover reordered calls. A unique unassigned genitive domain can occur before
or after the procedure noun/verb; competing domains are rejected instead of
selected by proximity. Distinguishing legacy genitive parameter labels from
domains still uses adjacency and needs a broader grammatical relation model.
An instrumental name alone no longer dispatches a procedure: the invocation
head must be canonical कृ without unsupported prefixes or sanādi derivation.
Ordinary printing/arithmetic verbs retain their own semantics even when an
instrumental noun happens to name a registered procedure.
Declaration analysis now constrains sup candidates by the required case and
number instead of selecting the first dictionary slot. This preserves competing
interpretations; it is not yet a general sentence-wide ambiguity resolver.
Scope operands with a resolved kāraka now retain the compatible sup slot rather
than the first syncretic entry (for example recipient भ्याम् uses the dative
dual). Unknown endings no longer default to accusative singular. Unresolved
ambiguity is still carried by binding diagnostics, not by a multi-slot executable
operand representation.
Tiṅanta derivation requests validate supported sanādi identities and duplicates
after canonicalization, and grammatical analysis rejects unknown sanādi entries
before considering derived-stem pada compatibility. Known-affix recognition is
not yet full validation of affix ordering or all derived-stem pada rules.
Nonfinite kṛt nodes expose `KrtAffix`; prior-action and range-exclusion analysis
consume that identity directly. Shared-agent identity compares typed sup
candidates alongside nominal morphology. General nonfinite attachment and
controller resolution beyond the supported constructions remain unfinished.
Collection-member morphology now uses typed sup slots throughout. A genitive
whole named अन्तिम is excluded from selector protection and validation; ordinal
and final-member extraction work in either word order without reserving that
spelling as a collection-only or selector-only name. Interpreter/compiler parity
tests cover both selectors against that same-named whole.
Ordinal-object retrieval similarly excludes genitive/ablative whole referents
from modifier counting: a collection named प्रथम is not another ordinal
modifier. Source-case and modifier agreement checks, plus list-declaration
name/type/copula checks, now consume typed affix identities. Parity tests cover
reordered ordinal-object phrases and ablative source notation.
Direct-result assignment rewrite suggestions now locate फल, its action modifier,
and destination by parsed relations and typed case evidence, not their relative
positions. Reordered clauses have execution-preservation and compiler parity
tests; ordering-qualified action-history references remain deliberately excluded.
One फल with competing genitive action modifiers is now rejected as ambiguous,
not attached to the nearest action noun. Rewrite suggestions decline that input;
interpreter and compiler rejection tests cover the supported ambiguity check.
Attachment of several result nouns to several modifiers still needs a general
relation model rather than positional fallback.
Further-derived action nouns (for example मतुप्/तरप् derivatives) no longer
borrow their base action's result history; unsupported reference semantics are
rejected. Gender-only affixes retain the existing action-reference treatment.
This does not establish full kṛt sense resolution or derived-action identity.

## Goal

PVM source is morphologically segmented Sanskrit. Computational meaning must
derive from morphology, kāraka relations, verbal valency, and discourse context,
not arbitrary spelling conventions or symbolic constructors.

An explicit goal is to make semantic analysis solid for the supported language:
resolve meaning through a shared semantic model, reject ambiguity or insufficient
evidence, and preserve identical meaning across interpreter, compiler, and
suspension/resumption. This does not claim complete Sanskrit understanding.

Semantic hardening is complete only when:

- Reference identity, grammatical agreement, declaration visibility, and lexical
  scope are consistent across all public evaluation paths, including discourse
  references beyond procedure participants and foreach members.
- Modifier/head attachment, coordination (including final च), and whole/member
  relations have explicit semantic resolution and ambiguity diagnostics.
- Procedure returns are distinct from loop breaks; normal and early returns
  enforce result contracts, unwind the correct scope, and resume correctly after
  suspension without executing skipped statements or losing caller ownership.
- Named-result and kāraka-participant history operands preserve action-time
  values, participant grouping, and operand order in every supported operation.
- Interpreter/compiler parity and negative tests cover nested control flow,
  recursion, mutation, ambiguous references, failures, and budget exhaustion.

Prioritize procedure return/control-effect separation and result-contract checks,
then enclosing continuation ownership, general references, and attachment.

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

- Result-producing procedure headers retain their product and predicate as native
  AST morphology: `परीक्षण + सुँ नाम प्रक्रिया + सुँ सङ्ख्या + अम् जन् + णिच् + लट् + तिप्`.
  Divādi `JanDhatu` (04.0044) owns the causative result-production meaning, not
  parser spelling checks. Shared signatures enforce header result contracts and
  reject duplicate body declarations. Existing copular headers remain supported.
  The signature boundary also rejects incomplete result/predicate pairs and
  non-accusative or non-singular products supplied directly as native AST nodes.
  JanDhatu validates an explicit causative vikaraṇa as शप्; base-root श्यन्
  and other incompatible affixes cannot silently inherit declaration semantics.
  This is a result declaration, not early-return source syntax.
  A procedure call in the first ततः stage now executes through procedure dispatch
  and supplies its typed result to the following action. Explicit display objects
  suppress piped injection; failed result contracts stop the rest of the chain.
  Interpreter/compiler regressions cover these cases.
  Remaining derivation work: causative pada selection and the मित् shortening
  required for जनयति. Until those rules are implemented, readable generation
  retains the segmented predicate rather than substituting an incorrect surface.

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
  Explicit शप् on a single णिच् stem in लट्/लोट् no longer incorrectly filters
  the original root to bhvādi: normal lexical ambiguity checks remain in force.
  Resolver regressions cover युज्/गण/मुद्र्; interpreter/JVM addition parity passes
  with the full execution/compiler suites. Runtime resolution and readable
  derivation selection now share lexicalGanaConstraint() over parsed morphology;
  rendering parity covers स्था/युज्/गण/मुद्र् with explicit and implicit शप्.
  Core boundary tests retain constraints for other affixes, tenses, and sanādi
  chains. A known contradiction check rejects explicit non-शप् affixes on
  single-णिच् लट्/लोट् parasmaipada forms before lexical lookup; readable
  generation retains their raw source. This does not validate passive forms,
  other tenses, or general sanādi/vikaraṇa compatibility.
- Procedure naming supports इति and नाम with nominative names and a finite
  प्रक्रिया declaration. Named arguments use locative slots. Argument values
  retain their types; compiled frame entry checks resolved parameter types.
  Procedure subjects and qualifiers must be nominative singular with the
  singular declaration predicate. Qualifier identity permits feminine suffixes
  but does not erase additional taddhita derivation such as मतुप्.
  Static argument analysis no longer treats contextual lexical ordinal names
  as proven numeric literals. Explicit सूची parameters accept a collection bound
  to प्रथम in both interpreter and JVM regressions. Runtime fallback meaning
  remains numeric when no named value is bound; general dynamic overload dispatch
  remains a separate incomplete task.
  Procedure-call IR now carries parsed numeric fallbacks for ordinal arguments,
  including feminine प्रथम + टाप्; runtime lookup still takes precedence.
  Bound/unbound plain and feminine ordinal parity regressions pass. Explicit IR
  fallbacks precede legacy runtime spelling-based numeral evaluation.
  Procedure argument binding now recognizes cardinal/truth literals before host
  lookup, matching ordinary actions; compiled calls emit those literals directly.
  A host binding named एक cannot replace the argument's numeric meaning.
  Known scalar/list mismatches fail during IR verification, while unconstrained
  frames accept known value kinds. Execution/compiler suites cover this boundary.
  Static procedure argument classification shares the literal classifier with
  call binding: underived सत्य/असत्य provide truth evidence, while derived
  truth-base nominals remain unconstrained. Opposite-valued host bindings cannot
  replace written truth arguments in interpreter/JVM regressions.
  Native call-frame construction also applies literal priority before environment
  lookup when no argument value was supplied; it no longer depends on registry
  preprocessing to protect cardinal/truth literals. Direct-frame regressions
  cover एक, सत्य, and असत्य against same-named host text values.
  Synthesized runtime dispatch now uses the same truth-literal evidence rather
  than classifying every nonnumeric AST operand as text. Native overload tests
  select truth for सत्य/असत्य and text for derived सत्य + मतुप्; this does not
  add source declarations for additional primitive parameter types.
  NominalValueAnalysis now owns shared AST literal evidence and numeric fallback
  independently of procedures. Ordinary action binding, procedure classification,
  call frames, and copular planning consume it; selector precedence and contextual
  ordinal lookup remain separate from intrinsic literal evidence.
  Copular cardinal regressions cover nominative subjects and ablative/instrumental
  standards: same-named host values cannot override written numerals in ordering
  or equality. Both interpreter and JVM paths pass these cases.
  Locative membership planning preserves a derived whole's referenceKey rather
  than collapsing it to semanticKey. A regression reuses one generated class
  with opposite contents under base and derived names, proving live selection
  of the derived whole and preventing base-name aliasing.
  Explicit member-selector frames now take precedence over competing मतुप्
  record-field detection. Compiler planning retains genitive wholes as live
  collection references, including derived names. Both source word orders and
  changed host contents pass interpreter/JVM regressions with the full suites.
  Missing, scalar/text, and empty derived wholes are also rejected for ordinal
  extraction despite a valid base-name binding. Missing compiled loads now report
  typed INVALID_VALUE failures rather than leaking a raw IllegalStateException.
  Compiled nested-record reference lookup now respects procedure/binding frames
  rather than reading only globals. Runtime regressions cover nested shadowing,
  frame restoration, and a local non-record value blocking global fallback.
  Broader source-level local record-chain analysis remains to audit.
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
  TypedOperand compiler lowering now preserves explicit referenceName across
  arithmetic, collection/counting, display, assignment, and participant capture:
  current bindings override captured fallback values. Unreferenced text literals
  cannot become references merely because their spelling matches a bound name.
  Direct typed-plan interpreter/JVM parity tests cover live and missing bindings.
  Generic-call bytecode serialization also retains typed referenceName, explicit
  Pada annotations (including रूढि), and memberSelection; emitted tests cover
  live/fallback references, literal isolation, number-member sums, and agent history.
  Ordinary Pada literals also remain isolated from same-spelled runtime variables
  in generic calls and participant capture. Copular equality/order planning now
  represents subject/standard references explicitly with nominal fallbacks;
  condition, field, pronoun, history, and loop parity no longer depend on runtime
  spelling-based substitution. Unbound nominal comparison fallbacks are tested.
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
  Legacy successful procedure-break exits now enforce declared result type and
  schema contracts instead of bypassing validation; failure and suspension do
  not validate an unfinished result. A distinct procedure-return effect and its
  grammatical expression remain unfinished.
  Compiled source procedures branch to a shared contract epilogue on early break;
  schema checks no longer skip pending break state. A source-level regression
  verifies both backends reject a scalar break result for a declared record schema.
  Nested-call tests reject early-break numeric contract violations despite an
  existing caller result; nearest-loop consumption still permits subsequent
  procedure sentences to produce a valid typed result in both backends.
  A distinct internal RETURN_PROCEDURE effect and value-consuming return-request
  IR now exist. Procedures consume the effect after contract validation; loops
  preserve it instead of producing a completion result. Compiled return state
  belongs to the nearest procedure frame, excluding lexical binding frames, and
  disappears when the callee frame exits. Emitted-JVM tests verify value transfer,
  caller continuation, contract errors, and separation from loop break; IR
  verification tracks the returned LastResult type and rejects stack underflow.
  This is internal infrastructure, not yet a usable source-level early return:
  grammatical syntax, complete lowering through structured constructs, callback
  ownership, and suspension/resumption remain unfinished.
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
- Complete general kāraka-history operand IR beyond display, assignment, and numeric
  folds; named-result
  arithmetic, multiple references, conditions, and procedure arguments are covered
  above. Audit history publication by specialized operations independently.
  Remembered-participant assignment now has interpreter/compiler parity: multiple
  participants assigned with दा become a Suchi; a sole collection-valued participant
  remains that collection. A scalar Reference to a packed Gana is not equivalent.
  Addition/multiplication now use explicit numeric-fold IR with aligned group
  expansion flags; the new action records expanded inputs, not its arithmetic
  result. Interpreter/JVM tests cover mixed remembered and written operands.
  Dynamic folds carry a positive operand minimum checked after expansion;
  multiplication requires two numbers. Emitted-IR tests distinguish this contract
  from source गण constructions that can instead select counting operations.
  Fold operator and minimum now come from the resolved NumericFoldDhatuAction,
  not duplicated compiler operation-name rules. Subtraction/division parity tests
  preserve remembered/written operand order and reject unsupported negative
  results. General case-role arithmetic constructions remain a separate audit.
  Dynamic arithmetic computes integer intermediates and renders only its final
  result; host-value parity tests cover negative inputs whose final sum is valid.
  Overflow and zero divisors produce typed compiled execution failures.
  Bare accusative history groups now use CallParticipantGroup IR when a numeric
  or counting verb's meaning cannot be selected from a scalar placeholder.
  Canonical verb features survive binding; the shared OperationResolver selects
  against actual runtime participants before action execution, without reparsing
  source or interpreter re-entry. Parity tests cover addition, multiplication,
  scalar counting, collection-valued participant counting, and history publication.
  This boundary intentionally uses action dispatch, not direct primitive lowering.
  Mixed history operands also defer selection when the verb has nonnumeric
  alternatives. Explicit grammatical-group packing preserves nested collection
  payloads; parity tests verify गण selects counting for collection participants
  rather than numeric multiplication. Numeric signature requirements now check
  NUMBER payload eligibility in addition to inherited सङ्ख्या annotations.
  Dynamic calls retain other bound participants, including agents, in completed
  history. Emitted-JVM tests cover agent reference resolution and cold-start
  action dispatch; a shared numeric-renderer fallback recursion was corrected.
  Explicit dative destinations now follow the selected operation's result-binding
  contract. Bare/mixed/coordinated history groups share scope-aware result storage;
  tests cover local procedure parameters without caller overwrites, failed calls
  without writes, original recipient snapshots, and multiple groups in source order.
  Remaining: general kāraka roles and verbs outside the numeric/counting family
  need broader deferred planning.
  Ordinary named गण operands now share runtime selection too: the same generated
  class multiplies number payloads but counts collection payloads, and a single
  named scalar/list counts correctly. Statically numeric AST operands retain
  direct primitive lowering. Runtime-boundary reporting includes deferred calls
  explicitly (`dynamic:गण`) instead of misreporting them as primitive-only.
  `examples/arithmetic/count_demo.pvm` now forms a list before counting it, matching
  its stated exercise; its generated companion and compiler result are verified.
  Failure-boundary emitted-JVM tests verify zero divisors, text participants,
  and collection participants preserve prior LastResult, destination bindings,
  and source history, and publish no completed frame for the failed action.
  `examples/control_flow/remembered_operands.pvm` demonstrates mixed-history
  arithmetic followed by ततः display; CLI execution prints द्वादश and its
  companion is CLI-generated. The renderer's कर्मन् accusative currently surfaces
  incorrectly as कर्माणम्; source morphology/semantic binding remains distinct
  from that unresolved nominal derivation.
  Foundation implemented: PackOperands IR expands explicitly marked grammatical
  group wrappers one level, keeps ordinary/nested collections intact, and packs
  one value as scalar or several as Suchi. Verifier and emitted-JVM tests cover
  order, nested payloads, invalid wrappers, and empty groups. Latest/ordinal
  history loads now offer an explicit group wrapper even for a single collection
  or Gana-valued participant; emitted-JVM tests verify one-level packing preserves
  that payload. RecordActionFrame now accepts aligned explicit group-expansion
  flags; the backend expands one wrapper layer before atomically snapshotting
  participant history. Generated-JVM tests preserve nested Suchi/Gana values and
  source order; invalid wrappers/flags cannot append a partial history entry.
  Assignment lowering now connects these primitives through the shared grammatical
  binder and operation signature. Tests cover latest/chronological history,
  original participant recording, mixed written operand order, preservation of
  a single list-valued participant, missing histories, and temporary-name isolation.
  Further parity tests retain nested host Suchi/Gana payloads after reassignment,
  read the old destination during self-assignment, and keep list-parameter writes
  local while satisfying a typed header result contract and a following pipeline.
  Unselected branches do not load missing participant histories or create targets.
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

## Operand and selector identity

- Local referent agreement shares nominal gender analysis with modifier agreement:
  explicit feminine derivation supplies STRI evidence; other derived forms do not
  automatically inherit a base noun's lexical gender. Declaration/identity-selection
  regressions and execution/compiler suites pass. This remains partial gender
  analysis, not complete derivational agreement or general discourse resolution.
  A local-binding integration regression resolves तद् from an explicitly feminine
  derived declaration, updates it through the pronoun, then reads the same live
  identity again. Source-level procedure parity now covers explicit feminine
  parameter derivation and pronoun mutation in interpreter/JVM execution. Two
  compatible derived parameters remain ambiguous in both analysis paths rather
  than resolving by recency. Focused local-anaphor and compiler regressions pass.
  Feminine evidence is taken only from the final derivational layer, not any
  earlier feminine suffix. Native AST order regressions and execution/compiler
  suites pass; later unsupported derivations leave gender unknown.
- Copular न्यून/अधिक requires one number-valued कर्तृ and अपादान; inherited
  सङ्ख्या annotations do not make collections scalar numbers.
  Equality declares one subject, instrumental standard, and predicate; ordering
  also declares one predicate. Participant cardinality does not restrict structured
  payload size. Copular parity coverage includes whole lists and records containing
  equivalent integer/fraction members; dhātupāṭha/execution/compiler suites pass.
  Plain comparison predicates bind from grammatical identity, not same-named
  host variables. Further-derived predicate morphology is retained; मतुप्/तरप्
  forms fail in interpreted and generated execution rather than becoming the base
  comparison. Execution/compiler suites and predicate-shadowing regressions pass.
  All six orders of the subject/standard/predicate are verified for equality and
  less-than, using changing host operands (36 interpreter/JVM parity cases).
  Shared binding validates written predicate/finite-copula number agreement before
  discarding endings. Dual/plural predicates with singular verbs fail; this does
  not infer runtime collection shape from vacana. Execution/compiler suites pass.
  A single explicit Subanta subject in these comparison frames is also checked
  against the verb. Validator regressions accept matching dual/plural morphology;
  this does not claim plural comparison execution semantics. Established gender
  incompatibility between one explicit subject and its predicate is rejected via
  shared nominal analysis; unknown gender does not prove a mismatch. Positive,
  mismatch, and unknown-evidence regressions plus execution/compiler suites pass.
  Host-bound feminine comparison parity exposed and fixed loss of derived subject
  identity: copular planning now uses full morphological reference keys rather than
  base semantic keys. Changing host values changes the generated comparison;
  established gender mismatch is still rejected before lowering. An unbound
  feminine ordinal subject retains its numeric meaning rather than becoming text;
  shared planning preserves numeral/nominal fallback semantics independently of
  reference identity. Bound/unbound regression and execution/compiler suites pass.
  A separate interpreter/JVM regression verifies that a base-stem host binding
  cannot override the derived subject, with or without an exact derived binding.
  Literal meaning for additional nominal suffixes remains incomplete:
  SvamRupamEngine currently evaluates semanticKey(), which drops MulaPratipadika
  vikaras. Reference isolation does not constitute compositional interpretation
  of those suffixes; that requires derivational semantic analysis.
  Coordinated/implicit subjects, complete gender derivation, and general
  non-comparison agreement remain incomplete.
  Interpreter and JVM ordering share exact integer/rational comparison, including
  negative denominators and overflow-safe cross-products; zero denominators fail.
  Core helper and host-bound copular parity regressions pass with the full
  core/execution/compiler suites. This does not extend integer arithmetic folds
  to rational arithmetic.
  Genitive-whole सङ्ख्या-member summation now supports integer/rational mixtures
  through shared exact reduction, without flattening. Fraction intermediates use
  arbitrary precision; final payloads must fit Long numerator/denominator fields.
  Integral results use normal Sanskrit number rendering; fractional results retain
  the existing numeric fraction display convention. Core/execution/compiler suites
  and mixed-member parity tests pass. General rational arithmetic folds and natural
  fractional-word rendering remain unfinished.
  Composition parity verifies a fractional member sum flowing through ततः into
  a numeric procedure parameter/result and then into copular equality. Numeric
  fraction labels are not displayed twice; lexical labels such as अर्ध remain.
  Core/execution/compiler suites pass after the display adjustment.
  Valid integer/rational equality now shares numeric meaning (1 equals 2/2),
  recursively within structured values without conflating list/group types.
  Core equality and bidirectional copular interpreter/JVM parity regressions pass;
  legacy equality behavior for zero-denominator payloads is unchanged.
  NUMBER operation eligibility and numeric-list member constraints now reject
  zero-denominator fractions while accepting negative denominators. Ordering parity
  tests reject undefined host operands on either side; core/execution/compiler
  suites pass. Procedure parameter, guard, and result validation now distinguish
  payload classification from acceptance and share NUMBER eligibility. Contract
  helper regressions and execution/compiler suites pass. End-to-end interpreter/JVM
  tests now reject undefined host fractions at numeric parameter and result
  boundaries, while the same generated classes accept valid negative-denominator
  fractions. Binary persistence rejects negative member counts, duplicate record
  fields, and invalid typed numeric-list members; valid fractions, records, and
  ranges round-trip. Execution-suite and focused codec regressions pass. General
  persistence resource limits remain unaudited. Legacy rational tags now require
  exactly two numeric components, rejecting extra or missing components rather
  than discarding data. Generated fractional-sum payload/display round-trips are
  covered; the execution suite passes. Broader legacy-format validation is pending.
- Signatures can forbid kāraka roles. Runtime resolution and the operation catalog
  enforce this; invalid ordering cannot fall through to legacy membership.
- Natural membership requires one subject and one collection-valued locative
  whole. Shared planning preserves named subjects and uses collection placeholders
  only for the existing अस् locative frame. Parity coverage includes changing host
  values, structured members without flattening, empty wholes, and invalid wholes.
- Truth and cardinal numeral literals precede environment lookup; host bindings
  cannot shadow सत्य or एक/द्वि. Lexical ordinals retain contextual named lookup
  and an unbound numeral fallback, including stored प्रथमा/द्वितीया collections.
- Genitive-whole ordinal selectors take precedence over same-named host bindings
  in either word order. Further-derived मतुप्/तरप् ordinal selectors are explicitly
  rejected before action selection, including nonfinite extraction chains.

Execution/compiler suites passed after these semantic changes. The latest added
nonfinite-selector regressions also pass. The guide documents literal, name, and
selector roles. General role/type inference across overloaded constructions is
still unfinished; these construction-specific checks are not a general analyzer.

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
