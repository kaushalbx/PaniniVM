# Full Aṣṭādhyāyī Implementation Plan

The project tracks a target of 3,959 implemented sūtras alongside auxiliary grammatical catalogs (Uṇādipāṭha, Dhātupāṭha, Gaṇapāṭha).

## Current Status

| Measure | Count |
| --- | ---: |
| Target Aṣṭādhyāyī sūtras | 3,959 |
| Registered executable sūtras | 474+ |
| Uṇādipāṭha registered sūtras | 33+ |
| Derivation-state sūtras | 346 |
| Sentence-level kāraka/vibhakti sūtras | 64 |
| Other (Saṃjñā/Paribhāṣā/Adhikāra/etc.) | 64 |
| Remaining Aṣṭādhyāyī sūtras | 3,485 |

The implemented subset supports end-to-end nominal and verbal derivations, declarative Uṇādipāṭha etymological parsing, and sentence kāraka analysis.

---

## Completed Architecture Milestones

1. **Declarative Uṇādipāṭha Subsystem (`:unadipatha`)**:
   - Implemented 33+ Uṇādi sūtras across all 5 Adhyāyas under Aṣṭādhyāyī 3.3.1 (*उणादयो बहुलम्*).
   - Etymological reverse lookup `(Dhātu, Pratyaya) → Saṁjñā` and stem classification (`RUDHI_PRATIPADIKA` vs `YAUGIKA_PRATIPADIKA`).
   - Integrated into `VakyaAnalyzer` for sentence-level nominal stem annotations.

2. **Centralized Partitioned Saṁjñās & Artha Architecture (`:core`)**:
   - `dev.panini.shiksha.Samjna`: Domain-partitioned into `Unit` (DHATU, PRATYAYA, ANGA, PADA, PRATIPADIKA, SAMASA, AVAYAVA), `Affix` (KRT, UNADI, TADDHITA, GHAN, NVUL, TRC, KTA, SHATRU, SHANAC, Aṇ...), `Phono` (VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, IT...), `Stem` (NADI, GHI, BHA, GHU, PRAGRHYA, SARVANAMA, ABHYASA...), `Avyaya` (AVYAYA, NIPATA, GATI, UPASARGA), `Karaka` (KARTA, KARMA...), and `Rudhi(word)`.
   - `dev.panini.shiksha.Artha`: Domain-partitioned into `Karaka` (KARTA, KARMA, BHAVA...), `Dispositional` (TAATSIILYA, SHILPA, AASHIS...), `Taddhita` (APATYA, RAGATA, SAMUHA, MATVARTHIYA...), `Rudhi`, and `Explanation`.

3. **Uṇādi Derivation Bridge & CLI**:
   - `UnadiDerivationEngine` bridges Uṇādi matches into `DerivationState` to run full Aṣṭādhyāyī rule traces.
   - CLI commands `--unadi [lookup|pair|list]` and `--derive-unadi <dhatu> <pratyaya>`.

---

## Implementation Principles

1. Implement each sūtra from reviewed sources as a typed derivation rule.
2. Represent adhikāra, anuvṛtti, dependency, blocking, and ordering metadata.
3. Implement paribhāṣā and asiddha/asiddhavat governance in the engine.
4. Count a sūtra only when its executable logic and tests are complete.
5. Require supported form plans to name the sūtras needed for a complete derivation.
6. Preserve the full rule trace so every surface can be audited.

---

## Sūtra Classification

The Aṣṭādhyāyī contains several functional types of sūtras:

| Type | Sanskrit | Function |
| :--- | :--- | :--- |
| **Vidhi** | विधिसूत्र | Prescribes an operation or rule. Example: add an affix, perform guṇa, substitute a sound. |
| **Niyama** | नियमसूत्र | Restricts a general rule to a narrower situation. "Do this only here." |
| **Parisaṅkhyā** | परिसंख्यासूत्र | Excludes all other possibilities, leaving only specified cases. |
| **Atideśa** | अतिदेशसूत्र | Extends properties or behavior from one item to another ("treat X like Y"). |
| **Adhikāra** | अधिकारसूत्र | Introduces a governing heading whose effect continues until another adhikāra replaces it. |
| **Saṃjñā** | संज्ञासूत्र | Defines technical terms and labels (e.g. *DHATU*, *PRATYAYA*, *PRATIPADIKA*, *BHA*, *GHI*, *NADI*). |
| **Paribhāṣā** | परिभाषासूत्र | Interpretative meta-rules guiding rule application order and scope. |
