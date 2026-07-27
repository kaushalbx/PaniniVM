# PaniniVM

Kotlin implementation of an executable Pāṇinian derivation system and natural semantic execution engine. Implemented sūtras carry typed metadata, executable eligibility, and state-transition logic. Derivations retain an ordered rule trace, including conflicts and blocked alternatives where available.

---

## Features

- **Executable Ashtadhyayi Engine**: Registered sūtras across derivation, compound formation, sentence analysis, and nominal/verbal morphological scopes.
- **Declarative Uṇādipāṭha Subsystem (`:unadipatha`)**: Pure declarative catalog of Uṇādi rules (33+ sūtras across all 5 Adhyāyas) performing suffix assignment, etymological reverse lookup `(Dhātu, Pratyaya) → Saṁjñā`, and stem classification (`RUDHI_PRATIPADIKA` vs `YAUGIKA_PRATIPADIKA`).
- **Centralized Partitioned Saṁjñā & Artha Architecture (`:core`)**:
  - `dev.panini.shiksha.Samjna`: Domain-partitioned into `Unit` (DHATU, PRATYAYA, ANGA, PADA, PRATIPADIKA, SAMASA, AVAYAVA), `Affix` (KRT, UNADI, TADDHITA, GHAN, NVUL, TRC, KTA, SHATRU, SHANAC, Aṇ...), `Phono` (VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, IT...), `Stem` (NADI, GHI, BHA, GHU, PRAGRHYA, SARVANAMA, ABHYASA...), `Avyaya` (AVYAYA, NIPATA, GATI, UPASARGA), `Karaka` (KARTA, KARMA...), and `Rudhi(word)`.
  - `dev.panini.shiksha.Artha`: Domain-partitioned into `Karaka` (KARTA, KARMA, BHAVA...), `Dispositional` (TAATSIILYA, SHILPA, AASHIS...), `Taddhita` (APATYA, RAGATA, SAMUHA, MATVARTHIYA...), `Rudhi`, and `Explanation`.
- **Uṇādi Derivation Bridge (`UnadiDerivationEngine`)**: Bridges Uṇādi suffix assignment into `DerivationState` to execute full Aṣṭādhyāyī morpho-phonological rule traces (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*) for nominal stems like `कारु`, `पितृ`, `कवि`, `ऋषि`.
- **Vākya Sentence Analyzer Integration (`:analysis`)**: `VakyaAnalyzer` automatically enriches sentence parses with Uṇādi etymological stem analyses.
- **Samāsa (Compound Formation) Subsystem**: Executable compound formation Sūtras across Avyayībhāva (2.1.6), Tatpuruṣa (2.1.24, 2.1.37, 2.2.8), Bahuvrīhi (2.2.24), and Dvandva (2.2.29).
- **Phonological Transformation & Sandhi Engine**: Type-safe Sandhi Sūtras (`6.1.109`, `6.1.132`, `6.3.111`, `8.3.14`, `8.3.17`, `8.3.22`, `8.4.59`, `8.4.60`, `8.4.62`, `8.4.63`, `8.4.65`) driven by `PratyaharaEngine` and inline rule matching.
- **Adhikāra Domain Registry (`AdhikaraRegistry`)**: Enforces governing heading scopes (`1.4.1`, `1.4.23`, `2.3.1 Anabhihite`, `3.1.1`, `3.1.91`, `6.4.1`, `8.1.16`) wrapping top-level concrete `Sutra` objects.
- **Paribhāṣā Meta-rule Registry (`ParibhashaRegistry`)**: Manages interpretive meta-rules (`1.1.3`, `1.1.46`, `1.1.47`, `1.1.49`, `1.1.50`, `1.1.51`, `1.1.52`, `1.1.53`, `1.1.54`, `1.1.55`, `1.1.56`, `1.1.66`, `1.1.67`) with type-safe `ParibhashaScope` enums.
- **Pratiṣedha Prohibition Engine (`NishedhaRuleEngine`)**: Evaluates prohibition sūtras (`1.1.5`, `1.1.6`, `1.1.10`, `1.2.4`) integrated into rule resolution loops.
- **ANTLR4 Segmented Sanskrit Parser (`VyakaranamLexer.g4` + `VyakaranamParser.g4`)**: Parses strictly segmented Pāṇinian words (`Prakṛti + Pratyaya`) through one canonical grammar with pure `IDENTIFIER` stem parsing.
- **Āryabhaṭīya Numeral Decoder (`:aryabhatiya`)**: Decodes Āryabhaṭīya Varga and Avarga consonant-vowel numerical encodings (`ख्युघृ` = 4,320,000).
- **Bhūtasamkhya Symbolic Decoder (`:bhutasamkhya`)**: Decodes symbolic noun numbers (`नेत्र` = 2, `वेद` = 4, `अग्नि` = 3) following the Pāṇinian right-to-left convention (*अङ्कानां वामतो गतिः*).
- **PaniniVM Program Script Runner (`.pvm`)**: Evaluate multi-line `.pvm` program script files with turn history and session persistence.
- **CLI Runner**: Command-line execution for `.pvm` scripts, Uṇādi inspection/derivation, verb derivations, nominal paradigms, and sūtra coverage.
- **Sanskrit Bytecode Compiler**: Compile multi-clause `.pvm` script files directly into native JVM `.class` bytecode.

