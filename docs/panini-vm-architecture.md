# PaniniVM Architecture & Segmented ANTLR4 Parser Specification

## Overview

PaniniVM provides an execution runtime for Sanskrit utterances specified in Pāṇinian segmented form (`Prakṛti + Pratyaya`). The canonical ANTLR4 pair `VyakaranamLexer.g4` and `VyakaranamParser.g4` creates the grammar parse tree, and `VyakaranamAstBuilder` constructs the typed AST. `VyakaranamExecutionAdapter` binds that AST directly to dhātu invocations and an `ExecutionProgram`. `OperationResolver` selects a registered executable meaning from grammatical features and kāraka signatures, after which the internal pipeline plans and executes it.

```text
segmented utterance
    -> canonical vyākaraṇa AST
    -> direct execution binding
    -> registered dhātu-operation resolution
    -> planning and authority checks
    -> action execution
```

Executable meanings are registered in the execution package. Dhātupāṭha entries contain linguistic data only and do not depend on runtime actions.

Sentence rules use `SutraScope.VAKYA`; no separate kāraka-rule base class or parallel rule framework exists.

---

## 1. Centralized & Partitioned Pāṇinian Saṁjñās & Artha (`:core`)

All grammatical labels and meanings are centralized in `:core` under `dev.panini.shiksha`:

### Saṁjñā Partitioned Hierarchy (`dev.panini.shiksha.Samjna`)
- **`Unit`**: `DHATU`, `PRATYAYA`, `ANGA`, `PADA`, `PRATIPADIKA`, `SAMASA`, `AVAYAVA` (1.3.1, 1.4.13, 1.4.14).
- **`Affix`**: `KRT`, `UNADI`, `TADDHITA`, `SARVADHATUKA`, `ARDHADHATUKA`, `GHAN`, `NVUL`, `TRC`, `KTA`, `SHATRU`, `SHANAC`, `GHINUN`, `AN`, `IN`, `CHHA`, `MATUP`, `MAYAT`, `TAL`... (3.1.93, 3.3.1, 4.1.76).
- **`Phono`**: `VRDDHI`, `GUNA`, `IK`, `AC`, `HAL`, `SAMYOGA`, `ANUNASIKA`, `SAVARNA`, `IT`, `CONSONANT_STEM` (1.1.1, 1.1.2, 1.1.7).
- **`Stem`**: `NADI`, `GHI`, `BHA`, `GHU`, `PRAGRHYA`, `SARVANAMA`, `APRUKTA`, `SAMBUDDHI`, `SARVANAMASTHANA`, `ABHYASA` (1.4.3, 1.4.7, 1.4.18).
- **`Avyaya`**: `AVYAYA`, `NIPATA`, `GATI`, `UPASARGA` (1.1.37, 1.4.56, 1.4.59).
- **`Karaka`**: `KARTA`, `KARMA`, `KARANA`, `SAMPRADANA`, `APADANA`, `ADHIKARANA` (1.4.23 - 1.4.55).
- **`Rudhi`**: `data class Rudhi(val word: String)` (Conventional lexical names).

### Artha Partitioned Hierarchy (`dev.panini.shiksha.Artha`)
- **`Karaka`**: `KARTA`, `KARMA`, `KARANA`, `SAMPRADANA`, `APADANA`, `ADHIKARANA`, `BHAVA`.
- **`Dispositional`**: `TAATSIILYA`, `TADDHARMA`, `TATSADHUKARI`, `SHILPA`, `AASHIS`.
- **`Taddhita`**: `APATYA`, `RAGATA`, `SAMUHA`, `MATVARTHIYA`, `THAK`, `BHAVA_TADDHITA`.
- **`Rudhi`**: `data class Rudhi(val devanagari: String, val english: String?)`.
- **`Explanation`**: `data class Explanation(val hindi: String, val english: String?)`.

---

## 2. Declarative Uṇādipāṭha Subsystem (`:unadipatha`)

The Uṇādipāṭha module provides a pure declarative catalog of Uṇādi rules (33+ sūtras across all 5 Adhyāyas under Aṣṭādhyāyī 3.3.1 *उणादयो बहुलम्*):

