# Derivation Sūtra Roadmap

The project adds Pāṇinian rules as typed, executable state transitions. A rule
is counted as executable when it is registered and implements the derivation
rule interface; supported forms additionally verify required rules end to end.

## Current scope

- 300 implemented sūtras.
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
- Representative `LAT`, `LOT`, `LANG`, and `LING` surfaces verified in both padas
  across all ten gaṇas with concise, table-driven paradigm tests.
- Additional `LAT`, `LOT`, `LANG`, `LING`, and `LRT` root-shape coverage for vowel-final, consonant-final,
  irregular `गम् → गच्छ्`, and Ubhayapada roots.
- A complete ten-gaṇa Dhātupāṭha catalogue with pada metadata.

## Near-term work

1. Broaden exact root validation.
   - extend root-shape audits into the perfect and aorist systems
   - keep the regression coverage concise and data-driven

2. Record lexical and optional behavior.
   - test anudātta/anudāttet and Ubhayapada behavior
   - preserve optional derivations as explicit branches
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