---

## Input Boundary

PaniniVM accepts annotated morphological notation, not ordinary surface Sanskrit. Declinable and conjugable inputs expose their grammatical components (e.g. `एक + अम्` or `युज् + णिच् + लोट् + सिप्`).

The execution path has one direction:

```text
segmented input → vyākaraṇa AST → binding → operation resolution → planning → runtime
```

---

## CLI Usage

### Uṇādipāṭha Subsystem Commands

```sh
# Etymological lookup for a nominal stem
./gradlew :cli:run --args="--unadi lookup पितृ"

# Reverse lookup by Dhātu and Pratyaya
./gradlew :cli:run --args="--unadi pair कृ कनिन्"

# List registered Uṇādipāṭha sūtras
./gradlew :cli:run --args="--unadi list"

# Step-by-step Aṣṭādhyāyī derivation trace for Uṇādi stems
./gradlew :cli:run --args="--derive-unadi कृ उण्"
./gradlew :cli:run --args="--derive-unadi पा तृन्"
```

### Executing `.pvm` Script Files

```sh
./gradlew :cli:run --args="--eval src/test/kotlin/dev/panini/parser/addition.pvm"
```

### Compiling `.pvm` Script Files to JVM Bytecode

```sh
./gradlew :cli:run --args="--compile src/test/kotlin/dev/panini/parser/addition.pvm SanskritAddition"
```

### Derivations & Sūtra Inspection

```sh
# Nominal paradigms and individual derivations
./gradlew :cli:run --args="--paradigm राम"
./gradlew :cli:run --args="--derive राम SASTHI BAHUVACANA"
./gradlew :cli:run --args="--derive राम षष्ठी बहुवचन"

# Verbal derivations
./gradlew :cli:run --args="--verb भू"
./gradlew :cli:run --args="--verb भू LING EKAVACANA"
./gradlew :cli:run --args="--verb भू LOT बहुवचन"

# Cardinal and ordinal numeral derivations
./gradlew :cli:run --args="--sankhya 23"
./gradlew :cli:run --args="--sankhya 6 ordinal"
./gradlew :cli:run --args="--sankhya 42 cardinal --variants"

# Registry inspection and implementation coverage
./gradlew :cli:run --args="--coverage"
./gradlew :cli:run --args="--sutra 7.1.54"
```

---

## Programmatic API

```kotlin
import dev.panini.execution.PaniniVM
import dev.panini.execution.ExecutionResult
import dev.panini.unadipatha.UnadiDerivationEngine
import dev.panini.unadipatha.analysis.UnadiAnalyzer
import java.io.File

// Uṇādi Etymological Stem Analysis
val stemAnalysis = UnadiAnalyzer.analyzeStem("पितृ")
println(stemAnalysis.classification) // Output: YAUGIKA_PRATIPADIKA

// Uṇādi Derivation Tracing
val derivationResult = UnadiDerivationEngine.derive("कृ", "उण्")
println(derivationResult.final.surface) // Output: कारु

// Single utterance evaluation via VM
val vm = PaniniVM()
val result = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
if (result is ExecutionResult.Success) {
    println(result.value) // Output: द्वादश
}
```

---

## Test Suite

Run all automated unit and integration tests across all 15 Gradle modules:

```sh
./gradlew check --no-daemon
```

---

## Current Ashtadhyayi Coverage

- **474+ registered executable sūtras** out of the target catalog.
- **33+ Uṇādipāṭha Sūtras** implemented in a pure, declarative catalog.
- **100% Full Coverage of Aṣṭādhyāyī 2.3 Vibhakti Sūtras**: All 64 classical non-Vedic sūtras implemented and verified.
- **100% Full Coverage of Aṣṭādhyāyī 1.4 Kāraka Sūtras**: All 33 classical Kāraka saṃjñā sūtras implemented and verified.
- **100% Full Coverage of Nominal (Subanta) Stem Classes**: All 31 Classical Sanskrit nominal stem categories implemented across 3 genders (masculine, feminine, neuter), pronouns, numerals, and consonant stems.
- **Centralized & Partitioned Pāṇinian Saṁjñās & Artha**: Cleanly typed in `:core` (`Samjna.Unit`, `Samjna.Affix`, `Samjna.Phono`, `Samjna.Stem`, `Samjna.Avyaya`, `Samjna.Karaka`, `Samjna.Rudhi`, `Artha.Karaka`, `Artha.Dispositional`, `Artha.Taddhita`, `Artha.Rudhi`).

---

## Text Encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the executable specification, so editors and terminals must preserve UTF-8 when changing Devanagari text.
