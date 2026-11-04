# Kāraka correctness benchmark

Dataset: [`src/test/resources/karaka_benchmark.json`](src/test/resources/karaka_benchmark.json).

Run every record:

```powershell
.\gradlew.bat :analysis:test --tests dev.panini.analysis.KarakaJsonBenchmarkTest
```

The runner reads and executes every JSON record and writes
`analysis/build/reports/karaka-benchmark.json`. It checks exact sūtra chains,
resolved relation/case, alternative cases, candidate/established governor counts,
and missing semantic premises. Duplicate IDs and invalid schema versions fail.

The 1069 records cover active/passive agents and objects, a locus,
causative and passive-causative participants, unannotated genitive versus explicit
śeṣa, comparison and benediction, reordered/intervening nominals, excluded
comparison terms, absent premises and identical competing governors.
Repeated identical dependents also test isolation of occurrence-local facts.
Negative dative cases distinguish case/category hypotheses from established
recipient meaning and preserve an unresolved benediction construction.
Written/implied recipient-object links test endpoint selection, rejection of an
unrelated endpoint despite another valid object, and known role with invalid case.
The written-object link is also checked with passive nominative object morphology.
Two records check that an unvalidated-case diagnostic belongs to the correct
nominal occurrence even when another occurrence has identical source text.
Passive instrumental cases distinguish supplied agent/instrument readings,
unsupported morphology-only hypotheses and equally supported competing roles.
Kṛt records cover finite/nominal action separation, outer voice independence,
genitive eligibility with implied objects, special kta and kṛtya cases, unknown
readings and rejection of an object belonging to another action.
Twelve additional records cover linked nominal-action prefixes for 1.4.46–48:
each special locus reaches 2.3.66 in active and passive outer clauses, while
the same prefix and governor without locus meaning establishes no such role.
The current report contains 1025 records with supplied premises and 44 without;
the supplied governor/reading links count as semantic annotations.
Thirty-six mediator regressions cover written and unbound recipient links in
active and passive clauses. Equal object/source, object/instrument and
object/locus readings are tested with an object-compatible ending, an
alternative-compatible ending, and an ending compatible with neither reading.
An unresolved mediator is recorded explicitly and cannot supply a recipient
operand to execution.
Forty-eight additional generated regressions contrast missing object evidence
with an explicit omitted-object link over twelve root spellings in active and
passive frames. They test removal of the sole-participant shortcut and blanket
root exclusions, rather than attesting every synthetic lexical reading as a
natural Sanskrit example. Special constructions still require their own premises.
Eighty further records check the mediator and recipient independently for all
ten registered object-designation rules (1.4.38, 43, 46–53), in active/passive
or causative/passive-causative frames, with written and discovered links.
Eight records ensure weaker śeṣa evidence cannot veto a resolved object. Twelve
records check stronger desired/undesired meanings, equal readings with both
canonical proofs, and invalid endings. These supplied-reading tests do not
measure independent discovery of intention, desire or lexical senses.
Forty additional records distinguish co-designation under 1.4.43 from optional
designation under 1.4.44/53. They check applied, instrumental, shared भ्याम्,
invalid-case and missing-premise readings; both हृ and कृ are included. The
runner checks canonical counter-branch proofs separately from optional decisions,
and checks simultaneous names and case projections. Two older passive-div
expectations were corrected: the common kāraka power is already expressed, so
no extra instrumental form may be generated.
Optional `lexicalRelations` supplies lexical semantic evidence separately from
clause premises. Optional `competingAnalyses` checks each competing role, nullable
case and exact proof chain. Eighteen source/recipient regressions cover stronger
evidence, equal readings, shared endings, a uniquely compatible case and endings
compatible with neither reading, in active and passive clauses.
Optional `krt.sanadi` preserves the nominal governor's own sanādi morphology.
Nominal causative ownership records distinguish its 1.4.52/53 designation from
the enclosing finite verb's root and causative morphology.
Twenty-eight object regressions separate 1.4.49 semantic designation from case
validation across active, passive, causative and passive-causative clauses:
four valid cases, twelve invalid endings with supplied object meaning, and
twelve missing-meaning counterexamples. Invalid endings retain the established
role and emit UNVALIDATED_VIBHAKTI without manufacturing a 2.3 proof.
Thirty-eight additional records cover independent agent designation with valid
and invalid endings, and equal object/instrument, object/locus and object/agent
readings. A unique case-compatible reading resolves; when neither reading fits,
both semantic proofs survive without a case proof. Agent cases include active,
transitive passive and intransitive bhāve constructions. The runner checks the
declared prayoga against the actual frame for every record. Sixteen formerly
KARMANI-labeled intransitive fixtures are now correctly labeled BHAVE.
Eighty-four source-boundary records cover each of 1.4.25–31 in two prayogas:
valid endings, invalid endings, missing special meaning, missing established
source meaning, overlap with separation and ordinary separation without the
special premise. Structured prefixes and ṇic are retained where required.
The ordinary rule defers to an applicable canonical special rule, rather than
excluding a whole root family. All defining special-source premises must have
established provenance; an ending alone cannot supply the missing meaning.
Fifty-two recipient-boundary records cover 1.4.33–38: valid and invalid endings,
missing special meaning and missing established recipient meaning, in two
prayogas per construction. Four records additionally check 1.4.36 versus 1.4.49
under the project's existing Nyāsa policy. Established most-desired-object
meaning takes the object designation even with a wrong dative ending; the
analyzer cannot change the meaning to make that ending valid.
Seventy-six late-recipient records cover 1.4.39–41: both inquiry roots, both
prefixes of each prior-agent construction, invalid endings, missing premises,
unknown/wrong prefixes, wrong roots and the swallowing homonym of गॄ. Optional
head `gana` selects fixture lexical metadata explicitly; lexical class is not
counted as a supplied semantic premise. Inquiry and praise use bhāve for their
intransitive passive-shaped clauses; promise uses transitive passive.
Forty mediator-eligibility records check written and legacy unbound recipient
links in active/passive clauses. Stronger locus/source/instrument meanings,
unsupported secondary objects and causee-tagged non-objects cannot mediate the
ordinary recipient rule. Valid unambiguous object constructions and known objects
with invalid endings remain eligible; invalid recipient endings retain 1.4.32
without a case proof. This checks object-rule eligibility, not complete donation
intent discovery or a joint resolution of every competing participant reading.
Eight records contrast the 1.4.49 object with the 1.4.42 milk instrument and
1.4.45 bean locus, following Kāśikā's two counterexamples. Both participants
are checked separately in active/passive constructions with supplied readings.
Four records supply a locus/instrument reading against an active accusative or
passive nominative object hypothesis. The stronger meaning is retained while
the incompatible written case receives UNVALIDATED_VIBHAKTI.
Eight formal-instruction records use structured अधि + इ and आङ् + गम् + णिच्,
including passive variants and missing formal-instruction-premise negatives.
Eight concealment records check structured नि + ली and explicitly supplied
self-concealment meaning with धा, requiring an intended observer and a
concealment domain separately. These supplied premises do not demonstrate
automatic recognition of अन्तर्धा or of the observer's intent.
Eight prevention records check structured नि + वृत् + णिच् in active and
passive-causative contexts, with missing-intent and wrong/unknown-prefix negatives.
Twelve action-marking records exercise 2.3.37–38 with active/passive outer verbs:
ordinary locative, disregard genitive/locative, ओस् case ambiguity, bare disregard
and incompatible genitive that cannot fall back to 2.3.50. Event linkage is
supplied as ACTION_MARKING; these records do not parse two events automatically.
Twenty-eight records check 2.3.33's four lexical stems, both cases, both outer
voices, syncretic भ्याम्, substance/other-stem/wrong-role readings and missing
instrument/non-substance premises. The non-substance reading is supplied;
generic quantity alone is insufficient.
Twenty-eight distance records distinguish a reference noun's genitive/ablative
under 2.3.34 from the non-substance distance word's own four cases under
2.3.35–36. They include missing premises, competing governor heads, ओस्/भ्याम्
case selection and incompatible cases. Missing-meaning reference records are
counted as unannotated, not as successful unassisted interpretation.
Twenty-six derived-avyaya records verify 2.3.30/31 using linked अतसुच्/एनप्
nodes, both outer voices, both ENAP cases and ओस्, with surface-only,
wrong-affix/base/case and marker-only negatives. The derivations and links are
supplied, not discovered or generated by this benchmark.
Thirty-four 2.3.29 records check seven supported nominal governors, आरात्/ऋते,
missing meanings, competing heads, lookalikes and marker-only negatives in both
voices. Linked आरात् with distance meaning takes ablative and rejects genitive.
Twenty-two purpose action-noun records check 2.3.15 with घञ्/क्तिन्/ल्युट्
representations, both outer voices, and missing purpose/denotation, agent-affix,
unparsed-stem, unknown-affix and wrong-case negatives. Action denotation and
tumartha purpose are supplied separately; the benchmark does not infer them.
Eighteen purpose-scope records distinguish an omitted purpose action's object
(1.4.49→2.3.14) from a written तुमुन् action's object (1.4.49→2.3.2).
Both outer voices, invalid cases, missing object meaning, a bare legacy marker
and an unsupported intransitive-object reading are checked. Objects remain in
`purposeKarakas`, outside finite participants, with their own action identity.
Source examples are distinguished from generated regression cases; source references
do not register additional executable rules.

