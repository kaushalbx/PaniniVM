# Derivation Sūtra Roadmap

The project adds Pāṇinian rules as typed, executable state transitions. A rule
is counted as executable when it is registered and implements the derivation
rule interface; supported forms additionally verify required rules end to end.

## Current scope

- 292 implemented sūtras.
- Typed rule metadata: number, text, role, action, scope, stage, ordering,
  dependencies, blockers, restrictions, and exceptions.
- Māheśvara-sūtra and pratyāhāra support with explicit varṇa and it markers.
- It-processing, pada and pratyaya selection, āgama, ādeśa, lopa, guṇa,
  vṛddhi, reduplication, and selected Tripādī phonology.
- Complete masculine a-stem `sup` paradigms.
- Declared `tiṅ` paradigms across all ten lakāras, including Parasmaipada and
  Ātmanepada coverage for representative roots.
- Gaṇa-specific `LAT`, `LOT`, `LANG`, and `LING` stem formation for all ten
  Dhātupāṭha gaṇas, including strong/weak stem selection.
- Kryādi `LOT` support in both padas, verified with all 18 forms of
  `डुक्रीञ्`.
- Representative Parasmaipada `LING` surfaces verified across all ten gaṇas,
  including `अद्यात्`, `जुहुयात्`, `सुनुयात्`, `रुन्ध्यात्`, `क्रीणीयात्`,
  and `चोरयेत्`.
- A complete ten-gaṇa Dhātupāṭha catalogue with pada metadata.

## Near-term work

1. Complete exact gaṇa-paradigm validation.
   - verify all nine Parasmaipada `LING` surfaces for a representative root
     from each gaṇa
   - extend exact validation to applicable Ātmanepada paradigms
   - repeat the surface audit for `LOT` and `LANG`
   - keep the regression coverage concise and data-driven

2. Broaden root validation.
   - test representative vowel-final and consonant-final roots
   - test anudātta/anudāttet and Ubhayapada behavior
   - record known lexical exceptions explicitly

3. Complete verbal alternatives.
   - optional aorist and subjunctive branches
   - additional perfect and periphrastic-future environments
   - passive and bhāve derivations

4. Expand nominal morphology.
   - additional stem classes and genders
   - pronouns and numerals
   - feminine formation, kṛdanta, and taddhita integration

5. Strengthen rule governance.
   - asiddha/asiddhavat visibility
   - vipratiṣedha and environment-specific blocking
   - deterministic handling of optional derivation branches

6. Continue phonological coverage.
   - remaining svara, visarga, and vyañjana sandhi environments
   - pada-end and pause behavior
   - Vedic accent-aware tokenization

## Definition of done for a rule

Each implemented sūtra should include:

- authoritative number and text
- typed role, action, scope, stage, and ordering metadata
- explicit applicability and state transition logic
- dependencies, blockers, restrictions, or exceptions where applicable
- focused unit tests and at least one end-to-end derivation when the rule is
  part of a supported form
- documentation of deliberate simplifications or unsupported environments