- **Suffix Assignment Only**: Performs suffix assignment without taking part directly in Sandhi or morpho-phonological rule applications.
- **Etymological Reverse Lookup**: Maps `(Dhātu, Pratyaya) → Saṁjñās` and nominal words to their underlying root + suffix breakdown.
- **Stem Classification**: `UnadiAnalyzer` classifies parsed nominal stems into `RUDHI_PRATIPADIKA` (conventional noun e.g. `दारु`, `शारु`) vs `YAUGIKA_PRATIPADIKA` (derivational noun e.g. `कारु`, `पायु`).
- **Derivation Bridge (`UnadiDerivationEngine`)**: Bridges Uṇādi suffix assignment into `DerivationState` to execute step-by-step Aṣṭādhyāyī derivation rule applications (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*).
- **Vākya Sentence Integration**: `VakyaAnalyzer` automatically enriches nominal stem parses in sentences with Uṇādi etymological annotations.

---

## 3. Grammatical Form Specification (सुप्तिङन्तं पदम्)

In accordance with Pāṇini's definition *सुप्तिङन्तं पदम्* (1.4.14), every declinable nominal or conjugable verb must be explicitly segmented with its suffix:

### Nominal Subanta Padas (`subantaPada` / `sankhyaPada`)
- **Format**: `nominalBase (+ stem)* + supPratyaya`
- **Examples**:
  - `एक + अम्` (Numeral stem `एक` + 2nd Vibhakti singular `अम्`)
  - `द्वि + औट्` (Numeral stem `द्वि` + 2nd Vibhakti dual `औट्`)
  - `त्रि + शस्` (Numeral stem `त्रि` + 2nd Vibhakti plural `शस्`)
  - `द्वि + तीय + अम्` (Ordinal stem `द्वि + तीय` + 2nd Vibhakti singular `अम्`)
  - `त्रि + तीय + अंश + अम्` (Fraction stem `त्रि + तीय + अंश` + 2nd Vibhakti singular `अम्`)
  - `सार्ध + द्वि + अम्` (Rational fraction `सार्ध + द्वि` = 2.5 + 2nd Vibhakti singular `अम्`)
  - `अभि + अधिक + शत + अम्` (Modifier stem `अभि + अधिक + शत` = 100 + 2nd Vibhakti singular `अम्`)
  - `ज्या + नवति + अम्` (Trigonometric stem `ज्या + नवति` = $\sin(90^\circ)$ + 2nd Vibhakti singular `अम्`)
  - `यन्त्र + सुँ` (Nominal stem `यन्त्र` + 1st Vibhakti singular `सुँ`)
  - `फल + अम्` (Result reference `फल` + 2nd Vibhakti singular `अम्`)
  - `पूर्वफल + अम्` (Historical reference `पूर्वफल` + 2nd Vibhakti singular `अम्`)

---

## 4. Syncretic Sup Endings and Kāraka Resolution

A sup surface may represent more than one slot. In particular:

```text
भ्याम् → {तृतीया-द्विवचन, चतुर्थी-द्विवचन, पञ्चमी-द्विवचन}
```

`PadaAnalyzer` preserves all candidates. The sentence analyzer then applies semantic dhātu-valency facts and ordered sūtras:

```text
राम + भ्याम् … दा
  → 1.4.32 सम्प्रदान
  → 2.3.13 चतुर्थी

लेखनी + भ्याम् … लिख्
  → 1.4.42 करण
  → 2.3.18 तृतीया

राम + भ्याम् … पलाय्
  → 1.4.24 अपादान
  → 2.3.28 पञ्चमी
```

The currently registered sentence rules are stored in `adhyaya1/pada4/*Sutra.kt` and `adhyaya2/pada3/*Sutra.kt`.

---

## 5. `.pvm` Script File Format & `PvmUktiSadhaka` Engine

`.pvm` (PaniniVM) files store sequential Pāṇinian grammatical and semantic program instructions.

### `PvmUktiSadhaka` (Pāṇinian Grammatical Rūpa-Siddhi)
The `PvmUktiSadhaka` engine evaluates parsed AST nodes to perform full `rūpa-siddhi` (रूपसिद्धि) on segmented `.pvm` script lines:
- **`sadhayaSubanta(subantaPada)`**: Binds `MūlaPrātipadika`, `KṛdantaPrātipadika`, `UṇādyantaPrātipadika`, or `SamāsaPrātipadika` with `SupAffix` via `SubantaEngine` to derive exact declension surfaces.
- **`sadhayaTinganta(tingantaPada)`**: Binds Dhātu root, `Upasarga` prefixes, Lakāra mode, and `TingAffix` via `TingantaEngine` to derive exact verbal conjugation surfaces.

---

## 6. Verification

Run the full test suite:
```powershell
./gradlew test --no-daemon
```
