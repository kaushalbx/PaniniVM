# Derivation Sūtra Roadmap

The project adds Pāṇinian rules as typed executable transformations. A rule is
counted when it is registered through the common `Sutra<C, R>` model and has
tested applicability and output. Derivation rules operate on derivation state;
sentence rules operate on typed kāraka or vibhakti contexts.

## Current scope

- 324 registered executable sūtras: 316 derivation-state rules and 8
  sentence-level kāraka/vibhakti rules.
- Typed rule metadata: number, text, role, action, scope, stage, ordering,
  dependencies, blockers, restrictions, and exceptions.
- Māheśvara-sūtra and pratyāhāra support with explicit varṇa and it markers.
- It-processing, pada and pratyaya selection, āgama, ādeśa, lopa, guṇa,
  vṛddhi, reduplication, and selected Tripādī phonology.
- Complete 21-slot `sup` paradigms for 11 stem-class/gender combinations:
  masculine a, i, u, ṛ, and n; feminine i, u, ī, and ā; and neuter a and s.
- Declared `tiṅ` paradigms across all ten lakāras, including Parasmaipada and
  Ātmanepada coverage for representative roots.
- Gaṇa-specific `LAT`, `LOT`, `LANG`, and `LING` stem formation for all ten
  Dhātupāṭha gaṇas, including strong/weak stem selection.
- Representative `LAT`, `LOT`, `LANG`, and `LING` surfaces verified in both padas
  across all ten gaṇas with concise, table-driven paradigm tests.
- Additional `LAT`, `LOT`, `LANG`, `LING`, `LRT`, and `LRNG` root-shape coverage for vowel-final, consonant-final,
  irregular `गम् → गच्छ्`, and Ubhayapada roots.
- Initial `LIT` root-shape coverage for vowel-final `णीञ्` in both padas, including weak-ending kit, yaṇ, and iṭ behavior.
- A complete ten-gaṇa Dhātupāṭha catalogue with pada metadata.
- Initial syntax coverage for `भ्याम्` syncretism through 1.4.24, 1.4.32,
  1.4.42, 2.3.13, 2.3.18, and 2.3.28, with evidence retained in the rule trace.

The numeral subsystem keeps generation and execution typing separate.
`SankhyaGenerator` produces auditable cardinal and ordinal derivations, while
annotated execution operands carry numeric identity as `SanskritValue.Sankhya`.
Surface numeral words are not reverse-parsed. Future numeral inflection should
connect generated prātipadikas to the regular `sup` pipeline without weakening
this boundary.

## Near-term work

1. Broaden exact root validation.
   - continue root-shape audits across the perfect and aorist systems
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
   - additional consonant stems, irregular classes, and gender/class
     combinations beyond the 11 complete paradigms
   - pronouns and numerals
   - feminine formation, kṛdanta, and taddhita integration

5. Strengthen rule governance.
   - asiddha/asiddhavat visibility
   - vipratiṣedha and environment-specific blocking
   - deterministic handling of optional derivation branches

6. Expand kāraka and vibhakti analysis.
   - implement the remaining 1.4.23 kāraka domain and lexical exceptions
   - model `anabhihita` and voice-sensitive realization explicitly
   - expand dhātu valency profiles without deriving semantics from case endings

7. Continue phonological coverage.
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