Inputs are segmented AST morphology, not raw Sanskrit text. Optional `premises`
fields supply semantic facts through `VakyaSemanticContext` for that nominal
occurrence only. They do not alter the fixture lexicon. Repeated identical stems
may have different premise sets. The report splits clauses with and without
supplied premises; a target occurrence may itself be unannotated in an annotated
clause. A passing annotated case establishes
the integration contract given those facts; it does not demonstrate independent
semantic discovery. Missing-premise cases test uncertainty preservation, not
successful interpretation of an unannotated sentence. This is a correctness
benchmark, not a runtime performance benchmark or complete grammar-coverage score.

Each `input` declares the root, base transitivity, voice, nominal stem/sup slots
and optional verbal `upasargas` as an ordered list,
and dependent index. `expected` declares the grammatical result and link-state
assertions. `kind` records provenance; canonical examples include a source key
resolved through the dataset's `sources` mapping. Extensions should include
negative and ambiguity records alongside positive examples and should keep
supplied semantic facts explicit.

A nominal may additionally declare `intendedObject` as
`{"kind":"written","index":1}` or `{"kind":"implied"}`. This is an explicitly
supplied object-mediated recipient relation, not automatic inference. It is
counted with supplied semantic premises in the report. Indices refer to the
input nominal list and are checked by the clause-context validator.

