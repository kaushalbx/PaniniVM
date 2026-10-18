# SandhiKosh evaluation

This is the reproducible benchmark contract and latest verified checkpoint.
Generated reports contain row-level details; Git history records older batches.

## Run

Sources are the unchanged SandhiKosh
[Aṣṭādhyāyī](https://github.com/sanskrit-sandhi/SandhiKosh/blob/master/Astaadhyaayii%20Corpus.xls)
and [UoH](https://github.com/sanskrit-sandhi/SandhiKosh/blob/master/UoH_Corpus.xls)
workbooks. Extraction preserves spelling, whitespace, shaping controls,
malformed input, and row identifiers.

```powershell
.\gradlew.bat -I scripts/sandhikosh-evaluation.init.gradle `
  -PsandhikoshEvaluationDir=build/sandhikosh-evaluation `
  -PsandhikoshLexicalAnalysis=declared `
  :derivation:test --tests '*SandhiKoshEvaluationTest' --no-parallel
```

Use `build/sandhikosh-uoh-evaluation` for UoH and add
`-PsandhikoshAlternatives=true` for alternatives. Reports are
`results-{sequence|alternatives}-declared-lexicon.*`.

## Policy

- Evaluate completed padas left-to-right with `CONTINUATION`; only the surviving
  rightmost phonological term enters the next boundary.
- Preserve term identity, annotations, signs, metadata, pada ownership, and
  traces. Use canonical `term.varnas` and exact Varṇa edits.
- Default chooses the configured derivation; alternatives enumerates `joinAll`.
  More than 4,096 outcomes is an error, never silently truncated.
- Match NFC with whitespace ignored; report exact NFC including spacing
  separately. Apply no other normalization.
- Never infer an underlying s/r source from written visarga. Declared forms
  require independent evidence and successful forward derivation.
- Empty/unusable records are explicit skips; exceptions remain errors.

## Verified checkpoint — 2026-10-18

| Corpus and mode | Pass | Fail | Errors | Skipped | Exact |
| --- | ---: | ---: | ---: | ---: | ---: |
| Aṣṭādhyāyī default | 887 | 1,808 | 4 | 1 | 618 |
| Aṣṭādhyāyī alternatives | 1,168 | 1,527 | 4 | 1 | 639 |
| UoH default | 5,052 | 4,312 | 0 | 4 | 4,491 |
| UoH alternatives | 5,857 | 3,507 | 0 | 4 | 4,554 |

Declared coverage is 235 Aṣṭādhyāyī rows/267 occurrences and 2,349 UoH
rows/2,637 occurrences. The complete 77-task suite and `git diff --check` pass.

## Current delta

- Raw āgama/ādeśa carriers retain इत् and target provenance; ś-it whole
  substitution executes through 1.1.55.
- Prāgdiśīya derivation covers 5.3.1–3, 5.3.7, 5.3.10, and 7.2.102/104,
  deriving `ततः`, `यतः`, `कुतः`, and `इतः`. Rule 5.3.2 consumes the existing
  `SarvadiGana` and its canonical द्व्यादि tail; no synthetic gaṇa is registered.
  Sources: [5.3.2](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/5/5.3.2.htm),
  [5.3.3](https://www.premabase.com/siddhanta-kaumudi/p40/textbook/index.html),
  [5.3.7](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/5/5.3.7.htm),
  [7.2.104](https://sanskritdocuments.org/learning_tools/ashtadhyayi/vyakhya/7/7.2.104.htm).
- Gender comes from registered `LinganushasanaSutra`s over gender-free core
  stems: authentic rules 35–37, 43, 51, 64, 93, 109–111, 114, 116, and 158.
  The complete enumerations of 109–111, 114, 116, and 158 are declared as
  executable rules; independently selected members are declared as gender-free
  identities for forward lexical analysis. Fabricated legacy registrations were removed.
  Semantic rules require explicit meaning provenance before forward declension.
- Rules 3.1.48 and 3.1.55 use exact `cli` substitution and retained derivational
  or Dhātupāṭha provenance, not spelling-based gaṇa guesses.

## Remaining failures

| Category | Required treatment |
| --- | --- |
| Incorrect rules | Source-audit and replace broad checks with exact typed operations. |
| Missing sūtras | Implement/register authentic rules with positive, negative, branch, and trace tests. |
| Applicability/precedence | Model Tripādī order, domains, lifecycle, and context explicitly. |
| Missing lexical/derivational provenance | Add only evidenced stems with successful forward derivations. |
| Optional alternatives | Preserve licensed branches; never tune defaults to expected strings. |
| Input errors | Four Aṣṭādhyāyī rows contain parenthesized material rejected by the parser. |
| Orthographic/boundary policy | Keep spacing, avagraha, shaping, anusvāra/parasavarṇa, and pause differences explicit. |

Largest clusters remain unresolved visarga provenance, multipart metalinguistic
input, optional anusvāra/parasavarṇa spelling, editorial avagraha, and missing
verbal or nominal context. Generated reports contain row-level evidence.

## Batch gate

Verify primary sources; add sourced positive, negative, optional, and trace
tests; run focused tests; evaluate both corpora in both modes; run
`.\gradlew.bat test --no-parallel`; run `git diff --check`; and record affected
rows, count deltas, modeling changes, and remaining categories.
