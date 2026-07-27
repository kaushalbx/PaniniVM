# PaniniVM

**PaniniVM** is a high-performance, modular Kotlin implementation of an executable Pāṇinian derivation engine (*Aṣṭādhyāyī Rūpa-Siddhi*) and natural semantic execution runtime. Implemented sūtras carry typed metadata, executable eligibility, rule dependencies, blocking rules, and state-transition logic. Every derivation retains an ordered, auditable rule trace including applied sūtras, *Adhikāras*, *Paribhāṣās*, *Niṣedhas*, conflicts, and alternatives.

---

## Workspace Module Architecture (16 Modules)

```
PaniniVM Root
├── :core          Central domain models, Sutra<C,R>, DerivationState, PratyaharaEngine, Adhikara/Paribhasha/Nishedha registries, centralized partitioned Samjna & Artha
├── :ashtadhyayi    Executable Aṣṭādhyāyī Sūtra catalog (474+ rules across 8 Adhyāyas: Sandhi, Samāsa, Kāraka, Vibhakti, Subanta, Tiṅanta)
├── :dhatupatha     Complete 10-Gaṇa Dhātupāṭha root catalog (Bhvādi, Adādi, Juhotyādi, Divādi, Svādi, Tudādi, Rudhādi, Tanādi, Kryādi, Curādi)
├── :unadipatha     Declarative Uṇādipāṭha subsystem (33+ sūtras across all 5 Adhyāyas under 3.3.1 उणादयो बहुलम्), reverse lookup, etymological stem analyzer
├── :derivation     DerivationEngine, SubantaEngine, TingantaEngine, SankhyaGenerator, UnadiDerivationBridge, UnadiDerivationEngine
├── :analysis       PadaAnalyzer, VakyaAnalyzer, sentence-level kāraka syncretism resolution (e.g. भ्याम्), automatic Uṇādi stem annotations
├── :parser         ANTLR4 canonical grammar (VyakaranamLexer.g4 + VyakaranamParser.g4), AST builder for segmented input (Prakṛti + Pratyaya)
├── :actions        Action dispatchers (PaniniAction, DhatuAction, SubantaAction, TingantaAction, SankhyaAction)
├── :compiler       BytecodeCompiler, compiling .pvm script files directly into native JVM .class bytecode with mapped stack constants
├── :cli            Command-line interface (Main.kt) for script execution, Uṇādi lookup/derivation, verb/nominal derivations, coverage inspection
├── :aryabhatiya    Āryabhaṭīya Varga & Avarga consonant-vowel numerical encoder/decoder (ख्युघृ = 4,320,000)
├── :bhutasamkhya   Bhūtasamkhyā symbolic noun decoder (नेत्र = 2, वेद = 4, अङ्कानां वामतो गतिः)
├── :sankhya        Numeral generator & transformer
├── :katapayadi     Kaṭapayādi numerical cipher encoder/decoder (नञावचश्च शून्यानि)
├── :ganapatha      Gaṇapāṭha nominal list registry
└── :idea-plugin    IntelliJ IDEA plugin integration module
```

---

## Major Features

- **Executable Aṣṭādhyāyī Derivation Engine (`:ashtadhyayi`)**: 474+ executable rules covering nominal declensions (*Subanta*), verbal conjugations (*Tiṅanta*), compound formations (*Samāsa*), case assignments (*Vibhakti*), semantic roles (*Kāraka*), and morpho-phonology (*Sandhi*).
- **Pure Declarative Uṇādipāṭha Subsystem (`:unadipatha`)**: 33+ sūtras across all 5 Adhyāyas performing suffix assignment, etymological reverse lookup `(Dhātu, Pratyaya) → Saṁjñā`, and nominal stem classification (`RUDHI_PRATIPADIKA` vs `YAUGIKA_PRATIPADIKA`).
- **Centralized & Partitioned Pāṇinian Saṁjñā & Artha Architecture (`:core`)**:
  - `dev.panini.shiksha.Samjna`: Domain-partitioned into `Unit` (DHATU, PRATYAYA, ANGA, PADA, PRATIPADIKA, SAMASA, AVAYAVA), `Affix` (KRT, UNADI, TADDHITA, SARVADHATUKA, ARDHADHATUKA, GHAN, NVUL, TRC, KTA, SHATRU, SHANAC, Aṇ...), `Phono` (VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, IT...), `Stem` (NADI, GHI, BHA, GHU, PRAGRHYA, SARVANAMA, ABHYASA...), `Avyaya` (AVYAYA, NIPATA, GATI, UPASARGA), `Karaka` (KARTA, KARMA...), and `Rudhi(word)`.
  - `dev.panini.shiksha.Artha`: Domain-partitioned into `Karaka` (KARTA, KARMA, BHAVA...), `Dispositional` (TAATSIILYA, SHILPA, AASHIS...), `Taddhita` (APATYA, RAGATA, SAMUHA, MATVARTHIYA...), `Rudhi`, and `Explanation`.
