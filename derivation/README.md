# Module `:derivation`

## Runtime-grantha migration

The opening saṃjñā cluster is the first Aṣṭādhyāyī slice migrated into the
shared runtime-grantha architecture:

- 1.1.1 `वृद्धिरादैच्`
- 1.1.2 `अदेङ्गुणः`
- 1.1.3 `इको गुणवृद्धी`
- 1.1.7 `हलोऽनन्तराः संयोगः`
- 1.1.8 `मुखनासिकावचनोऽनुनासिकः`
- 1.1.9 `तुल्यास्यप्रयत्नं सवर्णम्`

`Ashtadhyayi.executableSutras` owns the complete runtime membership.
`AshtadhyayiRuntimeGrantha` materializes those catalog entries with runtime
identities, inspectable artha, dependency ordering, and exports. The original
grammatical conditions and state changes
remain in the established rule objects while execution is verified through
`SutraMachine`; this permits incremental migration without duplicating the
Sanskrit rules or changing derivation results.

Rules 1.1.4–1.1.6 participate in the same runtime grantha so the shared
scheduler can apply niṣedha conflict precedence before the blocked rule's effect.

Core derivation engines and morpho-phonological pipeline execution.

## Overview

The `:derivation` module houses:
- **`DerivationEngine`**: State-transition execution engine evaluating ordered Aṣṭādhyāyī sūtras on `DerivationState`.
- **`SubantaEngine`**: 21-slot nominal paradigm derivation engine across all 31 Classical nominal stem classes.
- **`TingantaEngine`**: Verbal conjugation derivation engine across all 10 Lakāras and 10 Gaṇas.
- **`SankhyaGenerator`**: Cardinal and ordinal numeral derivation generator.
- **`UnadiDerivationBridge` & `UnadiDerivationEngine`**: Bridges Uṇādi matches into `DerivationState` to run full Aṣṭādhyāyī step-by-step derivation traces (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*).
