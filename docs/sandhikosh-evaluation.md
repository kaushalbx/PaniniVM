# SandhiKosh evaluation

The external runner is opt-in. Ordinary tests exercise the sequence evaluator
with small regressions; they do not download or require the external workbooks.

Sources:

- https://github.com/sanskrit-sandhi/SandhiKosh/blob/master/Astaadhyaayii%20Corpus.xls
- https://github.com/sanskrit-sandhi/SandhiKosh/blob/master/UoH_Corpus.xls

Extract each original data row into `cases.json` in a corpus-specific local
directory. The JSON is an array of objects with `sheet`, `row`, `id`, `type`,
`split`, and `expected` fields. Retain source row numbers, original split text,
and expected text. Do not repair malformed splits or remove shaping controls.
Both source workbooks have columns S.No, Word, Split, Rule, Type; map Word to
expected and Split to split, excluding only the header row.

Run on Windows (use `./gradlew` on other systems):

```powershell
.\gradlew.bat -I scripts/sandhikosh-evaluation.init.gradle -PsandhikoshEvaluationDir=build/sandhikosh-evaluation :derivation:test --tests '*SandhiKoshEvaluationTest' --no-parallel
```

Use `build/sandhikosh-uoh-evaluation` for UoH. Add
`-PsandhikoshAlternatives=true` for the separate alternative-inclusive run.
Default reports are `results-sequence.csv` and `summary-sequence.txt`;
alternative-inclusive reports use the `-alternatives` suffix. These do not
overwrite the original rendered-string-fold baseline reports.

## Method and limits

Evaluate adjacent completed padas from left to right, retaining transformed
terms, their grammatical context, orthographic signs, and each boundary trace.
Only the surviving rightmost phonological term enters the next boundary;
earlier padas remain frozen. Render contiguous annotated Varṇa sequences only
after joining, retaining explicitly licensed hiatus. This is an external-sandhi
evaluation policy, not exhaustive sentence parsing or morphological inference.

Default matching tests the selected derivation. Alternative-inclusive matching
tests all branches enumerated by `SandhiEngine.joinAll` at each boundary. It is
not proof that all grammatical options are implemented. The evaluator throws
an explicit error if a boundary exceeds 4,096 outcomes; it never truncates them.

Primary matching retains the previous benchmark's NFC-normalized,
whitespace-insensitive comparison. Strict NFC matches including spacing are
reported separately. No punctuation, shaping-control, or phonological
normalization is added. Empty split components remain skipped. Exceptions
remain errors, not mismatches or fallback outputs. A successful Gradle run means
evaluation completed, not that every corpus row matched.

## Measured checkpoint, 2026-10-14

| Corpus | Prior default passes | Sequence default passes | Including alternatives | Prior errors | Sequence errors | Skipped |
| --- | ---: | ---: | ---: | ---: | ---: | ---: |
| Aṣṭādhyāyī (2,700 rows) | 884 | 911 | 1,106 | 216 | 4 | 1 |
| UoH (9,368 rows) | 4,941 | 5,038 | 5,668 | 475 | 0 | 4 |

Strict default matches: 636 and 4,512 respectively. Strict alternative-inclusive
matches: 651 and 4,541. The remaining four Aṣṭādhyāyī errors contain parentheses;
they require source interpretation rather than silently discarding punctuation.
The counts above measure a runner correction and optional-output evaluation,
not newly implemented sūtras. Substantial grammatical mismatches remain.

## Parasavarṇa correction, 2026-10-14

Rules 8.4.58 and 8.4.59 now share a typed nasal-counterpart mapping. It retains
the five stop-class nasals, supports explicitly nasalized y/v/l tokens, and
returns no counterpart for ra or sibilants instead of inventing dental na.
The optional semivowel counterparts are described in the Bālamanoramā on
[8.4.59](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.59.htm).
Six native benchmark cases cover application and retention before y/v/l.

| Corpus | Default passes before → after | Including alternatives before → after | Errors |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 911 → 887 | 1,106 → 1,106 | 4 |
| UoH | 5,038 → 4,936 | 5,668 → 5,671 | 0 |

The default policy applies every optional rule, so it now selects nasalized
semivowels where many corpus rows retain anusvāra. The retention branch remains
available. Do not adjust rule selection to improve the default score.
Strict default matches remain 636 and 4,512; strict alternative-inclusive
matches are 651 and 4,544 respectively. Skipped rows remain 1 and 4.

## Pada-domain correction, 2026-10-14

Rules 8.3.23, 8.3.24, 8.4.58 and 8.4.59 share an explicit pada-final Varṇa
predicate. A term ending alone does not license 8.4.59: the left term must
carry the existing PADA/UPASARGA designation. The complementary non-pada domain
remains under 8.4.58. Follower lookup for 8.3.23, 8.3.24 and 8.4.59 now passes
over soundless terms without deleting their identities or provenance.

Five regressions cover the internal/external distinction, a PADA designation on
the wrong term, a soundless affix, the existing upasarga treatment, and a nasal
inside a designated pada. All four corpus counts above are unchanged: the
external runner already explicitly designates each input as a pada.
The pre-existing blanket affix restriction in 8.3.24 remains; its lifecycle
domain needs a separate review rather than broadening applicability in this batch.

Further migration audit identified string-based affix checks in 7.3.86 and
7.3.128. The latter also still renders a constructed Varṇa list inside the rule
before calling its merge helper. These need a typed-identity and term-boundary
composition batch; they are not evidence that migration is complete.

## Typed ghi composition, 2026-10-14

The 7.3.86 tiṅ check and 7.3.128 sup check now use current typed affix identities,
not sthānin-aware matching. Wrong term kinds and already-replaced sup forms do
not regain applicability through historical identity.

7.3.128 uses an exact final-Varṇa substitution at the term boundary instead of
rebuilding a surface and passing it through the legacy merger. The replacement
explicitly identifies its ā vowel as the incorporated affix vowel. Earlier
nasality and written signs survive; consumed affix identity, original surface,
markers, and the merged vowel locus remain in the lifecycle ledger. Pending or
deferred designations are explicitly consumed under 7.3.128, as required by
the existing feminine paradigms, not silently discarded or concatenated.

Six regressions cover annotated composition, final-vowel nasalization, short
and graded i/u domains, wrong/current versus historical identity, and raw and
deferred affix consumption. Both default and alternative-inclusive corpus
counts are unchanged from the parasavarṇa checkpoint above.

Legacy surface-taking merger users remain in the neighboring ghi, n-stem,
s-stem, ṛ-stem and pronoun rules (including GherNgiStriyam, GherShasiStriyam,
GherNgeStriyam, RtoAuo and YusmadAsmad). Their exact consumed ranges and affix
vowel loci must be specified before replacing those entry points.

## Neighboring ghi endings, 2026-10-14

GherShasi (7.3.124), GherShasiStriyam (7.3.126), GherNgeStriyam (7.3.127), and
GherNgiStriyam (7.3.129) now use current typed sup identities and the exact
final-Varṇa composition helper. The long vowel at replacement index 0 or the
yai/yām vowel at index 1 explicitly incorporates the affix vowel. Their existing
gender, number, case, GHI/NADI applicability, and output stages are retained.
Matrix regressions cover ordinary forms, nasalization, sign placement, consumed
affix provenance, renamed term IDs, and rejected historical/wrong-kind affixes.
All default and alternative-inclusive corpus counts remain unchanged.

The source audit still finds 35 files calling the legacy surface-taking merger:

| Family | Files |
| --- | ---: |
| n-stem endings | 11 |
| ṛ-stem endings | 7 |
| s-stem endings | 7 |
| neuter s-stem rules | 3 |
| pronouns | 2 |
| Savupadhayasca | 1 |
| vowel composition (AtoGune, AutoAmSaso, EngiPararupam, VrddhirEci) | 4 |

This is a specific call-site inventory, not a complete migration audit. The
n-/s-/ṛ-stem batches require exact consumed ranges rather than assuming every
operation changes only the final Varṇa. Vowel coalescence also needs explicit
annotation/provenance choices for both participating vowels.

## Benchmark-driven ṇatva fix, 2026-10-14

The evaluator now emits `failure-groups-sequence.txt` and
`failure-groups-alternatives.txt`, grouped by original two-pada Varṇa boundary
or multi-part length, with source row, expected output, actual output and trace.
Grouping does not change matching or exclude cases.

The UoH alternative-inclusive audit identified visarga + a as the largest
two-pada mismatch group (759 rows). Representative traces apply 8.3.17/8.3.19
where the expected output has o/avagraha. This remains a high-priority rutva
origin/modeling issue; expected output must not be used to infer the origin.

A separate verified error carried ṇatva triggers across completed external
padas (e.g. rāma + hanumān). Rules 8.4.1 and 8.4.2 now respect explicit completed
pada boundaries; 8.4.2 also protects completed pada-final n. Ordinary internal
stem/affix boundaries, including internal PADA-designated affixes, do not
automatically become external word boundaries. The same-pada restriction is
explicit in the [Kāśikā on 8.4.1](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.1.htm).

| Corpus | Default passes before → after | Including alternatives before → after | Errors |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 887 → 891 | 1,106 → 1,112 | 4 |
| UoH | 4,936 → 4,954 | 5,671 → 5,707 | 0 |

Strict default matches: 640 and 4,527. Strict alternative-inclusive matches:
655 and 4,560. Skips remain 1 and 4. Five focused regressions and two native
benchmark cases cover the correction. No corpus text or normalization changed.

This does not yet preserve original pada topology through vowel coalescence
that consumes a term. Such merged-boundary failures remain (including UoH's
nigṛhya + ānaya pattern), and require explicit boundary provenance rather than
a spelling-based exception.

## Missing-rule audit and 8.4.45, 2026-10-14

The registry had no 8.4.45, यरोऽनुनासिकेऽनुनासिको वा. It is now registered for
optional external-pada nasal assimilation, using YAR membership and the source
sound's typed nasal counterpart. For example, तत् + न has तन्न and तद्न
branches; वाक् + नयति has वाङ्नयति and वाग्नयति branches. The source class,
not the following nasal's class, determines the substitute. Already-nasal
sources and sounds without a nasal counterpart do not produce no-op cycles.

[Kāśikā on 8.4.45](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.45.htm)
gives the optional pada-final domain and separately discusses the obligatory
suffix varttika. This batch implements the external-pada rule, not that separate
suffix-specific varttika. Five focused tests and four native benchmark rows
cover both optional branches, the five source stop classes, annotations and
negative applicability.

| Corpus | Default passes before → after | Including alternatives before → after | Errors |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 891 → 906 | 1,112 → 1,127 | 4 |
| UoH | 4,954 → 5,025 | 5,707 → 5,792 | 0 |

Strict default matches: 655 and 4,605. Strict alternative-inclusive matches:
670 and 4,638. Skips remain 1 and 4. No corpus text or normalization changed.

Further confirmed gaps, distinguished from present-but-inapplicable rules:

| Rule | Registry finding | Benchmark evidence / next requirement |
| --- | --- | --- |
| 8.3.32 ङमो ह्रस्वादचि ङमुण् नित्यम् | Implemented in the next batch below | Short-vowel nasal augmentation with raw augment placement and it processing. |
| 8.3.7 नश्छव्यप्रशान् | Implemented in the later batch below | Coordinated ru substitution and 8.3.2/4 alternatives, not an output shortcut. |
| 6.1.113 / 6.1.114 | Present | Pausal visarga input loses ru versus lexical-r origin; adding duplicate rules cannot resolve that. |
| 8.4.56 वावसाने | Present; corrected in the pause-context batch below | Explicit avasāna is required; it now participates in the SANDHI phase. |

Sources for the next missing rules:
[8.3.32](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.32.htm)
and [8.3.7](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.7.htm).

## Missing 8.3.32 implemented, 2026-10-14

8.3.32 now inspects cached Varṇas: a short vowel immediately before pada-final
ṅ/ṇ/n and a following vowel-leading pada license the corresponding raw
ṅuṭ/ṇuṭ/nuṭ augment. Existing 1.3.3, 1.3.2, 1.1.46 and 1.3.9 handle designation,
initial placement and lopa, preserving exact it provenance. No new shared
abstraction was needed. The augment belongs to the following pada, leaving the
original nasal pada-final, as explained in the
[Kāśikā](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.32.htm).
Three focused tests cover five positive examples, five exclusions, target
ownership and provenance; three native benchmark cases cover trace and output.

| Corpus | Default pass / fail | Alternatives pass / fail | Errors / skips |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 906 / 1,789 | 1,128 / 1,567 | 4 / 1 |
| UoH | 5,039 / 4,325 | 5,806 / 3,558 | 0 / 4 |

Strict matches are 651 / 666 for Aṣṭādhyāyī and 4,619 / 4,652 for UoH
(default / alternatives). Compared with the preceding batch, UoH gains 14
passes in either mode; Aṣṭādhyāyī gains one alternative-inclusive pass but loses
four strict matches. Technical affix names such as liṅ and aṇ in the latter
corpus are currently submitted as completed padas by the harness, exposing its
missing metalinguistic/affix input classification. Corpus text and normalization
remain unchanged; this is not repaired through spelling-specific exclusions.

At that checkpoint, remaining benchmark priorities were missing 8.3.7 and its ru/nasalization
dependencies, visarga-origin provenance for existing 6.1.113/114, and original
pada topology through coalescence. These are grammatical/input-model issues,
not grounds for Unicode-based output patches. Standalone legacy Varṇa
composition migration remains deferred while benchmark work is prioritized.

Verification: all three focused tests and all 310 native sandhi benchmark cases
pass; the complete `gradlew test --no-parallel` suite passes (52 seconds), and
`git diff --check` passes. Changes remain uncommitted.

## Coordinated 8.3.2/4/7 and scoped fresh ru, 2026-10-14

8.3.7 now applies to pada-final n (except the lexical praśān) before CHAV
immediately followed by AM. The new typed pratyāhāras are tested for their exact
domains. Its two co-prescribed outcomes are nasalization of the preceding
occurrence under 8.3.2, or an anusvāra augment under 8.3.4 when nasalization is
not chosen. The source explicitly describes the preceding occurrence relative
to the source of the impending ru substitution, so those operations run before
8.3.7 in textual order; no scheduler bypass is introduced.

