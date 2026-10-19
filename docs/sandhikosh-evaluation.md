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
  :derivation:sandhiKoshEvaluation --no-parallel
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

## Verified checkpoint — 2026-10-19

| Corpus and mode | Pass | Fail | Errors | Skipped | Exact |
| --- | ---: | ---: | ---: | ---: | ---: |
| Aṣṭādhyāyī default | 890 | 1,805 | 4 | 1 | 621 |
| Aṣṭādhyāyī alternatives | 1,172 | 1,523 | 4 | 1 | 643 |
| UoH default | 5,203 | 4,161 | 0 | 4 | 4,614 |
| UoH alternatives | 6,015 | 3,349 | 0 | 4 | 4,677 |

Declared coverage is 252 Aṣṭādhyāyī rows/285 occurrences and 2,583 UoH
rows/2,936 occurrences. The complete 77-task suite and `git diff --check` pass.

## Current delta

- Exact āgama/ādeśa and `cli` operations retain target, इत्, Dhātupāṭha, and
  derivational provenance. Prāgdiśīya forms derive through registered rules and
  the existing `SarvadiGana`, without synthetic gaṇas.
- Gender is supplied by registered `LinganushasanaSutra`s over gender-free stems;
  rules 145 and 183 preserve both licensed masculine/neuter readings. Semantic
  rules still require meaning provenance.
- Correct 6.4.11 and 7.1.94 replace the former misnumbered shortcut. Selected
  s/r-final `SvaradiGana` members derive through 1.1.37/2.4.82; सुप्-luk retains
  pada identity and exact 8.3.15 repha provenance; 8.3.36 retains both śar
  branches. Cumulative gain from these gaṇa forms: Aṣṭādhyāyī +1 default/+2
  alternatives and UoH +105 default/+111 alternatives.
- Rule 6.1.132 now requires a forward-derived `तद्`/`एतद्` masculine nominative
  singular and its exact `सुँ`→`रुँ` provenance; bare `सः`/`एषः` spelling is not
  treated as proof. No benchmark row depended on the removed shortcut.
- Amarakośa 3.1.23 supplies `प्रभु`; registered Liṅgānuśāsana 51 supplies its
  gender. Five UoH rows now pass in each mode; undeclared shape matches remain
  excluded from the corpus lexicon.

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
