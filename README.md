# PaniniVM

**PaniniVM** is a natural programming language in which morphologically
segmented Sanskrit is executable source code. Its Kotlin runtime combines a
Pāṇinian derivation engine (*Aṣṭādhyāyī Rūpa-Siddhi*) with typed semantic
execution: grammatical relationships determine data flow, verbal roots select
operations, and derived identities carry meaning through a program.

Implemented sūtras carry typed metadata, executable eligibility, rule
dependencies, blocking rules, and state transitions. Every derivation retains
an ordered, auditable trace of applied sūtras, *Adhikāras*, *Paribhāṣās*,
*Niṣedhas*, conflicts, and alternatives.

---

## Sanskrit as a Natural Programming Language

Pāṇini’s Aṣṭādhyāyī is the earliest formal, rule-based computational system in human history. PaniniVM leverages this grammatical machinery to treat Sanskrit not merely as text, but as a **fully executable natural programming language**. By writing structured, morphologically segmented Sanskrit sentences (*Uktis*), you write program specifications directly in natural language:

- **Meaning comes from grammar**: Case markings (*Vibhaktis*) establish
  computational relationships through *Kārakas*. Accusative forms identify
  operands, dative forms identify destinations, and ablative forms can identify
  sources or exclusions.
- **Words remain morphologically visible**: Programs are written as fully
  segmented `Prakṛti + Pratyaya` expressions. Each segment contributes to the
  grammatical analysis instead of being treated as punctuation-like syntax.
- **Dhātus express operations**: A verbal root and its affixes select an action;
  nominal expressions supply its typed participants. Pipelines pass the typed
  result of one sentence directly into the next.
- **Natural control and reuse**: Repetition, conditions, ranges, collections,
  and reusable Sanskrit *kriyā* definitions are language constructs rather
  than host-language API calls.
- **Auditable execution**: Source is parsed into a grammatical AST, resolved
  through Pāṇinian identities, planned, and executed with a traceable semantic
  path. Capability-sensitive work can pause for approval and resume safely.

For example, this segmented sentence adds two numbers and sends its result
directly to `योग`:

```pvm
द्वि + अम् त्रि + अम् च युज् + णिच् + लोट् + सिप् ततः योग + ङे दा + लोट् + सिप् ।
```

The source is Sanskrit, while its execution model remains statically
inspectable: `द्वि + अम्` and `त्रि + अम्` are operands, `युज्` supplies
the operation identity, and `योग + ङे` is the result destination.

---

## Workspace Module Architecture (18 Modules)

```
PaniniVM Root
├── :core           Central domain models, Sutra<C,R>, DerivationState, PratyaharaEngine, Adhikara/Paribhasha/Nishedha registries, Saṁjñā & Artha
├── :ashtadhyayi     Executable Aṣṭādhyāyī Sūtra catalog (474+ rules across 8 Adhyāyas: Sandhi, Samāsa, Kāraka, Vibhakti, Subanta, Tiṅanta)
├── :dhatupatha      Complete 10-Gaṇa Dhātupāṭha root catalog (Bhvādi, Adādi, Juhotyādi, Divādi, Svādi, Tudādi, Rudhādi, Tanādi, Kryādi, Curādi)
├── :unadipatha      Declarative Uṇādipāṭha subsystem (33+ sūtras across all 5 Adhyāyas under 3.3.1 उणादयो बहुलम्), etymological stem analyzer
├── :linganushasanam Pāṇinian Liṅgānuśāsanam gender resolution subsystem (Sūtras across all 5 Adhyāyas for nominal, affixal, and compound gender)
├── :derivation      DerivationEngine, SubantaEngine, TingantaEngine, SamasaEngine, SankhyaGenerator, UnadiDerivationBridge, UnadiDerivationEngine
├── :analysis        Semantic sentence analyzer, resolver for case-marked dependencies (Kārakas) and morphological syncretism
├── :parser          ANTLR4 canonical segmented Sanskrit parser and AST compiler builder (Prakṛti + Pratyaya)
├── :actions         Action dispatchers (PaniniAction, DhatuAction, SubantaAction, TingantaAction, SankhyaAction)
├── :compiler        Sanskrit natural programming language bytecode compiler, compiling .pvm scripts directly to native JVM .class bytecode
├── :cli             Command-line interface (Main.kt) for script execution, etymological lookup, nominal/verbal derivations, and coverage inspection
├── :aryabhatiya     Āryabhaṭīya Varga & Avarga consonant-vowel numerical encoder/decoder (ख्युघृ = 4,320,000)
├── :bhutasamkhya    Bhūtasamkhyā symbolic noun decoder (नेत्र = 2, वेद = 4, अङ्कानां वामतो गतिः)
├── :sankhya         Numeral generator & transformer
├── :katapayadi      Kaṭapayādi numerical cipher encoder/decoder (नञावचश्च शून्यानि)
├── :ganapatha       Gaṇapāṭha nominal list registry
├── :execution       Sanskrit natural programming language execution runtime, turn-based dialogue context tracking, sandboxed safety gating
└── :idea-plugin     IntelliJ IDEA plugin integration module
```

