# Module `:derivation`

Core derivation engines and morpho-phonological pipeline execution.

## Overview

The `:derivation` module houses:
- **`DerivationEngine`**: State-transition execution engine evaluating ordered Aṣṭādhyāyī sūtras on `DerivationState`.
- **`SubantaEngine`**: 21-slot nominal paradigm derivation engine across all 31 Classical nominal stem classes.
- **`TingantaEngine`**: Verbal conjugation derivation engine across all 10 Lakāras and 10 Gaṇas.
- **`SankhyaGenerator`**: Cardinal and ordinal numeral derivation generator.
- **`UnadiDerivationBridge` & `UnadiDerivationEngine`**: Bridges Uṇādi matches into `DerivationState` to run full Aṣṭādhyāyī step-by-step derivation traces (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*).
