# PaniniVM

Kotlin implementation of an executable Pāṇinian derivation system and natural semantic execution engine. Implemented
sūtras carry typed metadata, executable eligibility, and state-transition logic. Derivations retain an ordered rule trace, including conflicts and blocked alternatives where available.

---

## Features

- **Executable Ashtadhyayi Engine**: 303 implemented sūtras with complete rule traces across nominal (`sup`) and verbal (`tiṅ`) paradigms.
- **ANTLR4 Segmented Sanskrit Parser (`VyakaranamLexer.g4` + `VyakaranamParser.g4`)**: Parses strictly segmented Pāṇinian words (`Prakṛti + Pratyaya`) through one canonical grammar:
  - Nominal Subanta: `एक + अम्`, `द्वि + औट्`, `त्रि + शस्`, `यन्त्र + सुँ`, `फल + अम्`, `पूर्वफल + अम्`
  - Verbal Tiṅanta: `युज् + णिच् + लोट् + सिप्`, `गण + णिच् + लोट् + सिप्`, `हृ + लोट् + सिप्`
- **Multi-Vākya Sentence Chaining**: Parse and execute multi-clause sentences separated by connectives (`ततः`, `अथ`, `अनन्तरम्`) or daṇḍas (`।`, `॥`).
- **Dynamic Inter-Clause Result References**: Refer to intermediate calculation results (`फल + अम्`) across clauses (`योग-1`, `योग-2`) or session history (`पूर्वफल + अम्`).
- **PaniniVM Program Script Runner (`.pvm`)**: Evaluate multi-line `.pvm` program script files with turn history and session persistence.
- **CLI Runner**: Command-line execution for `.pvm` scripts, verb derivations, nominal paradigms, and sūtra inspection.

---

## CLI Usage

### Executing `.pvm` Script Files

Execute PaniniVM `.pvm` program script files directly via CLI:

```sh
./gradlew run --args="--eval src/test/kotlin/dev/panini/parser/addition.pvm"
```

Sample `.pvm` Script ([addition.pvm](file:///src/test/kotlin/dev/panini/parser/addition.pvm)):
```text
हे यन्त्र + सुँ, एक + अम् द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।
एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः फल + औट् द्वि + औट् युज् + णिच् + लोट् + सिप् ।
```

Formatted CLI Output:
```text
=== PaniniVM Script Execution: addition.pvm ===
Line 1:
  ✓ Result: षट्
  ↳ Operation: panini.eval
Line 2:
  ✓ Result: पञ्च
  ↳ Operation: panini.eval
```

### Derivations & Sūtra Inspection

```sh
# Nominal paradigms and individual derivations
./gradlew run --args="--paradigm राम"
./gradlew run --args="--derive राम SASTHI BAHUVACANA"
./gradlew run --args="--derive राम षष्ठी बहुवचन"

# Verbal derivations
./gradlew run --args="--verb भू"
./gradlew run --args="--verb भू LING EKAVACANA"
./gradlew run --args="--verb भू LOT बहुवचन"

# Registry inspection and implementation coverage
./gradlew run --args="--coverage"
./gradlew run --args="--sutra 7.1.54"
```

---

## Programmatic API

```kotlin
import dev.panini.execution.PaniniVM
import dev.panini.execution.ExecutionResult
import java.io.File

val vm = PaniniVM()

// Single utterance evaluation
val result = vm.eval("दश + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।")
if (result is ExecutionResult.Success) {
    println(result.value) // Output: द्वादश
}

// Evaluating a .pvm script file with session persistence
val scriptFile = File("src/test/kotlin/dev/panini/parser/addition.pvm")
val results = vm.evalFile(scriptFile, sessionKey = "session_math")
results.forEach { res ->
    if (res is ExecutionResult.Success) {
        println("Result: ${res.value}")
    }
}
```

---

## Test Suite

Run all automated unit and integration tests:

```sh
./gradlew test --no-daemon
```

To print step-by-step derivation traces:

```sh
./gradlew test --tests "dev.panini.ScratchTest.testDerivationTrace" --info
```

---

## Current Ashtadhyayi Coverage

- 303 implemented sūtras out of the 3,959-rule target.
- It-marker processing, grammatical saṃjñās, rule ordering, substitutions, augment insertion, deletion, and selected Tripādī transformations.
- All 21 `sup` forms for masculine a-stems such as `राम` and `देव`.
- All ten lakāras: `LAT`, `LIT`, `LUT`, `LRT`, `LET`, `LOT`, `LANG`, `LING`, `LUNG`, and `LRNG`.
- Parasmaipada, Ātmanepada, and explicit Ubhayapada selection through the verbal API.
- Gaṇa-aware stem derivation for `LAT`, `LOT`, `LANG`, and `LING` across all ten Dhātupāṭha gaṇas.

---

## Text Encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the executable specification, so editors and terminals must preserve UTF-8 when changing Devanagari text.
