# PVM source review

Review expectations are in `execution/src/test/resources/karaka_corpus_expectations.json`.
They are grammatical judgments, not snapshots copied from analyzer output.
The generated inventory is `execution/build/reports/karaka-corpus.json`.

Current inventory: 99 files, 457 written clauses, 470 grammatical frames,
96 reviewed clauses (97 frames) passing, 49 word-order variants passing,
361 clauses unreviewed, no parse failures.
No lexical-head blocking diagnostics remain in the current corpus. This is not
proof that all nominal relations are resolved. Declaration headers are outside
this clause count.

## Arithmetic programs checked

| Source | Intended result | Verification |
|---|---|---|
| `examples/arithmetic/scaling.pvm` | 5 doubled to 10 | Both clauses reviewed; interpreter value and printed output; compiled output |
| `examples/arithmetic/comparison.pvm` | 10 > 3 is true | Both clauses reviewed; interpreter truth value and printed output; compiled output |
| `examples/arithmetic/min.pvm` | Minimum is 3 | Both clauses reviewed; interpreter value and printed output; compiled output |
| `examples/arithmetic/mod_demo.pvm` | Remainder is 1 | Both clauses reviewed, including the named-result genitive; interpreter value and printed output; compiled output |
| `examples/arithmetic/subtraction.pvm` | Difference is 7 | Both clauses reviewed; interpreter value and printed output; compiled output |
| `examples/arithmetic/addition.pvm` | Sum is 3 | Both clauses reviewed; typed interpreter result and printed/compiled output |
| `examples/arithmetic/addition_phala_demo.pvm` | Named addition result is 3 | Both clauses reviewed; typed interpreter result and printed/compiled output |
| `examples/arithmetic/average_demo.pvm` | Average is 20 | Both clauses reviewed; coordinated objects and named-result relation; interpreter/compiler results |
| `examples/arithmetic/sqrt_demo.pvm` | Integer square root is 3 | Both clauses reviewed; object and named-result relation; interpreter/compiler results |
| `examples/arithmetic/power_demo.pvm` | Power is 81 | All five clauses reviewed, including initialization, storage and printing; interpreter/compiler results |
| `examples/arithmetic/geometric_progression.pvm` | Final term is 54 | All five clauses reviewed, including both state initializations; interpreter/compiler results |
| `examples/arithmetic/factorial.pvm` | Factorial of 5 is 120 | All seven clauses reviewed; interpreter output state and compiled output |
| `examples/arithmetic/sum_iterative.pvm` | Sum from 5 down to 1 is 15 | All seven clauses reviewed; interpreter output state and compiled output |
| `examples/arithmetic/cumulative_sum.pvm` | Sum of five explicit numerals is 15 | Both clauses reviewed; typed interpreter result and compiled output |
| `examples/arithmetic/gcd.pvm` | One remainder step gives 6 | Both clauses reviewed; typed interpreter result and compiled output; not a complete general GCD algorithm |
| `examples/control_flow/conditional.pvm` | Select 7 or 0, then print it | Both branches and printing reviewed; true/false execution and compiler parity; condition's full grammatical review remains open |

These checks use the VM's declared operations. They verify nominal grammatical
relations and program results, not unrestricted interpretation of classical
Sanskrit or every inflectional/derivational rule.

`min_subset_sum_diff.pvm` has all four clauses reviewed, including the two
intended result receivers under 1.4.32→2.3.13 and their coordinated object
mediators. The corresponding four clauses in `double_danda_problems.pvm` are
reviewed separately. Each has an AST and reparsed word-order variant. These
judgments use the program's declared intended meaning; arbitrary dative action
nouns are not thereby classified as result receivers. Negative tests remove
the objects from the same receiver forms and require absence of 1.4.32 evidence.

Actual-file execution checks the subset example's result one and all five suite
outputs (one, twenty-three, two, twenty-seven, five). Compiler tests check the
final outputs. These are worked calculations on fixed subsets, not a general
minimum-partition search implementation.

