# Module `:core`

Core domain models, governance registries, and foundational types for the PaniniVM engine.

## Overview

The `:core` module contains:
- **`Sutra<C, R>`**: Generic interface for all Pāṇinian sūtra rules.
- **`DerivationState` & `DerivationTerm`**: State representations carrying terms, *it-markers*, tags (`TermKind`), and Saṁjñā assignments.
- **`Samjna` (`dev.panini.shiksha.Samjna`)**: Centralized sealed interface partitioned into `Unit`, `Affix`, `Phono`, `Stem`, `Avyaya`, `Karaka`, and `Rudhi`.
- **`Artha` (`dev.panini.shiksha.Artha`)**: Centralized sealed interface partitioned into `Karaka`, `Dispositional`, `Taddhita`, `Rudhi`, and `Explanation`.
- **`PratyaharaEngine`**: Type-safe sound matching for Māheśvara-sūtras (`Pratyahara.EN`, `Pratyahara.YAY`, `Pratyahara.JHAR`, `Pratyahara.HAL`).
- **Registries**: `AdhikaraRegistry` (heading scopes), `ParibhashaRegistry` (meta-rules), and `NishedhaRuleEngine` (prohibitions).
