# PaniniVM

Kotlin implementation of an executable Pāṇinian derivation system and natural semantic execution engine. Implemented
sūtras carry typed metadata, executable eligibility, and state-transition logic. Derivations retain an ordered rule trace, including conflicts and blocked alternatives where available.

---

## Features

- **Executable Ashtadhyayi Engine**: Registered sūtras across derivation and sentence-analysis scopes.
- **Adhikāra Domain Registry (`AdhikaraRegistry`)**: Enforces governing heading scopes (`1.4.1`, `1.4.23`, `2.3.1 Anabhihite`, `3.1.1`, `3.1.91`, `6.4.1`, `8.1.16`) wrapping top-level concrete `Sutra` objects.
- **Paribhāṣā Meta-rule Registry (`ParibhashaRegistry`)**: Manages interpretive meta-rules (`1.1.3`, `1.1.46`, `1.1.47`, `1.1.49`, `1.1.50`, `1.1.51`, `1.1.52`, `1.1.53`, `1.1.54`, `1.1.55`, `1.1.56`, `1.1.66`, `1.1.67`) with type-safe `ParibhashaScope` enums.
- **Pratiṣedha Prohibition Engine (`NishedhaRuleEngine`)**: Evaluates prohibition sūtras (`1.1.5`, `1.1.6`, `1.1.10`, `1.2.4`) integrated into rule resolution loops.
- **ANTLR4 Segmented Sanskrit Parser (`VyakaranamLexer.g4` + `VyakaranamParser.g4`)**: Parses strictly segmented Pāṇinian words (`Prakṛti + Pratyaya`) through one canonical grammar:
  - Nominal Subanta: `एक + अम्`, `द्वि + औट्`, `त्रि + शस्`, `यन्त्र + सुँ`, `फल + अम्`, `पूर्वफल + अम्`
  - Verbal Tiṅanta: `युज् + णिच् + लोट् + सिप्`, `गण + णिच् + लोट् + सिप्`, `हृ + लोट् + सिप्`
- **Multi-Vākya Sentence Chaining**: Parse and execute multi-clause sentences separated by connectives (`ततः`, `अथ`, `अनन्तरम्`) or daṇḍas (`।`, `॥`).
- **Dynamic Inter-Clause Result References**: Refer to intermediate calculation results (`फल + अम्`) across clauses (`योग-1`, `योग-2`) or session history (`पूर्वफल + अम्`).
- **Kāraka Constraint Analysis**: Preserves syncretic sup endings such as `भ्याम्` and resolves them through executable 1.4 kāraka-saṃjñā and 2.3 vibhakti sūtras (`2.3.26`, `2.3.44`, `2.3.52`).
- **PaniniVM Program Script Runner (`.pvm`)**: Evaluate multi-line `.pvm` program script files with turn history and session persistence.
- **CLI Runner**: Command-line execution for `.pvm` scripts, verb derivations, nominal paradigms, and sūtra inspection.
- **Sanskrit Bytecode Compiler**: Compile multi-clause `.pvm` script files directly into native JVM `.class` bytecode carrying mapped stack constants and resolved operations.


---

## Input boundary

PaniniVM accepts annotated morphological notation, not ordinary surface Sanskrit.
Declinable and conjugable inputs must expose their grammatical components, for
example `एक + अम्` and `युज् + णिच् + लोट् + सिप्`. The execution layer does not
attempt to recover a number, suffix, dhātu, or compound structure by parsing a
finished surface word.

Numeral handling has two distinct directions:

- `VyakaranamExecutionAdapter` binds a canonical numeral prātipadika from the
  parsed AST directly to a typed `SanskritValue.Sankhya`.
- `SankhyaGenerator` derives a Sanskrit cardinal or ordinal form from a numeric
  value and retains its grammatical derivation trace.

Arithmetic actions consume the numeric value carried by `SanskritValue.Sankhya`.
They never convert a Sanskrit output word back into a number. Typed results are
preserved when `फल + ...` refers to an earlier clause.