The bounded-loop sum source is named `sum_iterative.pvm`; its former recursive
name and description were misleading because it contains no self-call. The
source, readable companion, architecture test and execution parity references
were migrated together, with no old-path alias. The GCD-step source now uses
accusative-plural `शस्` for the cardinals eighteen and twelve.
The compound-cardinal rendering gap is fixed: declension now retains underlying
n-final teen stems, so existing 1.1.24 and 7.1.22 apply. The regenerated GCD line
reads `अष्टादश द्वादश च शेषय ।`. Tests check all nine teen cardinals in all
three genders, nominative/accusative plural and canonical derivation evidence,
plus instrumental, genitive and locative forms of twelve. The two subset-sum
sources also now give eleven its accusative-plural ending.

Above nineteen, declension follows the supplied expression's head rather than a
numeric-range shortcut. PVM rendering checks cover twenty, twenty-three,
thirty-five, one hundred, two hundred and one thousand. Existing tests retain
the feminine twenty head in an alternative nineteen expression (`एकोनविंशति`),
rather than replacing it with a same-value n-final numeral. The whole numeral
module is included in the validation gate for this change.

Stateful numerical regressions inspect the state actually named by the final
printing clause. In factorial, the final loop-counter update yields six while
the product state contains 120; checking only the last numeric action result
would inspect the wrong value. The print result must match the intended state.

`शस्` on these numerals is the accusative-plural **sup** ending. It must not be
mistaken for homographic distributive taddhita `शस्` under 5.4.43. The original
PVM numeral forms are retained.

The remainder example exposed an analyzer gap: the action noun's genitive in
`शेषस्य फलम्` was left unassigned. Written action-result evidence now supports
the nominal relation under 2.3.50. The inference requires one result head,
one unambiguous genitive action noun and no competing selected governor.
Multiple heads/genitives and syncretic endings do not receive a guessed link.
It uses the written clause and no runtime memory or supplied semantic premise.

The former `conditional.pvm` contained only subtraction despite its heading.
That arithmetic program now lives in `arithmetic/subtraction.pvm`; the compiler's
existing arithmetic boundary check follows that path. The conditional source
now has an actual if/else and prints the selected result. A false-condition
variant is tested as well as the default true branch. The dedicated branch
regression checks interpreter/compiler parity; it does not claim the conditional
has no runtime action boundary.

## Loop and control-flow programs checked

Actual-file regressions cover `print_1_to_10.pvm`, `punah.pvm`, `foreach.pvm`,
`yada_tada.pvm` and `local_pronouns.pvm`. They check the sequence 1 through 10,
the repeated update, traversal output, both when-condition outcomes, and local
pronoun output. Compiled results are compared with interpreter output.
These execution checks do not imply every clause in those files is gold-reviewed.

All six action clauses in `punah.pvm` now have independent grammatical reviews:
initial placement, addition, repeated addition, both result-storage occurrences,
and final printing. The written count reference is a locus when storing a value
and an object when adding or printing. Repetition preserves the relations without
collapsing separate occurrences. These checks cover the nominal relations;
the repetition operator's full grammatical derivation remains a separate issue.

