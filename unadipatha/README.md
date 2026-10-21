# Module `:unadipatha`

Declarative Uṇādipāṭha subsystem performing suffix assignment and etymological stem analysis.

## Overview

The `:unadipatha` module implements:
- **Complete source catalog**: all 748 sūtras, in source order across all 5 Adhyāyas, with प्रत्यय and सिद्धान्तकौमुदी text from [ashtadhyayi.com/unaadi](https://ashtadhyayi.com/unaadi). Access it through `UnadiPatha.sourceSutras` or `UnadiPathaCatalog`.
- **Structured `UnadiSutra` rules**: `UnadiPatha.sutras` contains only source-aligned rules that have been implemented without placeholders.
- **Executable rules**: `UnadiPatha.executableSutras` retains the currently supported derivations while source identities are reconciled.
- **Source-aligned semantic migration**: `UnadiPatha.modeledSourceSutras` contains the consecutively verified rules. Roots absent from the current Dhātupāṭha are retained explicitly as `rootReferences` instead of being discarded or guessed.
- **Reverse Etymological Lookup**: `(Dhātu, Pratyaya) → Saṁjñā` and nominal word decomposition.
- **`UnadiAnalyzer`**: Classifies stems into `RUDHI_PRATIPADIKA` (conventional noun e.g. `दारु`, `शारु`) vs `YAUGIKA_PRATIPADIKA` (derivational noun e.g. `कारु`, `पायु`).