Optional `expected.diagnostics` asserts the exact diagnostic-code set for the
target nominal occurrence, using `FrameDiagnostic.nominalIndex` rather than
matching source text. Clause-wide diagnostics are not counted as participant
diagnostics.
Optional `expected.roleCandidates` checks the exact ambiguous role set.
`expected.additionalDesignations` checks co-designations such as HETU under
1.4.55; omission asserts an empty set, so unrelated participants cannot acquire it.

`expected.jointAlternatives` checks coordinated reinflection choices in
`frame.nominalCaseLinks.jointAlternatives`. Each entry has `members` (indices
in the complete flattened input nominal list), `case`, and `chain`.
Omission asserts no joint alternatives. These are distinct from the
per-participant `alternatives`: a 2.3.27 case switch requires reinflecting
the pronoun(s) and written हेतु together. The evidence is optional and
unapplied, and no source pada is mutated. Invalid phrases, ambiguous
attachments, and groups containing a mandatory-genitive member do not
receive this suggestion. The benchmark tests those exclusions as well as
both switch directions and shared-head groups.

A kṛdanta nominal declares `krt: {"root":"कृ","affix":"ल्युट्"}` (and optional
base `transitivity`). A dependent declares `krtGovernor` with its nominal `index`,
`expressedRoles` (null for unknown, empty for an action-only reading), and optional
`genitiveEligibleRoles`. These selected-reading annotations count as supplied
semantic facts. The runner checks the result in `frame.nominalKarakas`, including
that it is absent from the finite verb's participant list. Known simple lexical
eligibility is derived by the analyzer rather than copied from expected results.

A dependent may declare `avyayaGovernor: {"form":"दक्षिणेन","base":"दक्षिण",
"affix":"एनप्"}`. The runner constructs that actual derived avyaya occurrence
in the clause and supplies its identity through `VakyaSemanticContext`.
Omitting base/affix creates an unparsed surface-only negative. Such links count
as supplied premises; no extra sup slot is fabricated for the indeclinable.

`purposeGovernor` supplies a selected pursued action, e.g.
`{"kind":"IMPLIED","id":"gather","root":"हृ","upasargas":["आङ्"]}`.
Use `kind: "WRITTEN"` with `form: "आहर्तुम्"` to place an actual तुमुन्
node in the clause. These action/mode links are supplied premises, not inferred
ellipsis or purpose. Repeated IDs share a consistent selected action; distinct
IDs retain separate actions. `transitivity` describes the base root, and optional
`sanadi` records its morphology. The current verified pipeline covers ordinary
primary-object readings; the full nonfinite argument system is not certified.

Thirty-two 2.3.16 records check the linked नमः/स्वस्ति/स्वाहा/स्वधा/वषट्/अलम्
expressions, supported प्रभु/शक्त sufficiency readings, missing senses, competing
heads, and स्वस्ति benediction precedence over genitive. Bare-dative and
missing-sufficiency negatives use a neutral outer root so they do not accidentally
test an independently available verbal recipient hypothesis. These construction
records do not certify automatic salutation/sufficiency interpretation.
