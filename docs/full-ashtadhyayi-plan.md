# Full Aṣṭādhyāyī Implementation Plan

The project tracks a target of 3,959 implemented sūtras.

## Current status

| Measure | Count |
| --- | ---: |
| Target sūtras | 3,959 |
| Registered implemented sūtras | 464 |
| Derivation-state sūtras | 341 |
| Sentence-level kāraka/vibhakti sūtras | 64 |
| Other (Saṃjñā/Paribhāṣā/Adhikāra/etc.) | 59 |
| Remaining | 3,495 |

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

## Sūtra classification

The Aṣṭādhyāyī contains several functional types of sūtras. A single sūtra can sometimes belong to more than one category, but the traditional classification is as follows:

| Type | Sanskrit | Function |
| :--- | :--- | :--- |
| **Vidhi** | विधिसूत्र | Prescribes an operation or rule. This is the most common type. Example: add an affix, perform guṇa, substitute a sound. |
| **Niyama** | नियमसूत्र | Restricts a general rule to a narrower situation. "Do this only here." |
| **Parisaṅkhyā** | परिसंख्यासूत्र | Excludes all other possibilities, leaving only specified cases. |
| **Atideśa** | अतिदेशसूत्र | Extends properties or behavior from one item to another ("treat X like Y"). |
| **Adhikāra** | अधिकारसूत्र | Introduces a governing heading whose effect continues until another adhikāra replaces it. Like a scope or namespace. |
| **Anuvṛtti** | अनुवृत्ति | Not a separate written sūtra but the carried-forward words from previous sūtras, avoiding repetition. Essential for interpretation. |
| **Saṃjñā** | संज्ञासूत्र | Defines technical terms used elsewhere. Example: defining what constitutes a pratyāhāra, dhātu, etc. |
| **Paribhāṣā** | परिभाषासूत्र | Meta-rules that determine how other rules interact, resolve conflicts, precedence, interpretation, and application order. |
| **Apavāda** | अपवादसूत्र | Exception to a general rule (utsarga-apavāda principle). Overrides the general rule when applicable. |
| **Vibhāṣā** | विभाषासूत्र | Makes an operation optional. |
| **Pratiṣedha** | प्रतिषेधसूत्र | Explicitly prohibits or blocks an operation. |
| **Nipātana** | निपातनसूत्र | Declares an irregular form directly, bypassing normal derivation. |

## Domain milestones

1. Śikṣā and pratyāhāra
   - varṇa inventory and Māheśvara-sūtra tokenization
   - savarṇa and articulatory relations
   - Vedic accent support

2. Sup morphology
   - complete implemented paradigms: masculine a/i/u/ṛ/n, feminine i/u/ī/ā,
     and neuter a/s stems
   - remaining vowel and consonant stem classes and irregular paradigms
   - pronoun and numeral paradigms

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
   - initial kāraka and vibhakti constraint engine: 1.4.24, 1.4.32, 1.4.42,
     2.3.2, 2.3.13, 2.3.18, 2.3.28, and 2.3.36
   - remaining kāraka definitions, lexical exceptions, `anabhihita`
     environments, and voice-sensitive assignments
   - samāsa formation
   - semantic conditions and optional readings

6. Phonology and governance
   - internal and external sandhi
   - Tripādī ordering
   - vipratiṣedha, asiddha, exceptions, and optional branches

## Numeral architecture constraint

Numeral generation proceeds from a numeric value to a grammatical derivation.
Execution input proceeds from an explicitly segmented numeral prātipadika to a
typed numeric value. Neither direction authorizes parsing ordinary surface
Sanskrit, ad-hoc arithmetic notation, or generated output words back into
numbers.

Generated numeral prātipadikas should eventually enter the regular `sup`
derivation pipeline while retaining their typed numeric identity.

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
