# Module `:unadipatha`

Declarative Uṇādipāṭha subsystem performing suffix assignment and etymological stem analysis.

## Overview

The `:unadipatha` module implements:
- **Declarative `UnadiSutra` Catalog**: 33+ sūtras across all 5 Adhyāyas under Aṣṭādhyāyī 3.3.1 (*उणादयो बहुलम्*).
- **Reverse Etymological Lookup**: `(Dhātu, Pratyaya) → Saṁjñā` and nominal word decomposition.
- **`UnadiAnalyzer`**: Classifies stems into `RUDHI_PRATIPADIKA` (conventional noun e.g. `दारु`, `शारु`) vs `YAUGIKA_PRATIPADIKA` (derivational noun e.g. `कारु`, `पायु`).