Sources: [Kāśikā 8.3.2](https://sanskritdocuments.org/~sanskrit/learning_tools/ashtadhyayi/vyakhya/8/8.3.2.htm),
[8.3.4 in the Kāśikā](https://sa.wikisource.org/wiki/काशिका/अष्टमोऽध्यायः/तृतीयः_पादः),
and [8.3.7](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.7.htm).
This batch implements 8.3.2/4 in the implemented 8.3.7 domain, not unimplemented
ru-section rules or Vedic variants.

Shared `substituteFinalVarnaWithRutva` introduces annotated r + nasalized u at
the exact final occurrence. `pendingRutvaUpadeshaIndex` limits fresh upadeśa
processing to that newly introduced segment: existing nasalized vowels and
initial sounds are not newly designated it. Existing 1.3.2 and 1.3.9 process the
new u with exact provenance. Carrier identity, lexical upadeśa, creator, written
signs and historical metadata survive. A wholly replaced one-sound affix instead
acquires the ru upadeśa while retaining its original sup/tiṅ slot and sthānin.
8.2.66 now shares this helper and waits for outstanding deferred it-lopa rather
than discarding its designation. 8.3.15 recognizes the exact ru occurrence
introduced by either 8.2.66 or 8.3.7.

The new nasal-vowel branch also exposed an existing 8.3.22 overreach: it deleted
visarga/s and even a following vowel together with y. It now deletes only final
y in its eligible domain. Existing tests were corrected to supply the actual
y substitute, not a raw visarga or ya syllable.
[Kāśikā 8.3.22](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.22.htm)
supports the narrower sound target.

| Corpus | Default passes before → after | Alternatives passes before → after | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 906 → 924 | 1,128 → 1,150 | 1,771 / 1,545 |
| UoH | 5,039 → 5,045 | 5,806 → 5,818 | 4,319 / 3,546 |

Errors/skips remain 4/1 and 0/4. Strict matches are 665/682 and 4,625/4,664
(default/alternatives). No corpus text, optional selection policy or matching
normalization changed. Six focused tests cover both branches, exact CHAV/AM
membership, exclusions, existing nasality, it history and initial-marker safety.
Six additional native benchmark cases exercise outputs and required/forbidden
traces, bringing the native total to 316.

Remaining priorities: missing 8.3.36 and further ru-section domains; written
visarga origin for existing 6.1.113/114; original pada topology after coalescence;
and metalinguistic input classification in the Aṣṭādhyāyī corpus. Legacy shared
string-returning Varṇa composition also remains to be migrated; this batch does
not claim completion of that wider audit.
The current search finds 35 sūtra files using `mergeTermsByVarnaSubstitution`;
their remaining stem/vowel compositions need exact occurrence and it-designation
maps rather than replacement-surface strings. No sūtra directly calls
`surface.toVarnas()` in the searched implementation tree.

Verification: six focused new tests, the internal Ghi/affix regressions, and all
316 native sandhi benchmarks pass. The complete `gradlew test --no-parallel`
suite passes (1 minute 27 seconds), and `git diff --check` passes. No commit.

## Missing visarga-retention rules 8.3.35/36, 2026-10-14

Added mandatory 8.3.35 retention before KHAR immediately followed by SHAR and
optional 8.3.36 retention directly before SHAR. Declining 8.3.36 exposes the
existing 8.3.34 s-substitution and subsequent ścutva/ṣṭutva branch. Examples and
domains follow [Kāśikā 8.3.35](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.35.htm)
and [8.3.36](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.36.htm).
The additional optional-deletion vārttika mentioned under 8.3.36 is not modeled
as the sūtra itself; it remains a separate multi-outcome/trace modeling task.

Shared `VisargaBoundary` lookup uses cached Varṇas, looks through soundless
terms, and supplies exact following phonological positions. 8.3.34 now uses the
same lookup and honors boundary-scoped protection. Retention changes only the
protection ledger, not term identity, annotations, metadata or sounds. Existing
ru origin is not reconstructed from written visarga.

An identity-retention guard explicitly does not advance the phonological
Tripādī frontier, so protecting one boundary cannot hide still-pending lower
operations at another boundary. The engine validates that such guards do not
change terms. Explicit conflicts carrying boundary keys are checked against
the controlled target rather than applied globally. A multi-boundary regression
exercises 8.3.35, then 8.3.36, then independent 8.3.34 work with correct traces.

| Corpus | Default passes before → after | Alternatives passes before → after | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 924 → 937 | 1,150 → 1,171 | 1,758 / 1,524 |
| UoH | 5,045 → 5,042 | 5,818 → 5,818 | 4,322 / 3,546 |

Errors/skips remain 4/1 and 0/4. Strict matches are 664/682 and 4,622/4,664
(default/alternatives). Default UoH loses three matches because APPLY_ALL now
chooses optional retention where the corpus expects the s branch; the previous
matches remain available when alternatives are evaluated. No selection policy,
corpus data or matching normalization was changed to hide this difference.

Eight focused tests and eight source-backed native benchmark rows cover all
three sibilants, compulsory clusters, immediate-position exclusions, explicit
underlying s, annotations and independent boundary scheduling. All 324 native
benchmarks and the full suite pass (`gradlew test --no-parallel`, 1 minute 29
seconds); `git diff --check` passes. Changes remain uncommitted.

Next modeling gaps include 8.3.37 and its jihvāmūlīya/upadhmānīya alternatives,
the separate deletion vārttika, written visarga origin, original pada topology
through coalescence, and the 35 remaining legacy composition callers. These
require rule-choice or occurrence/provenance modeling, not character-based fixes.
The Ayogavaha enum and codec already support jihvāmūlīya/upadhmānīya; 8.3.37
therefore needs correct retention/allophone alternatives and precedence, not new
phonological types. The
[Kāśikā](https://sa.wikisource.org/wiki/काशिका/अष्टमोऽध्यायः/तृतीयः_पादः)
explicitly preserves the 8.3.35 exception and excludes the ordinary s branch in
the general 8.3.37 domain.

## Intermediate 8.3.37 / prescribed alternatives checkpoint

8.3.37 now has two actual prescribed outcomes, the corresponding existing
Ayogavaha allophone or retained visarga. Both carry the rule trace and exclude
8.3.34. `hasAlternatives`, `applyAll`, explicit `alternativeChoices`, and
application-level frontier bookkeeping distinguish these from an optional rule
being omitted. Replay enumerates choice prefixes rather than retaining invalid
later context-dependent choice indices. Eight native cases raise the total to
332; focused allophone, retention, independent-choice and generic three-outcome
tests passed at the intermediate checkpoint.

Intermediate corpus results before the latest internal-rule changes:

| Corpus | Default pass / fail | Alternatives pass / fail | Errors / skips |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 936 / 1,759 | 1,204 / 1,491 | 4 / 1 |
| UoH | 5,042 / 4,322 | 5,818 / 3,546 | 0 / 4 |

These are not a verified final checkpoint. Correcting independent Tripādī
ordering exposed the legacy 8.2.80 whole-form shortcut in the full suite.
It has been replaced with exact d/following-occurrence substitutions and
duration preservation. Latest applicability uses explicit 7.2.102 provenance
instead of confusing an incorporated sup's terminal s/r with the lexical head.
The existing 8.2.81 plural-e specialization remains folded into 8.2.80 and still
requires separate modeling. Sources:
[8.2.80](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.2.80.htm),
[8.2.81](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.2.81.htm).

At this intermediate checkpoint, verification was **not executed**: automatic approval review
failed because of an account usage limit. Do not treat the latest 8.2.80 edits
as tested or this batch as complete. `git diff --check` passes. No commit.
Resume with focused `AdasMutvaVarnaTest`, `PronominalStemsTest`,
`KupuVisargaSandhiTest`, `PrescribedAlternativesTest` and native benchmarks, then
the full suite and both corpus modes. The corpus harness now also writes
`error-groups-*.txt` so the four remaining errors can be examined without
changing source inputs or matching normalization.

## Verified 8.3.37 / prescribed alternatives / exact 8.2.80 batch

The approval path became available again. All pending focused tests and the full
suite have now run successfully against the latest code. 8.3.37 uses the existing
phonological Ayogavaha types for jihvāmūlīya and upadhmānīya, with retained
visarga as a second actual prescribed outcome. Neither admits an ordinary
s-substitution; 8.3.35 still excludes both allophones in its protected domain.

Shared abstractions: prescribed `applyAll` outcome enumeration (separate from
optional omission), explicit `alternativeChoices`, context-dependent choice
prefix replay, and per-application non-advancing frontier provenance. Independent
Tripādī operations now retain textual order rather than granting unrelated later
apavādas global scheduling priority. This exposed and allowed migration of the
legacy 8.2.80 whole-form shortcut to exact phonological occurrences, preserving
vowel duration, annotations, incorporated sup provenance and completed stage.
Its existing plural-e 8.2.81 specialization is still explicitly deferred for
separate rule modeling; no claim of full 8.2.81 implementation is made.

| Corpus | Default passes before → after | Alternatives passes before → after | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 937 → 936 | 1,171 → 1,204 | 1,759 / 1,491 |
| UoH | 5,042 → 5,042 | 5,818 → 5,818 | 4,322 / 3,546 |

Errors/skips remain 4/1 and 0/4. Strict matches: 664/683 and 4,622/4,664
(default/alternatives). First-choice allophone output is distinct from ordinary
visarga spelling; alternatives retain the actual visarga outcome. No matching
normalization, corpus data, or default choice policy was changed to improve scores.

Verification: seven Kupu/visarga tests, four prescribed-outcome tests, four exact
adas tests, existing pronominal/engine regressions, and all 332 native sandhi
benchmarks pass. The complete `gradlew test --no-parallel` suite passes (1 minute
38 seconds); both corpora were rerun in both modes against this final code, and
`git diff --check` passes. No commit.

The new text diagnostics identify all four Aṣṭādhyāyī errors as parser rejection
of parenthesized input components (rows 903, 920, 2500, 2506), not rule cycles.
Examples include `(शकलकर्दमात्)` and `(संज्ञायाम्)`. The source components were
not stripped or treated as silent phonological material. Their metalinguistic
interpretation still needs an explicit corpus-input model. UoH has no errors.

Remaining scope: these source annotations; separate 8.2.81 and deletion-vārttika
modeling; additional visarga exceptions and lexical-origin provenance; original
pada boundaries through coalescence; and the 35 legacy string-returning
composition callers. The wider migration/benchmark goal remains incomplete.

## Explicit pause context and 8.4.56, 2026-10-14

8.4.56 was present but its unspecified stage excluded it from external sandhi.
It now belongs to SANDHI and requires typed `SpeechBoundary.AVASANA` rather
than inferring a pause from the end of the term list. `SandhiEngine` defaults
to CONTINUATION and accepts an explicit final boundary; standalone derivations
retain their historical pause-ending default. The sequence evaluator supplies
AVASANA only at the final join, never at internal joins.

8.3.15 now honors the same context when no following sound exists, while a
known KHAR follower still licenses visarga in continuation. It uses the shared
phonological follower lookup through soundless terms and selects an eligible
occurrence rather than letting an ineligible first candidate hide another one.
Written visarga input is not reverse-mapped into an assumed underlying origin.

Shared `carStopCounterpart` gives the voiceless unaspirated member of each stop
class to 8.4.55/56. The arbitrary dental fallback is removed; nasal/semivowel
inputs have no such stop counterpart. All substitutions use exact Varṇa
positions and preserve annotations and written signs.

Sources: [Kāśikā 8.4.56](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.56.htm),
[8.4.55](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.55.htm),
and [8.3.15](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.15.htm).
Pause examples include vā k/vā g and triṣṭup/triṣṭub; the corpus regression
āpaḥ + tāvat now has āpastāvat and āpastāvad when a final pause is explicit.

### Comparable continuation results

The existing input scenario and counts remain unchanged in both modes:

| Corpus | Default passes before → after | Alternatives passes before → after | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 936 → 936 | 1,204 → 1,204 | 1,759 / 1,491 |
| UoH | 5,042 → 5,042 | 5,818 → 5,818 | 4,322 / 3,546 |

Strict matches remain 664/683 and 4,622/4,664 (default/alternatives).

### Separately evaluated final-pause scenario

This supplies additional grammatical context, not a new normalization policy.
It must not replace the comparable continuation figures above silently.

| Corpus | Default pass / fail | Alternatives pass / fail | Gain versus continuation default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī | 986 / 1,709 | 1,259 / 1,436 | +50 / +55 |
| UoH | 5,133 / 4,231 | 5,942 / 3,422 | +91 / +124 |

Strict matches: 706/727 and 4,700/4,747 (default/alternatives). Errors/skips
remain 4/1 and 0/4 in either scenario. Source text and whitespace/NFC matching
are unchanged. Pause reports have an `-avasana` suffix so prior reports remain
separate. To reproduce, add `-PsandhikoshFinalBoundary=AVASANA` to either corpus
command (and `-PsandhikoshAlternatives=true` for optional outcomes).

Verification: seven focused pause regressions, existing sequence/rutva tests,
and all 338 native sandhi benchmarks pass. All eight combinations of corpus,
endpoint and alternative mode ran successfully. The complete
`gradlew test --no-parallel` suite passes (1 minute 18 seconds), and
`git diff --check` passes. No commit.

Remaining scope is unchanged: explicit handling of the four source annotations,
separate 8.2.81 and deletion-vārttika modeling, visarga-origin and original-pada
provenance through composition, and 35 legacy composition callers whose
string-returning operations still need exact occurrence/it maps.

## Independent 8.2.81 and related 1.1.12, 2026-10-14

8.2.81, एत ईद्बहुवचने, is now independently registered rather than folded into
8.2.80. Shared `AdasMutvaTarget` preserves the exact source domain and d/following
occurrences. 8.2.80 supplies the general u grade; 8.2.81 explicitly overrides it
for e in semantic BAHUVACANA, including after the sup is incorporated. It
supplies both m and ī under its own trace, with no artificial 8.2.80 application.
[Kāśikā/Nyāsa 8.2.81](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.2.81.htm)
explicitly distinguishes semantic plurality from a currently present plural
ending.

The related missing 1.1.12, अदसो मात्, now designates completed adas ī/ū directly
after m as pragṛhya. It uses preserved adas lexical identity, not a guess from
the spelling amī/amū. It does not designate internal stems before an active
sup, unrelated lexical m, or e/intervening-consonant forms such as amuke.
The precise 1.1.12 visibility exception after 8.2.80/81 follows the
[Nyāsa/Bālamanoramā](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.1.12.htm)
explanation of vacanasāmarthya despite pūrvatrāsiddham. Other early rules are
not generally reopened. The producer establishes pragṛhya; a completed-pada
handoff retains it and existing 6.1.125 preserves hiatus in amī atra.

Native cases can now state `leftUpadesha`/`rightUpadesha` explicitly. Six new
source-backed rows cover amī atra, amī īśāḥ, amū atra, amū āsāte and the negative
śamī/amuke examples, bringing the native total to 344. Seven mutva tests and
six new adas-pragṛhya tests verify exact loci, conflict traces, semantic
applicability, annotations, actual subanta production and completed-word handoff.

### Corpus comparison (before → after)

| Corpus / endpoint | Default passes | Alternatives passes | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī / continuation | 936 → 936 | 1,204 → 1,204 | 1,759 / 1,491 |
| UoH / continuation | 5,042 → 5,042 | 5,818 → 5,818 | 4,322 / 3,546 |
| Aṣṭādhyāyī / avasāna | 986 → 986 | 1,259 → 1,259 | 1,709 / 1,436 |
| UoH / avasāna | 5,133 → 5,133 | 5,942 → 5,942 | 4,231 / 3,422 |

Strict matches, errors and skips are unchanged from the preceding batch.
All eight combinations were rerun. A scan of trimmed source split-components
found no standalone amī/amū before a following component in either imported
corpus; these new native cases fill an actual coverage gap, not a claimed
external score gain. No origin was guessed or added to imported rows, and no
source text or matching normalization changed.

Verification: focused tests, existing pronominal/pragṛhya regressions and all
344 native benchmarks pass; the complete `gradlew test --no-parallel` suite
passes (1 minute 17 seconds), and `git diff --check` passes. No commit.

Remaining scope: the four source annotations, deletion-vārttika modeling,
visarga-origin and original-pada provenance through composition, and 35 legacy
string-returning composition callers. Separate 8.2.81 modeling is no longer an
outstanding item; the wider goal remains incomplete.

## Additional missing-rule audit, 2026-10-14

Registry and implementation search confirms that 8.3.26 (हे मपरे वा),
8.3.27 (नपरे नः), 8.3.28 (ङ्णोः कुक्टुक् शरि),
8.3.29 (डः सि धुट्), and 8.3.30 (नश्च) are absent. Their
[Kāśikā commentaries](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.26.htm)
prescribe narrowly conditioned alternatives, not general nasal assimilation.
8.3.28 adds a preceding-pada-final augment; 8.3.29/30 add an augment
to the following pada. Placement, raw it processing, and optional decline
must therefore be modeled explicitly before registering these rules.

A spelling-based candidate scan of the unchanged imported JSON found no
m + h(m/n/y/v/l), ṭ/ḍ + s, or ṅ/ṇ + ś/ṣ/s boundaries in UoH,
and no first-two patterns in Aṣṭādhyāyī. Aṣṭādhyāyī has three
ṅ/ṇ + sibilant candidate rows (391, 1954, 2409) and two n + s candidate
rows (891, 1263). These are candidate counts, not confirmed grammatical
applications or predicted score gains: quoted affixes and metalinguistic
context require inspection. Both augmentation and retention alternatives
need native source-backed tests even when imported data expects retention.

The largest UoH alternative-mode failure group remains VISARGA + A
(759 rows). 6.1.113 and 6.1.114 are already implemented and registered;
these failures must not be labeled missing-sūtra coverage. Written visarga
has lost the distinction between ru and lexical r. The
[6.1.113 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.113.htm)
explicitly requires the ru marker and contrasts lexical r. Restoring this
provenance and checking rule precedence is the higher-impact benchmark
priority, not adding duplicate utva rules or guessing every visarga's origin.

This is a read-only implementation/corpus audit plus documentation. No
new rule was implemented, no benchmark rerun or score change is claimed,
and the in-progress pararūpa migration remains unverified as a batch.

## Exact pararūpa composition, 2026-10-14

6.1.94 (एङि पररूपम्) and 6.1.97 (अतो गुणे) now use
`pararupaAtBoundary`: replace the exact preceding vowel using the following
vowel's annotation donor, delete that following occurrence, then concatenate
the surviving annotated terms. Applicability and survivor identity are unchanged.
The consumed original form and affix-vowel locus are retained; in भव + अन्ति
the incorporated first affix vowel is one vowel from the end, not the final इ.
Existing designation projection and deferred-it transfer are reused rather
than rebuilding a surface string in the rule.

Five focused regressions pass: the affix locus, following nasalization and
unaffected nasal vowels, plain following-vowel dominance, identical-vowel
consumption, written-sign projection, and prefix/root lifecycle identity
(some tests contain multiple assertions). The complete `gradlew test
--no-parallel` passes, including these tests. No commit.

There are now 33 rule files with 34 legacy composition call sites. Remaining
vowel-sensitive cases include 6.1.88's augment-survivor and exact it-consumption
mapping, and 6.1.93's literal go-result construction plus the narrower domain
required for downstream 6.1.103. These must not be migrated by merely hiding
string construction or broadening a predicate. Written-visarga origin and
original-pada topology through vowel composition remain separate open models.

All eight corpus/endpoint/alternative evaluations were rerun successfully.
Before → after passes are unchanged: Aṣṭādhyāyī continuation 936 → 936
(alternatives 1,204 → 1,204), avasāna 986 → 986 (1,259 → 1,259);
UoH continuation 5,042 → 5,042 (5,818 → 5,818), avasāna 5,133 → 5,133
(5,942 → 5,942). Failures, strict matches, errors and skips also remain
unchanged from the preceding verified tables. `git diff --check` passes.

## Missing 8.3.30 and internal 8.4.55, 2026-10-14

8.3.30 (नश्च) now optionally introduces raw धुँट् on the following
s-leading completed pada after a pada-final n. Existing 1.3.2/3/9 and
1.1.46 process its exact it designations and establish initial placement.
The preceding n remains pada-final. Existing 8.4.55 now examines internal
as well as inter-term adjacent Varṇas, giving dh → t before s after incorporation.
It reuses `firstVarnaAfterTerm` across soundless terms and exact single-occurrence
replacement. No direct ts output or later-to-earlier rutva reopening is added.

[Kāśikā/Nyāsa 8.3.30](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.30.htm)
supports bhavān tsāye / bhavān sāye and explicitly explains the lack of renewed
8.3.7. Four focused tests cover both branches, placement/provenance, negative
nasal/follower and non-pada domains, and internal khari's exact annotation-preserving
locus. Two native source-backed benchmark rows cover application and decline,
bringing the native total to 346. All pass, as does the complete
`gradlew test --no-parallel` suite and `git diff --check`.

All eight imported-corpus scenarios were rerun without changing inputs or
normalization. Aṣṭādhyāyī continuation default passes 936 → 935, failures
1,759 → 1,760; avasāna default passes 986 → 985, failures 1,709 → 1,710.
Alternative-inclusive counts remain 1,204/1,491 and 1,259/1,436.
Strict matches remain 664/683 (continuation) and 706/727 (avasāna).
UoH counts remain unchanged in all modes: continuation default 5,042/4,322,
alternatives 5,818/3,546; avasāna default 5,133/4,231, alternatives 5,942/3,422.
Errors/skips remain 4/1 and 0/4. This introduces a new default optional branch,
not an alternative-inclusive corpus score gain; the one default loss is reported
rather than tuned away with corpus-specific selection.

8.3.29 is still missing: its retroflex source requires the explicit
न पदान्ताट्टोरनाम् restriction discussed in the
[8.3.29 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.29.htm)
before it can safely share this augment lifecycle. 8.3.26–28, visarga-origin,
original-pada topology and the 33 legacy composition rule files remain open.
No commit; overall goal remains incomplete.

## Missing 8.3.26/27, 2026-10-14

8.3.26 (हे मपरे वा) and 8.3.27 (नपरे नः) are now independently
registered. Shared `haClusterBoundary` selects pada-final m and the immediately
following h+m or h+n Varṇas, skipping soundless stored terms without ignoring
intervening vowels. 8.3.26 is an identity-retention branch: it protects only
that boundary from 8.3.23 and does not advance the Tripādī frontier.
8.3.27 replaces the exact final m with n. Declining either rule leaves
8.3.23 applicable. Its target selection now consistently respects those
boundary-scoped protections in both matches and apply.

[Kāśikā 8.3.26](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.26.htm)
and [8.3.27](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.27.htm)
support kim hmalayati / kiṃ hmalayati and kin hnute / kiṃ hnute.
The separately stated y/v/l-before-h vārttika in the 8.3.26 commentary is
not silently folded into the numbered rule and remains to be modeled.

Five focused tests pass, covering both alternatives and traces, negative
cluster conditions, absence of a pada designation, and protection isolation
from a second m-boundary. Four native source-backed rows bring the benchmark
total to 350, all passing. The complete `gradlew test --no-parallel` suite
passes (1 minute 19 seconds). No commit.

All eight corpus scenarios completed successfully with unchanged before →
after counts for this batch. Aṣṭādhyāyī continuation default 935 → 935
(fail 1,760), alternatives 1,204 → 1,204 (fail 1,491); avasāna default
985 → 985 (fail 1,710), alternatives 1,259 → 1,259 (fail 1,436).
UoH continuation default 5,042 → 5,042 (fail 4,322), alternatives
5,818 → 5,818 (fail 3,546); avasāna default 5,133 → 5,133 (fail 4,231),
alternatives 5,942 → 5,942 (fail 3,422). Strict matches, errors and skips
are unchanged. `git diff --check` passes. Missing 8.3.28/29, separately
stated vārttikas, visarga-origin, original-pada topology and 33 legacy
composition files remain; the overall goal is not complete.

## Missing 8.3.29 and prerequisite 8.4.42, 2026-10-14

8.3.29 (डः सि धुट्) now shares `dhutTarget`/`introduceDhut` with
8.3.30 while retaining its narrower pada-final ḍ domain. Raw धुँट् is
processed and placed on the following pada through the existing exact it
and augment lifecycle. 8.4.42 (न पदान्ताट्टोरनाम्) independently protects
following sto from a preceding pada-final retroflex stop. Its nām exception
uses typed genitive-plural AM_6 identity with an initial n; a lexical word
spelled nām does not acquire that exception. This guard is identity-only
and does not advance the Tripādī frontier. The separately stated navati/
nagarī exceptions in the commentary remain vārttika work, not implemented
as untraced output exceptions.

Sources: [Kāśikā/Nyāsa 8.3.29](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.29.htm)
and [Kāśikā 8.4.42](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.42.htm).
Two prerequisite issues were exposed by the first failing end-to-end test:
8.2.39 had skipped external jaś before khar, hiding the ḍ required by
8.3.29; conflict matching recognized only substitutions on the preceding
word. External jaś now also precedes later khar devoicing. Boundary conflicts
also recognize an explicitly recorded following initial occurrence, without
blocking unrelated internal occurrences in that following carrier.
8.4.41 respects the resulting boundary protection when selecting its locus.

Four new focused tests and four existing dhut tests pass. They cover both
ṣaṭ tsantaḥ / ṣaṭ santaḥ branches, following-augment provenance, the typed
nām exception, internal versus pada-final triggers, the sibilant negative,
and continued internal retroflexion behind a protected initial occurrence.
Two source-backed native rows bring the native total to 352, all passing.
The complete `gradlew test --no-parallel` passes (1 minute 21 seconds).
All eight corpus evaluations completed successfully. No commit.

| Corpus / endpoint | Default passes before → after | Alternatives passes before → after | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī / continuation | 935 → 938 | 1,204 → 1,207 | 1,757 / 1,488 |
| Aṣṭādhyāyī / avasāna | 985 → 988 | 1,259 → 1,262 | 1,707 / 1,433 |
| UoH / continuation | 5,042 → 5,042 | 5,818 → 5,818 | 4,322 / 3,546 |
| UoH / avasāna | 5,133 → 5,133 | 5,942 → 5,942 | 4,231 / 3,422 |

Aṣṭādhyāyī strict matches improve 664/683 → 666/685 (continuation)
and 706/727 → 708/729 (avasāna). UoH strict matches are unchanged.
Errors/skips remain 4/1 and 0/4; input text and normalization are unchanged.
These are combined-batch gains, not an attribution to 8.3.29 alone.
`git diff --check` passes. Remaining work includes missing 8.3.28,
separate vārttikas, visarga-origin and original-pada topology, plus 33 legacy
composition files. The full goal remains incomplete.

## Missing 8.3.28 and scoped introduced upadeśa, 2026-10-14

8.3.28 (ङ्णोः कुक्टुक् शरि) now introduces raw कुक्/टुक् for the
corresponding pada-final ṅ/ṇ before śar. Exact 1.3.2/3 designations and
1.1.46 kit placement make the augment a final member of the preceding
pada. Existing 8.4.42 protects the following dental s after ṭ. Source:
[Kāśikā/Nyāsa 8.3.28](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.28.htm).
The separately prescribed optional aspirated forms under the Pauṣkarasādi
vārttika remain unimplemented; they are not silently conflated with the
numbered rule or generated by bypassing khari ca.

Carrier-preservation tests exposed a shared lifecycle issue. `DerivationTerm`
now has `itUpadeshaVarnaIndices`: exact newly operative upadeśa occurrences
after raw augmentation of an already-processed carrier. 1.1.46 projects
the carrier and augment scopes by Varṇa position. The it-designation rules
honor that scope; 1.3.9 clears it after lopa, and consume/fresh-upadeśa
policies reset it explicitly. The completion gate rejects leftover scopes.
This protects already-pronounced nasal vowels and existing initials rather
than declaring the entire carrier to be a fresh upadeśa.

Initial-it rules 1.3.5/6/8 now also isolate pending terms, as 1.3.7 already
did, so adding a raw augment cannot reopen an unrelated processed carrier
before placement. Focused coverage includes kit and tit insertion, processed
affix and dhātu initials, nasal-vowel preservation, optional retention,
the following sibilant domain and downstream optional cha.
Four native source-backed rows bring the native total to 356.

All seven focused tests and all 356 native benchmark cases pass. All eight
imported-corpus evaluations completed with unchanged before → after counts:
Aṣṭādhyāyī continuation default 938 → 938 (fail 1,757), alternatives
1,207 → 1,207 (fail 1,488); avasāna default 988 → 988 (fail 1,707),
alternatives 1,262 → 1,262 (fail 1,433). UoH continuation default
5,042 → 5,042 (fail 4,322), alternatives 5,818 → 5,818 (fail 3,546);
avasāna default 5,133 → 5,133 (fail 4,231), alternatives 5,942 → 5,942
(fail 3,422). Strict matches, errors and skips remain unchanged. No input
or matching policy was altered and no corpus-score gain is claimed.

The final complete `gradlew test --no-parallel` run passes (1 minute
28 seconds), including the final cha and completion-gate regressions.
`git diff --check` passes. No commit. The audited numbered 8.3.26–30
gaps are now implemented, but the independent vārttikas, written-visarga
origin, original-pada topology and 33 legacy composition files remain.
The overall goal remains active and incomplete.

## Exact neighbouring s-stem composition, 2026-10-14

`StoNgeSutra`, `StoNgiSutra`, and `StoOsiSutra` no longer construct a rendered
whole-term result for the legacy merger. Each replaces the exact final s
with an explicit Varṇa list and consumes the following sup through
`substituteFinalVarnaAndConsumeFollowingTerm`. The helper now optionally
accepts an annotation donor; these rules select the following affix's vowel
so its nasalization survives without altering earlier stem vowels.
Current sup identity uses `hasCurrentAffix` and typed NGE/NGI/OS_6/OS_7,
not historical sthānin identity or an arbitrary lexical term with the same
upadeśa spelling. Existing case/number/gender predicates remain unchanged.

Three new focused tests cover four sup slots, plain results/stages, exact
source indices, stem identity and created-by metadata, dropped affix origin
and it provenance, the incorporated vowel locus, surviving stem written
signs/nasal vowels and the following vowel donor. Two existing complete
s-stem paradigms also pass. The complete `gradlew test --no-parallel` suite
passes. No commit.

There are now 30 rule files containing 31 legacy composition calls. Remaining
cases include grade/augment composition, n/ṛ/s-stem range substitutions,
neuter/pronominal formation, and the already identified visarga-origin and
original-pada models. Arbitrary written signs inside a wholly consumed affix
still need an explicit source-to-replacement boundary map; retaining its
original form in the drop ledger is not proof of final-render sign projection.
The focused sign assertions here cover the surviving stem, not that open
consumed-affix mapping problem. The overall goal remains incomplete.

All eight corpus scenarios completed successfully with unchanged before →
after counts: Aṣṭādhyāyī continuation default 938 → 938 (fail 1,757),
alternatives 1,207 → 1,207 (fail 1,488); avasāna default 988 → 988
(fail 1,707), alternatives 1,262 → 1,262 (fail 1,433). UoH continuation
default 5,042 → 5,042 (fail 4,322), alternatives 5,818 → 5,818
(fail 3,546); avasāna default 5,133 → 5,133 (fail 4,231), alternatives
5,942 → 5,942 (fail 3,422). Strict matches, errors and skips are unchanged.
No corpus text or normalization changed. `git diff --check` passes.

## Explicit consumed-affix written-sign projection, 2026-10-14

`substituteFinalVarnaAndConsumeFollowingTerm` now requires an explicit decision
for every written sign in the consumed affix: project it to a replacement-relative
Varṇa boundary or explicitly consume its glyph qualifier. It validates complete
source coverage and replacement bounds, renders at the term boundary, and verifies
that sign projection has not changed the resulting phonological Varṇa list.
No sign is converted to a phonological Varṇa and no source corpus is normalized.

Shared `consumedVowelAffixSignProjection` is used by all eight current callers:
StoNge/Ngi/Osi and the five Ghi composition rules. Avagraha before/after the
incorporated vowel follows that exact replacement locus; a terminal sign follows
the complete replacement. Glyph qualifiers on wholly consumed affix consonants
are explicitly consumed rather than attached to a different glyph such as
visarga. Nasalization remains governed by the caller's annotated vowel policy,
not by copying a detached chandrabindu. The full original affix remains in its
drop ledger in every case.

Six new tests plus thirteen existing composition regressions pass. They cover
terminal and internal avagraha, source/target Varṇa counts and exact trace loci,
nasal-vowel donation, distinct signs from both operands at a shared target
boundary, consumed joiner provenance, malformed/missing projection rejection,
and a Ghi caller. The complete `gradlew test --no-parallel` suite passes.
There are still 30 legacy composition files; this batch closes the previously
documented consumed-affix projection gap for these eight callers, not all
composition paths. Existing deduplication elsewhere still needs a multiplicity
audit so later transformations cannot collapse repeated same-boundary signs.
All eight corpus evaluations completed successfully. Before → after passes
are unchanged: Aṣṭādhyāyī continuation 938 → 938 (alternatives 1,207 → 1,207),
avasāna 988 → 988 (1,262 → 1,262); UoH continuation 5,042 → 5,042
(5,818 → 5,818), avasāna 5,133 → 5,133 (5,942 → 5,942).
Failures, strict matches, errors and skips are unchanged from the preceding
tables. `git diff --check` passes. No commit; the overall goal remains active.

## Remaining vowel-extending s-stem endings, 2026-10-14

`StoTaSutra`, `StoAamSutra`, and `StoNgasyosSutra` now use exact final-s
composition rather than rendered whole-term merger strings. Current sup
identities are typed TA, AM_6, NGASI and NGAS; case/number/gender predicates
and ordinary outputs/stages are unchanged. Shared `extendSStemAndConsumeAffix`
also serves the previously migrated NGE/NGI/OS rules, retaining the final s,
donating the affix vowel's annotations, explicitly projecting written signs,
and preserving the dropped affix lifecycle and vowel locus.

The affix-sign policy now accepts an explicit retained-consonant occurrence
map. When the complete affix tail exactly equals the prescribed ending tail,
surviving consonant glyph qualifiers follow those exact occurrences. Thus m
in ām retains its joiner/non-joiner, whereas s replaced by visarga does not
transfer a dead-consonant shaping request to visarga. This is phonological
occurrence mapping, not rendered-output repair. Obsolete `extendFinalS`, which
returned an entire replacement Varṇa list for string rendering, is removed.

Five shared migration tests now cover eight sup slots, with two complete
existing paradigms and six consumed-sign tests: all thirteen pass. New checks
cover retained-m joiners/non-joiners and terminal affix avagraha, alongside
identity, provenance, exact loci, nasal annotations and negative applicability.
The complete `gradlew test --no-parallel` suite passes. There are now 27 rule
files with 28 legacy composition calls. No commit.

All eight corpus reruns completed successfully. Before → after pass counts are
unchanged: Aṣṭādhyāyī continuation 938 → 938 (alternatives 1,207 → 1,207),
avasāna 988 → 988 (1,262 → 1,262); UoH continuation 5,042 → 5,042
(5,818 → 5,818), avasāna 5,133 → 5,133 (5,942 → 5,942). Failures,
strict matches, errors and skips are unchanged from the preceding tables.
`git diff --check` passes. Remaining range/grade compositions, independent
vārttikas, written-visarga origin, original-pada topology and written-sign
multiplicity still need work. No commit; the full goal remains active.

## Original-pada ownership through vowel composition, 2026-10-15

Completed-word handoff now assigns an origin set to each cached phonological
Varṇa. Exact replacements, range coalescence, deletions, insertion, prefix
resegmentation, designated-it mapping/lopa and concatenation project those
occurrences. A vowel shared by two original padas carries both origins;
unaffected following sounds retain only their own original origin. Consumed
terms retain ownership snapshots paired with their original dropped surfaces.
Handing an already-composed word into another join preserves those sets rather
than relabeling its entire rendered string as one original word.

8.4.1/2 now check original occurrence membership for samānapada, not merely
whether two sounds happen to occupy one stored term after sandhi. Internal
derivations without completed-word handoff retain their existing grammatical
PADA-endpoint logic. The
[Kāśikā/Nyāsa 8.4.1](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.1.htm)
requires the trigger and target in the same pada and explicitly excludes
agnir nayati; the restriction continues into
[8.4.2](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.4.2.htm).

The ordinary 6.1.88 path is also migrated to exact vowel replacement/deletion
and annotated concatenation, preserving original-pada ownership. Historical
it-marker presence no longer deletes an arbitrary pronounced final consonant.
An actual transferred deferred designation instead makes the composed carrier
await exact 1.3.9 lopa. Its operative upadeśa scope is empty, so its pronounced
nasal vowels/initials are not newly designated. This fixed the initially exposed
dvau regression without restoring the blanket consonant-deletion heuristic.
The old string merger rejects a tracked consumed pada instead of fabricating
ownership for an unmodeled composition.

Verification: six core projection tests, twelve natva/pada tests and the numeral
regression pass; all 358 native benchmarks pass. Tests cover dirgha, guna and
vrddhi, shared vocalic-r grade occurrences, three-input handoff, original drop
snapshots, exact it lopa and retained historical-marker consonants. The full
UoH row 85 expected alternative is directly tested, not just its reduced
nigṛhya + ānaya boundary. Two native rows fill this boundary coverage gap.
The complete `gradlew test --no-parallel` suite passes, including its final rerun.

### Comparable corpus results (before → after)

| Corpus / endpoint | Default passes | Alternatives passes | Failures default / alternatives |
| --- | ---: | ---: | ---: |
| Aṣṭādhyāyī / continuation | 938 → 944 | 1,207 → 1,215 | 1,751 / 1,480 |
| Aṣṭādhyāyī / avasāna | 988 → 994 | 1,262 → 1,270 | 1,701 / 1,425 |
| UoH / continuation | 5,042 → 5,056 | 5,818 → 5,833 | 4,308 / 3,531 |
| UoH / avasāna | 5,133 → 5,147 | 5,942 → 5,957 | 4,217 / 3,407 |

Strict matches improve 666/685 → 671/690 and 708/729 → 713/734 for
Aṣṭādhyāyī; 4,622/4,664 → 4,634/4,676 and 4,700/4,747 → 4,712/4,759
for UoH (continuation / avasāna, default/alternatives within each pair).
Errors/skips remain 4/1 and 0/4. All eight evaluations completed with unchanged
source text and normalization; no underlying visarga origin was guessed.
`git diff --check` passes. No commit.

Remaining scope: 27 legacy composition files now contain 27 calls, including
the still-legacy augment branch of 6.1.88. Wider composition/annotation and
original-domain handling still requires audit; this batch is not proof of all
phonological consumers being domain-aware. Written-visarga origin, independent
vārttikas, sign multiplicity and the four metalinguistic corpus inputs also remain.
The full goal stays active and incomplete.

## Complete 6.1.88 augment-path migration, 2026-10-15

The remaining augment branch of `VrddhirEciSutra` now grades the exact initial
root vowel, consumes the exact preceding augment vowel, and retains the root
as survivor through atomic `DerivationState.coalescePrecedingAugmentVowel`.
Root annotations and unrelated phonological occurrences are not rebuilt from
a plain rendered Varṇa list. Written boundaries on the consumed augment are
projected onto the shared grade, and its complete original spelling, signs,
it provenance and original-pada ownership remain in the drop ledger.
The existing narrow at-agama domain now also verifies AGAMA kind, a/ā vowel,
target linkage and established beginning placement; it is not a broad ec-root
predicate. Identical grades still consume the augment under their own trace.

An empty, signless augment is the identity operand in
`concatenatePrecedingAugment`: the transformed target is retained without a
new rendering/deduplication pass. A regression verifies two independently
contributed signs at a shared boundary. This is not a global fix for the
remaining written-sign multiplicity work.

The first full run caught rendering mutation outside the established lifecycle
owner. The implementation was moved into DerivationState; the architecture
test was not weakened or given a new exemption. Six augment tests and that
architecture test pass, as does the complete `gradlew test --no-parallel`
suite, including all 358 native sandhi benchmarks. All eight corpus modes
were rerun after the final identity-composition change.

Before → after pass counts are unchanged: Aṣṭādhyāyī continuation 944 → 944
(alternatives 1,215 → 1,215), avasāna 994 → 994 (1,270 → 1,270);
UoH continuation 5,056 → 5,056 (5,833 → 5,833), avasāna 5,147 → 5,147
(5,957 → 5,957). Failures, strict matches, errors and skips remain as in the
preceding table. Input text and normalization are unchanged.
`git diff --check` passes. There are now 26 legacy composition files/calls.
No commit.

Related missing-rule audit: 6.4.72 (आडजादीनाम्) and 6.1.90 (आटश्च) have
no implementation or registration. The
[6.4.72 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.4.72.htm)
prescribes āṭ for vowel-initial verbal bases in the specified lakāras, and
[6.1.90](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.90.htm)
extends its vṛddhi domain to ac, not just ec. They require their own producer,
it/placement lifecycle and rule precedence; the a/ā operand tests here do not
claim that those rules or full āṭ derivations exist. 6.1.88 remains ec-specific.
The wider goal, including these gaps, written-visarga origin, independent
vārttikas, remaining grade/range compositions and sign multiplicity, stays active.

### Missing-rule follow-up: āṭ, 2026-10-15

Implemented and registered 6.4.72 (आडजादीनाम्) and 6.1.90 (आटश्च),
using the commentaries linked above. The former introduces a fresh target-linked
raw āṭ for vowel-initial verbal bases in luṅ/laṅ/lṛṅ, including replacement of
an existing aṭ when its target becomes vowel-initial. The latter coalesces a
processed, placed āṭ with any following vowel using exact Varṇa vṛddhi grades;
it does not broaden 6.1.88 or license arbitrary lexical ā.

Shared past-augment construction now records the prescribed udātta of both
6.4.71 and 6.4.72 as per-occurrence grammatical accent metadata, not a new Varṇa
or invented lexical accent. Exact token operations project this metadata;
coalescence transfers it to the surviving vowel and keeps the dropped source
snapshot. Svara preparation recognizes the prescribing sūtra as its source.
The verbal completion validator accepts the specific completed 6.4.72 → 6.1.90
path instead of incorrectly insisting that its general rule 6.4.71 also ran.

Focused `AtAugmentRulesTest` and `TingantaEngineTest` pass. The new tests cover
all 14 vowel grades, the three licensed lakāras, nonpast/consonant exclusions,
target identity, source accent snapshots, and rejection of unrelated augments.
The complete `gradlew test --no-parallel` suite passes, as does
`git diff --check`. This is a verbal-derivation coverage correction, not a claim
that the remaining imported external-sandhi failures are solved. The remaining
26 legacy composition calls, independent vārttikas, written-visarga origin,
sign multiplicity and metalinguistic input modeling still require work. No commit.

Both corpora were rerun in all eight continuation/avasāna and default/alternative
combinations. Pass counts remain unchanged: Aṣṭādhyāyī continuation 944
(alternatives 1,215), avasāna 994 (1,270); UoH continuation 5,056 (5,833),
avasāna 5,147 (5,957). Failure/error/skip and strict-match counts are unchanged
from the preceding verified batch. No benchmark normalization changed.

### Exact n-stem extensions and canonical-rule audit, 2026-10-15

Migrated `NtoNgeSutra`, `NtoNgiSutra`, `NtoNgasyosSutra`, `NtoOsiSutra`
and `NtoTaSutra` from rendered whole-stem mergers to exact final-consonant
extensions. Generalized the existing s-stem helper as
`extendConsonantStemAndConsumeAffix`; existing s-stem callers retain their
specific wrapper. Earlier stem occurrences remain untouched; the incorporated
affix vowel supplies its annotation, written signs are explicitly projected,
and affix identity, drop spelling, it provenance and vowel-locus linkage remain.
Applicability predicates are unchanged, including the instrumental handler's
narrow original consonant environment. There are now 21 legacy merger files/calls.

Focused n-stem derivation, n-stem annotation/lifecycle and shared s-stem
regressions pass (9 tests). New cases exercise seven sup slots across the five
handlers, nasal annotations, terminal signs and exact substitution positions.

Important coverage caveat: these existing handlers carry internal-looking
numbers 7.3.142–147, not verified canonical sūtra numbers. The
[closing 7.3 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.120.htm)
explicitly ends the pāda at आङो नाऽस्त्रियाम् and proceeds to 7.4. Their migration
does not establish canonical grammatical coverage; replacing the broader
synthetic n/s-stem family with authentic rule sequences remains necessary.
This batch preserves existing applicability rather than silently rewriting it
or counting these handlers as newly implemented Aṣṭādhyāyī sūtras.

Verification: full `gradlew test --no-parallel` passes; `git diff --check`
passes. All eight corpus modes were rerun. Before → after pass counts remain
Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215), avasāna
994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056 (5,833 → 5,833),
avasāna 5,147 → 5,147 (5,957 → 5,957). Failure/error/skip/strict counts are
unchanged. The large visarga groups still need explicit grammatical-origin
modeling rather than an inferred blanket s/r substitution. No commit.

### Explicit completed-pada visarga source, 2026-10-15

Added optional typed `VisargaSource.S` / `VisargaSource.R` on
`SandhiEngine.Pada`. A lexical or morphological analysis may explicitly supply
the pre-visarga source. At the input term boundary the final visarga token is
decoded to that source using the exact token operation, retaining the supplied
spelling and source in `CompletedPadaVisargaInput`. This is input analysis, not
a reversed grammatical rule: it fabricates no sūtra application, substitution
history or it-designation. The actual engine must still derive s → raw ru →
processed r → u, with the genuine 8.2.66 / 1.3.2 / 1.3.9 / 6.1.113–114 trace.
The spelling snapshot is historical input metadata, not an operative Varṇa.

Source decoding rejects non-visarga endings, repeated decoding and incomplete
it-processing. It preserves term identity, upadeśa, per-occurrence completed-pada
ownership, earlier vowel annotations and written signs. Source is never guessed
for the existing string overload or imported corpus. In particular, neither
an expected benchmark output nor a word-specific output table supplies it.

Corrected 8.3.15 to include lexical repha at an explicitly completed external
pada, not just rutva repha. It still requires khar or explicit avasāna and does
not broaden arbitrary internal stems. This follows the
[8.3.15 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.15.htm).
The rutva-only distinction in
[6.1.113](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.113.htm)
and [6.1.114](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.114.htm)
remains intact; lexical prātar/punar never acquire an invented 8.2.66 history.

Expanded native benchmark input with optional `leftVisargaSource` and
`rightVisargaSource` and eight source-annotated cases, including the cited
vṛkṣo 'tra / prātar atra / agnir nayati environments and additional source/rule
contrasts. All 366 native cases and six dedicated input-provenance/domain tests
pass, together with pause regressions. Imported source-ambiguous visarga
groups remain unresolved: their provenance needs actual lexical/morphological
analysis, not blanket normalization. No commit.

Full `gradlew test --no-parallel` and `git diff --check` pass. All eight imported
corpus modes were rerun. Before → after pass counts remain Aṣṭādhyāyī
continuation 944 → 944 (alternatives 1,215 → 1,215), avasāna 994 → 994
(1,270 → 1,270); UoH continuation 5,056 → 5,056 (5,833 → 5,833), avasāna
5,147 → 5,147 (5,957 → 5,957). Failures, errors, skips and strict counts are
unchanged. Native coverage increased from 358 to 366 cases. The remaining
21 legacy mergers, canonical replacement of synthetic declension handlers,
independent vārttikas, metalinguistic inputs and sign multiplicity remain open.

### O-stem coalescence and authentic śas sequence, 2026-10-15

Migrated 6.1.93 from a whole-stem merger that constructed literal gām/gāḥ to
exact final-o / initial-affix-a coalescence. It now retains arbitrary earlier
stem material, the actual am/śas consonants and their annotations; ordinary
rutva/visarga rules subsequently produce the śas ending. The
[Kāśikā](https://ashtadhyayi-lite.github.io/sutra/6.1.93.html)
explicitly gives both go and dyo examples, including dyām/dyāḥ. An au-ending
input is not treated as o, and typed sup provenance distinguishes nominal am
from its verbal homonym. No expected output supplies the substitution.

Migrated the existing combined 7.1.90 handler to exact final-o replacement
and current typed sup matching. It no longer reconstructs the whole stem or
uses Unicode surface comparisons. Its bundled vṛddhi action explicitly yields
to 6.1.93 before sup-am, including before it-processing finishes; otherwise it
would prematurely erase the o required for that mandatory exception. Splitting
this combined handler into canonical ṇit-atideśa and subsequent 7.2.115 remains
a distinct grammatical-modeling task, not claimed complete here.

6.1.103 now changes an exact final s only when the dropped, typed śas records
actual 6.1.102 coalescence into the surviving term. A merely long-vowel+s shape
is insufficient: the
[6.1.103 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.103.htm)
explicitly excludes gāḥ from this n-substitution. Full-suite testing exposed
synthetic 7.3.124 as the source of short-i/u accusative plurals. It was removed
from the executable registry; its deprecated class remains source-compatible,
not a runtime fallback. Short i/u before typed śas now use genuine
[6.1.102](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.102.htm)
followed by 6.1.103, as verified for agnīn/vāyūn and full kavi/ṛṣi/bhānu paradigms.
The jas exclusions remain intact rather than broadening every ik boundary.

Six new tests cover generic/compound stem material, nasal vowels, affix glyph
qualifiers, coalesced ownership, source affix/drop/vowel-locus provenance,
historical/incorrect affix exclusions, verbal-am exclusion, dyo derivations and
the genuine short-ik śas trace. Focused noun and Ghi regressions pass. Legacy
merger files/calls are down from 21 to 20. The larger synthetic r/n/s-stem family
and long-ik exception domains still need authentic modeling. No commit.

Final verification includes the typed sup-origin guard: full
`gradlew test --no-parallel` passes, including all 366 native sandhi cases;
`git diff --check` passes. All eight imported corpus modes were rerun.
Before → after pass counts are unchanged: Aṣṭādhyāyī continuation 944 → 944
(alternatives 1,215 → 1,215), avasāna 994 → 994 (1,270 → 1,270);
UoH continuation 5,056 → 5,056 (5,833 → 5,833), avasāna 5,147 → 5,147
(5,957 → 5,957). Failure/error/skip/strict-match counts are unchanged. No
input normalization or imported provenance annotations were introduced.

### Feminine / ṛ śas canonical replacement and registry verification, 2026-10-15

Extended 6.1.102's typed śas domain to the existing ik vowel inventory, short
and long. 6.1.103 recognizes long ṛ/ḷ as well, but still requires the actual
6.1.102 śas-source/drop linkage and masculine morphology. Feminine śas retains
s and reaches its visarga through genuine rutva and 8.3.15. The
[6.1.103 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.103.htm)
gives the ṛ examples and the feminine exclusion; the
[6.1.105 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.105.htm)
distinguishes long-vowel jas/ic prohibitions from śas-a. Dual/jas behavior was
not swept into the expanded śas predicate.

Removed synthetic 7.3.126 and 7.3.134 from the executable pāda registry. Their
deprecated classes remain compatibility source only, as with 7.3.124; none is
a runtime fallback. Full derivations now verify matīḥ, dhenūḥ, nadīḥ, vadhūḥ,
pitṝn and kartṝn through canonical traces. Token fixtures additionally cover
all eight existing ik length variants without claiming attested lexical forms
for each fixture. Exact coalescence preserves nasal marking, avagraha, retained
consonant glyph qualifiers, grammatical accent and the original affix ledger.
Five new canonical-sequence tests and existing noun/Ghi regressions pass.

At the user's request, checked all 18 newly added canonical rule objects against
the central registry. All were already connected through their pāda lists.
Added `SandhiMigrationRegistryTest`: two tests assert exact singleton identity,
single registration, executable/runtime membership, continued registration of
6.1.102/103, and exclusion of 7.3.124/126/134. These pass. The full suite passes
with the new tests, and `git diff --check` passes. No commit.

There remain 20 source files using the legacy merger; one (deprecated
`RtoShasiSutra`) is now excluded from execution, leaving 19 executable legacy
merger handlers. This is not claimed as complete source migration. The wider
synthetic declension family, explicit exception modeling, combined ṇit/vṛddhi,
visarga-source analysis, independent vārttikas, sign multiplicity and malformed
metalinguistic corpus inputs remain open.

All eight imported modes were rerun after the registry tests. Before → after
pass counts remain Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957). Failures, errors,
skips and strict counts are unchanged. All 366 native sandhi cases pass.

### User-directed deletion of synthetic rules and output shortcuts, 2026-10-15

This supersedes the preceding compatibility/green-suite checkpoints. Deleted
38 confirmed noncanonical classes: the invented 7.3.121–158 identities that
had implementations, plus the false NadyahNge/NadyahNgasi identities occupying
the genuine 7.3.117/118 numbers. Removed all their registry entries, including
the three formerly deprecated śas compatibility classes. Also deleted four
now-unused n/s/ṛ composition helper files and four obsolete unit-test files
that directly exercised those synthetic classes. Grammatical full-paradigm
expectations remain unchanged. Generic sign-projection coverage now tests core
operations as explicitly labeled fixtures, not fake sūtras.

Deleted three output-level shortcuts under real numbers as well:
`YusmadAsmadSutra` (7.2.86, full pronoun output table), `SavupadhayascaSutra`
(7.1.99, literal anaḍvān output) and `AdasAuSulopascaSutra` (7.2.107, combined
literal asau output). The actual sūtras are now implementation gaps rather
than falsely advertised complete derivations. No replacement lookup or
renamed runtime fallback was introduced. No sūtra implementation now calls
`mergeTermsByVarnaSubstitution`; that legacy core API still has test callers
and is not claimed removed from the codebase.

Added registry guards against out-of-range 7.3 identities and the removed
output shortcuts. Verified all remaining declared rule numbers against the
published [3,983-entry catalogue](https://ashtadhyayi.github.io/): no remaining
number was absent. Number validity alone is not authenticity proof. A text
comparison before the last three shortcut deletions found 116 mismatches;
these include spelling variants/errors and wrong mappings, requiring individual
review rather than blind automatic deletion. The genuine 7.3.117
[इदुद्भ्याम्](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.117.htm)
is not the deleted fabricated dative rule.

The full `test --no-parallel --continue` run fails in noun-derivation and
execution rendering tests that had depended on shortcuts. This is an exposed
coverage regression, not a successful full-suite checkpoint. Do not commit or
restore synthetic rules to make it green; implement authentic rule sequences.
Focused surviving-rule, registry and core-sign tests pass, as do all 366 native
sandhi cases. `git diff --check` passes. No commit.

All eight imported corpus modes were rerun after all 41 rule/shortcut classes
were deleted. Pass counts remain unchanged: Aṣṭādhyāyī continuation 944
(alternatives 1,215), avasāna 994 (1,270); UoH continuation 5,056 (5,833),
avasāna 5,147 (5,957). Fail/error/skip/strict counts are unchanged. These sandhi
results do not make the failing full noun/execution suite green.

Final exact `gradlew test --no-parallel` run reports 764 ashtadhyayi tests,
11 failures. The preceding full `--continue` run also exposes 5 execution
failures, totaling 16 remaining failures with unchanged grammatical assertions.
Affected full paradigms: feminine mati/bhūmi, dhenu/tanu, nadī/devī,
masculine pitṛ/bhrātṛ, neuter manas/payas, and the yad/kim/idam test.
Downstream failures concern feminine agreement/possession, ordinal collection
slicing, final-member extraction and reproducible example rendering.
The deletion checkpoint is intentionally red pending genuine replacements.
`git diff --check` passes. Tracked committed versions remain recoverable in Git;
no reset, commit, benchmark normalization or grammatical expectation weakening
was performed.

### Authentic nadī āṭ and nuṭ recovery, 2026-10-15

Added and registered genuine 7.3.112 आण्नद्याः from the
[Kāśikā](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.112.htm).
It introduces raw, target-linked āṭ for the ṅit affix after a nadī-designated
stem. The augment must undergo 1.3.3, 1.1.46 and 1.3.9; no final ending is
looked up or manufactured. Its repeat guard checks the augment/target lifecycle
including dropped sources, not a specific completed output.

Extended 6.1.90's authenticated producer domain to nominal 7.3.112 in addition
to verbal 6.4.72. Both use the same exact `coalescePrecedingAugmentVowel` core
operation. The nominal producer does not invent the verbal āṭ's prescribed
udātta. Unrelated lexical ā and unidentified augments remain excluded.
7.3.116 now substitutes exact `[AA, MA]`, preserves typed ṅi provenance, and
records its establishment so the locative substitution precedes nominal āṭ
without reapplication. No synthetic 7.3.117/118 identities were restored.

Corrected existing 7.1.54 to include its omitted nadī branch before typed
genitive-plural ām, preserving the short-vowel and āp branches. The
[Siddhāntakaumudī text](https://www.premabase.com/siddhanta-kaumudi/p08/index.html)
explicitly states the nadī domain. Its numeral tests now use cached Varṇa
lists rather than surface spelling, and the existing nuṭ guard is target-linked.
Also corrected 1.4.3 to require feminine interpretation rather than assigning
nadī solely from the final long vowel's shape.

Lifecycle testing exposed 1.1.46's merged-augment branch deleting the original
source record. It now archives the consumed augment and links it to its
target, while the exact transferred designations still undergo normal lopa.
The nuṭ source spelling and provenance therefore remain inspectable instead
of vanishing. This reuses the existing core lifecycle drop operation.

Four new tests verify raw āṭ, genuine marker/placement/vṛddhi/yaṇ traces,
locative order, nuṭ genitive plural and the gender restriction. Focused nadī,
Ghi, verbal-āṭ and registry tests pass. Both full 21-slot nadī/devī paradigms
now pass; native sandhi remains 366/366. Full `test --no-parallel --continue`
improves from 16 failures to 10: 9 noun-paradigm/pronominal failures and 1
downstream example-rendering failure. Four downstream connector regressions
also recover. The suite is still red; nothing is committed.

Remaining work includes authentic short-i/u optional nadī designation and its
locative exceptions, other 1.4.4 exception domains, ṛ/s-stem/pronominal rule
sequences, catalogue identity/text review, visarga-source analysis, independent
vārttikas, metalinguistic inputs and legacy core API removal. No deleted
synthetic or output-lookup handler was restored.

All eight corpus modes were rerun for this batch. Before → after pass counts
remain Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957). Fail/error/skip/strict
counts are unchanged. `git diff --check` passes. The exact final
`test --no-parallel` command still fails in the noun module; the full
`--continue` run above establishes the 10 outstanding failures across modules.

### Short-i/u optional nadī recovery and branch verification, 2026-10-15

Registered authentic 1.4.6 ङिति ह्रस्वश्च and 7.3.117 इदुद्भ्याम्.
The implemented 1.4.6 branch requires a feminine short-i/u stem and a
processed ṅit affix, and replaces residual Ghi designation with nadī only
when the optional branch is chosen. The separate long-vowel iyaṅ/uvaṅ
eligibility domain remains explicitly incomplete, not inferred from spelling.
Sources: [1.4.6 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.6.htm)
and [7.3.117 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.3.117.htm).

7.3.117 substitutes exact `[AA, MA]` for typed locative ṅi in the
short-i/u nadī branch. It retains the affix identity and source provenance;
7.3.112 then produces raw āṭ for normal marker processing and 6.1.90.
7.3.116 excludes this established substitution, preventing reapplication.
No deleted fabricated dative/ablative handlers were restored.

Two dedicated tests verify all eight mati/dhenu dative, ablative, genitive
and locative alternative sets through `deriveAll`, including the declined
Ghi branch, mutually exclusive designations, feminine/processed-ṅit guards,
and locative 7.3.117 → 7.3.112 → 6.1.90 → 6.1.77 order.
Focused feminine-i/u, augment lifecycle and registry tests pass. The four
full mati/bhūmi/dhenu/tanu paradigm failures recover. Full
`test --no-parallel --continue` now has six failures: five ṛ-stem/s-stem/
pronoun tests and one example-rendering test. The exact required
`test --no-parallel` run after the new branch tests reports 770 noun-module
tests, five failures, and stops there. Assertions were not weakened; no commit.

All eight imported corpus modes were rerun. Before → after passes are
unchanged: Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957).
The corresponding fail/error/skip/strict counts remain unchanged as well.
These nominal-coverage improvements do not imply corpus-score improvement.
`git diff --check` passes. Remaining work includes long-vowel designation
eligibility, authentic ṛ/s-stem and pronominal coverage, individual catalogue
text/mapping review, visarga-source provenance, independent vārttikas and
metalinguistic corpus-input modeling. The wider goal remains incomplete.

### Authentic ṛ-stem ablative/genitive coalescence, 2026-10-15

Added and registered 6.1.111 ऋत उत्, authenticated against the
[sūtrapāṭha](https://ashtadhyayi.github.io/suutra/6.1/) and
[Kāśikā commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.111.htm).
It requires short ṛ plus initial a of a processed, currently typed ṅasi/ṅas
with original sup identity. Exact `[U, RA]` realizes u-ekādeśa with the
interpretive 1.1.51 rapara requirement. The suffix's s remains for genuine
8.2.24 rather than being folded into a completed word substitution.

Reused exact coalescence/concatenation helpers, transferring the shared vowel's
accent and completed-pada ownership while archiving the original consumed
suffix. Shared concatenation now preserves an already established suffix
pada boundary on the completed combination; it does not create a designation
without evidence. 8.3.15 accepts that established newly derived pada as well
as external padas, still requiring khar or explicit avasāna.

Full testing exposed false conflict resolution: two sound transformations
that inherited the same pada designation were treated as competing saṃjñā
rules, causing 6.1.97 to override ordinary plural formation. Limited that
saṃjñā-intersection criterion to actual saṃjñā rules; explicit grammatical
blocking relationships remain unchanged. This fixes the cause rather than
adding output- or rule-number-specific scheduling exceptions.

Seven targeted tests cover exact intermediate sounds, typed-affix/gender-free
domain restrictions (short ṛ, not long ṝ), source identity, existing versus
absent pada boundaries, written avagraha projection, shared grammatical accent
and ownership, eight real ablative/genitive forms and ordinary plural regression.
Focused full pitṛ/bhrātṛ paradigms, registry, engine and pause/visarga tests pass.
Full `test --no-parallel --continue` has four remaining failures, down from six:
manas/payas full paradigms, yad/kim/idam forms and example rendering. Native
sandhi remains 366/366. The exact `test --no-parallel` gate also remains red,
stopping on the existing execution rendering failure. No expectations were
weakened and no commit was made.

The next s-stem investigation has concrete evidence: manas has missing neuter
am-lopa, num/anusvāra and pada-boundary operations; payas additionally acquires
an unsolicited yak before nominal inflection. This needs grammatical intent
and boundary modeling, not a replacement declension table.

All eight corpus reruns completed successfully. Before → after pass counts
remain Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957).
Fail/error/skip/strict counts are unchanged. `git diff --check` passes.
The full goal is still incomplete; this batch restores genuine nominal
coverage but does not improve imported sandhi scores.

### Yak domain and authentic neuter singular processing, 2026-10-15

Corrected 3.1.27 कण्ड्वादिभ्यो यक् to require the dhātu interpretation of
the lexical entry, not its prātipadika interpretation. Membership now uses
retained lexical upadeśa, not a repeatedly transformed surface, and the
repeat guard includes consumed affixes. The
[Kāśikā/Nyāsa](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/3/3.1.27.htm)
explicitly distinguish these interpretations. The prior unit test asserted
the wrong interpretation and now verifies the dhātu-positive/prātipadika-negative
domain. Two additional tests verify processed कण्डू retains its original
lexical identity and ordinary पयस् inflection no longer introduces verbal yak.

Added and registered authentic 7.1.23 स्वमोर्नपुंसकात् for complete su/am-luk
after a neuter stem, using typed current and original sup identities and
the existing lifecycle drop operation. It waits for pending 1.2.47 stem
shortening so that 7.1.24 can correctly apply to the resulting short-a stem.
Sources: [7.1.23](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.23.htm)
and [7.1.24](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.24.htm).

Removed 7.1.24's visible-m shortcut. It now substitutes complete `[A, MA]`,
retains the original sup identity, establishes its operation without cycling,
and blocks general 7.1.23. 6.1.107 no longer incorrectly excludes neuters;
it applies to processed am, with explicit precedence over general vowel
sandhi, using the exact existing coalescence helper. No final-word lookup
or concatenation of manufactured endings was added.

Indeclinable-specific 2.4.82 sup-luk now precedes nominal 7.1.23 and 6.1.107;
2.4.83's prescribed complete am precedes nominal su/am substitution/deletion.
One obsolete avyayībhāva trace assertion expecting the old visible-m step
was changed to require 2.4.83 → 6.1.107. Surface expectations are unchanged.
Three new neuter lifecycle tests verify complete am, original affix identity,
archived luk, gender restriction, ten singular forms and authentic traces.
Focused tests pass for these rules, kaṇḍvādi membership, registry, ordinary
nominal engine and avyayībhāva behavior. Full s-stem paradigms remain
incomplete: num/anusvāra and consonant-boundary operations are still missing.

Final full `test --no-parallel --continue` reports the same four outstanding
failures: three noun/pronoun tests and one execution rendering test. The exact
`test --no-parallel` gate reports 782 noun-module tests, three failures, then
stops. Native sandhi remains 366/366. All eight imported corpus reruns completed:
Aṣṭādhyāyī continuation passes 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957).
Fail/error/skip/strict counts are unchanged. `git diff --check` passes; no commit.

Next identified cause: 8.3.24 currently rejects every state containing any
pratyaya, preventing internal n+s → anusvāra in the still-separated
neuter plural. The guard's provenance and domain must be reviewed rather
than indiscriminately removing boundary protections. The full goal remains active.

### Internal anusvāra with retained affixes, 2026-10-15

Corrected existing 8.3.24 नश्चापदान्तस्य झलि: a retained pratyaya no
longer suppresses all internal nasal operations. The
[Kāśikā/Nyāsa](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.24.htm)
explicitly give पयांसि/यशांसि and their num-based sequence as examples.
Replaced the blanket term-kind exclusion with protection for unfinished
it-processing and unresolved exact/deferred designations. Existing actual
pada-boundary and jhal restrictions remain intact. The mutation reuses the
exact Varṇa operation; suffix identity and metadata are not changed.

Four new tests cover retained processed śi, original jas identity, raw-affix
protection, internal versus designated-pada boundaries, non-jhal rejection,
and six full manas/payas/yaśas plural derivations. Focused annotation, boundary,
cycle and compound tests pass. The manas/payas nominative and accusative
plurals now recover as मनांसि/पयांसि. Their remaining full-paradigm failures
are the consonant-initial bhyām/bhis/bhyas and su slots: existing output is
मनद्भ्याम्/मनस्सु rather than मनोभ्याम्/मनःसु, with corresponding payas forms.

A bahuvrīhi trace test assumed exactly one 8.4.58 across constituent and
completed-compound runs. It now requires every parasavarṇa operation to
change a term and any repeated application to have a fresh 8.3.24 between
occurrences. The grammatical output expectation is unchanged; focused
single-run cycle testing remains intact. No sūtra or registry identity was
invented, and no new shared abstraction was needed.

Final full `test --no-parallel --continue` has the same four outstanding
failures: three noun/pronoun tests and one example-rendering test. The exact
`test --no-parallel` gate stops at 786 noun-module tests with three failures.
Native sandhi remains 366/366. All eight corpus modes completed: passes remain
Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215), avasāna
994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056 (5,833 → 5,833),
avasāna 5,147 → 5,147 (5,957 → 5,957). Fail/error/skip/strict counts remain
unchanged. `git diff --check` passes. No commit; goal incomplete.