---

## Major Features

- **Executable Aṣṭādhyāyī Derivation Engine (`:ashtadhyayi`)**: 474+ executable rules covering nominal declensions (*Subanta*), verbal conjugations (*Tiṅanta*), compound formations (*Samāsa*), case assignments (*Vibhakti*), semantic roles (*Kāraka*), and morpho-phonology (*Sandhi*).
- **Pure Declarative Uṇādipāṭha Subsystem (`:unadipatha`)**: 33+ sūtras across all 5 Adhyāyas performing suffix assignment, etymological reverse lookup `(Dhātu, Pratyaya) → Saṁjñā`, and nominal stem classification (`RUDHI_PRATIPADIKA` vs `YAUGIKA_PRATIPADIKA`).
- **Sūtra-Driven Liṅgānuśāsanam Subsystem (`:linganushasanam`)**: Standalone Sūtra-by-Sūtra gender resolution across all 5 canonical sections (*Strīliṅgam*, *Puṃliṅgam*, *Napuṃsakaliṅgam*, *Viśeṣyanighnaliṅgam*, *Samāsaliṅgam*), driving authentic Pāṇinian gender resolution for `SubantaEngine` and `SamasaEngine`.
- **Centralized & Partitioned Pāṇinian Saṁjñā & Artha Architecture (`:core`)**:
  - `dev.panini.shiksha.Samjna`: Domain-partitioned into `Unit` (DHATU, PRATYAYA, ANGA, PADA, PRATIPADIKA, SAMASA, AVAYAVA), `Affix` (KRT, UNADI, TADDHITA, SARVADHATUKA, ARDHADHATUKA, GHAN, NVUL, TRC, KTA, SHATRU, SHANAC, Aṇ...), `Phono` (VRDDHI, GUNA, IK, AC, HAL, SAMYOGA, IT...), `Stem` (NADI, GHI, BHA, GHU, PRAGRHYA, SARVANAMA, ABHYASA...), `Avyaya` (AVYAYA, NIPATA, GATI, UPASARGA), `Karaka` (KARTA, KARMA...), and `Rudhi(word)`.
  - `dev.panini.shiksha.Artha`: Domain-partitioned into `Karaka` (KARTA, KARMA, BHAVA...), `Dispositional` (TAATSIILYA, SHILPA, AASHIS...), `Taddhita` (APATYA, RAGATA, SAMUHA, MATVARTHIYA...), `Rudhi`, and `Explanation`.
- **Uṇādi Derivation Bridge (`UnadiDerivationEngine`)**: Bridges Uṇādi suffix assignment into `DerivationState` to execute full Aṣṭādhyāyī morpho-phonological rule traces (*Anubandha-lopa*, *Guṇa/Vṛddhi*, *Aṅga-kārya*) for nominal stems like `कारु`, `पितृ`, `कवि`, `ऋषि`, `वेधस्`.
- **Vākya Sentence Analyzer (`:analysis`)**: `VakyaAnalyzer` resolves syncretic case endings (e.g. `भ्याम्` across तृतीया, चतुर्थी, पञ्चमी) via 1.4 Kāraka and 2.3 Vibhakti sūtras, and automatically enriches sentence parses with Uṇādi etymological stem analyses.
- **Samāsa (Compound Formation) Subsystem**: Executable compound formation Sūtras across Avyayībhāva (2.1.6), Tatpuruṣa (2.1.24, 2.1.37, 2.2.8), Bahuvrīhi (2.2.24), and Dvandva (2.2.29).
- **Phonological Sandhi Engine**: Type-safe Sandhi Sūtras (`6.1.109`, `6.1.132`, `6.3.111`, `8.3.14`, `8.3.17`, `8.3.22`, `8.4.59`, `8.4.60`, `8.4.62`, `8.4.63`, `8.4.65`) driven by `PratyaharaEngine` (Māheśvara-sūtras).
- **ANTLR4 Segmented Sanskrit Parser (`:parser`)**: Parses strictly segmented Pāṇinian words (`Prakṛti + Pratyaya`) through one canonical grammar.
- **JVM Sanskrit Bytecode Compiler (`:compiler`)**: Compiles multi-clause `.pvm` script files directly into native JVM `.class` bytecode carrying stack constants and resolved execution plans.
- **First-class Sanskrit Kriyā Definitions (`:execution`)**: Reusable `saṃjñā-kriyā` blocks support typed and named inputs, positional or named calls, primitive and structured results, local scope, overload dispatch, multi-file visibility, and direct typed pipeline composition.