All eight action clauses in `print_1_to_10.pvm` now have reviewed nominal
relations, including both collection-storage occurrences. Its ninefold addition
also checks the action qualification: frequency, event scope and count nine.
The count follows canonical [5.4.17](https://ashtadhyayi.com/sutraani/5/4/17).
The benchmark verifies this qualification after AST permutation and text reparsing.
The coordinating `च` remains an unclassified qualification rather than receiving
an invented adverbial meaning; nominal coordination is checked by the object roles.

This review exposed discarded derivation data: `PadaAnalyzer` converted a counted
numeral pada to an indeclinable without preserving its base and suffix. It now
retains that evidence. Tests also distinguish `सुच्` counting from `धा`
distribution and reject a five-plus-`सुच्` count. This does not certify the full
morphological derivation or scope of every multi-action loop.

The collection declarations in `foreach.pvm` and `local_pronouns.pvm` now have
reviewed member relations under 2.3.50. A coordinated genitive enumeration with
one written nominative `सूची` head supplies construction evidence: unambiguous
genitives anchor the shared relation and resolve the dual `ओस्` member as
genitive. This inference uses source structure, not execution lowering or memory.
Both declarations are checked with the collection head and verb preceding the
enumeration as well as in the source order. The foreach printing body is reviewed
separately; its traversal header is still outside the clause inventory.

Negative checks preserve uncertainty without coordination, without an
unambiguous genitive anchor, with mixed cases, competing collection heads,
another finite root, or a conflicting supplied relation. This is a bounded
collection construction; it does not resolve arbitrary genitives near a noun.

## Further numeral source corrections

`count_demo.pvm` now uses dual `ओस्` for two and plural `आम्` for three
in its member declaration. All three clauses have reviewed relations, and
actual interpreter/compiler tests verify the count three.

`power_demo.pvm` and `geometric_progression.pvm` now use `त्रि + सुच्`
for three action occurrences under canonical
[5.4.18](https://ashtadhyayi.com/sutraani/5/4/18), rather than the general
`कृत्वसुच्`. Their multiplication clauses have reviewed object relations and
event-frequency count three. Actual-file tests check final numeric values 81
and 54 and matching compiled display results. Numeral output spelling itself
is not certified by these checks.

The same review corrected numeral genitive endings in `sort_and_partition.pvm`,
`group_by.pvm`, and `distinct.pvm`. Tests verify the declared ordered members,
including duplicates, stable distinct output, and compiled/interpreted result
agreement for all three files. Readable companions were regenerated for all six
corrected sources.

All eight action clauses across those three collection files now have reviewed
nominal relations. Sorting and distinct take the list as object; grouping and
partitioning distinguish that object from the instrumental key operation through
1.4.42→2.3.18. Five action-clause word-order variants are checked through AST
permutation and reparsing.

Collection-member evidence now accepts chained `च` coordination, including the
parser's structured coordination nodes, while retaining the original member
occurrences. Repeated equal numerals are not merged. The three declarations
verify every member relation under 2.3.50. Leading/repeated connectives and the
previous ambiguity boundaries have negative tests. These declarations have no
text-reparsed permutation check because repeated identical source fragments
require an explicit occurrence-alignment scheme in the benchmark.

The cardinal two now uses accusative dual `औट्` in `yada_tada.pvm` and
`local_pronouns.pvm`; its comparison standard uses instrumental dual `भ्याम्`
in the latter. The readable companions were regenerated from the PVM sources.

The sequence example exposed two compiler defects: later statements lost the
declared collection destination's identity, and a prior pipeline result was
prepended even when the next invocation already supplied its object. The
compiler now retains declared destination names and respects written objects.
Regression coverage checks collection initialization, exact sequence contents,
and traversal over a snapshot even when its source variable is replaced.

## Validation

The two-sum stop now explicitly names `रमुँ` rather than the underspecified
`रम्`. Exact upadeśa lookup can identify roots without VM operations, while
retaining executable-root collisions and refusing ambiguous bare roots.
The stop construct and its renderer require the selected lexical identity,
the `वि` prefix, imperative lakāra and second-person singular ending. A different
homonym, causative, contradictory gaṇa or ordinary present form does not match.
Ordinary lexical-only roots remain non-executable. Tests cover these boundaries,
procedure escape diagnostics and the actual two-sum result.

Canonical [1.3.83](https://ashtadhyayi.github.io/sutra-details/?sutra=1.3.83)
licenses parasmaipada with the specified prefixes on this root; the language's
top-level halt effect is a VM construct, not a kāraka sūtra. Bare `वि + रम्`
remains an unresolved-identity negative test.

The preceding `क्त्वा` clause now has a two-frame gold review: the genitive
action nominal modifies `कर्मणी` under 2.3.50, and `कर्मणी` is the printing
object under 1.4.49→2.3.2. The stop frame has no written participant. The JSON
review checks both original head forms, nonfinite/finite status and their
prior-action link. The expanded written-action nominal inference remains bounded
to one action genitive and one `फल` or `कर्मन्` head; competing heads and syncretic
genitives are not given a guessed relation.

`PriorActionStructure` now shares written action boundaries between grammar and
execution. The nonfinite derivation and source span remain available on internal
head projections; their inherited binding fields are not claimed as source tense
or person. Grammar keeps written participants, whereas execution may complete
an omitted display result and share a linked agent. Separate frames retain
their original source-clause identity. Composite range-exclusion binding obtains
each operand's relation from its own action, and frequency lookup uses source
identity rather than assuming one frame per invocation.

In an active prior-action construction, a written nominative controller now
belongs to the finite action even when positioned before `क्त्वा`. The preceding
action receives the shared-agent link, without a copied noun or borrowed finite
case realization. Tests require 1.4.54→2.3.46 and finite singular/dual agreement
with the controller on either side of the prior-action boundary.

That test also exposed a separate 1.4.55 error: both the lexical `कृ` entry and
fallback profile declared `PROMPTER_CAUSE` for ordinary `करोति`. The root-wide
premise was removed. Plain `कृ` now uses 1.4.54→2.3.46; a real `णिच्`
construction still uses 1.4.55→2.3.46 and retains HETU.

The same-agent/prior-time relation follows canonical
[3.4.21](https://ashtadhyayi.com/sutraani/3/4/21); this does not certify every
nonfinite construction. Grammar analysis now accepts passive and bhāve main
actions when their action ownership is represented. The finite nominative is a
patient in karmani; an established instrumental agent can share the prior action
without becoming a copied instrumental in its active frame. The tests require
1.4.49→2.3.2 on the prior object, 1.4.49→2.3.46 on the passive patient, and
1.4.54→2.3.18 on an established finite agent. Agreement follows the passive
patient, while bhāve remains impersonal singular.

The positive instrumental-agent fixtures explicitly supply that reading. Without
it, actor/tool ambiguity remains and no definite shared agent is created. A
preposed instrumental can attach to the finite action when independent lexical
or clause-context agent evidence establishes its role and instrumental case.
Without that evidence it retains an explicit blocking ownership diagnostic.
Original occurrences are preserved, including across multiple prior actions.
Ordinary VM execution of passive
prior/main chains remains unsupported; grammatical acceptance does not remove
that execution guard. Four JSON word-order variants now check explicit
action-local alignment when finite patients or selected agents precede or
separate prior objects and their heads. Broader multi-action permutation
coverage remains outstanding; unit tests also cover reordered prior operands.

PVM parsing has one public source entry point, `PvmScript.parse`, backed by the
ANTLR document parser and `fromDocument` projection. The migration-only physical
line parser, its validation API, sentence scanner, block-boundary helper and
separate adhikāra header parser were deleted. Scope, negation and declaration
tests now exercise the canonical document path. Repository parsing includes all
PVM files, including cross-daṇḍa continuations formerly skipped by a parity test.

`FileScriptExecutionParityTest` compares file loading with direct source execution
for the selected arithmetic, collection, control-flow and project programs.
The prior old/new parser comparison was removed. Canonical procedure termination
is tested across line breaks; an extra completed sentence terminator followed by
a separate block terminator is rejected rather than accepted by a fallback.

- `KarakaCorpusBenchmarkTest`: all reviewed source clauses and order variants.
  It rejects duplicate answer keys for the same path, normalized clause and
  occurrence before inventory matching.
- `ReviewedScalarExamplesTest` in execution: actual PVM files, typed results and output.
- `ReviewedScalarExamplesTest` in compiler: the same files as generated JVM programs.
- `ActionNominalRelationEvidenceTest`: order independence and ambiguity boundaries.
- `PriorActionGrammarTest`: original ownership and spans, source identity,
  shared-agent omission, no invented display object, and passive limitations.
- `PriorActionGrammarBenchmarkTest`: seven independent base JSON cases and four variants in
  `prior_action_grammar_expectations.json`, covering selected passive roles,
  a selected preposed agent, bhāve, competing agent/tool meanings, ambiguity,
  agreement error and unsupported attachment. Each analyzed participant must
  preserve its original occurrence. These separate
  grammatical fixtures do not increase the reviewed PVM corpus count.
- `ReviewedConditionalExampleTest` in execution/compiler: both actual branch paths.
- `ReviewedLoopExamplesTest` in execution/compiler: actual loop/control-flow sources.
- `AdverbialAnalyzerTest`: counting derivations survive pada analysis, with
  distribution and invalid-affix negative cases.

The nine-module gate (`core`, `dhatupatha`, `ashtadhyayi`, `analysis`, `parser`,
`actions`, `execution`, `compiler`, `cli`) passes after these changes. The gate
also exposed obsolete dhātupāṭha contract tests for removed comparison/membership
aliases; those tests now assert the subject/nominal-standard and entity/locus
contracts directly.

Remaining source work includes the unreviewed clauses, procedure declarations,
remaining loop/condition meanings, sandhi segmentation, and full nonfinite/passive ownership.
Parsing success alone is not a correctness certificate for those sources.