Confirmed next missing canonical rule:
[1.4.17 स्वादिष्वसर्वनामस्थाने](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.17.htm)
is absent from the registry. Its broader svādi domain, sarvanāmasthāna
exclusion and coordination with 1.4.18/internal rutva need explicit modeling;
adding a predicate solely for manas/payas outputs would not suffice.

### Svādi-pada designation and s-stem recovery, 2026-10-15

Added and registered genuine 1.4.17 स्वादिष्वसर्वनामस्थाने, following
[1.4.17](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.17.htm)
and [1.4.18](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.4.18.htm).
Shared `SvadiDomain` recognizes retained typed sup/taddhita sources and
affixes introduced in the chapter-four/five nominal suffix domain, not all
pratyayas. Added `sourceTaddhitaAffix` at the typed term-construction boundary;
it survives later substitutions through the existing immutable term lifecycle.
Affix spelling alone is not sufficient: a kṛt homonym must not be svādi.
Pending it-processing and pending neuter jas/śas-to-śi substitution are excluded
until their operative domain is established. Sarvanāmasthāna is excluded.

Corrected 1.4.18 to use the same svādi and sarvanāmasthāna conditions, require
a nominal stem, override 1.4.17 instead of the complete-word 1.4.14 designation,
and replace only the preceding stem's pada assignment with bha. Internal
8.2.66 now recognizes that genuinely designated pada before a svādi affix,
including typed taddhitas, while retaining separate completed-pada/numeral
domains. It still introduces raw ru for normal it-processing, not an output
replacement. No Unicode sound predicates were added.

