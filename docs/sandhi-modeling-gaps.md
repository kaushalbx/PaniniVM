# Open sandhi modeling gaps

These are unresolved findings, not supported derivations. Passing the executable
benchmark corpus does not establish complete sandhi coverage.

## Ghi nominal endings: source and rendering audit

The 2026-10-10 XERJ-assisted lookup located `GherNgasyoSutra` and
`GherAuoSutra`; direct source inspection found repository identifiers 7.3.125
and 7.3.123 respectively. The [primary sutra text listing](https://ashtadhyayi.github.io/suutra/7.3/)
ends this pada at 7.3.120. These identifiers must not be treated as verified
Paninian sutras simply because existing declension tests pass.

The former `GherNgasyoSutra` appended visarga and inserted avagraha after every qualifying
guna vowel, then replaces the survivor's orthographic-sign list. This both
bypasses the intermediate affix/sandhi lifecycle and risks losing existing
annotations. The [Kashika text for 7.3.111](https://sa.wikisource.org/wiki/काशिका_(पदमञ्जरीव्याख्यासहिता)/सप्तमोऽध्यायः/तृतीयः_पादः)
gives अग्नेरागच्छति, वायोरागच्छति, अग्नेः स्वम् and वायोः स्वम्, not an
obligatory avagraha between the guna vowel and visarga.

The masculine singular shortcut has now been removed. `SupAffix.NGASI`
retains its compatibility identity but starts from annotated ङसिँ, so 1.3.2
can designate the final nasal vowel instead of leaving an erroneous इ behind.
The existing 6.1.110, 8.2.66 and 8.3.15 pipeline produces अग्नेः and वायोः;
`GhiDerivationTest` asserts those intermediate rule identities for both
ablative and genitive. The 6.1.110 mutation now uses exact Varna deletion
without introducing avagraha. Corrected paradigm expectations are कवेः,
ऋषेः and भानोः, not their formerly avagraha-bearing spellings.

The dual shortcut has also been removed. Short i/u before typed AU/AUT now
uses 6.1.102, with exact stem-vowel replacement, suffix-vowel deletion and
annotation-preserving term composition. The [Kashika on 6.1.102](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.102.htm)
explicitly gives अग्नी and वायू. `PurvasavarnaDualTest` checks both first and
second vibhakti duals and the canonical rule trace. Other ik cases and the
exceptions to purvasavarna still require a complete applicability audit;
following-word provenance is not established by these isolated-form tests.

## Completed-pada visarga provenance

The initial 2026-10-10 benchmark audit reproduced these failures through the string-input
`SandhiEngine.join` API:

| Inputs | Required result | Observed result | Observed rules |
| --- | --- | --- | --- |
| हरिः + रम्यः | हरीरम्यः | हरियरम्यः | 8.3.17 |
| शम्भुः + राजते | शम्भूराजते | शम्भुयराजते | 8.3.17, 8.3.24, 8.4.58 |

The [commentary on ढ्रलोपे पूर्वस्य दीर्घोऽणः](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.3.111.htm)
derives the examples from s-final underlying forms, through rutva and r-lopa.
Executable cases SANDHI_286–287 therefore use हरिस् and शम्भुस् and require
8.2.66, 8.3.14, and the repository's 6.3.111 identifier. They do **not** test or
repair the completed-pada visarga inputs above.

The active `BhoBhagoAghoApurvasyaYoshiSutra` now checks the lexical/a-purva domain,
requires rutva provenance for a final repha, and substitutes only consonantal y.
Regression tests reject the i/u-final visarga inputs above for this rule. The
table records the original diagnostic, not the output after that scope correction.
The external-pada input model still lacks an explicit underlying s/r identity for
a written visarga; rejecting the incorrect yatva is not a complete derivation.
Do not infer a unique historical s/r identity from the
written sign, add output-specific substitutions, or silently treat these failures
as covered by the underlying-form benchmarks.

The source's displayed numbering varies between 6.3.110 and 6.3.111. This document
uses the existing executable repository identifier, without claiming the numbering
audit is complete.

### Guṇa composition and deferred इत् lifecycle

An exact-token replacement/deletion/composition trial for ordinary 6.1.87
passed the 291 sandhi benchmarks but failed `SubantaNumeralDeclensionTest`:
deleting the initial vowel of `sup-aut` invalidated a deferred it-designation.
The trial was withdrawn; ordinary guṇa still uses the legacy merger.
Before migrating it, establish an explicit exact-position policy for transferring
or consuming deferred designations during coalescence. Do not bypass the
designation checks, add a legacy fallback, or exclude the numeral context.
Focused vowel-sandhi tests alone do not cover this lifecycle requirement.

`deleteTermVarnas` now remaps surviving active/deferred designations through
exact token positions and reprojects their written spans. Its regression test
also requires rejection when the deleted range contains a designated token.
This addresses deletion bookkeeping only: composition of a still-deferred
affix and coalescence of a designated vowel still need explicit policies.

### Rutva-to-u intermediate representation

6.1.113 and 6.1.114 now replace the final rutva-derived repha only, retaining
the preceding short a. Their intermediate tokens are therefore a + u, not u
with an implicitly discarded a. The causal 6.1.87 follow-up coalesces that exact
pair into o and retains the preceding a's annotations. Regression tests check
both intermediate and final states, signs, term metadata, exact trace positions,
and rejection without 8.2.66 history. This does not solve arbitrary historical
position alignment or reconstruction of underlying s/r from written visarga.

Commentary evidence:
https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.113.htm
and https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/6/6.1.114.htm.

Benchmarks SANDHI_292–295 now require the explicit rutva/utva/guna sequence
for the commentary's vrksa atra and purusa yati/hasati/dadati examples, using
underlying s-final inputs. They exposed orchestration gaps: the post-tripadi
exception admitted guna after 6.1.114 but not 6.1.113, and did not admit the
causally subsequent purvarupa. The latter exception is now limited to consecutive
6.1.113 → 6.1.87 substitutions on the same target. Internal guna leaves the
state at PADA_FORMED so an exhausted external derivation reaches FINAL normally.

### Sarvanāmasthāna identity versus applicability

6.4.8 and 6.4.10 now select the five sup identities through a shared typed
helper, including retained source identity, rather than generated term IDs.
This is not a complete implementation of sarvanāmasthāna applicability:
gender restrictions, asambuddhi context, and the separate śi substitution
must still be audited against their designation rules. The helper deliberately
does not assert those conditions merely from an affix identity.

### Beginning-augment savarṇa composition

6.1.101 now lengthens the augment's final vowel, deletes the target's initial
coalescing vowel, and prepends the processed augment with annotated-token
insertion. The target retains its identity and metadata; the augment remains in
the dropped-term ledger. The shared composition operation validates adjacency,
the explicit 1.1.46 target relationship, and completed augment it-processing.
Its core test also verifies exact reprojection of a surviving target designation.
This removes the beginning-augment legacy merger from 6.1.101. The beginning-
augment branch of 6.1.87 now uses the same composition operation with its exact
guna replacement; tests cover e, o, and ar replacements while preserving target
identity, surviving nasal annotations, and orthographic signs. Ordinary 6.1.87
initially retained the legacy merger until the deferred-designation transfer
policy below was implemented.
General two-source accent/nasality coalescence and consumption of a designated
coalescing vowel still need explicit grammatical policies.

Ordinary 6.1.87 now uses exact replacement/deletion and annotated composition
with explicit transfer of surviving deferred suffix designations. The transfer
reprojects their written spans and token indices onto the surviving term, where
1.3.9 can perform exact lopa and record designation provenance. Composition still
rejects active designations and unfinished affix processing; the transfer is
opt-in rather than a fallback. Regression coverage includes the former numeral
failure and a direct guṇa → transferred-marker → 1.3.9 chain. The earlier failed
trial above records why the explicit transfer policy was needed.