- **Uṇādi Derivation Bridge (`UnadiDerivationEngine`)**: Bridges Uṇādi suffix assignment into `DerivationState` to execute full Aṣṭādhyāyī morpho-phonological rule traces (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*) for nominal stems like `कारु`, `पितृ`, `कवि`, `ऋषि`, `वेधस्`.
- **Vākya Sentence Analyzer (`:analysis`)**: `VakyaAnalyzer` resolves syncretic case endings (e.g. `भ्याम्` across तृतीया, चतुर्थी, पञ्चमी) via 1.4 Kāraka and 2.3 Vibhakti sūtras, and automatically enriches sentence parses with Uṇādi etymological stem analyses.
- **Samāsa (Compound Formation) Subsystem**: Executable compound formation Sūtras across Avyayībhāva (2.1.6), Tatpuruṣa (2.1.24, 2.1.37, 2.2.8), Bahuvrīhi (2.2.24), and Dvandva (2.2.29).
- **Phonological Sandhi Engine**: Type-safe Sandhi Sūtras (`6.1.109`, `6.1.132`, `6.3.111`, `8.3.14`, `8.3.17`, `8.3.22`, `8.4.59`, `8.4.60`, `8.4.62`, `8.4.63`, `8.4.65`) driven by `PratyaharaEngine` (Māheśvara-sūtras).
- **ANTLR4 Segmented Sanskrit Parser (`:parser`)**: Parses strictly segmented Pāṇinian words (`Prakṛti + Pratyaya`) through one canonical grammar.
- **JVM Sanskrit Bytecode Compiler (`:compiler`)**: Compiles multi-clause `.pvm` script files directly into native JVM `.class` bytecode carrying stack constants and resolved execution plans.

---

## Input Boundary

PaniniVM accepts annotated morphological notation, not ordinary surface Sanskrit. Declinable and conjugable inputs expose their grammatical components (e.g. `एक + अम्` or `युज् + णिच् + लोट् + सिप्`).

The execution path follows a single direction:

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
./gradlew :cli:run --args="--derive-karaka राम KARMAN EKAVACANA भू KARTARI"

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

## Test Suite Verification

Run all automated unit and integration tests across all 16 Gradle modules:

```sh
./gradlew check --no-daemon
```

---

## Implementation Statistics & Coverage

- **474+ registered executable Aṣṭādhyāyī sūtras** across derivation, sandhi, samāsa, kāraka, and vibhakti scopes.
- **33+ Uṇādipāṭha Sūtras** registered in pure declarative catalog.
- **100% Full Coverage of Aṣṭādhyāyī 2.3 Vibhakti Sūtras**: All 64 classical non-Vedic sūtras implemented and verified.
- **100% Full Coverage of Aṣṭādhyāyī 1.4 Kāraka Sūtras**: All 33 classical Kāraka saṃjñā sūtras implemented and verified.
- **100% Full Coverage of Nominal (Subanta) Stem Classes**: All 31 Classical Sanskrit nominal stem categories implemented across 3 genders (masculine, feminine, neuter), pronouns, numerals, and consonant stems.
- **10-Gaṇa Dhātupāṭha Catalog**: Complete root coverage across all ten traditional verbal gaṇas.
- **Centralized Partitioned Saṁjñās & Artha**: Typed in `:core` (`Samjna.Unit`, `Samjna.Affix`, `Samjna.Phono`, `Samjna.Stem`, `Samjna.Avyaya`, `Samjna.Karaka`, `Samjna.Rudhi`, `Artha.Karaka`, `Artha.Dispositional`, `Artha.Taddhita`, `Artha.Rudhi`, `Artha.Explanation`).

---

## Text Encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the executable specification, so editors and terminals must preserve UTF-8 when changing Devanagari text.