Five new tests cover consonantal sup/taddhita, kṛt/tiṅ exclusion, shared-spelling
disambiguation, sarvanāmasthāna/śi exclusion, unfinished markers, retained typed
source, local bha/pada exclusivity and genuine 1.4.17 → 8.2.66 → 6.1.114 or
8.3.15 traces. Focused full 21-slot manas/payas paradigms and registry tests
pass: मनोभ्याम्/मनोभिः/मनःसु and their payas counterparts now recover.
The grammatical paradigm assertions remain unchanged. Separate vati/chāndasa
exceptions mentioned under 1.4.18 still need explicit eligibility modeling.

The previous 'pronoun' grouped test now fails specifically at numeral
पञ्चानाम्: current 7.1.55 produces पञ्चआनाम् and the test still names deleted
fabricated 7.3.146. This is an authentic numeral/augment lifecycle gap, not
permission to restore a synthetic rule. Full goal remains incomplete.

Final verification after broadening internal rutva to both typed sup and
taddhita domains: focused tests pass. Full `test --no-parallel --continue`
improves from four failures to two: the grouped numeral assertion and example
rendering. Exact `test --no-parallel` reports 791 noun-module tests, one failure,
then stops. Native sandhi stays 366/366. All eight corpus modes completed with
unchanged passes: Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957).
Fail/error/skip/strict counts are unchanged. `git diff --check` passes; no commit.
Next: remove the unrelated stem rewrite currently folded into 7.1.55 and
implement the authentic numeral lengthening/lopa/nuṭ sequence.

### Separate numeral nuṭ and upadhā operations, 2026-10-15

Removed the unrelated stem deletion/lengthening folded into 7.1.55. Its
only mutation now introduces raw, target-linked nuṭ. Applicability uses
ṣaṭ designation or catur lexical/head identity, typed genitive-plural ām,
processed affix material and a target-specific repeat guard. Subordinate
bahuvrīhi numerals are excluded, following the
[Kāśikā/Nyāsa](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.55.htm).
The number's literal output is never constructed.

Added and registered genuine 6.4.7 नोपधायाः from its
[commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.4.7.htm).
It lengthens the actual vowel immediately before final n, leaving n for
later 8.2.7. Shared `hasNutAugmentedGenitivePlural` requires typed ām, exact
operative `[NA, AA, MA]` and a linked nuṭ source archived by actual 1.1.46
placement. Unrelated written nām or a nuṭ targeting another affix is rejected.
The existing n-lopa rule recognizes this placed nām only with established
pada evidence. 1.4.17 now replaces stale bha when the processed affix's
operative initial changes to a non-y consonant; both designations cannot coexist.

Migrated 7.2.84's surface equality and whole-word reconstruction to retained
lexical identity and exact final-n → plain-ā substitution. Source review
corrected an intermediate attempt to replace the whole ṭi portion: the
[commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.84.htm)
prescribes plain ā for n, retaining the preceding a. Extended 6.1.101 to exact
adjacent internal savarṇa vowels so that their subsequent coalescence has its
own trace. Completed-word hiatus with disjoint tracked origins is protected.
No producer-number-specific output fallback was introduced.

Six focused tests verify raw nuṭ without stem mutation, typed source/target
restrictions, subordinate-number exclusion, separate upadhā and n-lopa,
four numeral genitives, both aṣṭan optional branches, independent 7.2.84 →
6.1.101 steps, accent retention and completed-word hiatus protection. Focused
registry and svādi tests pass. The obsolete 7.3.146 assertion now requires
genuine 6.4.7 and 8.2.7; grammatical output expectations are unchanged.
The grouped test advances past numeral genitives and now fails at असौ,
whose deleted combined-output implementation still needs authentic replacement.

Remaining deeper work includes complete 7.2.84 hal-initial/tadanta eligibility,
the broader n-lopa domain/exceptions, independent adas su-lopa/au operations,
yusmad/asmad segmentation, and the remaining catalogue/migration/benchmark gaps.
This does not certify complete numeral or noun grammar.

Final `test --no-parallel --continue` retains two outstanding failures:
the grouped pronoun test now at असौ and the example-rendering test.
The exact `test --no-parallel` gate reports 797 noun-module tests, one failure,
then stops. Native sandhi remains 366/366. All eight imported modes completed:
passes unchanged at Aṣṭādhyāyī continuation 944 → 944 (alternatives
1,215 → 1,215), avasāna 994 → 994 (1,270 → 1,270); UoH continuation
5,056 → 5,056 (5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957).
Fail/error/skip/strict counts are unchanged. `git diff --check` passes;
no commit, and the full goal remains active.

### Authentic adas su/au and separate satva, 2026-10-15