---

## Input Boundary

PaniniVM accepts annotated morphological notation, not ordinary surface Sanskrit. Declinable and conjugable inputs expose their grammatical components (e.g. `एक + अम्` or `युज् + णिच् + लोट् + सिप्`).

The execution path follows a single direction:

```text
segmented input → vyākaraṇa AST → binding → operation resolution → planning → runtime
```

## Reusable Typed Sanskrit Kriyās

For a complete language tutorial, see
[`docs/pvm-language-guide.md`](docs/pvm-language-guide.md).
For upcoming syntax and tooling milestones, see the
[`PVM language enhancement plan`](docs/pvm-language-enhancement-plan.md).

A `.pvm` program can declare a reusable operation using a nominal
`saṃjñā-kriyā` header. Signature declarations are grammatical sentences inside
the block and are not executed as body actions.

```pvm
योजन + ल्युट् + सुँ ।
वाम + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
दक्षिण + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
सङ्ख्या + सुँ इति परिणाम + सुँ ।
वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥
```

The operation accepts the original positional call form:

```pvm
द्वि + अम् त्रि + अम् च योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

It also accepts named arguments. A parameter name uses ṣaṣṭhī and its value
immediately follows in dvitīyā; named arguments may appear in any order.

```pvm
दक्षिण + ङस् त्रि + अम् वाम + ङस् द्वि + अम् योजन + ल्युट् + टा कृ + लोट् + सिप् ।
```

Supported signature types are `सङ्ख्या`, `शब्द`, and `सूची`. A result may also
name a declared `…परिणाम + मतुप्` schema. Typed values retain their semantic
type when one saṃjñā-kriyā feeds another pipeline stage. The runtime and IDEA
plugin diagnose duplicate declarations, missing or unknown named arguments,
arity/type mismatches, incompatible pipeline stages, and invalid result schemas.

Function bodies have isolated child scopes and may call other public or
file-private `अन्तरङ्ग` definitions. Domain dispatch through `अधिकार`,
`अपवाद`/`नित्य`/`अन्तरङ्ग` precedence, typed overload selection, and `क्त`
memoization continue to apply. Optional/default parameters and explicit early
return syntax are planned conveniences; they are not required for reusable
first-class definitions.

---

## Running PaniniVM

The command-line module evaluates and compiles `.pvm` programs, renders
readable Sanskrit companions, and provides an interactive REPL. Build the CLI
distribution once, then use its launcher directly. This is required for
interactive programs because Gradle's console proxy may not forward terminal
input correctly.

```sh
./gradlew :cli:installDist
```

Run a segmented Sanskrit program, including one that reads interactive input:

```sh
./cli/build/install/cli/bin/cli --eval examples/algorithms/fibonacci_array.pvm
./cli/build/install/cli/bin/cli --eval examples/io/io_demo.pvm
./cli/build/install/cli/bin/cli --eval projects/number-guessing-game/number_guessing_game.pvm
```

Running the launcher without arguments starts the PaniniVM REPL:

```sh
./cli/build/install/cli/bin/cli
```

Generate readable Sanskrit companions from segmented source, or compile a
`.pvm` program to a JVM class:

```sh
./cli/build/install/cli/bin/cli --render-readable examples/algorithms/fibonacci_array.pvm
./cli/build/install/cli/bin/cli --compile examples/arithmetic/addition.pvm SanskritAddition
```

Canonical sūtra programs can be emitted, checked, and executed independently:

```sh
./cli/build/install/cli/bin/cli --emit-grantha examples/arithmetic/addition.pvm addition.sutra
./cli/build/install/cli/bin/cli --check-grantha addition.sutra
./cli/build/install/cli/bin/cli --grantha addition.sutra
```

See the complete [CLI command reference](cli/README.md) for Windows launcher
commands, typed interactive input, derivation tools, and every supported flag.

For `.pvm` editing, diagnostics, navigation, and gutter execution in IntelliJ
IDEA or Android Studio, see the detailed
[IDEA plugin installation guide](idea-plugin/README.md#install-the-plugin-from-disk).

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
val result = vm.eval("दशन् + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
if (result is ExecutionResult.Success) {
    println(result.value) // Output: द्वादश
}
```

---

## Test Suite Verification

Run all automated unit and integration tests across the Gradle workspace:

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
