# Full Aṣṭādhyāyī Implementation Plan

The project tracks a target of 3,959 implemented sūtras.

## Current status

| Measure | Count |
| --- | ---: |
| Target sūtras | 3,959 |
| Implemented sūtras | 295 |
| Remaining | 3,664 |

The implemented subset supports end-to-end nominal and verbal derivations,
but the count does not imply complete linguistic coverage of every rule
environment.

## Implementation principles

1. Implement each sūtra from reviewed sources as a typed derivation rule.
2. Represent adhikāra, anuvṛtti, dependency, blocking, and ordering metadata.
3. Implement paribhāṣā and asiddha/asiddhavat governance in the engine.
4. Count a sūtra only when its executable logic and tests are complete.
5. Require supported form plans to name the sūtras needed for a complete
   derivation.
6. Preserve the full rule trace so every surface can be audited.

## Domain milestones

1. Śikṣā and pratyāhāra
   - varṇa inventory and Māheśvara-sūtra tokenization
   - savarṇa and articulatory relations
   - Vedic accent support

2. Sup morphology
   - masculine a-stems
   - remaining vowel and consonant stems
   - feminine, neuter, pronoun, and numeral paradigms

3. Tiṅ morphology
   - all ten lakāras and both padas
   - gaṇa-aware present-system stems
   - passive, bhāve, causative, desiderative, and intensive derivations
   - lexical and optional alternatives

4. Derived morphology
   - kṛdanta
   - taddhita
   - strī-pratyaya

5. Syntax and compounds
   - kāraka and vibhakti selection
   - samāsa formation
   - semantic conditions and optional readings

6. Phonology and governance
   - internal and external sandhi
   - Tripādī ordering
   - vipratiṣedha, asiddha, exceptions, and optional branches

## Reporting and verification

Print the live implementation count:

```sh
./gradlew run --args="--coverage"
```

Run the complete verification suite:

```sh
./gradlew test
```

Counts in this document must be updated together with
`AshtadhyayiTest.kt` whenever the registered sūtra set changes.