Added and registered genuine 7.2.107 अदस औ सुलोपश्च as a replacement for
the previously deleted literal-asau handler. The new implementation changes
only final s → au and separately elides current, originally typed su through
the lifecycle operation. It leaves d and preceding a unchanged. It does not
require rendering a completed word or copying a declension result.
Sources: the original commentary under
[7.2.107](https://ashtadhyayi.com/sutraani/rp1246) and the Sanskrit text in
[Siddhāntakaumudī](https://www.premabase.com/siddhanta-kaumudi/p11/textbook/index.html).

Migrated 7.2.106 to retained tad/etad/adas identities and exact non-final t/d
positions. It supplies dental s, not a hardcoded retroflex ṣ for etad.
Adas su-lopa retains its governing condition through the archived typed su
and the stem's established 7.2.107 provenance. Extended 6.1.88 to internal
a/ā + ec using shared `findInternalVowelJunction`, also reused by 6.1.101.
The helper protects unfinished it-processing and tracked completed-word hiatus.

8.3.59 now recognizes an exact recorded s-substitution inside a processed
non-affix carrier, not only an s in an active pratyaya. It requires the current
occurrence, substitution index, non-pada-final status and actual iṇ/ku predecessor.
Added cached `VarnaSubstitution.replacementVarnas` at the trace boundary so
the rule does not inspect rendered characters or repeatedly tokenize a trace.
Neuter 7.1.23 explicitly precedes tyadādi/kim/su substitutions, as its
commentary prescribes, rather than producing masculine nominative changes.

Five new tests verify exact intermediate `[A, DA, A, AU]`, whole-su archive,
identity/applicability restrictions, both masculine/feminine असौ traces,
independent etad s → ṣ, and neuter तत्/एतत्/अदः/किम् through luk.
Focused registry and neuter lifecycle tests pass. The grouped test advances
past असौ/अमू/अमी and now fails at त्वम्, whose deleted yusmad/asmad output table
still needs genuine segmented grammatical operations. No surface expectation
was weakened and no synthetic handler was restored.

Remaining scope includes independent akac/vārttika alternatives, broader
substitution-occurrence projection after resegmentation, other lopa-governing
contexts, the personal-pronoun rules and the wider migration/benchmark audit.
This batch is not a claim of complete pronoun grammar.

Corpus auditing caught one regression in every UoH mode: row 9007,
`अतएव + इति`, changed to अतैवेति when new internal vṛddhi reprocessed
pre-existing hiatus inside a supplied completed word. A read-only spreadsheet
comparison isolated that row. The internal-junction helper now requires a
recorded positional change exposing the junction before re-entering a
completed lexical input. Cached original Varṇas support that check without
rendered-character reasoning. Newly exposed adas junctions still apply normally.
Added a native regression case with required 6.1.87 and forbidden 6.1.88;
neither corpus data nor normalization was changed. Native benchmark is now
367 cases. An additional focused test verifies both opaque supplied hiatus
and a legitimately newly exposed junction.

Final rerun after that provenance fix: native sandhi passes 367/367.
Full `test --no-parallel --continue` still has two known failures, now at त्वम्
in the grouped pronoun test and the existing example-rendering assertion.
Exact `test --no-parallel` remains red; the noun module reports 803 tests,
one failure. All eight imported modes completed and restored the original
baseline: Aṣṭādhyāyī continuation 944 → 944 (alternatives 1,215 → 1,215),
avasāna 994 → 994 (1,270 → 1,270); UoH continuation 5,056 → 5,056
(5,833 → 5,833), avasāna 5,147 → 5,147 (5,957 → 5,957).
Relative to the detected temporary regression, each UoH mode recovers one
pass. Final fail/error/skip/strict counts equal the pre-batch baseline.
`git diff --check` passes. No commit; the full goal remains active.

### Segmented personal-pronoun nominatives, 2026-10-15

Implemented and registered authentic 7.1.28 ङे प्रथमयोरम्, 7.2.91 मपर्यन्तस्य,
7.2.93 यूयवयौ जसि, 7.2.94 त्वाहौ सौ and 7.2.90 शेषे लोपः. Sources:
[7.1.28](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.28.htm),
[7.2.93](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.93.htm),
[7.2.94](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.94.htm)
and [7.2.90](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.90.htm).
For 7.2.90 this implementation follows the explicitly attested
Siddhāntakaumudī ṭi-lopa interpretation, with dual and ā/y domains excluded.

Shared typed `PersonalPronounIdentity` stores lexical identities, not output
paradigms. `personalPronounMaparyantaRange` identifies the current m and original
head footprint in cached Varṇa positions. `replacePersonalPronounMaparyanta`
replaces only that range and retains the rest, its annotations and term identity.
The prescribed tva/aha or yūya/vaya substitutes leave ad intact. Independent
7.2.90 deletes the residual ṭi; 7.1.28 supplies complete `[A, MA]`; 6.1.107
performs actual final coalescence. No completed त्वम्/अहम्/यूयम्/वयम् output
is stored in production code. The deleted 7.2.86 output table remains absent.

Tests exposed 7.1.17 reselecting original jas after 7.1.28 had already changed
the current affix to am. Corrected it to require current typed jas or the
exact processed `[A, SA]` sequence with original jas provenance, rather
than original provenance alone. This retains processed jas support without
allowing substituted am to be reselected. The four old assertions attributing complete
nominative outputs to 7.2.86 now require actual 7.2.94/93; their surface
expectations are unchanged.

Four focused tests verify exact tva + ad intermediate material, separate ṭi
lopa, full am and preserved original jas, no jas reapplication, all four
nominatives across all three requested genders, genuine rule traces and
sup-drop provenance, scope/case exclusions and an uncoalesced compound head's
untouched preceding prefix. Focused registry tests pass. The grouped test
advances past these forms and now fails at अनड्वान् (actual अनड्वाक्), whose
deleted combined handler still needs authentic replacement.

Remaining personal-pronoun work includes duals, oblique prefix/tail rules,
the genuine non-nominative 7.2.86 domain, semantic number in compounds and
exact occurrence mapping for coalesced compound heads. These are not claimed
complete by this nominative batch; the full goal remains active.

Processed-jas regression follow-up: the 13 focused sup-substitution,
personal-pronoun and registry tests pass. Full `test --no-parallel --continue`
has two failing tests: the anaḍvān paradigm assertion (807 noun tests, one
failure) and the existing reproducible execution-rendering assertion
(665 execution tests, one failure). Native sandhi remains 367/367. The
additional processed-jas failure has been removed without accepting substituted
am as jas. No whole-word lookup or synthetic rule was restored.

### Anaḍuh source/trace audit, 2026-10-15

The removed literal anaḍvān handler was incorrectly attributed to 7.1.99.
The genuine 7.1.99 is **अम् सम्बुद्धौ**, a vocative rule, not this nominative
derivation. Corrected the nominative regression's trace requirements to
7.1.98 and 7.1.82, retaining its surface expectation. Corrected 7.1.98's
catalogue text to **चतुरनडुहोरामुदात्तः**.

Primary sources: [7.1.98 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.98.htm),
[7.1.82 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.82.htm),
and [7.1.99 catalogue](https://ashtadhyayi.github.io/suutra/7.1/7.1.99/).
The 7.1.82 commentary explicitly requires coexistence of ām/am and num;
its num has pronunciation-only u and m-it placement after the final vowel.
The existing 7.1.98 handler still combines insertion with yaṇ and does not
record the prescribed udātta. The existing 1.1.47 handler only accepts
PRATYAYA, whereas newer augment producers use AGAMA. These are identified
implementation gaps, not a completed anaḍuh repair. A shared mit-augment
lifecycle, independent yaṇ, num, and subsequent cluster operations must
replace the combined handler; no literal nominative fallback is justified.

Verification: focused PronominalStemsTest has 3 passing tests and the known
anaḍvān failure. Full `test --no-parallel` also fails at that assertion;
`git diff --check` passes. This source/trace audit changes no corpus runtime
behavior and does not claim a fresh corpus evaluation or score improvement.

### Shared mit placement lifecycle, 2026-10-15

1.1.47 now accepts explicitly targeted AGAMA terms as well as the existing
PRATYAYA śnam representation. It still requires an exact terminal m
designation from 1.3.3; a final-m affix without an augment target cannot
enter placement. Archived insertion records now retain mergedIntoTermId.
The explanation is generic rather than falsely naming every insertion śnam.

Two new lifecycle tests verify raw ām designation, exact insertion after
the last vowel, remapping of its m-it occurrence, subsequent independent
1.3.9 deletion, preservation of the target's final consonant, target/creator
provenance, repeat exclusion and rejection of an untargeted affix. These and
all twelve RemainingAffixLifecycleTest cases pass (14 focused tests).
This enables authentic augment producers; it does not yet replace the
combined 7.1.98 handler or implement missing 7.1.82.

Full `test --no-parallel --continue` verification: 809 noun tests with the
same one anaḍvān failure, 665 execution tests with the same one rendering
failure; no additional failing module. Native sandhi remains 367/367.
No fresh imported-corpus score is claimed for this infrastructure batch.
`git diff --check` passes; no commit and the full goal remains active.

### Raw ām and independent internal yaṇ, 2026-10-15

7.1.98 no longer replaces an entire catur/anaḍuh stem with a precomposed
va-ā sequence. It introduces raw AGAMA ām, targeted to the unchanged stem,
with grammatical udātta at its own vowel. 1.3.3, 1.1.47 and 1.3.9 independently
designate, place and delete m. Its scope uses grammatical sarvanāmasthāna
designation/typed sup identities, never affix ids or surface-vowel guesses.
The existing exact lexical stem domain is retained; tadanta expansion is
still pending rather than inferred from written suffix matching.

6.1.77 now handles internal vowel junctions through the shared completed-pada
hiatus/origin guard, making yaṇ an independently traced exact substitution.
An isolated regression verifies unchanged stem at introduction, raw augment,
udātta projection through placement/lopa/yaṇ, and repeat exclusion.
It and the other two mit lifecycle tests plus twelve remaining-affix tests
pass (15 focused tests). The full paradigm recovers चत्वारः and चत्वारि and
still reaches the known अनड्वान् failure, pending num.

Two lifecycle defects exposed by this real producer were corrected:
1.3.4 cannot treat an AGAMA homographic with sup ām as a case ending;
1.3.9 deletes actual eligible designations even when another raw term exists,
rather than selecting a deferred deletion and then performing no operation.
Full `test --no-parallel --continue` retains the two known failing tests;
the exact `test --no-parallel` run fails at the known noun assertion
(810 noun tests, one failure). No output fallback was introduced.

Fresh default CONTINUATION corpus reruns are unchanged before/after:
Aṣṭādhyāyī 944→944 pass, 1751→1751 fail, 4→4 errors, 1 skip, 671 exact;
UoH 5056→5056 pass, 4308→4308 fail, 0 errors, 4 skips, 4634 exact.
AVASANA modes were not rerun for this batch. `git diff --check` passes.
Fresh CONTINUATION alternatives runs are also unchanged: Aṣṭādhyāyī
1215→1215 pass / 1480→1480 fail / 4 errors / 1 skip / 690 exact;
UoH 5833→5833 pass / 3531→3531 fail / 0 errors / 4 skips / 4676 exact.

### Genuine anaḍuh num producer, 2026-10-15

Added and registered 7.1.82 **सावनडुहः**, following the
[primary commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.82.htm).
It requires the lexical anaḍuh stem, original typed su, active aṅga domain,
and an operative final a/ā vowel, so it follows ām/am rather than pre-empting
them. Exact target-specific archived provenance prevents repeat insertion.
It precedes deletion of the governing su; ordinary non-su forms are excluded.
Compound-head extension and vocative am remain pending domains.

Introduced typed MitAugment.NUM and generalized the existing term-boundary
pronunciation-only projection used by frequency affixes. Source upadeśa
retains num with its uccāraṇārtha u span; operative raw material is `[NA, MA]`.
1.3.3, 1.1.47 and 1.3.9 separately designate m, place the material after the
final vowel, and delete only m. No orthographic inspection occurs in 7.1.82.
Drop lifecycle now retains nonoperative source-upadeśa spans, previously lost
on consumption even though source upadeśa was retained.

Direct lifecycle tests verify exact tokens, su/non-su domain, absence before
ām, independent m deletion, source span/target provenance and repeat exclusion.
Four mit tests, twelve remaining-affix tests and four registry tests pass.
The full anaḍvān derivation still fails (actual अनड्वाङ्क्): subsequent cluster
and pada handling must be repaired, not replaced with literal output.

Verification: full `test --no-parallel --continue` still reports the same two
failing tests (811 noun tests, one failure; 665 execution tests, one failure).
The exact `test --no-parallel` command fails at the noun assertion.
Fresh CONTINUATION default/alternatives scores remain unchanged:
Aṣṭādhyāyī default 944→944 pass / 1751→1751 fail / 4 errors / 1 skip;
alternatives 1215→1215 pass / 1480→1480 fail / 4 errors / 1 skip.
UoH default 5056→5056 pass / 4308→4308 fail / 0 errors / 4 skips;
alternatives 5833→5833 pass / 3531→3531 fail / 0 errors / 4 skips.
No AVASANA rerun this batch. `git diff --check` passes. No commit.

### Ending-lopa pada carrier, 2026-10-15

The complete failing trace showed 6.1.68 dropping su but leaving its
established PADA designation only on the dropped term. Consequently 8.2.23
could not see the surviving cluster-final pada and 8.2.39 incorrectly became
the next effective operation for the intended derivation.
6.1.68 now projects an already-established ending PADA assignment onto the
surviving preceding carrier, retaining the original archived assignment and
sup provenance. It does not assign PADA when none was present.

Two direct tests verify carrier identity, original su/drop provenance,
independent exact final-h deletion through 8.2.23, retained n, and no invented
PADA on an unclassified input. These, four mit lifecycle tests, and all four
grouped pronominal tests pass (10 focused tests). अनड्वान् and अनड्वाहम् now
derive without restoring the deleted whole-output handler. The canonical
nominal trace requires 7.1.98 and 7.1.82, not vocative 7.1.99.
The [8.2.23 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.2.23.htm)
confirms the pada-final cluster domain following ending lopa.

The remaining execution architecture failure was a stale generated-artifact
comparison, not a runtime exception. Updated only the three text artifacts
identified by reproducibility checks, leaving their .pvm sources unchanged:
remembered_operands now renders neuter accusative कर्म rather than कर्माणं;
two_counter_machine renders चर् + ल्युट् as चरणम्/चरणेन, without the old
unsolicited yak; method_mukhya renders nasal assimilation युङ्ग्धि rather
than युन्ग्धि. All eleven ExecutionArchitectureTest cases now pass. No runtime
output-specific branch or expected-form substitution was added.

Final batch gate: `test --no-parallel` PASSES, including 813 noun tests and
665 execution tests with no failures. `git diff --check` passes. Fresh
CONTINUATION corpus scores remain unchanged before/after: Aṣṭādhyāyī default
944→944 pass / 1751→1751 fail / 4 errors / 1 skip / 671 exact; alternatives
1215→1215 pass / 1480→1480 fail / 4 errors / 1 skip / 690 exact.
UoH default 5056→5056 pass / 4308→4308 fail / 0 errors / 4 skips / 4634 exact;
alternatives 5833→5833 pass / 3531→3531 fail / 0 errors / 4 skips / 4676 exact.
AVASANA was not rerun for this batch. No commit. Green tests do not establish
complete phonological migration or corpus coverage; the full goal remains active.

### Yatva provenance guard, 2026-10-15

Largest current imported failure cluster: visarga + a (759 UoH alternative
failures, 154 Aṣṭādhyāyī alternative failures before this batch). Many traces
incorrectly applied 8.3.17 directly to unknown written visarga, bypassing
underlying-source analysis and the ru/u/guṇa sequence.
The [8.3.17 commentary](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.17.htm)
requires ru, explicitly excluding native lexical repha. Written visarga alone
is insufficient evidence of that source.

Restricted 8.3.17 to repha/visarga with established 8.2.66 provenance on the
carrier. Bare s is not already ru; bare visarga is not silently decoded as
s-origin. Existing explicit completed-pada source input remains supported.
No source was guessed from benchmark expectations and normalization is unchanged.
Positive transformation fixtures now explicitly supply rutva provenance;
the bho external case uses existing VisargaSource.S input, preserving its
original expected result. Added rejection coverage for unanalysed visarga and s.
Thirteen focused yatva/annotation tests pass; all eighteen phonological
transformation tests also pass in the final full-suite run.

The exact `test --no-parallel` command passes after correcting those source
fixtures. This is an applicability correction, not a claim that the dominant
visarga cluster is solved: morphological/lexical source analysis and exact
ru-occurrence tracking remain necessary. No output fallback was introduced.

Fresh CONTINUATION before/after results, reporting the coverage loss rather
than retaining unsupported inference for scores:
Aṣṭādhyāyī default 944→871 pass, 1751→1824 fail, 4 errors, 1 skip,
671→609 exact; alternatives 1215→1134 pass, 1480→1561 fail, 4 errors,
1 skip, 690→626 exact.
UoH default 5056→4681 pass, 4308→4683 fail, 0 errors, 4 skips,
4634→4271 exact; alternatives 5833→5451 pass, 3531→3913 fail,
0 errors, 4 skips, 4676→4312 exact.
These drops expose reliance on absent source annotations. Correct source
analysis must recover legitimate forms without treating every visarga as ru.
AVASANA not rerun in this batch. `git diff --check` passes. No commit.

### Visarga input-source sensitivity diagnostic, 2026-10-15

Inspected PadaAnalyzer: it analyzes parsed typed pada syntax, not inverse
morphology of arbitrary completed corpus spellings. It cannot justify
automatically attaching S/R source annotations to these words.
Added opt-in SandhiKoshVisargaDiagnosticTest, which keeps all accepted score
files untouched. It evaluates completed-pada inputs without source, then
with uniform explicit S or R hypotheses on visarga-final words. These are
unverified sensitivity experiments, NOT morphological analyses, prescribed
optional alternatives, accepted passes, or a runtime fallback. Expected text
is used only to measure matches, never to choose sources or generate output.
Normalization remains NFC plus whitespace compaction; CONTINUATION/default
grammatical choices only. Mixed-source and optional-rule combinations are
deliberately not claimed explored.

| Corpus | Visarga-bearing cases | Unannotated matches | Uniform S matches | Uniform R matches |
|---|---:|---:|---:|---:|
| Aṣṭādhyāyī | 1715 | 455 | 658 | 430 |
| UoH | 4336 | 1181 | 2579 | 1116 |

S recovers 386 previously unmatched Aṣṭādhyāyī rows but loses 183 baseline
matches; R recovers 158 previously unmatched rows. UoH S recovers 1811 but
loses 413 baseline matches; R recovers 458. Thus a universal S assumption is
not justified. Four Aṣṭādhyāyī malformed-input errors and zero UoH errors are
reported explicitly, including row/error detail; they are not silently dropped.
Per-row diagnostic artifacts are written to visarga-source-sensitivity.txt in
each evaluation directory, not to ordinary corpus summaries/results.

Both opt-in diagnostic runs pass. Full `test --no-parallel` passes;
`git diff --check` passes. Production rules and accepted before/after scores
are unchanged in this diagnostic-only batch (Aṣṭādhyāyī default 871/1824,
alternatives 1134/1561; UoH default 4681/4683, alternatives 5451/3913
pass/fail); ordinary corpus evaluations were not rerun or reclassified.
Next modeling requirement is independently evidenced lexical/morphological
source candidates and exact ru occurrence provenance, not another broad
phonological predicate. No commit; the full goal remains active.

### Exact rutva occurrence model foundation, 2026-10-15

Added RutvaOccurrence(varnaIndex, sutra) on DerivationTerm. A carrier can now
distinguish an actual ru occurrence from a different native repha in the same
term. Positional range projection retains ru through one-to-one visarga
conversion, shifts it through insertions/deletions, and consumes it through
u/ya substitution. Invalid/stale loci fail explicitly rather than marking a
different sound. Designated-term composition projects each carrier's separate
occurrences; exact it-lopa projects surviving ru positions independently.

Four focused core tests pass: range changes/ru consumption, two-carrier
composition, exact it-lopa, and stale-locus rejection. This batch is model
foundation, NOT activated replacement of term-wide trace predicates. Actual
rutva producers and consumers are not switched yet. Before activation, all
remaining mutation paths need exact projection: single/range/group changes,
prefix transfer, vowel coalescence, whole-affix replacement, drop/archive
lifecycle, and RuleVisibility rollback in DerivationEngine. No fallback to a
term-wide flag is being added to the new model.

Production applicability and imported accepted scores remain unchanged in
this batch; ordinary corpus evaluations were not rerun. No commit.
Final `test --no-parallel` passes, as does `git diff --check`. The full
migration/benchmark objective remains active and unproven complete.

### Rutva positional mutation integration, 2026-10-15

Integrated exact ru-occurrence projection into the shared single, range,
grouped replacement, contiguous/grouped deletion and insertion APIs. A
single ru-to-u/ya transformation consumes its occurrence; unrelated native-r
changes cannot consume or acquire it. Range substitutions use the explicit
source-occurrence map to retain a mapped repha/visarga, never spelling or
equal-length inference. Ambiguous legacy edits require an explicit projection
instead of silently preserving a term-wide rutva flag. Resulting loci are
validated against operative repha/visarga values.

Expanded RutvaOccurrenceTest from four to six tests, exercising actual state
mutation APIs as well as pure projection/composition/it-lopa. Remaining
activation prerequisites still include prefix transfer, coalescence,
whole-affix/drop lifecycle and visibility rollback. Production rutva producers
and rule consumers remain unswitched in this batch; no claim of complete
occurrence migration or benchmark recovery is made.
Final six focused tests and `test --no-parallel` pass. `git diff --check`
passes. Accepted corpus scores are unchanged in this inactive-model batch;
corpus evaluations were not rerun. No commit; the full goal remains active.

### Rutva whole-replacement and drop lifecycle, 2026-10-15

Whole-term/whole-affix replacement now clears old operative ru occurrences.
Designation preservation alone cannot establish sound identity on a new
affix. Explicit whole-affix surviving-position maps independently preserve
mapped repha/visarga occurrences and consume ones mapped to other sounds.
Drop lifecycle clears operative occurrences and stores them alongside the
original dropped surface in originalRutvaOccurrencesBeforeDrop, for both
affixes and non-affixes. Asiddhavat restoration of a dropped term restores
that original occurrence ledger as well as its surface.

Two added core tests (eight total) cover whole replacement policies,
explicit preservation/consumption, lexical replacement and all three
PRATIPADIKA/PRATYAYA/AGAMA drop paths. The existing explicit visibility
fixture now also verifies restoration of a dropped ru-bearing affix.
Rutva producers and consumer predicates remain unswitched: substitution
rollback, prefix transfers and coalescence still require integration before
activation. Production applicability and corpus scores are not claimed
improved in this lifecycle batch.
Eight focused occurrence tests and all 28 DerivationEngineTest cases pass.
Final `test --no-parallel` and `git diff --check` pass. Corpus scores remain
unchanged with producers inactive; corpus evaluations were not rerun.
No commit; the full objective remains active.

### Confirmed false rule entries removed, 2026-10-16

Removed `AtoYukSutra` (false 7.3.114, directly constructing dual योः)
and `VatoYukSutra` (false 7.3.33, applying to वा/पा/म + उ).
The canonical texts are respectively सर्वनाम्नः स्याड्ढ्रस्वश्च and
आतो युक् चिण्कृतोः; these deleted implementations must not be relabelled
as the genuine rules. Removed their registry/engine references and tests
asserting their invented operations, but retained the expected वायु output
test so the resulting missing grammatical coverage remains visible.

Registered the existing 7.3.105 आङि चापः implementation. Forward-derived
feminine प्रथमयोः candidates are checked for 7.3.105, 6.1.78, 8.2.66 and
8.3.15 provenance, rather than a preconstructed visarga. Masculine/neuter
homographs independently use 7.3.104 and are not mistaken for āp candidates.
Registry regression checks exclude both deleted identities until genuinely
implemented. No blanket assertion that all legacy implementations are
shortcut-free is justified. Also removed the 7.3.113 implementation because
it directly constructed combined endings. The genuine याडापः rule remains
unimplemented pending authentic augment-lifecycle modeling; grammatical
output expectations were not changed to hide the resulting coverage gap.

Corpus evaluations were not rerun for this removal batch; no new external
benchmark pass counts are claimed. No commit.

Removal verification: the derivation module passes, including the genuine
feminine dual provenance regression. The full-suite run exposes three
ashtadhyayi failures (वायु and the रमा/सीता paradigms) and four execution
failures dependent on ā-final noun rendering. Expected grammatical forms
and generated example artifacts were not rewritten to accept these gaps.
`git diff --check` passes. This batch is not a green-suite checkpoint.

### Authentic yāṭ lifecycle rebuild, 2026-10-16

Reimplemented and registered genuine 7.3.113 याडापः as raw याट्
introduction only. Applicability requires established āp formation evidence,
an actual Sup ending with effective ṅit status, completed prior it processing,
and locative 7.3.116 substitution before augment introduction. Existing
1.3.3, 1.1.46 and 1.3.9 independently designate, place and delete the augment
marker. Existing internal 6.1.88/6.1.101 then coalesce vowels; 8.2.66 and
8.3.15 independently derive the ablative/genitive visarga. No stem shortening,
finished ending replacement, synthetic visarga, or literal output fix is used.
The augment's target, dropped-carrier record and it provenance are preserved.

No new shared abstraction was necessary: the existing exact augment insertion
and internal vowel-junction operations support this transformation. Added two
focused lifecycle tests and included the rebuilt rule in the exact-once
executable registry test. These tests and both full feminine paradigms pass.
The full suite now has one failure (वायु expected, वाउ actual), down from seven
after shortcut removal. The six āp-related failures are recovered genuinely.
The remaining failure needs an independently sourced Uṇādi derivation, not
reintroduction of the false 7.3.33 rule. Nothing committed.

Reran both corpora at CONTINUATION, default and alternatives separately:

| Corpus / mode | Before pass / fail | After pass / fail | Errors / skipped | Exact |
| --- | --- | --- | --- | --- |
| Aṣṭādhyāyī default | 871 / 1824 | 871 / 1824 | 4 / 1 | 609 |
| Aṣṭādhyāyī alternatives | 1134 / 1561 | 1134 / 1561 | 4 / 1 | 626 |
| UoH default | 4681 / 4683 | 4681 / 4683 | 0 / 4 | 4271 |
| UoH alternatives | 5451 / 3913 | 5451 / 3913 | 0 / 4 | 4312 |

This rebuild repairs nominal provenance and coverage, not external sandhi
scores. Input normalization and visarga-source assumptions are unchanged.
The four Aṣṭādhyāyī metalinguistic-input errors remain. Broader authentication,
canonical 7.3.114 and the remaining unresolved source/orthographic modeling
are not claimed complete.

### Canonical 7.3.33 rebuild, 2026-10-16

Source verification corrects the earlier overbroad description of the removed
`VatoYukSutra`: 7.3.33 itself is genuine and does derive वायु, but the old
entry's text वातो युक् and its root-list plus bare-उ applicability were wrong.
The canonical rule is आतो युक् चिण्कृतोः. Kāśikā specifies an ā-final aṅga
before ciṇ or a ñit/ṇit kṛt affix, not an arbitrary u-shaped suffix.
Source: https://ashtadhyayi.github.io/suutra/7.3/7.3.33/?transliteration_target=gurmukhi

Implemented `AtoYukCinKrtohSutra` with cached Varṇa inspection and grammatical
kṛt/marker evidence. Added typed `KitAugment.YUK`, reusing the existing
pronunciation-only source-token projection at the term boundary. The rule
introduces raw युक् with pronunciation-only उ, leaving k designation,
end-placement and lopa to 1.3.3, 1.1.46 and 1.3.9. It neither guesses an
output nor directly inserts a processed y. The restricted Uṇādi engine now
includes existing 3.1.93 to establish its suffix's kṛt classification.
Registered the canonical implementation and verified exact-once registry
membership. Bare उ without kṛt evidence and unrelated taddhita/sanādi domains
are excluded.

Three lifecycle tests cover the domain, raw augment, independent trace,
वायु/पायु, retained nasalization/avagraha, unchanged root/affix identity and
exact projected it indices. They, the participle tests and registry tests
pass. The complete `test --no-parallel` suite passes: the last previously
exposed वायु failure is repaired through the genuine rule, not a fallback.
No commit; broad migration and benchmark coverage remain unfinished.

Post-rebuild corpus reruns at CONTINUATION preserve the preceding baseline:
Aṣṭādhyāyī default 871 pass / 1824 fail, alternatives 1134 / 1561,
both with 4 errors and 1 skip; UoH default 4681 / 4683, alternatives
5451 / 3913, both with 0 errors and 4 skips. Exact-match counts remain
609/626 and 4271/4312 respectively. These are unchanged before/after counts,
not claimed score gains from a nominal derivation repair.

### Canonical pronominal āp syāṭ, 2026-10-16

Implemented and registered genuine 7.3.114 सर्वनाम्नः स्याड्ढ्रस्वश्च,
replacing the former unimplemented slot rather than restoring the deleted
आतो युक् shortcut. Kāśikā requires sarvanāma and āp evidence before a ṅit
ending; syāṭ is an apavāda to yāṭ. Source:
https://ashtadhyayi.com/sutraani/plsk116

The implementation makes an exact final AA→A Varṇa substitution and introduces
raw स्याट्. Independent 1.3.3/1.1.46/1.3.9 process/place the augment; existing
6.1.88/6.1.101 and 8.2.66/8.3.15 derive the vowel and visarga results.
Locative 7.3.116 runs before augment introduction. No finished ending is
constructed. The shared `hasApFormation` and `apNgitJunctions` helpers now
serve both 7.3.113 and 7.3.114 using grammatical provenance, not ā shape alone.

Supplied formed stems may explicitly provide `pratipadikaUpadesha`, e.g.
सर्वा with lexical identity सर्व and declared AP formation. Existing callers
retain their previous default identity. 1.1.27 now consults this stable
upadeśa instead of the mutable surface, preserving gaṇa conditions rather
than guessing a lexical base from its spelling. Its text typo was corrected
to सर्वादीनि सर्वनामानि.

Focused tests exposed re-entry into earlier 7.1.14/7.1.15 after āp shortening.
Their a-stem domains now exclude established āp formations, so the shortened
vowel cannot wrongly trigger स्मै/स्मात्/स्मिन्. Positive original a-stem
applicability is retained and regression-tested.

Three new tests cover twelve सर्वा/विश्वा/अन्या ṅit forms, raw augmentation,
independent traces, nasalization/avagraha preservation, identities, rejection
without either grammatical qualification, and earlier-rule domain retention.
They and existing feminine/pronominal paradigms and registry tests pass.
The complete `test --no-parallel` suite passes. No commit.

This is not a complete lexical analyzer: the standard lexical entries still
lack typed stem-formation/base metadata. Neither feminine shape nor expected
benchmark output is used to fill that gap. 7.3.115 ordinal alternatives and
the broader authentication/source-modeling work remain outstanding.

Reran both corpora at CONTINUATION, default and optional alternatives
separately. Before/after pass/fail counts are unchanged: Aṣṭādhyāyī
871/1824 default and 1134/1561 alternatives (4 errors, 1 skip);
UoH 4681/4683 default and 5451/3913 alternatives (0 errors, 4 skips).
Exact counts remain 609/626 and 4271/4312. `git diff --check` passes.

### Optional ordinal syāṭ, 2026-10-16

Implemented and registered 7.3.115 विभाषा द्वितीयातृतीयाभ्याम्. Kāśikā
prescribes optional syāṭ and āp shortening after द्वितीया/तृतीया before ṅit,
with द्वितीयस्यै/द्वितीयायै and तृतीयस्यै/तृतीयायै as examples. Source:
https://www.ashtadhyayi.com/kashika/28?scroll=kashika-73111

The new rule uses exact lexical identities, established āp formation and
typed ṅit Sup evidence. It does not generalize from a suffix spelling or
assign sarvanāma to every ordinal. Extracted `introduceApSyat` for the shared
7.3.114/115 operation: one positional AA→A substitution plus raw स्याट्
introduction. Existing independent placement, it-lopa, vowel coalescence and
rutva/visarga rules retain their own trace/provenance. The common junction
helper tracks all three āp augment producers to prevent repeated augmentation.

Two focused tests cover both ordinals in four ṅit cases, the separate syāṭ
and yāṭ derivations, explicit optional skipping, terminal stages, single
producer application, independent it-processing traces, and rejection of
missing āp/ṅit evidence or an unrelated ordinal. These and the pronominal
āp, ordinary yāṭ lifecycle and exact-once registry tests pass.

Final verification: `test --no-parallel` passes, followed by successful
CONTINUATION corpus evaluations with default/alternatives kept separate.
Before/after scores are unchanged: Aṣṭādhyāyī default 871 pass/1824 fail,
alternatives 1134/1561 (4 errors, 1 skip); UoH default 4681/4683,
alternatives 5451/3913 (0 errors, 4 skips). Exact counts remain 609/626 and
4271/4312. `git diff --check` passes. No commit. Lexical formation metadata,
visarga-source analysis and the full authentication/Varṇa audit remain open.

### Typed lexical formation and complete optional candidate paths, 2026-10-16

`PratipadikaEntry` now carries typed `stemFormation` and stable lexical
`upadesha` metadata independently of gender, spelling and legacy stem-class
labels. Declared the existing सङ्ख्या and three feminine ordinal entries as
āp formations; the ordinal base identities are explicit lexical data, not
inferred by removing a vowel sign. सङ्ख्या's अङ् + टाप् formation is attested
in the Sanskrit Hindi Kosh:
https://www.jainqq.org/pagetext/Sanskrit_Hindi_Kosh/020643/1358

The forward visarga index now passes this metadata to the nominal request.
New `SubantaEngine.deriveAll` validates every grammatical alternative against
the same Sup plan/final-stage requirements as single derivation. The index
retains every returned case/alternative proof rather than only the default
path; failures and unproven visarga sources still remain explicit.

Focused tests show a concrete correction: प्रथमाः no longer acquires a false
singular ablative/genitive analysis from missing āp metadata. प्रथमायाः is
derived through याट्, and द्वितीयस्याः/द्वितीयायाः both retain independent
7.3.115/7.3.113 source derivations. Existing native-repha and rutva candidate
tests, homographic cases, unknown gender and error reporting remain green.
Seven focused index tests pass. Undeclared formation metadata stays
UNSPECIFIED; no spelling-driven default or expected-output inference is added.

This remains forward analysis under the supplied lexicon, not an exhaustive
inverse analyzer. Plain-string sandhi inputs and external corpus source
assumptions are unchanged. No commit; broader source analysis and rule audits
remain open.

Full-suite verification passes, followed by both CONTINUATION corpus modes.
Before/after pass/fail counts remain Aṣṭādhyāyī 871/1824 default,
1134/1561 alternatives (4 errors, 1 skip), and UoH 4681/4683 default,
5451/3913 alternatives (0 errors, 4 skips). Exact counts remain 609/626
and 4271/4312. `git diff --check` passes. No sandhi score increase or
unannotated-input source inference is claimed for this metadata batch.

### Explicit declared-lexicon corpus policy and active-boundary decoding, 2026-10-16

Added opt-in `-PsandhikoshLexicalAnalysis=declared` to the corpus harness.
The default remains `none`. Declared-mode output filenames include
`-declared-lexicon`, so these scores never overwrite or masquerade as the
unannotated baseline. Analysis uses only the pre-existing declared lexicon
and forward nominal derivations; expected sandhi outputs do not participate
in lexical construction, lookup or source selection. No corpus-specific
lexical entries or spelling-driven s/r assumptions were added.

All matching case/alternative proofs participate independently. The sequence
outcome retains original input carriers and full nominal source derivations
across later boundaries, and CSV reports include the selected nominal proofs.
Strict branch limits fail explicitly rather than silently dropping analyses.
Unknown words retain the existing unannotated policy. Incomplete declared
lexical derivations stop the mode instead of triggering a source fallback.

Integration exposed premature decoding of the right word's supplied visarga.
`SandhiEngine` now opens visarga-source input only for the active left
boundary, or for the right word at explicit AVASANA. An unresolved continuation
does not expose the right word's underlying r/s before its following context
is known. 8.3.15's grammatical khar/avasāna conditions are unchanged:
https://www.ashtadhyayi.com/kashika/32?scroll=kashika-83030

The old internal `SANDHI_VISARGA_SOURCE_7` fixture expected premature right-r
exposure. Corrected that input-scope expectation from अत्रपुनर् to अत्रपुनः;
the explicit-pause fixture still requires independent 8.3.15. No external
corpus expected outputs were edited. The sequence adapter transfers an
unchanged visarga ending's source using exact completed-pada token origins,
including after vowel coalescence, instead of spelling or term-wide history.

Focused tests verify baseline preservation, real प्रथमः rutva analysis,
unmatched रामः remaining unannotated, both पितुः case proofs, deferred source
use at the next boundary, exact ending-source transfer through coalescence,
and non-truncating limits. Focused and full `test --no-parallel` pass; all
370 native sandhi cases pass. `git diff --check` passes. No commit.

Reran both corpora at CONTINUATION under both policies and both sandhi modes:

| Corpus / sandhi mode | Unannotated before/after pass / fail | Declared-lexicon pass / fail | Net pass difference | Errors / skips |
| --- | --- | --- | --- | --- |
| Aṣṭādhyāyī default | 871 / 1824 | 872 / 1823 | +1 | 4 / 1 |
| Aṣṭādhyāyī alternatives | 1134 / 1561 | 1136 / 1559 | +2 | 4 / 1 |
| UoH default | 4681 / 4683 | 4714 / 4650 | +33 | 0 / 4 |
| UoH alternatives | 5451 / 3913 | 5485 / 3879 | +34 | 0 / 4 |

Declared coverage is 6 rows / 6 word occurrences in Aṣṭādhyāyī and 56 rows /
58 word occurrences in UoH. Exact-match counts are 610/627 and 4295/4336 in
declared mode versus unchanged baseline 609/626 and 4271/4312. These are net
policy comparisons, not claims that the declared lexicon is exhaustive or
that all visarga ambiguity has been resolved. The largest residual UoH
alternative-mode group remains VISARGA + A (749 cases with declared analysis,
versus 767 unannotated). Four metalinguistic Aṣṭādhyāyī input errors remain.
Broader lexical coverage, authentication and Varṇa/provenance audits remain
unfinished.

### Internal and cross-carrier consonant lopa authentication, 2026-10-16

Implemented missing 8.4.64 हलो यमां यमि लोपः and corrected 8.4.65
झरो झरि सवर्णे to inspect internal as well as cross-term clusters. Kāśikā
attests the optional आदित्य/आदित्य्य forms; Bālamanoramā attests
कृष्णर्धि/कृष्णर्द्धि. The two domains remain distinct: corresponding यम्
after हल् for 64, and सवर्ण झर् after हल् for 65. They are not replaced by
a broad "duplicate consonant" predicate. Sources:
https://www.ashtadhyayi.com/kashika/33?scroll=kashika-84002
https://www.ashtadhyayi.com/balamanorama/3?scroll=balamanorama-11057

Added the exact YAM pratyāhāra and shared `LocatedVarna`/`findVarnaTriplets`
operations. They inspect cached phonemes, keep owner IDs/local Varṇa indices,
span empty and separate carriers, and reject unresolved it-processing states.
Deletion uses the exact positional helper, preserving other carriers,
nasalization, orthographic signs and source identity. Registered 8.4.64 and
verified exact-once membership. Corrected the contradictory NITYA metadata
on already-optional 8.4.63 and 8.4.65 to VIBHASHA.

Six focused tests cover YAM membership, distinct negative domains (अन्नम्,
माहात्म्यम्, शार्ङ्गम्, प्रत्तम्, तर्प्ता, मूर्ध्नः), internal annotated
deletion, three-carrier/empty-carrier ownership, both optional outcomes and
termination, plus रुन्धे/रुन्द्धे caller selection.

Broader tests exposed previously unexpressed retained-cluster preferences.
Added general `DerivationConfig` forwarding to `TingantaEngine`; no root or
output-specific exception is used. Six traditional paradigm fixtures now
explicitly select cluster retention without changing their expected forms.
The existing महर्द्धिः native fixture likewise explicitly skips optional
8.4.65, and a separate new fixture tests महर्धिः. Five source-backed native
cases cover retained/deleted internal yam/jhar and guṇa-created clusters.
All 375 native cases and the complete `test --no-parallel` suite pass.

The 8.4.63 छत्वममि vārttika found during the audit is not silently folded
into the canonical अट् rule; it still requires explicitly sourced vārttika
ownership/registration. Broader authentication and source analysis remain
open. No commit.

Multiple-occurrence regression exposed rule-wide optional suppression in the
generic engine. The cluster rules now enumerate each currently eligible
Varṇa deletion position as an application alternative, plus retention, so
retaining an earlier cluster does not prevent deleting a later one. Exact
owner/index explanations and positional substitutions preserve the chosen
operation's evidence; no rule cycles or spelling-based selection are added.
Both झर् clusters in सार्द्धम् + अर्ज्जनम् and both यम् clusters in
कार्य्यम् + आर्य्यम् produce all four retain/delete combinations. This fixes
these rules' occurrence selection without inventing a new sūtra or changing
the generic engine's grammar. Other optional-rule multi-occurrence domains
still require their own audit.

Final verification after independent-position enumeration: focused tests,
full `test --no-parallel`, all 375 native cases, and all eight CONTINUATION
corpus configurations pass their execution gates. `git diff --check` passes.
External corpus expectations and normalization are unchanged.

| Corpus / policy / mode | Before pass / fail | After pass / fail | Net passes |
| --- | --- | --- | --- |
| Aṣṭādhyāyī unannotated default | 871 / 1824 | 876 / 1819 | +5 |
| Aṣṭādhyāyī unannotated alternatives | 1134 / 1561 | 1148 / 1547 | +14 |
| Aṣṭādhyāyī declared default | 872 / 1823 | 877 / 1818 | +5 |
| Aṣṭādhyāyī declared alternatives | 1136 / 1559 | 1150 / 1545 | +14 |
| UoH unannotated default | 4681 / 4683 | 4659 / 4705 | -22 |
| UoH unannotated alternatives | 5451 / 3913 | 5451 / 3913 | 0 |
| UoH declared default | 4714 / 4650 | 4692 / 4672 | -22 |
| UoH declared alternatives | 5485 / 3879 | 5485 / 3879 | 0 |

Errors/skips remain 4/1 for Aṣṭādhyāyī and 0/4 for UoH. Exact matches are
611/630 unannotated and 612/631 declared in Aṣṭādhyāyī; 4251/4312
unannotated and 4275/4336 declared in UoH (default/alternatives).
Declared lexical coverage remains 6 rows/6 word occurrences and 56/58.

The default UoH decrease is reported rather than hidden by selecting a
corpus-favored default or normalization. Failure examples include row 1871
कार्य्यम् + अग्रे (expected कार्य्यमग्रे, APPLY_ALL produces कार्यमग्रे),
row 4582 सार्द्धम् + आगच्छति (expected सार्द्धमागच्छति, produces
सार्धमागच्छति), and row 1884 तर्ज्जनी + अनामिका (expected तर्ज्जन्यनामिका,
produces तर्जन्यनामिका). These are retained versus optionally shortened
clusters; the alternative policy still admits retention. No per-word runtime
exception or expected-output substitution was introduced. No commit; the
complete migration/authentication objective remains active.

### Exact rutva producer/consumer activation, 2026-10-15

Activated RutvaOccurrence in the shared raw-rutva producer used by 8.2.66
and 8.3.7. It records the exact newly prescribed repha position; independent
it processing retains that occurrence while deleting its nasalized u.
6.1.113, 6.1.114 (including its numeral-internal target) and 8.3.17 now
require hasRutvaAt at the operative final position, replacing term-wide
substitution-history predicates. u/ya replacement consumes that occurrence
through the already-tested projection helpers. Native repha, stale history
and unknown bare visarga do not acquire ru identity.

Updated explicit representation fixtures to supply exact ru metadata instead
of pretending a historical trace alone established the current occurrence.
Historical-only inputs are now negative tests. Surface/annotation expectations
remain unchanged. All 34 focused yatva/annotation/phonological/guṇa cases pass;
final `test --no-parallel` and `git diff --check` pass.

Fresh CONTINUATION before/after counts are unchanged: Aṣṭādhyāyī default
871→871 pass / 1824→1824 fail / 4 errors / 1 skip / 609 exact; alternatives
1134→1134 pass / 1561→1561 fail / 4 errors / 1 skip / 626 exact.
UoH default 4681→4681 pass / 4683→4683 fail / 0 errors / 4 skips / 4271 exact;
alternatives 5451→5451 pass / 3913→3913 fail / 0 errors / 4 skips / 4312 exact.
AVASANA not rerun in this batch. Exact ru identity is now active; independent
analysis of unknown completed visarga inputs remains a separate unfinished
requirement. No source guesses, output fallbacks or commits.

### Provenance-backed completed-pada handoff, 2026-10-16

Added ESTABLISHED_RU input-source classification, distinct from underlying
S/R lexical hypotheses and not a new Varṇa. It requires an exact surviving
RutvaOccurrence. At a new external boundary, a visarga-final term carrying
that evidence is decoded to its already-established repha while preserving
the occurrence and original input record. No reverse 8.3.15 trace or fresh
8.2.66 application is invented. Unannotated visarga remains unknown, and an
explicit ESTABLISHED_RU claim without occurrence evidence fails.

SandhiEngine.padaFromDerivation composes the exact token carriers of a
completed morphological word, validates finished it-processing, projects
occurrences/signs/accents through the existing composition primitive, uses a
caller-supplied external identity, and retains the full original result as
Pada.sourceDerivation. It requires morphological case/lakāra context and
rejects arbitrary external-sandhi phrases, avoiding phrase-as-single-pada
normalization. Existing case/rūpa and word-level PADA/PRAGRHYA classifications
survive the handoff. Internal root/prefix/affix classifications remain scoped
to the retained source result rather than being relabeled onto the whole word.
Composition is anchored on the morphological head, not an initial prefix;
the completed external carrier is not a new raw DHATU. It is input
construction, not a grammatical substitution rule.

Five added tests verify actual nominal रामः + अस्ति → रामोऽस्ति without a new
8.2.66 application, provenance-preserving repeated boundaries using every
ordinary-visarga/allophone alternative of 8.3.37, rejection of unsupported
ru source, rejection of phrase collapse, and correct internal classification
scope. All eleven completed-visarga and four sequence tests pass.
Source-sensitivity diagnostics explicitly enumerate
only S/R hypotheses; ESTABLISHED_RU is not an extra guess or scored branch.
The previous verification launch was rejected by approval-review usage limits
before execution; the retry on 2026-10-16 succeeded.

Ru occurrence projection also retains the origin through JIHVAMULIYA and
UPADHMANIYA, not just ordinary VISARGA. The
[8.3.37 Kāśikā](https://ashtadhyayi-lite.github.io/sutra/8.3.37.html)
prescribes these as visarga substitutes alongside ordinary visarga; losing
origin in only the allophone branches was an annotation defect. Handoff
decodes those already-derived variants using the same exact origin evidence,
without guessing the source of a bare allophone. Both ku/pu branches and
their ordinary-visarga alternatives are exercised independently.

Final focused occurrence, completed-visarga, sequence and kupu tests pass.
`test --no-parallel` and `git diff --check` pass. Fresh CONTINUATION
before/after scores remain unchanged: Aṣṭādhyāyī default 871→871 pass /
1824→1824 fail / 4 errors / 1 skip / 609 exact; alternatives 1134→1134
pass / 1561→1561 fail / 4 errors / 1 skip / 626 exact.
UoH default 4681→4681 pass / 4683→4683 fail / 0 errors / 4 skips / 4271 exact;
alternatives 5451→5451 pass / 3913→3913 fail / 0 errors / 4 skips / 4312 exact.
AVASANA not rerun this batch. No commit; unknown-input source analysis and
the broader migration/benchmark goal remain unfinished.

### Missing 8.3.20 and consonant precedence, 2026-10-16

Added and registered genuine **ओतो गार्ग्यस्य** 8.3.20. The
[Kāśikā text](https://sanskritdocuments.org/~sanskrit/learning_tools/ashtadhyayi/vyakhya/8/8.3.20.htm)
prescribes mandatory lopa of ordinary pada-final y after o before aś;
Gārgya's name is honorific here, not a generic optional-skip license.
The rule checks exact cached `[O, YA]`, actual completed-pada status and the
following aś Varṇa, then deletes only the final y. Existing boundary blocks
preserve tripadi hiatus, including preventing new 6.1.109 pūrvarūpa. Rendering
and sequence evaluation recognize that established hiatus without adding a
space or orthographic sign as a Varṇa. No new phonological helper was needed;
the existing exact deletion and boundary-block primitives are reused.

Full verification exposed overlap with 8.3.22 before consonants. Corrected
the narrower hal rule to override 8.3.19/20 in its own domain, preserving its
existing output/trace behavior rather than weakening the regression. Its
inherited aś restriction is now explicit, as described in the
[8.3.22 commentary](https://sanskritdocuments.org/~sanskrit/learning_tools/ashtadhyayi/vyakhya/8/8.3.22.htm).
Added exclusions for y before khar and for an unclassified internal y,
without changing ordinary voiced cases. The pada-final condition is explicit;
the existing positive representation fixture now supplies its required PADA
designation, retaining its original expected surface. Negative khar fixtures
also supply PADA so they test inherited aś rather than accidentally failing
only because the word designation is missing.

Three focused 8.3.20 tests cover six genuine source-annotated combinations,
mandatory behavior even when optional rules are skipped, no reopened
pūrvarūpa, exact annotation/identity retention, repeat exclusion and negative
v/other-vowel/internal-y/non-aś domains. Registry inclusion is checked.
Three primary-source native cases (भो अत्र, भगो इदम्, अघो अत्र) expand the
native benchmark from 367 to 370; all 370 pass in the focused run.
Reduced-effort y under still-missing 8.3.18 requires independent phonetic
annotation and is not advertised as implemented by this ordinary-y batch.

Final focused rule, scope, registry and native-benchmark tests pass;
`test --no-parallel` and `git diff --check` pass. Native benchmark 367→370
cases, all passing. Fresh CONTINUATION before/after scores are unchanged:
Aṣṭādhyāyī default 871→871 pass / 1824→1824 fail / 4 errors / 1 skip /
609 exact; alternatives 1134→1134 pass / 1561→1561 fail / 4 errors / 1 skip /
626 exact. UoH default 4681→4681 pass / 4683→4683 fail / 0 errors / 4 skips /
4271 exact; alternatives 5451→5451 pass / 3913→3913 fail / 0 errors / 4 skips /
4312 exact. AVASANA not rerun in this batch. Unknown-input visarga source
analysis and reduced-effort phonetic annotation remain unresolved, not hidden
inside Unicode helpers or runtime output fallbacks. No commit; goal active.

### Rutva substitution visibility snapshots, 2026-10-15

Positional substitutions now snapshot the pre-operation ru occurrence ledger
alongside originalSurface, including exact deletion while it designations
are pending. VarnaSubstitution stores the ledger in its data payload so a
copy does not discard it. Asiddhavat substitution rollback restores the
matching ledger with the original surface, including an occurrence consumed
by a now-invisible ya/u change. Multiple changes restore their snapshots in
reverse order. Legacy text rollback explicitly rejects a carrier with active
exact ru metadata instead of guessing an occurrence from characters.

Three new visibility mechanics fixtures (not grammatical output derivations)
verify consumed-origin restoration, reverse position restoration, copied
snapshot retention, immutable actual state, and rejection of unsupported
legacy rollback. These and the 28 existing engine tests pass (31 focused).
Producer/consumer activation remains pending prefix-transfer/coalescence
integration; no production source analysis or benchmark recovery is claimed.
Two whole-affix trace assertions now explicitly expect a known-empty ru
snapshot, distinguishing it from a legacy unknown (null) snapshot without
changing their surface, designation or lifecycle expectations. All 14
whole-affix and eight occurrence tests pass, in addition to the 31 focused
engine/visibility cases.
Final `test --no-parallel` and `git diff --check` pass. Accepted corpus scores
are unchanged in this inactive-model batch; corpus evaluations were not rerun.
No commit; the full goal remains active.

### Rutva boundary composition integration, 2026-10-15

Prefix transfer now partitions exact ru occurrences with the moved Varṇa
prefix, preserving left occurrences and reindexing the right remainder.
Native repha in the remaining material does not acquire rutva. Initial-vowel
coalescence retains the following term's projected ru through concatenation
and archives its original (pre-coalescence) occurrence positions with the
original surface. Preceding augment coalescence likewise archives the original
occurrence ledger alongside its other original provenance.

Ten focused occurrence tests pass, including new two-carrier prefix transfer
and following-vowel coalescence/archive coverage. Producer/consumer activation
is the next integration step, not claimed done by this batch. Unknown corpus
visarga sources remain unanalysed; this metadata work does not justify a
default source guess or count source hypotheses as accepted benchmark passes.
Final `test --no-parallel` and `git diff --check` pass. Accepted corpus scores
are unchanged with producers still inactive; corpus evaluations were not rerun.
No commit; the full objective remains active.

### Separate Uṇādipāṭha source ownership, 2026-10-16

UnadiSutra previously exposed its source as SutraSource.Ashtadhyayi despite
belonging to a separate collection. It now exposes SutraSource.Unadipatha,
with a distinct `unadipatha` portable source tag. Existing rule IDs, text,
grammatical applicability, effects and expected forms are unchanged. This
corrects attribution; it does not certify every Uṇādi implementation.

Focused core codec and Uṇādipāṭha tests pass. The regression traverses every
registered Uṇādi rule and verifies collection ownership, unchanged ID/text,
and complete blueprint serialization round trips. Full `test --no-parallel`
and `git diff --check` pass. Fresh CONTINUATION, unannotated corpus results
are unchanged before/after this attribution-only batch:

| Corpus / policy | Pass before→after | Fail before→after | Errors | Skips | Exact |
| --- | --- | --- | --- | --- | --- |
| Aṣṭādhyāyī default | 875→875 | 1820→1820 | 4 | 1 | 610 |
| Aṣṭādhyāyī alternatives | 1148→1148 | 1547→1547 | 4 | 1 | 630 |
| UoH default | 4659→4659 | 4705→4705 | 0 | 4 | 4251 |
| UoH alternatives | 5451→5451 | 3913→3913 | 0 | 4 | 4312 |

Declared-lexicon and AVASANA modes were not rerun in this batch. Individual
rule authentication, unknown-input visarga provenance and reduced-effort
phonetic annotation remain incomplete. No commit; the goal remains active.
### Corpus input-error classification, 2026-10-16

The four Aṣṭādhyāyī error rows (903, 920, 2500, 2506) contain parenthesized
commentary as word operands. The sequence evaluator now validates each
original word at its input boundary and reports a typed InvalidPadaInput
with the original spelling, operand position and parser cause. Error groups
distinguish INVALID_PADA_INPUT from DERIVATION_RUNTIME. Both still count as
ERROR, never PASS or SKIPPED. No parentheses, annotations or expected-output
material are stripped. This is diagnosis, not four repaired derivations.

Focused sequence tests cover all four corpus inputs, malformed nasalization,
and independent branch-limit errors, which must remain runtime errors.
Both unannotated CONTINUATION corpora were rerun in default and alternative
modes: Aṣṭādhyāyī remains 875/1820 and 1148/1547 pass/fail, with 4 input
errors and 1 skip; UoH remains 4659/4705 and 5451/3913, with 0 errors and
4 skips. Exact counts remain 610/630 and 4251/4312 respectively.
No corpus expectations or normalization policies changed. Final focused tests,
full `test --no-parallel` and `git diff --check` pass. No commit; goal active.

### Varṇa-native luṭ ending substitution, 2026-10-16

Migrated 2.4.85 लुटः प्रथमस्य डारौरसः from raw string replacement to
exact Varṇa lists, selected with current typed TingAffix identities. Both
padas retain the prescribed eka/dvi/bahu mapping. The original tiṅ remains
available as sthānin/source provenance but cannot reselect the rule after
substitution. डा retains FreshUpadesha, with independent ड्-it processing;
रौ and रस् retain Consume. Rendering remains at the whole-affix boundary.
No new shared abstraction or output table was introduced.

Source: [Kāśikā and Nyāsa on 2.4.85](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/2/2.4.85.htm).
Focused luṭ substitution and existing verbal paradigm tests pass (67 tests).
They cover all six endings, exact replacements, term identity, original tiṅ,
fresh-it state, non-reapplication, other puruṣas/lakāras and non-affix
lookalikes. Full `test --no-parallel` and `git diff --check` pass. Both
unannotated CONTINUATION corpora were rerun with unchanged before/after scores:
Aṣṭādhyāyī default 875/1820 pass/fail, alternatives 1148/1547, four input
errors and one skip, exact 610/630. UoH default 4659/4705, alternatives
5451/3913, zero errors and four skips, exact 4251/4312. Declared-lexicon and
AVASANA modes were not rerun. No commit; goal active.

Remaining audit findings include 1.2.47's in-rule rendering and whole-term
replacement, whose exact occurrence annotations need preservation, and
1.1.20's unchanged-state घु placeholder using घि as a proxy in its predicate.
The latter requires authentic root identity and its own saṃjñā; a valid
canonical number alone does not establish a working grammatical rule.

### Exact neuter-stem shortening, 2026-10-16

Migrated 1.2.47 ह्रस्वो नपुंसके प्रातिपदिकस्य to one exact final-Varṇa
substitution. Removed in-rule rendering and whole-surface replacement. Its
existing unchanged-stem guard now compares cached varnas/upadeshaVarnas,
not display strings, so orthographic signs and nasalization do not prevent
otherwise identical phonological stems from matching. Changed stem shapes
remain excluded; this is not a broad relaxation to inflected forms.

Source: [Kāśikā and Nyāsa on 1.2.47](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.2.47.htm).
Focused tests cover every long vowel's existing short counterpart, exact
final occurrence provenance, nasalization, unchanged earlier tokens,
avagraha positions, grammatical accent metadata, identity and upadeśa,
non-reapplication and negative genders/nonstems/changed stems. Existing
focused noun derivations pass. No new shared helper was required; the rule
uses the existing occurrence-preserving replacement API. Full
`test --no-parallel` and `git diff --check` pass. Both unannotated CONTINUATION
corpora were rerun: before/after unchanged, Aṣṭādhyāyī default 875/1820
pass/fail and alternatives 1148/1547, four input errors and one skip, exact
610/630; UoH default 4659/4705 and alternatives 5451/3913, zero errors and
four skips, exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
1.1.20's घु placeholder still needs authentic implementation. No commit;
the full goal remains active.

### Authentic घु designation and registry activation, 2026-10-16

Replaced GhuSutra's unchanged-state placeholder and घि proxy condition.
It now assigns the existing GHU saṃjñā to the six exact lexical upadeśas
डुदाञ्, दाण्, दो, देङ्, डुधाञ्, धेट्, independently of their mutable surface.
The homophonous दाप्/दैप् entries and unproven bare दा/धा spellings do not
match. The previously unregistered rule is included in Adhyaya1Pada1 and
therefore the executable/runtime registries. Corrected the missing final
virāma in the displayed canonical text. No output mutation or new helper.

Source: [Kāśikā on दाधा घ्वदाप्](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.1.20.htm).
Four designation tests cover all six identities, changed surfaces, both
excluded roots, nonroot lookalikes, existing घि coexistence, idempotence,
registry inclusion and one real application on Dhātupāṭha दा/धा entries
through the designation-only engine. Existing verbal paradigm tests pass.

A separate high-level दा present-tense probe exposed the existing incomplete
plan validation (required 7.3.84 missing); this batch does not claim that
complete paradigm is implemented or bypass its validator. Full
`test --no-parallel` and `git diff --check` pass. Fresh unannotated CONTINUATION
before/after results are unchanged: Aṣṭādhyāyī default 875/1820 pass/fail,
alternatives 1148/1547, four input errors and one skip, exact 610/630;
UoH default 4659/4705, alternatives 5451/3913, zero errors and four skips,
exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
No commit; the full goal remains active.

### Exact pronoun-su deletion and attested examples, 2026-10-16

Migrated 6.1.132's final-su deletion from in-rule rendering/whole-surface
substitution to deleteTermVarnas at the exact final occurrence. Corrected
its displayed text to एतत्तदोः सुलोपोऽकोरनञ्समासे हलि. Existing simple
सः/एषः applicability is unchanged, including s and visarga carriers.
Focused tests verify surviving nasalized tokens, avagraha positions, term
identity/upadeśa, untouched follower, exact deletion trace and no repeat.
Negative examples cover vowel followers, other pronouns, ka forms and
visible negative compounds. Full grammatical compound/head provenance
remains a separate applicability gap, not certified by these spelling tests.

Four [Kāśikā examples](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.132.htm)
(स/एष before ददाति/भुङ्क्ते) expand native sandhi data from 379 to 383,
all passing. Stored expected forms follow the existing joined-word rendering
policy; no external corpus expectations or normalization were changed.
The existing exact deletion helper was reused, with no new shared abstraction.
Focused rule/transformation and all 383 native benchmark tests pass. Full
`test --no-parallel` and `git diff --check` pass. Both unannotated CONTINUATION
corpora were rerun with unchanged before/after results: Aṣṭādhyāyī default
875/1820 pass/fail, alternatives 1148/1547, four input errors and one skip,
exact 610/630; UoH default 4659/4705, alternatives 5451/3913, zero errors and
four skips, exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
No commit; full goal remains active.

### Three occurrence migrations and false 3.3.56 removal, 2026-10-16

Deleted EchaIgGhanSutra and its registry entry: it falsely attributed an
ec→ik operation to 3.3.56, whose actual text is एरच् and whose operation is
अच् prescription. Removed the test asserting that invented behavior; retained
all unrelated tests and grammatical output expectations. Registry regression
now rejects the vacant 3.3.56 slot until the authentic rule is implemented.
No renamed fallback or replacement output lookup was introduced.
Source: [Kāśikā on 3.3.56](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/3/3.3.56.htm).

Migrated 3.4.100 and 6.1.66 deletion to exact deleteTermVarnas, and 6.1.79
o/au replacement to replaceTermVarna. This removes their whole-surface
round trips, preserving unaffected tokens, signs and exact substitution
positions. 3.4.100 handles the affix and joined-aṅga carrier through the same
operation instead of constructing a whole-affix replacement.

Added shared Pratyahara.VAL. 6.1.66 previously tested HAL, incorrectly
including following y; both its predicate and operation now test VAL.
Existing domain gates remain unchanged. Internal-word and vṛścādi-domain
coverage still requires an applicability audit; this batch does not broaden
the rule merely to match a corpus spelling.
Sources: [6.1.66](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.66.htm),
[6.1.79](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.79.htm).

82 focused migration, verbal paradigm, surviving batch and registry tests pass.
New tests cover VAL membership, y-follower exclusion, nasalized-vowel/sign
preservation, exact indices and independent affix identity. Full
`test --no-parallel` and `git diff --check` pass. Fresh unannotated CONTINUATION
before/after scores are unchanged: Aṣṭādhyāyī default 875/1820 pass/fail,
alternatives 1148/1547, four input errors and one skip, exact 610/630;
UoH default 4659/4705, alternatives 5451/3913, zero errors and four skips,
exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
Remaining whole-surface call sites include numeral/compound substitutions,
4.1.42, 3.1.12 and 7.3.78; exact correspondence and source applicability
need individual review. The authentic 3.3.56 remains unimplemented.
No commit; full goal active.

### Shared numeral occurrence substitutions, 2026-10-16

Migrated 6.3.47 and optional 6.3.49 from whole-output strings to shared
substituteCompoundNumeral exact Varṇa-range operations. Current stems are
represented by CompoundNumeralStem; following-number inventory uses
CompoundNumeralUttara with cached declaration-time phonology. Rules inspect
term.varnas, never parse or slice the mutable surface. Prefix tokens remain
unchanged; the replaced vowel supplies the surviving vowel's annotation.
The exact source occurrence/range replaces former synthetic ∅ trace records.

Both rules now exclude explicit BAHUVRIHI contexts, previously omitted despite
the inherited restriction. Mandatory 10/20/30 and optional 40/50/60/70/90
domains remain disjoint; 80, 100 and 1000 stay excluded. No expanded runtime
fallback or bare spelling-derived compound semantics were introduced.
Sources: [Kāśikā on 6.3.47](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.3.47.htm),
[Nyāsa 6.3.47–49](https://sa.wikisource.org/wiki/काशिकाविवरणपञ्जिका/षष्ठोऽध्यायः/तृतीयः_पादः).

Focused numeral and existing compound-transformation tests pass, including
all configured upper-number domains, negative compound type, annotated
द्वि/अष्टन्/त्रि transformations, exact trace positions, no reapplication,
and both optional branches. Full `test --no-parallel` and `git diff --check`
pass. Fresh unannotated CONTINUATION before/after counts are unchanged:
Aṣṭādhyāyī default 875/1820 pass/fail, alternatives 1148/1547, four input
errors and one skip, exact 610/630; UoH default 4659/4705, alternatives
5451/3913, zero errors and four skips, exact 4251/4312. Declared-lexicon and
AVASANA modes were not rerun. No commit; full goal remains active.
The separate 6.3.76 आदुक् lifecycle, feminine numeral allomorphs and remaining
whole-surface root substitutions are not certified by this batch.

### Feminine numeral allomorph occurrence mapping, 2026-10-16

Migrated 7.2.99 त्रिचतुरोः स्त्रियां तिसृचतसृ to current Varṇa-list
predicates and exact range substitutions. The unchanged prefix survives;
the changed range explicitly maps old vowel and repha annotations to the
new allomorph occurrences. Eliminated whole-output strings and synthetic
∅/स्त्रीआदेश trace material. Retained typed sup provenance, dropped-sup
support, current-shape/non-reapplication guard and existing gender domain.
No new shared helper was required.

16 focused numeral, sup-origin and noun-derivation tests pass. New tests
cover nasalized forms, avagraha placement, exact changed positions,
original-surface trace snapshots, identity/upadeśa, untouched affix,
renamed/dropped sup and negative gender/already-substituted cases. Written
spans are excluded from token-equality assertions because त्
before र् and ति have different spans in their respective renderings.

Source: [Kāśikā on 7.2.99](https://avg-sanskrit.org/sutras/7-2-99.html).
The source qualifies the numeral's feminine referent, not necessarily the
whole compound's gender. Per-head compound semantic gender is not modeled
by this migration and remains a genuine applicability gap. No broad
whole-compound predicate was introduced to conceal it. Full
`test --no-parallel` and `git diff --check` pass. Fresh unannotated CONTINUATION
before/after counts are unchanged: Aṣṭādhyāyī default 875/1820 pass/fail,
alternatives 1148/1547, four input errors and one skip, exact 610/630;
UoH default 4659/4705, alternatives 5451/3913, zero errors and four skips,
exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
No completion claim for the larger semantic domain. No commit; goal active.

### Root/stem occurrence migration, 2026-10-16

Migrated the implemented स्था→तिष्ठ member of 7.3.78 to cached Varṇa
matching and an explicit whole-root Varṇa-range substitution, retaining the
source vowel's nasalization and grammatical accent on the substituted vowel.
Other members of the rule's root list are not claimed implemented. Migrated
3.1.12's final consonant deletion to deleteTermVarnas, leaving raw क्यङ् and
its independent it lifecycle unchanged. Focused root/stem, verbal-paradigm
and gaṇa tests pass. Full `test --no-parallel` and `git diff --check` pass.
Both unannotated CONTINUATION corpora were rerun unchanged: Aṣṭādhyāyī
875/1820 default pass/fail and 1148/1547 alternatives, four input errors,
one skip; UoH 4659/4705 and 5451/3913, zero errors, four skips. Exact counts
remain 610/630 and 4251/4312. No commit.

### Explicit raw ādeśa carrier lifecycle, 2026-10-16

At the user's request, modeled TermKind.ADESHA separately from pratyaya,
āgama and target stem. AdeshaTarget records the target ID, original Varṇa
snapshot and producer-selected placement. The first licensed placement is
NGIT_FINAL; ṅit flags alone never select it. AdeshaOrigin links the integrated
stem to its archived carrier, prescribing and targeting sūtras, historical
source occurrence, raw upadeśa and exact it-marker provenance.

4.1.126 now introduces raw इनङ्, including its operative अ, and raw ढक्.
It no longer inserts bare इन् or claims marker lopa already happened.
1.3.3 and 1.3.9 independently establish and consume ङ्. Registered 1.1.53
integrates only a processed, proven, explicitly licensed carrier at the
original final occurrence. Stale targets fail instead of falling back.
The stem keeps its identity/upadeśa and does not inherit the carrier's ङित्
marker as a stem property. The carrier retains its own complete archive.

Constructor and completion guards reject missing/misclassified targets and
unintegrated carriers. Integration is eligible even alongside two completed
padas, while archived carriers do not permanently suppress external sandhi.
Existing it-bearing-carrier drop mechanics explicitly include ADESHA without
assigning pratyaya saṃjñā. No fabricated sūtra identity or output lookup.

Six lifecycle tests pass, alongside existing gaṇa and taddhita tests. They
cover the untouched target on introduction, raw and processed phases,
designation/lopa/targeting order, exact archive links, annotation/sign
preservation, stale target rejection, unproven marker flags, ordinary
ङित् affixes such as तातङ्, constructor/completion guards, and full-registry
4.1.126→it processing→1.1.53→6.4.148 अ-lopa execution. The old unit test
asserting immediate finished इन् now checks raw introduction instead; final
grammatical output expectations were not rewritten to accommodate a shortcut.

Sources: [4.1.126 Kāśikā](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/4/4.1.126.htm),
[1.1.53 Kāśikā/Nyāsa, including the तातङ् exception](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/1/1.1.53.htm).
This implements explicit final-only ṅit targeting, not every possible
ādeśa-placement policy or the entire taddhita system. Final full
`test --no-parallel` and `git diff --check` pass. Both unannotated CONTINUATION
corpora were rerun: before/after unchanged, Aṣṭādhyāyī default 875/1820
pass/fail, alternatives 1148/1547, four input errors and one skip, exact
610/630; UoH default 4659/4705, alternatives 5451/3913, zero errors and four
skips, exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
No commit; full goal remains active.

### False 6.3.76 eka-daśa shortcut removed, 2026-10-16

Deleted EkadishCaikasyaCadukSutra and its registry entry. Its text was a
genuine canonical sūtra, but its implemented domain/operation were false:
it changed eka+daśa directly to ekā+daśa and called that āduk. Kāśikā 6.3.76
instead governs eka-prefixed nañ construction, retention of nañ and a
real aduk augment (ekānnaviṃśati/ekād naviṃśati). Nyāsa explains ekādaśa's
āttva by yogavibhāga under 6.3.46, not by this false operation.
Sources: [Kāśikā 6.3.76](https://sa.wikisource.org/wiki/काशिका/षष्ठोऽध्यायः/तृतीयः_पादः),
[Nyāsa 6.3.46/76](https://sa.wikisource.org/wiki/काशिकाविवरणपञ्जिका/षष्ठोऽध्यायः/तृतीयः_पादः).

Registry/domain-view tests pass. The existing cardinal and ordinal numeral
tests now expose ekadaśa instead of expected ekādaśa: two SankhyaGeneratorTest
failures in the focused run. Expected grammatical forms remain unchanged;
no output table, alternate false number or renamed fallback was introduced.
This is an unfinished authenticity repair, not a green-suite checkpoint.

The source audit now finds no calls to substituteTermSurface or
replaceWholeTermSurface in ashtadhyayi main Kotlin sources. This is only a
call-site inventory, not proof that every condition, source attribution or
derivation is authentic. Full `test --no-parallel --continue` fails with
three tests: cardinal and ordinal eleven, plus the reproducible example
rendering test (ekadaśaṃ instead of ekādaśaṃ). Registry tests and native
sandhi tests pass; `git diff --check` passes. Both unannotated CONTINUATION
corpora were rerun unchanged before/after: Aṣṭādhyāyī default 875/1820
pass/fail, alternatives 1148/1547, four input errors and one skip, exact
610/630; UoH default 4659/4705, alternatives 5451/3913, zero errors and four
skips, exact 4251/4312. Declared-lexicon and AVASANA modes were not rerun.
Do not commit or restore the false rule to hide the regression. Authentic
ekādaśa āttva and the distinct 6.3.76 nañ/aduk construction remain required.
No commit; full goal remains active.

### Attested eka–daśa āttva (2026-10-16)

Repaired the exposed eleven derivation using Nyāsa's explicit `āt`
yogavibhāga under 6.3.46, not the removed false 6.3.76 operation.
Source: https://sa.wikisource.org/wiki/काशिकाविवरणपञ्जिका/षष्ठोऽध्यायः/तृतीयः_पादः
(6.3.46 discussion; cites 5.3.49 as evidence for ekādaśa).
Added typed `SutraSource.Yogavibhaga` with parent, split text, commentary
and reference, portable codec support and parent/identity/order validation.
The extension is registered separately from canonical pāṭha entries under
`yogavibhaga:6.3.46:eka-dasa-atva`. It requires numeral-composition provenance
on both lexical members and replaces only eka's final A with AA through
the canonical occurrence operation. No final-output lookup is used.

Source codec, existing numeral and execution regressions pass. Three new
tests cover construction/lexical exclusions, annotation and source trace
preservation, non-reapplication and separate registry ownership. Full suite
passes, recovering the three previously exposed eleven failures.
Both unannotated CONTINUATION corpora were rerun: all before/after counts
are unchanged. Aṣṭādhyāyī default 875 pass / 1820 fail / 4 errors / 1 skip
(610 exact); alternatives 1148 / 1547 / 4 / 1 (630 exact). UoH default
4659 / 4705 / 0 / 4 (4251 exact); alternatives 5451 / 3913 / 0 / 4
(4312 exact). Declared-lexicon and AVASANA modes were not rerun.

Remaining: genuine 6.3.76 needs nested nañ-compound and aduk-augment
provenance, not a substitute-carrier shortcut. Catalog validation also
exposes pre-existing noncanonical 1.4.24V and 1.4.42V source ownership
issues; this batch does not suppress them. The full migration/benchmark
goal remains active. No commit.

### Corpus lexical evidence: etad (2026-10-16)

Expanded the pronoun regression to all 84 feminine case forms and 252
forward nominal requests for kim, yad, tad and etad. All six focused tests
passed, including archived feminine-affix and it provenance checks.
Declared etad in the lexical inventory only after these checks; eshah
is generated from its nominal derivation, not stored as a surface alias.
No sutra, scoring normalization or expected corpus answer was changed.

CONTINUATION with declared lexical analysis, before -> after:

| Corpus/mode | Pass | Fail | Errors | Skipped | Exact |
| --- | --- | --- | --- | --- | --- |
| Ashtadhyayi default | 883 -> 884 | 1812 -> 1811 | 4 | 1 | 615 -> 616 |
| Ashtadhyayi alternatives | 1161 -> 1162 | 1534 -> 1533 | 4 | 1 | 635 -> 636 |
| UoH default | 4823 -> 4838 | 4541 -> 4526 | 0 | 4 | 4363 -> 4375 |
| UoH alternatives | 5622 -> 5637 | 3742 -> 3727 | 0 | 4 | 4425 -> 4437 |

The evaluator also retains every failed row with some forward lexical
evidence in provenance-failures reports. These are evidence inventories,
not claims that the unresolved boundary or expected output is correct.
Unknown lexical sources, quoted grammatical homographs, orthographic
joiners and incomplete supplied contexts remain distinct audit concerns.

Verification: all four corpus runs completed, the complete Gradle test
suite passed (77 tasks, 1m 55s), and git diff --check passed. No commit.
The overall migration and corpus goal remains active.

### Corpus lexical evidence: agni (2026-10-16)

Kāśikā on 6.1.113 independently attests अग्निरत्र as the counterexample
to preceding अ: https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.113.htm
Added masculine अग्नि only after forward generation of all 21 nominal
requests completed without failures or unresolved visarga sources.
The new regression checks full source derivation retention, established
rutva, अग्निः + अत्र -> अग्निरत्र and nonapplication of 6.1.113.
All nine focused DerivedVisargaLexicon tests passed.
No new sutra, output alias or scoring normalization was introduced.

Declared-lexicon CONTINUATION results, before -> after:

| Corpus/mode | Pass | Fail | Errors | Skipped | Exact |
| --- | --- | --- | --- | --- | --- |
| Ashtadhyayi default | 884 -> 884 | 1811 -> 1811 | 4 | 1 | 616 -> 616 |
| Ashtadhyayi alternatives | 1162 -> 1162 | 1533 -> 1533 | 4 | 1 | 636 -> 636 |
| UoH default | 4838 -> 4845 | 4526 -> 4519 | 0 | 4 | 4375 -> 4382 |
| UoH alternatives | 5637 -> 5644 | 3727 -> 3720 | 0 | 4 | 4437 -> 4444 |

Remaining agni-containing failures retain distinct causes: UoH 5526
has unresolved धूमः, 5571 unresolved अनुष्णः and editorial avagraha,
and 1376 includes editorial avagraha after यथा. These are not patched
by guessing the source of another word or altering normalization.

Verification: all four corpus runs and the full test suite passed
(77 tasks, 1m 46s); git diff --check passed. No commit. The complete
corpus and Varṇa migration goal remains active, including exact
substitution/coalescence provenance in older pronoun implementations.

### Exact tyadadi substitution audit (2026-10-16)

Current 7.2.102 combines the final consonant replacement and the ensuing
internal a + a coalescence in one legacy whole-term substitution. It
does not yet prove the exact final occurrence or separately trace 6.1.97.
Kāśikā and Bālamanoramā explicitly describe the final substitution:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.102.htm

An exact-occurrence experiment exposed the intermediate ya-a stem.
The focused 84-feminine-form/252-forward-request regressions caught:

- Feminine formation introduced tap before internal coalescence; the
  trace then used 6.1.87 and produced yah rather than yaa.
- Delaying tap alone was insufficient: Sup substitutions still acted
  on the unfinished base, yielding ye for feminine nominative plural
  and incomplete feminine dative derivations.

The experimental production edits were removed rather than retaining
regressions or folding coalescence under an incorrect sutra number.
The nominal test now includes full application traces on disagreement.
The required next model change is an explicit pending base-composition
dependency that precedes both feminine formation and Sup substitution;
idam and dvi additionally require their independent substitution steps.
This is an incomplete migration item, not a completed provenance fix.
No benchmark normalization or expected output changed in this audit.

### Pending base composition implementation (2026-10-16)

Implemented the dependency identified above rather than leaving the
ordinary tyadadi path on whole-term mutation. PendingBaseComposition
records an exact substitution-exposed vowel pair, its index and origin.
The engine permits completion of this dependency before using the base
for either feminine formation or Sup operations; it does not hardcode
rule numbers, lexical spellings or expected forms in the scheduler.
Final completion rejects surviving pending base compositions.

Ordinary 7.2.102 now replaces its exact final occurrence with a.
Registered 6.1.97 then independently coalesces the pending internal
pair, retaining the following vowel's annotations and projecting signs
and provenance through the existing exact-range transformation.
Seven corpus-pronoun tests and four PronominalStems tests passed,
including all 84 feminine forms, 252 forward requests and separate
7.2.102/6.1.97 occurrence traces preceding tap introduction.
No new sutra was invented. The idam/dvi special branches are being
audited separately against their independently prescribed operations.

The subsequent coherent batch removed both special branches from
7.2.102: idam and dvi now use the same exact final-varna replacement.
Added and registered genuine 7.2.109 दश्च, independently replacing
the d occurrence of idam with m under the retained case-affix condition.
Nyāsa confirms that the condition is vibhakti, not only su:
https://sa.wikisource.org/wiki/काशिकाविवरणपञ्जिका/सप्तमोऽध्यायः/द्वितीयः_पादः

Corrected 7.2.111's text to इदोऽय् पुंसि and migrated its condition to
cached varnas and typed current Sup identity. Its exact two-varna
replacement preserves the following annotated a/m carrier instead
of constructing a whole-word ayam substitution. Siddhāntakaumudī 344
explicitly prescribes the id portion:
https://sa.wikisource.org/wiki/सिद्धान्तकौमुदी/प्रकरण‌११-२०

Focused coverage now includes separate exact 7.2.102/6.1.97/7.2.109
traces for imau/ime; 21 dual-case requests for dvi across all genders,
including genuine feminine tap; and preserved following nasalization
and avagraha in 7.2.111 with renamed typed su and a negative fake-su
test. All ten corpus-pronoun tests and four PronominalStems tests pass.

Final verification: full suite passed (77 tasks, 1m 37s) and all four
declared-lexicon CONTINUATION corpus runs completed without regression.
Before and after: Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip,
alternatives 1162/1533/4/1; UoH default 4845/4519/0/4,
alternatives 5644/3720/0/4. Exact counts remain 616/636 and 4382/4444.
git diff --check passed. No commit.

This completes the identified pending-composition implementation, not
the whole migration or all idam paradigms: the independent 7.2.108,
7.2.110, 7.2.112 and 7.2.113 domains, quoted grammatical homographs,
unknown lexical sources and other corpus rule gaps still need their
own evidence-backed implementations and verification. The goal remains
active; these are not represented as completed or bypassed by fallback.

### Grah samprasāraṇa occurrence migration (2026-10-16)

Migrated the implemented grah/śnā branch of 6.1.16 from rendered-form
equality and whole-term reconstruction to cached Varṇas and an exact
RA → R occurrence replacement. 6.1.108 now locates the samprasāraṇa
occurrence from the 6.1.16 trace and performs an exact two-vowel pūrvarūpa
range replacement, retaining the first vowel's annotations. It does not
infer samprasāraṇa from an unrelated vocalic ṛ. Reused existing positional
helpers; no new shared abstraction or sūtra registration was needed.
Sources: Kāśikā on
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.16.htm
and https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.108.htm.
Other roots and kit/ṅit constructions of 6.1.16 remain unimplemented; this
batch does not claim complete samprasāraṇa coverage or general historical
occurrence tracking across arbitrary intervening mutations.

Focused verification: 3 new occurrence/annotation/domain tests and all
65 TingantaEngineTest tests pass. Full `test --no-parallel` passes and
`git diff --check` passes. Both unannotated CONTINUATION corpora were rerun;
before/after counts are unchanged: Aṣṭādhyāyī default 875/1820 pass/fail,
alternatives 1148/1547, 4 errors and 1 skip, exact 610/630. UoH default
4659/4705, alternatives 5451/3913, 0 errors and 4 skips, exact 4251/4312.
Declared-lexicon and AVASANA modes were not rerun. The four input errors
remain explicitly reported parenthesized-commentary rows, not normalized
away. Large visarga groups require trustworthy lexical/source provenance;
orthographic differences are not permission to fabricate phonology.

Remaining rendered-form guards include 7.1.1 (yu/vu affix substitution),
7.2.82 (processed āna and augment lifecycle), and 6.1.67 (processed kvip).
These need affix applicability/lifecycle review, not renamed Unicode
helpers. Full goal remains active; no commit.

### Yuv affix convention and 7.1.1 (2026-10-16)

The 7.1.1 source audit found that plain processed yu/vu is insufficient:
Kāśikā and Nyāsa specify conventionally nasalized yaṇ affixes and exclude
roots and nonnasal yus-type affixes. Source:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.1.htm.
Added `YuvAffixConvention` lexical declarations for lyu/lyuṭ/ṭyuṭ/ṭyul and
ṇvul/vun/vuñ, plus support for an explicitly nasalized yaṇ in cached raw
upadeśa text. The current term must be a processed pratyaya with no pending
exact it-designations and the correct cached yu or vu token sequence.
Ordinary roots, unproven yu, nonnasal yus, mismatched classes and raw
carriers cannot enter the operation. 7.1.1 uses typed Varṇa list mappings
through 1.3.10 and an exact range substitution, preserving stable identity,
historical it proof and written signs. No output table or runtime fallback.

All 32 focused tests pass: 4 new convention/provenance/domain tests,
12 existing remaining-affix lifecycle tests and 16 KrdantaEngine tests.
Full `test --no-parallel` and `git diff --check` pass. Both unannotated
CONTINUATION corpora were rerun unchanged before/after: Aṣṭādhyāyī default
875 pass / 1820 fail / 4 errors / 1 skip (610 exact); alternatives
1148 / 1547 / 4 / 1 (630 exact). UoH default 4659 / 4705 / 0 / 4
(4251 exact); alternatives 5451 / 3913 / 0 / 4 (4312 exact).
Declared-lexicon and AVASANA modes were not rerun.

Remaining deeper modeling: pronunciation-only vowels in legacy raw
affixes, kvip's actual apṛkta v versus the current vi carrier (6.1.67),
and muk versus the current muṭ carrier/placement (7.2.82). Those were not
silently rewritten or claimed fixed by this batch. Existing 4.1.4 ajādi
lexical membership and processed-a guard also remain to audit. Full goal
remains active; no commit.

### Authentic muk carrier and final-a locus (2026-10-16)

Corrected 7.2.82 from a fabricated muṭ prefix of āna to kit muk at the
preceding aṅga's final short a. Kāśikā/Nyāsa explicitly make muk dependent
on the a occurrence; Bālamanoramā identifies k as it and u as pronunciation-only:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.82.htm.
Added KitAugment.MUK using the existing raw-term pronunciation-only
projection, and explicit augmentTargetVarnaIndex/augmentTargetVarna proof.
1.1.46 validates that this explicitly targeted kit locus is still final
and has its prescribed value, refusing stale proof rather than relocating
by guesswork. The augment retains its own term/it ledger; it does not
leak kit designation into the preceding a or śānac. The āna predicate now
uses cached Varṇas and requires a preceding a-final operative term;
imperative śānac substitutions remain excluded.

All 22 focused tests pass (3 new locus/marker/domain tests, 12 lifecycle,
1 śnā provenance and 6 participle tests). Existing labhamāna final output
is unchanged. Two direct-operation fixtures now supply the missing
processed śap/a grammatical premise; their old muṭ-prefix model was not
retained as a fallback. Full `test --no-parallel` and `git diff --check`
pass. Both unannotated CONTINUATION corpora were rerun unchanged
before/after: Aṣṭādhyāyī default 875/1820 pass/fail, alternatives 1148/1547,
4 errors and 1 skip, exact 610/630; UoH default 4659/4705, alternatives
5451/3913, 0 errors and 4 skips, exact 4251/4312. Declared-lexicon and
AVASANA modes were not rerun.

Remaining: kvip's pronunciation-only i/apṛkta carrier, broader raw-affix
pronunciation annotations, complete vowel-dependent accent inheritance
(including 6.1.180), and remaining rendered-form applicability guards.
The exact-locus support here is final kit placement, not arbitrary
historical token tracking or a complete accent implementation. Full goal
remains active; no commit.

### Kvip pronunciation projection and apṛkta lopa (2026-10-16)

Added KrtAffix.KVIP with pronunciation-only i at raw upadeśa token index 2,
and a KrtAffix.rawTerm boundary that uses the existing source-segment
projection. Both current kvip prescribing implementations now use it.
Source for the pronunciation-only i and single-v apṛkta interpretation:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.67.htm
(Bālamanoramā; Kāśikā/Nyāsa explain the kvibādi class and Uṇādi exclusions).
The raw operative carrier is K/V/P, preserving full kvip upadeśa and
the explicit nonoperative i source span. After exact k/p it processing,
6.1.67 matches only the single operative VA in the implemented kvip branch
and deletes that occurrence. It no longer accepts a vi syllable as apṛkta.
Reused deleteTermVarnas for positional tracing and written-sign projection;
historical marker proof and term identity survive the lopa.

All 31 focused tests pass (3 new carrier/domain/sign tests, 12 lifecycle,
16 KrdantaEngine). Full `test --no-parallel` and `git diff --check` pass.
Both unannotated CONTINUATION corpora rerun unchanged before/after:
Aṣṭādhyāyī default 875/1820 pass/fail, alternatives 1148/1547, 4 errors and
1 skip, exact 610/630; UoH default 4659/4705, alternatives 5451/3913,
0 errors and 4 skips, exact 4251/4312. Declared-lexicon and AVASANA modes
were not rerun. No expected final grammatical output was changed.

Remaining: generic TypedAffix.term construction does not yet honor the
new pronunciation-only metadata; prescribing rules use the explicit raw
factory. Complete kvibādi coverage needs kvin/ṇvi/Uṇādi provenance rather
than a broad v-ending predicate. The existing broad root predicates and
abbreviated text in kvip-prescribing rules also need separate source-backed
applicability repair; this carrier batch does not authenticate them.
Full goal remains active; no commit.

### Corpus-focused lexical provenance expansion (2026-10-16)

At the user's request, further morphology migration was paused in favor
of the corpus. The pending constructor gap was closed first: TypedAffix
exposes pronunciation-only indices, and generic term construction uses
the same source-segment projection as the raw factory, with correct
augment/pratyaya kind and no invented prescribing source. Two constructor
tests and the full suite pass.

The corpus failure audit still finds unknown visarga source as a major
input-evidence gap. Added four independently attested masculine lexical
declarations: vṛkṣa/plakṣa from Kāśikā 6.1.113, puruṣa from Kāśikā 6.1.114,
and Śiva from the Siddhāntakaumudī/Bālamanoramā examples on these rules:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.113.htm
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.114.htm.
These are lexical stems, not stored sandhi outputs, surface aliases or
inferred s/r endings. Every candidate is generated through all relevant
nominal cases with full derivation/source proof, preserving ambiguity.
No new sūtra is required for this lexical batch; existing 6.1.113/114
execute once their independently established rutva prerequisite is present.

Reran both corpora in explicit declared-lexicon CONTINUATION mode,
comparing with the preceding recorded declared-lexicon baseline:

| Corpus/mode | Pass before → after | Fail before → after | Exact before → after |
| --- | --- | --- | --- |
| Aṣṭādhyāyī default | 876 → 876 | 1819 → 1819 | 611 → 611 |
| Aṣṭādhyāyī alternatives | 1150 → 1150 | 1545 → 1545 | 631 → 631 |
| UoH default | 4692 → 4709 | 4672 → 4655 | 4275 → 4283 |
| UoH alternatives | 5485 → 5502 | 3879 → 3862 | 4336 → 4344 |

Aṣṭādhyāyī retains 4 input errors and 1 skip; UoH retains 0 errors and
4 skips. UoH lexical coverage increased from 56 to 82 analyzed rows,
58 to 84 word occurrences. Aṣṭādhyāyī increased from 6 to 8 analyzed
rows/occurrences, without additional complete-row matches. These are
provenance-aware mode scores, not replacements for unannotated scores.
Unannotated and AVASANA modes were not rerun in this batch. Expectations,
normalization, skips and optional-branch evaluation are unchanged.

The forward-provenance regression test passes, all declared candidates
have no hidden derivation failures/unresolved source, and full
`test --no-parallel` plus `git diff --check` pass. Remaining corpus work
includes other independently sourced vocabulary, genuine rule gaps and
orthographic/editorial discrepancies; unknown sources are not guessed.
Full goal remains active. No commit.

### Numeral shapes and typed jas-shas identities (2026-10-16)

Migrated 7.1.21 and 7.1.22 away from literal surface membership and
display-name affix selection. PrimitiveSankhya now caches its canonical
pratipadika Varna list once at the lexical boundary. The rules reuse
those typed canonical shapes with explicit aṣṭa/aṣṭā and ṣaṭ allomorphs;
no changing term surface is reparsed. The precise earlier numeral set
is retained, not broadened to the five-to-nineteen inflection class.
Existing ṣaṭ designation and lexical upadesha evidence remain independent.

Both prescriptions require actual sourceSupAffix JAS/SAS and its current
affix identity. Renamed genuine affixes work; fake display IDs and an
already substituted śi/auś do not reselect the operation. Auś still uses
fresh-upadesha processing, and luk still archives the original typed Sup.

Three new NumeralSupIdentity tests and four PronominalStems tests pass:
annotated aṣṭan/aṣṭa/aṣṭā, all seven earlier ṣaṭ shapes, unchanged stem
carriers, correct archived Sup provenance, no repeat selection and negative
fake/wrong/already-substituted affix cases. No sutra or expected corpus
output was added or changed. This batch migrates representation and
identity selection, not a new grammatical expansion of numeral domains.

The remaining literal surface equality found in adhyāya 7 is 7.3.84's
liṭ processing-state guard (surface == upadesha). It requires lifecycle
phase/provenance auditing rather than a renamed string helper. Compound
accent and quoted grammatical homographs also remain explicit work.

Verification completed: all four corpus runs and full test suite passed
(77 tasks, 1m 53s); git diff --check passed. Before/after unchanged:
Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip, alternatives
1162/1533/4/1; UoH default 4861/4503/0/4, alternatives 5661/3703/0/4.
Exact counts remain 616/636 and 4388/4450. Full goal remains active;
no commit.

### Explicit lit guna readiness (2026-10-16)

Replaced 7.3.84's literal surface == upadesha liṭ guard with explicit
itProcessingPending, active it-designation and deferred-designation
checks. Equal spelling does not prove unfinished processing: a processed
vowel-only ending can legitimately retain its upadesha spelling.
The existing raw/designated/processed lifecycle is reused; no renamed
Unicode helper or rule-specific surface exception was introduced.

New LitGunaReadiness regression keeps spelling and upadesha identical
while varying only the processing phase. Raw material is ineligible,
processed material is eligible and its exact final-vowel substitution
preserves the ending carrier. All 67 focused tests pass: this regression,
65 TingantaEngine tests and the KriLitDerivation test, including supported
perfect paradigms. No adhyāya-7 literal surface comparison or string
phonological-operation match remained in the targeted rg audit; this
is evidence for that search, not proof of the complete migration.

The investigation also identified a separate grammatical/provenance gap:
3.4.82 currently supplies processed a rather than raw ṇal for MIP, and
7.1.91 णलुत्तमो वा is not registered. Bālamanoramā explicitly describes
MIP's ṇal and its optional ṇit status. Addressing it requires retaining
the actual ṇ-it designation while modeling its optional grammatical
effect separately; deleting historical it provenance is not a valid fix.
https://sa.wikisource.org/wiki/सिद्धान्तकौमुदी_(बालमनोरमा_उत्तर-१)
The readiness migration does not falsely certify those operations.

Verification: all four corpus modes completed unchanged, full suite
passed (77 tasks, 1m 38s), and git diff --check passed.
Ashtadhyayi default remains 884 pass/1811 fail/4 errors/1 skip,
alternatives 1162/1533/4/1; UoH default 4861/4503/0/4,
alternatives 5661/3703/0/4. Exact counts remain 616/636 and 4388/4450.
The broader goal, including the identified perfect-affix marker-effect
model and genuine 7.1.91 coverage, remains active. No commit.

### Raw first-person nal and optional marker-work modeling (2026-10-16)

Resolved the preceding identified gap: 3.4.82 now introduces genuine
raw ṇal for MIP with FreshUpadesha, as it already did for TIP. Initial
ṇa and final l receive actual it designation and lopa. The liṭ guṇa
readiness guard now waits for both current TIP and MIP replacement,
not a sthānin-aware spelling check that could exclude later operative a.

Added and registered genuine 7.1.91 णलुत्तमो वा. Kāśikā supplies cakara/
cakāra and papaca/papāca; Nyāsa explicitly distinguishes optional ṇit
work from denying the ṇ-it designation established in upadesha:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.1.91.htm

Introduced ItMarkerEffectDecision, keyed by the actual marker, with a
prescribing sutra and enabled/disabled work decision. hasEffectiveMarker
consults this grammatical effect while retaining itMarkers, active and
deferred designation records, sthānin identity and completed it provenance.
FreshUpadesha clears prior effect decisions so they cannot leak into a
new affix. 7.1.91 requires genuine NIT designation/provenance, current
ṇal and source MIP; a manually supplied marker bit alone is insufficient.
Both outcomes are explicit applied grammatical decisions, not a declined
no-op with lost provenance. The default non-ṇit-work branch preserves the
existing valid first-person outputs; engine enumeration retains both.

Migrated its vṛddhi consumers 7.2.115 and 7.2.116 to exact final/upadhā
occurrence replacements, preserving nasalization and orthographic signs
instead of relying on whole-term Varna rewrites.

All 72 focused tests pass: five NalMarkerEffect tests, one readiness
regression, 65 TingantaEngine tests and one KriLitDerivation test. They
verify raw first-person ṇal, marker-proof retention on both effect choices,
actual engine enumeration of ninaya/nināya and the independently attested
cakara/cakāra, negative fake-marker and wrong-person-source cases,
fresh-upadesha reset and exact annotated vṛddhi loci. Supported default
perfect paradigms remain unchanged. No derivation result is hardcoded.

Final verification: all four corpus runs completed unchanged; after
correcting two old intermediate-table tests to require raw first-person
ṇal rather than the former processed-a shortcut, the full suite passed
(77 tasks, 1m 53s). Final-form and corpus expectations were not changed.
git diff --check passed. Ashtadhyayi default remains 884 pass/1811 fail/
4 errors/1 skip, alternatives 1162/1533/4/1; UoH default 4861/4503/0/4,
alternatives 5661/3703/0/4. Exact counts remain 616/636 and 4388/4450.
The 3.4.82/7.1.91 gap identified above is now implemented and verified,
not deferred. Broader corpus and migration work remains active. No commit.

### Corpus lexical evidence: kapi and udadhi (2026-10-17)

Independent primary dictionary transcripts identify kapi as masculine
(Śabdakalpadruma, also citing Amarakośa 2.5.4) and udadhi as masculine
(Vācaspatyam). The corpus expected forms did not supply this evidence:
https://sanskritkosha.com/?search=कपि
https://kosha.sanskrit.today/word/sa/उदधि
Direct Wikisource/Wiktionary requests were unavailable; the cited
dictionary transcriptions were used, not inferred surface genders.

Verified all 42 nominal case forms before declaring the two stems.
The forward index has no failed or unresolved visarga derivations;
actual nominative-Sup/rutva proofs derive kapirapi and udadhirapi and
retain their complete source derivations. No generated surface alias,
new sutra, runtime fallback or normalization change was introduced.
The existing shared forward nominal index supplies the corpus evidence.

CONTINUATION with declared lexical analysis, before -> after:

| Corpus/mode | Pass | Fail | Errors | Skipped | Exact |
| --- | --- | --- | --- | --- | --- |
| Ashtadhyayi default | 884 -> 884 | 1811 -> 1811 | 4 | 1 | 616 -> 616 |
| Ashtadhyayi alternatives | 1162 -> 1162 | 1533 -> 1533 | 4 | 1 | 636 -> 636 |
| UoH default | 4861 -> 4863 | 4503 -> 4501 | 0 | 4 | 4388 -> 4390 |
| UoH alternatives | 5661 -> 5663 | 3703 -> 3701 | 0 | 4 | 4450 -> 4452 |

UoH rows 94 and 189 now pass in alternatives mode. Those cases did not
need an additional sandhi sutra; their missing lexical provenance was
the actual gap. Evidence coverage is now 185 Ashtadhyayi rows/215 word
occurrences and 1991 UoH rows/2199 occurrences. All 21 focused
derived-lexicon/sequence tests pass, including both complete paradigms.

Verification completed: all four corpus runs completed and the full suite
passed (77 tasks, 2m 2s); git diff --check passed. No commit.
Also reconciled two historical statements in sandhi-modeling-gaps.md with
the current typed visarga-source and exact guṇa-transfer implementations.
Unknown lexical evidence, genuine rule gaps, quoted grammatical homographs
and independent compound/accent policies remain explicit work. The full
goal remains active; these two corrected corpus rows do not complete it.

### Corpus pronoun analysis exposed cycles and feminine-formation gap (2026-10-16)

Audited forward analyses for frequent kim/yad/tad visarga inputs across
all three genders and 21 sup slots before admitting lexical candidates.
The diagnostic exposed real cycles in neuter accusative plural yad/tad:
7.2.102 removed the num inserted by 7.1.72, then designation operations
repeated. Restricted 7.2.102 to the actual lexical ending rather than an
arbitrary newly final consonant, using cached Varṇas and stable lexical
membership. Source for the prescribed tyadādi operation and exclusions:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.102.htm.

Neuter nominative plural also exposed precedence defects: 7.1.17 could
replace jas with śī, or 7.3.103 could transform the a before jas, before
neuter 7.1.20 replaced jas with śi. Added explicit 7.1.20 blocking of those
two displaced-nimitta operations. The primary Padamañjarī discussion of
1.1.47 attests tad → tāni with jaśśasoḥ śiḥ:
https://sa.wikisource.org/wiki/काशिका_(पदमञ्जरीव्याख्यासहिता)/प्रथमोऽध्यायः/प्रथमः_पादः.
All six nominative/accusative neuter plural regressions (yāni/tāni/kāni)
now pass, and all 189 forward requests terminate without hidden source
errors. This is not a claim that every resulting paradigm form is correct.

The added gender test exposes a remaining real failure: feminine raw kim
derives kaḥ instead of kā. The prospective pronoun lexical expansion is
therefore NOT admitted, not restricted to convenient genders, and not
replaced with stored outputs. The full `test --no-parallel --continue`
run fails with this one newly exposed test; the two other new tests pass.
The existing 7.2.102 substitution implementation still needs its exact
a-order/coalescence provenance audited; only applicability/cycle scope
was repaired here, not silently certified as complete.

Both declared-lexicon CONTINUATION corpora rerun unchanged before/after:
Aṣṭādhyāyī default 876/1819 pass/fail, alternatives 1150/1545, 4 errors
and 1 skip, exact 611/631; UoH default 4709/4655, alternatives 5502/3862,
0 errors and 4 skips, exact 4283/4344. Unannotated/AVASANA modes were not
rerun. `git diff --check` passes. Next required corpus-enabling work is
authentic feminine pronoun formation/provenance, with all-gender tests
retained. This is an unfinished checkpoint, not a green suite. No commit;
full goal remains active.

### Corpus-enabling feminine pronoun formation and su-lopa (2026-10-16)

Resolved the previously exposed feminine kim failure through an explicit
ṭāp carrier, not a directly stored kā result. 4.1.4 can prescribe ṭāp
after the independently recorded 7.2.102/103 operation while sup remains
present. The carrier has a typed owner identity; the shared machine
finishes this formation dependency before downstream case operations.
It-processing is independent, and 6.1.101 coalesces the exact owned
stem–ṭāp boundary. The archived suffix retains source identity, marker
proof and its merged owner. An uncomposed owned feminine suffix cannot
pass completion validation. The ordinary earlier 4.1.4 path is unchanged.
Source order (kādeśa → ṭāp → coalescence) is attested in the Laghu
Siddhāntakaumudī discussion of feminine kim:
https://jainqq.org/pagetext/Laghu_Siddhant_Kaumudi_Part_01/006148/115.
The selected source excerpt is available in search; direct retrieval was
unavailable. 4.1.4 itself prescribes ṭāp after short-a stems in striyām.

Removed the tyadādi identity bypass of the actual short-a condition in
7.1.14/15/17, which had allowed premature smai/smāt/smin/śī substitutions
before feminine formation. The complete feminine case tests also exposed
double suṭ/nuṭ in genitive plural. 7.1.52 now introduces raw nasalized-u
suṭ, followed by actual it designation, 1.1.46 placement and 1.3.9 lopa;
its explicit domain takes precedence over 7.1.54 and retains prescribing
proof on the target. 1.1.46 archives the carrier's established marker
designations as historical provenance when merging before lopa. Source
for avarṇa-final sarvanāma suṭ and its feminine domain:
https://sa.wikisource.org/wiki/सिद्धान्तकौमुदी/प्रकरण‌०१-१० (217).
No new canonical sūtra was invented or required; these registered rules
were incomplete/incorrectly ordered.

Admitted kim/yad/tad as all-gender lexical declarations only after all
63 feminine forms, the six neuter plural forms and all 189 forward
requests passed. Newly formed analyses revealed a real sandhi regression:
decoded established ru in saḥ was invisible to 6.1.132's s/visarga-only
predicate. Added the ru form only with actual tad/etad masculine
nominative-singular provenance and an exact rutva occurrence; native
repha is explicitly excluded. Source for the su relation and exclusions:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.132.htm.
The first expanded UoH run temporarily fell to 4654 default / 5453
alternative passes; source-proven su-lopa repair corrected that regression.
No fallback to unknown-source analyses was introduced to inflate scores.

Final declared-lexicon CONTINUATION results, before → after this batch:

| Corpus/mode | Pass | Fail | Exact |
| --- | --- | --- | --- |
| Aṣṭādhyāyī default | 876 → 883 | 1819 → 1812 | 611 → 615 |
| Aṣṭādhyāyī alternatives | 1150 → 1161 | 1545 → 1534 | 631 → 635 |
| UoH default | 4709 → 4823 | 4655 → 4541 | 4283 → 4363 |
| UoH alternatives | 5502 → 5622 | 3862 → 3742 | 4344 → 4425 |

Errors/skips remain Aṣṭādhyāyī 4/1, UoH 0/4. Aṣṭādhyāyī analyzed rows
increased 8 → 40; UoH 82 → 501, word occurrences 84 → 517. Expectations,
normalization and optional-branch matching were unchanged. Unannotated
and AVASANA modes were not rerun and these scores are not attributed to
those modes. Focused provenance/pronoun/affix tests pass, and final full
`test --no-parallel` plus `git diff --check` pass. The previous feminine
test is no longer failing and was not weakened or removed.

Remaining corpus work includes unknown lexical visarga sources, actual
rule gaps, orthographic/editorial discrepancies and broader grammatical
construction coverage. Exact ādeśa/coalescence provenance in the older
7.2.102 implementation was an audit item at that checkpoint; the later
pending-composition batch above resolves its exact final substitution
and separately traced coalescence. This does not certify every pronoun
paradigm or every feminine-formation construction. Full goal remains
active. No commit.

### Idam nominative retention and feminine substitution (2026-10-16)

Added and registered genuine 7.2.108 इदमो मः and 7.2.110 यः सौ.
Kāśikā explicitly explains 7.2.108 as m in place of m to block tyadadi
atva; Siddhāntakaumudī and Bhāṣāvṛtti explicitly identify the feminine
d-to-y substitution in 7.2.110:
https://sa.wikisource.org/wiki/सिद्धान्तकौमुदी/प्रकरण‌११-२०
https://sa.wikisource.org/wiki/भाषावृत्तिः/सप्तमोऽध्यायः/द्वितीयः_पादः

Introduced retainTermVarna for an explicitly prescribed identity
substitution: the exact occurrence and original surface/sign/rutva
snapshots are recorded while all original tokens remain unchanged.
The established prescription prevents 7.2.102 from re-entering after
the immediate blocking candidate is no longer applicable.
No artificial spelling change or relaxing of ordinary substitution
validation was used. Both nominative rules use typed current su,
retained lexical identity and exact Varna positions.

All 12 CorpusPronounProvenance tests and four PronominalStems tests
passed. New coverage checks ayam/iyam, 7.2.108 retention traces,
7.2.110/111 gender separation, unchanged annotated tokens and avagraha,
repeat prevention, and negative dual and fake-su domains.

Verification completed: full test suite and git diff --check passed;
all four declared-lexicon CONTINUATION corpus runs remain unchanged:
Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip, alternatives
1162/1533/4/1; UoH default 4845/4519/0/4, alternatives 5644/3720/0/4.
No corpus input, expected output, normalization or scoring policy changed.
Idam is not yet declared for automatic corpus analysis: its oblique
cases needed the independent 7.2.112/113 prescriptions and complete
forward-paradigm verification at that checkpoint. See the following batch.
The overall goal remains active. No commit.

### Complete idam paradigms and corpus admission (2026-10-16)

Added and registered genuine 7.2.112 अनाप्यकः and 7.2.113 हलि लोपः.
Their shared selector interprets āp as the Sup pratyāhāra from instrumental
ṭā through sup, requires a processed operative suffix, retains idam's
lexical identity and the independently traced 7.2.109 substitution,
and excludes kakāra-bearing forms. Exact prefix replacement/deletion
uses Varna positions and preserves the containing term and its ledgers.
Kāśikā supplies anena/anayoh and ābhyām/ebhih/ebhyah/esām/esu:
https://sa.wikisource.org/wiki/काशिका/सप्तमोऽध्यायः/द्वितीयः_पादः

The forward regressions exposed two lifecycle bugs which were fixed:

- Prefix operations must precede the suffix-consuming 7.3.102/103
  composition steps. The applicable prescriptions now declare these
  conflicts; the composition becomes eligible again after the prefix
  operation has finished. Genitive plural prefix selection waits for
  the real 7.1.52 suṭ carrier and its retained placement/it provenance.
- 1.1.37 previously classified a transformed pronominal a carrier as
  lexical indeclinable a and then 2.4.82 incorrectly deleted its case
  suffix. Svarādi membership now uses lexical upadesha on a nominal
  carrier, not its transformed surface or a homographic case affix.

All 63 idam gender/case forms pass their independently specified full
paradigms. Expanded declared pronoun analysis to 315 forward requests,
with no failures or unresolved visarga proofs, and only then declared
idam for corpus analysis. No derived forms are stored as output aliases.
Focused set: 15 corpus-pronoun tests, 9 derived-lexicon tests and four
PronominalStems tests pass. Negative lexical-avyaya regressions preserve
the genuine svarādi word while excluding a derived a and an ām affix.

Final verification: all four corpus runs and full test suite passed
(77 tasks, 1m 47s); git diff --check passed. Before/after scores unchanged:
Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip, alternatives
1162/1533/4/1; UoH default 4845/4519/0/4, alternatives 5644/3720/0/4.
Exact counts remain 616/636 and 4382/4444. No commit.

The current DerivedVisargaLexicon intentionally indexes only final-visarga
forms; therefore non-visarga idam forms are not yet analyzed by the corpus
runner. General forward nominal evidence, including ambiguous dual
pragrhya versus plural readings, needs its own explicit model rather
than assigning a visarga source to non-visarga forms. The full migration
and corpus goal remains active; this batch does not claim all gaps closed.

### Full forward nominal evidence and pragṛhya pūrvarūpa guard (2026-10-16)

Extended the shared forward index with NominalCandidate and nominalCandidates,
retaining the existing final-visarga candidates API unchanged. Non-visarga
forms have full nominal requests, source derivations, formed-pada morphology
and samjñas but no invented VisargaSource. Failed or unresolved derivations
remain explicit errors; expected corpus outputs never influence lookup.
The corpus evaluator now selects from this full nominal view and retains
all homographic gender/case proofs through subsequent boundaries.

The new regression distinguishes dual ime atra from plural ime 'tra in
a three-pada sequence. It exposed a missing 6.1.125 block on 6.1.109:
the dual acquired a word gap but still lost the following a to pūrvarūpa.
Added the missing conflict and exact boundary block; the dual now preserves
the following vowel while the plural remains eligible for pūrvarūpa.
The same existing rule and rendering model is used, without a special
ime branch. Siddhāntakaumudī 90 describes obligatory prakṛtibhāva:
https://sa.wikisource.org/wiki/सिद्धान्तकौमुदी/प्रकरण‌०१-१०

All 33 focused sequence/lexicon/pronoun tests pass, including distinct
nominal proofs, null visarga sources on non-visarga forms, inherited
pragṛhya status, later-boundary ownership and explicit alternative-limit
failure without discarded branches. No new sutra was invented.

Final verification: all four corpus runs completed, full test suite
passed (77 tasks, 1m 38s) and git diff --check passed. Scores unchanged:
Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip, alternatives
1162/1533/4/1; UoH default 4845/4519/0/4, alternatives 5644/3720/0/4.
Exact counts remain 616/636 and 4382/4444.

Nominal evidence coverage increased from 44 to 184 Ashtadhyayi rows
(44 -> 214 analyzed word occurrences), and 585 to 1933 UoH rows
(612 -> 2134 occurrences). These evidence counts are not extra passes.
Unknown lexical sources, quoted grammatical homographs, editorial
joiners/avagrahas and continuation-versus-pause expectations remain
separate unresolved concerns. Full goal remains active; no commit.

### Corpus lexical evidence: Rama and deva (2026-10-16)

Independently sourced masculine rāma from Siddhāntakaumudī 187ff.
and deva from Śabdamañjarī's masculine nominal table, page 15:
https://sa.wikisource.org/wiki/सिद्धान्तकौमुदी/प्रकरण‌०१-१०
https://sa.wikisource.org/wiki/पृष्ठम्:शब्दमञ्जरी.pdf/15
All 42 case forms were independently asserted before admission,
including rāmeṇa versus devena and rāmāṇām versus devānām.
Forward indexing has no failures or unresolved visarga sources, and
rāmah/devaḥ + atra use established rutva with complete source results.
Declared the two stems, never their generated case forms as aliases.
The unknown-word regressions now use undeclared aghoṣita rather than
newly declared rāma; their no-guessing assertions remain intact.

CONTINUATION with declared lexical analysis, before -> after:

| Corpus/mode | Pass | Fail | Errors | Skipped | Exact |
| --- | --- | --- | --- | --- | --- |
| Ashtadhyayi default | 884 -> 884 | 1811 -> 1811 | 4 | 1 | 616 -> 616 |
| Ashtadhyayi alternatives | 1162 -> 1162 | 1533 -> 1533 | 4 | 1 | 636 -> 636 |
| UoH default | 4845 -> 4861 | 4519 -> 4503 | 0 | 4 | 4382 -> 4388 |
| UoH alternatives | 5644 -> 5661 | 3720 -> 3703 | 0 | 4 | 4444 -> 4450 |

UoH nominal evidence increases from 1933 rows/2134 occurrences to
1989 rows/2197 occurrences. Ashtadhyayi remains 184 rows/214 occurrences.
No scoring normalization, corpus expectations or grammatical rules changed.
No shared abstraction was introduced; the verified forward nominal index
and existing genuine sandhi rules provide the improvement.

UoH row 8, rāmah + hanumān + ca, now passes in alternatives mode.
The anusvāra branch already has genuine 8.3.4 and 8.3.7; no additional
sutra is needed or fabricated. The default nasalized-vowel branch is
grammatically separate and is not changed to chase the corpus spelling.
A sequence regression records the passing branch and rāma's lexical proof.

Verification: all 20 focused lexicon/sequence tests pass, including the
row-8 regression; all four corpus runs completed. Full suite passed
(77 tasks, 1m 45s), then passed again after the added sequence regression
(77 tasks, 15s). git diff --check passed. No commit.
Unknown visarga sources, actual rule gaps, quoted grammatical forms,
orthographic/editorial discrepancies and deeper provenance audits remain
in scope. The full goal remains active; the corpus is not claimed complete.

### Raw avang substitute carrier and final-only targeting (2026-10-16)

Migrated the shared 6.1.123/6.1.124 avaṅ producer from whole-term list
mutation to raw avang as an ADESHA carrier with explicit NGIT_FINAL
placement. Genuine it rules process the actual final ṅ; 1.1.53 then
substitutes the operative ava list at the exact final o occurrence.
The archived carrier retains its own NGIT provenance and prescribing
rule, and the target records its linked AdeshaOrigin without inheriting
the carrier's it markers. The following vowel remains a separate term
for the genuine subsequent guṇa, vṛddhi or savarṇadīrgha operation.
Source: Bālamanoramā on 6.1.123 explicitly invokes ṅicca for final targeting:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.123.htm

Reused the existing raw-adesha lifecycle rather than adding a special
go-result carrier. Added generic scheduler gating while any explicit
adesha target is pending: it processing and actual carrier integration
precede unrelated phonological mutations of its snapshotted owner.
The go-boundary selector excludes an already targeted owner, avoiding
duplicate prescription during raw-marker processing.

Focused set: four IndraSandhi, three GoPrakrtibhava and six
AdeshaCarrierLifecycle tests pass. New assertions cover raw material,
exact owner snapshot, separate 1.1.53 integration, unchanged final
gavendraḥ, source nasalization and written avagraha, prefix annotations,
archived NGIT provenance and no NGIT leakage into the target.
Strengthened native benchmark cases SANDHI_251/252/254 to require
1.3.3, 1.3.9 and 1.1.53; expected spellings were not changed.

This migrates the operative substitution and marker provenance, not
all Vedic accent prescriptions: the independently prescribed ādyudātta
and compound-accent precedence discussed by the commentary still need
their own accent-domain audit. Other literal-surface candidate checks
identified in this audit include 7.2.103 and numeral 7.1.21/22; they
remain explicit migration work, not hidden Unicode helpers.

Verification completed: full suite passed (77 tasks, 1m 43s), including
the strengthened native benchmark trace requirements. All four corpus
runs remain unchanged: Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip,
alternatives 1162/1533/4/1; UoH default 4861/4503/0/4,
alternatives 5661/3703/0/4. Exact counts remain 616/636 and 4388/4450.
git diff --check passed. Full goal remains active; no commit.

### Exact kim whole-stem substitute (2026-10-16)

Migrated 7.2.103 किमः कः from surface equality and legacy whole-term
substitution to cached kim varnas and exact range replacement. The
recorded substitute is the complete operative ka list at position zero,
not a misleading final m -> k summary paired with a different whole word.
The existing exact-range helper projects annotations and orthographic
signs and preserves term identity, lexical upadesha and the separate Sup.
No new helper, sutra or output-specific branch was introduced.

All 16 CorpusPronounProvenance tests and four PronominalStems tests pass,
including annotated kim with nasalized i and avagraha, exact original and
replacement varnas, renamed typed su, no repeat application, and negative
fake-su and wrong lexical-identity cases. The full 315 forward pronoun
requests and independently specified paradigms remain part of this set.
This preserves the existing simple-kim domain; the independently attested
akac-bearing cases are not falsely claimed implemented by this migration.

Verification: all four corpus runs completed without regression and the
full suite passed (77 tasks, 1m 39s); git diff --check passed.
Before/after unchanged: Ashtadhyayi default 884 pass/1811 fail/4 errors/1 skip,
alternatives 1162/1533/4/1; UoH default 4861/4503/0/4,
alternatives 5661/3703/0/4. Exact counts remain 616/636 and 4388/4450.
Literal numeral-surface fallbacks in 7.1.21/22 remain the next bounded
audit, alongside broader carrier/accent and quoted-input modeling.
Full goal remains active. No commit.

### 2026-10-17: remaining third-adhyaya ending conditions

Migrated 3.1.83 (halah shnah shanaj jhau) from a rendered hi equality to
the exact HA/I Varna sequence. Its raw shanac replacement and fresh it
processing remain unchanged. Migrated 3.1.85 (vyatyayo bahulam) from rendered
mi equality to MA/I, retaining the LET and mip upadesha restrictions.
The latter now substitutes only MA with NA through replaceTermVarna;
the untouched I token, nasalization, orthographic signs, term identity,
and marker provenance survive. No new helper or sutra was necessary.

Focused verification: 80 tests passed (2 new ending tests, 13 ending-condition
tests, 65 tinganta engine tests). Both corpora were rerun with declared lexical
analysis and CONTINUATION, in default and alternatives modes; all counts
were unchanged: Ashtadhyayi default 884/1811/4/1, alternatives 1162/1533/4/1;
UoH default 4863/4501/0/4, alternatives 5663/3701/0/4
(pass/fail/error/skip). Exact counts remain 616/636 and 4390/4452.

Full verification for this batch: gradlew test --no-parallel passed
(77 tasks, 1m 48s); git diff --check passed. Branch remains dev.

Remaining audit includes Halantyam's processed/raw compatibility check
(surface versus upadesha): replacing it requires an explicit lifecycle
decision, not merely comparing phonemes. Lexical/gana membership checks
also remain, distinct from phonological conditions. Corpus unknown visarga
sources, quoted grammatical inputs, and general accent/carrier modeling
remain genuine gaps; this batch does not claim they are solved.

### 2026-10-17: independently sourced Indra and Shakra nominal proofs

Declared masculine इन्द्र and शक्र stems, not completed-word aliases.
Sources: [Śabdakalpadruma transcription, Indra](https://sa.wiktionary.org/wiki/इन्द्र)
and [Amarakośa svargavarga](https://sanskritdocuments.org/doc_z_misc_major_works/amarfin1.html).
The former explicitly states masculine gender and lists Shakra as a synonym;
the latter independently supplies both names (line-number labels 1.1.100–102
in this edition; numbering differs from the dictionary's verse numbering).
The separate Shakra dictionary URL was inaccessible; it is not claimed as
independently inspected. All 42 case forms were checked against explicit
paradigms before the forward-derived index admitted these stems.

No new rule/helper or phonological shortcut was introduced. Existing nominal
derivation establishes rutva, and 6.1.114/6.1.87 establish the before-va forms.
Regression tests retain the full derivation and ESTABLISHED_RU source, and
confirm the unanalysed mode still does not guess a source from visarga.
Both focused test classes passed (23 tests).

All four declared-lexicon CONTINUATION corpus runs passed. Before/after:
Ashtadhyayi default 884/1811/4/1 unchanged; alternatives 1162/1533/4/1
unchanged. UoH default 4863/4501/0/4 -> 4868/4496/0/4; alternatives
5663/3701/0/4 -> 5668/3696/0/4 (pass/fail/error/skip).
Exact matches rise 4390 -> 4391 and 4452 -> 4453. UoH lexical coverage
rises 1991 -> 1998 rows and 2199 -> 2208 occurrences; Ashtadhyayi coverage
rises 185 -> 186 rows, 215 -> 216 occurrences.

New UoH passes are rows 14, 20, 1697, 1947, 3028. Row 5749 retains a
failure: the corpus deletes visarga from the first Shakra before another
Shakra. A nominal source proof does not justify that deletion; no expected
output or normalization was altered to force a pass. Unknown lexical
sources and quoted grammatical input modeling remain separate gaps.

The lexical-admission batch's complete suite passed (77 tasks, 1m 54s),
followed by git diff --check. No commit was made.

### 2026-10-17: genuine kharpara-shar visarga-lopa varttika

The [Kāśikā under 8.3.36](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/8/8.3.36.htm)
states an optional lopa extension before shar immediately followed by khar.
Registered KharpareShariVisargaLopaVartika separately as
vartika:8.3.36:kharpare-shari-lopa, with SutraSource.Vartika and its real
parent/source URL; canonical paatha counts and 8.3.36 itself remain unchanged.
The rule reuses the existing exact visarga-boundary helper, SHAR/KHAR
membership, and replaceTermVarna deletion. No Unicode inspection or new
spelling helper was introduced. Declining it retains both existing branches.

The independently attested example वृक्षाः + स्थातारः now has all three
outputs: वृक्षास्थातारः, वृक्षाःस्थातारः, वृक्षास्स्थातारः.
Added three sourced native benchmark cases with required/forbidden and
declined-rule traces. All 386 native benchmark cases passed. The broader
focused run passed 35 tests (4 new varttika, 8 retention, 23 lexical/sequence).
Exact deletion also preserves the untouched vowel token, nasalization,
avagraha placement, term identity, lexical metadata, and the right term.

An initial reading of शक्रः as a consonant cluster was disproved by inspecting
its actual Varna sequence: SHA/A/KA/RA/A/VISARGA. It is NOT an instance
of this varttika. The negative corpus regression retains row 5749's failure
and verifies no varttika trace can be used to force that expected deletion.

Corpus reruns for this second batch (declared lexicon, CONTINUATION):
Ashtadhyayi default 884/1811/4/1 -> 882/1813/4/1, exact 616 unchanged;
alternatives 1162/1533/4/1 -> 1163/1532/4/1, exact 636 -> 637.
The default APPLY_ALL policy selects newly available optional deletion;
retention remains available in the alternatives, whose coverage increases.
UoH remains 4868/4496/0/4 default and 5668/3696/0/4 alternatives,
exact 4391/4453. No input errors, skips, lexical metadata, or normalization
were changed in this varttika batch. Default-match loss is reported rather
than concealed by changing the selector to fit the corpus.

Complete verification passed: gradlew test --no-parallel (77 tasks,
1m 43s), including all 386 native sandhi benchmarks; git diff --check
passed after the report update. Full goal remains active, no commit.

### 2026-10-17: correct the misidentified 3.1.29 affix producer

Source inspection found more than a surface-check migration issue:
3.1.29 is ऋतेरीयङ्, not the former misspelled text and fabricated
ṛtīya-to-kyaṅ prescription. [Kāśikā and Bālamanoramā](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/3/3.1.29.htm)
identify the consonant-final sautradhātu ऋत् and prescribe ईयङ्.
Replaced the misleading RtIyIyAnehKyanSutra with RterIyangSutra in the
registry, corrected its text and old intermediate test, and introduced
SanadiAffix.IYANG. No fake canonical number or compatibility alias retains
the wrong operation.

The rule selects a DHATU by recorded ऋत् identity or exact R/TA Varna
shape, never by rendered surface. It excludes ऋति, ऋत, and ऋतीय as
new source roots, rejects nominal homographs, and prevents reselection.
It does not misuse the requested krt meaning BHAVA as a gate for स्वार्थ.
The new raw affix processes final NGA independently through 1.3.3 and
1.3.9, retains its NGIT provenance, and receives 3.1.32 designation through
the typed sanadi inventory. The operative stem is ऋतीय; no final conjugated
form or comprehensive optional 3.1.31 coverage is claimed by this test.

Added shared insertAfterTerm(ownerId, carrier) placement. An attempted use
of insertBeforeTingOrLingAugment was disproved by tests: that existing helper
assumes the last term is ting, so it put the new suffix before a bare root.
Explicit owner placement preserves the root and other terms, both with no
ending and with an existing typed ting. It performs no Unicode slicing.

Focused verification: 13 tests passed (3 new lifecycle, 4 morphology,
3 sanadi engine, 3 typed-sanadi request). Tests verify raw/processed identity,
unmodified root annotations/signs, exact operative material, independent
NGIT provenance, registry identity, and placement before existing ting.

The source audit also found unresolved neighboring modeling concerns:
3.1.28's paṇ sense restriction must distinguish praise from transaction;
3.1.31's optional āyādi behavior needs actual affix and environment evidence.
These are not solved by turning their existing spelling fallbacks into broad
Varna predicates. Other third-adhyaya lexical fallbacks, Halantyam's lifecycle
compatibility gate, quoted-input morphology, and general accent policy remain
under audit. No corpus expectation, normalization, or lexical source was changed.

Additional primary-source audit findings (not claimed fixed in this batch):
[3.1.48](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/3/3.1.48.htm)
is णिश्रिद्रुस्रुभ्यः कर्तरि चङ्: the ṇi reference means a ṇi-ending derived
dhatu, not the ni preverb; śri has short I. The current Nishri-named
implementation misses these distinctions and must substitute actual cli,
not independently add a duplicate vikarana. Likewise
[3.1.55](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/3/3.1.55.htm)
requires the specified divadi pushadi group, dyutadi group or genuine
lṛdit evidence, and parasmaipada; a small surface-root list cannot certify
these domains. These findings change the next action from mechanical
string replacement to typed group, marker, prayoga and substitute modeling.

Verification complete: all four declared-lexicon CONTINUATION corpus runs
remain unchanged from the immediately preceding checkpoint: Ashtadhyayi
default 882/1813/4/1, alternatives 1163/1532/4/1; UoH default
4868/4496/0/4, alternatives 5668/3696/0/4 (pass/fail/error/skip).
Exact counts remain 616/637 and 4391/4453. The complete test suite passed
(77 tasks, 1m 59s), including all 386 native sandhi benchmark cases.
git diff --check passed. Branch dev; no commit. Full goal remains active.