The execution path has one direction:

```text
segmented input → vyākaraṇa AST → binding → operation resolution → planning → runtime
```

The implementation is organized accordingly:

```text
execution/
  binding/       AST-to-invocation binding
  planning/      operation resolution, ordering, and disposition
  runtime/       execution, continuations, and the typed value environment
  operations/    numeric, linguistic, state, and external meanings
  persistence/   host-provided state storage
  external/      host-provided capability dispatch
```

Dhātupāṭha entries remain linguistic catalogue data. Executable meanings live
only under `execution/operations` and are connected to dhātus by registry ID.

Sentence analysis does not infer a kāraka from one arbitrarily selected sup
slot. For example, `भ्याम्` initially retains तृतीया, चतुर्थी, and पञ्चमी as
possibilities. Dhātu valency and semantic relations select a kāraka through an
Aṣṭādhyāyī 1.4 rule; an applicable 2.3 rule then validates the corresponding
vibhakti. Both sūtras are retained in the analysis and execution trace.

---

## CLI Usage

### Executing `.pvm` Script Files

Execute PaniniVM `.pvm` program script files directly via CLI:

```sh
./gradlew run --args="--eval src/test/kotlin/dev/panini/parser/addition.pvm"
```

### Compiling `.pvm` Script Files to JVM Bytecode

Compile segmented Sanskrit `.pvm` script files into standard JVM class bytecode:

```sh
./gradlew run --args="--compile src/test/kotlin/dev/panini/parser/addition.pvm SanskritAddition"
```

Sample `.pvm` script: [`addition.pvm`](src/test/kotlin/dev/panini/parser/addition.pvm)
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

# Cardinal and ordinal numeral derivations
./gradlew run --args="--sankhya 23"
./gradlew run --args="--sankhya 6 ordinal"
./gradlew run --args="--sankhya 42 cardinal --variants"

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

For direct execution API tests or host-created invocations, construct a numeral
explicitly instead of tagging an untyped string:

```kotlin
val operand = ExecutionExpression.sankhya(5, "पञ्च")
```

`SanskritValue.of("पञ्च")` intentionally creates a `Shabda`; it does not guess
that arbitrary Sanskrit text is numeric.

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

- **430 registered executable sūtras** out of the 3,959-rule target (3,529 remaining).
- **100% Full Coverage of Aṣṭādhyāyī 2.3 Vibhakti Sūtras**: All 64 classical non-Vedic sūtras implemented and verified.
- **100% Full Coverage of Aṣṭādhyāyī 1.4 Kāraka Sūtras**: All 33 classical Kāraka saṃjñā sūtras implemented and verified.
- **100% Full Coverage of Nominal (Subanta) Stem Classes**: All 31 Classical Sanskrit nominal stem categories implemented across 3 genders (masculine, feminine, neuter), pronouns, numerals, and consonant stems.
- **Sanādyanta Secondary Verbal Formations**: Desiderative (*san* 3.1.5, 3.1.7), Intensive (*yaṅ* 3.1.22), Causative (*ṇic* 3.1.26), and Secondary Root saṃjñā (*dhātu* 3.1.32).
- Structural, technical, and interpretative Saṃjñā and Paribhāṣā sūtras across Aṣṭādhyāyī Pādas 1.1, 1.2, 1.3, and 1.4.
- Complete 21-slot `sup` paradigms verified across all major stem classes with 330+ passing automated tests.
- All ten lakāras: `LAT`, `LIT`, `LUT`, `LRT`, `LET`, `LOT`, `LANG`, `LING`, `LUNG`, and `LRNG`.
- Parasmaipada, Ātmanepada, and explicit Ubhayapada selection through the verbal API.
- Gaṇa-aware stem derivation for `LAT`, `LOT`, `LANG`, and `LING` across all ten Dhātupāṭha gaṇas.

---

## Text Encoding

Source, tests, and documentation are UTF-8. Sanskrit literals are part of the executable specification, so editors and terminals must preserve UTF-8 when changing Devanagari text.
